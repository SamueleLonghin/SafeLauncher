package it.samuelelonghin.safelauncher.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.ListActivityBinding
import it.samuelelonghin.safelauncher.support.DEFAULT_INDEX
import it.samuelelonghin.safelauncher.support.UIObject
import it.samuelelonghin.safelauncher.support.intendedSettingsPause

var intendedChoosePause = false // know when to close


/**
 * The [ListActivity] is the most general purpose activity in Launcher:
 * - used to view all apps and edit their settings
 * - used to choose an app / intent to be launched
 *
 * The activity itself can also be chosen to be launched as an action.
 */
class ListActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: ListActivityBinding

    private var intention: String = "view"
    private var index: Int = DEFAULT_INDEX
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super<AppCompatActivity>.onStart()
        // get info about which action this activity is open for
        val bundle = intent.extras
        if (bundle != null) {
            intention = bundle.getString("intention")!! // why choose an app
            if (intention != "view") {
//                forApp = bundle.getString("forApp")!! // which app we choose
                index = bundle.getInt("index") // which app we choose
            }
        }
        super<UIObject>.onStart()
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

    override fun setOnClicks() {
        binding.backLayout.root.setOnClickListener { finish() }
    }

    override fun adjustLayout() {
        // Hide tabs for the "view" action
        if (intention == "view") {
            binding.listTabs.visibility = View.GONE
        }

        when (intention) {
            "view" -> binding.listHeading.text = getString(R.string.list_title_view)
            "pick" -> binding.listHeading.text = getString(R.string.list_title_pick)
        }

        val sectionsPagerAdapter =
            ListSectionsPagerAdapter(this, supportFragmentManager, intention, index)
        val viewPager: ViewPager = findViewById(R.id.list_viewpager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.list_tabs)
        tabs.setupWithViewPager(viewPager)
    }
}
