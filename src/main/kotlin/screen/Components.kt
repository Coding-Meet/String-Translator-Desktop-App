package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.PurpleBlue
import theme.TextFieldBackground

@Composable
fun CustomTextField(
    text: String,
    placeHolderText: String,
    modifier: Modifier,
    readOnly: Boolean = false,
    focusable: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(placeHolderText, color = Color.White) },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            placeholderColor = Color.White.copy(alpha = 0.4f),
            focusedIndicatorColor = if (focusable) PurpleBlue else Color.White,
            backgroundColor = TextFieldBackground
        ),
        readOnly = readOnly,
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).height(280.dp)
    )
}

@Composable
fun CustomButton(modifier: Modifier, text: String, onClick: () -> Unit, isEnable: Boolean = true) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(TextFieldBackground, Color.White),
        shape = RoundedCornerShape(12.dp),
        enabled = isEnable,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun Toast(message: String, onDismiss: () -> Unit) {
    val modifier =
        Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp).clip(RoundedCornerShape(12.dp))
            .background(
                if (message.startsWith("Error")) {
                    MaterialTheme.colors.error
                } else {
                    Color(0xFF4BB543)
                }
            ).fillMaxWidth()
            .wrapContentHeight().height(40.dp)

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Text(
            text = message, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = onDismiss, modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White
            )
        }
    }
}

