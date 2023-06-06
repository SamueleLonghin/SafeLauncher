package it.samuelelonghin.safelauncher.home.contacts

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.setIconTintSecondary
import java.io.Serializable
import kotlin.random.Random


open class ContactInfoPlaceholder(context: Context, name: String, id: String) : Serializable {

    @Transient
    protected var localContext: Context

    var id: String
    var name: String
    var mobileNumber: String? = null
    var email: String? = null

    var photoURI: String? = null

    init {
        this.localContext = context
        this.name = name
        this.id = id
    }

    constructor(context: Context) : this(context, "", "0")

    open fun getPhotoBitmap(): Bitmap {
        val users = listOf(
            R.drawable.ic_baseline_face_24,
            R.drawable.ic_baseline_face_3_24,
            R.drawable.ic_baseline_face_6_24,
            R.drawable.ic_baseline_face_2_24
        )
        return BitmapFactory.decodeResource(localContext.resources, users[Random.nextInt(users.size)])
    }

    open fun getRandomDrawable(): Int {
        val users = listOf(
            R.drawable.ic_baseline_face_24,
            R.drawable.ic_baseline_face_3_24,
            R.drawable.ic_baseline_face_6_24,
            R.drawable.ic_baseline_face_2_24
        )
        return users[Random.nextInt(users.size)]
    }

    open fun getPhotoDrawable(): Drawable {
        return setIconTintSecondary(localContext, getRandomDrawable())
    }

    open fun setPhoto(imageView: ImageView) {
        imageView.setImageDrawable(getPhotoDrawable())
    }

    fun appendToList(list: MutableMap<String, ContactInfoPlaceholder>) {
        list[id] = this
    }

    fun setContext(context: Context) {
        localContext = context
    }
}