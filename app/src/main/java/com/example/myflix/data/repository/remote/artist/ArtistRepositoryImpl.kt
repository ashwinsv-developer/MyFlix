package com.example.myflix.data.repository.remote.artist

import com.example.myflix.data.dataSource.remote.ApiService
import com.example.myflix.utils.network.DataState
import com.example.myflix.utils.network.safeApiCall
import com.example.myflix.data.model.artist.ArtistDetail
import com.example.myflix.data.model.artist.ArtistMovies
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ArtistRepository {
    override suspend fun artistAllMovies(movieId: Int): Flow<DataState<ArtistMovies>> =
        safeApiCall { apiService.artistAllMovies(movieId) }
    override suspend fun artistDetail(personId: Int): Flow<DataState<ArtistDetail>> =
        safeApiCall { apiService.artistDetail(personId) }
}