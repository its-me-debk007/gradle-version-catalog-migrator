import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import component.CopyButton
import component.CustomTextField

@Composable
@Preview
fun App() {
    var inputDependencyText by remember { mutableStateOf("") }
    var outputDependencyText by remember { mutableStateOf("") }
    var inputPluginText by remember { mutableStateOf("") }
    var outputPluginText by remember { mutableStateOf("") }

    MaterialTheme(colors = MaterialTheme.colors.copy(background = Background, primary = Rose, isLight = false)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dependency Converter") },
                    modifier = Modifier.clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                )
            }
        ) {

            Column(Modifier.fillMaxSize().padding(24.dp)) {

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    CustomTextField(inputDependencyText, "Paste ur dependencies here") { inputDependencyText = it }

                    CustomTextField(inputPluginText, "Paste ur plugins here") { inputPluginText = it }
                }

                Button(
                    onClick = {
                        outputDependencyText = Converter.convertDependencies(inputDependencyText.trim())
                        outputPluginText = Converter.convertPlugins(inputPluginText.trim())
                    },
                    shape = CircleShape,
                    enabled = inputDependencyText.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                ) {
                    Text("Convert")
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Box(modifier = Modifier, contentAlignment = Alignment.TopEnd) {
                        CustomTextField(
                            outputDependencyText,
                            "Find your updated dependencies here",
                            true
                        ) { outputDependencyText = it }

                        CopyButton(outputDependencyText)
                    }

                    Box(modifier = Modifier, contentAlignment = Alignment.TopEnd) {
                        CustomTextField(
                            outputPluginText,
                            "Find your updated plugins here",
                            true
                        ) { outputPluginText = it }

                        CopyButton(outputPluginText)
                    }

                }
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gradle Version Catalog Converter",
        state = WindowState(size = DpSize(750.dp, 700.dp)),
    ) {
        App()
    }
}
