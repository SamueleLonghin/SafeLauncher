package it.samuelelonghin.safelauncher.notification

import android.app.Notification
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import it.samuelelonghin.safelauncher.support.BROADCAST_NOTIFICATIONS
import it.samuelelonghin.safelauncher.support.addNotification
import it.samuelelonghin.safelauncher.support.getBundleAsJson
import it.samuelelonghin.safelauncher.support.removeNotification


const val NOTIFICATION_USER_ID = Notification.EXTRA_TITLE //"android.title"


class NotificationListener : NotificationListenerService() {
    override fun onBind(intent: Intent): IBinder? {
        println("Servizio notifiche attaccato")
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        val json = getBundleAsJson(sbn.notification.extras)
        val app = getApp(sbn)
        val userid = getUser(sbn)

        if (app != null && app != "android" && userid != null) {
            addNotification(userid, app, sbn.notification)
            println("AGGIUNTA: $app $userid $json")
        }

        val intent = Intent(BROADCAST_NOTIFICATIONS)
        intent.putExtra("notification", sbn)
        intent.putExtra("target.user", userid)
        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)

//      launchNotification(sbn.notification)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Implement what you want here
//        val json = getBundleAsJson(sbn.notification.extras)
        val app = getApp(sbn)
        val userid = getUser(sbn)

        if (app != null && app != "android" && userid != null) {
            removeNotification(userid, app, sbn.notification)
            println("RIMOSSA: " + sbn.user.toString())
        }

        val intent = Intent(BROADCAST_NOTIFICATIONS)
        intent.putExtra("notification", sbn)
        intent.putExtra("target.user", userid)
        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)

    }

    private fun getApp(sbn: StatusBarNotification): String? {
        return sbn.packageName
    }

    private fun getUser(sbn: StatusBarNotification): String? {
        if (sbn.notification.extras.containsKey(NOTIFICATION_USER_ID))
            return sbn.notification.extras.getCharSequence(NOTIFICATION_USER_ID).toString()
        return null
    }

    private fun getIntent(sbn: StatusBarNotification): String? {
        if (sbn.notification.extras.containsKey(NOTIFICATION_USER_ID))
            return sbn.notification.extras.getCharSequence(NOTIFICATION_USER_ID).toString()
        return null
    }
}