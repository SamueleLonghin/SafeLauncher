package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import java.io.Console

internal class WidgetGridAdapter(
    private val context: Context,
    private val widgetsList: List<WidgetInfo>,
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return widgetsList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.widget_frame_adapter, null)
        }
        val textView: TextView = convertView!!.findViewById(R.id.text_view_widget)
        val imageView: ImageView = convertView.findViewById(R.id.image_view_widget)
        textView.text = widgetsList[position].name
        imageView.setImageResource(widgetsList[position].icon)

        convertView.setOnClickListener {
            println("Cliccato widget " + widgetsList[position].name)
            widgetsList[position].onClick(context)
        }
        return convertView
    }
}