import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import component.*
import theme.*
import util.*

@Composable
@Preview
fun App() {
    var inputDependencyText by remember {
        mutableStateOf("")
    }
    var outputDependencyText by remember { mutableStateOf("") }
    var inputPluginText by remember {
        mutableStateOf("")
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
                    "Paste ur dependencies here",
                    Modifier.weight(1f, true).padding(8.dp),
                ) { inputDependencyText = it }

                CustomTextField(
                    inputPluginText, "Paste ur plugins here",
                    Modifier.weight(1f, true).padding(8.dp)
                ) {
                    inputPluginText = it
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
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
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                ) {
                    Text("Convert", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        inputDependencyText = """
                                implementation("androidx.core:core-ktx:1.9.0")
                                implementation("androidx.appcompat:appcompat:1.6.1")
                                implementation("com.google.android.material:material:1.9.0")
                                implementation("androidx.constraintlayout:constraintlayout:2.1.4")
                                testImplementation("junit:junit:4.13.2")
                                androidTestImplementation("androidx.test.ext:junit:1.1.5")
                                androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
                        """.trimIndent()
                        inputPluginText = """
                                id("com.android.application") version "8.1.1" apply false
                                id("org.jetbrains.kotlin.android") version "1.9.0" apply false
                        """.trimIndent()


                        if (inputDependencyText.isNotBlank()) outputDependencyText =
                            convertDependencies(inputDependencyText.trim())
                        if (inputPluginText.isNotBlank()) outputPluginText =
                            convertPlugins(inputPluginText.trim())

                        setupToml()
                    },
                    colors = ButtonDefaults.buttonColors(disabledBackgroundColor = Color.DarkGray),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                ) {
                    Text("Some Example", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                Box(
                    modifier = Modifier.weight(1f, true).padding(horizontal = 8.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    CustomTextField(
                        outputDependencyText,
                        "Find ur updated dependencies here",
                        Modifier, true,
                        focusable = false
                    ) { outputDependencyText = it }

                    CopyButton(outputDependencyText)
                }

                Box(
                    modifier = Modifier.weight(1f, true).padding(horizontal = 8.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    CustomTextField(
                        outputPluginText,
                        "Find ur updated plugins here",
                        Modifier, true,
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
