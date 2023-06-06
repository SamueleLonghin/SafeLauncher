package it.samuelelonghin.safelauncher.settings

import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.google.android.material.switchmaterial.SwitchMaterial
import it.samuelelonghin.safelauncher.support.CONTACTS_NUMBER_COLUMNS
import it.samuelelonghin.safelauncher.support.CONTACTS_NUMBER_COLUMNS_PREF
import it.samuelelonghin.safelauncher.support.launcherPreferences
import it.samuelelonghin.safelauncher.support.updatePreference

fun setSwitch(
    switch: SwitchMaterial,
    preferenceName: String,
    preferenceDefault: Boolean,
    onChange: ((checked: Boolean) -> Unit)? = null
) {
    switch.isChecked = launcherPreferences.getBoolean(preferenceName, preferenceDefault)
    switch.setOnCheckedChangeListener { _, checked ->
        updatePreference(preferenceName, checked)
        if (onChange != null)
            onChange(checked)
    }
}


fun setSpinnerNumeric(
    spinner: Spinner,
    preferenceName: String, preferenceDefault: Int
) {
    val nCols = launcherPreferences.getInt(preferenceName, preferenceDefault)
    spinner.setSelection(nCols - 1)
    spinner.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                updatePreference(preferenceName, position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
}

fun getIntValue(value: Editable, def: Int = -1): Int {
    var out: Int = def
    try {
        out = Integer.parseInt(value.toString())
    } catch (_: NumberFormatException) {

    }
    return out
}