package com.mendelu.xnovotn1.beerbook.ui.screens.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.database.Beer
import com.mendelu.xnovotn1.beerbook.database.IBeersLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AddEditBeerViewModel @Inject constructor(private val repository: IBeersLocalRepository) : ViewModel(), AddEditBeerActions {

    private val _addEditBeerUIState: MutableStateFlow<AddEditBeerUIState> =
        MutableStateFlow(value = AddEditBeerUIState())

    val addEditBeerUIState = _addEditBeerUIState.asStateFlow()

    override fun onTextChanged(text: String) {
        _addEditBeerUIState.value =
            _addEditBeerUIState.value.copy(
                beer = _addEditBeerUIState.value.beer.copy(name = text),
                beerTextError = null
            )

    }

    override fun saveBeer() {
        if (_addEditBeerUIState.value.beer.name.isNotEmpty()) {
            viewModelScope.launch {
                if (_addEditBeerUIState.value.beer.id == null) {
                    // pridavam
                    repository.insertData(_addEditBeerUIState.value.beer)
                } else {
                    // edituji
                    repository.update(_addEditBeerUIState.value.beer)
                }
                _addEditBeerUIState.value = _addEditBeerUIState.value.copy(beerSaved = true)
            }
        } else {
            _addEditBeerUIState.value =
                _addEditBeerUIState.value.copy(beerTextError = R.string.cannot_be_empty)
        }

    }

    fun removeBeer(){
        viewModelScope.launch {
            repository.delete(_addEditBeerUIState.value.beer)
            _addEditBeerUIState.value = _addEditBeerUIState.value.copy(beerDeleted = true)
        }
    }


    fun loadBeer(id: Long?){
        if (_addEditBeerUIState.value.beer.isDefault()) {
            if (id != null) {
                viewModelScope.launch {
                    _addEditBeerUIState.value = _addEditBeerUIState.value.copy(
                        beer = repository.getById(id),
                        loading = false
                    )

                }
            } else {
                _addEditBeerUIState.value = _addEditBeerUIState.value.copy(
                    beer = Beer("", Instant.now(),"","",0.0f,"", 0.0, 0.0),
                    loading = false
                )
            }
        }
    }


}