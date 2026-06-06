package com.myflix.ui.screens.PopularMovie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.myflix.ui.home.HomeViewModel



@Composable
fun PopularMoviesScreen(
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
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(
                count = movies.itemCount,
                key = movies.itemKey { it.id },
                contentType = movies.itemContentType { "movies" }
            ) { index ->
                val movie = movies[index]
                movie?.let {
                    Text(
                        text = it.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clickable { onNavigateToDetails(it.id.toString()) }
                    )
                }
            }

            when (val state = movies.loadState.append) {
                is LoadState.Error -> {
                    item { Text("Error loading more...") }
                }
                is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                else -> {}
            }
        }

        if (movies.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        Button(
            onClick = { movies.refresh() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Refresh")
        }
    }
}
