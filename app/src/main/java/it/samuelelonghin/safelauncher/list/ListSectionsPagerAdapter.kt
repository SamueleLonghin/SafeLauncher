package it.samuelelonghin.safelauncher.list

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.list.apps.ListFragmentApps
import it.samuelelonghin.safelauncher.list.other.ListFragmentOther


/**
 * The [ListSectionsPagerAdapter] returns the fragment,
 * which corresponds to the selected tab in [ListActivity].
 */
class ListSectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    private val intention: String,
    val index: Int
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf(
        R.string.list_tab_app,
        R.string.list_tab_other
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ListFragmentApps(intention, index)
            1 -> ListFragmentOther(index)
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabTitles[position])
    }

    override fun getCount(): Int {
        return when (this.intention) {
            "view" -> 1
            else -> 2
        }
    }
}