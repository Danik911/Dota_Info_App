package com.example.hero_use_cases

import com.example.hero_datasource.network.model.HeroService

data class HeroUseCases(
    val getHeros: GetHeros,
    //TODO(Add other hero use cases)
) {
    companion object Factory{
        fun build(): HeroUseCases{
            val service = HeroService.build()
            return HeroUseCases(
                getHeros = GetHeros(
                    service = service
                ),
            )
        }
    }

}