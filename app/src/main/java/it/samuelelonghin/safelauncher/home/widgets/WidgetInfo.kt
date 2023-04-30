package it.samuelelonghin.safelauncher.home.widgets

import android.net.Uri
import it.samuelelonghin.safelauncher.support.ACTIVITY_TO_CLASS
import it.samuelelonghin.safelauncher.support.ACTIVITY_TO_NAME


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
        ACTIVITY_TO_NAME[activity].toString(),
        WidgetType.ACTIVITY,
        ACTIVITY_TO_CLASS[activity]!!
    )
}