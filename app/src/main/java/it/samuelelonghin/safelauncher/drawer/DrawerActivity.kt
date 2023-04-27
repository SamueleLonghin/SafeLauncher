package it.samuelelonghin.safelauncher.drawer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.DrawerActivityBinding
import it.samuelelonghin.safelauncher.databinding.HomeBinding
import it.samuelelonghin.safelauncher.support.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


var intendedChoosePause = false // know when to close

// TODO: Better solution for this intercommunication fuctionality (used in list-fragments)
var intention = "view"
var forApp = ""

/**
 * The [ListActivity] is the most general purpose activity in Launcher:
 * - used to view all apps and edit their settings
 * - used to choose an app / intent to be launched
 *
 * The activity itself can also be chosen to be launched as an action.
 */
class DrawerActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: DrawerActivityBinding

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("Drawer :: OnCreate")
        binding = DrawerActivityBinding.inflate(layoutInflater)

        // Initialise layout
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        intendedSettingsPause = false
        if (!intendedChoosePause) finish()
    }

    override fun onResume() {
        super.onResume()
        intendedChoosePause = false
    }

    //
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UNINSTALL) {
            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(this, getString(R.string.list_removed), Toast.LENGTH_LONG).show()
                finish()
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
//                Toast.makeText(this, getString(R.string.list_not_removed), Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
    override fun setOnClicks() {
//        binding.back.setOnClickListener() { finish() }
    }
//
//    override fun adjustLayout() {
//        // get info about which action this activity is open for
//        val bundle = intent.extras
//        if (bundle != null) {
//            intention = bundle.getString("intention")!! // why choose an app
//            if (intention != "view")
//                forApp = bundle.getString("forApp")!! // which app we choose
//        }
//
//        // Hide tabs for the "view" action
//        if (intention == "view") {
//            list_tabs.visibility = View.GONE
//        }
//
//        when (intention) {
//            "view" -> list_heading.text = getString(R.string.list_title_view)
//            "pick" -> list_heading.text = getString(R.string.list_title_pick)
//        }
//
//        val sectionsPagerAdapter = ListSectionsPagerAdapter(this, supportFragmentManager)
//        val viewPager: ViewPager = findViewById(R.id.list_viewpager)
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = findViewById(R.id.list_tabs)
//        tabs.setupWithViewPager(viewPager)
//    }
    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
        println("Drawer :: onStart")
    }
}
//
//private val TAB_TITLES = arrayOf(
//    R.string.list_tab_app,
//    R.string.list_tab_other
//)
//
///**
// * The [ListSectionsPagerAdapter] returns the fragment,
// * which corresponds to the selected tab in [ListActivity].
// */
//class ListSectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
//    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//    override fun getItem(position: Int): Fragment {
//        return when (position) {
//            0 -> ListFragmentApps()
//            1 -> ListFragmentOther()
//            else -> Fragment()
//        }
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return context.resources.getString(TAB_TITLES[position])
//    }
//
//    override fun getCount(): Int {
//        return when (intention) {
//            "view" -> 1
//            else -> 2
//        }
//    }
//}