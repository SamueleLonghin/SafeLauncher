package it.samuelelonghin.safelauncher.home.contacts

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.view_contact.ViewContactActivity
import it.samuelelonghin.safelauncher.support.BaseCursorAdapter
import it.samuelelonghin.safelauncher.support.getUserNotificationsCount

class ContactCursorAdapter(
    private val context: Context,
    cursor: Cursor
) : BaseCursorAdapter<ContactViewHolder>(cursor) {

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

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        cursor: Cursor
    ) {
        val contact = ContactInfo(cursor, context)


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
}