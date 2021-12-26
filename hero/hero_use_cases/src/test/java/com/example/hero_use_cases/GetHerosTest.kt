package com.example.hero_use_cases

import com.example.core.domain.DataState
import com.example.core.domain.ProgressBarState
import com.example.core.domain.UIComponent
import com.example.core.domain.UIComponentState
import com.example.hero_datasource_test.cache.HeroCacheFake
import com.example.hero_datasource_test.cache.HeroDatabaseFake
import com.example.hero_datasource_test.network.HeroServiceFake
import com.example.hero_datasource_test.network.HeroServiceResponseType
import com.example.hero_datasource_test.network.data.HeroDataValid
import com.example.hero_datasource_test.network.data.HeroDataValid.NUM_HEROS
import com.example.hero_datasource_test.network.data.serializeHeroData
import com.example.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
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

    @ExperimentalSerializationApi
    @Test
    fun getHeros_malformedData_successFromCache() = runBlocking {
        //setup
        val heroDatabase = HeroDatabaseFake()
        val heroCache = HeroCacheFake(db = heroDatabase)
        val heroService = HeroServiceFake.build(
            type = HeroServiceResponseType.MalformedData
        )

        getHeros = GetHeros(
            service = heroService,
            cache = heroCache
        )

        //confirm the cache is empty, before any use-case has been executed
        var cachedHeros = heroCache.selectAll()
        assert(cachedHeros.isEmpty())

        //Add some data to the cache
        val heroData = serializeHeroData(HeroDataValid.data)
        heroCache.insert(heroData)

        //confirm the cache is not empty
        cachedHeros = heroCache.selectAll()
        assert(cachedHeros.isNotEmpty())

        //Execute the use-case
        val emissions = getHeros.execute().toList()

        //first emission should be loading
        assert(emissions[0] == DataState.Loading<List<Hero>>(ProgressBarState.Loadind))

        //confirm the second emission is error response
        assert(emissions[1] is DataState.Response)
        assert(
            ((emissions[1] as DataState.Response)
                .uiComponent as UIComponent.Dialog)
                .title == "Network error"
        )
        assert(
            ((emissions[1] as DataState.Response)
                .uiComponent as UIComponent.Dialog)
                .description.contains("Unexpected JSON token at offset")
        )

        //confirm third emission is data from the cache
        assert(emissions[2] is DataState.Data)
        assert((emissions[2] as DataState.Data).data?.size == 122)

        //third emission should be loading idle
        assert(emissions[3] == DataState.Loading<List<Hero>>(ProgressBarState.Idle))


    }
    @Test
    fun getHeros_emptyList() = runBlocking {
        //setup
        val heroDatabase = HeroDatabaseFake()
        val heroCache = HeroCacheFake(db = heroDatabase)
        val heroService = HeroServiceFake.build(
            type = HeroServiceResponseType.EmptyList
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
        assert((emissions[1] as DataState.Data).data?.size == 0)

        //confirm the cache is no longer empty
        cachedHeros = heroCache.selectAll()
        assert(cachedHeros.isEmpty())

        //third emission should be loading idle
        assert(emissions[2] == DataState.Loading<List<Hero>>(ProgressBarState.Idle))
    }
}