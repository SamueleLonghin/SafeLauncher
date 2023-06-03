package it.samuelelonghin.safelauncher.list.other

import android.graphics.drawable.Drawable

/**
 * Stores information used in [OtherRecyclerAdapter] rows.
 *
 * Represents an `other` action - something that can be selected to be launched
 * when an action is recognized.
 *
 * @param data - a string identifying the thing to be launched
 */
class OtherInfo(label: String, data: String, var icon: Int) {
    var label: CharSequence? = label
    var data: CharSequence? = data
}