package components

import AppWindowTitleBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.Language
import theme.PurpleBlue
import theme.TextFieldBackground
import translate.translateHttpURLConnection
import utils.createOutputMainFolder
import utils.createStringFolder
import utils.defaultLanguageCode
import utils.endResourcesWrite
import utils.readAllStrings
import utils.startResourcesWrite
import utils.stringWrite
import java.net.URLEncoder

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
        undecorated = true,
        resizable = false,
        content = {
            Column(
                Modifier.fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = TextFieldBackground,
                        shape = RoundedCornerShape(2.dp)
                    )
            ) {
                AppWindowTitleBar(isWindow = false) { onDismiss(countryListState) }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top

                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
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
                            colors = CheckboxDefaults.colors(checkedColor = TextFieldBackground)
                        )
                        Text(
                            text = "Select All",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(end = 8.dp)
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
                                    colors = CheckboxDefaults.colors(checkedColor = TextFieldBackground)
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
            }
        })
}

@Composable
fun translateDialog(
    languageList: List<Language>,
    folderName: String,
    stringState: String,
    errorCallback: (String) -> Unit,
    onDismiss: (String) -> Unit,
) {
    var checkedCountryListState by remember { mutableStateOf(languageList) }
    var progressState by remember { mutableStateOf(0) }
    val dialogState = DialogState(
        width = 1000.dp, height = 720.dp, position = WindowPosition(Alignment.Center)
    )
    DialogWindow(
        state = dialogState,
        onCloseRequest = {
            onDismiss("")
        },
        title = "Translating All String",
        undecorated = true,
        resizable = false,
        content = {
            Column(
                Modifier.fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = TextFieldBackground,
                        shape = RoundedCornerShape(2.dp)
                    )
            ) {
                AppWindowTitleBar(isWindow = false) { onDismiss("") }

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    LinearProgressIndicator(
                        progressState.toFloat() / 100,
                        modifier = Modifier
                            .height(10.dp)
                            .fillMaxWidth(),
                        color = TextFieldBackground,
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Text(
                            "Progress: ${progressState}%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (progressState == 100) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            CustomButton(Modifier, "Done", onClick = {
                                onDismiss("All strings have been successfully translated.")
                            })
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        verticalArrangement = Arrangement.Top,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                    ) {
                        itemsIndexed(checkedCountryListState) { _, language ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = !language.isChecked,
                                    enabled = false,
                                    onCheckedChange = {},
                                    colors = CheckboxDefaults.colors(checkedColor = TextFieldBackground)
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
            }
        })
    CoroutineScope(Dispatchers.IO).launch {
        try {
            createOutputMainFolder(folderName)
            val allStrings = readAllStrings(stringState)
            progressState = 0
            delay(300)
            languageList
                .forEachIndexed { index, language ->
                    val stringFile =
                        createStringFolder(folderName, language.code)
                    stringFile.startResourcesWrite()
                    allStrings.forEachIndexed { _, stringModel ->

                        val (name, isTranslatable, textContent) = stringModel
                        if (isTranslatable) {
                            translateHttpURLConnection(
                                URLEncoder.encode(textContent, "UTF-8"),
                                URLEncoder.encode(defaultLanguageCode, "UTF-8"),
                                URLEncoder.encode(language.code, "UTF-8"),
                                { convertedString ->
                                    stringFile.stringWrite(name, convertedString)
                                }, { errorMsg ->
                                    errorCallback(errorMsg)
                                })
                            delay(300)
                        } else {
                            stringFile.stringWrite(name, textContent, false)
                        }
                        progressState =
                            ((index.toFloat() / (languageList.size - 1)) * 100).toInt()

                    }
                    stringFile.endResourcesWrite()
                    checkedCountryListState =
                        checkedCountryListState.toMutableList().apply {
                            this[index] = language.copy(isChecked = false)
                        }
                    delay(500)
                }
            progressState = 100
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}