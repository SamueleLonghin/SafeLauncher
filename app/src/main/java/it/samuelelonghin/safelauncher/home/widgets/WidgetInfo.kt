package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import it.samuelelonghin.safelauncher.home.widgets.actions.Action
import it.samuelelonghin.safelauncher.home.widgets.actions.ActionFlash
import it.samuelelonghin.safelauncher.home.widgets.actions.ActionMute
import it.samuelelonghin.safelauncher.support.*


class WidgetInfo(name: String, type: WidgetType) {
    var name: String
    var type: WidgetType
    var action: Action? = null
    var app: String? = null
    private var uri: Uri? = null
    var activity: Class<*>? = null

    enum class WidgetType : java.io.Serializable {
        APP,
        ACTIVITY,
        ACTION;
    }

    init {
        this.name = name
        this.type = type
    }

    constructor(name: String, type: WidgetType, uri: Uri) : this(
        name,
        type,
    ) {
        this.uri = uri
    }

    constructor(name: String, type: WidgetType, value: String) : this(
        name,
        type,
    ) {
        when (type) {
            WidgetType.APP -> this.app = value
            WidgetType.ACTION -> this.action = when (value) {
                ACTION_MUTE -> ActionMute()
                ACTION_FLASH -> ActionFlash()
                else -> ActionMute()
            }
            WidgetType.ACTIVITY -> activity = ACTIVITY_TO_CLASS[value]!!
        }
    }

    constructor(name: String, type: WidgetType, activity: Class<*>) : this(
        name,
        type,
    ) {
        this.activity = activity
    }

    constructor(activity: String) : this(
        activity,
        WidgetType.ACTIVITY,
        ACTIVITY_TO_CLASS[activity]!!
    )

    constructor(action: Action) : this(
        "Action Creata",
        WidgetType.ACTION,
    ) {
        this.action = action
    }

    fun setIcon(imageView: ImageView, context: Context) {
        when (type) {
            WidgetType.ACTIVITY ->
                imageView.setImageDrawable(
                    setIconTintPrimary(
                        context,
                        ACTIVITY_TO_RESOURCE_ICON[name]!!
                    )
                )
            WidgetType.ACTION -> imageView.setImageDrawable(
                setIconTintPrimary(
                    context,
                    action!!.icon
                )
            )

            else -> {
                try {
                    val icon = context.packageManager.getApplicationIcon(app!!)
                    imageView.setImageDrawable(icon)
                } catch (_: Exception) {
                    System.err.println("Errore nel icon di $name")
                }
            }
        }
    }

    fun setLabel(textView: TextView, context: Context? = null) {
        when (type) {
            WidgetType.ACTIVITY -> textView.text = ACTIVITY_TO_NAME[name]!!
            WidgetType.ACTION -> textView.text = action?.name ?: ""

            else -> {
                try {
                    if (context != null) {
                        val info = getAppInfo(context, app!!)
                        textView.text = info.loadLabel(context.packageManager)
                    } else {
                        System.err.println("È necessario il context per trovare l'icona del pacchetto $app")
                    }
                } catch (_: Exception) {
                    System.err.println("Errore nel icon di $name")
                }
            }
        }
    }
}