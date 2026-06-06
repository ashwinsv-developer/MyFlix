package com.myflix.data.repository.remote.celebrity

import androidx.paging.PagingData
import com.myflix.utils.network.DataState
import com.myflix.data.model.SearchBaseModel
import com.myflix.data.model.celebrities.Celebrity
import kotlinx.coroutines.flow.Flow

interface CelebrityRepository {
    fun popularCelebrities(page: Int): Flow<PagingData<Celebrity>>
    fun trendingCelebrities(page: Int): Flow<PagingData<Celebrity>>

    suspend fun searchCelebrity(searchKey: String): Flow<DataState<SearchBaseModel>>
}
