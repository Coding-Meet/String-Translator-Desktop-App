
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import screen.CustomButton
import screen.CustomTextField
import screen.SelectCountries
import screen.Toast
import screen.translateDialog
import utils.WindowState
import utils.isValidXml
import utils.languageList

@Composable
@Preview
fun App() {

    var isWindowShow by remember { mutableStateOf(WindowState.NO_STATE) }
    var stringState by remember { mutableStateOf("") }
    var folderState by remember { mutableStateOf("") }
    var countryListState by remember { mutableStateOf(languageList) }
    var isShowToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    MaterialTheme {
        if (isShowToast) {
            Toast(toastMessage) {
                isShowToast = false
            }
        }
        Column(Modifier.fillMaxSize().padding(8.dp)) {
            CustomTextField(
                stringState,
                "Enter the Strings",
                Modifier.fillMaxHeight().weight(0.9f, true),
            ) { stringState = it }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    modifier = Modifier.weight(1f).height(50.dp),
                    "Select Languages",
                    onClick = {
                        isWindowShow = WindowState.SELECT_COUNTRY
                    })
                CustomTextField(
                    folderState,
                    "Enter the Folder Name",
                    Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .weight(1f) // Use weight to make the TextField fill the available space
                        .height(50.dp)
                ) { folderState = it }
                CustomButton(
                    modifier = Modifier.weight(1f).height(50.dp),
                    "Translate",
                    onClick = {
                        if (stringState.isEmpty()){
                            isShowToast = true
                            toastMessage = "Error: Please enter the source strings"
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                isShowToast = false
                            }
                        }else if (!isValidXml(stringState)){
                            isShowToast = true
                            toastMessage ="Error: Please enter the source strings in XML format into the provided text field."
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                isShowToast = false
                            }
                        } else if (folderState.isEmpty()){
                            isShowToast = true
                            toastMessage ="Error: Enter the folder name where you want to store the translated strings."
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                isShowToast = false
                            }
                        } else if (!countryListState.any { it.isChecked }){
                            isShowToast = true
                            toastMessage = "Error: Please select at least one target language before translating."
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                isShowToast = false
                            }
                        }else {
                            isWindowShow = WindowState.CONVERT_TRANSLATE
                        }
                    }
                )
            }
        }
        when (isWindowShow) {
            WindowState.SELECT_COUNTRY -> {
                SelectCountries(countryListState) {
                    countryListState = it
                    isWindowShow = WindowState.NO_STATE
                    if (!it.any { elements -> elements.isChecked }){
                        isShowToast = true
                        toastMessage = "Error: Please select at least one target language before translating."
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(3000)
                            isShowToast = false
                        }
                    }
                }
            }

            WindowState.CONVERT_TRANSLATE -> {
                translateDialog(
                    countryListState.filter { it.isChecked },
                    folderState,
                    stringState,
                    { errorMsg ->
                        isShowToast = true
                        toastMessage = "Error: $errorMsg"
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(3000)
                            isShowToast = false
                        }
                    },
                    { successMsg ->
                        isWindowShow = WindowState.NO_STATE
                        if (successMsg.trim().isNotEmpty()) {
                            isShowToast = true
                            toastMessage = successMsg
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                isShowToast = false
                            }
                        }
                    })
            }

            else -> {

            }
        }


    }
}