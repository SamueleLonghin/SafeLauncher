package it.samuelelonghin.safelauncher.settings

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import it.samuelelonghin.safelauncher.databinding.ListAppsSettingsFragmentBinding
import it.samuelelonghin.safelauncher.support.APPS_LIST_VIEW_TYPE
import it.samuelelonghin.safelauncher.support.APPS_LIST_VIEW_TYPE_PREF
import it.samuelelonghin.safelauncher.support.launcherPreferences
import it.samuelelonghin.safelauncher.support.updatePreference

fun setAppList(binding: ListAppsSettingsFragmentBinding, activity: Activity) {
    val viewInt = launcherPreferences.getInt(
        APPS_LIST_VIEW_TYPE, APPS_LIST_VIEW_TYPE_PREF
    )
    binding.settingsListAppsDisplayViewInput.setSelection(viewInt)
    binding.settingsListAppsDisplayViewInput.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                updatePreference(APPS_LIST_VIEW_TYPE, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
}