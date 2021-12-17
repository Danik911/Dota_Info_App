package com.example.hero_use_cases

import com.example.core.DataState
import com.example.core.ProgressBarState
import com.example.core.UIComponent
import com.example.hero_datasource.network.model.HeroService
import com.example.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetHeros(
    private val service: HeroService,
    //TODO (Add caching)
) {
    fun execute(): Flow<DataState<List<Hero>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loadind))

            val heroes: List<Hero> = try {
                service.getHeroStats()
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    DataState.Response<List<Hero>>(
                        uiComponent = UIComponent.Dialog(
                            title = "Network error",
                            description = e.message ?: "Unknown error"
                        )
                    )
                )
                listOf()
            }

            //TODO(caching)

            emit(DataState.Data(heroes))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                DataState.Response<List<Hero>>(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.message ?: "Unknown error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Idle))
        }
    }
}