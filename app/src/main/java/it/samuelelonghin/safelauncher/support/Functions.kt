package it.samuelelonghin.safelauncher.support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.drawer.apps.AppInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull.serializer
import java.io.Serializable

fun <T : Serializable?> getSerializable(intent: Intent, name: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getSerializableExtra(
        name,
        clazz
    )!!
    else intent.getSerializableExtra(name) as T
}

fun checkUserCanCall(activity: Activity): Boolean {
    if (ContextCompat.checkSelfPermission(
            activity, android.Manifest.permission.CALL_PHONE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(android.Manifest.permission.CALL_PHONE), 100
        )
        return false
    }
    return true
}

fun setWindowFlags(window: Window) {
    window.setFlags(0, 0) // clear flags

    // Display notification bar
    if (launcherPreferences.getBoolean(PREF_SCREEN_FULLSCREEN, true)) window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    else window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

    // Screen Timeout
    if (launcherPreferences.getBoolean(PREF_SCREEN_TIMEOUT_DISABLED, false)) window.setFlags(
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
    )
    else window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

/**
 * [loadApps] is used to speed up the [AppsRecyclerAdapter] loading time,
 * as it caches all the apps and allows for fast access to the data.
 */
fun loadApps(packageManager: PackageManager) {
    val loadList = mutableListOf<AppInfo>()

    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = packageManager.queryIntentActivities(i, 0)
    for (ri in allApps) {
        val app = AppInfo()
        app.label = ri.loadLabel(packageManager)
        app.packageName = ri.activityInfo.packageName
        app.icon = ri.activityInfo.loadIcon(packageManager)
        loadList.add(app)
    }
    loadList.sortBy { it.label.toString() }

    appsList.clear()
    appsList.addAll(loadList)
}

fun resetSettings(context: Context) {

    val editor = launcherPreferences.edit()

    // set default theme
    dominantColor = context.resources.getColor(R.color.background_color)
    vibrantColor = context.resources.getColor(R.color.accent_color)

    editor.putString(PREF_WALLPAPER, "").putInt(PREF_DOMINANT, dominantColor)
        .putInt(PREF_VIBRANT, vibrantColor).putString(PREF_THEME, "light")
        .putBoolean(PREF_SCREEN_TIMEOUT_DISABLED, false).putBoolean(PREF_SEARCH_AUTO_LAUNCH, false)
        .putInt(PREF_DATE_FORMAT, 0).putBoolean(PREF_SCREEN_FULLSCREEN, true)
        .putBoolean(PREF_DOUBLE_ACTIONS_ENABLED, false).putInt(PREF_SLIDE_SENSITIVITY, 50)

    //TODO prendere altre cose da pref
    editor.apply()
}


/* Settings related functions */

fun getSavedTheme(context: Context): String {
    return launcherPreferences.getString(PREF_THEME, "light").toString()
}

fun saveTheme(themeName: String): String {
    launcherPreferences.edit().putString(PREF_THEME, themeName).apply()

    return themeName
}

// Used in Tutorial and Settings `ActivityOnResult`
fun saveListActivityChoice(data: Intent?) {
    val value = data?.getStringExtra("value")
    val forApp = data?.getStringExtra("forApp") ?: return

    launcherPreferences.edit().putString("action_$forApp", value.toString()).apply()
}


fun resetToDefaultTheme(activity: Activity) {
    dominantColor = activity.resources.getColor(R.color.background_color)
    vibrantColor = activity.resources.getColor(R.color.accent_color)

    launcherPreferences.edit().putString(PREF_WALLPAPER, "").putInt(PREF_DOMINANT, dominantColor)
        .putInt(PREF_VIBRANT, vibrantColor).apply()

    saveTheme("light")

    intendedSettingsPause = true
    activity.recreate()
}

fun resetToDarkTheme(activity: Activity) {
    dominantColor = activity.resources.getColor(R.color.background_color)
    vibrantColor = activity.resources.getColor(R.color.accent_color)

    launcherPreferences.edit().putString(PREF_WALLPAPER, "").putInt(PREF_DOMINANT, dominantColor)
        .putInt(PREF_VIBRANT, vibrantColor).apply()

    saveTheme("dark")

    intendedSettingsPause = true
    activity.recreate()
}


fun loadPreferences(activity: Activity) {
    launcherPreferences = activity.getSharedPreferences(
        activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    )
    widgetNumberColumns = launcherPreferences.getInt(WIDGET_NUMBER_COLUMNS, 3)
    widgetNumberRows = launcherPreferences.getInt(WIDGET_NUMBER_ROWS, 2)
    contactsNumberColumns = launcherPreferences.getInt(CONTACTS_NUMBER_COLUMNS, 2)

}

class WidgetListWrapper(list: MutableList<WidgetInfo>) : Serializable {
    private val list: MutableList<WidgetInfo>

    init {
        this.list = list
    }

    fun getList(): MutableList<WidgetInfo> {
        return list
    }
}

fun serializeWidgets(widgets: MutableList<WidgetInfo>): String {
    val widgetListWrapper = WidgetListWrapper(widgetsList)
    return Json.encodeToString(widgetListWrapper)
}

fun deserializeWidgets(serialisedList: String): MutableList<WidgetInfo> {
    return Json.decodeFromString<WidgetListWrapper>(serialisedList).getList()
}

fun loadWidgets() {
    val json = launcherPreferences.getString(WIDGETS_LIST, """{"list":[]}""")
    widgetsList = deserializeWidgets(json!!)
}

fun saveWidgets() {
    val editor = launcherPreferences.edit()
    val json = serializeWidgets(widgetsList)
    editor.putString(WIDGETS_LIST, json)
    editor.apply()
}