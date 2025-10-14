## Weather App

This project uses modularization and MVVM architecture for scalability and maintainability.

### Architecture Overview

**Modules:**

- **app**: Entry point, navigation, initialization, Koin setup.
- **core**: Shared utilities, constants, base classes.
- **di**: Dependency injection modules (Koin configuration for Retrofit, Room, Repositories).
- **ui**: Screens, ViewModels, themes (MVVM presentation layer).
- **domain**: Business logic, use cases, domain models.
- **data**: Repositories, data sources, API (Retrofit), offline support (Room DB).

### Tech Stack

**Networking:**
- Retrofit 2.9.0 - REST API client
- OkHttp - HTTP client with logging interceptor
- Gson - JSON serialization/deserialization

**Local Database:**
- Room 2.6.1 - Offline data persistence
- SQLite - Local storage

**Dependency Injection:**
- Koin 3.5.3 - Lightweight DI framework

**Async Operations:**
- Kotlin Coroutines - Asynchronous programming
- Flow - Reactive data streams

**Code Quality:**
- KtLint - Code formatting and linting

**UI Framework:**
- Jetpack Compose - Modern declarative UI
- Material3 - Material Design components

### API Integration

**OpenWeatherMap API:**
- Current Weather: `/data/2.5/weather`
- 5-Day Forecast: `/data/2.5/forecast`
- API Key: Securely stored in `local.properties`

### Data Flow

1. **Remote → Local**: API data fetched via Retrofit, mapped to DTOs, converted to Room entities, and cached locally
2. **Local → UI**: Room database emits Flow, observed by ViewModels, and rendered in Compose UI
3. **Offline-First**: App displays cached data when offline, refreshes when online

### Module Dependencies

```
app
 ├─ data
 ├─ domain
 ├─ di → data
 ├─ ui
 └─ core

data
 ├─ Retrofit (API)
 ├─ Room (Database)
 └─ Koin (DI)
```

### Key Features

✅ Modular architecture for scalability  
✅ MVVM pattern with clear separation of concerns  
✅ Offline-first with Room database caching  
✅ Reactive UI with Kotlin Flow  
✅ Dependency injection with Koin  
✅ RESTful API integration with Retrofit  
✅ Null-safe data models  
✅ Code quality enforcement with KtLint  

### Security

- API keys stored in `local.properties` (not version controlled)
- BuildConfig integration for secure key access
- Internet and network state permissions configured

### Development Setup

1. Clone the repository
2. Add `WEATHER_API_KEY=your_api_key_here` to `local.properties`
3. Sync Gradle dependencies
4. Run the app

### Code Quality

Run code formatting:
```bash
./gradlew ktlintFormat
```

Check code style:
```bash
./gradlew ktlintCheck
```