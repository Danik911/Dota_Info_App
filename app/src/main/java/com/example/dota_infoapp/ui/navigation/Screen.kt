package com.example.dota_infoapp.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument>
) {
    object HeroList: Screen(
        route = "heroList",
        arguments = emptyList()
    )

    object HeroDetails: Screen(
        route = "heroDetails",
        arguments = listOf(
            navArgument(name = "heroId"){
                type = NavType.IntType
            }
        )
    )
}
