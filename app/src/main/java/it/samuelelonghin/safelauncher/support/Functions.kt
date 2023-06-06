package it.samuelelonghin.safelauncher.support

import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial
import it.samuelelonghin.safelauncher.list.ListActivity
import it.samuelelonghin.safelauncher.list.apps.AppInfo
import it.samuelelonghin.safelauncher.notification.NotificationListener
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable


@Suppress("DEPRECATION", "UNCHECKED_CAST")
fun <T : Serializable?> getSerializable(intent: Intent, name: String, clazz: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getSerializableExtra(
        name, clazz
    )!!
    else intent.getSerializableExtra(name) as T
}

fun canCall(activity: Activity): Boolean {
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

fun canFullScreen(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}

fun canChangeNotificationPolicy(context: Context): Boolean {
    return (context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager).isNotificationPolicyAccessGranted
}

fun canReceiveNotifications(context: Context): Boolean {
    try {

        val cn = ComponentName(context, NotificationListener::class.java)
        val flat: String =
            Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat.contains(cn.flattenToString())
    } catch (e: Exception) {
        System.err.println(e)
        return false
    }
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


fun loadApps(packageManager: PackageManager) {
    val loadList = mutableListOf<AppInfo>()

    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    for (ri in allApps) {
        if (ri.sourceDir.startsWith("/data/app/")) {
            val app = AppInfo()
            app.label = ri.loadLabel(packageManager)
            app.packageName = ri.packageName
            app.icon = ri.loadIcon(packageManager)
            loadList.add(app)
        } else println("DIR: " + ri.sourceDir)
    }
    loadList.sortBy { it.label.toString() }

    appsList.clear()
    appsList.addAll(loadList)
}

fun getAppInfo(context: Context, packageName: String): ApplicationInfo {

    val packageManager = context.packageManager;
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            packageName, PackageManager.ApplicationInfoFlags.of(0)
        )
    } else {
        packageManager.getApplicationInfo(
            packageName, PackageManager.GET_META_DATA
        )
    }
}

fun loadPreferences(activity: Activity) {
    launcherPreferences = activity.getSharedPreferences(
        activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE
    )
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


fun serializeWidgets(widgets: MutableList<WidgetSerial>): String {
    return Json.encodeToString(widgets)
}

fun deserializeWidgets(serialisedList: String): MutableList<WidgetSerial> {
    return Json.decodeFromString(serialisedList)
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

fun removeWidget(position: Int) {
    if (position < widgetsList.size) {
        widgetsList.removeAt(position)
        saveWidgets()
    } else System.err.println("Non posso rimuovere elemento $position dalla lista di widgets")
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int
): Int {
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attrColor))
    val textColor = typedArray.getColor(0, 0)
    typedArray.recycle()
    return textColor
}

private fun getIntent(packageName: String, context: Context): Intent? {
    val intent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)
    intent?.addCategory(Intent.CATEGORY_LAUNCHER)
    return intent
}

fun launch(
    data: String,
    activity: Activity,
    animationIn: Int = android.R.anim.fade_in,
    animationOut: Int = android.R.anim.fade_out
) {
    launchApp(data, activity)
    activity.overridePendingTransition(animationIn, animationOut)
}

fun launchActivity(
    activity: String, context: Context, selectApp: ActivityResultLauncher<Intent>? = null
) {
    if (activity == ACTIVITY_PICK) {
        if (selectApp != null) {
            val intent = Intent(context, ListActivity::class.java)
            intent.putExtra("intention", "pick")
            intent.putExtra("forApp", "0") // for which action we choose the app
            intent.putExtra("index", 100) // for which action we choose the app
            intendedSettingsPause = true
            selectApp.launch(intent)
        }
        Toast.makeText(context, "Non riesco a far partire Intento", Toast.LENGTH_SHORT).show()
    }
    context.startActivity(Intent(context, ACTIVITY_TO_CLASS[activity]))
}

fun launchApp(packageName: String, context: Context) {
    val intent = getIntent(packageName, context)
    if (intent != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(
            context, "Non riesco ad aprire l'app scelta", Toast.LENGTH_SHORT
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


fun setIconTintPrimary(context: Context, icon: Int): Drawable {
    val r = ResourcesCompat.getDrawable(
        context.resources, icon, context.theme
    )
    r!!.setTint(context.getColorFromAttr(android.R.attr.colorPrimary))
    return r
}

fun setIconTintSecondary(context: Context, icon: Int): Drawable {
    val r = ResourcesCompat.getDrawable(
        context.resources, icon, context.theme
    )
    r!!.setTint(context.getColorFromAttr(android.R.attr.colorBackground))
    return r
}


fun getBundleAsJson(bundle: Bundle): String {
    val json = JSONObject()
    for (key in bundle.keySet()) {
        try {
            // json.put(key, bundle.get(key)); see edit below
            json.put(key, JSONObject.wrap(bundle.get(key)))
        } catch (e: JSONException) {
            //Handle exception here
        }
    }
    return json.toString()
}

fun isMyAppLauncherDefault(context: Context): Boolean {
    val filter = IntentFilter(Intent.ACTION_MAIN)
    filter.addCategory(Intent.CATEGORY_HOME)
    val filters: MutableList<IntentFilter> = ArrayList()
    filters.add(filter)
    val myPackageName = context.packageName
    val activities: List<ComponentName> = ArrayList()
    val packageManager = context.packageManager as PackageManager
    packageManager.getPreferredActivities(filters, activities, null)
    for (activity in activities) {
        if (myPackageName == activity.packageName) {
            return true
        }
    }
    return false
}

fun askForChangeLauncher(context: Context) {
    val packageManager: PackageManager = context.packageManager
    val componentName = ComponentName(context, FakeActivity::class.java)
    packageManager.setComponentEnabledSetting(
        componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
    )

    val selector = Intent(Intent.ACTION_MAIN)
    selector.addCategory(Intent.CATEGORY_HOME)
    selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(selector)

    packageManager.setComponentEnabledSetting(
        componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP
    )
}