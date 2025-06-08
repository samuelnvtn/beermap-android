package com.mendelu.xnovotn1.beerbook.ui.screens.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.screens.map.MapScreenContent
import com.mendelu.xnovotn1.beerbook.ui.theme.darkTextColor
import com.mendelu.xnovotn1.beerbook.ui.theme.lightButton
import java.util.Locale

@Composable
fun SettingsScreen(
   navigation: INavigationRouter,
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val theme by viewModel.theme.collectAsState()
    val language by viewModel.language.collectAsState()

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.SettingsScreen,
                navigation = navigation
            )
        },
        actions = {

        },
        topBarText = (stringResource(R.string.settings)),
        floatingActionButton = {

        },
        onBackClick = {
            navigation.returnBack()
        },
        content = {
            language?.let { it1 ->
                SettingsScreenContent(
                    paddingValues = it,
                    navigation = navigation,
                    viewModel = viewModel,
                    language = it1
                )
            }
        }
    )
}
fun updateLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val resources = context.resources
    val config = resources.configuration
    config.setLocale(locale)

    resources.updateConfiguration(config, resources.displayMetrics)
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    viewModel: SettingsViewModel,
    language: String,
){
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("Čeština" to "cs", "English" to "en")
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(text = "Select Language")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(12.dp)
        ) {
            Text(text = languages.find { it.second == language }?.first ?: language)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                languages.forEach { (label, code) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            viewModel.setLanguage(code)
                            updateLocale(context, code)
                            expanded = false
                            activity?.recreate()
                        }
                    )
                }
            }
        }
    }
}