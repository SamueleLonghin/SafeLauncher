package it.samuelelonghin.safelauncher.settings

import android.text.Editable
import com.google.android.material.switchmaterial.SwitchMaterial
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

fun getIntValue(value: Editable, def: Int = -1): Int {
    var out: Int = def
    try {
        out = Integer.parseInt(value.toString())
    } catch (_: NumberFormatException) {

    }
    return out
}