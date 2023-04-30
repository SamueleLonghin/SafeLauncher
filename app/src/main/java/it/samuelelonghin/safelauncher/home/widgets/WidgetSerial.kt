package it.samuelelonghin.safelauncher.home.widgets

@kotlinx.serialization.Serializable
data class WidgetSerial(
    val index: Int,
    val value: String,
    val type: WidgetInfo.WidgetType,
)