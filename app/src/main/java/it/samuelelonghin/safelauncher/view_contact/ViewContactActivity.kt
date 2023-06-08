package it.samuelelonghin.safelauncher.view_contact

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.DynamicColors
import it.samuelelonghin.safelauncher.databinding.ViewContactActivityBinding
import it.samuelelonghin.safelauncher.home.contacts.ContactInfo
import it.samuelelonghin.safelauncher.support.*


class ViewContactActivity : BaseActivity(), LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {

    private lateinit var binding: ViewContactActivityBinding

    private lateinit var contact: ContactInfo

    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewContactActivityBinding.inflate(layoutInflater)
        val view = binding.root

        view.minimumHeight = 400
        view.minimumWidth = 300


        // Get the contact
        contact = getContactFromIntent()

        setValues()
        setOnClicks()
        setNotifications()

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        DynamicColors.applyToActivityIfAvailable(this)
        setContentView(view)

        val metrics = resources.displayMetrics
        val screenWidth = (metrics.widthPixels * 0.90).toInt()
        val screenHeight = (metrics.heightPixels * 0.90).toInt()
        window.setLayout(screenWidth, screenHeight)

        createReceiver()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver()
    }

    private fun setValues() {
        binding.textViewViewContact.text = contact.name

        if (contact.photoURI != null) {
            contact.setPhoto(binding.imageViewViewContact)
        } else
            binding.imageViewViewContact.setImageDrawable(
                setIconTintPrimary(this, contact.getRandomDrawable())
            )

        if (!launcherPreferences.getBoolean(
                VIEW_CONTACT_BUTTONS_DIRECTION, VIEW_CONTACT_BUTTONS_DIRECTION_PREF
            )
        ) {
            binding.callLayoutVertical.visibility = View.VISIBLE
            binding.callLayoutHorizontal.visibility = View.GONE
        } else {
            binding.callLayoutVertical.visibility = View.GONE
            binding.callLayoutHorizontal.visibility = View.VISIBLE
        }


        // Rapid Call
        if (!launcherPreferences.getBoolean(
                VIEW_CONTACT_SHOW_RAPID_CALL, VIEW_CONTACT_SHOW_RAPID_CALL_PREF
            )
        ) {
            binding.buttonRapidCallV.visibility = View.INVISIBLE
            binding.buttonRapidCallH.visibility = View.INVISIBLE
        }

        // Rapid Chat
        if (!launcherPreferences.getBoolean(
                VIEW_CONTACT_SHOW_RAPID_CHAT, VIEW_CONTACT_SHOW_RAPID_CHAT_PREF
            )
        ) {
            binding.buttonRapidChatV.visibility = View.INVISIBLE
            binding.buttonRapidChatH.visibility = View.INVISIBLE
        }

        // Notifications
        if (!launcherPreferences.getBoolean(
                VIEW_CONTACT_SHOW_NOTIFICATIONS, VIEW_CONTACT_SHOW_NOTIFICATIONS_PREF
            )
        ) binding.listViewNotification.visibility = View.INVISIBLE

    }

    private fun setNotifications() {
        val userNotifications = getUserNotifications(contact.name)

        println(this.contact.name + " -> " + userNotifications + "Tutte: " + notifiche)
        binding.listViewNotification.adapter =
            NotificationsAdapter(this, contact.name, userNotifications)
        binding.listViewNotification.layoutManager = LinearLayoutManager(this)
    }

    override fun setOnClicks() {
        val handleCall = View.OnClickListener {
            if (canCall(this)) {
                val intent = Intent(
                    Intent.ACTION_CALL, Uri.parse("tel:${contact.mobileNumber}")
                )
                startActivity(intent)
            }
        }
        val handleChat = View.OnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            val url: String
            when (launcherPreferences.getString(
                VIEW_CONTACT_RAPID_CHAT_APP, VIEW_CONTACT_RAPID_CHAT_APP_PREF
            )) {
                "WhatsApp" -> {
                    url = VIEW_CONTACT_RAPID_APP_URL["WhatsApp"]!!
                }
                "Telegram" -> {
                    url = VIEW_CONTACT_RAPID_APP_URL["Telegram"]!!
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.setPackage("org.telegram.messenger")
                }
                else -> {
                    url = VIEW_CONTACT_RAPID_APP_URL["Default"]!!
                }
            }
            i.data = Uri.parse(url.format(contact.mobileNumber))
            startActivity(i)
        }
        val handleBack = View.OnClickListener {
            this.finish()
        }

        binding.buttonRapidCallV.setOnClickListener(handleCall)
        binding.buttonRapidCallH.setOnClickListener(handleCall)
        binding.buttonRapidChatV.setOnClickListener(handleChat)
        binding.buttonRapidChatH.setOnClickListener(handleChat)
        binding.backLayout.root.setOnClickListener(handleBack)
    }

    private fun getContactFromIntent(): ContactInfo {
        val contact = getSerializable(intent, "contact", ContactInfo::class.java)
        contact.setContext(this)
        return contact
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        println("ViewContactFragment :: CreateLoader")
        return CursorLoader(this)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        println("ViewContactFragment :: LoadFinished")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        println("ViewContactFragment :: LoaderReset")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("ViewContactFragment :: ItemClick")
    }


    private fun createReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.extras != null) {
                    val user = intent.extras!!.getString("targetUser")
                    if (user != null && user == contact.name)
                        setNotifications()
                }
            }
        }
    }

    private fun registerReceiver() {
        if (::broadcastReceiver.isInitialized)
            LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, IntentFilter(BROADCAST_NOTIFICATIONS))
    }

    private fun unregisterReceiver() {

        if (::broadcastReceiver.isInitialized)
            LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(broadcastReceiver)
    }
}