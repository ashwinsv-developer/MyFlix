package com.example.myflix.data.repository.remote.tvseries

import androidx.paging.PagingData
import com.example.myflix.utils.network.DataState
import com.example.myflix.data.model.SearchBaseModel
import com.example.myflix.data.model.TvSeriesItem
import com.example.myflix.data.model.artist.Artist
import com.example.myflix.data.model.tv_series_detail.TvSeriesDetail
import kotlinx.coroutines.flow.Flow

interface TvSeriesRepository {
    fun airingTodayTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>>
    fun onTheAirTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>>
    fun popularTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>>
    fun topRatedTvSeriesPagingDataSource(genreId: String?): Flow<PagingData<TvSeriesItem>>
    suspend fun searchTvSeries(searchKey: String): Flow<DataState<SearchBaseModel>>
    suspend fun tvSeriesDetail(seriesId: Int): Flow<DataState<TvSeriesDetail>>
    suspend fun recommendedTvSeries(seriesId: Int): Flow<DataState<List<TvSeriesItem>>>
    suspend fun artistDetail(personId: Int): Flow<DataState<Artist>>
}