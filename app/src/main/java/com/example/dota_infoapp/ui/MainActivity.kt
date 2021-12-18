package com.example.dota_infoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.example.core.DataState
import com.example.core.Logger
import com.example.core.ProgressBarState
import com.example.core.UIComponent
import com.example.dota_infoapp.R
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageLoader = ImageLoader.Builder(applicationContext)
            .error(R.drawable.error_image)
            .placeholder(R.drawable.white_background)
            .availableMemoryPercentage(.25)
            .crossfade(enable = true)
            .build()






        setContent {
            DotaInfoTheme {
                val viewModel: HeroListViewModel = hiltViewModel()
                HeroList(
                    state = viewModel.state.value,
                    imageLoader = imageLoader
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}