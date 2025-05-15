# Food Review App

This Android application showcases food reviews fetched from a remote API. Built using modern Android development best practices, the app emphasizes clean architecture, scalability, and maintainability.

## Architecture & Tech Stack

- Architecture: MVI (Model–View–Intent) with Clean Code principles
- State Management: Kotlin Flows for reactive data handling
- Dependency Injection: Hilt
- Networking: Retrofit + OkHttp
- Asynchronous Programming: Kotlin Coroutines
- Unit Testing: JUnit and MockK
- UI Testing: Espresso and Jetpack Compose Test Rules

## Features

- Food Reviews List  
  Fetches and displays a list of food reviews from the API.

- Category Dropdown Filter  
  Select a category from a dropdown to filter food reviews. The selected category is passed to the API via the `categories` parameter.

- Range Filter  
  Use Min Range and Max Range buttons to filter results based on `min_range` and `max_range` parameters.

- Detail View Navigation  
  Tap on any food review to navigate to a detailed view with more information.

- YouTube Redirection  
  From the detail page, click the play button to open the corresponding video in the YouTube app or browser.

## Testing

- Unit tests using JUnit and MockK for ViewModel and business logic
- Coroutine and network layer testing with mocking
- UI testing using Espresso for View-based components
- Jetpack Compose testing using Compose Test Rules

## Attaching coverage report

<img width="1433" alt="CoverageReport" src="https://github.com/user-attachments/assets/d94ee2ca-bf3d-4b19-baf8-9ecd8cdbdd78" />
