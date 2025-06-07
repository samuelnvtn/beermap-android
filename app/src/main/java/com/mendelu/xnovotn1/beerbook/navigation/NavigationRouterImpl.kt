package com.mendelu.xnovotn1.beerbook.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToAddEditBeer(id: Long?) {
        if (id != null) {
            navController.navigate("${Destination.AddEditBeerScreen.route}/${id}")
        } else {
            navController.navigate(Destination.AddEditBeerScreen.route)
        }

    }

    override fun navigateToList() {
        navController.navigate(Destination.BeersListScreen.route)
    }

    override fun navigateToSettings() {
        TODO("Not yet implemented")
    }

    override fun navigateToMap() {
        navController.navigate(Destination.MapScreen.route)
    }

    override fun navigateToDetail(id: Long) {
        navController.navigate("${Destination.DetailScreen.route}/${id}")
    }

    override fun returnBack() {
        navController.popBackStack()
    }
}