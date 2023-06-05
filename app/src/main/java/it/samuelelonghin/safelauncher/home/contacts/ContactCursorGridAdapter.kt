package it.samuelelonghin.safelauncher.home.contacts

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.view_contact.ViewContactActivity
import it.samuelelonghin.safelauncher.support.BaseCursorAdapter
import it.samuelelonghin.safelauncher.support.contactsList
import it.samuelelonghin.safelauncher.support.getUserNotifications
import it.samuelelonghin.safelauncher.support.getUserNotificationsCount

class ContactCursorGridAdapter(
    private val context: Context,
    cursor: Cursor
) : BaseCursorAdapter<ContactCursorGridAdapter.ContactViewHolder>(cursor) {

    private var cursor: Cursor

    init {
        this.cursor = cursor
    }

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

    override fun onBindViewHolder(holder: ContactViewHolder, cursor: Cursor) {
        val contact = ContactInfo(cursor, context)

        // Add this contact to the global contacts list
        contact.appendToList(contactsList)

        // Populate fields with extracted properties
        // Populate fields with extracted properties
        holder.textViewContact.text = contact.name
        contact.setPhoto(holder.imageViewContact)

        //Imposto badge di notifica
        val notificationCount = getUserNotificationsCount(contact.name)
        if (notificationCount > 0) {
            holder.textViewNotificationBadge.visibility = View.VISIBLE
            holder.textViewNotificationBadge.text = notificationCount.toString()
        }

        //Set onclick
        holder.itemView.setOnClickListener {
            println("Cliccato ${contact.name}")
            val intent = Intent(context, ViewContactActivity::class.java)
            intent.putExtra("contact", contact)
            context.startActivity(intent)
        }
    }


    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewContact: TextView =
            itemView.findViewById(R.id.text_view_view_contact)
        val imageViewContact: ImageView =
            itemView.findViewById(R.id.image_view_contact)
        val textViewNotificationBadge: TextView =
            itemView.findViewById(R.id.text_view_notification_badge)
    }
}