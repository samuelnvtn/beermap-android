package com.mendelu.xnovotn1.beerbook.ui.screens.map

import com.mendelu.xnovotn1.beerbook.database.Beer

sealed class MapUIState {
    class Default: MapUIState()
    class Success(val beers: List<Beer>) : MapUIState()
}