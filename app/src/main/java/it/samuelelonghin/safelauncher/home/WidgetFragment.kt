package it.samuelelonghin.safelauncher.home

import android.content.Context
import android.database.Cursor
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
import it.samuelelonghin.safelauncher.databinding.ContactsFrameBinding

class WidgetFragment :
    Fragment(R.layout.widget_frame) {

    /**
     * View
     */
    private lateinit var binding: ContactsFrameBinding
    private lateinit var _view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = ContactsFrameBinding.inflate(inflater)
        _view = binding.root
        System.out.println("WidgetFragement :: CreateView")

        return _view
    }


    override fun onStart() {
        super.onStart()
        System.out.println("WidgetFragement :: Start")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.out.println("WidgetFragement :: Create")
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        System.out.println("WidgetFragement :: Inflate")
    }
}