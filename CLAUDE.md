# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What — Weekly Weather

Multi-module Android app (Kotlin, Jetpack Compose) showing weather forecasts via the Open-Meteo API. App ID: `com.arklan.weather`, base package: `com.codeskraps`. Licensed MIT.

**Modules:** `app` (entry point, nav host) | `feature:common` (MVI base, nav routes) | `feature:weather` | `feature:maps` | `feature:settings` | `core:local` (Room DB) | `core:location` (GPS) | `core:umami` (analytics)

**Stack:** Compose + Material3, Koin DI, Retrofit + Moshi, Room, Navigation Compose, Coroutines/Flow

## Why — Architecture Decisions

MVI with Clean Architecture. Each feature module has `presentation/mvi/` (State, Event, Action), `domain/` (repository interfaces, models), and `data/` (implementations, DTOs, mappers). ViewModels extend `StateReducerViewModel<STATE, EVENT, ACTION>` from `feature:common` — state transitions are pure functions in `reduceState()`, side effects go through `actionChannel`. UI collects via `collectAsStateWithLifecycle()`.

Koin modules live in each module's `di/` package, all registered in `WeatherApp.kt`. ViewModels injected via `koinViewModel()`.

## How — Build & Verify

```bash
./gradlew assembleDebug                              # debug build
./gradlew assembleRelease                            # release build (R8 minified)
./gradlew testDebugUnitTest                          # all unit tests
./gradlew :feature:weather:testDebugUnitTest         # single module tests
```

SDK config: `buildSrc/.../ConfigData.kt`. Deps: `gradle/libs.versions.toml`. Maps API key: `secrets.properties`.

## Detailed Docs

For deeper context, read these as needed:

- `docs/architecture.md` — MVI pattern, data flow, module boundaries
- `docs/build-and-test.md` — build variants, signing, ProGuard, testing
- `docs/adding-a-feature.md` — step-by-step guide for new feature modules
