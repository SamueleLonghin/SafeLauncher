package it.samuelelonghin.safelauncher.support

import android.app.Notification
import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.result.ActivityResultLauncher
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial
import it.samuelelonghin.safelauncher.list.apps.AppInfo

lateinit var launcherPreferences: SharedPreferences

/* Objects used by multiple activities */
val appsList: MutableList<AppInfo> = ArrayList()
//val contactsList: MutableMap<String, ContactInfoPlaceholder> = mutableMapOf()
var widgetsList: MutableList<WidgetSerial> = mutableListOf()

var intendedSettingsPause = false // know when to close



/***
 * Activity results
 */

lateinit var activityResultNotificationPolicy: ActivityResultLauncher<Intent>
lateinit var activityResultNotificationAccess: ActivityResultLauncher<Intent>
lateinit var setAuth: ActivityResultLauncher<Intent>
lateinit var activityResultEnableFullScreen: ActivityResultLauncher<Intent>


/**
 * Mappa       Utente -> App -> Notifiche[]
 */
var notifiche = mutableMapOf<String, MutableMap<String, MutableList<Notification>>>()