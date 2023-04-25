package it.samuelelonghin.safelauncher.home.contacts

import android.Manifest
import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.PackageManager
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
import androidx.recyclerview.widget.GridLayoutManager
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.ContactsFrameBinding
import it.samuelelonghin.safelauncher.support.CONTACTS_NUMBER_COLUMNS
import it.samuelelonghin.safelauncher.support.contactsNumberColumns
import it.samuelelonghin.safelauncher.support.launcherPreferences


class ContactsFragment :
    Fragment(R.layout.contacts_frame),
    LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getContacts()
            Toast.makeText(
                context,
                "Permission granted!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Until you grant the permission, we cannot display the names",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * View
     */
    private lateinit var binding: ContactsFrameBinding
    private lateinit var _view: View


    // An adapter that binds the result Cursor to the ListView
    private lateinit var contactCursor: Cursor

    // Request code for READ_CONTACTS. It can be any number > 0.
    private var cursorAdapter: ContactCursorGridAdapter? = null

    private val sharedPreferenceChangeListener = OnSharedPreferenceChangeListener { _, key ->
        if (key == CONTACTS_NUMBER_COLUMNS) {
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
        _view = binding.root
        println("ContactsFragement :: CreateView")

        //Getting contacts from OS
        getContacts()

        return _view
    }


    override fun onResume() {
        super.onResume()
        println("ContactsFragement :: resume")

        launcherPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        //Displaying contacts
        displayContacts()
    }

    override fun onStart() {
        super.onStart()
        println("ContactsFragement :: Start")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("ContactsFragement :: Create")
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        println("ContactsFragement :: CreateLoader")
        return activity?.let {
            CursorLoader(requireContext())
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        println("ContactsFragement :: LoadFinished")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        println("ContactsFragement :: LoaderReset")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("ContactsFragement :: ItemClick")
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        println("ContactsFragement :: Inflate")
    }

    private fun getContacts() {
        val context = requireContext()

        if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            return
        }


        /**
         * Versione dove trova duplicati in base a google / whatsapp ...
         */
        contactCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "starred=?" + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1",
            arrayOf("1"),
            null
        )!!
    }

    private fun displayContacts() {
        val context = requireContext()
        if (::contactCursor.isInitialized) {
            cursorAdapter = ContactCursorGridAdapter(context, contactCursor)

            val courseRV = binding.idRVCourses

            // on below line we are initializing our list

            // on below line we are creating a variable
            // for our grid layout manager and specifying
            // column count as 2
            val layoutManager = GridLayoutManager(context, contactsNumberColumns)

            courseRV.layoutManager = layoutManager

            // on below line we are initializing our adapter

            // on below line we are setting
            // adapter to our recycler view.
            courseRV.adapter = cursorAdapter
        } else {
            System.err.println("Cursor not loaded in ContactsFragment")
            //todo display placeholder
        }
    }
}
