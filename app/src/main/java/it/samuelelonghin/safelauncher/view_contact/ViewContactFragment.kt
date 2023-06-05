package it.samuelelonghin.safelauncher.view_contact

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.ViewContactFrameBinding
import it.samuelelonghin.safelauncher.home.contacts.ContactInfo
import it.samuelelonghin.safelauncher.support.*


class ViewContactFragment : Fragment(R.layout.view_contact_frame),
    LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    /**
     * View
     */
    private lateinit var binding: ViewContactFrameBinding

    private lateinit var contact: ContactInfo

    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = ViewContactFrameBinding.inflate(inflater)

        // Get the contact
        contact = getContactFromIntent()

        setValues()
        setOnClicks()
        setNotifications()
        return binding.root
    }

    private fun setValues() {
        binding.textViewViewContact.text = contact.name

        if (contact.photoURI != null) {
            contact.setPhoto(binding.imageViewViewContact)
        } else
            binding.imageViewViewContact.setImageDrawable(
                setIconTintPrimary(
                    this.requireContext(),
                    contact.getRandomDrawable()
                )
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
            NotificationsAdapter(requireActivity(), contact.name, userNotifications)
        binding.listViewNotification.layoutManager = LinearLayoutManager(context)
    }

    private fun setOnClicks() {
        val handleCall = View.OnClickListener {
            if (canCall(requireActivity())) {
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
            this.requireActivity().finish()
        }

        binding.buttonRapidCallV.setOnClickListener(handleCall)
        binding.buttonRapidCallH.setOnClickListener(handleCall)
        binding.buttonRapidChatV.setOnClickListener(handleChat)
        binding.buttonRapidChatH.setOnClickListener(handleChat)
        binding.backLayout.root.setOnClickListener(handleBack)
    }

    private fun getContactFromIntent(): ContactInfo {
        val contact = getSerializable(requireActivity().intent, "contact", ContactInfo::class.java)
        contact.setContext(requireContext())

        return contact
    }

    override fun onStart() {
        super.onStart()
        println("ViewContactFragment :: Start")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("ViewContactFragment :: Create")
        registerReceiver()
    }

    override fun onStop() {
        super.onStop()
        if (::broadcastReceiver.isInitialized)
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(broadcastReceiver)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        println("ViewContactFragment :: CreateLoader")
        return activity?.let {
            CursorLoader(requireContext())
        } ?: throw IllegalStateException()
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

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        println("ViewContactFragment :: Inflate")
    }

    private fun registerReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.extras != null) {
                    val myData = getBundleAsJson(intent.extras!!)
                    println("Not Broadcast: $myData")
                }
            }
        }
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(broadcastReceiver, IntentFilter(BROADCAST_NOTIFICATIONS))
    }
}
