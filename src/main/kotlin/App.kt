import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import components.CustomButton
import components.CustomTextField
import components.SelectCountries
import components.Toast
import components.translateDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import utils.WindowState
import utils.languageList

@Composable
@Preview
fun App() {

    var isWindowShow by remember { mutableStateOf(WindowState.NO_STATE) }
    var stringState by remember {
        mutableStateOf(
            """
<resources>
    <string name="app_name">String Translator App</string>
    <string name="hello">Hello, I am Meet</string>
    <string name="how_are_you">How are You?</string>
    <string name="video_directory" translatable="false">Video folder</string>
    <string name="no_internet">Please check your network connection</string>
    <string name="api_failed">Something not right, Please try again</string>
    <string name="permission_error">This App will need Permission on your device.</string>
    <string name="question_favorites">What are your favorites?</string>
    <string name="answer_grateful">I'm grateful for your help</string>
    <string name="loading_ad" translatable="false">loading_ad</string>
    <string name="app_id" translatable="false">ca-app-pub-3940256099942544~3347511713</string>
    <string name="app_open_ads" translatable="false">ca-app-pub-3940256099942544/3419835294</string>
    <string name="request_assistance">Can you assist me?</string>
</resources>
    """.trimIndent()
        )
    }
    var folderState by remember { mutableStateOf("String Translator App") }
    var countryListState by remember { mutableStateOf(languageList) }
    var isShowToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    MaterialTheme {
        Row(
            Modifier.fillMaxSize().padding(10.dp)
        ) {
            CustomTextField(
                stringState,
                "Enter the Strings",
                Modifier.fillMaxHeight().weight(0.8f, true),
            ) { stringState = it }

            Column(
                Modifier.weight(0.2f, true).padding(start = 10.dp)
            ) {
                CustomButton("Select Languages", onClick = {
                    isWindowShow = WindowState.SELECT_COUNTRY
                })
                CustomTextField(
                    folderState,
                    "Enter the Folder Name",
                    Modifier.height(80.dp),
                ) { folderState = it }
                CustomButton("Convert",
                    isEnable = stringState.isNotEmpty() && folderState.isNotEmpty() && countryListState.any { it.isChecked },
                    onClick = {
                        isWindowShow = WindowState.CONVERT_TRANSLATE
                    }
                )
            }
        }
        when (isWindowShow) {
            WindowState.SELECT_COUNTRY -> {
                SelectCountries(countryListState) {
                    countryListState = it
                    isWindowShow = WindowState.NO_STATE
                }
            }

            WindowState.CONVERT_TRANSLATE -> {
                translateDialog(countryListState.filter { it.isChecked }, folderState,stringState, { errorMsg ->
                    isShowToast = true
                    toastMessage = errorMsg
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(3000)
                        isShowToast = false
                    }
                }, {
                    isWindowShow = WindowState.NO_STATE
                })
            }

            else -> {

            }
        }
        if (isShowToast) {
            Toast(toastMessage) {
                isShowToast = false
            }
        }

    }
}