package it.samuelelonghin.safelauncher.drawer

import android.graphics.drawable.Drawable

/**
 * Stores information used to create [AppsAdapter] rows.
 *
 * Represents an app installed on the users device.
 */
class AppInfo {
    var label: CharSequence? = null
    var packageName: CharSequence? = null
    var activity: Class<*>? = null
    var icon: Drawable? = null
    var isSystemApp: Boolean = false
}