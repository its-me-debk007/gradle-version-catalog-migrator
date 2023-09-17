import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    MaterialTheme(colors = MaterialTheme.colors.copy(background = Background, primary = Rose, isLight = false)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dependency Converter") },
                    modifier = Modifier.clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                )
            }
        ) {
            val textFieldColors = TextFieldDefaults.textFieldColors(textColor = Color.White, placeholderColor = Color.White.copy(alpha = 0.4f))

            Column(Modifier.fillMaxSize().padding(24.dp)) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Paste ur dependencies here", fontStyle = FontStyle.Italic) },
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )

                Button(onClick = {
                    outputText = Converter.convertDependencies(inputText.trim())
                },
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Click")
                }

                TextField(
                    value = outputText,
                    onValueChange = { outputText = it },
                    placeholder = { Text("Find your updated dependencies here", fontStyle = FontStyle.Italic) },
                    colors = textFieldColors,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                )
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gradle Version Catalog Converter",
        state = WindowState(size = DpSize(524.dp, 700.dp)),
    ) {
        App()
    }
}
