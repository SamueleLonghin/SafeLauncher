package it.samuelelonghin.safelauncher.home.contacts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.ContactsFrameBinding
import it.samuelelonghin.safelauncher.support.*


class ContactsFragment :
    Fragment(R.layout.contacts_frame),
    LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {

    /**
     * View
     */
    private lateinit var binding: ContactsFrameBinding


    // An adapter that binds the result Cursor to the ListView
    private lateinit var contactCursor: Cursor

    // Request code for READ_CONTACTS. It can be any number > 0.
    private lateinit var cursorAdapter: RecyclerView.Adapter<ContactViewHolder>

    private val sharedPreferenceChangeListener = OnSharedPreferenceChangeListener { _, key ->
        if (key in CONTACTS_PREFERENCES) {
            displayContacts()
        }
    }

    private lateinit var broadcastReceiver: BroadcastReceiver


    // A UI Fragment must inflate its View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = ContactsFrameBinding.inflate(inflater)
        println("ContactsFragement :: CreateView")


        // Per passarlo all'adapter
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                println("RESULT: ${it.resultCode}")
                if (context != null && canReadContacts(requireContext())) {
                    println("ANDATOOOO: ${it.resultCode}")
                    getContacts()
                    displayContacts()
                    Toast.makeText(
                        context,
                        getString(R.string.permesso_contatti_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.permesso_contatti_not_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

        //Getting contacts from OS
        getContacts()

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createReceiver()
    }

    override fun onStart() {
        super.onStart()

        //Displaying contacts
        displayContacts()
        registerReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver()
    }

    override fun onResume() {
        super.onResume()
        println("ContactsFragement :: resume")

        launcherPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        println("ContactsFragement :: CreateLoader")
        return activity?.let {
            CursorLoader(requireContext())
        } ?: throw IllegalStateException()
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        println("ContactsFragement :: Inflate")
    }

    private fun getContacts() {
        val context = requireContext()

        if (!canReadContacts(context)) {
            return
        }

        /**
         * Ottengo i contatti
         */


        contactCursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            "${ContactsContract.Contacts.STARRED}=?",
            arrayOf("1"),
            null
        )!!

    }

    private fun displayContacts() {
        val context = context ?: return
        val contactsRecycleView = binding.listContacts

        val nCols = launcherPreferences.getInt(
            CONTACTS_NUMBER_COLUMNS, CONTACTS_NUMBER_COLUMNS_PREF
        )

        val isScrollable = launcherPreferences.getBoolean(
            CONTACTS_IS_SCROLLABLE, CONTACTS_IS_SCROLLABLE_PREF
        )

        contactsRecycleView.layoutManager = object : GridLayoutManager(context, nCols) {
            override fun canScrollVertically() = isScrollable
        }

        cursorAdapter = if (::contactCursor.isInitialized) {
            ContactCursorAdapter(context, contactCursor)
        } else {
            ContactPlaceholderAdapter(
                context,
                nCols * (if (isScrollable) 3 else 2),
                permissionLauncher
            )
        }
        contactsRecycleView.adapter = cursorAdapter
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }


    private fun createReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.extras != null) {
                    val myData = getBundleAsJson(intent.extras!!)
                    println("Not Broadcast: $myData")
                    displayContacts()
                }
            }
        }
    }

    private fun registerReceiver() {
        if (::broadcastReceiver.isInitialized)
            LocalBroadcastManager.getInstance(requireActivity())
                .registerReceiver(broadcastReceiver, IntentFilter(BROADCAST_NOTIFICATIONS))
    }

    private fun unregisterReceiver() {

        if (::broadcastReceiver.isInitialized)
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(broadcastReceiver)
    }
}
