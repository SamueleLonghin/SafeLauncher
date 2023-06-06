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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.widget_frame_adapter, parent, false)
        return WidgetViewHolder(view)
    }

    override fun onViewAttachedToWindow(holder: WidgetViewHolder) {
        super.onViewAttachedToWindow(holder)
        widgetsList[holder.actualId].action?.registerListener(context)
    }

    override fun onViewDetachedFromWindow(holder: WidgetViewHolder) {
        super.onViewDetachedFromWindow(holder)
        widgetsList[holder.actualId].action?.unRegisterListener(context)
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        val wi = widgetsList[position]
        holder.actualId = position

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
            wi.action?.createListener(context)
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

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        println("DETACHED")
    }

    override fun getItemCount(): Int {
        return widgetsList.size
    }
}