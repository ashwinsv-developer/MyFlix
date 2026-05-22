package com.example.myflix.data.repository

import com.example.myflix.data.remote.MovieApi
import com.example.myflix.data.remote.dto.MovieListDto
import com.example.myflix.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): MovieListDto {
        return api.getPopularMovies(apiKey = "YOUR_API_KEY", page = page)
    }
}