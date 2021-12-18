package com.example.dota_infoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.example.HeroDetails
import com.example.core.DataState
import com.example.core.Logger
import com.example.core.ProgressBarState
import com.example.core.UIComponent
import com.example.dota_infoapp.R
import com.example.dota_infoapp.ui.navigation.Screen
import com.example.dota_infoapp.ui.theme.DotaInfoTheme
import com.example.hero_use_cases.HeroUseCases
import com.example.ui_herolist.HeroList
import com.example.ui_herolist.ui.HeroListState
import com.example.ui_herolist.ui.HeroListViewModel
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
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
                    addHeroDetailsComposable()


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
private fun NavGraphBuilder.addHeroDetailsComposable(){
    composable(
        route = Screen.HeroDetails.route + "/{heroId}",
        arguments = Screen.HeroDetails.arguments
    ) {
        HeroDetails(heroId = it.arguments?.getInt("heroId"))
    }
}
}