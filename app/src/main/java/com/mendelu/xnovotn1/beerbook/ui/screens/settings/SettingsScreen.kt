package com.mendelu.xnovotn1.beerbook.ui.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.mendelu.xnovotn1.beerbook.navigation.INavigationRouter

@Composable
fun SettingsScreen(
   navigation: INavigationRouter,
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val theme by viewModel.theme.collectAsState()
    val language by viewModel.language.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        SimpleDropdown(
            label = "Vzhled",
            options = listOf("LIGHT", "DARK", "SYSTEM"),
            selectedOption = theme ?: "SYSTEM",
            onOptionSelected = viewModel::setTheme
        )

        Spacer(modifier = Modifier.height(24.dp))

        SimpleDropdown(
            label = "Jazyk",
            options = listOf("cs", "en"),
            selectedOption = language ?: "cs",
            onOptionSelected = viewModel::setLanguage
        )
    }
}

@Composable
fun SimpleDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            Text(
                text = selectedOption,
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(12.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .fillMaxWidth()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
