package it.samuelelonghin.safelauncher.info

import android.content.Context
import android.content.Intent
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
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.ViewContactFrameBinding
import it.samuelelonghin.safelauncher.home.contacts.ContactInfo
import it.samuelelonghin.safelauncher.support.checkUserCanCall
import it.samuelelonghin.safelauncher.support.getSerializable

class ViewContactFragment :
    Fragment(R.layout.view_contact_frame),
    LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {


    /**
     * View
     */
    private lateinit var binding: ViewContactFrameBinding
    private lateinit var _view: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = ViewContactFrameBinding.inflate(inflater)
        _view = binding.root
        println("ViewContactFragment :: CreateView")

        // Get the contact
        val contact: ContactInfo =
            getSerializable(requireActivity().intent, "contact", ContactInfo::class.java)
        contact.setContext(requireContext())


        binding.textViewViewContact.text = contact.name

        if (contact.photoURI != null)
            binding.imageViewViewContact.setImageBitmap(contact.getPhotoBitmap())
        else
            binding.imageViewViewContact.setImageDrawable(null)

        val handleCall = View.OnClickListener {
            if (checkUserCanCall(requireActivity())) {
                val intent = Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:${contact.mobileNumber}")
                )
                startActivity(intent)
            }
        }
        val handleChat = View.OnClickListener {
            println("WH")
            val url = "https://api.whatsapp.com/send?phone=${contact.mobileNumber}"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        val handleBack = View.OnClickListener {
            this.requireActivity().finish()
        }

        binding.buttonCall.setOnClickListener(handleCall)
        binding.imageView2.setOnClickListener(handleCall)
        binding.imageViewBack.setOnClickListener(handleBack)

        binding.backLayout.setOnClickListener(handleBack)
        binding.backButton.setOnClickListener(handleBack)

        binding.buttonOpenWhatsapp.setOnClickListener(handleChat)

        return _view
    }


    override fun onStart() {
        super.onStart()
        System.out.println("ViewContactFragment :: Start")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.out.println("ViewContactFragment :: Create")
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        System.out.println("ViewContactFragment :: CreateLoader")
        return activity?.let {
            CursorLoader(requireContext())
        } ?: throw IllegalStateException()
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        System.out.println("ViewContactFragment :: LoadFinished")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        System.out.println("ViewContactFragment :: LoaderReset")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        System.out.println("ViewContactFragment :: ItemClick")
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        System.out.println("ViewContactFragment :: Inflate")
    }
}
