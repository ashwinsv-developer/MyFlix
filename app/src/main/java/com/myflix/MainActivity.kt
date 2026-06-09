package com.myflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.myflix.ui.component.MoviesGrid
import com.myflix.ui.details.DetailsScreen
import com.myflix.ui.home.HomeViewModel
import com.myflix.ui.screens.PopularMovie.PopularMoviesScreen
import com.myflix.ui.theme.MyFlixTheme
import com.myflix.utils.networkconnection.ConnectionState
import com.myflix.utils.networkconnection.connectivityState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Details(val itemId: String)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFlixTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val snackbarHostState = remember { SnackbarHostState() }
                val connectionState by connectivityState()
                val noInternetMessage = stringResource(R.string.no_internet_connection)

                // This block reacts to the connection state
                LaunchedEffect(connectionState) {
                    if (connectionState is ConnectionState.Unavailable) {
                        snackbarHostState.showSnackbar(
                            message = noInternetMessage,
                            duration = SnackbarDuration.Indefinite // Keep it visible until online
                        )
                    } else {
                        // Dismiss if connection returns
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                val title = when {
                                    currentDestination?.route?.contains("Home") == true -> "Popular Movies"
                                    currentDestination?.route?.contains("Details") == true -> "Movie Details"
                                    else -> "MyFlix"
                                }
                                Text(text = title)
                            },
                            navigationIcon = {
                                if (navController.previousBackStackEntry != null) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Home,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Home> {
                            PopularMoviesScreen(
                                snackbarHostState = snackbarHostState,
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
