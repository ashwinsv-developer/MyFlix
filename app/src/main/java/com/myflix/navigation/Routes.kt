package com.myflix.navigation

sealed class Routes(val route: String) {
    object Celebrity : Routes("Celebrity")
    object Movie : Routes("movie")
    object  MovieDetail : Routes("MovieDetail")
    object CelebrityDetail : Routes("CelebrityDetails ")


}