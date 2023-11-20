package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import models.Language
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
        placeholder = { Text(placeHolderText, fontStyle = FontStyle.Italic) },
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
fun CustomButton(text: String, onClick: () -> Unit, isEnable: Boolean = true) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(disabledBackgroundColor = Color.DarkGray),
        shape = RoundedCornerShape(12.dp),
        enabled = isEnable,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun Toast(message: String, onDismiss: () -> Unit) {
    val modifier = Modifier.background(MaterialTheme.colors.primary).padding(8.dp).fillMaxWidth()
        .wrapContentHeight().height(60.dp)

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Text(
            text = message, color = Color.White, fontSize = 16.sp
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

@Composable
fun SelectCountries(
    languageList: MutableList<Language>,
    onDismiss: (MutableList<Language>) -> Unit,
) {
    var countryListState by remember { mutableStateOf(languageList) }
    val dialogState = DialogState(
        width = 1000.dp, height = 720.dp, position = WindowPosition(Alignment.Center)
    )
    var selectAllState by remember { mutableStateOf(languageList.all { it.isChecked }) }

    DialogWindow(
        state = dialogState,
        onCloseRequest = { onDismiss(countryListState) },
        title = "Select Language",
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
                verticalArrangement = Arrangement.Top // Align to the top

            ) {
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        selectAllState = !selectAllState
                        countryListState =
                            countryListState.map { it.copy(isChecked = selectAllState) }
                                .toMutableList()

                    }) {
                    Checkbox(
                        checked = selectAllState,
                        onCheckedChange = {
                            selectAllState = it
                            countryListState =
                                countryListState.map { element -> element.copy(isChecked = selectAllState) }
                                    .toMutableList()

                        },
                    )
                    Text(
                        text = "Select All",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(end=8.dp)
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(countryListState) { index, language ->
                        Row(horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable {
                                countryListState = countryListState.toMutableList().apply {
                                    this[index] = language.copy(isChecked = !language.isChecked)
                                }
                                selectAllState = languageList.all { it.isChecked }
                            }) {
                            Checkbox(
                                checked = language.isChecked,
                                onCheckedChange = {
                                    countryListState = countryListState.toMutableList().apply {
                                        this[index] = language.copy(isChecked = it)
                                    }
                                    selectAllState = languageList.all { it.isChecked }
                                },
                            )
                            Text(
                                text = "${language.name} (${language.code})",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            )
                        }
                    }
                }
            }
        },
    )

}