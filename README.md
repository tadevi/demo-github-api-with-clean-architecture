# Android - Demo Clean Architecture With Github API

## Introduction
This is a sample app for an Android application using Uncle Bob's Clean Architecture approach. It demonstrates the application of clean architecture principles to create a scalable and maintainable codebase.

## Module Architecture

### Domain
- **Business Logic**: Contains the core business logic of the application. This module is independent of any other layers. It includes:
  - **Entities**: Core data structures representing the business models.
  - **Use Cases**: Interactors that encapsulate the application's use cases, implementing the business logic.
  - **Repositories**: Interfaces defining the methods for data access, ensuring loose coupling between the Domain layer and the data implementation.

### Data
- **Remote Data Source**: Using Retrofit & Gson for network api.
- **Local Data Source**: Using Room database to cache local data.
- **Repository Implementations**: Concrete implementations of the Repository interfaces defined in the Domain module, managing data from various sources.

### App
- **MVVM Architecture**: Implements the Model-View-ViewModel pattern to manage UI-related data in a lifecycle-conscious way.
- **Reactive Streams**: Using Kotlin Mutable StateFlow.
- **Paging 3**: Integrates with the Paging library for loading data in chunks, ensuring smooth scrolling and efficient data handling.
- **Navigation Component**: Utilizes Jetpack's Navigation Component to handle in-app navigation in a type-safe and modular way.
- **Single Activity Multiple Fragments Architecture**

## Features
- **Dependency Injection**: Uses Koin for dependency management, providing an easy and efficient way to handle dependencies.
- **Unit Testing**: Ensures robust and reliable code through comprehensive unit tests for ViewModels, Use Cases, and Repositories.


