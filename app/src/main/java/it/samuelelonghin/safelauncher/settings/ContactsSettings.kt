package it.samuelelonghin.safelauncher.settings

import android.app.Activity
import it.samuelelonghin.safelauncher.databinding.ContactsSettingsFragmentBinding
import it.samuelelonghin.safelauncher.support.*

fun setContacts(binding: ContactsSettingsFragmentBinding, activity: Activity) {
    binding.settingsContactsNumberColumnsInput.setText(
        launcherPreferences.getInt(CONTACTS_NUMBER_COLUMNS, CONTACTS_NUMBER_COLUMNS_PREF)
            .toString()
    )
    binding.settingsContactsNumberColumnsInput.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            updatePreference(
                CONTACTS_NUMBER_COLUMNS,
                getIntValue(binding.settingsContactsNumberColumnsInput.editableText)
            )
        }
    }
    setSwitch(
        binding.settingsContactsIsScrollableInput,
        CONTACTS_IS_SCROLLABLE,
        CONTACTS_IS_SCROLLABLE_PREF
    )
}