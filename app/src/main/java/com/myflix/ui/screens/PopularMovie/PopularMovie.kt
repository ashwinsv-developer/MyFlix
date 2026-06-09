package com.myflix.ui.screens.PopularMovie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.myflix.ui.component.MoviesGrid
import com.myflix.ui.home.HomeViewModel
import com.myflix.utils.network.isConnectedToInternet
import kotlinx.coroutines.launch

@Composable
fun PopularMoviesScreen(
    onNavigateToDetails: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val noInternetMessage = LocalContext.current.getString(com.myflix.R.string.no_internet_connection)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MoviesGrid(
            moviesItems = movies,
            onClick = { movie ->
                onNavigateToDetails(movie.id.toString())
            }
        )

        if (movies.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = {
                isConnectedToInternet(
                    context = context,
                    onNoInternet = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = noInternetMessage,
                                actionLabel = "OK"
                            )
                        }
                    }
                ) {
                    movies.refresh()
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Refresh")
        }
    }
}
