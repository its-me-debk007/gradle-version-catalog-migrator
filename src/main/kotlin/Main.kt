import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import component.CopyButton
import component.CustomTextField
import theme.Background
import theme.PurpleBlue

@Composable
@Preview
fun App() {
    var inputDependencyText by remember { mutableStateOf("") }
    var outputDependencyText by remember { mutableStateOf("") }
    var inputPluginText by remember { mutableStateOf("") }
    var outputPluginText by remember { mutableStateOf("") }

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            background = Background,
            primary = PurpleBlue,
            isLight = false
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Gradle Version Catalog Migrator",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    backgroundColor = PurpleBlue,
                    modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 160.dp)
                        .clip(RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp))
                )
            }
        ) {

            Column(Modifier.fillMaxSize().padding(24.dp)) {

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    CustomTextField(
                        inputDependencyText,
                        "Paste ur dependencies here"
                    ) { inputDependencyText = it }

                    CustomTextField(inputPluginText, "Paste ur plugins here") {
                        inputPluginText = it
                    }
                }

                Button(
                    onClick = {
                        outputDependencyText =
                            Converter.convertDependencies(inputDependencyText.trim())
                        outputPluginText = Converter.convertPlugins(inputPluginText.trim())
                    },
                    colors = ButtonDefaults.buttonColors(disabledBackgroundColor = Color.DarkGray),
                    shape = RoundedCornerShape(12.dp),
                    enabled = inputDependencyText.isNotBlank() || inputPluginText.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 190.dp)
                ) {
                    Text("Convert", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Box(modifier = Modifier, contentAlignment = Alignment.TopEnd) {
                        CustomTextField(
                            outputDependencyText,
                            "Find ur updated dependencies here",
                            true,
                            focusable = false
                        ) { outputDependencyText = it }

                        CopyButton(outputDependencyText)
                    }

                    Box(modifier = Modifier, contentAlignment = Alignment.TopEnd) {
                        CustomTextField(
                            outputPluginText,
                            "Find ur updated plugins here",
                            true,
                            focusable = false
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
        title = "Gradle Version Catalog Migrator",
        state = WindowState(
            width = 850.dp,
            height = 800.dp,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = false
    ) {
        App()
    }
}
