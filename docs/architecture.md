# Architecture

## MVI Pattern

Every feature uses `StateReducerViewModel<STATE, EVENT, ACTION>` from `feature:common/mvi/StateReducerFlow.kt`:

- **State** — immutable data class with a `companion object { val initial }` factory. Represents the full UI state.
- **Event** — sealed interface. User actions and internal signals (e.g., `LoadWeatherInfo`, `Refresh`, `Error`).
- **Action** — sealed interface. One-shot side effects consumed by the UI (e.g., `Toast`).

The ViewModel's `reduceState(currentState, event) -> STATE` is a pure function. For async work, launch a coroutine inside the `when` branch, return the intermediate state immediately (e.g., `isLoading = true`), then call `state.handleEvent(...)` when the result arrives.

Side effects use `actionChannel` (a Kotlin `Channel`) exposed as `action: Flow`. UI collects with `LaunchedEffect`.

```
UI ──event──▶ ViewModel.reduceState() ──▶ new State ──▶ UI recomposes
                    │
                    └── viewModelScope.launch { ... actionChannel.send(Action) }
                                                          │
                                                          ▼
                                                    UI LaunchedEffect
```

## Data Flow

```
Retrofit API  ──DTO──▶  mapper functions  ──▶  domain model  ──▶  Resource<T>  ──▶  ViewModel state
```

- **DTOs** live in `data/remote/` (annotated with `@Json` for Moshi).
- **Mappers** are extension functions in `data/mappers/`.
- **Domain models** live in `domain/model/`.
- `Resource<T>` is a sealed interface (`Success` / `Error`) in `feature:common/util/`.

## Module Dependency Graph

```
app
 ├── feature:weather   ─┐
 ├── feature:maps       ├──▶ feature:common
 ├── feature:settings  ─┘        │
 │                                ▼
 ├── core:local    (Room DB, LocalResourceRepository)
 ├── core:location (FusedLocationProviderClient wrapper)
 └── core:umami    (analytics, self-contained)
```

Feature modules depend on `feature:common` and relevant `core:*` modules. Feature modules never depend on each other — they communicate through navigation arguments.

## Navigation

Routes defined as `sealed class Screen` in `feature:common/.../navigation/Screen.kt`:

- `Weather` — `weather` (start destination, shows forecast for active location)
- `Map` — `map` (Google Maps with search, radar overlay, and location picking)
- `Settings` — `settings` (unit system and theme preferences)

`WeatherNavHost` in the `app` module hosts the `NavHost`. Each composable screen receives `state`, `handleEvent`, `action`, and navigation callbacks. Screens never hold a direct reference to `NavController`.

## Koin DI

Each module declares a `val xxxModule = module { ... }` in its `di/` package. All registered in `WeatherApp.kt`:

```kotlin
modules(commonModule, localModule, locationModule, weatherModule, mapsFeatureModule, settingsModule, umamiModule)
```

Pattern inside a module:
1. `single { }` for API clients (Retrofit) and repository implementations
2. `viewModel { }` for ViewModels with constructor injection
3. Interfaces bound via `single<Interface> { Impl(...) }`

ViewModels obtained in Composables via `koinViewModel<T>()`.
