package com.mendelu.xnovotn1.beerbook.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mendelu.xnovotn1.beerbook.R
import com.mendelu.xnovotn1.beerbook.ui.theme.textColor

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit?,
    modifier: Modifier = Modifier
) {
    val starHalf = ImageVector.vectorResource(id = R.drawable.star_half)
    val outlinedStar = ImageVector.vectorResource(id = R.drawable.outstar)

    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        for (i in 1..5) {
            val current = i.toFloat()
            val icon = when {
                rating >= current -> Icons.Filled.Star
                rating >= current - 0.5f -> starHalf
                else -> outlinedStar
            }
            Icon(
                imageVector = icon,
                contentDescription = "Rating $i",
                tint = textColor(),
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        val newRating = when {
                            rating >= current -> current - 0.5f
                            rating >= current - 0.5f -> current - 1f
                            else -> current
                        }
                        onRatingChanged(newRating.coerceIn(0f, 5f))
                    }
            )
        }
    }
}