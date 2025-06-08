package com.mendelu.xnovotn1.beerbook.ui.screens.addedit

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
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
import com.mendelu.xnovotn1.beerbook.ui.theme.barColor
import com.mendelu.xnovotn1.beerbook.ui.theme.basicMargin
import com.mendelu.xnovotn1.beerbook.ui.theme.border
import com.mendelu.xnovotn1.beerbook.ui.theme.buttonColor
import com.mendelu.xnovotn1.beerbook.ui.theme.darkTextColor
import com.mendelu.xnovotn1.beerbook.ui.theme.fieldColor
import com.mendelu.xnovotn1.beerbook.ui.theme.lightButton
import com.mendelu.xnovotn1.beerbook.ui.theme.lightTextColor
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.sin

@Composable
fun AddEditBeerScreen(
    navigation: INavigationRouter,
    id: Long?
){
    println("AddEditBeerScreen otevřen s id: $id")
    val viewModel = hiltViewModel<AddEditBeerViewModel>()
    val state = viewModel.addEditBeerUIState.collectAsStateWithLifecycle()


    LaunchedEffect(id) {
        viewModel.loadBeer(id)
    }

    if (state.value.beerDeleted){
        navigation.navigateToList()
    }
    if (state.value.beerSaved){
        navigation.returnBack()
    }

    BaseScreen(
        showLoading = state.value.loading,
        topBarText = if (id == null) stringResource(R.string.add_beer) else stringResource(R.string.edit_beer),
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
            navigation = navigation,
            id = id
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBeerScreenContent(
    paddingValues: PaddingValues,
    data: AddEditBeerUIState,
    actions: AddEditBeerActions,
    navigation: INavigationRouter,
    id: Long?
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<AddEditBeerViewModel>()
    val isBeerLoaded by viewModel.isBeerLoaded.collectAsState()

    LaunchedEffect(data.beer.latitude, data.beer.longitude, isBeerLoaded) {
        if (!isBeerLoaded) {

            return@LaunchedEffect
        }
        if (data.beer.latitude == 0.0 && data.beer.longitude == 0.0) {
            val permissionGranted = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (permissionGranted) {
                val location = fusedLocationClient.lastLocation.await()
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    actions.onLatLngChange(it.latitude, it.longitude)
                }
            }
        } else {
            val latLng = LatLng(data.beer.latitude, data.beer.longitude)
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            actions.onLatLngChange(data.beer.latitude, data.beer.longitude)
        }
    }

    var hasLocationPermission by remember { mutableStateOf(false) }

    LocationPermissionHandler(
        onPermissionGranted = { hasLocationPermission = true },
        onPermissionDenied = { hasLocationPermission = false }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues,)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .height(160.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
        ) {
            Marker(
                state = MarkerState(position = cameraPositionState.position.target)
            )
        }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {

            LaunchedEffect(cameraPositionState.isMoving) {
                if (!cameraPositionState.isMoving) {
                    val target = cameraPositionState.position.target
                    actions.onLatLngChange(target.latitude, target.longitude)
                }
            }

            Text(
                text = stringResource(R.string.beer_name),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                color = textColor()
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
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
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = fieldColor(),
                    disabledBorderColor = border(),
                    focusedTextColor = textColor(),
                    disabledTextColor = textColor(),
                    focusedBorderColor = lightButton,
                    unfocusedBorderColor = border(),
                )
            )

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.systemDefault())
            val zonedDateTime = remember(data.beer.timestamp) {
                data.beer.timestamp.atZone(ZoneId.systemDefault())
            }


            val dateDialogState = rememberMaterialDialogState()
            val timeDialogState = rememberMaterialDialogState()

            var selectedDate by remember { mutableStateOf(zonedDateTime.toLocalDate()) }
            var selectedTime by remember { mutableStateOf(zonedDateTime.toLocalTime()) }

            LaunchedEffect(id, data.beer.timestamp) {
                if (id != null) {
                    selectedDate = data.beer.timestamp.atZone(ZoneId.systemDefault()).toLocalDate()
                    selectedTime = data.beer.timestamp.atZone(ZoneId.systemDefault()).toLocalTime()
                }
            }

            Text(
                text = stringResource(R.string.beer_dt),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                color = textColor()
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dateDialogState.show() },
                value = formatter.format(data.beer.timestamp),
                onValueChange = {},
                supportingText = {},
                readOnly = true,
                enabled = false,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = fieldColor(),
                    disabledBorderColor = border(),
                    focusedTextColor = textColor(),
                    disabledTextColor = textColor(),
                    focusedBorderColor = lightButton,
                    unfocusedBorderColor = border(),
                )
            )

            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(
                    medium = RoundedCornerShape(8.dp)
                )
            ) {
                MaterialDialog(
                    dialogState = dateDialogState,
                    backgroundColor = barColor(),
                    buttons = {
                        positiveButton(
                            stringResource(R.string.next),
                            textStyle = TextStyle(color = lightButton) )
                        {
                            timeDialogState.show()
                        }
                        negativeButton(
                            stringResource(R.string.cancel),
                            textStyle = TextStyle(color = lightButton)
                        )
                    }
                ) {
                    datepicker(
                        initialDate = selectedDate,
                        colors = DatePickerDefaults.colors(
                            headerTextColor = darkTextColor,
                            headerBackgroundColor = lightButton,
                            dateActiveTextColor = textColor(),
                            dateInactiveTextColor = textColor(),
                            dateActiveBackgroundColor = lightButton,
                            calendarHeaderTextColor = textColor(),
                        )
                    ) { date ->
                        selectedDate = date
                    }
                }
            }

            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(
                    medium = RoundedCornerShape(8.dp)
                ),
            ) {
                MaterialDialog(
                    dialogState = timeDialogState,
                    backgroundColor = barColor(),
                    buttons = {
                        positiveButton(
                            stringResource(R.string.next),
                            textStyle = TextStyle(color = lightButton)
                        ) {
                            val newZoned =
                                ZonedDateTime.of(selectedDate, selectedTime, ZoneId.systemDefault())
                            actions.onTimestampChange(newZoned.toInstant())
                        }
                        negativeButton(
                            stringResource(R.string.cancel),
                            textStyle = TextStyle(color = lightButton) )
                    }
                ) {
                    timepicker(
                        initialTime = selectedTime,
                        is24HourClock = true,
                        colors = TimePickerDefaults.colors(
                            headerTextColor = darkTextColor,
                            activeTextColor = textColor(),
                            inactiveTextColor = textColor(),
                            activeBackgroundColor = lightButton,
                            selectorColor = lightButton
                        )
                    ) { time ->
                        selectedTime = time
                    }
                }
            }

            Text(
                text = stringResource(R.string.beer_place),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                color = textColor()
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
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
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = fieldColor(),
                    disabledBorderColor = border(),
                    focusedTextColor = textColor(),
                    disabledTextColor = textColor(),
                    focusedBorderColor = lightButton,
                    unfocusedBorderColor = border(),
                ),
            )

            Text(
                text = stringResource(R.string.beer_desc),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                color = textColor()
            )

            data.beer.description?.let {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    value = it,
                    onValueChange = {
                        actions.onDescChange(it)
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = fieldColor(),
                        disabledBorderColor = border(),
                        focusedTextColor = textColor(),
                        disabledTextColor = textColor(),
                        focusedBorderColor = lightButton,
                        unfocusedBorderColor = border(),
                    ),
                    supportingText = {

                    },
                )
            }

            Text(
                text = stringResource(R.string.beer_rating),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                color = textColor()
            )

            RatingBar(
                rating = data.beer.rating,
                onRatingChanged = { actions.onRatingChange(it) },
                modifier = Modifier
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
             onClick = {
                    actions.saveBeer()
                },
                colors = ButtonColors(
                    containerColor = lightButton,
                    contentColor = textColor(),
                    disabledContainerColor = lightButton,
                    disabledContentColor = textColor(),
                )
            ) {
                Text(
                    stringResource(R.string.save),
                    color = darkTextColor)
            }
        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Task<Location>.await(): Location? {
    return suspendCancellableCoroutine { cont ->
        addOnSuccessListener { location ->
            cont.resume(location, null)
        }
        addOnFailureListener {
            cont.resume(null, null)
        }
    }
}