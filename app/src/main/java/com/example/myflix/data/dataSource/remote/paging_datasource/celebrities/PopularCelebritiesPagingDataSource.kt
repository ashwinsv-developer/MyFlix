package com.example.myflix.data.dataSource.remote.paging_datasource.celebrities

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myflix.data.dataSource.remote.ApiService
import com.example.myflix.data.model.celebrities.Celebrity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PopularCelebritiesPagingDataSource @Inject constructor(private val apiService: ApiService) :
    PagingSource<Int, Celebrity>() {

    override fun getRefreshKey(state: PagingState<Int, Celebrity>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Celebrity> {
        return try {
            val nextPage = params.key ?: 1
            val movieList = apiService.popularCelebrities(nextPage)
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