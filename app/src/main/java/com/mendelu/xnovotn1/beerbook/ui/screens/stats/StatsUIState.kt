package com.mendelu.xnovotn1.beerbook.ui.screens.stats

import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.database.BeersDao
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListUIState

sealed class StatsUIState {
    class Default: StatsUIState()
    object Empty : StatsUIState()
    class Success(val count:Int, val mostDrank: String, val avgRating: Float?, val mostBeersInOneDay: BeersDao.MostBeersDay?,          // nový
                  val beersPerMonth: List<BeersDao.BeersPerMonth>,) : StatsUIState()
}