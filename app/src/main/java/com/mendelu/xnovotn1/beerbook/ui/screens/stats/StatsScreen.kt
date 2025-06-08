package com.mendelu.xnovotn1.beerbook.ui.screens.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.elements.PlaceholderScreenContent
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListScreenContent
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListUIState
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListViewModel
import com.mendelu.xnovotn1.beerbook.ui.theme.darkTextColor
import com.mendelu.xnovotn1.beerbook.ui.theme.fieldColor
import com.mendelu.xnovotn1.beerbook.ui.theme.lightButton
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun StatsScreen(
    navigation: INavigationRouter,
) {

    val viewModel = hiltViewModel<StatsViewModel>()

    val state = viewModel.statsUIState.collectAsStateWithLifecycle()

    val currentState = state.value


    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.StatsScreen,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (currentState is StatsUIState.Empty) {
            PlaceholderScreenContent(
                image = R.drawable.undraw_beer,
                text = stringResource(R.string.beer_emptystats)
                )
        } else null,

        actions = {
            IconButton(onClick = {
                navigation.navigateToSettings()
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        topBarText = (stringResource(R.string.beer_stats)),
        floatingActionButton = {
        },
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        StatsScreenContent(
            paddingValues = it,
            navigation = navigation,
            statsState = state.value
            )
    }
}

@Composable
fun StatsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    statsState: StatsUIState
){
    Column(modifier = Modifier.padding(paddingValues)) {
        when (statsState) {
            is StatsUIState.Success -> {
                StatCard(title = (stringResource(R.string.stat1)), value = statsState.count.toString())
                StatCard(title = (stringResource(R.string.stat2)), value = statsState.mostDrank)
                StatCard(title = (stringResource(R.string.stat3)), value = statsState.avgRating?.let { "%.2f".format(it) } ?: "N/A")
                statsState.mostBeersInOneDay?.let {
                    StatCard(
                        title = (stringResource(R.string.stat4)),
                        value = "${it.total}" + " " + (stringResource(R.string.stat4_5)) + " " + "${formatDate(it.day)}"
                    )
                }
                StatCard(
                    title = (stringResource(R.string.stat5)),
                    value = statsState.beersPerMonth.joinToString(separator = "\n") {
                        "${formatMonth(it.month)}: ${it.total}"
                    }
                )
            }
            is StatsUIState.Empty -> {
                Text("Zatím žádné záznamy. Zaznamenej si první pivo!")
            }
            is StatsUIState.Default -> {
                Text("Načítání statistik...")
            }
        }
    }

}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = fieldColor()
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = textColor())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, color = textColor())
        }
    }
}


fun formatDate(input: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd. MM. yyyy", Locale.getDefault())
        val date = inputFormat.parse(input)
        if (date != null) outputFormat.format(date) else input
    } catch (e: Exception) {
        input
    }
}

fun formatMonth(input: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM. yyyy", Locale.getDefault())
        val date = inputFormat.parse(input)
        if (date != null) outputFormat.format(date) else input
    } catch (e: Exception) {
        input
    }
}