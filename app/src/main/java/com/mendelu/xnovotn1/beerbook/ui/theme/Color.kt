package com.mendelu.xnovotn1.beerbook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//LightTheme
val lightBar = Color(0xFFFAFAFA)
val lightButton = Color(0xFFFE7606)
val lightTextColor = Color(0xA1000000)
val lightField = Color(0xFFFAFAFA)

//DarkTheme
val darkBar = Color(0xFF000000)
val darkButton = Color(0xFF49454F)
val darkTextColor = Color(0xFFFFFFFF)
val darkField = Color(0xFF49454F)
val darkBorder = Color(0x0049454F)

val Black = Color(0xFF000000)
val SofterBlack = Color(0xFF0A0A0A)
val DarkPurple = Color(0xFF49454F)
val Background = Color(0xFF0A0A0A)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


@Composable
fun textColor() = if (isSystemInDarkTheme()) darkTextColor else lightTextColor

@Composable
fun barColor() = if (isSystemInDarkTheme()) darkBar else lightBar

@Composable
fun buttonColor() = if (isSystemInDarkTheme()) darkButton else lightButton

@Composable
fun fieldColor() = if (isSystemInDarkTheme()) darkField else lightField

@Composable
fun border() = if(isSystemInDarkTheme()) darkBorder else darkButton

