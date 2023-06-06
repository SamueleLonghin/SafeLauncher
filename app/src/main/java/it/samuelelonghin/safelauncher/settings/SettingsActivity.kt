package it.samuelelonghin.safelauncher.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.SettingsBinding
import it.samuelelonghin.safelauncher.home.widgets.WidgetFragment
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetSerial
import it.samuelelonghin.safelauncher.support.*


class SettingsActivity : BaseActivity() {
    private lateinit var binding: SettingsBinding
    private lateinit var selectApp: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)

        setLayout()
        selectApp =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == REQUEST_CHOOSE_APP) {
                    val extras = result.data!!.extras!!

                    val i = extras.getInt("index")
                    val typeId = extras.getInt("type")
                    val t: WidgetInfo.WidgetType = WidgetInfo.WidgetType.values()[typeId]
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
                registerNotificationSwitch(
                    binding.homeSettings.settingsHomeForceFullScreenInput,
                    this
                )
            }
        activityResultNotificationAccess =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                registerNotificationSwitch(
                    binding.viewContactSettings.settingsViewContactShowNotificationsInput,
                    this
                )
            }
        setContentView(binding.root)
    }


    override fun onResume() {
        super.onResume()
        intendedSettingsPause = false
    }

    override fun onPause() {
        super.onPause()
        if (!intendedSettingsPause) finish()
    }


    private fun setLayout() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val widgetFragment = WidgetFragment()
        widgetFragment.mode = WidgetFragment.Mode.PICK

        fragmentTransaction.add(R.id.widgets_frame, widgetFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    override fun setOnClicks() {
        binding.backLayout.root.setOnClickListener { finish() }

        /**
         * Home
         */
        setHome(binding.homeSettings, this)

        /**
         * Contacts
         */
        setContacts(binding.contactsSettings, this)

        /**
         * View Contact
         */
        setViewContacts(binding.viewContactSettings, this)

        /**
         * Widgets
         */

        setWidgets(binding.widgetsSettings, this)

        /**
         * Apps List
         */
        setAppList(binding.listAppsSettings, this)

    }
}