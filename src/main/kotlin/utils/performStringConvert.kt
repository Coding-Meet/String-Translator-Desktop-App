package utils

import models.StringModel
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun readAllStrings(xmlString: String): ArrayList<StringModel> {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val document = builder.parse(xmlString.byteInputStream())

    val result = arrayListOf<StringModel>()

    val stringElements = document.getElementsByTagName("string")
    for (i in 0 until stringElements.length) {
        val element = stringElements.item(i) as Element
        val name = element.getAttribute("name")
        val translatable = element.getAttribute("translatable")
        val textContent = element.textContent
        val isTranslatable = !translatable.equals("false", true)
        result.add(StringModel(name, isTranslatable, textContent))

    }

    return result
}


fun createOutputMainFolder(folderName:String) {
    File("output").mkdir()
    File("./output/$folderName").deleteRecursively()
    File("./output/$folderName").mkdir()
}

fun createStringFolder(folderName:String, languageCode: String): File {
    File("./output/$folderName/values-$languageCode").mkdir()
    val file = File("./output/$folderName/values-$languageCode/strings.xml")
    if (!file.exists()) file.createNewFile()
    return file
}

fun File.stringWrite(name: String, convertedString: String, isTranslatable: Boolean = true) {
    if (isTranslatable) {
        appendText(
            """
    <string name="$name">$convertedString</string>"""
        )
    } else {
        appendText(
            """
    <string name="$name" translatable="false">$convertedString</string>"""
        )
    }
}

fun File.startResourcesWrite() {
    writeText("<resources>")
}

fun File.endResourcesWrite() {
    appendText("\n</resources>")
}