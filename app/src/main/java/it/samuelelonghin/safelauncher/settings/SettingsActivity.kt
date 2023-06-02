package it.samuelelonghin.safelauncher.settings

import android.os.Bundle
import it.samuelelonghin.safelauncher.databinding.SettingsBinding
import it.samuelelonghin.safelauncher.support.BaseActivity
import it.samuelelonghin.safelauncher.support.intendedSettingsPause


class SettingsActivity : BaseActivity() {
    private lateinit var binding: SettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onResume() {
        super.onResume()
        intendedSettingsPause = false
    }

    override fun onPause() {
        super.onPause()
        if (!intendedSettingsPause) finish()
    }

    override fun setOnClicks() {
        binding.backLayout.root.setOnClickListener { finish() }
    }
}