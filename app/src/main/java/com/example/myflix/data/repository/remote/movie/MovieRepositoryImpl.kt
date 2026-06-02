package com.example.myflix.data.repository.remote.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myflix.data.dataSource.remote.ApiService
import com.example.myflix.data.dataSource.remote.paging_datasource.GenrePagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.movie.NowPlayingMoviePagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.movie.PopularMoviePagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.movie.TopRatedMoviePagingDataSource
import com.example.myflix.data.dataSource.remote.paging_datasource.movie.UpcomingMoviePagingDataSource
import com.example.myflix.data.model.Genres
import com.example.myflix.data.model.MovieItem
import com.example.myflix.data.model.SearchBaseModel
import com.example.myflix.data.model.artist.Artist
import com.example.myflix.data.model.moviedetail.MovieDetail
import com.example.myflix.utils.network.DataState
import com.example.myflix.utils.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : MovieRepository {


    override suspend fun movieDetail(movieId: Int): Flow<DataState<MovieDetail>> =
        safeApiCall { apiService.movieDetail(movieId) }

    override suspend fun recommendedMovie(movieId: Int): Flow<DataState<List<MovieItem>>> =
        safeApiCall { apiService.recommendedMovie(movieId).results }


    override suspend fun movieSearch(searchKey: String): Flow<DataState<SearchBaseModel>> =
        safeApiCall { apiService.searchMovie(searchKey) }

    override suspend fun genreList(): Flow<DataState<Genres>> =
        safeApiCall { apiService.genreList() }

    override suspend fun movieCredit(movieId: Int): Flow<DataState<Artist>> =
        safeApiCall { apiService.movieCredit(movieId) }

    override fun nowPlayingMoviePagingDataSource(genreId: String?): Flow<PagingData<MovieItem>> =
        Pager(
            pagingSourceFactory = { NowPlayingMoviePagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun popularMoviePagingDataSource(genreId: String?): Flow<PagingData<MovieItem>> =
        Pager(
            pagingSourceFactory = { PopularMoviePagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun topRatedMoviePagingDataSource(genreId: String?): Flow<PagingData<MovieItem>> =
        Pager(
            pagingSourceFactory = { TopRatedMoviePagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun upcomingMoviePagingDataSource(genreId: String?): Flow<PagingData<MovieItem>> =
        Pager(
            pagingSourceFactory = { UpcomingMoviePagingDataSource(apiService, genreId) },
            config = PagingConfig(pageSize = 20)
        ).flow

    override fun genrePagingDataSource(genreId: String): Flow<PagingData<MovieItem>> = Pager(
        pagingSourceFactory = { GenrePagingDataSource(apiService, genreId) },
        config = PagingConfig(pageSize = 20)
    ).flow

}