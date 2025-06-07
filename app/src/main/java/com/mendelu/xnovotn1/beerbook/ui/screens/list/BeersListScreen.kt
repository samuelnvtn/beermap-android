package com.mendelu.xnovotn1.beerbook.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.mendelu.xnovotn1.beerbook.ui.theme.buttonColor
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.acos


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeersListScreen(
    navigation: INavigationRouter
) {
    val viewModel = hiltViewModel<BeersListViewModel>()

    val state = viewModel.beerListUIState.collectAsStateWithLifecycle()

    val beers = remember { mutableStateListOf<Beer>() }

    state.value.let {
        when (it) {
            is BeersListUIState.Default -> {
                viewModel.loadBeers()
            }

            is BeersListUIState.Success -> {
                // dostal jsem seznam
                beers.clear()
                beers.addAll(it.beers)
            }
        }

    }

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.BeersListScreen,
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
            }},
        topBarText = "Beers",
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigation.navigateToAddEditBeer(id = null)
            }, containerColor = buttonColor(), contentColor = textColor()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        BeersListScreenContent(
            paddingValues = it,
            beers = beers,
            navigation = navigation,
            //actions = viewModel
        )
    }

}

@Composable
fun BeersListScreenContent(
    paddingValues: PaddingValues,
    beers: List<Beer>,
    navigation: INavigationRouter,
){
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        beers.forEach {
            item {
                BeerRow(
                    beer = it,
                    onRowClick = {
                        it.id?.let { it1 -> navigation.navigateToDetail(it1) }

                    }
                )
            }
        }
    }
}

@Composable
fun BeerRow(
    beer: Beer,
    onRowClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRowClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = beer.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor()
            )
            Text(
                text = formatDateTime(beer.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = textColor()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tint = textColor(),
                imageVector = Icons.Default.Place,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                color = textColor(),
                text = beer.locationName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))


        RatingBar(
            rating = beer.rating,
            modifier = Modifier.padding(vertical = 4.dp)
        )


        Spacer(modifier = Modifier.height(4.dp))

            if (!beer.description.isNullOrBlank()) {
                Text(
                    text = beer.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        Divider(modifier = Modifier.padding(top = 12.dp))
    }
}

fun formatDateTime(timestamp: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(timestamp)
}

@Composable
fun RatingBar(rating: Float, modifier: Modifier = Modifier) {
    val starHalf = ImageVector.vectorResource(id = R.drawable.star_half)
    val outlinedStar = ImageVector.vectorResource(id = R.drawable.outstar)

    Row(modifier = modifier) {
        for (i in 1..5) {
            val current = i.toFloat()
            val icon = when {
                rating >= current -> Icons.Filled.Star
                rating >= current - 0.5f -> starHalf
                else -> outlinedStar
            }
            Icon(
                tint = textColor(),
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


