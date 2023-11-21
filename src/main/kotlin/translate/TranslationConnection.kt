package translate


import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private fun getTextHttpURLConnection(url: String, onErrorCallback: (String) -> Unit): String {
    var response = ""
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        val inputStream = connection.inputStream
        response = inputStream.bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        e.printStackTrace()
        onErrorCallback(e.message.toString())
    }
    return response
}

fun translateHttpURLConnection(
    query: String,
    sourceLang: String,
    targetLang: String,
    onSuccessCallback: (String) -> Unit,
    onErrorCallback: (String) -> Unit,
    isFirstTime: Boolean = true,
) {
    try {
        val convertedStringB = StringBuilder()
        var responseText =
            getTextHttpURLConnection(
                String.format(
                    "https://translate.google.com/translate_a/single?&client=gtx&sl=%s&tl=%s&q=%s&dt=t",
                    sourceLang,
                    targetLang,
                    query
                ),
                onErrorCallback
            )
        if (responseText.isEmpty()) {
            responseText =
                getTextHttpURLConnection(
                    String.format(
                        "https://clients4.google.com/translate_a/t?client=dict-chrome-ex&sl=%s&tl=%s&q=%s&dt=t",
                        sourceLang,
                        targetLang,
                        query
                    ),
                    onErrorCallback
                )
            if (responseText.isEmpty()) {
                responseText =
                    getTextHttpURLConnection(
                        String.format(
                            "https://translate.google.com/m?sl=%s&tl=%s&q=%s",
                            sourceLang,
                            targetLang,
                            query
                        ),
                        onErrorCallback
                    )
                if (responseText.isEmpty()) {
                    if (isFirstTime) {
                        convertedStringB.append(
                            translateHttpURLConnection(
                                sourceLang,
                                targetLang,
                                query,
                                onSuccessCallback,
                                onErrorCallback,
                                false,
                            )
                        )
                    } else {
                        println("Error" + "Url Failed")
                    }
                } else {
                    convertedStringB.append(getTranslationData(responseText, onErrorCallback))
                }
            } else {
                convertedStringB.append(getJsonObjectResponseToString(responseText))
            }
        } else {
            convertedStringB.append(getJsonArrayResponseToString(responseText))
        }
        return onSuccessCallback(convertedStringB.toString())
    } catch (e: Exception) {
        e.printStackTrace()
        onErrorCallback(e.message.toString())
    }
    return onSuccessCallback("")
}

private fun getJsonObjectResponseToString(responseText: String): String {
    val sb = StringBuilder()
    val jsonObject = JSONObject(responseText)
    if (jsonObject.has("sentences")) {
        val jsonArray = jsonObject.getJSONArray("sentences")
        for (i in 0 until jsonArray.length()) {
            val jsonObject1 = jsonArray.getJSONObject(i)
            if (jsonObject1.has("trans")) {
                sb.append(jsonObject1.getString("trans"))
            }
        }
    }
    return sb.toString()
}

private fun getJsonArrayResponseToString(responseText: String): String {
    val sb = StringBuilder()
    val jSONArray = JSONArray(responseText).getJSONArray(0)
    for (i in 0 until jSONArray.length()) {
        val string = jSONArray.getJSONArray(i).getString(0)
        if (string.isNotEmpty() && string != "null") {
            sb.append(string)
        }
    }
    return sb.toString()
}

fun getTranslationData(responseText: String, onErrorCallback: (String) -> Unit): String {
    try {
        var nativeText = "class=\"t0\">"
        val result =
            responseText.substring(responseText.indexOf(nativeText) + nativeText.length)
                .split("<".toRegex()).toTypedArray()[0]
        return if (result == "html>") {
            nativeText = "class=\"result-container\">"
            responseText.substring(responseText.indexOf(nativeText) + nativeText.length)
                .split("<".toRegex()).toTypedArray()[0] + "+" + ""
        } else {
            result
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onErrorCallback(e.message.toString())
    }
    return ""
}
