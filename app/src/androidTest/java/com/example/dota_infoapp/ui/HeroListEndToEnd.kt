package com.example.dota_infoapp.ui

import com.example.dota_infoapp.di.HeroUseCasesModule
import com.example.hero_datasource.cache.HeroCache
import com.example.hero_datasource.network.HeroService
import com.example.hero_datasource_test.cache.HeroCacheFake
import com.example.hero_datasource_test.cache.HeroDatabaseFake
import com.example.hero_datasource_test.network.HeroServiceFake
import com.example.hero_datasource_test.network.HeroServiceResponseType
import com.example.hero_use_cases.FilterHeros
import com.example.hero_use_cases.GetHeroFromCache
import com.example.hero_use_cases.GetHeros
import com.example.hero_use_cases.HeroUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@UninstallModules(HeroUseCasesModule::class)
@HiltAndroidTest
class HeroListEndToEnd {

    @Module
    @InstallIn(SingletonComponent::class)
    object TestHeroUseCasesModule {

        @Provides
        @Singleton
        fun provideHeroCache(): HeroCache {
            return HeroCacheFake(HeroDatabaseFake())
        }

        @Provides
        @Singleton
        fun provideHeroService(): HeroService {
            return HeroServiceFake.build(
                type = HeroServiceResponseType.ValidData
            )
        }

        @Provides
        @Singleton
        fun provideHeroUseCases(
            cache: HeroCache,
            service: HeroService
        ): HeroUseCases {
            return HeroUseCases(
                getHeros = GetHeros(
                    service = service,
                    cache = cache
                ),
                getGetHeroFromCache = GetHeroFromCache(
                    cache = cache
                ),
                filterHeros = FilterHeros()
            )
        }

    }
}