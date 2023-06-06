package it.samuelelonghin.safelauncher.home.contacts

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.tutorial.RequestContactsActivity

class ContactPlaceholderAdapter(
    private val context: Context,
    private val count: Int,
    private val requestPermissionViewContactsLauncher: ActivityResultLauncher<Intent>,
) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_frame_adapter, parent, false
        )
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = ContactInfoPlaceholder(context)

        holder.textViewContact.text = contact.name
        contact.setPhoto(holder.imageViewContact)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, RequestContactsActivity::class.java)
            requestPermissionViewContactsLauncher.launch(intent)
        }
    }

    override fun getItemCount(): Int {
        return count
    }

}