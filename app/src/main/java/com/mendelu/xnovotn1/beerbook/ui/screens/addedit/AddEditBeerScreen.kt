package com.mendelu.xnovotn1.beerbook.ui.screens.addedit

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.permissions.LocationPermissionHandler
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.elements.RatingBar
import com.mendelu.xnovotn1.beerbook.ui.theme.basicMargin
import com.mendelu.xnovotn1.beerbook.ui.theme.buttonColor
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor
import kotlinx.coroutines.suspendCancellableCoroutine

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

        val context = LocalContext.current
        val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
        val cameraPositionState = rememberCameraPositionState()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            if (data.beer.latitude == 0.0 && data.beer.longitude == 0.0) {
                val permissionGranted = androidx.core.app.ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                if (permissionGranted) {
                    val location = fusedLocationClient.lastLocation.await()
                    location?.let {
                        val latLng = com.google.android.gms.maps.model.LatLng(it.latitude, it.longitude)
                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        actions.onLatLngChange(it.latitude, it.longitude)
                    }
                }
            } else {
                val latLng = com.google.android.gms.maps.model.LatLng(data.beer.latitude, data.beer.longitude)
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }

        var hasLocationPermission by remember { mutableStateOf(false) }

        LocationPermissionHandler(
            onPermissionGranted = { hasLocationPermission = true },
            onPermissionDenied = { hasLocationPermission = false }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
        ) {
            Marker(
                state = MarkerState(position = cameraPositionState.position.target)
            )
        }
        }

        LaunchedEffect(cameraPositionState.isMoving) {
            if (!cameraPositionState.isMoving) {
                val target = cameraPositionState.position.target
                actions.onLatLngChange(target.latitude, target.longitude)
            }
        }


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = {
                Text(text = stringResource(R.string.beer_name))
            },
            value = data.beer.name,
            onValueChange = {
                actions.onNameChange(it)
            },
            isError = data.beerTextError != null,
            shape = RoundedCornerShape(8.dp),
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

        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(java.time.ZoneId.systemDefault())
        val zonedDateTime = remember(data.beer.timestamp) {
            data.beer.timestamp.atZone(java.time.ZoneId.systemDefault())
        }

        val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = interactionSource
                ) {
                    val datePicker = android.app.DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            val pickedDate = java.time.LocalDate.of(year, month + 1, day)

                            android.app.TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    val pickedTime = java.time.LocalTime.of(hour, minute)
                                    val newZoned = java.time.ZonedDateTime.of(
                                        pickedDate,
                                        pickedTime,
                                        java.time.ZoneId.systemDefault()
                                    )
                                    actions.onTimestampChange(newZoned.toInstant())
                                },
                                zonedDateTime.hour,
                                zonedDateTime.minute,
                                true
                            ).show()
                        },
                        zonedDateTime.year,
                        zonedDateTime.monthValue - 1,
                        zonedDateTime.dayOfMonth
                    )
                    datePicker.show()
                },
            readOnly = true,
            enabled = false,
            interactionSource = interactionSource,
            label = {
                Text(text = stringResource(R.string.beer_dt))
            },
            value = formatter.format(data.beer.timestamp),
            onValueChange = {},
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = buttonColor()
            ),
            supportingText = {

            },
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
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
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = buttonColor(),
                //todo: barvicky
            ),
        )

        data.beer.description?.let {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                label = {
                    Text(text = "Description")
                },
                value = it,
                onValueChange = {
                    actions.onDescChange(it)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = buttonColor(),
                    //todo: barvicky
                ),
                supportingText = {

                },
            )
        }

        Text(
            text = stringResource(R.string.beer_rating),
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
            color = textColor()
        )

        RatingBar(
            rating = data.beer.rating,
            onRatingChanged = { actions.onRatingChange(it) },
            modifier = Modifier
        )

        Button(
            modifier = Modifier.fillMaxWidth(), onClick = {
                actions.saveBeer()
            }) {
            Text(stringResource(R.string.save))
        }
    }
}


suspend fun com.google.android.gms.tasks.Task<android.location.Location>.await(): android.location.Location? {
    return suspendCancellableCoroutine { cont ->
        addOnSuccessListener { location ->
            cont.resume(location, null)
        }
        addOnFailureListener {
            cont.resume(null, null)
        }
    }
}