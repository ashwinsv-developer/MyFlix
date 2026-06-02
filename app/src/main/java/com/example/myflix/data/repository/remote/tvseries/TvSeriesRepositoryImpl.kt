package com.example.myflix.data.repository.remote.tvseries

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myflix.data.dataSource.remote.ApiService
import com.example.myflix.data.dataSource.remote.paging_datasource.tv_series.AiringTodayTvSeriesPagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.tv_series.OnTheAirTvSeriesPagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.tv_series.PopularTvSeriesPagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.tv_series.TopRatedTvSeriesPagingDataSource
import com.example.myflix.data.model.SearchBaseModel
import com.example.myflix.data.model.TvSeriesItem
import com.example.myflix.data.model.artist.Artist
import com.example.myflix.data.model.tv_series_detail.TvSeriesDetail
import com.example.myflix.utils.network.DataState
import com.example.myflix.utils.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvSeriesRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TvSeriesRepository {
    override fun airingTodayTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>> =
        Pager(
            pagingSourceFactory = { AiringTodayTvSeriesPagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun onTheAirTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>>  =
        Pager(
            pagingSourceFactory = { OnTheAirTvSeriesPagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun popularTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>>  =
        Pager(
            pagingSourceFactory = { PopularTvSeriesPagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun topRatedTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>>  =
        Pager(
            pagingSourceFactory = { TopRatedTvSeriesPagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override suspend fun searchTvSeries(searchKey: String): Flow<DataState<SearchBaseModel>>  =
        safeApiCall { apiService.searchTvSeries(searchKey) }

    override suspend fun tvSeriesDetail(seriesId: Int): Flow<DataState<TvSeriesDetail>>  =
        safeApiCall { apiService.tvSeriesDetail(seriesId) }

    override suspend fun recommendedTvSeries(seriesId: Int): Flow<DataState<List<TvSeriesItem>>> =
        safeApiCall { apiService.recommendedTvSeries(seriesId).results }

    override suspend fun artistDetail(personId: Int): Flow<DataState<Artist>> =
        safeApiCall { apiService.tvSeriesCredit(personId) }
}