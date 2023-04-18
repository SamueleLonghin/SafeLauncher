package it.samuelelonghin.safelauncher.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.samuelelonghin.safelauncher.databinding.ViewContactActivityBinding
import it.samuelelonghin.safelauncher.home.ContactInfo
import it.samuelelonghin.safelauncher.home.HomeActivity
import it.samuelelonghin.safelauncher.support.*


class InfoActivity : AppCompatActivity(), UIObject {
    private lateinit var binding: ViewContactActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewContactActivityBinding.inflate(layoutInflater)
        val view = binding.root

        val contact: ContactInfo = getSerializable(intent, "contact", ContactInfo::class.java)
        contact.setContext(this)
        binding.viewContactFrame.textViewViewContact.text = contact.name
        if (contact.photoURI != null)
            binding.viewContactFrame.imageViewViewContact.setImageBitmap(contact.getPhotoBitmap())

        binding.viewContactFrame.buttonCall.setOnClickListener {
            run {
                if (checkUserCanCall(this)) {
                    val intent =
                        Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.mobileNumber))
                    startActivity(intent)
                }
            }
        }
        binding.viewContactFrame.backLayout.setOnClickListener {
            run {
                this.finish()
            }
        }
        binding.viewContactFrame.backButton.setOnClickListener {
            run {
                this.finish()
            }
        }

        // Initialise layout
        setContentView(view)
    }

    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
    }
}