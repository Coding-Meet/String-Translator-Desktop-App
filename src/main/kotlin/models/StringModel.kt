package models

data class StringModel(
    val name : String,
    val isTranslatable : Boolean = true,
    val textContent : String,
)