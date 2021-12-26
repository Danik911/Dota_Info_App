package com.example.hero_use_cases

import com.example.core.domain.DataState
import com.example.core.domain.ProgressBarState
import com.example.hero_datasource_test.cache.HeroCacheFake
import com.example.hero_datasource_test.cache.HeroDatabaseFake
import com.example.hero_datasource_test.network.data.HeroDataValid
import com.example.hero_datasource_test.network.data.serializeHeroData
import com.example.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Test
import kotlin.random.Random


/**
 * 1. Success (Retrieve a hero from the cache successfully)
 * 2. Failure (The hero does not exist in the cache)
 */

@ExperimentalSerializationApi
class GetHeroFromCacheTest {

    //system in test
    private lateinit var getHeroFromCache: GetHeroFromCache

    @Test
    fun getHeroFromCache_success() = runBlocking {
        //setup
        val heroDatabase = HeroDatabaseFake()
        val heroCache = HeroCacheFake(heroDatabase)

        getHeroFromCache = GetHeroFromCache(heroCache)

        //insert heros into the cache
        val heroData = serializeHeroData(HeroDataValid.data)
        heroCache.insert(heroData)

        //choose a hero at random
        val hero = heroData[Random.nextInt(0, heroData.size - 1)]

        //Execute the use-case
        val emissions = getHeroFromCache.execute(hero.id).toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<Hero>(ProgressBarState.Loadind))

        //Confirm second emission is data from the cache
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.id == hero.id)
        assert((emissions[1] as DataState.Data).data?.localizedName == hero.localizedName)

        // Confirm loading state is IDLE
        assert(emissions[2] == DataState.Loading<Hero>(ProgressBarState.Idle))

    }
}