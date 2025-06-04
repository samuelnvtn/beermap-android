package com.mendelu.xnovotn1.beerbook.navigation

interface INavigationRouter {
    fun navigateToAddEditBeer(id: Long?)
    fun navigateToList()
    fun navigateToSettings()
    fun navigateToMap()
    fun returnBack()
}