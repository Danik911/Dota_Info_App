package com.example.ui_hero_details.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.example.HeroDetails
import com.example.hero_datasource_test.network.data.HeroDataValid
import com.example.hero_datasource_test.network.data.serializeHeroData
import com.example.ui_hero_details.coil.FakeImageLoader
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random


@ExperimentalComposeUiApi
@ExperimentalAnimationApi
class HeroDetailsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context = context)
    private val heroData = serializeHeroData(HeroDataValid.data)

    @Test
    @ExperimentalCoilApi
    fun isHeroDataShown() {
        //choose a random hero
        val hero = heroData[Random.nextInt(0, heroData.size)]
        composeTestRule.setContent {
            val state = remember {
                HeroDetailsState(
                    hero = hero
                )
            }
            HeroDetails(
                state = state,
                imageLoader = imageLoader,
                evens = {}
            )
        }
        composeTestRule.onNodeWithText(hero.localizedName).assertIsDisplayed()
        composeTestRule.onNodeWithText(hero.primaryAttribute.uiValue).assertIsDisplayed()
        composeTestRule.onNodeWithText(hero.attackType.uiValue).assertIsDisplayed()

        val proWinPercentage = (hero.proWins.toDouble() / hero.proPick.toDouble() * 100).toInt()
        composeTestRule.onNodeWithText("$proWinPercentage %")

        val turboWinPercentage =
            (hero.turboWins.toDouble() / hero.turboPicks.toDouble() * 100).toInt()
        composeTestRule.onNodeWithText("$turboWinPercentage %")
    }
}