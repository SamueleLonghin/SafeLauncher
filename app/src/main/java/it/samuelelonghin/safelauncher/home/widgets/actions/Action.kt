package it.samuelelonghin.safelauncher.home.widgets.actions

import android.content.Context
import it.samuelelonghin.safelauncher.home.widgets.WidgetAdapter
import it.samuelelonghin.safelauncher.home.widgets.WidgetViewHolder
import it.samuelelonghin.safelauncher.support.setIconTint

abstract class Action(name: String, state: Int) {
    var name: String
    protected lateinit var holder: WidgetViewHolder
    protected var state: Int
    abstract val states: List<String>
    abstract val icons: List<Int>
    abstract val texts: List<Int>

    abstract fun toggle(context: Context)
    abstract fun getStatus(context: Context)
    fun reloadLayout(context: Context) {
        holder.imageView.setImageDrawable(setIconTint(context, icon))
        holder.textView.setText(text)
    }
    val icon: Int
        get() = icons[state]


    val text: Int
        get() = texts[state]

    init {
        this.name = name
        this.state = state
    }

    fun addHolder(holder: WidgetViewHolder) {
        this.holder = holder
    }
}