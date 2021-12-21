package com.example.ui_herolist.di


import com.example.core.util.Logger
import com.example.hero_use_cases.FilterHeros
import com.example.hero_use_cases.GetHeros
import com.example.hero_use_cases.HeroUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Provides
    @Singleton
    @Named("heroListLogger")
    fun provideLogger(): Logger {
        return Logger(
            tag = "HeroList",
            isDebug = true
        )
    }

    @Provides
    @Singleton
    fun provideGetHeros(
        useCases: HeroUseCases
    ): GetHeros {
        return useCases.getHeros
    }

    @Provides
    @Singleton
    fun provideFilterHeros(
        useCases: HeroUseCases
    ): FilterHeros {
        return useCases.filterHeros
    }

}