package com.mendelu.xnovotn1.beerbook.ui.screens.stats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.database.IBeersLocalRepository
import com.mendelu.xnovotn1.beerbook.ui.screens.list.BeersListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: IBeersLocalRepository
) : ViewModel() {

    private val _statsUIState: MutableStateFlow<StatsUIState> =
        MutableStateFlow(value = StatsUIState.Default())

    val statsUIState = _statsUIState.asStateFlow()

    fun loadStats() {
        viewModelScope.launch {
            val count = repository.getCount()
            val mostDrank = repository.getMostDrankBeer()
            val avgRating = repository.getAvgRating()
            val mostBeersInOneDay = repository.getMostBeersInOneDay()
            val beersPerMonth = repository.getBeersPerMonth()

            if(count == 0) {
                _statsUIState.update {
                    StatsUIState.Empty
                }
            } else {
                _statsUIState.update {
                    StatsUIState.Success(
                        count = count,
                        mostDrank = mostDrank ?: "Neznámé pivo",
                        avgRating = avgRating,
                        mostBeersInOneDay = mostBeersInOneDay,
                        beersPerMonth = beersPerMonth,
                    )
                }
            }
        }
    }

}