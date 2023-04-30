package it.samuelelonghin.safelauncher.support

import android.content.SharedPreferences
import android.graphics.Bitmap
import it.samuelelonghin.safelauncher.drawer.AppInfo
import it.samuelelonghin.safelauncher.home.contacts.ContactInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial

lateinit var launcherPreferences: SharedPreferences

/* Objects used by multiple activities */
val appsList: MutableList<AppInfo> = ArrayList()
val contactsList: MutableMap<String, ContactInfo> = mutableMapOf()


var background: Bitmap? = null

var dominantColor = 0
var vibrantColor = 0

var intendedSettingsPause = false // know when to close


/**
 * PREFERENCES
 */

var widgetsList: MutableList<WidgetSerial> = mutableListOf()

