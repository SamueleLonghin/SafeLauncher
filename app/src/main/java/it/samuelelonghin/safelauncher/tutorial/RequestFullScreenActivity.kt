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
    private lateinit var localActivityResultEnableFullScreen: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestFullScreenBinding.inflate(layoutInflater)

        /**
         * Intercetta il risultato della chimata a sistema e la propaga indietro verso la pagina i impostazioni
         */
        localActivityResultEnableFullScreen =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                println("INTERMEDIATE ACTIVITY RESULT CODE: ${result.resultCode}")
                setResult(result.resultCode, intent);
                finish()
            }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.tutorialRequestFullScreenButton.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                    "package:${this.packageName}"
                )
            )
            localActivityResultEnableFullScreen.launch(intent)
        }
    }

}