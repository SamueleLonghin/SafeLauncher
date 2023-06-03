package it.samuelelonghin.safelauncher.home.widgets.actions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.widget.Toast
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.activityResultNotificationPolicy
import it.samuelelonghin.safelauncher.support.canChangeNotificationPolicy
import it.samuelelonghin.safelauncher.tutorial.RequestNotificationPolicyActivity


class ActionMute : Action("Mute", 0) {
    override val states: List<String>
        get() = listOf("Mute", "unMute")
    override val icons: List<Int>
        get() = listOf(
            R.drawable.ic_baseline_notifications_active_24,
            R.drawable.ic_baseline_vibration_24,
            R.drawable.ic_baseline_notifications_off_24
        )
    override val texts: List<Int>
        get() = listOf(
            R.string.unmute,
            R.string.vibration,
            R.string.mute,
        )


    private lateinit var audioManager: AudioManager
    private lateinit var receiver: BroadcastReceiver

    override fun toggle(context: Context) {
        println("Toggle $name")
        if (setPhoneMode(context)) {
            state = (state + 1) % 3
        } else {
            Toast.makeText(context, "Non sono riuscito", Toast.LENGTH_SHORT).show()
        }
        reloadLayout(context)
    }


    fun setAudioState(ringerMode: Int) {
        state = when (ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> 0
            AudioManager.RINGER_MODE_VIBRATE -> 1
            AudioManager.RINGER_MODE_SILENT -> 2
            else -> 0
        }
    }

    override fun getStatus(context: Context) {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        println("AUDIO MODE: ")
        setAudioState(audioManager.ringerMode)
        reloadLayout(context)
    }

    override fun createListener(context: Context) {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                setAudioState(audioManager.ringerMode)
                reloadLayout(context)
            }
        }

    }

    override fun registerListener(context: Context) {
        context.registerReceiver(
            receiver, IntentFilter(
                AudioManager.RINGER_MODE_CHANGED_ACTION
            )
        )
    }

    override fun unRegisterListener(context: Context) {
        context.unregisterReceiver(receiver)
    }

    private fun setPhoneMode(context: Context): Boolean {
        if (canChangeNotificationPolicy(context)) {
            val am: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.ringerMode = when(state){
                0 -> AudioManager.RINGER_MODE_NORMAL
                1 -> AudioManager.RINGER_MODE_VIBRATE
                2 -> AudioManager.RINGER_MODE_SILENT
                else -> 2
            }
            return true
        } else {
            /**
             * Se non ho il permesso avvio la richiesta
             */
            val intent = Intent(context, RequestNotificationPolicyActivity::class.java)
            activityResultNotificationPolicy.launch(intent)
        }
        return false
    }
}