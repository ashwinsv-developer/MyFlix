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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.myflix.navigation.AppNavHost
import com.myflix.navigation.BottomBar
import com.myflix.navigation.Routes
import com.myflix.ui.theme.MyFlixTheme
import com.myflix.utils.networkconnection.ConnectionState
import com.myflix.utils.networkconnection.connectivityState
import dagger.hilt.android.AndroidEntryPoint
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
                var previousConnectionState by remember { mutableStateOf<ConnectionState?>(null) }
                val noInternetMessage = stringResource(R.string.no_internet_connection)
                val backOnlineMessage = stringResource(R.string.back_online)

                // This block reacts to the connection state
                LaunchedEffect(connectionState) {
                    when (connectionState) {
                        is ConnectionState.Unavailable -> {
                            snackbarHostState.showSnackbar(
                                message = noInternetMessage,
                                duration = SnackbarDuration.Indefinite // Keep it visible until online
                            )
                        }
                        is ConnectionState.Available -> {
                            if (previousConnectionState is ConnectionState.Unavailable) {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = backOnlineMessage,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                    previousConnectionState = connectionState
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                val title = when {
                                    currentDestination?.route == Routes.Home.route -> "Popular Movies"
                                    currentDestination?.route == Routes.Profile.route -> "Popular Celebrities"
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
                    },
                    bottomBar = {
                        // Only show bottom bar on top-level destinations
                        val showBottomBar = currentDestination?.route in listOf(Routes.Home.route, Routes.Profile.route)
                        if (showBottomBar) {
                            BottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier.padding(innerPadding)
                    )
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
