package com.mendelu.xnovotn1.beerbook.ui.screens.calendar

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
class CalendarViewModel @Inject constructor(private val repository: IBeersLocalRepository) : ViewModel() {

    private val _calendarUIState: MutableStateFlow<CalendarUIState> =
        MutableStateFlow(value = CalendarUIState.Default())

    val calendarUIState = _calendarUIState.asStateFlow()

    fun loadBeers(){
        viewModelScope.launch {
            repository.getAll().collect { beers ->
                _calendarUIState.update {
                    CalendarUIState.Success(beers)
                }
            }
        }
    }
}