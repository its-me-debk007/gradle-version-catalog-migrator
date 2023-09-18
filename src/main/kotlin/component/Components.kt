package component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.PurpleBlue
import theme.TextFieldBackground
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
fun CustomTextField(
    text: String,
    placeHolderText: String,
    readOnly: Boolean = false,
    focusable: Boolean = true,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = { Text(placeHolderText, fontStyle = FontStyle.Italic) },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            placeholderColor = Color.White.copy(alpha = 0.4f),
            focusedIndicatorColor = if (focusable) PurpleBlue else Color.Transparent,
            backgroundColor = TextFieldBackground
        ),
        readOnly = readOnly,
        modifier = Modifier.height(280.dp).width(378.dp)
    )
}

@Composable
fun CopyButton(text: String) {
    TextButton(
        onClick = {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(text)
            clipboard.setContents(selection, null)
        },
        enabled = text.isNotBlank(),
        colors = ButtonDefaults.buttonColors(disabledBackgroundColor = Color.DarkGray),
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 6.dp),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(end = 6.dp)
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
    ) {
        Text("COPY", fontSize = 12.sp, letterSpacing = (0.3).sp)
    }
}