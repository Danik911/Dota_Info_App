package com.example.ui_hero_details.ui

import com.example.core.ProgressBarState
import com.example.hero_domain.Hero

data class HeroDetailsState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val hero: Hero? = null,

)