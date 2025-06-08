package com.mendelu.xnovotn1.beerbook.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.theme.barColor
import com.mendelu.xnovotn1.beerbook.ui.theme.buttonColor
import com.mendelu.xnovotn1.beerbook.ui.theme.fieldColor
import com.mendelu.xnovotn1.beerbook.ui.theme.lightButton
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor

data class BottomNavItem(val icon: ImageVector, val label: String, val destination: Destination)

@Composable
fun BottomNavBar(
    selectedDestination: Destination,
    navigation: INavigationRouter
) {
    val items = listOf(
        BottomNavItem(Icons.Default.Menu, "List", Destination.BeersListScreen),
        BottomNavItem(Icons.Default.LocationOn, "Map", Destination.MapScreen),
        BottomNavItem(Icons.Default.DateRange, "Calendar", Destination.CalendarScreen),
        BottomNavItem(Icons.Default.Person, "Stats", Destination.StatsScreen),
    )

    NavigationBar(
        containerColor = barColor(),
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedDestination == item.destination,
                onClick = {
                    when (item.destination) {
                        Destination.BeersListScreen -> navigation.navigateToList()
                        Destination.MapScreen -> navigation.navigateToMap()
                        Destination.CalendarScreen -> navigation.navigateToCalendar()
                        Destination.StatsScreen -> navigation.navigateToStats()
                        else -> {}
                    }

                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = lightButton,
                    selectedTextColor = lightButton,
                    unselectedIconColor = textColor(),
                    unselectedTextColor = textColor(),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}