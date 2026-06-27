package com.myflix.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.myflix.Details
import com.myflix.ui.details.DetailsScreen
import com.myflix.ui.screens.PopularCelebrity.PoupularMoviesScreen as PopularCelebrityScreen
import com.myflix.ui.screens.PopularMovie.PopularMoviesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Celebrity.route,
        modifier = modifier
    ) {
        // Home Screen (Popular Movies)
        composable(Routes.Celebrity.route) {
            PopularMoviesScreen(
                snackbarHostState = snackbarHostState,
                onNavigateToDetails = { id ->
                    navController.navigate(Details(itemId = id))
                }
            )
        }

        // Profile Screen (Popular Celebrities)
        composable(Routes.Movie.route) {
            PopularCelebrityScreen(navController = navController)
        }

        // Movie Details Screen
        composable<Details> { backStackEntry ->
            val details: Details = backStackEntry.toRoute()
            DetailsScreen(
                itemId = details.itemId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
