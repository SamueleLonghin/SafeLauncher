package it.samuelelonghin.safelauncher.list.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import it.samuelelonghin.safelauncher.R
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

    override fun adjustLayout() {

        val viewType = launcherPreferences.getInt(
            APPS_LIST_VIEW_TYPE, APPS_LIST_VIEW_TYPE_PREF
        )
        val lm: LayoutManager = if (viewType == 0) LinearLayoutManager(context)
        else GridLayoutManager(context, 3)

        val view = if (viewType == 0) R.layout.list_apps_row else R.layout.list_apps_card
        val appsAdapter = AppsRecyclerAdapter(requireActivity(), intention, index, view)
        // set up the list / recycler
        binding.listAppsRview.apply {
            // improve performance (since content changes don't change the layout size)
            setHasFixedSize(true)
            layoutManager = lm
            adapter = appsAdapter
        }

        binding.listAppsSearchview.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                appsAdapter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                appsAdapter.filter(newText)
                return false
            }
        })

        if (intention == "view" && launcherPreferences.getBoolean(
                OPEN_KEYBOARD_ON_SEARCH,
                OPEN_KEYBOARD_ON_SEARCH_DEF
            )
        ) {
            openSoftKeyboard(requireContext(), binding.listAppsSearchview)
        }
    }
}