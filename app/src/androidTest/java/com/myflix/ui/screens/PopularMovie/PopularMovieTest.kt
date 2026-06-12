package com.myflix.ui.screens.PopularMovie

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.myflix.data.model.MovieItem
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class PopularMovieTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun popularMoviesContent_refreshButton_callsOnRefresh() {
        // Arrange
        val onRefreshMock = mockk<() -> Unit>(relaxed = true)
        val moviesFlow = MutableStateFlow(PagingData.from(listOf<MovieItem>()))

        composeTestRule.setContent {
            val movies = moviesFlow.collectAsLazyPagingItems()
            PopularMoviesContent(
                movies = movies,
                onNavigateToDetails = {},
                onRefresh = onRefreshMock
            )
        }

        // Act
        composeTestRule.onNodeWithText("Refresh").performClick()

        // Assert
        verify { onRefreshMock.invoke() }
    }
}
