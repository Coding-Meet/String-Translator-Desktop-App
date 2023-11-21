package models

data class Language(
    val code: String,
    val name: String,
    var isChecked: Boolean = false
)