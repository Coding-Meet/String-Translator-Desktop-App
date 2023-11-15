import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import components.*
import utils.languageList

@Composable
@Preview
fun App() {

    var isShowWindowState by remember { mutableStateOf(false) }
    var stringState by remember { mutableStateOf("") }
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
                        println(countryListState.filter { it.isChecked })
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

fun showToast() {
//    isShowToast = true
//    toastMessage = "This is a toast message!"
//    // Delay to simulate a real-world scenario
//    CoroutineScope(Dispatchers.IO).launch {
//        delay(3000)
//        isShowToast = false
//    }
}
