package cz.mendelu.todo.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class PlaceholderScreenContent(
    val text: String? = null,
    val image: Int? = null
)

@Composable
fun PlaceholderScreen(
    placeholderScreenContent: PlaceholderScreenContent){

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            if (placeholderScreenContent.image != null){
                // todo image
            }
            if (placeholderScreenContent.text != null){
                Text(text = placeholderScreenContent.text)
            }
        }
    }

}