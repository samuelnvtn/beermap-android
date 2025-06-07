package com.mendelu.xnovotn1.beerbook.ui.screens.calendar

import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.ui.screens.detail.DetailUIState

sealed class CalendarUIState {
    class Default: CalendarUIState()
    class Success(val beers: List<Beer>) : CalendarUIState()
}