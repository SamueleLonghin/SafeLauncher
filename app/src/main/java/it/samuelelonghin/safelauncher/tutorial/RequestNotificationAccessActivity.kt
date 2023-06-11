package it.samuelelonghin.safelauncher.tutorial

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import it.samuelelonghin.safelauncher.databinding.ActivityRequestNotificationAccessBinding
import it.samuelelonghin.safelauncher.support.BaseActivity

class RequestNotificationAccessActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestNotificationAccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestNotificationAccessBinding.inflate(layoutInflater)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.tutorialRequestNotificationAccessButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            startActivity(intent)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.tutorialRequestNotificationAccessBackButton.setOnClickListener {
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

}