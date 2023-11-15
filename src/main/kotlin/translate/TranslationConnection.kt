package translate


import org.json.*
import java.io.*
import java.net.*

const val charset = "UTF-8"

private fun getTextHttpURLConnection(url: String): String {
    var connection: HttpURLConnection? = null
    val response = StringBuilder()
    try {
        connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("Accept-Charset", charset)
        connection.addRequestProperty(
            "User-Agent",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"
        )
        val `in` = BufferedReader(
            InputStreamReader(
                connection.inputStream, charset
            )
        )
        while (true) {
            val inputLine = `in`.readLine()
            if (inputLine == null) {
                `in`.close()
                return response.toString()
            }
            response.append(inputLine)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.disconnect()
    }
    return response.toString()
}

fun translateHttpURLConnection(
    query: String,
    sourceLang: String,
    targetLang: String,
): String {
    try {
        try {
            val sb = java.lang.StringBuilder()
            var responseText =
                getTextHttpURLConnection(
                    String.format(
                        "https://translate.google.com/translate_a/single?&client=gtx&sl=%s&tl=%s&q=%s&dt=t",
                        sourceLang,
                        targetLang,
                        query
                    )
                )
            if (responseText.isEmpty()) {
                responseText =
                    getTextHttpURLConnection(
                        String.format(
                            "https://clients4.google.com/translate_a/t?client=dict-chrome-ex&sl=%s&tl=%s&q=%s&dt=t",
                            sourceLang,
                            targetLang,
                            query
                        )
                    )
                if (responseText.isEmpty()) {
                    responseText =
                        getTextHttpURLConnection(
                            String.format(
                                "https://translate.google.com/m?sl=%s&tl=%s&q=%s",
                                sourceLang,
                                targetLang,
                                query
                            )
                        )
                    if (responseText.isEmpty()) {
                        sb.append(translateURLConnection(sourceLang, targetLang, query))
                    } else {
                        sb.append(getTranslationData(responseText))
                    }
                } else {
                    val jsonObject = JSONObject(responseText)
                    if (jsonObject.has("sentences")) {
                        val jsonArray = jsonObject.getJSONArray("sentences")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null && jsonObject1.has("trans")) {
                                sb.append(jsonObject1.getString("trans"))
                            }
                        }
                    }
                }
            } else {
                val jSONArray = JSONArray(responseText).getJSONArray(0)
                for (i in 0 until jSONArray.length()) {
                    val string = jSONArray.getJSONArray(i).getString(0)
                    if (string.isNotEmpty() && string != "null") {
                        sb.append(string)
                    }
                }
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

private fun translateURLConnection(sourceLang: String, targetLang: String, query: String): String {
    try {
        try {
            val sb = StringBuilder()
            var text =
                getTextUrlConnection(
                    String.format(
                        "https://translate.google.com/translate_a/single?&client=gtx&sl=%s&tl=%s&q=%s&dt=t",
                        sourceLang,
                        targetLang,
                        query
                    )
                )
            if (text.isEmpty()) {
                text =
                    getTextUrlConnection(
                        String.format(
                            "https://clients4.google.com/translate_a/t?client=dict-chrome-ex&sl=%s&tl=%s&q=%s&dt=t",
                            sourceLang,
                            targetLang,
                            query
                        )
                    )
                if (text.isEmpty()) {
                    text =
                        getTextUrlConnection(
                            String.format(
                                "https://translate.google.com/m?sl=%s&tl=%s&q=%s",
                                sourceLang,
                                targetLang,
                                query
                            )
                        )
                    if (text.isEmpty()) {
                        println("Error: "+ "Url Failed")
                    } else {
                        sb.append(getTranslationData(text))
                    }
                } else {
                    val jsonObject = JSONObject(text)
                    if (jsonObject.has("sentences")) {
                        val jsonArray = jsonObject.getJSONArray("sentences")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null && jsonObject1.has("trans")) {
                                sb.append(jsonObject1.getString("trans"))
                            }
                        }
                    }
                }
            } else {
                val jSONArray = JSONArray(text).getJSONArray(0)
                for (i in 0 until jSONArray.length()) {
                    val string = jSONArray.getJSONArray(i).getString(0)
                    if (string.isNotEmpty() && string != "null") {
                        sb.append(string)
                    }
                }
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

private fun getTextUrlConnection(url: String): String {
    try {
        val connection = URL(url).openConnection()
        connection.setRequestProperty("Accept-Charset", charset)
        connection.addRequestProperty(
            "User-Agent",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"
        )
        val `in` = BufferedReader(InputStreamReader(connection.getInputStream(), charset))
        val response = java.lang.StringBuilder()
        while (true) {
            val inputLine = `in`.readLine()
            if (inputLine == null) {
                `in`.close()
                return response.toString()
            }
            response.append(inputLine)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun getTranslationData(responseText: String): String {
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
    }
    return ""
}
