# GitHub Copilot Instructions for the TideRunner Project

## 1. Project Overview: TideRunner 🎣

**Your primary goal is to maintain and enhance TideRunner, an AI-powered fishing forecast companion for Android.**

### Core Purpose:
TideRunner analyzes real-time marine and astronomical data to calculate a personalized **"Fishing Suitability Score" (0-100)**. This score helps anglers determine the best times and places to catch specific fish species.

### Key Features to Respect:
- **Smart Species Targeting:** The app is built around selecting a target fish (from 16+ pre-configured species) and analyzing conditions based on that species' unique preferences.
- **Real-Time Marine Intelligence:** The app aggregates data from multiple free APIs (e.g., Open-Meteo, NOAA, Solunar) to get a comprehensive view of water temperature, wave height, tides, wind, and moon phases.
- **Fishing Suitability Score:** This is the core output. It's a 0-100 rating that must be calculated based on a weighted algorithm of the marine data.
- **100% Free to Use:** All core features must remain free. Only use APIs that offer a robust free tier without requiring user-facing API keys. OpenStreetMap is the mandated mapping provider.
- **Interactive Map:** The map is the primary user interface. Users tap a location to get the fishing forecast for that spot.

## 2. Core Technologies & Architecture

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** Hilt
- **Networking:** Retrofit
- **Database:** Room for caching
- **Asynchronous:** Kotlin Coroutines & Flow
- **Testing:** JUnit 5, MockK, Truth, Turbine, Robolectric

## 3. MANDATORY Development Workflow

**You MUST follow this strict, test-driven workflow for EVERY request. Do not deviate.**

### **Step 1: ALWAYS Run All Tests First**
Before writing or modifying any code, you **MUST** run the complete test suite to establish a baseline.
```bash
./gradlew clean testDebugUnitTest
```
If any tests are failing, your first priority is to fix them before proceeding with the user's request.

### **Step 2: Implement Changes Following Leading Practices**
When you add or modify code, you **MUST** adhere to the following quality standards:
- **MVVM Adherence:**
    - UI logic belongs in Composable functions.
    - State management and business logic belong in ViewModels.
    - Data fetching and manipulation belong in Repositories.
- **Clean Code:** Write simple, readable, and maintainable Kotlin code. Use meaningful names and keep functions small.
- **Immutability:** Prefer `val` over `var` and use immutable data structures where possible.
- **Dependency Injection:** Use Hilt to provide dependencies. Do not manually instantiate classes like ViewModels or Repositories.
- **Error Handling:** Implement robust error handling, especially for network calls and data parsing. Use sealed classes or result wrappers to represent UI states (e.g., `Loading`, `Success`, `Error`).

### **Step 3: Update and Create Test Cases**
For any new feature or bug fix, you **MUST** create or update tests to cover the changes.
- **New Features:** Require new unit tests. If the feature involves UI, new UI tests may also be required.
- **Bug Fixes:** Require a new test that specifically reproduces the bug and then passes once the fix is applied.
- **Test Coverage:** Aim for 100% coverage on all new business logic (ViewModels, UseCases, Repositories).

### **Step 4: Run the Linter and Remediate ALL Findings**
After implementing changes, you **MUST** run Android Lint and fix every reported issue before proceeding. This mirrors the pre-commit lint check that runs on every commit attempt.
```bash
./gradlew lintDebug
```
- The lint report is generated at `app/build/reports/lint-results-debug.html` and `lint-results-debug.xml`.
- **ALL findings must be remediated** — errors AND warnings. Do not suppress a lint rule unless there is a documented, justified reason.
- If a finding is a false positive or intentionally acceptable, suppress it **inline** with a `@SuppressLint` annotation or a `tools:ignore` attribute, and add a comment explaining why.
- Do **not** rely on `abortOnError = false` to hide failures — that setting exists for CI pipeline flexibility only, not as a workaround for unresolved issues.
- Re-run `./gradlew lintDebug` after fixes to confirm zero findings remain before moving on.

### **Step 5: ALWAYS Run All Tests Again**
After implementing changes, adding new tests, and clearing all lint findings, you **MUST** run the entire test suite again to ensure nothing has been broken.
```bash
./gradlew clean testDebugUnitTest
```
**All tests MUST pass AND lint MUST be clean before you consider your work complete.**

## 4. Strict File Management Rules

- **NO SINGLE-USE OR TEMPORARY FILES:** Do not create temporary `.md`, `.txt`, `.sh`, or any other files for one-time explanations or scripts. All operations must be self-contained within your responses or by modifying existing project files.
- **ONE README.MD ONLY:** The root `README.md` is the **single source of truth** for project documentation. Do not create any other documentation files. If the project's description needs updating, modify the `README.md` directly.

By adhering to these instructions, you will ensure the continued quality, stability, and maintainability of the TideRunner project.
