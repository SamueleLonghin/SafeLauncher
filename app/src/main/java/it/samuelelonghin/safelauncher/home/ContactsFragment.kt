package it.samuelelonghin.safelauncher.home

import android.Manifest
import android.content.Context
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
    lateinit var contactCursor: Cursor

    // Request code for READ_CONTACTS. It can be any number > 0.
    private var cursorAdapter: ContactCursorGridAdapter? = null

    // A UI Fragment must inflate its View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = ContactsFrameBinding.inflate(inflater)
        _view = binding.root
        System.out.println("ContactsFragement :: CreateView")
        getContacts()

        return _view
    }


    override fun onStart() {
        super.onStart()
        System.out.println("ContactsFragement :: Start")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.out.println("ContactsFragement :: Create")
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        System.out.println("ContactsFragement :: CreateLoader")
        return activity?.let {
            CursorLoader(requireContext())
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        System.out.println("ContactsFragement :: LoadFinished")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        System.out.println("ContactsFragement :: LoaderReset")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        System.out.println("ContactsFragement :: ItemClick")
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        System.out.println("ContactsFragement :: Inflate")
    }

    private fun getContacts() {
        val context = requireContext()


        if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            return
        }


        // create cursor and query the data
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

        /**
         * Versione che boh
         */
//        val PROJECTION: Array<String?> = arrayOf(
//            ContactsContract.Contacts._ID,
//            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,  // Honeycomb+ should use this
//            ContactsContract.CommonDataKinds.Phone.NUMBER,
////            ContactsContract.Contacts.DISPLAY_NAME,
//            ContactsContract.Contacts.PHOTO_URI,
//            ContactsContract.Contacts.HAS_PHONE_NUMBER,
//        )
//        contactCursor = context.contentResolver.query(
//            ContactsContract.Contacts.CONTENT_URI,
//            null,
//            null,
//            null,
//            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " COLLATE NOCASE ASC"
//        )!!

        // creation of adapter using ContactCursorAdapter class
//        cursorAdapter = ContactCursorAdapter(context, contactCursor, _view)
        cursorAdapter = ContactCursorGridAdapter(context, contactCursor)
        // Calling setAdaptor() method to set created adapter
//        binding.listViewContacts.adapter = cursorAdapter


        var courseRV = binding.idRVCourses

        // on below line we are initializing our list

        // on below line we are creating a variable
        // for our grid layout manager and specifying
        // column count as 2
        val layoutManager = GridLayoutManager(context, 2)

        courseRV.layoutManager = layoutManager

        // on below line we are initializing our adapter

        // on below line we are setting
        // adapter to our recycler view.
        courseRV.adapter = cursorAdapter

        // on below line we are adding data to our list
//        courseList.add(CourseRVModal("Android Development", R.drawable.android))
//        courseList.add(CourseRVModal("C++ Development", R.drawable.c))
//        courseList.add(CourseRVModal("Java Development", R.drawable.java))
//        courseList.add(CourseRVModal("Python Development", R.drawable.python))
//        courseList.add(CourseRVModal("JavaScript Development", R.drawable.js))
//
//        // on below line we are notifying adapter
//        // that data has been updated.
//        courseRVAdapter.notifyDataSetChanged()

    }
}
