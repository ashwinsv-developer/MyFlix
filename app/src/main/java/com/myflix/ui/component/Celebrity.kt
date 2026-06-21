package com.myflix.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.myflix.data.dataSource.remote.ApiURL
import com.myflix.data.model.celebrities.Celebrity
import com.myflix.utils.items
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import dagger.Component
import java.util.Locale

@Composable
fun Celeberity (
    onItemClick :(String )-> Unit,
    celebrities: LazyPagingItems<Celebrity>,
    navController: NavController
    ){
    Column(modifier = Modifier.padding(12.dp)) {
        DisplayCelebrity(celebrities = celebrities, navController = navController)
    }
}

@Composable
fun DisplayCelebrity (
    celebrities: LazyPagingItems<Celebrity>,
    navController: NavController,

){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(horizontal = 5.dp)) {
       items(celebrities) { item ->
           item?.let {
               CoilImage(
                   modifier = Modifier
                       .aspectRatio(0.66f)
                       .clip(RoundedCornerShape(10.dp)),
                   imageModel = { ApiURL.IMAGE_URL +item.profilePath },
                   imageOptions = ImageOptions(
                       contentScale = ContentScale.Crop,
                       alignment = Alignment.Center,
                   ),
                   component = rememberImageComponent {
                       +CircularRevealPlugin(duration = 800)
                       +ShimmerPlugin(
                           shimmer = Shimmer.Flash(
                               baseColor = MaterialTheme.colorScheme.surfaceVariant,
                               highlightColor = MaterialTheme.colorScheme.surface
                           )
                       )
                   }
               )
               Column(Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp)) {
                   Text(
                       text = item.name,
                       fontWeight = FontWeight.Bold,
                       fontSize = 18.sp
                   )
                   Text(
                       text = "Popularity: ${String.format(Locale.US, "%.1f", item.popularity)}",
                       fontSize = 14.sp,
                       color = Color.Gray
                   )
               }
           }
       }

    }


}