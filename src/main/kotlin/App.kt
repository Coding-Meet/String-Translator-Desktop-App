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
import utils.languageList

@Composable
@Preview
fun App() {

    var showToast by remember { mutableStateOf(false) }
    var inputDependencyText by remember { mutableStateOf("") }
    var countryListState by remember { mutableStateOf(languageList) }
  
    MaterialTheme {
        Row(
            Modifier.fillMaxSize()
                .padding(10.dp)
        ) {
            CustomTextField(
                inputDependencyText,
                "Enter the Strings",
                Modifier.fillMaxHeight().weight(0.8f, true),
            ) { inputDependencyText = it }

            Column(
                Modifier.weight(0.2f, true).padding(start = 10.dp)
            ) {
                CustomButton("Select Languages", onClick = {
                    showToast = true
                })
                CustomButton("Convert", isEnable = inputDependencyText.isNotEmpty() && countryListState.any { it.isChecked }, onClick = {
                    println(countryListState.filter { it.isChecked })
                })
            }
        }
        if (showToast) {
            SelectCountries(countryListState){
//                countryListState = it
                showToast = false
            }

        }

    }
}

