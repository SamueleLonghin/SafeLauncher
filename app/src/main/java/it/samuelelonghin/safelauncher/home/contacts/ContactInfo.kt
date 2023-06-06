package it.samuelelonghin.safelauncher.home.contacts

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.MediaStore
import android.widget.ImageView
import java.io.FileNotFoundException
import java.io.IOException
import java.io.Serializable


class ContactInfo(cursor: Cursor, context: Context) : ContactInfoPlaceholder(context),
    Serializable {

//    var id: String
//    var name: String
//    var mobileNumber: String? = null
//    var email: String? = null
//
//    var photoURI: String? = null

//    lateinit var notification: MutableList<Notification>

    init {
        name =
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
        photoURI =
            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))


        id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))

        if (cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                .toInt() > 0
        ) {
            val cr: ContentResolver = this.localContext.contentResolver

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
        }
    }

    override fun getPhotoBitmap(): Bitmap {
        if (photoURI != null) {
            try {
                return MediaStore.Images.Media
                    .getBitmap(
                        localContext.contentResolver,
                        Uri.parse(photoURI)
                    )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return super.getPhotoBitmap()
    }

    override fun setPhoto(imageView: ImageView) {
        if (photoURI != null) {
            try {
                imageView.setImageBitmap(
                    MediaStore.Images.Media
                        .getBitmap(
                            localContext.contentResolver,
                            Uri.parse(photoURI)
                        )
                )
                return
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        super.setPhoto(imageView)
    }
}