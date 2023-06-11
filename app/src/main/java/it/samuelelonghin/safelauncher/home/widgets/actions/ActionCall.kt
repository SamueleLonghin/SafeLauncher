package it.samuelelonghin.safelauncher.home.widgets.actions

import android.content.Context
import android.content.Intent
import it.samuelelonghin.safelauncher.R


class ActionCall : Action("Mute", 0) {
    override val states: List<String>
        get() = listOf("Chiama")
    override val icons: List<Int>
        get() = listOf(
            R.drawable.ic_baseline_call_240,
        )
    override val texts: List<Int>
        get() = listOf(
            R.string.call,
        )


    override fun toggle(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL)
        context.startActivity(intent)
    }
}