package com.example.hero_use_cases

import com.example.core.domain.FilterOrder
import com.example.hero_domain.Hero
import com.example.hero_domain.HeroAttribute
import com.example.hero_domain.HeroFilter
import kotlin.math.round

class FilterHeros {

    fun execute(
        current: List<Hero>,
        heroName: String,
        heroFilter: HeroFilter,
        attribute: HeroAttribute,
    ): List<Hero> {
        val filteredList: MutableList<Hero> = current.filter {
            it.localizedName.lowercase().contains(heroName.lowercase())
        }.toMutableList()

        when (heroFilter) {
            is HeroFilter.Hero -> {
                when (heroFilter.order) {
                    is FilterOrder.Descending -> {
                        filteredList.sortByDescending { it.localizedName }
                    }
                    is FilterOrder.Ascending -> {
                        filteredList.sortBy { it.localizedName }
                    }
                }
            }
            is HeroFilter.ProWins -> {
                when (heroFilter.order) {
                    is FilterOrder.Descending -> {
                        filteredList.sortByDescending {
                            getWinRate(
                                it.proPick.toDouble(),
                                it.proWins.toDouble()
                            )
                        }
                    }
                    is FilterOrder.Ascending -> {
                        filteredList.sortBy {
                            getWinRate(
                                it.proPick.toDouble(),
                                it.proWins.toDouble()
                            )
                        }
                    }
                }
            }
        }


    }

    private fun getWinRate(proWins: Double, proPick: Double): Int {
        return if (proPick <= 0) {
            0
        } else {
            val winRate: Int = round(proWins / proPick * 100).toInt()
            winRate
        }
    }
}