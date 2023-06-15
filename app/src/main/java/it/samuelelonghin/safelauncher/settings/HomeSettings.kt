package it.samuelelonghin.safelauncher.settings

import android.app.Activity
import android.content.Intent
import com.google.android.material.switchmaterial.SwitchMaterial
import it.samuelelonghin.safelauncher.databinding.HomeSettingsFragmentBinding
import it.samuelelonghin.safelauncher.support.*
import it.samuelelonghin.safelauncher.tutorial.RequestFullScreenActivity

fun setHome(binding: HomeSettingsFragmentBinding, activity: Activity) {
    binding.settingsHomeRequireAuthForSettingsInput.isChecked = launcherPreferences.getBoolean(
        SETTINGS_REQUIRES_AUTH, SETTINGS_REQUIRES_AUTH_DEF
    )
    binding.settingsHomeRequireAuthForSettingsInput.setOnCheckedChangeListener { _, checked ->
        if (checked) {
            val intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra("intention", AuthActivity.Intention.CREATE.toString())
            intendedSettingsPause = true
            activityResultSetPassword.launch(intent)
        } else updatePreference(SETTINGS_REQUIRES_AUTH, false)
    }

    //Full Screen
    registerFullScreenSwitch(binding.settingsHomeForceFullScreenInput, activity)

    //Fast update contacts
    setSwitch(
        binding.settingsHomeFastUpdateContactsInput, FAST_UPDATE_CONTACTS,
        FAST_UPDATE_CONTACTS_DEF
    )

    //Change default launcher
    binding.settingsHomeSetAsPreferredLauncherButton.setOnClickListener {
        askForChangeLauncher(activity)
    }
}


fun registerFullScreenSwitch(materialSwitch: SwitchMaterial, activity: Activity) {
    if (launcherPreferences.getBoolean(
            SETTINGS_FORCE_FULL_SCREEN, SETTINGS_FORCE_FULL_SCREEN_DEF
        ) && !canFullScreen(activity)
    ) updatePreference(SETTINGS_FORCE_FULL_SCREEN, false)

    setSwitch(
        materialSwitch, SETTINGS_FORCE_FULL_SCREEN, SETTINGS_FORCE_FULL_SCREEN_DEF
    ) { checked ->
        if (checked) {
            if (!canFullScreen(activity)) {
                val intent = Intent(activity, RequestFullScreenActivity::class.java)
                intendedSettingsPause = true
                activityResultEnableFullScreen.launch(intent)
            }
        }
    }
}