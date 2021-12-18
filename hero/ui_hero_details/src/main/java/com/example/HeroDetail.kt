package com.example

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HeroDetails(
    heroId: Int?,
){
    Text(text = "Hero ID: $heroId")
}