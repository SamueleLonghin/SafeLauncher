package it.samuelelonghin.safelauncher.support

import android.app.Notification
import android.app.PendingIntent

fun addNotification(user: String, app: String, no: Notification) {
    if (!notifiche.containsKey(user))
        notifiche[user] = mutableMapOf()
    if (!notifiche[user]!!.containsKey(app))
        notifiche[user]!![app] = mutableListOf()
    notifiche[user]!![app]!!.add(no)
}


fun getUserNotifications(user: String): MutableMap<String, MutableList<Notification>> {
    if (!notifiche.containsKey(user))
        return mutableMapOf()
    return notifiche[user]!!
}

/**
 * For notification opening
 */
fun launchNotification(notification: Notification) {
    launchPendingIntent(notification.contentIntent)
}

fun launchPendingIntent(pi: PendingIntent) {

    try {
        pi.send()
    } catch (e: Exception) {
        System.err.println(e)
    }
}