package it.samuelelonghin.safelauncher.home.widgets.actions

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.provider.Settings
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.activityResultNotificationPolicy
import it.samuelelonghin.safelauncher.support.setIconTint


class ActionMute : Action("Mute", 0) {
    override val states: List<String>
        get() = listOf("Mute", "unMute")
    override val icons: List<Int>
        get() = listOf(
            R.drawable.ic_baseline_notifications_active_24,
            R.drawable.ic_baseline_notifications_off_24
        )
    override val texts: List<Int>
        get() = listOf(
            R.string.unmute,
            R.string.mute,
        )

    override fun toggle(context: Context) {
        println("Togglato " + name)
        if (setPhoneMode(context)) {
            state = 1 - state
        } else {
            Toast.makeText(context, "Non sono riuscito", Toast.LENGTH_SHORT).show()
        }
        reloadLayout(context)
    }
    override fun getStatus(context: Context) {
        val am: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        println("AUDIO MODE: ")
        when (am.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> state = 0
            AudioManager.RINGER_MODE_SILENT -> state = 1
        }
        reloadLayout(context)
    }

    private fun checkUserCanChangeNotificationPolicy(context: Context): Boolean {
        return (context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager).isNotificationPolicyAccessGranted
    }

    private fun setPhoneMode(context: Context): Boolean {
        if (checkUserCanChangeNotificationPolicy(context)) {
            val am: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (state == 0)
                am.ringerMode = AudioManager.RINGER_MODE_NORMAL
            else
                am.ringerMode = AudioManager.RINGER_MODE_SILENT
            return true
        } else {
            //todo mostrare un messaggio prima di avviare l'activity
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            activityResultNotificationPolicy.launch(intent)
        }
        return false
    }
}