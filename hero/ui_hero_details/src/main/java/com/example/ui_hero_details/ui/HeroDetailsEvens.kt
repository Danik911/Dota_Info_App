package com.example.ui_hero_details.ui

sealed class HeroDetailsEvens{

    data class GetHeroFromCache(val heroId: Int): HeroDetailsEvens()

}
