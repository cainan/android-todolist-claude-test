# My Todos

A simple Android to-do list app built with Jetpack Compose, Room, and Hilt — demonstrating a clean single-module MVVM architecture.

## Features

- View all todos in a scrollable list
- Add a new todo via the FAB (auto-navigates to the edit screen)
- Edit title and description with automatic save-on-back
- Toggle done/undone with a checkbox on each item
- Swipe left on any item to delete it

## Tech Stack

| Layer | Library | Version |
|---|---|---|
| Language | Kotlin | 2.2.10 |
| UI | Jetpack Compose (BOM) | 2026.02.01 |
| UI components | Material 3 | BOM-managed |
| Navigation | Navigation Compose | 2.9.0 |
| Database | Room | 2.7.1 |
| DI | Hilt | 2.59.2 |
| Lifecycle | Lifecycle / ViewModel | 2.8.7 |
| Build | AGP | 9.2.1 |
| Annotation processing | KSP | 2.2.10-2.0.2 |

## Architecture

Single-module MVVM with no use-cases layer:

```
data → repository → ViewModel → Compose UI
```

- **Room** emits `Flow<List<TodoEntity>>` from the DAO
- **Repository** wraps the DAO; interface bound via Hilt `@Binds`
- **ViewModels** convert the Flow to `StateFlow` via `stateIn(WhileSubscribed(5_000))`
- **Compose screens** collect with `collectAsStateWithLifecycle()`

### Navigation

Two destinations managed by `NavHost`:

| Route | Screen |
|---|---|
| `todo_list` | List of all todos |
| `todo_detail/{todoId}` | Edit a single todo |

### Save-on-Back

The detail screen holds local `rememberSaveable` state for `title` and `description`. Both the system back gesture (`BackHandler`) and the TopAppBar back arrow call `viewModel.save()` before popping the back stack. The save dispatches on `Dispatchers.IO` inside `viewModelScope`, so it completes even after navigation fires.

## Project Structure

```
app/src/main/java/com/cso/claudetest/
  data/
    model/TodoEntity.kt
    db/TodoDao.kt
    db/TodoDatabase.kt
    repository/TodoRepository.kt
    repository/TodoRepositoryImpl.kt
  di/
    DatabaseModule.kt
    RepositoryModule.kt
  ui/
    navigation/Screen.kt
    navigation/NavGraph.kt
    list/TodoListViewModel.kt
    list/TodoListScreen.kt
    detail/TodoDetailViewModel.kt
    detail/TodoDetailScreen.kt
  TodoApplication.kt
  MainActivity.kt
```

## Requirements

- Android Studio Meerkat or newer
- Android SDK 36.1 (compileSdk)
- Minimum device: Android 8.0 (API 26)
- Java 11 toolchain

## Build & Run

```bash
# Assemble debug APK
./gradlew :app:assembleDebug

# Run KSP annotation processing only (fast first check)
./gradlew :app:kspDebugKotlin

# Run unit tests
./gradlew :app:testDebugUnitTest

# Run instrumented tests (requires connected device/emulator)
./gradlew :app:connectedDebugAndroidTest
```

Install the APK on a connected device or emulator:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```
