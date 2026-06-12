package com.myflix.ui.home

import androidx.paging.PagingData
import com.myflix.data.model.MovieItem
import com.myflix.data.repository.remote.movie.MovieRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val repository: MovieRepository = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock the repository call used during ViewModel initialization
        every { repository.popularMoviePagingDataSource(null) } returns flowOf(PagingData.empty())
        
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel initialization should call popularMoviePagingDataSource`() {
        // Verify that the repository was called during init
        verify { repository.popularMoviePagingDataSource(null) }
    }
}
