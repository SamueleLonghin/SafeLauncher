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

fun getUserNotificationsCount(user: String): Int {
    var c = 0
    val n = getUserNotifications(user)
    for ((_, v) in n) {
        c += v.size
    }
    return c
}

fun removeNotification(user: String, app: String, no: Notification) {
    if (!notifiche.containsKey(user))
        return
    if (!notifiche[user]!!.containsKey(app))
        return
    notifiche[user]!![app]!!.remove(no)
}

fun removeUserAppNotifications(user: String, app: String) {
    if (!notifiche.containsKey(user))
        return
    if (!notifiche[user]!!.containsKey(app))
        return
    notifiche[user]!!.remove(app)
}

fun launchPendingIntent(pi: PendingIntent) {

    try {
        pi.send()
    } catch (e: Exception) {
        System.err.println(e)
    }
}