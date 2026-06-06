package com.myflix.ui.component

import androidx.appcompat.view.menu.MenuView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.myflix.data.model.MovieItem
import com.myflix.utils.items

@Composable
fun MoviesGrid(
    moviesItems: LazyPagingItems<MovieItem>,
    onClick :(MovieItem)-> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().
        padding(horizontal = 5.dp)
    ){
        items(moviesItems) { item ->
            item?.let {
                ItemView(
                    item = item, itemImageUrlExtractor = { it.posterPath }, onclick = onClick
                )
            }
        }
    }

}
