package it.samuelelonghin.safelauncher.settings


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.SettingsLauncherFragmentBinding
import it.samuelelonghin.safelauncher.home.widgets.WidgetFragment
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo.WidgetType
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial
import it.samuelelonghin.safelauncher.support.*


/**
 * The [SettingsFragmentLauncher] is a used as a tab in the SettingsActivity.
 *
 * It is used to change themes, select wallpapers ... theme related stuff
 */

class SettingsFragmentLauncher : Fragment(), UIObject {

    private lateinit var binding: SettingsLauncherFragmentBinding
    private lateinit var selectApp: ActivityResultLauncher<Intent>
    private lateinit var setAuth: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = SettingsLauncherFragmentBinding.inflate(inflater)
        println("SettingsFragmentLauncher :: CreateView")
        setLayout()
        selectApp =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == REQUEST_CHOOSE_APP) {
                    val extras = result.data!!.extras!!

                    val i = extras.getInt("index")
                    val typeId = extras.getInt("type")
                    val t: WidgetType = WidgetType.values()[typeId]
                    val v = extras.getString("value")!!
                    val n = extras.getString("name")!!
                    println("RESULT: $result")
                    println("index: $i, value: $v, type: $t, name: $n, oldT: $typeId")
                    val ws = WidgetSerial(i, v, t)
                    setWidgetListItem(i, ws)
                } else System.err.println("RESULT: $result")
            }

        setAuth =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == REQUEST_AUTH_FOR_SETTINGS) {
                    val extras = result.data!!.extras!!
                    val pass = extras.getString("value")
                    if (pass != null && pass.length == SETTINGS_AUTH_LENGTH) {
                        println("NUOVA PASSWORD: $pass")
                        intendedSettingsPause = false
                        updatePreference(SETTINGS_REQUIRES_AUTH, true)
                        updatePreference(SETTINGS_AUTH, pass)
                    }
                } else System.err.println("RESULT: $result")
            }
        return binding.root
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

    private fun setLayout() {
        val fragmentTransaction = childFragmentManager.beginTransaction()

        val widgetFragment = WidgetFragment()
        widgetFragment.mode = WidgetFragment.Mode.PICK

        fragmentTransaction.add(R.id.widgets_frame, widgetFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    override fun setOnClicks() {
        /**
         * Home
         */
        binding.homeSettings.settingsHomeRequireAuthForSettingsInput.isChecked =
            launcherPreferences.getBoolean(
                SETTINGS_REQUIRES_AUTH, SETTINGS_REQUIRES_AUTH_DEF
            )
        binding.homeSettings.settingsHomeRequireAuthForSettingsInput.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                val intent = Intent(activity, AuthActivity::class.java)
                intent.putExtra("intention", AuthActivity.Intention.CREATE.toString())
                intendedSettingsPause = true
                setAuth.launch(intent)
            } else updatePreference(SETTINGS_REQUIRES_AUTH, false)
        }
        /**
         * Contacts
         */
        binding.contactsSettings.settingsContactsNumberColumnsInput.setText(
            launcherPreferences.getInt(CONTACTS_NUMBER_COLUMNS, CONTACTS_NUMBER_COLUMNS_PREF)
                .toString()
        )
        binding.contactsSettings.settingsContactsNumberColumnsInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updatePreference(
                    CONTACTS_NUMBER_COLUMNS,
                    getIntValue(binding.contactsSettings.settingsContactsNumberColumnsInput.editableText)
                )
            }
        }
        setSwitch(
            binding.contactsSettings.settingsContactsIsScrollableInput,
            CONTACTS_IS_SCROLLABLE,
            CONTACTS_IS_SCROLLABLE_PREF
        )

        /**
         * View Contact
         */
        //Buttons Direction
        setSwitch(
            binding.viewContactSettings.settingsViewContactButtonsDirectionInput,
            VIEW_CONTACT_BUTTONS_DIRECTION,
            VIEW_CONTACT_BUTTONS_DIRECTION_PREF
        )
        //Rapid Call
        setSwitch(
            binding.viewContactSettings.settingsViewContactShowRapidCallInput,
            VIEW_CONTACT_SHOW_RAPID_CALL,
            VIEW_CONTACT_SHOW_RAPID_CALL_PREF
        )
        //Rapid Chat
        setSwitch(
            binding.viewContactSettings.settingsViewContactShowRapidChatInput,
            VIEW_CONTACT_SHOW_RAPID_CHAT,
            VIEW_CONTACT_SHOW_RAPID_CHAT_PREF
        )
        //Notifications
        setSwitch(
            binding.viewContactSettings.settingsViewContactShowNotificationsInput,
            VIEW_CONTACT_SHOW_NOTIFICATIONS,
            VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF
        )
        // Rapid Chat App
        val appInt: Int = VIEW_CONTACT_RAPID_APP_TO_INDEX[launcherPreferences.getString(
            VIEW_CONTACT_RAPID_CHAT_APP, VIEW_CONTACT_RAPID_CHAT_APP_PREF
        )]!!
        binding.viewContactSettings.settingsViewContactRapidChatAppInput.setSelection(appInt)
        binding.viewContactSettings.settingsViewContactRapidChatAppInput.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    val app: String = VIEW_CONTACT_RAPID_INDEX_TO_APP[position]!!
                    updatePreference(VIEW_CONTACT_RAPID_CHAT_APP, app)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        /**
         * Widgets
         */

        binding.widgetsSettings.settingsWidgetsNumberColumnsInput.setText(
            launcherPreferences.getInt(WIDGET_NUMBER_COLUMNS, WIDGET_NUMBER_COLUMNS_PREF).toString()
        )
        binding.widgetsSettings.settingsWidgetsNumberColumnsInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                println("Widgets Colonne Ha il fuoco")
            } else {
                updatePreference(
                    WIDGET_NUMBER_COLUMNS,
                    getIntValue(binding.widgetsSettings.settingsWidgetsNumberColumnsInput.text)
                )
            }
        }
        binding.widgetsSettings.settingsWidgetsNumberRowsInput.setText(
            launcherPreferences.getInt(WIDGET_NUMBER_ROWS, WIDGET_NUMBER_ROWS_PREF).toString()
        )
        binding.widgetsSettings.settingsWidgetsNumberRows.visibility = if (
            launcherPreferences.getBoolean(
                WIDGET_IS_SCROLLABLE,
                WIDGET_IS_SCROLLABLE_PREF
            )
        ) View.VISIBLE else View.GONE

        binding.widgetsSettings.settingsWidgetsNumberRowsInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                println("Widgets Rows Ha il fuoco")
            } else {
                updatePreference(
                    WIDGET_NUMBER_ROWS,
                    getIntValue(binding.widgetsSettings.settingsWidgetsNumberRowsInput.text)
                )

            }
        }
        //Show text
        setSwitch(
            binding.widgetsSettings.settingsWidgetsShowLabelsInput, WIDGET_SHOW_LABELS,
            WIDGET_SHOW_LABELS_PREF
        )

        // Possibilità di scorrere
        setSwitch(
            binding.widgetsSettings.settingsWidgetsIsScrollableInput, WIDGET_IS_SCROLLABLE,
            WIDGET_IS_SCROLLABLE_PREF
        ) {
            binding.widgetsSettings.settingsWidgetsNumberRowsInput.visibility = if (
                launcherPreferences.getBoolean(
                    WIDGET_IS_SCROLLABLE,
                    WIDGET_IS_SCROLLABLE_PREF
                )
            ) View.VISIBLE else View.GONE
        }
        // Forzare presenza Settings
        setSwitch(
            binding.widgetsSettings.settingsWidgetsForceSettingsInput,
            WIDGET_FORCE_SETTINGS,
            WIDGET_FORCE_SETTINGS_DEF
        ) { checked: Boolean ->
            if (checked) {
                binding.widgetsSettings.settingsWidgetsForceAppsInput.isChecked =
                    launcherPreferences.getBoolean(
                        WIDGET_FORCE_APPS,
                        WIDGET_FORCE_APPS_DEF
                    )
            } else {
                binding.widgetsSettings.settingsWidgetsForceAppsInput.isChecked = true
            }
        }
        // Forzare presenza Apps
        setSwitch(
            binding.widgetsSettings.settingsWidgetsForceAppsInput,
            WIDGET_FORCE_APPS,
            WIDGET_FORCE_APPS_DEF
        ) { checked: Boolean ->
            if (!checked) {
                binding.widgetsSettings.settingsWidgetsForceSettingsInput.isChecked = true
            } else {
                binding.widgetsSettings.settingsWidgetsForceSettingsInput.isChecked =
                    launcherPreferences.getBoolean(
                        WIDGET_FORCE_SETTINGS,
                        WIDGET_FORCE_SETTINGS_DEF
                    )
            }
        }


        /**
         * Apps List
         */
        val viewInt = launcherPreferences.getInt(
            APPS_LIST_VIEW_TYPE, APPS_LIST_VIEW_TYPE_PREF
        )
        binding.listAppsSettings.settingsListAppsDisplayViewInput.setSelection(viewInt)
        binding.listAppsSettings.settingsListAppsDisplayViewInput.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    updatePreference(APPS_LIST_VIEW_TYPE, position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setSwitch(
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
}