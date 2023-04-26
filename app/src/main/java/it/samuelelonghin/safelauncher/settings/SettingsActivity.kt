package it.samuelelonghin.safelauncher.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import it.samuelelonghin.safelauncher.databinding.SettingsBinding
import it.samuelelonghin.safelauncher.support.*


class SettingsActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: SettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        val view = binding.root

        // Initialise layout
        setContentView(view)
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
        binding.settingsContainer.setBackgroundColor(dominantColor)
        binding.settingsAppbar.setBackgroundColor(dominantColor)
    }

    override fun setOnClicks() {
        // As older APIs somehow do not recognize the xml defined onClick
        binding.settingsClose.setOnClickListener { finish() }
        // open device settings (see https://stackoverflow.com/a/62092663/12787264)
        binding.settingsSystem.setOnClickListener {
            intendedSettingsPause = true
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

}