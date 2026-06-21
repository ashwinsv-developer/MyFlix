package com.myflix.ui.screens.PopularCelebrity

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.myflix.ui.component.Celeberity


@Composable
fun PoupularMoviesScreen(  navController: NavController,  viewModel: PopularCelebrityViewModel = hiltViewModel<PopularCelebrityViewModel>(), ){

    Celeberity(
        onItemClick = { iym -> },
        navController = navController,
        celebrities = viewModel.popularcelebrities.collectAsLazyPagingItems()
    )

}
