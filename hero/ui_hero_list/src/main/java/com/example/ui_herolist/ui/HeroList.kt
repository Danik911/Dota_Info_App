package com.example.ui_herolist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.example.core.domain.ProgressBarState
import com.example.ui_herolist.components.HeroListFilter
import com.example.ui_herolist.components.HeroListItem
import com.example.ui_herolist.components.HeroListToolbar
import com.example.ui_herolist.ui.HeroListEvents
import com.example.ui_herolist.ui.HeroListState

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun HeroList(
    state: HeroListState,
    events: (HeroListEvents) -> Unit,
    imageLoader: ImageLoader,
    navigateToDetailsScreen: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {

            HeroListToolbar(
                heroName = state.heroName,
                onHeroNameChanged = { heroName ->
                    events(HeroListEvents.UpdateHeroName(heroName = heroName))
                },
                onExecuteSearch = {
                    events(HeroListEvents.FilterHeros)
                },
                onShowFilterDialog = { }
            )
            LazyColumn {
                items(state.filteredHeros) { hero ->
                    HeroListItem(
                        hero = hero,
                        imageLoader = imageLoader,
                        onSelectHero = { heroId ->
                            navigateToDetailsScreen(heroId)
                        }
                    )

                }
            }
        }
        HeroListFilter(
            heroFilter = state.heroFilter,
            onUpdateHeroFilter = {heroFilter ->
                  events(HeroListEvents.UpdateHeroFilter(heroFilter = heroFilter))
            },
            onCloseDialog = {

            }

        )

        if (state.progressBarState is ProgressBarState.Loadind) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}