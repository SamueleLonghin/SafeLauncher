package it.samuelelonghin.safelauncher.view_contact

import android.app.Notification
import android.app.PendingIntent

data class InfoNotification(
    val app: String?,
    val title: String,
    val user: String,
    val content: String? = null,
) {
    var intent: PendingIntent? = null

    constructor(pkg: String, user: String, notificationsList: MutableList<Notification>) : this(
        pkg,
        "${notificationsList.size} Messaggi",
        user,
        null,
    ) {
        if (notificationsList.isNotEmpty()) {
            intent = notificationsList.first().contentIntent
        }
    }
}