package com.mendelu.xnovotn1.beerbook.ui.screens.addedit

interface AddEditBeerActions {
    fun onNameChange(text: String)
    fun onPlaceChange(text: String)
    fun onDescChange(text: String)
    fun onRatingChange(stars: Float)
    fun saveBeer()
}