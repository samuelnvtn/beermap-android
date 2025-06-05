package com.mendelu.xnovotn1.beerbook.ui.screens.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.theme.basicMargin
import com.mendelu.xnovotn1.beerbook.ui.theme.buttonColor

@Composable
fun AddEditBeerScreen(
    navigation: INavigationRouter,
    id: Long?
){
    val viewModel = hiltViewModel<AddEditBeerViewModel>()

    LaunchedEffect(id) {
        viewModel.loadBeer(id)
    }

    val state = viewModel.addEditBeerUIState.collectAsStateWithLifecycle()

    if (state.value.beerDeleted || state.value.beerSaved){
        navigation.returnBack()
    }

    BaseScreen(
        showLoading = state.value.loading,
        topBarText = stringResource(R.string.add_beer),
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            if (id != null){
                IconButton(onClick = {
                    viewModel.removeBeer()
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        },
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.AddEditBeerScreen,
                navigation = navigation
            )
        }
    ) {
        AddEditBeerScreenContent(
            paddingValues = it,
            actions = viewModel,
            data = state.value,
            navigation = navigation
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBeerScreenContent(
    paddingValues: PaddingValues,
    data: AddEditBeerUIState,
    actions: AddEditBeerActions,
    navigation: INavigationRouter
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = basicMargin,
                start = basicMargin,
                end = basicMargin,
            )
    ){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(R.string.beer_name))
            },
            value = data.beer.name,
            onValueChange = {
                actions.onNameChange(it)
            },
            isError = data.beerTextError != null,
            supportingText = {
                if (data.beerTextError != null) {
                    Text(text = stringResource(data.beerTextError!!))
                    //Text(text = context.getString(data.taskTextError!!))
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = buttonColor(),
                //todo: barvicky
                )
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Place")
            },
            value = data.beer.locationName,
            onValueChange = {
                actions.onPlaceChange(it)
            },
            isError = data.beerTextError != null,
            supportingText = {
                if (data.beerTextError != null) {
                    Text(text = stringResource(data.beerTextError!!))
                    //Text(text = context.getString(data.taskTextError!!))
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = buttonColor(),
                //todo: barvicky
            )
        )

        data.beer.description?.let {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Description")
                },
                value = it,
                onValueChange = {
                    actions.onDescChange(it)
                },
                isError = data.beerTextError != null,
                supportingText = {
                    if (data.beerTextError != null) {
                        Text(text = stringResource(data.beerTextError!!))
                        //Text(text = context.getString(data.taskTextError!!))
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = buttonColor(),
                    //todo: barvicky
                )
            )
        }


        Button(
            modifier = Modifier.fillMaxWidth(), onClick = {
                actions.saveBeer()
            }) {
            Text(stringResource(R.string.save))
        }
    }
}