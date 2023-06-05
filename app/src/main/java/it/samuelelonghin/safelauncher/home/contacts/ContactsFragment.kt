package it.samuelelonghin.safelauncher.home.contacts

import android.content.Context
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.ContactsFrameBinding
import it.samuelelonghin.safelauncher.settings.registerNotificationSwitch
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
    private var cursorAdapter: ContactCursorGridAdapter? = null

    private val sharedPreferenceChangeListener = OnSharedPreferenceChangeListener { _, key ->
        if (key in CONTACTS_PREFERENCES) {
            displayContacts()
        }
    }


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
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//            if (isGranted) {
//                getContacts()
//                Toast.makeText(
//                    context,
//                    getString(R.string.permesso_contatti_granted),
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                Toast.makeText(
//                    context,
//                    getString(R.string.permesso_contatti_not_granted),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }

//        localActivityResult =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//                if (result.resultCode == AppCompatActivity.RESULT_OK) {
//                    getContacts()
//                    Toast.makeText(
//                        context,
//                        getString(R.string.permesso_contatti_granted),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        context,
//                        getString(R.string.permesso_contatti_not_granted),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }

        //Getting contacts from OS
        getContacts()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        //Displaying contacts
        displayContacts()
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


//            val intent = Intent(activity, RequestContactsActivity::class.java)
//            localActivityResult.launch(intent)

//            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            return
        }

        /**
         * Ottengo i contatti
         */
        println("Tabella: " + ContactsContract.Contacts.CONTENT_URI)
        contactCursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            "starred=?"
//            "${ContactsContract.Contacts.STARRED}=?"
//                    + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1"
            ,
            arrayOf("1"),
            null
        )!!
    }

    private fun displayContacts() {
        val context = context ?: return
        val nCols = launcherPreferences.getInt(
            CONTACTS_NUMBER_COLUMNS, CONTACTS_NUMBER_COLUMNS_PREF
        )

        val isScrollable = launcherPreferences.getBoolean(
            CONTACTS_IS_SCROLLABLE, CONTACTS_IS_SCROLLABLE_PREF
        )

        val layoutManager = object : GridLayoutManager(context, nCols) {
            override fun canScrollVertically() = isScrollable
        }

        val contactsRecycleView = binding.listContacts
        contactsRecycleView.layoutManager = layoutManager
        if (::contactCursor.isInitialized) {
            cursorAdapter = ContactCursorGridAdapter(context, contactCursor)
            contactsRecycleView.adapter = cursorAdapter
        } else {
            System.err.println("Cursor not loaded in ContactsFragment")
            contactsRecycleView.adapter =
                ContactPlaceholderCursorAdapter(
                    context,
                    nCols * (if (isScrollable) 3 else 2),
                    permissionLauncher
                )
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}
