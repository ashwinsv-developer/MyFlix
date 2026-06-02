package com.example.myflix.data.repository.remote.celebrity

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myflix.data.dataSource.remote.ApiService
import com.example.myflix.data.dataSource.remote.paging_datasource.celebrities.PopularCelebritiesPagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.celebrities.TrendingCelebritiesPagingDataSource
import com.example.myflix.data.model.SearchBaseModel
import com.example.myflix.data.model.celebrities.Celebrity
import com.example.myflix.utils.network.DataState
import com.example.myflix.utils.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CelebrityRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : CelebrityRepository {
    override fun popularCelebrities(page: Int): Flow<PagingData<Celebrity>> =
        Pager(
            pagingSourceFactory = { PopularCelebritiesPagingDataSource(apiService) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun trendingCelebrities(page: Int): Flow<PagingData<Celebrity>> =
        Pager(
            pagingSourceFactory = { TrendingCelebritiesPagingDataSource(apiService) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override suspend fun searchCelebrity(searchKey: String): Flow<DataState<SearchBaseModel>> =
        safeApiCall { apiService.searchCelebrity(searchKey) }

}