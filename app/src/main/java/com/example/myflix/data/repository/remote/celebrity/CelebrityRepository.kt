package com.example.myflix.data.repository.remote.celebrity

import androidx.paging.PagingData
import com.example.myflix.utils.network.DataState
import com.example.myflix.data.model.SearchBaseModel
import com.example.myflix.data.model.celebrities.Celebrity
import kotlinx.coroutines.flow.Flow

interface CelebrityRepository {
    fun popularCelebrities(page: Int): Flow<PagingData<Celebrity>>
    fun trendingCelebrities(page: Int): Flow<PagingData<Celebrity>>

    suspend fun searchCelebrity(searchKey: String): Flow<DataState<SearchBaseModel>>
}