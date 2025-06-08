package com.mendelu.xnovotn1.beerbook

import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph
import com.mendelu.xnovotn1.beerbook.navigation.Destination
import com.mendelu.xnovotn1.beerbook.navigation.NavGraph
import com.mendelu.xnovotn1.beerbook.ui.screens.settings.SettingsViewModel
import com.mendelu.xnovotn1.beerbook.ui.theme.BeerBookTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val theme by settingsViewModel.theme.collectAsState()

            val context = androidx.compose.ui.platform.LocalContext.current
            val activity = context as? android.app.Activity
            val previousTheme = remember { androidx.compose.runtime.mutableStateOf(theme) }

            androidx.compose.runtime.LaunchedEffect(theme) {
                if (theme != previousTheme.value) {
                    previousTheme.value = theme
                    activity?.recreate()
                }
            }


            val isDarkTheme = when (theme.uppercase()) {
                "LIGHT" -> false
                "DARK" -> true
                "SYSTEM" -> isSystemInDarkTheme()
                else -> isSystemInDarkTheme()
            }
            BeerBookTheme(darkTheme = isDarkTheme) {
                NavGraph(startDestination = Destination.BeersListScreen.route)
            }
        }
    }
}

@Composable
 fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BeerBookTheme {
        Greeting("Android")
    }
}