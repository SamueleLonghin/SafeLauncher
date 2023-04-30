package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.list.ListActivity
import it.samuelelonghin.safelauncher.support.intendedSettingsPause

internal class WidgetGridAdapter(
    private val context: Context,
    private val widgetsList: List<WidgetInfo>,
    private val mode: WidgetFragment.Mode
) :
    BaseAdapter() {
    lateinit var selectApp: ActivityResultLauncher<Intent>

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
        val wi = widgetsList[position]

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

        textView.text = wi.name

        try {
            val icon = context.packageManager.getApplicationIcon(wi.app!!)
            imageView.setImageDrawable(icon)
        } catch (_: Exception) {
            System.err.println("Errore nel icon di ${wi.name}")
        }

        convertView.setOnClickListener {
            if (mode == WidgetFragment.Mode.USE)
                when (wi.type) {
                    WidgetInfo.WidgetType.APP -> {
                        val pm: PackageManager = context.packageManager
                        val launchIntent = pm.getLaunchIntentForPackage(wi.app!!)
                        context.startActivity(launchIntent)
                    }
                    WidgetInfo.WidgetType.ACTIVITY -> {
                        context.startActivity(Intent(context, wi.activity))
                    }
                    WidgetInfo.WidgetType.ACTION -> {

                    }
                }
            else if (mode == WidgetFragment.Mode.PICK) {
                println("SELEZIONATO ${wi.name}")
                val intent = Intent(context, ListActivity::class.java)
                intent.putExtra("intention", "pick")
                intent.putExtra("forApp", "0")
                intent.putExtra("index", position)
                intendedSettingsPause = true
                selectApp.launch(intent)

            }
        }
        return convertView
    }
}