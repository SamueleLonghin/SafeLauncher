package it.samuelelonghin.safelauncher.settings

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import it.samuelelonghin.safelauncher.databinding.ContactsSettingsFragmentBinding
import it.samuelelonghin.safelauncher.support.*

fun setContacts(binding: ContactsSettingsFragmentBinding, activity: Activity) {
    setSpinnerNumeric(
        binding.settingsContactsNumberColumnsInput,
        CONTACTS_NUMBER_COLUMNS,
        CONTACTS_NUMBER_COLUMNS_PREF
    )
    setSwitch(
        binding.settingsContactsIsScrollableInput,
        CONTACTS_IS_SCROLLABLE,
        CONTACTS_IS_SCROLLABLE_PREF
    )
}