package com.mendelu.xnovotn1.beerbook.ui.screens.calendar

import android.widget.CalendarView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.kizitonwose.calendar.compose.WeekCalendar
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.elements.PlaceholderScreenContent
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListScreenContent
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListUIState
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListViewModel
import com.mendelu.xnovotn1.beerbook.ui.screens.list.RatingBar
import com.mendelu.xnovotn1.beerbook.ui.screens.list.formatDateTime
import com.mendelu.xnovotn1.beerbook.ui.theme.darkTextColor
import com.mendelu.xnovotn1.beerbook.ui.theme.lightButton
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor
import java.time.YearMonth

@Composable
fun CalendarScreen(
    navigation: INavigationRouter,
){
    val viewModel = hiltViewModel<CalendarViewModel>()

    val state = viewModel.calendarUIState.collectAsStateWithLifecycle()

    val beers = remember { mutableStateListOf<Beer>() }

    state.value.let {
        when (it) {
            is CalendarUIState.Default -> {
                viewModel.loadBeers()
            }

            is CalendarUIState.Success -> {
                // dostal jsem seznam
                beers.clear()
                beers.addAll(it.beers)
            }
        }

    }

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.CalendarScreen,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (beers.size == 0) {
            PlaceholderScreenContent(
                //image = R.drawable.undraw_chore_list_ylw0,
                text = "Nemáte žádné záznamy")
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
        topBarText = "Calendar",
        floatingActionButton = {

        },
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        CalendarScreenContent(
            paddingValues = it,
            beers = beers,
            navigation = navigation,
            //actions = viewModel
        )
    }

}

@Composable
fun CalendarScreenContent(
    paddingValues: PaddingValues,
    beers: List<Beer>,
    navigation: INavigationRouter,
){

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
    @Composable
    fun Day(day: CalendarDay) {
        Box(
            modifier = Modifier
                .aspectRatio(1f), // This is important for square sizing!
            contentAlignment = Alignment.Center
        ) {
            Text(text = day.date.dayOfMonth.toString())
        }
    }
    HorizontalCalendar(
        state = state,
        dayContent = { Day(it) }
    )

}

