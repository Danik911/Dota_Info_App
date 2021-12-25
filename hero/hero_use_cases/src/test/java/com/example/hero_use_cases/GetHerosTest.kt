package com.example.hero_use_cases

import com.example.core.domain.DataState
import com.example.core.domain.ProgressBarState
import com.example.hero_datasource_test.cache.HeroCacheFake
import com.example.hero_datasource_test.cache.HeroDatabaseFake
import com.example.hero_datasource_test.network.HeroServiceFake
import com.example.hero_datasource_test.network.HeroServiceResponseType
import com.example.hero_datasource_test.network.data.HeroDataValid.NUM_HEROS
import com.example.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetHerosTest {

    //system in test
    private lateinit var getHeros: GetHeros

    @Test
    fun getHeros_success() = runBlocking {
        //setup
        val heroDatabase = HeroDatabaseFake()
        val heroCache = HeroCacheFake(db = heroDatabase)
        val heroService = HeroServiceFake.build(
            type = HeroServiceResponseType.ValidData
        )

        getHeros = GetHeros(
            service = heroService,
            cache = heroCache
        )

        //confirm the cache is empty, before any use-case has been executed
        var cachedHeros = heroCache.selectAll()
        assert(cachedHeros.isEmpty())

        //execute the use-case
        val emissions = getHeros.execute().toList()

        //first emission should be loading
        assert(emissions[0] == DataState.Loading<List<Hero>>(ProgressBarState.Loadind))

        //confirm the second emission is data
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.size ?: 0 == NUM_HEROS)

        //confirm the cache is no longer empty
        cachedHeros = heroCache.selectAll()
        assert(cachedHeros.size == NUM_HEROS)

        //third emission should be loading idle
        assert(emissions[2] == DataState.Loading<List<Hero>>(ProgressBarState.Idle))




    }

}