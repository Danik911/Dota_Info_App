package com.example.hero_use_cases

import com.example.core.domain.DataState
import com.example.core.domain.ProgressBarState
import com.example.core.domain.UIComponent
import com.example.hero_datasource.cache.HeroCache
import com.example.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetHeroFromCache(private val cache: HeroCache) {

    fun execute(
        id: Int
    ): Flow<DataState<Hero>> = flow {
        try {
            emit(DataState.Loading<Hero>(progressBarState = ProgressBarState.Loadind))

            val cachedHero = cache.getHero(id = id)

            if (cachedHero == null) {
                throw Exception("That hero does not exist in the cache")
            }
            emit(DataState.Data(cachedHero))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                DataState.Response<Hero>(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.message ?: "Unknown error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading<Hero>(progressBarState = ProgressBarState.Idle))
        }
    }
}
