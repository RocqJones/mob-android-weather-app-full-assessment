## Weather App

This project uses modularization and MVVM architecture for scalability and maintainability.

### Architecture Overview

**Modules:**

- **app**: Entry point, navigation, initialization, Koin setup.
- **core**: Shared utilities, constants, base classes, network connectivity service.
- **di**: Dependency injection modules (Koin configuration for Retrofit, Room, Repositories).
- **ui**: Screens, ViewModels, themes (MVVM presentation layer).
- **domain**: Business logic, use cases, domain models.
- **data**: Repositories, data sources, API (Retrofit), offline support (Room DB), sync service.

### Tech Stack

**Networking:**
- Retrofit - REST API client
- OkHttp - HTTP client with logging interceptor
- Gson - JSON serialization/deserialization

**Local Database:**
- Room - Offline data persistence

**Dependency Injection:**
- Koin - Lightweight DI framework

**Async Operations:**
- Kotlin Coroutines - Asynchronous programming
- Flow - Reactive data streams

**Code Quality:**
- KtLint - Code formatting and linting
- KSP - Kotlin Symbol Processing

**UI Framework:**
- Jetpack Compose - Modern declarative UI
- Material3 - Material Design components

### API Integration

**OpenWeatherMap API:**
- Current Weather: `/data/2.5/weather`
- 5-Day Forecast: `/data/2.5/forecast`
- API Key: Securely stored in `local.properties`

### Offline-First Architecture

The app implements a robust offline-first strategy:

**Data Flow:**
1. **UI Request** → ViewModel calls Use Case
2. **Immediate Response** → Cached data from Room DB displayed instantly
3. **Background Sync** → API refresh triggered if network available
4. **Auto-Update** → Flow emits new data, UI updates automatically

**Network Monitoring:**
- `NetworkConnectivityService` monitors real-time network status
- `WeatherSyncService` triggers automatic sync when internet becomes available
- Repository checks network before API calls, gracefully handles offline scenarios

**Benefits:**
- ✅ Instant UI updates from local cache
- ✅ Works seamlessly offline
- ✅ Automatic data refresh when online
- ✅ No crashes due to network failures
- ✅ Smooth user experience regardless of connectivity

### Module Dependencies

```
app
 ├─ data
 ├─ domain
 ├─ di → core, data
 ├─ ui
 └─ core

data → core
 ├─ Retrofit (API)
 ├─ Room (Database)
 └─ Network Service
```

### Key Features

✅ Modular architecture for scalability  
✅ MVVM pattern with clear separation of concerns  
✅ Offline-first with Room database caching  
✅ Real-time network monitoring and auto-sync  
✅ Reactive UI with Kotlin Flow  
✅ Dependency injection with Koin  
✅ RESTful API integration with Retrofit  
✅ Null-safe data models  
✅ Code quality enforcement with KtLint  
✅ Error handling with Result sealed class  

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