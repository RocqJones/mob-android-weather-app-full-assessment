## Weather App

This project uses modularization and MVVM architecture for scalability and maintainability. Modules include:

- **app**: Entry point, navigation, initialization.
- **core**: Shared utils, constants, base classes.
- **di**: Dependency injection setup (Koin).
- **ui**: Screens, ViewModels, themes (presentation layer).
- **domain**: Business logic, use cases, domain models.
- **data**: Repositories, data sources, API (Retrofit), offline support (Room Db).