package it.samuelelonghin.safelauncher.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.samuelelonghin.safelauncher.databinding.ListAppFragmentBinding
import it.samuelelonghin.safelauncher.support.PREF_SEARCH_AUTO_KEYBOARD
import it.samuelelonghin.safelauncher.support.UIObject
import it.samuelelonghin.safelauncher.support.launcherPreferences
import it.samuelelonghin.safelauncher.support.openSoftKeyboard

/**
 * The [ListAppFragment] is used as a tab in ListActivity.
 *
 * It is a list of all installed applications that are can be launched.
 */
class ListAppFragment : Fragment(), UIObject {
    private lateinit var binding: ListAppFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = ListAppFragmentBinding.inflate(inflater)
        println("ListAppFragment :: CreateView")

        return binding.root
    }

    override fun onStart() {
        super<Fragment>.onStart()
        super<UIObject>.onStart()
    }
}