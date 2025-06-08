package com.mendelu.xnovotn1.beerbook.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.elements.PlaceholderScreenContent
import com.mendelu.xnovotn1.beerbook.ui.screens.list.RatingBar
import com.mendelu.xnovotn1.beerbook.ui.screens.list.formatDateTime
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
                    println("Naviguju na edit s id: $id")
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null
                )
            }
        },
        topBarText = "Detail",
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
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = textColor()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = textColor(),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = beer.locationName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = textColor()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = formatDateTime(beer.timestamp),
            style = MaterialTheme.typography.bodySmall,
            color = textColor()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(beer.latitude, beer.longitude), 15f
                )
            }

            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true)
            ) {
                Marker(
                    state = MarkerState(position = LatLng(beer.latitude, beer.longitude)),
                    title = beer.name,
                    snippet = beer.locationName
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        RatingBar(
            rating = beer.rating,
            modifier = Modifier.padding(vertical = 4.dp),
            starSize = 36.dp
        )


        Spacer(modifier = Modifier.height(8.dp))

        if (!beer.description.isNullOrBlank()) {
            Text(
                text = beer.description!!,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor()
            )
        }
    }
}