package it.samuelelonghin.safelauncher.settings

import android.app.Activity
import android.view.View
import it.samuelelonghin.safelauncher.databinding.WidgetSettingsFragmentBinding
import it.samuelelonghin.safelauncher.support.*

fun setWidgets(binding: WidgetSettingsFragmentBinding, activity: Activity) {
    setSpinnerNumeric(
        binding.settingsWidgetsNumberColumnsInput,
        WIDGET_NUMBER_COLUMNS, WIDGET_NUMBER_COLUMNS_PREF
    )
    setSpinnerNumeric(
        binding.settingsWidgetsNumberRowsInput,
        WIDGET_NUMBER_ROWS, WIDGET_NUMBER_ROWS_PREF
    )

    binding.settingsWidgetsNumberRows.visibility = if (
        launcherPreferences.getBoolean(
            WIDGET_IS_SCROLLABLE,
            WIDGET_IS_SCROLLABLE_PREF
        )
    ) View.VISIBLE else View.GONE

    //Show text
    setSwitch(
        binding.settingsWidgetsShowLabelsInput, WIDGET_SHOW_LABELS,
        WIDGET_SHOW_LABELS_PREF
    )

    // Possibilità di scorrere
    setSwitch(
        binding.settingsWidgetsIsScrollableInput,
        WIDGET_IS_SCROLLABLE,
        WIDGET_IS_SCROLLABLE_PREF
    ) {
        binding.settingsWidgetsNumberRows.visibility = if (
            launcherPreferences.getBoolean(
                WIDGET_IS_SCROLLABLE,
                WIDGET_IS_SCROLLABLE_PREF
            )
        ) View.VISIBLE else View.GONE
    }
    // Forzare presenza Settings
    setSwitch(
        binding.settingsWidgetsForceSettingsInput,
        WIDGET_FORCE_SETTINGS,
        WIDGET_FORCE_SETTINGS_DEF
    ) { checked: Boolean ->
        if (checked) {
            binding.settingsWidgetsForceAppsInput.isChecked =
                launcherPreferences.getBoolean(
                    WIDGET_FORCE_APPS,
                    WIDGET_FORCE_APPS_DEF
                )
        } else {
            binding.settingsWidgetsForceAppsInput.isChecked = true
        }
    }
    // Forzare presenza Apps
    setSwitch(
        binding.settingsWidgetsForceAppsInput,
        WIDGET_FORCE_APPS,
        WIDGET_FORCE_APPS_DEF
    ) { checked: Boolean ->
        if (!checked) {
            binding.settingsWidgetsForceSettingsInput.isChecked = true
        } else {
            binding.settingsWidgetsForceSettingsInput.isChecked =
                launcherPreferences.getBoolean(
                    WIDGET_FORCE_SETTINGS,
                    WIDGET_FORCE_SETTINGS_DEF
                )
        }
    }

}