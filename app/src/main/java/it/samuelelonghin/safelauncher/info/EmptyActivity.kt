package it.samuelelonghin.safelauncher.info

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import it.samuelelonghin.safelauncher.databinding.ViewContactActivityBinding
import it.samuelelonghin.safelauncher.support.UIObject

class EmptyActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: ViewContactActivityBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = ViewContactActivityBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

    }
    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
    }

}