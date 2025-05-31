package com.mendelu.xnovotn1.beerbook.navigation

interface INavigationRouter {
    fun navigateToAddEditTask(id: Long?)
    fun navigateToList()
    fun returnBack()
}