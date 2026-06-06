package com.myflix.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myflix.data.model.moviedetail.MovieDetail
import com.myflix.data.repository.remote.movie.MovieRepository
import com.myflix.utils.network.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<DataState<MovieDetail>>(DataState.Loading)
    val movieDetail: StateFlow<DataState<MovieDetail>> = _movieDetail.asStateFlow()

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            repository.movieDetail(movieId).collect {
                _movieDetail.value = it
            }
        }
    }
}
