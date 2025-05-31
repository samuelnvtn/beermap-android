package com.mendelu.xnovotn1.beerbook.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToAddEditTask(id: Long?) {
        if (id != null) {
            navController.navigate("${Destination.AddEditBeerScreen.route}/${id}")
        } else {
            navController.navigate(Destination.AddEditBeerScreen.route)
        }

    }

    override fun navigateToList() {
        navController.navigate(Destination.BeersListScreen.route)
    }

    override fun returnBack() {
        navController.popBackStack()
    }
}