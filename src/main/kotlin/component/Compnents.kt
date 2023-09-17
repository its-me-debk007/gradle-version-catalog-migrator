package component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
fun CustomTextField(text: String, placeHolderText: String, readOnly: Boolean = false, onValueChange: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = { Text(placeHolderText, fontStyle = FontStyle.Italic) },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            placeholderColor = Color.White.copy(alpha = 0.4f)
        ),
        readOnly = readOnly,
        modifier = Modifier.height(250.dp)
    )
}

@Composable
fun CopyButton(text: String) {
    Button(
        onClick = {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(text)
            clipboard.setContents(selection, null)
        },
        enabled = text.isNotBlank(),
        modifier = Modifier.padding(top = 12.dp, end = 12.dp)
    ) {
        Text("COPY", fontSize = 12.sp)
    }
}