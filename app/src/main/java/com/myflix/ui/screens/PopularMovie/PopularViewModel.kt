package com.myflix.ui.screens.PopularMovie

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myflix.data.model.MovieItem
import com.myflix.data.repository.remote.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    val movies: Flow<PagingData<MovieItem>> = repository.popularMoviePagingDataSource(null)
        .cachedIn(viewModelScope)
}

