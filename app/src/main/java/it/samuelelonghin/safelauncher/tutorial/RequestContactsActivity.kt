package it.samuelelonghin.safelauncher.tutorial

import android.content.Intent
import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import it.samuelelonghin.safelauncher.databinding.ActivityRequestContactsBinding
import it.samuelelonghin.safelauncher.support.*

class RequestContactsActivity : BaseActivity() {
    private lateinit var binding: ActivityRequestContactsBinding
    private lateinit var localActivityResultEnableFullScreen: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestContactsBinding.inflate(layoutInflater)

        /**
         * Intercetta il risultato della chimata a sistema e la propaga indietro verso la activity chiamante
         */
        localActivityResultEnableFullScreen = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                setResult(RESULT_OK)
            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//            if (isGranted) {
//                getContacts()
//                Toast.makeText(
//                    context,
//                    getString(R.string.permesso_contatti_granted),
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                Toast.makeText(
//                    context,
//                    getString(R.string.permesso_contatti_not_granted),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
//        binding.tutorialRequestFullScreenButton.setOnClickListener {
//            localActivityResultEnableFullScreen.launch(Manifest.permission.READ_CONTACTS)
//        }


        binding.tutorialRequestFullScreenButton.setOnClickListener {
            println("Click ContAccess")
            localActivityResultEnableFullScreen.launch(Manifest.permission.READ_CONTACTS)
//            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
//            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
//            startActivity(intent)
//            setResult(RESULT_OK, intent)
//            finish()
        }
        binding.tutorialRequestNotificationAccessBackButton.setOnClickListener {
            println("BACK ContAccess")
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

}