# Adding a Feature Module

Step-by-step guide for adding a new feature to this project.

## 1. Create the module

Add to `settings.gradle.kts`:
```kotlin
include(":feature:yourfeature")
```

Create `feature/yourfeature/build.gradle.kts` following the same pattern as `feature/weather/build.gradle.kts`:
- Apply `android.library`, `kotlin.android`, `ksp`, and `compose.compiler` plugins
- Set `namespace = "com.codeskraps.feature.yourfeature"`
- Use `ConfigData` for SDK versions
- Depend on `:feature:common` and any needed `core:*` modules

## 2. Define the MVI triplet

Create three files in `presentation/mvi/`:

**State** — immutable data class with `companion object { val initial }`:
```kotlin
data class YourState(
    val isLoading: Boolean,
    val error: String?,
    val data: YourData?
) {
    companion object {
        val initial = YourState(isLoading = true, error = null, data = null)
    }
}
```

**Event** — sealed interface for all inputs:
```kotlin
sealed interface YourEvent {
    data object Load : YourEvent
    data object Refresh : YourEvent
    data class Error(val message: String) : YourEvent
}
```

**Action** — sealed interface for one-shot side effects:
```kotlin
sealed interface YourAction {
    data class Toast(val message: String) : YourAction
}
```

## 3. Create the ViewModel

Extend `StateReducerViewModel<YourState, YourEvent, YourAction>`:

```kotlin
class YourViewModel(
    private val repository: YourRepository,
    private val dispatcherProvider: DispatcherProvider
) : StateReducerViewModel<YourState, YourEvent, YourAction>(YourState.initial) {

    override fun reduceState(currentState: YourState, event: YourEvent): YourState {
        return when (event) {
            is YourEvent.Load -> onLoad(currentState)
            // ...
        }
    }
}
```

Key pattern: `reduceState` returns the new state synchronously. For async work, launch a coroutine inside the branch, return an intermediate state (e.g., `isLoading = true`), then call `state.handleEvent(...)` when the result arrives.

## 4. Create the Composable screen

```kotlin
@Composable
fun YourScreen(
    state: YourState,
    handleEvent: (YourEvent) -> Unit,
    action: Flow<YourAction>,
    navRoute: (String) -> Unit
) { ... }
```

Screens receive state and callbacks — never hold a `NavController` reference.

## 5. Register DI

Create `di/FeatureModule.kt`:
```kotlin
val yourModule = module {
    single<YourRepository> { YourRepositoryImpl(api = get()) }
    viewModel { YourViewModel(repository = get(), dispatcherProvider = get()) }
}
```

Add `yourModule` to the `modules(...)` list in `app/.../WeatherApp.kt`.

## 6. Add navigation

Add a route to `Screen` sealed class in `feature:common/.../navigation/Screen.kt`:
```kotlin
data object YourScreen : Screen("yourscreen")
```

Add a `composable(...)` block in `WeatherNavHost.kt`'s `NavHost`, following the existing pattern:
- Get ViewModel via `koinViewModel<YourViewModel>()`
- Collect state via `collectAsStateWithLifecycle()`
- Pass `state`, `handleEvent`, `action`, and nav callbacks to your Composable

## 7. Wire up app module

In `app/build.gradle.kts`, add:
```kotlin
implementation(project(mapOf("path" to ":feature:yourfeature")))
```

If using R8/ProGuard, add keep rules in `app/proguard-rules.pro`:
```
-keep class com.codeskraps.feature.yourfeature.** { *; }
```
