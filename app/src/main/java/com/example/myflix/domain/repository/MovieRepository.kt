package com.example.myflix.domain.repository

import androidx.paging.PagingData
import com.example.myflix.data.remote.dto.MovieDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(): Flow<PagingData<MovieDto>>
}