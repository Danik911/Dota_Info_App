package com.example.dota_infoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import com.example.core.DataState
import com.example.core.Logger
import com.example.core.ProgressBarState
import com.example.core.UIComponent
import com.example.dota_infoapp.ui.theme.DotaInfoTheme
import com.example.hero_domain.Hero
import com.example.hero_use_cases.HeroUseCases
import com.example.ui_herolist.HeroList
import com.example.ui_herolist.HeroListState
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MainActivity : ComponentActivity() {

    private val state: MutableState<HeroListState> = mutableStateOf(HeroListState())
    private val progressBarState: MutableState<ProgressBarState> =
        mutableStateOf(ProgressBarState.Idle)
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageLoader = ImageLoader.Builder(applicationContext)
            .error(R.drawable.error_image)
            .placeholder(R.drawable.white_background)
            .availableMemoryPercentage(.25)
            .crossfade(enable = true)
            .build()

        val getHeros = HeroUseCases.build(
            sqlDriver = AndroidSqliteDriver(
                schema = HeroUseCases.schema,
                context = this,
                name = HeroUseCases.dbName
            )
        ).getHeros
        val logger = Logger(tag = "GetHeroesTest")
        getHeros.execute().onEach { dataState ->
            when (dataState) {
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            logger.log((dataState.uiComponent as UIComponent.Dialog).description)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.None).message)
                        }
                    }

                }
                is DataState.Data -> {
                    state.value = state.value.copy(heros = dataState.data ?: listOf())

                }
                is DataState.Loading -> {
                    progressBarState.value = dataState.progressBarState

                }

            }
        }.launchIn(CoroutineScope(IO))


        setContent {
            DotaInfoTheme {
                HeroList(
                    state = state.value,
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