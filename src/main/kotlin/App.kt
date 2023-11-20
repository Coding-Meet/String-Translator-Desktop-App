
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import translate.translateHttpURLConnection
import utils.createOutputMainFolder
import utils.createStringFolder
import utils.defaultLanguageCode
import utils.endResourcesWrite
import utils.languageList
import utils.readAllStrings
import utils.startResourcesWrite
import utils.stringWrite
import java.net.URLEncoder

@Composable
@Preview
fun App() {

    var isShowWindowState by remember { mutableStateOf(false) }
    var stringState by remember {
        mutableStateOf(
            """
<resources>
    <string name="video_directory" translatable="false">Video folder</string>
    <string name="extract_audio">Hello Meet</string>
    <string name="extract">How are You?</string>
</resources>
    """.trimIndent()
        )
    }
//      <string name="create_thumbnail">Save thumbnail</string>
//    <string name="settings">Settings</string>
//    <string name="download_settings_desc" translatable="false">General, format, custom command</string>
//    <string name="download">Download</string>
//    <string name="url_empty">The link cannot be empty</string>
//    <string name="extract_audio_summary">Download and save audio, instead of video</string>
//    <string name="create_thumbnail_summary" translatable="false">Save video thumbnail as a file</string>
//    <string name="yt_dlp_up_to_date">Using the latest version of yt-dlp</string>

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
                    isShowWindowState = true
                })
                CustomButton("Convert",
                    isEnable = stringState.isNotEmpty() && countryListState.any { it.isChecked },
                    onClick = {
                        createOutputMainFolder()
                        try {
                            val allStrings = readAllStrings(stringState)

                            println(countryListState.filter { it.isChecked })
                            countryListState.filter { it.isChecked }
                                .forEachIndexed { index, language ->
                                    val stringFile =
                                        createStringFolder(language.code)
                                    stringFile.startResourcesWrite()
                                    for ((name, isTranslatable, textContent) in allStrings) {
                                        println("Name: $name, isTranslatable: $isTranslatable, Text Content: $textContent")
                                        if (isTranslatable) {

//                                            stringFile.stringWrite(name,  someConvertText.random())
                                            val convertedString = translateHttpURLConnection(
                                                URLEncoder.encode(textContent, "UTF-8"),
                                                URLEncoder.encode(defaultLanguageCode, "UTF-8"),
                                                URLEncoder.encode(language.code, "UTF-8"),
                                            ) {
                                                isShowToast = true
                                                toastMessage = it
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    delay(3000)
                                                    isShowToast = false
                                                }
                                            }
                                            stringFile.stringWrite(name, convertedString)
                                        } else {
                                            stringFile.stringWrite(name, textContent, false)
                                        }
                                    }
                                    stringFile.endResourcesWrite()
                                }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })
            }
        }
        if (isShowWindowState) {
            SelectCountries(countryListState) {
                countryListState = it
                isShowWindowState = false
            }
        }
        if (isShowToast) {
            Toast(toastMessage) {}
        }

    }
}

//fun showToast() {
//    isShowToast = true
//    toastMessage = "This is a toast message!"
//    // Delay to simulate a real-world scenario
//    CoroutineScope(Dispatchers.IO).launch {
//        delay(3000)
//        isShowToast = false
//    }
//}
