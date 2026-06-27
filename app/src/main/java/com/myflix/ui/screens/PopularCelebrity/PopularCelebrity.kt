package com.myflix.ui.screens.PopularCelebrity

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.myflix.ui.viewmodel.MainActivityViewModel
import com.myflix.ui.component.Celeberity


@Composable
fun PoupularMoviesScreen(  navController: NavController,  viewModel: PopularCelebrityViewModel = hiltViewModel<PopularCelebrityViewModel>(), ){

    var mainViewModel: MainActivityViewModel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
    LaunchedEffect(Unit)  {
        mainViewModel.setTopBarTitle("Popular Celebrity")
        mainViewModel.setBackButtonVisible(false)
    }


    Celeberity(
        onItemClick = { iym -> },
        navController = navController,
        celebrities = viewModel.popularcelebrities.collectAsLazyPagingItems()
    )

}
