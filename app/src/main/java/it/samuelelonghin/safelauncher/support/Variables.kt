package it.samuelelonghin.safelauncher.support

import android.content.SharedPreferences
import android.graphics.Bitmap
import it.samuelelonghin.safelauncher.drawer.apps.AppInfo
import it.samuelelonghin.safelauncher.home.contacts.ContactInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo

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

var widgetNumberColumns: Int = 0
var widgetNumberRows: Int = 0
var contactsNumberColumns: Int = 0

var widgetsList: MutableList<WidgetInfo> = mutableListOf()

