package it.samuelelonghin.safelauncher.home.contacts

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewContact: TextView =
        itemView.findViewById(R.id.text_view_view_contact)
    val imageViewContact: ImageView =
        itemView.findViewById(R.id.image_view_contact)
    val textViewNotificationBadge: TextView =
        itemView.findViewById(R.id.text_view_notification_badge)
}