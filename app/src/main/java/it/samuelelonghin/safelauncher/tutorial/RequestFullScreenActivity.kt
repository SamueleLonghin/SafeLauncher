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
import it.samuelelonghin.safelauncher.support.*

class RequestFullScreenActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestFullScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestFullScreenBinding.inflate(layoutInflater)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.tutorialRequestFullScreenButton.setOnClickListener {
            println("Click FullScreenAccess")
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                    "package:${this.packageName}"
                )
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            startActivity(intent)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.tutorialRequestFullScreenBackButton.setOnClickListener {
            println("BACK FullScreenAccess")
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

}