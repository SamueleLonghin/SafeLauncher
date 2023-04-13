package it.samuelelonghin.safelauncher.support

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.DisplayMetrics
import it.samuelelonghin.safelauncher.drawer.apps.AppInfo

lateinit var launcherPreferences: SharedPreferences

/* Objects used by multiple activities */
val appsList: MutableList<AppInfo> = ArrayList()

/* Variables containing settings */
val displayMetrics = DisplayMetrics()

var background : Bitmap? = null

var dominantColor = 0
var vibrantColor = 0



