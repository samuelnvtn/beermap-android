package com.mendelu.xnovotn1.beerbook.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xnovotn1.beerbook.database.IBeersLocalRepository
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: IBeersLocalRepository) : ViewModel() {

    private val _detailUIState: MutableStateFlow<DetailUIState> =
        MutableStateFlow(value = DetailUIState.Default())

    val detailUIState = _detailUIState.asStateFlow()

    fun loadBeer(id: Long) {
        viewModelScope.launch {
            val beer = repository.getById(id)
            _detailUIState.value = DetailUIState.Success(beer)

        }
    }

}