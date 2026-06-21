package com.myflix.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(
    navController: NavHostController
) {

    val items = listOf(
        Triple("Movie", Icons.Default.Home, Routes.Home.route),
        Triple("Celebrity", Icons.Default.Person, Routes.Profile.route)
    )

    val currentRoute =
        navController.currentBackStackEntryAsState()
            .value?.destination?.route

    NavigationBar {

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.third,

                onClick = {
                    navController.navigate(item.third) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId)
                    }
                },

                icon = {
                    Icon(
                        imageVector = item.second,
                        contentDescription = item.first
                    )
                },

                label = {
                    Text(item.first)
                }
            )
        }
    }
}