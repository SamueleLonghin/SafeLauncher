package it.samuelelonghin.safelauncher.home.contacts

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.MediaStore
import java.io.FileNotFoundException
import java.io.IOException
import java.io.Serializable


class ContactInfo(cursor: Cursor, context: Context) : Serializable {

    @Transient
    private var context: Context

    var id: String
    var name: String
    var mobileNumber: String? = null
    var email: String? = null

    var photoURI: String? = null

//    lateinit var notification: MutableList<Notification>

    init {
        this.context = context
        name =
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
        photoURI =
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))


        id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))

        if (cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                .toInt() > 0
        ) {
            val cr: ContentResolver = this.context.contentResolver

            // Query phone here. Covered next

            val cp = cr.query(
                Phone.CONTENT_URI, null,
                Phone.CONTACT_ID + " = ?", arrayOf(id), null
            )
            var phone: String? = null
            if (cp != null && cp.moveToFirst()) {
                phone = cp.getString(cp.getColumnIndexOrThrow(Phone.NUMBER))
                phone = phone.replace("\\s".toRegex(), "")
                cp.close()
            }
            val ce = cr.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id), null
            )
            if (ce != null && ce.moveToFirst()) {
                email =
                    ce.getString(ce.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA))
                ce.close()
            }
            mobileNumber = phone
//            System.out.print(name + " " + id + " " + email + " ")
//            System.out.println("Numero: " + phone)


//            val phones: Cursor? = cr.query(
//                Phone.CONTENT_URI, null,
//                Phone.CONTACT_ID + " = " + id, null, null
//            )
//            if (phones != null) {
//                while (phones.moveToNext()) {
//                    var number = phones.getString(phones.getColumnIndexOrThrow(Phone.NUMBER))
//                    val type = phones.getInt(phones.getColumnIndexOrThrow(Phone.TYPE))
//                    when (type) {
//                        Phone.TYPE_HOME,
//                        Phone.TYPE_WORK -> {
//                            if (mobileNumber == null) mobileNumber = number
//                        }
//                        Phone.TYPE_MOBILE -> {
//                            mobileNumber = number
//                        }
//                    }
//                    System.out.print(name + " ")
//                    System.out.println("Numero: " + number + " type: " + type)
//                }
//            }
//            phones?.close()
        }
    }

    fun getPhotoBitmap(): Bitmap? {
        var photo: Bitmap? = null
//        var photo: Bitmap = BitmapFactory.decodeResource(
//            context.resources,
//            R.drawable.ic_launcher_background
//        )
        if (photoURI != null) {
            try {
                photo = MediaStore.Images.Media
                    .getBitmap(
                        context.contentResolver,
                        Uri.parse(photoURI)
                    )
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        return photo
    }

    fun appendToList(list: MutableMap<String, ContactInfo>) {
        //todo gestire il sovrascrimento delle notifiche
        list.put(id, this)
    }

    fun setContext(context: Context) {
        this.context = context
    }
}