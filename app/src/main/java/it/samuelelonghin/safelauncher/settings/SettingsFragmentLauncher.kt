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
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.SettingsLauncherFragmentBinding
import it.samuelelonghin.safelauncher.home.widgets.WidgetFragment
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo.WidgetType
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial
import it.samuelelonghin.safelauncher.list.ListActivity
import it.samuelelonghin.safelauncher.support.*


/**
 * The [SettingsFragmentLauncher] is a used as a tab in the SettingsActivity.
 *
 * It is used to change themes, select wallpapers ... theme related stuff
 */

class SettingsFragmentLauncher : Fragment(), UIObject {

    private lateinit var binding: SettingsLauncherFragmentBinding
    private lateinit var selectApp: ActivityResultLauncher<Intent>

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
//                    val icon = requireActivity().packageManager.getApplicationIcon(v)
                    println("RESULT: $result")
                    println("index: $i, value: $v, type: $t, name: $n")
                    val ws = WidgetSerial(i, v, t)
                    setWidgetListItem(i, ws)
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
        binding.contactsSettings.settingsContactsIsScrollableInput.isChecked =
            launcherPreferences.getBoolean(CONTACTS_IS_SCROLLABLE, CONTACTS_IS_SCROLLABLE_PREF)
        binding.contactsSettings.settingsContactsIsScrollableInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                CONTACTS_IS_SCROLLABLE,
                binding.contactsSettings.settingsContactsIsScrollableInput.isChecked
            )
        }

        /**
         * View Contact
         */
        //Buttons Direction
        binding.viewContactSettings.settingsViewContactButtonsDirectionInput.isChecked =
            launcherPreferences.getBoolean(
                VIEW_CONTACT_BUTTONS_DIRECTION, VIEW_CONTACT_BUTTONS_DIRECTION_PREF
            )
        binding.viewContactSettings.settingsViewContactButtonsDirectionInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                VIEW_CONTACT_BUTTONS_DIRECTION,
                binding.viewContactSettings.settingsViewContactButtonsDirectionInput.isChecked
            )
        }
        //Rapid Call
        binding.viewContactSettings.settingsViewContactShowRapidCallInput.isChecked =
            launcherPreferences.getBoolean(
                VIEW_CONTACT_SHOW_RAPID_CALL, VIEW_CONTACT_SHOW_RAPID_CALL_PREF
            )
        binding.viewContactSettings.settingsViewContactShowRapidCallInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                VIEW_CONTACT_SHOW_RAPID_CALL,
                binding.viewContactSettings.settingsViewContactShowRapidCallInput.isChecked
            )
        }
        //Rapid Chat
        binding.viewContactSettings.settingsViewContactShowRapidChatInput.isChecked =
            launcherPreferences.getBoolean(
                VIEW_CONTACT_SHOW_RAPID_CHAT, VIEW_CONTACT_SHOW_RAPID_CHAT_PREF
            )
        binding.viewContactSettings.settingsViewContactShowRapidChatInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                VIEW_CONTACT_SHOW_RAPID_CHAT,
                binding.viewContactSettings.settingsViewContactShowRapidChatInput.isChecked
            )
        }
        //Notifications
        binding.viewContactSettings.settingsViewContactShowNotificationsInput.isChecked =
            launcherPreferences.getBoolean(
                VIEW_CONTACT_SHOW_NOTIFICATIONS, VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF
            )
        binding.viewContactSettings.settingsViewContactShowNotificationsInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                VIEW_CONTACT_SHOW_NOTIFICATIONS,
                binding.viewContactSettings.settingsViewContactShowNotificationsInput.isChecked
            )
        }
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
        binding.widgetsSettings.settingsWidgetsShowLabelsInput.isChecked =
            launcherPreferences.getBoolean(
                WIDGET_SHOW_LABELS, WIDGET_SHOW_LABELS_PREF
            )
        binding.widgetsSettings.settingsWidgetsShowLabelsInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                WIDGET_SHOW_LABELS,
                binding.widgetsSettings.settingsWidgetsShowLabelsInput.isChecked
            )
        }

        // Possibilità di scorrere
        binding.widgetsSettings.settingsWidgetsIsScrollableInput.isChecked =
            launcherPreferences.getBoolean(
                WIDGET_IS_SCROLLABLE, WIDGET_IS_SCROLLABLE_PREF
            )
        binding.widgetsSettings.settingsWidgetsIsScrollableInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                WIDGET_IS_SCROLLABLE,
                binding.widgetsSettings.settingsWidgetsIsScrollableInput.isChecked
            )
            binding.widgetsSettings.settingsWidgetsNumberRowsInput.visibility = if (
                launcherPreferences.getBoolean(
                    WIDGET_IS_SCROLLABLE,
                    WIDGET_IS_SCROLLABLE_PREF
                )
            ) View.VISIBLE else View.GONE
        }

        binding.widgetsSettings.widgetsButton.setOnClickListener {
            println("Cliccato widget")

            val intent = Intent(activity, ListActivity::class.java)
            intent.putExtra("intention", "pick")
            intent.putExtra("forApp", "0") // for which action we choose the app
            intent.putExtra("index", 100) // for which action we choose the app
            intendedSettingsPause = true
            selectApp.launch(intent)
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


}