package it.samuelelonghin.safelauncher.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import it.samuelelonghin.safelauncher.databinding.AuthBinding
import it.samuelelonghin.safelauncher.support.UIObject

class AuthActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: AuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.authPassword.doOnTextChanged { text, start, before, count ->
            if (text != null && text.length == 6) {

            }
        }
    }


    override fun setOnClicks() {
        binding.backLayout.root.setOnClickListener { finish() }
    }
}