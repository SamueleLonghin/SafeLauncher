package it.samuelelonghin.safelauncher.info

import android.graphics.drawable.Icon

data class InfoNotification(
    val app: String,
    val name: String,
    val content: String,
    val icon: Icon
)