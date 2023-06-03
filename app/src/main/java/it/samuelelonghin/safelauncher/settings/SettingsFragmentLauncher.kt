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
        activityResultEnableFullScreen =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (activity != null)
                    registerNotificationSwitch(
                        binding.homeSettings.settingsHomeForceFullScreenInput,
                        requireActivity()
                    )
            }
        activityResultNotificationAccess =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (activity != null)
                    registerNotificationSwitch(
                        binding.viewContactSettings.settingsViewContactShowNotificationsInput,
                        requireActivity()
                    )
            }
        return binding.root
    }

    override fun onStart() {
        super<Fragment>.onStart()
        super<UIObject>.onStart()
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
        setHome(binding.homeSettings, requireActivity())

        /**
         * Contacts
         */
        setContacts(binding.contactsSettings, requireActivity())

        /**
         * View Contact
         */
        setViewContacts(binding.viewContactSettings, requireActivity())

        /**
         * Widgets
         */

        setWidgets(binding.widgetsSettings, requireActivity())

        /**
         * Apps List
         */
        setAppList(binding.listAppsSettings, requireActivity())

    }
}