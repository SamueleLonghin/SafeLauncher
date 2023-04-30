package it.samuelelonghin.safelauncher.home.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import it.samuelelonghin.safelauncher.home.widgets.WidgetFragment.Mode
import java.io.Serializable


class WidgetInfo(name: String, type: WidgetType, icon: Int, mode: Mode) : Serializable {
    var name: String
    var icon: Int
    private var type: WidgetType
    private var mode: Mode
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
        this.mode = mode
    }

    constructor(name: String, type: WidgetType, icon: Int, mode: Mode, uri: Uri) : this(
        name,
        type,
        icon,
        mode
    ) {
        this.uri = uri
    }

    constructor(name: String, type: WidgetType, icon: Int, mode: Mode, app: String) : this(
        name,
        type,
        icon,
        mode
    ) {
        this.app = app
    }

    constructor(name: String, type: WidgetType, icon: Int, mode: Mode, activity: Class<*>) : this(
        name,
        type,
        icon,
        mode
    ) {
        this.activity = activity
    }


    fun onClick(context: Context) {
        if (mode == Mode.USE)
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
        else {
            println("CLiccato " + this.name)
        }
    }

}