package it.samuelelonghin.safelauncher.support

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import it.samuelelonghin.safelauncher.list.apps.AppInfo
import it.samuelelonghin.safelauncher.home.contacts.ContactInfo
import it.samuelelonghin.safelauncher.home.contacts.ContactInfoPlaceholder
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial

lateinit var launcherPreferences: SharedPreferences

/* Objects used by multiple activities */
val appsList: MutableList<AppInfo> = ArrayList()
val contactsList: MutableMap<String, ContactInfoPlaceholder> = mutableMapOf()
var widgetsList: MutableList<WidgetSerial> = mutableListOf()

var background: Bitmap? = null

var dominantColor = 0
var vibrantColor = 0

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