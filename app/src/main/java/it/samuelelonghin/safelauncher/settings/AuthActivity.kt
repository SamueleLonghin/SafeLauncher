package it.samuelelonghin.safelauncher.settings

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.color.DynamicColors
import it.samuelelonghin.safelauncher.databinding.AuthBinding
import it.samuelelonghin.safelauncher.support.*

class AuthActivity : AppCompatActivity(), UIObject {
    enum class Intention {
        LOGIN,
        CREATE
    }

    private lateinit var binding: AuthBinding
    private var intention: Intention = Intention.LOGIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthBinding.inflate(layoutInflater)

        val bundle = intent.extras
        if (bundle != null) {
            intention = Intention.valueOf(bundle.getString("intention")!!)
        } else
            println("BUNDLEEEEEEEEEE")


        if (intention == Intention.LOGIN &&
            !launcherPreferences.getBoolean(SETTINGS_REQUIRES_AUTH, SETTINGS_REQUIRES_AUTH_DEF)
        ) {
            launchActivity(ACTIVITY_SETTINGS, this)
        }

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        DynamicColors.applyToActivityIfAvailable(this)
    }

    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
        binding.authPassword.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.authPassword, 0)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }


    override fun setOnClicks() {
        binding.backLayout.root.setOnClickListener { finish() }
        binding.authPassword.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.length == SETTINGS_AUTH_LENGTH) {
                val savedAuth = launcherPreferences.getString(SETTINGS_AUTH, SETTINGS_AUTH_DEF)
                val auth = text.toString()
                if (intention == Intention.LOGIN) {
                    if (auth == savedAuth) {
                        launchActivity(ACTIVITY_SETTINGS, this)
                    } else {
                        binding.authPassword.setText("")
                    }
                } else {
                    val returnIntent = Intent()
                    returnIntent.putExtra("value", auth)
                    setResult(REQUEST_AUTH_FOR_SETTINGS, returnIntent)
                    finish()
                }
            }
        }
    }

    override fun applyTheme() {
        if (intention == Intention.CREATE) binding.textViewPasswordTitle.text =
            "Scegli una password di lunghezza $SETTINGS_AUTH_LENGTH"
    }
}