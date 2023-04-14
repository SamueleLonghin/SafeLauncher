package it.samuelelonghin.safelauncher.support

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.DisplayMetrics
import it.samuelelonghin.safelauncher.drawer.apps.AppInfo
import it.samuelelonghin.safelauncher.home.ContactInfo
import java.util.Dictionary

lateinit var launcherPreferences: SharedPreferences

/* Objects used by multiple activities */
val appsList: MutableList<AppInfo> = ArrayList()
val contactsList: MutableMap<String, ContactInfo> = mutableMapOf()

/* Variables containing settings */
val displayMetrics = DisplayMetrics()

var background: Bitmap? = null

var dominantColor = 0
var vibrantColor = 0

var intendedSettingsPause = false // know when to close


