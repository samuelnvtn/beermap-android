package com.mendelu.xnovotn1.beerbook.ui.screens.detail

import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListUIState

sealed class DetailUIState {
    class Default: DetailUIState()
    class Success(val beer: Beer) : DetailUIState()
}