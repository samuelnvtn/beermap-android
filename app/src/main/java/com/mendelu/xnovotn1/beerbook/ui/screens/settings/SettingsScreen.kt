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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter
import com.mendelu.xnovotn1.beerbook.ui.elements.BaseScreen
import com.mendelu.xnovotn1.beerbook.ui.elements.BottomNavBar
import com.mendelu.xnovotn1.beerbook.ui.screens.map.MapScreenContent
import com.mendelu.xnovotn1.beerbook.ui.theme.darkTextColor
import com.mendelu.xnovotn1.beerbook.ui.theme.lightButton
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor
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
                    language = it1,
                    theme = theme
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
    theme: String,

){
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf(stringResource(R.string.cz) to "cs", stringResource(R.string.en) to "en")
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.lang), color = textColor())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(12.dp)
        ) {
            Text(text = languages.find { it.second == language }?.first ?: language, color = textColor())
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
        val themeOptions = listOf(
            stringResource(R.string.t_system) to "system",
            stringResource(R.string.t_light) to "light",
            stringResource(R.string.t_dark) to "dark"
        )

        var themeExpanded by remember { mutableStateOf(false) }

        Spacer(Modifier.height(24.dp))
        Text(text = stringResource(R.string.theme), color = textColor())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .clickable { themeExpanded = true }
                .padding(12.dp)
        ) {
            Text(
                text = themeOptions.find { it.second == theme }?.first ?: theme,
                color = textColor()
            )
            DropdownMenu(
                expanded = themeExpanded,
                onDismissRequest = { themeExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                themeOptions.forEach { (label, code) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            viewModel.setTheme(code)
                            themeExpanded = false
                        }
                    )
                }
            }
        }
        VersionFooter()
    }
}

@Composable
fun VersionFooter() {
    val context = LocalContext.current
    val version = remember {
        try {
            val pkgInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pkgInfo.versionName ?: "?"
        } catch (e: Exception) {
            "?"
        }
    }

    Text(
        text = "v$version",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        textAlign = TextAlign.Center,
        color = textColor()
    )
}