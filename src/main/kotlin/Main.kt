import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import theme.TextFieldBackground

fun main() = application {
    val windowState = WindowState(
        width = 1280.dp,
        height = 720.dp,
        position = WindowPosition(Alignment.Center)
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "String Translator Desktop App",
        state = windowState,
        resizable = false,
        undecorated = true
    ) {
        Column(
            Modifier.fillMaxSize()
                .border(width = 2.dp, color = TextFieldBackground, shape = RoundedCornerShape(2.dp))
                .clip(RoundedCornerShape(bottomStart = 120.dp, bottomEnd = 120.dp))
        ) {
            AppWindowTitleBar(isMinimized = {
                windowState.isMinimized = true
            }) { exitApplication() }
            App()
        }
    }
}

@Composable
fun WindowScope.AppWindowTitleBar(
    isWindow: Boolean = true,
    isMinimized: () -> Unit = { },
    onClose: () -> Unit,
) =
    WindowDraggableArea {
        Box(
            Modifier.fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TopAppBar(
                title = {
                    Text(
                        "String Translator Desktop App",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                contentColor = Color.White,
                backgroundColor = TextFieldBackground,
                modifier = Modifier.fillMaxSize().padding(horizontal = 160.dp)
                    .clip(RoundedCornerShape(bottomStart = 100.dp, bottomEnd = 100.dp))
            )
            if (isWindow) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Minimise",
                    tint = TextFieldBackground,
                    modifier = Modifier.padding(end = 32.dp).clickable { isMinimized() }
                )
            }

            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Close",
                tint = TextFieldBackground,
                modifier = Modifier.padding(start = 8.dp).clickable { onClose() }
            )
        }
    }
