package com.mendelu.xnovotn1.beerbook.ui.screens.addedit

import com.mendelu.xnovotn1.beerbook.database.Beer
import java.time.Instant

data class AddEditBeerUIState(
    var beer: Beer = Beer("", Instant.now(),"","",0.0f,"", 0.0, 0.0),
    var beerTextError: Int? = null,
    var loading: Boolean = false,
    var beerSaved: Boolean = false,
    var beerDeleted: Boolean = false
)