package it.samuelelonghin.safelauncher.home

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.info.EmptyActivity
import it.samuelelonghin.safelauncher.support.BaseCursorAdapter
import it.samuelelonghin.safelauncher.support.contactsList

class ContactCursorGridAdapter(
    private val context: Context,
    cursor: Cursor?
) : BaseCursorAdapter<ContactCursorGridAdapter.CourseViewHolder>(cursor) {

    var cursor: Cursor?

    init {
        this.cursor = cursor
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_frame_adapter,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, cursor: Cursor?) {
        val contact = ContactInfo(cursor!!, context)

        // Add this contact to the global contacts list
        contact.appendToList(contactsList)

        // Populate fields with extracted properties
        // Populate fields with extracted properties
        holder.text_view_contact.text = contact.name
        if (contact.photoURI != null)
            holder.image_view_contact.setImageBitmap(contact.getPhotoBitmap())
        else
            holder.image_view_contact.setImageDrawable(null)


        //Set onclick
        holder.itemView.setOnClickListener {
            println("Cliccato ${contact.name}")
//            System.out.println(parent)
//            parent.foreground.alpha = 220
//            val intent = Intent(context, InfoActivity::class.java)
            val intent = Intent(context, EmptyActivity::class.java)
            intent.putExtra("contact", contact)

            context.startActivity(intent)
        }
        print(contact.name)
        print(" -> ")
        println(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
    }


    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text_view_contact: TextView =
            itemView.findViewById<TextView>(R.id.text_view_view_contact)
        val image_view_contact: ImageView =
            itemView.findViewById<ImageView>(R.id.image_view_contact)
    }
}