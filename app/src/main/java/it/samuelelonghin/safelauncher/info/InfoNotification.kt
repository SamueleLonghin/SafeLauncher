package it.samuelelonghin.safelauncher.info

import android.app.Notification
import android.app.PendingIntent

data class InfoNotification(
    val app: String?,
    val title: String,
    val content: String? = null,
) {
    var intent: PendingIntent? = null

    constructor(pkg: String, notificationsList: MutableList<Notification>) : this(
        pkg,
        "${notificationsList.size} Messaggi",
        null,
    ) {
        if (notificationsList.isNotEmpty()) {
            intent = notificationsList.first().contentIntent
        }
    }
}