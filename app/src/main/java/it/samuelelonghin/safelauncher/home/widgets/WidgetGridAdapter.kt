package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.WidgetFrameAdapterBinding
import it.samuelelonghin.safelauncher.list.ListActivity
import it.samuelelonghin.safelauncher.support.*

internal class WidgetGridAdapter(
    private val context: Context,
    private val widgetsList: List<WidgetInfo>,
    private val mode: WidgetFragment.Mode
) :
    BaseAdapter() {
    lateinit var selectApp: ActivityResultLauncher<Intent>
    lateinit var binding: WidgetFrameAdapterBinding

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

    private var widgetHeight: Int = 200

    init {
        // trovo l'altezza effettiva di ogni widget
        val nRows =
            launcherPreferences.getInt(WIDGET_NUMBER_ROWS, WIDGET_NUMBER_ROWS_PREF)
        widgetHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            widgetHeight / nRows.toFloat(),
            context.resources.displayMetrics
        ).toInt()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val wi = widgetsList[position]


        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (view == null) {
            // on blow line we are checking if layout inflater
            // is null, if it is null we are initializing it.
            if (layoutInflater == null) {
                layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            binding = WidgetFrameAdapterBinding.inflate(layoutInflater!!)
            view = binding.root

            // set widgetHeight
            view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, widgetHeight)
        }
        val textView: TextView = view.findViewById(R.id.text_view_widget)
        val imageView: ImageView = view.findViewById(R.id.image_view_widget)


        // Check if user whats labels
        if (launcherPreferences.getBoolean(WIDGET_SHOW_LABELS, WIDGET_SHOW_LABELS_PREF)) {
            textView.visibility = View.VISIBLE
            wi.setLabel(textView, context)
        }
        // set widget icon
        wi.setIcon(imageView, context)

        view.setOnClickListener {
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

        return view
    }
}