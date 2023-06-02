package it.samuelelonghin.safelauncher.settings

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import com.google.android.material.switchmaterial.SwitchMaterial
import it.samuelelonghin.safelauncher.databinding.HomeSettingsFragmentBinding
import it.samuelelonghin.safelauncher.support.*
import it.samuelelonghin.safelauncher.tutorial.RequestFullScreenActivity
import it.samuelelonghin.safelauncher.tutorial.RequestNotificationAccessActivity

fun setHome(binding: HomeSettingsFragmentBinding, activity: Activity) {
    binding.settingsHomeRequireAuthForSettingsInput.isChecked = launcherPreferences.getBoolean(
        SETTINGS_REQUIRES_AUTH, SETTINGS_REQUIRES_AUTH_DEF
    )
    binding.settingsHomeRequireAuthForSettingsInput.setOnCheckedChangeListener { _, checked ->
        if (checked) {
            val intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra("intention", AuthActivity.Intention.CREATE.toString())
            intendedSettingsPause = true
            setAuth.launch(intent)
        } else updatePreference(SETTINGS_REQUIRES_AUTH, false)
    }


//    if (launcherPreferences.getBoolean(
//            SETTINGS_FORCE_FULL_SCREEN,
//            SETTINGS_FORCE_FULL_SCREEN_DEF
//        ) && canFullScreen(activity)
//    ) updatePreference(SETTINGS_FORCE_FULL_SCREEN, false)
//
//    binding.settingsHomeForceFullScreenInput.isChecked = launcherPreferences.getBoolean(
//        SETTINGS_FORCE_FULL_SCREEN, SETTINGS_FORCE_FULL_SCREEN_DEF
//    )
//    binding.settingsHomeForceFullScreenInput.setOnCheckedChangeListener { _, checked ->
//        if (checked) {
//            if (!Settings.canDrawOverlays(activity)) {
//                val intent = Intent(activity, RequestFullScreenActivity::class.java)
//                intendedSettingsPause = true
//                activityResultEnableFullScreen.launch(intent)
//            } else updatePreference(SETTINGS_FORCE_FULL_SCREEN, true)
//        } else updatePreference(SETTINGS_FORCE_FULL_SCREEN, false)
//    }

    //Full Screen
    registerFullScreenSwitch(binding.settingsHomeForceFullScreenInput, activity)


    binding.settingsHomeSetAsPreferredLauncherButton.setOnClickListener {
        askForChangeLauncher(activity)
    }
}


fun registerFullScreenSwitch(materialSwitch: SwitchMaterial, activity: Activity) {
    if (launcherPreferences.getBoolean(
            SETTINGS_FORCE_FULL_SCREEN,
            SETTINGS_FORCE_FULL_SCREEN_DEF
        ) && !canFullScreen(activity)
    ) updatePreference(SETTINGS_FORCE_FULL_SCREEN, false)

    setSwitch(
        materialSwitch,
        SETTINGS_FORCE_FULL_SCREEN,
        SETTINGS_FORCE_FULL_SCREEN_DEF
    ) { checked ->
        if (checked) {
            if (!canFullScreen(activity)) {
                val intent = Intent(activity, RequestFullScreenActivity::class.java)
                intendedSettingsPause = true
                activityResultNotificationAccess.launch(intent)
            }
        }
    }
}