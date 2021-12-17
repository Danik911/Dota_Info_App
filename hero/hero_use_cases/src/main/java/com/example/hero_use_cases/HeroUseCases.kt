package com.example.hero_use_cases

import com.example.hero_datasource.cache.HeroCache
import com.example.hero_datasource.network.HeroService
import com.squareup.sqldelight.db.SqlDriver

data class HeroUseCases(
    val getHeros: GetHeros,
    //TODO(Add other hero use cases)
) {
    companion object Factory {
        fun build(sqlDriver: SqlDriver): HeroUseCases {
            val service = HeroService.build()
            val cache = HeroCache.build(sqlDriver = sqlDriver)
            return HeroUseCases(
                getHeros = GetHeros(
                    service = service,
                    cache = cache
                ),
            )
        }
        val schema: SqlDriver.Schema = HeroCache.schema
        val dbName: String = HeroCache.dbName
    }
}