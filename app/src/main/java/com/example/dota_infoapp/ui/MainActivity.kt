package com.example.dota_infoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.example.HeroDetails
import com.example.dota_infoapp.ui.navigation.Screen
import com.example.dota_infoapp.ui.theme.DotaInfoTheme
import com.example.ui_hero_details.ui.HeroDetailsViewModel
import com.example.ui_herolist.HeroList
import com.example.ui_herolist.ui.HeroListViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoilApi
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaInfoTheme {
                val navController = rememberAnimatedNavController()
                BoxWithConstraints {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.HeroList.route
                    ) {
                        addHeroListComposable(
                            imageLoader = imageLoader,
                            navController = navController,
                            width = constraints.maxWidth / 2
                        )
                        addHeroDetailsComposable(
                            imageLoader = imageLoader,
                            width = constraints.maxWidth / 2
                        )


                    }
                }
            }
        }
    }


    @ExperimentalAnimationApi
    private fun NavGraphBuilder.addHeroListComposable(
        imageLoader: ImageLoader,
        navController: NavController,
        width: Int,
    ) {
        composable(
            route = Screen.HeroList.route,
            exitTransition = { _, _ ->
                slideOutHorizontally(
                    targetOffsetX = { -width },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { -width },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            val viewModel: HeroListViewModel = hiltViewModel()
            HeroList(
                state = viewModel.state.value,
                imageLoader = imageLoader,
                events = viewModel::onTriggerEvent,
                navigateToDetailScreen = { heroId ->
                    navController.navigate("${Screen.HeroDetails.route}/$heroId")
                },

                )
        }
    }

    @ExperimentalAnimationApi
    private fun NavGraphBuilder.addHeroDetailsComposable(
        imageLoader: ImageLoader,
        width: Int
    ) {
        composable(
            route = Screen.HeroDetails.route + "/{heroId}",
            arguments = Screen.HeroDetails.arguments,
            enterTransition = { _, _ ->
                slideInHorizontally(
                    initialOffsetX = { width },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = { _, _ ->
                slideOutHorizontally(
                    targetOffsetX = { width },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            val viewModel: HeroDetailsViewModel = hiltViewModel()
            HeroDetails(
                state = viewModel.state.value,
                imageLoader = imageLoader,
                events = viewModel::onTriggerEvent,

            )
        }
    }
}