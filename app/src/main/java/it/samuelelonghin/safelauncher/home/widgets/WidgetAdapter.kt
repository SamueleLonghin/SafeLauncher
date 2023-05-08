package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.list.ListActivity
import it.samuelelonghin.safelauncher.support.*

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

        val ocl = View.OnClickListener {
            if (mode == WidgetFragment.Mode.USE) when (wi.type) {
                WidgetInfo.WidgetType.APP ->
                    launchApp(wi.app!!, context)

                WidgetInfo.WidgetType.ACTIVITY ->
                    launchActivity(wi.name, context, selectApp)

                WidgetInfo.WidgetType.ACTION -> {
                    println("ACTION CLICCATA")
                }
            }
            else if (mode == WidgetFragment.Mode.PICK) {
                println("SELEZIONATO ${wi.name}")
                val intent = Intent(context, ListActivity::class.java)
                intent.putExtra("intention", "pick")
                intent.putExtra("index", position)
                intendedSettingsPause = true
                selectApp.launch(intent)
            } else {
                println("ALTRO")
            }
        }


        holder.itemView.setOnClickListener(ocl)
        holder.imageView.setOnClickListener(ocl)

        if (mode == WidgetFragment.Mode.PICK) {
            holder.imageView.setOnLongClickListener {
                println("LongClick")
                removeWidget(position)
                notifyItemChanged(position)
                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount(): Int {
        return widgetsList.size
    }
}