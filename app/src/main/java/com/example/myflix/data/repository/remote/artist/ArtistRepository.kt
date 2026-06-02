package com.example.myflix.data.repository.remote.artist

import com.example.myflix.utils.network.DataState
import com.example.myflix.data.model.artist.ArtistDetail
import com.example.myflix.data.model.artist.ArtistMovies
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    suspend fun artistAllMovies(movieId: Int): Flow<DataState<ArtistMovies>>
    suspend fun artistDetail(personId: Int): Flow<DataState<ArtistDetail>>
}