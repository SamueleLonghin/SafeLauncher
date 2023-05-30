package it.samuelelonghin.safelauncher.info

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors
import it.samuelelonghin.safelauncher.databinding.EmptyActivityBinding
import it.samuelelonghin.safelauncher.support.UIObject


class ViewContactActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: EmptyActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EmptyActivityBinding.inflate(layoutInflater)
        val view = binding.root

        view.minimumHeight = 400
        view.minimumWidth = 300

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(view)

        val metrics = resources.displayMetrics
        val screenWidth = (metrics.widthPixels * 0.90).toInt()
        val screenHeight = (metrics.heightPixels * 0.90).toInt()
        window.setLayout(screenWidth, screenHeight)

        DynamicColors.applyToActivityIfAvailable(this)
    }


    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
    }

}