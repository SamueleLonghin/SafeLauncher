package it.samuelelonghin.safelauncher.tutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.finnmglas.launcher.*
import com.finnmglas.launcher.tutorial.tabs.*
import com.google.android.material.tabs.TabLayout
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.*
import it.samuelelonghin.safelauncher.tutorial.tabs.TutorialFragmentConcept
import it.samuelelonghin.safelauncher.tutorial.tabs.TutorialFragmentFinish
import it.samuelelonghin.safelauncher.tutorial.tabs.TutorialFragmentSetup
import it.samuelelonghin.safelauncher.tutorial.tabs.TutorialFragmentStart

/**
 * The [TutorialActivity] is displayed automatically on new installations.
 * It can also be opened from Settings.
 *
 * It tells the user about the concept behind launcher
 * and helps with the setup process (on new installations)
 */
class TutorialActivity : AppCompatActivity(), UIObject {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise layout
        setContentView(R.layout.tutorial)

        // Check if the app was started before
        if (launcherPreferences.getBoolean(PREF_STARTED, false))
            tutorial_appbar.visibility = View.VISIBLE
        else resetSettings(this)

        loadSettings()

        // set up tabs and swiping in settings
        val sectionsPagerAdapter = TutorialSectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.tutorial_viewpager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tutorial_tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
    }

    override fun applyTheme() {
        tutorial_appbar.setBackgroundColor(dominantColor)
        tutorial_container.setBackgroundColor(dominantColor)
        tutorial_close.setTextColor(vibrantColor)
    }

    override fun setOnClicks() {
        tutorial_close.setOnClickListener() { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHOOSE_APP -> saveListActivityChoice(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Default: prevent going back, allow if viewed again later
    override fun onBackPressed() {
        if (launcherPreferences.getBoolean(PREF_STARTED, false))
            super.onBackPressed()
    }

}

/**
 * The [TutorialSectionsPagerAdapter] defines which fragments are shown when,
 * in the [TutorialActivity].
 *
 * Tabs: (Start | Concept | Usage | Setup | Finish)
 */
class TutorialSectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TutorialFragmentStart()
            1 -> TutorialFragmentConcept()
            2 -> TutorialFragmentUsage()
            3 -> TutorialFragmentSetup()
            4 -> TutorialFragmentFinish()
            else -> Fragment()
        }
    }

    /* We don't use titles here, as we have the dots */
    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun getCount(): Int {
        return 5
    }
}