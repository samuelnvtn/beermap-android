package com.mendelu.xnovotn1.beerbook.ui.screens.addedit

import java.time.Instant

interface AddEditBeerActions {
    fun onNameChange(text: String)
    fun onPlaceChange(text: String)
    fun onDescChange(text: String)
    fun onRatingChange(stars: Float)
    fun onTimestampChange(toInstant: Instant)
    fun onLatLngChange(latitude: Double, longitude: Double)
    fun saveBeer()
}