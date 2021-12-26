package com.example.hero_datasource_test.network.data

import com.example.hero_datasource.network.model.HeroDto
import com.example.hero_datasource.network.model.toHero
import com.example.hero_domain.Hero
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
}

@ExperimentalSerializationApi
fun serializeHeroData(jsonData: String): List<Hero>{
    return json.decodeFromString<List<HeroDto>>(jsonData).map { it.toHero() }
}