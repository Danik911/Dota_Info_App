package com.example.ui_hero_details.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.DataState
import com.example.core.Logger
import com.example.core.ProgressBarState
import com.example.hero_use_cases.GetHeroFromCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HeroDetailsViewModel
@Inject constructor(
    private val getHeroFromCache: GetHeroFromCache,
    private val savedStateHandle: SavedStateHandle,

) : ViewModel() {

    val state: MutableState<HeroDetailsState> =
        mutableStateOf(HeroDetailsState())

    init {
        savedStateHandle.get<Int>("heroId")?.let { heroId ->
            onTriggerEvent(HeroDetailsEvens.GetHeroFromCache(heroId = heroId))
        }
    }

    fun onTriggerEvent(event: HeroDetailsEvens) {
        when (event) {
            is HeroDetailsEvens.GetHeroFromCache -> {
                getHeroFromCache(event.heroId)
            }
        }
    }

    private fun getHeroFromCache(heroId: Int) {
        getHeroFromCache.execute(id = heroId).onEach { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Response -> {
                    //TODO (Handle errors)

                }
                is DataState.Data -> {
                    state.value = state.value.copy(hero = dataState.data)
                }
            }

        }.launchIn(viewModelScope)
    }

}