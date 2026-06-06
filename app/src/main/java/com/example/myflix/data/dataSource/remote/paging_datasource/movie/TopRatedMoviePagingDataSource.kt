package com.example.myflix.data.dataSource.remote.paging_datasource.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myflix.data.dataSource.remote.ApiService
import com.example.myflix.data.model.MovieItem
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.isNotEmpty

class TopRatedMoviePagingDataSource @Inject constructor(private val apiService: ApiService, private val genreId:String?) :
    PagingSource<Int, MovieItem>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val nextPage = params.key ?: 1
            val movieList = apiService.topRatedMovies(nextPage, genreId)
            LoadResult.Page(
                data = movieList.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey =  if (movieList.results.isNotEmpty()) movieList.page + 1 else  null
            )
        } catch (exception: IOException) {

            return LoadResult.Error(exception)
        } catch (httpException: HttpException) {

            return LoadResult.Error(httpException)
        }
    }
}