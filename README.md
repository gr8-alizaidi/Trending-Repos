<h1 align="center"> Github Trending Repositories</h1>

<p>  
Android App that lists trending Github repositories
</p>

<p align="center">
<img src="/screenshots/home.png" width="200"/> <img src="/screenshots/repo.png" width="200"/> <img src="/screenshots/saved.png" width="200"/> <img src="/screenshots/error.png" width="200"/>
</p>

## API
I have decided to use [GitHub Search API](https://developer.github.com/v3/search/#search-repositories). Using /repositories endpoint to fetch the data. 

## Tech stack
- Minimum SDK level 23
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous.
- Dagger-Hilt (alpha) for dependency injection.
- JetPack
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Navigation Component - handle everything needed for in-app navigation.
  - Data Binding - declaratively bind observable data to UI elements.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Reposit)ory pattern
- [Room](https://github.com/androidx-releases/Room) - to serve offline purpose
- [Glide](https://github.com/bumptech/glide) - loading images.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like ripple animation, cardView.

<!-- ## Made By [Ali Zaidi](https://www.instagram.com/gr8_alizaidi/) -->
