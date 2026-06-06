package com.myflix.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> AutoPaginationComponent(
    threshold: Int = 3,
    items: LazyPagingItems<T>,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()

        items.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                loadState.refresh is LoadState.Error -> {
                    val error = items.loadState.refresh as LoadState.Error
                    ErrorComponent(
                        message = error.error.localizedMessage ?: "Unknown Error",
                        onRetry = { retry() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                loadState.append is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                    )
                }

                loadState.append is LoadState.Error -> {
                    val error = items.loadState.append as LoadState.Error
                    ErrorComponent(
                        message = error.error.localizedMessage ?: "Unknown Error",
                        onRetry = { retry() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorComponent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, modifier = Modifier.padding(bottom = 8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

