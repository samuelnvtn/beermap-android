package com.mendelu.xnovotn1.beerbook.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.mendelu.xnovotn1.beerbook.ui.screens.addedit.AddEditBeerScreen
import com.mendelu.xnovotn1.beerbook.ui.screens.calendar.CalendarScreen
import com.mendelu.xnovotn1.beerbook.ui.screens.detail.DetailScreen
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListScreen
import com.mendelu.xnovotn1.beerbook.ui.screens.map.MapScreen
import com.mendelu.xnovotn1.beerbook.ui.screens.stats.StatsScreen


@Composable
fun NavGraph(
    startDestination: String,
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember {
        NavigationRouterImpl(navHostController)
    }
){

    NavHost(navController = navHostController, startDestination = startDestination) {

        composable(route = Destination.BeersListScreen.route) {
            BeersListScreen(navRouter)
        }

        composable(route = Destination.AddEditBeerScreen.route) {
            AddEditBeerScreen(navRouter, null)
        }

        composable(route = "${Destination.AddEditBeerScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            if(id != null) {
                AddEditBeerScreen(navRouter, id)
            }
        }


        composable(route = Destination.MapScreen.route) {
            MapScreen(navRouter)
        }

        composable(route = Destination.CalendarScreen.route) {
            CalendarScreen(navRouter)
        }

        composable(route = Destination.StatsScreen.route) {
            StatsScreen(navRouter)
        }

        composable(route = "${Destination.DetailScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            if (id != null) {
                DetailScreen(navRouter, id)
            }
        }
    }

}
