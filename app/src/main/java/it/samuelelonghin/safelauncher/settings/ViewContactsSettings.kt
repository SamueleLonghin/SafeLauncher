package it.samuelelonghin.safelauncher.settings

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import com.google.android.material.switchmaterial.SwitchMaterial
import it.samuelelonghin.safelauncher.databinding.ViewContactSettingsFragmentBinding
import it.samuelelonghin.safelauncher.support.*
import it.samuelelonghin.safelauncher.tutorial.RequestNotificationAccessActivity

fun setViewContacts(binding: ViewContactSettingsFragmentBinding, activity: Activity) {
    setSwitch(
        binding.settingsViewContactButtonsDirectionInput,
        VIEW_CONTACT_BUTTONS_DIRECTION,
        VIEW_CONTACT_BUTTONS_DIRECTION_PREF
    )
    //Rapid Call
    setSwitch(
        binding.settingsViewContactShowRapidCallInput,
        VIEW_CONTACT_SHOW_RAPID_CALL,
        VIEW_CONTACT_SHOW_RAPID_CALL_PREF
    )
    //Rapid Chat
    setSwitch(
        binding.settingsViewContactShowRapidChatInput,
        VIEW_CONTACT_SHOW_RAPID_CHAT,
        VIEW_CONTACT_SHOW_RAPID_CHAT_PREF
    )

    //Notifications
    registerNotificationSwitch(binding.settingsViewContactShowNotificationsInput, activity)


    // Rapid Chat App
    val appInt: Int = VIEW_CONTACT_RAPID_APP_TO_INDEX[launcherPreferences.getString(
        VIEW_CONTACT_RAPID_CHAT_APP, VIEW_CONTACT_RAPID_CHAT_APP_PREF
    )]!!
    binding.settingsViewContactRapidChatAppInput.setSelection(appInt)
    binding.settingsViewContactRapidChatAppInput.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val app: String = VIEW_CONTACT_RAPID_INDEX_TO_APP[position]!!
                updatePreference(VIEW_CONTACT_RAPID_CHAT_APP, app)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
}

        fun registerNotificationSwitch(materialSwitch: SwitchMaterial, activity: Activity) {
            /**
             * Nel caso non abbia il permesso ma il valore nelle impostazioni risulta negativo visualizzo
             * lo switch come spento
             */
            if (launcherPreferences.getBoolean(
                    VIEW_CONTACT_SHOW_NOTIFICATIONS,
                    VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF
                ) && !canReceiveNotifications(activity)
            ) updatePreference(VIEW_CONTACT_SHOW_NOTIFICATIONS, false)

            setSwitch(
                materialSwitch,
                VIEW_CONTACT_SHOW_NOTIFICATIONS,
                VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF
            ) { checked ->
                if (checked) {
                    if (!canReceiveNotifications(activity)) {
                        val intent = Intent(activity, RequestNotificationAccessActivity::class.java)
                        intendedSettingsPause = true
                        activityResultNotificationAccess.launch(intent)
                    }
                }
            }
        }


