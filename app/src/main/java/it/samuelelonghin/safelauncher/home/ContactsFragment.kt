package it.samuelelonghin.safelauncher.home


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
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
    private var cursorAdapter: ContactCursorAdapter? = null

    // A UI Fragment must inflate its View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        contactCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "starred=?",
            arrayOf("1"),
            null
        )!!
        // creation of adapter using ContactCursorAdapter class
        cursorAdapter = ContactCursorAdapter(context, contactCursor, _view)
        // Calling setAdaptor() method to set created adapter
        binding.listViewContacts.adapter = cursorAdapter

    }
}
