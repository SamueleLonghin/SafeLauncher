package it.samuelelonghin.safelauncher.tutorial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import it.samuelelonghin.safelauncher.databinding.ActivityRequestFullScreenBinding
import it.samuelelonghin.safelauncher.databinding.ActivityRequestNotificationPolicyBinding
import it.samuelelonghin.safelauncher.support.*

class RequestNotificationPolicyActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestNotificationPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestNotificationPolicyBinding.inflate(layoutInflater)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.tutorialRequestFullScreenButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            startActivity(intent)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.tutorialRequestFullScreenBackButton.setOnClickListener {
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

}