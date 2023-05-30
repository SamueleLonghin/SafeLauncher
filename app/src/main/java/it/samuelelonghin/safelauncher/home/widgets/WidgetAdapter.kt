package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
) : RecyclerView.Adapter<WidgetViewHolder>() {
    init {
//        val sharedPreferenceChangeListener =
//            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
//                if (key in setOf(
//                        WIDGET_NUMBER_ROWS, WIDGET_NUMBER_COLUMNS, WIDGET_FORCE_APPS,
//                        WIDGET_FORCE_SETTINGS, WIDGET_SHOW_LABELS,
//                        WIDGET_IS_SCROLLABLE
//                    )
//                ) {
//                    print("WIDGETS PREF CHANGED: ")
//                    println(key)
//                    notifyDataSetChanged()
//                }
//            }
//        launcherPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.widget_frame_adapter, parent, false)
        return WidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        val wi = widgetsList[position]

        // Check if user whats labels
        if (launcherPreferences.getBoolean(WIDGET_SHOW_LABELS, WIDGET_SHOW_LABELS_PREF)) {
            holder.textView.visibility = View.VISIBLE
            wi.setLabel(holder.textView, context)
        }
        // set widget icon
        wi.setIcon(holder.imageView, context)

        if (wi.type == WidgetInfo.WidgetType.ACTION) {
            wi.action?.addHolder(holder)
            wi.action?.getStatus(context)
        }

        val ocl = View.OnClickListener {
            when (mode) {
                WidgetFragment.Mode.USE -> when (wi.type) {
                    WidgetInfo.WidgetType.APP ->
                        launchApp(wi.app!!, context)

                    WidgetInfo.WidgetType.ACTIVITY ->
                        launchActivity(wi.name, context, selectApp)

                    WidgetInfo.WidgetType.ACTION -> {
                        wi.action!!.toggle(context)
//                        toggleAction(wi.action,context)
                    }
                }
                WidgetFragment.Mode.PICK -> {
                    println("SELEZIONATO ${wi.name}")
                    val intent = Intent(context, ListActivity::class.java)
                    intent.putExtra("intention", "pick")
                    intent.putExtra("index", position)
                    intendedSettingsPause = true
                    selectApp.launch(intent)
                }
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