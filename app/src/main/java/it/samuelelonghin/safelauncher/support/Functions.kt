package it.samuelelonghin.safelauncher.support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.list.apps.AppInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    val allApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    for (ri in allApps) {
        if (ri.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
            val app = AppInfo()
            app.label = ri.loadLabel(packageManager)
            app.packageName = ri.packageName
            app.icon = ri.loadIcon(packageManager)
            loadList.add(app)
        }
    }
    loadList.sortBy { it.label.toString() }

    appsList.clear()
    appsList.addAll(loadList)
}

fun getAppInfo(context: Context, packageName: String): ApplicationInfo {

    val packageManager = context.packageManager;
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.ApplicationInfoFlags.of(0)
        )
    } else {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        )
    }
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
//    widgetNumberColumns = launcherPreferences.getInt(WIDGET_NUMBER_COLUMNS, 3)
//    widgetNumberRows = launcherPreferences.getInt(WIDGET_NUMBER_ROWS, 2)
//    contactsNumberColumns = launcherPreferences.getInt(CONTACTS_NUMBER_COLUMNS, 2)

}

fun updatePreference(key: String, value: Int) {
    val editor = launcherPreferences.edit()
    editor.putInt(key, value)
    editor.apply()
}

fun updatePreference(key: String, value: String) {
    val editor = launcherPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun updatePreference(key: String, value: Boolean) {
    val editor = launcherPreferences.edit()
    editor.putBoolean(key, value)
    editor.apply()
}

@kotlinx.serialization.Serializable
data class WidgetListWrapper(
    private val list: MutableList<WidgetSerial>
)


fun serializeWidgets(widgets: MutableList<WidgetSerial>): String {
    val widgetListWrapper = WidgetListWrapper(widgetsList)
    return Json.encodeToString(widgetsList)
//    return Json.encodeToString(widgetListWrapper)
}

fun deserializeWidgets(serialisedList: String): MutableList<WidgetSerial> {
    return Json.decodeFromString<MutableList<WidgetSerial>>(serialisedList)
//    return Json.decodeFromString<WidgetListWrapper>(serialisedList)
}

fun setWidgetListItem(position: Int, ws: WidgetSerial) {
    if (widgetsList.size <= position) widgetsList.add(ws)
    else widgetsList[position] = ws
    saveWidgets()
}

fun loadWidgets() {
    val json = launcherPreferences.getString(WIDGETS_LIST, """[]""")
    widgetsList = deserializeWidgets(json!!)
    System.err.println("WIDGETS CARICATI CORRETTAMENTE, lunghezza: ${widgetsList.size}")
}

fun saveWidgets() {
    val json = serializeWidgets(widgetsList)
    updatePreference(WIDGETS_LIST, json)
    System.err.println("WIDGETS SALVATI CORRETTAMENTE, lunghezza: ${widgetsList.size}")
}


private fun getIntent(packageName: String, context: Context): Intent? {
    val intent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)
    intent?.addCategory(Intent.CATEGORY_LAUNCHER)
    return intent
}

fun isInstalled(uri: String, context: Context): Boolean {
    if (uri.startsWith("safeLauncher:")) return true // All internal actions

    try {
        context.packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        return true
    } catch (_: PackageManager.NameNotFoundException) {
    }
    return false
}


fun launch(
    data: String, activity: Activity,
    animationIn: Int = android.R.anim.fade_in, animationOut: Int = android.R.anim.fade_out
) {

    if (data.startsWith("launcher:")) // [type]:[info]
        when (data.split(":")[1]) {
//            "settings" -> openSettings(activity)
//            "choose" -> openAppsList(activity)
//            "volumeUp" -> audioVolumeUp(activity)
//            "volumeDown" -> audioVolumeDown(activity)
//            "nextTrack" -> audioNextTrack(activity)
//            "previousTrack" -> audioPreviousTrack(activity)
//            "tutorial" -> openTutorial(activity)
        }
    else launchApp(data, activity) // app

    activity.overridePendingTransition(animationIn, animationOut)
}

fun launchApp(packageName: String, context: Context) {
    val intent = getIntent(packageName, context)

    if (intent != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(
            context,
            "Non riesco ad aprire l'app scelta",
            Toast.LENGTH_SHORT
        ).show()
//        if (isInstalled(packageName, context)) {
//
//            AlertDialog.Builder(
//                context,
////                R.style.AlertDialogCustom
//            )
//                .setTitle(context.getString(R.string.alert_cant_open_title))
//                .setMessage(context.getString(R.string.alert_cant_open_message))
//                .setPositiveButton(android.R.string.yes,
//                    DialogInterface.OnClickListener { dialog, which ->
//                        openAppSettings(
//                            packageName,
//                            context
//                        )
//                    })
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .show()
//        } else {
//            Toast.makeText(
//                context,
//                context.getString(R.string.toast_cant_open_message),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }
}


// Taken form https://stackoverflow.com/a/50743764/12787264
fun openSoftKeyboard(context: Context, view: View) {
    view.requestFocus()
    // open the soft keyboard
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}