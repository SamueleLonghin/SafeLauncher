package it.samuelelonghin.safelauncher.home.widgets

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R

class WidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var actualId: Int = -1
    val textView: TextView = view.findViewById(R.id.text_view_widget)
    val imageView: ImageView = view.findViewById(R.id.image_view_widget)
}