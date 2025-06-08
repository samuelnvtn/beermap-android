package com.mendelu.xnovotn1.beerbook.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendelu.xnovotn1.beerbook.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: IDataStoreRepository
) : ViewModel() {

    fun updateAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
    val theme: StateFlow<String> = repository.getTheme()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "SYSTEM")

    fun setTheme(theme: String) {
        viewModelScope.launch {
            repository.setTheme(theme)
        }
    }

    val language: StateFlow<String?> = repository.getLanguage()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")


    fun setLanguage(language: String) {
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }
}