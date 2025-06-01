package com.mendelu.xnovotn1.beerbook

import com.mendelu.xnovotn1.beerbook.database.Beer

sealed class BeersListUIState {
    class Default: BeersListUIState()
    class Success(val beers: List<Beer>) : BeersListUIState()
}