package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.list.ListActivity
import it.samuelelonghin.safelauncher.support.WIDGET_SHOW_LABELS
import it.samuelelonghin.safelauncher.support.WIDGET_SHOW_LABELS_PREF
import it.samuelelonghin.safelauncher.support.intendedSettingsPause
import it.samuelelonghin.safelauncher.support.launcherPreferences

class WidgetAdapter(
    private val context: Context,
    private val widgetsList: List<WidgetInfo>,
    private val mode: WidgetFragment.Mode,
    private var selectApp: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<WidgetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view_widget)
        val imageView: ImageView = view.findViewById(R.id.image_view_widget)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.widget_frame_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wi = widgetsList[position]

        // Check if user whats labels
        if (launcherPreferences.getBoolean(WIDGET_SHOW_LABELS, WIDGET_SHOW_LABELS_PREF)) {
            holder.textView.visibility = View.VISIBLE
            wi.setLabel(holder.textView, context)
        }
        // set widget icon
        wi.setIcon(holder.imageView, context)

        holder.itemView.setOnClickListener {
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
    }

    override fun getItemCount(): Int {
        return widgetsList.size
    }
}