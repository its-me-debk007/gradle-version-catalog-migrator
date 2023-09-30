import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import component.*
import theme.*
import util.*

@Composable
@Preview
fun App() {
    var inputDependencyText by remember {
        mutableStateOf(
            "implementation(\"androidx.core:core-ktx:1.9.0\")\n" +
                    "    implementation(\"androidx.appcompat:appcompat:1.6.1\")\n" +
                    "    implementation(\"com.google.android.material:material:1.9.0\")\n" +
                    "    implementation(\"androidx.constraintlayout:constraintlayout:2.1.4\")"
        )
    }
    var outputDependencyText by remember { mutableStateOf("") }
    var inputPluginText by remember {
        mutableStateOf(
            "id(\"com.android.application\") version \"8.1.1\" apply false\n" +
                    "    id(\"org.jetbrains.kotlin.android\") version \"1.9.0\" apply false"
        )
    }
    var outputPluginText by remember { mutableStateOf("") }

    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            background = Background,
            primary = PurpleBlue,
            isLight = false
        )
    ) {
        Column(
            Modifier.fillMaxSize()
                .background(Background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {

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
                    if (inputDependencyText.isNotBlank()) outputDependencyText =
                        convertDependencies(inputDependencyText.trim())
                    if (inputPluginText.isNotBlank()) outputPluginText =
                        convertPlugins(inputPluginText.trim())

                    setupToml()
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

@Composable
private fun WindowScope.AppWindowTitleBar(state: WindowState, onClose: () -> Unit) =
    WindowDraggableArea {
        Box(
            Modifier.fillMaxWidth()
                .height(48.dp)
                .background(Background)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TopAppBar(
                title = {
                    Text(
                        APP_NAME,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                contentColor = Color.White,
                backgroundColor = PurpleBlue,
                modifier = Modifier.fillMaxSize().padding(horizontal = 160.dp)
                    .clip(RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp))
            )

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = MINIMISE,
                tint = Color.White,
                modifier = Modifier.clickable { state.isMinimized = true }.padding(end = 32.dp)
            )

            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = CLOSE,
                tint = Color.White,
                modifier = Modifier.clickable { onClose() })
        }
    }

fun main() = application {
    val windowState = WindowState(
        width = 850.dp,
        height = 740.dp,
        position = WindowPosition(Alignment.Center)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = APP_NAME,
        state = windowState,
        resizable = false,
        undecorated = true,
        icon = painterResource("gradle.svg")
    ) {
        Column(Modifier.fillMaxSize()) {
            AppWindowTitleBar(windowState) { exitApplication() }
            App()
        }
    }
}
