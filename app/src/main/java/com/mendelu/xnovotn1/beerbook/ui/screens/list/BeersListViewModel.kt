package com.mendelu.xnovotn1.beerbook.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xnovotn1.beerbook.database.IBeersLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeersListViewModel @Inject constructor(private val repository: IBeersLocalRepository) : ViewModel(), BeerListActions{

    private val _beerListUIState: MutableStateFlow<BeersListUIState> =
        MutableStateFlow(value = BeersListUIState.Default())

    val beerListUIState = _beerListUIState.asStateFlow()

    fun loadBeers(){
        viewModelScope.launch {
            repository.getAll().collect { beers ->
                _beerListUIState.update {
                    BeersListUIState.Success(beers)
                }
            }
        }
    }

    override fun changeBeerState(id: Long, state: Boolean) {
        viewModelScope.launch {
            repository.changeBeerState(id, state)
        }

    }


}