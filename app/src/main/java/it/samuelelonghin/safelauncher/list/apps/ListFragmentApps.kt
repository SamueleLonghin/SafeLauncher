package it.samuelelonghin.safelauncher.list.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.samuelelonghin.safelauncher.databinding.ListAppsBinding
import it.samuelelonghin.safelauncher.support.*


/**
 * The [ListFragmentApps] is used as a tab in ListActivity.
 *
 * It is a list of all installed applications that are can be launched.
 */
class ListFragmentApps(private val intention: String, private val index: Int = DEFAULT_INDEX) :
    Fragment(),
    UIObject {
    private lateinit var binding: ListAppsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListAppsBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super<Fragment>.onStart()
        super<UIObject>.onStart()
    }

    override fun setOnClicks() {}

    override fun adjustLayout() {

        val appsRViewAdapter = AppsRecyclerAdapter(requireActivity(), intention, index)

        // set up the list / recycler
        binding.listAppsRview.apply {
            // improve performance (since content changes don't change the layout size)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = appsRViewAdapter
        }

        binding.listAppsSearchview.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                appsRViewAdapter.filter(query);
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                appsRViewAdapter.filter(newText);
                return false
            }
        })

        if (intention == "view" && launcherPreferences.getBoolean(
                PREF_SEARCH_AUTO_KEYBOARD,
                true
            )
        ) {
            openSoftKeyboard(requireContext(), binding.listAppsSearchview)
        }
    }
}