package com.myflix.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.myflix.data.dataSource.remote.ApiURL
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun <T> ItemView(
    item: T,
    itemImageUrlExtractor: (T) -> String,
    onclick: (T) -> Unit,
) {
    Column(modifier = Modifier.padding(5.dp)) {
        CoilImage(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onclick(item)
                },
            imageModel = { ApiURL.IMAGE_URL + itemImageUrlExtractor(item) },
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
    }
}

