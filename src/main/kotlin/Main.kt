import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
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
                            GradleCatalogUtils.convertDependencies(inputDependencyText.trim())
                        outputPluginText = GradleCatalogUtils.convertPlugins(inputPluginText.trim())

                        GradleCatalogUtils.setupToml()
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

@Composable
private fun WindowScope.AppWindowTitleBar() = WindowDraggableArea {
    Row (Modifier.fillMaxWidth().height(48.dp).background(Background)) {
//        Text()
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
//        resizable = false,
        undecorated = true
    ) {
        Column(Modifier.fillMaxSize()) {
            AppWindowTitleBar()
            App()
        }
    }
}
