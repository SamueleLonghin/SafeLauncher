package it.samuelelonghin.safelauncher.home

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.info.EmptyActivity
import it.samuelelonghin.safelauncher.info.InfoActivity
import it.samuelelonghin.safelauncher.support.contactsList
import java.io.FileNotFoundException
import java.io.IOException

class ContactCursorAdapter(context: Context?, cursor: Cursor, private val parent: View) :
    CursorAdapter(context, cursor, 0) {
    init {
        println("NUmero di cosi:" + cursor.count)
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.contact_frame_adapter, parent, false)
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    override fun bindView(view: View, context: Context, cursor: Cursor) {
        // Find fields to populate in inflated template
        // Find fields to populate in inflated template
        val text_view_contact = view.findViewById<TextView>(R.id.text_view_view_contact)
        val image_view_contact = view.findViewById<ImageView>(R.id.image_view_contact)
        // Extract properties from cursor
        // Extract properties from cursor

        val contact = ContactInfo(cursor, context)

        // Add this contact to the global contacts list
        contact.appendToList(contactsList)

        // Populate fields with extracted properties
        // Populate fields with extracted properties
        text_view_contact.text = contact.name
        if (contact.photoURI != null)
            image_view_contact.setImageBitmap(contact.getPhotoBitmap())
        else
            image_view_contact.setImageDrawable(null)


        //Set onclick
        view.setOnClickListener { v: View? ->
            println("Cliccato${contact.name}")
            println("Da rimpiazzare")
            System.out.println(parent)
//            parent.foreground.alpha = 220
//            val intent = Intent(context, InfoActivity::class.java)
            val intent = Intent(context, EmptyActivity::class.java)
            intent.putExtra("contact", contact)
            context.startActivity(intent)
        }
        println(contact.name)


        //photo_uri
        //starred
        //[phonetic_name, status_res_package, custom_ringtone, contact_status_ts, account_type, data_version, photo_file_id, contact_status_res_package, group_sourceid, display_name_alt, sort_key_alt, mode, last_time_used, starred, contact_status_label, has_phone_number, chat_capability, raw_contact_id, carrier_presence, contact_last_updated_timestamp, res_package, photo_uri, data_sync4, phonebook_bucket, times_used, display_name, sort_key, data_sync1, version, data_sync2, data_sync3, photo_thumb_uri, status_label, contact_presence, in_default_directory, times_contacted, _id, account_type_and_data_set, name_raw_contact_id, status, phonebook_bucket_alt, last_time_contacted, pinned, is_primary, photo_id, contact_id, contact_chat_capability, contact_status_icon, in_visible_group, phonebook_label, account_name, display_name_source, data9, dirty, sourceid, phonetic_name_style, send_to_voicemail, data8, lookup, data7, data6, phonebook_label_alt, data5, is_super_primary, data4, data3, data2, data1, data_set, contact_status, backup_id, preferred_phone_account_component_name, raw_contact_is_user_profile, status_ts, data10, preferred_phone_account_id, data12, mimetype, status_icon, data11, data14, data13, hash_id, data15]
//        tvPriority.text = priority.toString()
    }
}