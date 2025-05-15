# 🍽️ Food Review App

This Android application showcases food reviews fetched from a remote API. Built using modern Android development best practices, the app emphasizes clean architecture, scalability, and maintainability.

---

## 🧱 Architecture & Tech Stack

- **Architecture**: MVI (Model–View–Intent) with Clean Code principles
- **State Management**: [Kotlin Flows](https://kotlinlang.org/docs/flow.html) for reactive data handling
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/)
- **Asynchronous Programming**: [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines)
- **Testing**: Unit tests using [JUnit](https://junit.org/) and [MockK](https://mockk.io/)

---

## 🚀 Features

- 📦 **Food Reviews List**  
  Fetches and displays a list of food reviews from the API.

- 🔽 **Category Dropdown Filter**  
  Select a category from a dropdown to filter food reviews. The selected category is passed to the API via the `categories` parameter.

- 🔢 **Range Filter**  
  Use **Min Range** and **Max Range** buttons to filter results based on `min_range` and `max_range` parameters.

- 🔍 **Detail View Navigation**  
  Tap on any food review to navigate to a detailed view with more information.

- ▶️ **YouTube Redirection**  
  From the detail page, click the play button to open the corresponding video in the YouTube app or browser.

---

## ✅ Testing

- Written unit tests with **JUnit** and **MockK**
- ViewModel and business logic are covered
- Coroutines and API responses are tested with proper mocking

---

##Attaching coverage report

<img width="1433" alt="CoverageReport" src="https://github.com/user-attachments/assets/d94ee2ca-bf3d-4b19-baf8-9ecd8cdbdd78" />
