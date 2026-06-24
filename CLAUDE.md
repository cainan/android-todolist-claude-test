# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Assemble debug APK
./gradlew :app:assembleDebug

# Run KSP annotation processing only (Room + Hilt codegen — fast first check)
./gradlew :app:kspDebugKotlin

# Run unit tests
./gradlew :app:testDebugUnitTest

# Run instrumented tests (requires connected device/emulator)
./gradlew :app:connectedDebugAndroidTest

# Run a single unit test class
./gradlew :app:testDebugUnitTest --tests "com.cso.claudetest.ExampleUnitTest"
```

## Architecture

Single-module MVVM app: `data` → `repository` → `ViewModel` → Compose UI. No use cases layer.

```
app/src/main/java/com/cso/claudetest/
  data/
    model/TodoEntity.kt          # Room @Entity
    db/TodoDao.kt                # Flow-based DAO
    db/TodoDatabase.kt           # RoomDatabase singleton
    repository/TodoRepository.kt         # interface
    repository/TodoRepositoryImpl.kt     # @Inject constructor(dao)
  di/
    DatabaseModule.kt    # @Provides DB + DAO (object)
    RepositoryModule.kt  # @Binds interface→impl (abstract class)
  ui/
    navigation/Screen.kt         # sealed class with route strings
    navigation/NavGraph.kt       # NavHost wiring
    list/TodoListViewModel.kt
    list/TodoListScreen.kt
    detail/TodoDetailViewModel.kt
    detail/TodoDetailScreen.kt
  TodoApplication.kt   # @HiltAndroidApp
  MainActivity.kt      # @AndroidEntryPoint, hosts NavGraph
```

**Data flow:** Room emits `Flow<List<TodoEntity>>` → DAO → Repository → ViewModel converts to `StateFlow` via `stateIn(WhileSubscribed(5_000))` → Compose collects with `collectAsStateWithLifecycle()`.

**Navigation:** Two destinations: `todo_list` and `todo_detail/{todoId}` (Long). FAB inserts a blank `TodoEntity`, receives the auto-generated `id`, and navigates to the detail screen with that id. `SavedStateHandle` in `TodoDetailViewModel` reads `todoId` directly.

**Save-on-back pattern:** `TodoDetailScreen` holds `title` and `description` in `rememberSaveable` (keyed on `todo?.id`). Both `BackHandler` and the TopAppBar back arrow call `viewModel.save(title, description)` before `onBack()`. The save runs on `viewModelScope` + `Dispatchers.IO` so it completes even after `popBackStack()` fires.

## Key Tooling Constraints

- **compileSdk 36.1** (AGP 9.x DSL: `version = release(36) { minorApiLevel = 1 }`). SDK 37 is not installed — do not bump library versions that declare `minCompileSdk = 37`. Currently pinned: `core-ktx = "1.16.0"`, `lifecycle = "2.8.7"`.
- **KSP, not kapt.** All annotation processors (`room-compiler`, `hilt-android-compiler`) use `ksp(...)`. KSP version must match the `kotlinVersion` prefix: currently `2.2.10-2.0.2`.
- **`android.disallowKotlinSourceSets=false`** in `gradle.properties` — required for KSP 2.x + AGP 9.x to register generated source directories.
- **`kotlin-android` plugin must NOT be applied.** AGP 9.x registers the Kotlin Android extension internally; applying it again causes "extension already registered" error.
- **Hilt 2.59.2+** required for AGP 9.x compatibility (`BaseExtension` was removed in AGP 9).
- All Compose artifact versions are managed by the BOM (`composeBom = "2026.02.01"`). Lifecycle artifacts are NOT managed by the BOM and must be versioned explicitly.
