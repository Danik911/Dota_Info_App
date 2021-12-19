package com.example.dota_infoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.example.HeroDetails
import com.example.dota_infoapp.ui.navigation.Screen
import com.example.dota_infoapp.ui.theme.DotaInfoTheme
import com.example.ui_hero_details.ui.HeroDetailsViewModel
import com.example.ui_herolist.HeroList
import com.example.ui_herolist.ui.HeroListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaInfoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroList.route
                ) {
                    addHeroListComposable(
                        imageLoader = imageLoader,
                        navController = navController
                    )
                    addHeroDetailsComposable(imageLoader = imageLoader)


                }
            }
        }
    }


    private fun NavGraphBuilder.addHeroListComposable(
        imageLoader: ImageLoader,
        navController: NavController
    ) {
        composable(Screen.HeroList.route) {
            val viewModel: HeroListViewModel = hiltViewModel()
            HeroList(
                state = viewModel.state.value,
                imageLoader = imageLoader,
                navigateToDetailsScreen = { heroId ->
                    navController.navigate("${Screen.HeroDetails.route}/$heroId")
                }
            )
        }
    }

    private fun NavGraphBuilder.addHeroDetailsComposable(imageLoader: ImageLoader) {
        composable(
            route = Screen.HeroDetails.route + "/{heroId}",
            arguments = Screen.HeroDetails.arguments
        ) {
            val viewModel: HeroDetailsViewModel = hiltViewModel()
            HeroDetails(
                state = viewModel.state.value,
                imageLoader = imageLoader
            )
        }
    }
}