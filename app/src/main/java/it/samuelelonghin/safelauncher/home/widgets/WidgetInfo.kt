package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.*


class WidgetInfo(name: String, type: WidgetType) {
    //    lateinit var mode: WidgetFragment.Mode
    var name: String
    var type: WidgetType
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

    constructor(name: String, type: WidgetType, app: String) : this(
        name,
        type,
    ) {
        this.app = app
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

    fun setIcon(imageView: ImageView, context: Context) {
        when (type) {
            WidgetType.ACTIVITY -> {
                val icon = ResourcesCompat.getDrawable(
                    context.resources,
                    ACTIVITY_TO_RESOURCE_ICON[name]!!,
                    context.theme
                )
                icon!!.setTint(context.getColorFromAttr(android.R.attr.colorPrimary))
                imageView.setImageDrawable(icon)
            }
            WidgetType.ACTION -> imageView.setImageResource(ACTION_TO_RESOURCE_ICON[name]!![0])

            else -> {
                try {
                    val icon = context.packageManager.getApplicationIcon(app!!)
                    imageView.setImageDrawable(icon)
                } catch (_: Exception) {
                    System.err.println("Errore nel icon di ${name}")
                }
            }
        }
    }

    fun setLabel(textView: TextView, context: Context? = null) {
        when (type) {
            WidgetType.ACTIVITY -> textView.text = ACTIVITY_TO_NAME[name]!!
            WidgetType.ACTION -> textView.text = ACTION_TO_NAME[name]!![0]

            else -> {
                try {
                    if (context != null) {
                        val info = getAppInfo(context, app!!)
                        textView.text = info.loadLabel(context.packageManager)
                    } else {
                        System.err.println("È necessario il context per trovare l'icona del pacchetto $app")
                    }
                } catch (_: Exception) {
                    System.err.println("Errore nel icon di ${name}")
                }
            }
        }
    }
}