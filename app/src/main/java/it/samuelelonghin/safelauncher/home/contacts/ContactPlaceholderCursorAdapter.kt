package it.samuelelonghin.safelauncher.home.contacts

import android.Manifest
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.contactsList

class ContactPlaceholderCursorAdapter(
    private val context: Context,
    private val count: Int,
    private val permissionLauncher: ActivityResultLauncher<String>,
) : RecyclerView.Adapter<ContactPlaceholderCursorAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_frame_adapter,
            parent, false
        )
        return ContactViewHolder(itemView)
    }


    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewContact: TextView =
            itemView.findViewById(R.id.text_view_view_contact)
        val imageViewContact: ImageView =
            itemView.findViewById(R.id.image_view_contact)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = ContactInfoPlaceholder(context)

        // Add this contact to the global contacts list
        contact.appendToList(contactsList)

        holder.textViewContact.text = contact.name
        contact.setPhoto(holder.imageViewContact)

        holder.itemView.setOnClickListener {
            println("Cliccato utente nullo")
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    override fun getItemCount(): Int {
        return count
    }
}