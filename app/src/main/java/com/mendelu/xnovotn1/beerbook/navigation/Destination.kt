package com.mendelu.xnovotn1.beerbook.navigation

sealed class Destination(val route: String) {
    object BeersListScreen : Destination("beers_list")
    object AddEditBeerScreen : Destination("add_edit")
    object SettingsScreen : Destination("settings")
    object DetailScreen : Destination("detail")
    object MapScreen : Destination("map")
    object CalendarScreen : Destination("calendar")
    object StatsScreen : Destination("stats")
}