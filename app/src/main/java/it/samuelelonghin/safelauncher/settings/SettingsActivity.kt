package it.samuelelonghin.safelauncher.settings;

import android.app.Activity;
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.*
import kotlinx.android.synthetic.main.settings.*

var intendedSettingsPause = false // know when to close

class SettingsActivity : AppCompatActivity(), UIObject {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise layout
        setContentView(R.layout.settings)

        // set up tabs and swiping in settings
        val viewPager: ViewPager = findViewById(R.id.settings_viewpager)
        val tabs: TabLayout = findViewById(R.id.settings_tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
    }

    override fun onResume() {
        super.onResume()
        intendedSettingsPause = false
    }

    override fun onPause() {
        super.onPause()
        if (!intendedSettingsPause) finish()
    }

    override fun applyTheme() {
        settings_container.setBackgroundColor(dominantColor)
        settings_appbar.setBackgroundColor(dominantColor)

        settings_system.setTextColor(vibrantColor)
        settings_close.setTextColor(vibrantColor)
        settings_tabs.setSelectedTabIndicatorColor(vibrantColor)
    }

    override fun setOnClicks() {
        // As older APIs somehow do not recognize the xml defined onClick
        settings_close.setOnClickListener() { finish() }
        // open device settings (see https://stackoverflow.com/a/62092663/12787264)
        settings_system.setOnClickListener {
            intendedSettingsPause = true
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHOOSE_APP -> saveListActivityChoice(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}