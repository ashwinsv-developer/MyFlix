package com.example.myflix.domain.repository

import com.example.myflix.data.remote.dto.MovieListDto

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): MovieListDto
}