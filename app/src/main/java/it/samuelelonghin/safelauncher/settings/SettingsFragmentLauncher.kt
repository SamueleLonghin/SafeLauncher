package it.samuelelonghin.safelauncher.settings


import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.samuelelonghin.safelauncher.databinding.SettingsLauncherFragmentBinding
import it.samuelelonghin.safelauncher.support.*


/**
 * The [SettingsFragmentLauncher] is a used as a tab in the SettingsActivity.
 *
 * It is used to change themes, select wallpapers ... theme related stuff
 */
class SettingsFragmentLauncher : Fragment(), UIObject {

    private lateinit var binding: SettingsLauncherFragmentBinding
    private lateinit var _view: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = SettingsLauncherFragmentBinding.inflate(inflater)
        _view = binding.root
        println("SettingsFragmentLauncher :: CreateView")

        return _view
    }

    override fun onStart() {
        super<Fragment>.onStart()
        super<UIObject>.onStart()
    }


    private fun getIntValue(value: Editable, def: Int = -1): Int {
        var out: Int = def
        try {
            out = Integer.parseInt(value.toString())
        } catch (_: NumberFormatException) {

        }
        return out
    }

    override fun setOnClicks() {
        binding.settingsContactsNumberColumnsInput.setText(
            launcherPreferences.getInt(CONTACTS_NUMBER_COLUMNS, CONTACTS_NUMBER_COLUMNS_PREF)
                .toString()
        )
        binding.settingsContactsNumberColumnsInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                println("Colonne Ha il fuoco")
            } else {
                updatePreference(
                    CONTACTS_NUMBER_COLUMNS,
                    getIntValue(binding.settingsContactsNumberColumnsInput.editableText)
                )
            }
        }
        binding.settingsContactsIsScrollableInput.isChecked =
            launcherPreferences.getBoolean(CONTACTS_IS_SCROLLABLE, CONTACTS_IS_SCROLLABLE_PREF)
        binding.settingsContactsIsScrollableInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                CONTACTS_IS_SCROLLABLE, binding.settingsContactsIsScrollableInput.isChecked
            )

        }



        binding.settingsWidgetsNumberColumnsInput.setText(
            launcherPreferences.getInt(WIDGET_NUMBER_COLUMNS, WIDGET_NUMBER_COLUMNS_PREF).toString()
        )
        binding.settingsWidgetsNumberColumnsInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                println("Widgets Colonne Ha il fuoco")
            } else {
                updatePreference(
                    WIDGET_NUMBER_COLUMNS,
                    getIntValue(binding.settingsWidgetsNumberColumnsInput.text)
                )
            }
        }
        binding.settingsWidgetsNumberRowsInput.setText(
            launcherPreferences.getInt(WIDGET_NUMBER_ROWS, WIDGET_NUMBER_ROWS_PREF).toString()
        )
        binding.settingsWidgetsNumberRowsInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                println("Widgets Rows Ha il fuoco")
            } else {
                updatePreference(
                    WIDGET_NUMBER_ROWS, getIntValue(binding.settingsWidgetsNumberRowsInput.text)
                )

            }
        }
    }


}