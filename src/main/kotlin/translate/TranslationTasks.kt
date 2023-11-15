package translate

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslationTasks(
    query: String,
    sourceLang: String,
    targetLang: String,
    private var resultCallback: (translation: String) -> Unit,
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val translated = translateHttpURLConnection(
                    query,
                    sourceLang,
                    targetLang,
                )

                withContext(Dispatchers.Main) {
                    resultCallback(translated)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}