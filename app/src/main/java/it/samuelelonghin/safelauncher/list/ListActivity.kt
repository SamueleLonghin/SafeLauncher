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

    /**
     * Rappresenta l'indice nel quale andare ad inserire il widget nel caso l'intention sia pick
     */
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
                index = bundle.getInt("index") // which app we choose
            }
        }
        super<UIObject>.onStart()
    }

    override fun onPause() {
        super.onPause()
        intendedSettingsPause = false
        finish()
    }

    override fun setOnClicks() {
        binding.backLayout.root.setOnClickListener { finish() }
    }

    override fun adjustLayout() {
        val viewPager: ViewPager = findViewById(R.id.list_viewpager)
        val tabs: TabLayout = findViewById(R.id.list_tabs)

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

        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
}
