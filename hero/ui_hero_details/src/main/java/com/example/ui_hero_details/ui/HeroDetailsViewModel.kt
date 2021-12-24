package com.example.ui_hero_details.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.DataState
import com.example.core.domain.Queue
import com.example.core.domain.UIComponent
import com.example.core.util.Logger
import com.example.hero_use_cases.GetHeroFromCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroDetailsViewModel
@Inject constructor(
    private val getHeroFromCache: GetHeroFromCache,
    private val savedStateHandle: SavedStateHandle,
    private val logger: Logger,

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
            is HeroDetailsEvens.OnRemoveHeadFromQueue -> {
               removeHeadMessage()
            }
        }
    }
    private fun removeHeadMessage() {
        try {
            val queue = state.value.errorQueue
            queue.remove()
            state.value = state.value.copy(errorQueue = Queue(mutableListOf()))//force recompose
            state.value = state.value.copy(errorQueue = queue)
        } catch (e: Exception){
            logger.log("Nothing to remove from Dialog Queue")
        }
    }

    private fun getHeroFromCache(heroId: Int) {
        getHeroFromCache.execute(id = heroId).onEach { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Response -> {
                    when (dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            appendToMessageQueue(uiComponent = dataState.uiComponent)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.None).message)
                        }
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(hero = dataState.data)
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent) {
        val queue = state.value.errorQueue
        queue.add((uiComponent))
        state.value = state.value.copy(errorQueue = Queue(mutableListOf()))//force recompose
        state.value = state.value.copy(errorQueue = queue)
    }


}