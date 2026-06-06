package com.myflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.myflix.ui.component.MoviesGrid
import com.myflix.ui.details.DetailsScreen
import com.myflix.ui.home.HomeViewModel
import com.myflix.ui.theme.MyFlixTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Details(val itemId: String)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFlixTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Home> {
                            HomeScreen(
                                onNavigateToDetails = { id ->
                                    navController.navigate(Details(itemId = id))
                                }
                            )
                        }
                        composable<Details> { backStackEntry ->
                            val details: Details = backStackEntry.toRoute()
                            DetailsScreen(
                                itemId = details.itemId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onNavigateToDetails: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Popular Movies",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        MoviesGrid(
            moviesItems = movies,
            onClick = { movie ->
                onNavigateToDetails(movie.id.toString())
            }
        )

        if (movies.loadState.refresh is LoadState.Loading || movies.loadState.append is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        if (movies.loadState.refresh is LoadState.Error || movies.loadState.append is LoadState.Error) {
            val error = (movies.loadState.refresh as? LoadState.Error) ?: (movies.loadState.append as? LoadState.Error)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Error: ${error?.error?.localizedMessage}")
                Button(onClick = { movies.retry() }) {
                    Text("Retry")
                }
            }
        }
    }
}

