package it.samuelelonghin.safelauncher.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import it.samuelelonghin.safelauncher.databinding.SettingsBinding
import it.samuelelonghin.safelauncher.support.*


class SettingsActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: SettingsBinding
    private lateinit var auth: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    override fun setOnClicks() {
        binding.backLayout.root.setOnClickListener { finish() }
    }
}