package com.mendelu.xnovotn1.beerbook.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mendelu.xnovotn1.beerbook.ui.theme.DarkPurple
import com.mendelu.xnovotn1.beerbook.ui.theme.barColor
import com.mendelu.xnovotn1.beerbook.ui.theme.buttonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String,
    onBackClick: (() -> Unit)? = null,
    showLoading: Boolean = false,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    floatingActionButton: @Composable () -> Unit = {},
    bottomBarContent: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit,
    ){

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = topBarText)
            }, navigationIcon = {
                if (onBackClick != null){
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null)
                    }
                }

            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = barColor()
            ), actions = actions)
        },
        bottomBar = {
            NavigationBar {
                bottomBarContent()
            }
        },
        floatingActionButton = floatingActionButton

    )
    {
        if (showLoading){
            LoadingScreen()
        } else if (placeholderScreenContent != null){
           PlaceholderScreen(placeholderScreenContent)
        } else {
            content(it)
        }
    }

}