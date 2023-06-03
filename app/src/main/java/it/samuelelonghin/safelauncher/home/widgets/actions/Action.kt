package it.samuelelonghin.safelauncher.home.widgets.actions

import android.content.Context
import it.samuelelonghin.safelauncher.home.widgets.WidgetViewHolder
import it.samuelelonghin.safelauncher.support.setIconTintPrimary

abstract class Action(name: String, state: Int) {
    var name: String
    protected lateinit var holder: WidgetViewHolder
    protected var state: Int
    abstract val states: List<String>
    abstract val icons: List<Int>
    abstract val texts: List<Int>
    abstract fun toggle(context: Context)
    fun reloadLayout(context: Context) {
        holder.imageView.setImageDrawable(setIconTintPrimary(context, icon))
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

    open fun getStatus(context: Context) {}

    open fun createListener(context: Context) {}

    open fun registerListener(context: Context) {}

    open fun unRegisterListener(context: Context) {}
}