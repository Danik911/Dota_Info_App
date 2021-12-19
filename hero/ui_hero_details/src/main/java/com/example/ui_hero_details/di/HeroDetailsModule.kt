package com.example.ui_hero_details.di

import com.example.core.Logger
import com.example.hero_use_cases.GetHeroFromCache
import com.example.hero_use_cases.HeroUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroDetailsModule {

    /**
     * @param UseCases is provided in app module.
     */

    @Provides
    @Singleton
    fun provideGetHeroFromCache(
        useCases: HeroUseCases
    ): GetHeroFromCache {
        return useCases.getGetHeroFromCache
    }
}