package it.samuelelonghin.safelauncher.settings


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
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
import it.samuelelonghin.safelauncher.drawer.DrawerActivity
import it.samuelelonghin.safelauncher.home.widgets.WidgetFragment
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo.WidgetType
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial
import it.samuelelonghin.safelauncher.info.EmptyActivity
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

        fragmentTransaction.add(R.id.widgets_settings, widgetFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    override fun setOnClicks() {
        /**
         * Contacts
         */
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
        binding.settingsContactsIsScrollableInput.isChecked =
            launcherPreferences.getBoolean(CONTACTS_IS_SCROLLABLE, CONTACTS_IS_SCROLLABLE_PREF)
        binding.settingsContactsIsScrollableInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                CONTACTS_IS_SCROLLABLE, binding.settingsContactsIsScrollableInput.isChecked
            )
        }

        /**
         * View Contact
         */
        //Rapid Call
        binding.settingsViewContactShowRapidCallInput.isChecked = launcherPreferences.getBoolean(
            VIEW_CONTACT_SHOW_RAPID_CALL, VIEW_CONTACT_SHOW_RAPID_CALL_PREF
        )
        binding.settingsViewContactShowRapidCallInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                VIEW_CONTACT_SHOW_RAPID_CALL,
                binding.settingsViewContactShowRapidCallInput.isChecked
            )
        }
        //Rapid Chat
        binding.settingsViewContactShowRapidChatInput.isChecked = launcherPreferences.getBoolean(
            VIEW_CONTACT_SHOW_RAPID_CHAT, VIEW_CONTACT_SHOW_RAPID_CHAT_PREF
        )
        binding.settingsViewContactShowRapidChatInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                VIEW_CONTACT_SHOW_RAPID_CHAT,
                binding.settingsViewContactShowRapidChatInput.isChecked
            )
        }
        //Notifications
        binding.settingsViewContactShowNotificationsInput.isChecked =
            launcherPreferences.getBoolean(
                VIEW_CONTACT_SHOW_NOTIFICATIONS, VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF
            )
        binding.settingsViewContactShowNotificationsInput.setOnCheckedChangeListener { _, checked ->
            println(checked)
            updatePreference(
                VIEW_CONTACT_SHOW_NOTIFICATIONS,
                binding.settingsViewContactShowNotificationsInput.isChecked
            )
        }
        // Rapid Chat App
        val appInt: Int = VIEW_CONTACT_RAPID_APP_TO_INDEX[launcherPreferences.getString(
            VIEW_CONTACT_RAPID_CHAT_APP, VIEW_CONTACT_RAPID_CHAT_APP_PREF
        )]!!
        binding.settingsViewContactRapidChatAppInput.setSelection(appInt)
        binding.settingsViewContactRapidChatAppInput.onItemSelectedListener =
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


        binding.widgetsButton.setOnClickListener {
            println("Cliccato widget")

            val intent = Intent(activity, ListActivity::class.java)
            intent.putExtra("intention", "pick")
            intent.putExtra("forApp", "0") // for which action we choose the app
            intent.putExtra("index", 100) // for which action we choose the app
            intendedSettingsPause = true
            selectApp.launch(intent)
        }
    }


}