package com.mendelu.xnovotn1.beerbook.navigation

sealed class Destination(val route: String) {
    object BeersListScreen : Destination("beers_list")
    object AddEditBeerScreen : Destination("add_edit")
}