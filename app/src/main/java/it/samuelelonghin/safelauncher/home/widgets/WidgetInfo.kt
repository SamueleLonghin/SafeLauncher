package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import java.io.Serializable


class WidgetInfo(name: String, type: WidgetType, icon: Int) : Serializable {
    var name: String
    var type: WidgetType
    var icon: Int
    private var app: String? = null
    private var uri: Uri? = null
    private var activity: Class<*>? = null

    enum class WidgetType {
        APP,
        ACTIVITY,
        ACTION
    }

    init {
        this.name = name
        this.type = type
        this.icon = icon
    }

    constructor(name: String, type: WidgetType, icon: Int, uri: Uri) : this(name, type, icon) {
        this.uri = uri
    }

    constructor(name: String, type: WidgetType, icon: Int, app: String) : this(name, type, icon) {
        this.app = app
    }

    constructor(name: String, type: WidgetType, icon: Int, activity: Class<*>) : this(
        name,
        type,
        icon
    ) {
        this.activity = activity
    }

    fun onClick(context: Context) {
        when (type) {
            WidgetType.APP -> {

                val pm: PackageManager = context.packageManager
                val launchIntent = pm.getLaunchIntentForPackage(app!!)
                context.startActivity(launchIntent)
            }
            WidgetType.ACTIVITY -> {
                context.startActivity(Intent(context, activity))
            }
            WidgetType.ACTION -> {

            }
        }
    }

}