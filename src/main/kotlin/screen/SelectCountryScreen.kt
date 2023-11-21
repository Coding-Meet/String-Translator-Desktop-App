package screen

import AppWindowTitleBar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import models.Language
import theme.TextFieldBackground

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
        icon = painterResource("translate.svg"),
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
