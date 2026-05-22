package com.example.myflix.data.remote

import com.example.myflix.data.remote.dto.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieListDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}