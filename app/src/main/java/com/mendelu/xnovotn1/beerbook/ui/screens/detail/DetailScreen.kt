package com.mendelu.xnovotn1.beerbook.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.elements.PlaceholderScreenContent
import com.mendelu.xnovotn1.beerbook.ui.theme.buttonColor
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor

@Composable
fun DetailScreen(
    navigation: INavigationRouter,
    id: Long
){
    val viewModel = hiltViewModel<DetailViewModel>()

    val state = viewModel.detailUIState.collectAsStateWithLifecycle()

    val beer = remember { mutableStateOf<Beer?>(null) }

    LaunchedEffect(id) {
        viewModel.loadBeer(id)
    }

    state.value.let {
        when (it) {
            is DetailUIState.Success -> {
                beer.value = it.beer
            }
            else -> {}
        }
    }

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.BeersListScreen,
                navigation = navigation
            )
        },
        placeholderScreenContent = if (beer.value == null) {
            PlaceholderScreenContent(
                //image = R.drawable.undraw_chore_list_ylw0,
                text = "Nemáte žádné záznamy")
        } else null,

        actions = {
            IconButton(onClick = {
                beer.value?.let { loadedBeer ->
                    navigation.navigateToAddEditBeer(loadedBeer.id)
                    println("Naviguju na editaci piva s id: $id")
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null
                )
            }
        },
        topBarText = "Beers",
        onBackClick = {
            navigation.returnBack()
        }
    ) { paddingValues ->
        beer.value?.let { loadedBeer ->
            DetailContent(
                paddingValues = paddingValues,
                beer = loadedBeer,
                navigation = navigation,
            )
        }
    }
}

@Composable
fun DetailContent(
    paddingValues: PaddingValues,
    beer: Beer,
    navigation: INavigationRouter,
){
    Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
        Text(
            text = beer.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Lokalita: ${beer.locationName}")
        Text("Popis: ${beer.description ?: "—"}")
        Text("Hodnocení: ${beer.rating}/5")
    }
}