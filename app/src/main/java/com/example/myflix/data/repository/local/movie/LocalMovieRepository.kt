package com.example.myflix.data.repository.remote

import com.piashcse.hilt_mvvm_compose_movie.data.model.moviedetail.MovieDetail

interface LocalMovieRepository {
    suspend fun favoriteMovies(): List<MovieDetail?>
    suspend fun removeMovieById(movieId: Int)
}