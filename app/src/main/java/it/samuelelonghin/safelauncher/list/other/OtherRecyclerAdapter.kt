package it.samuelelonghin.safelauncher.list.other

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo.*
import it.samuelelonghin.safelauncher.support.*

/**
 * The [OtherRecyclerAdapter] will only be displayed in the ListActivity,
 * if an app / intent / etc. is picked to be launched when an action is recognized.
 *
 * It lists `other` things to be launched that are not really represented by a URI,
 * rather by Launcher- internal conventions.
 */
class OtherRecyclerAdapter(val activity: Activity, val index: Int) :
    RecyclerView.Adapter<OtherRecyclerAdapter.ViewHolder>() {

    private val othersList: MutableList<OtherInfo>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.list_other_row_name)
        var iconView: ImageView = itemView.findViewById(R.id.list_other_row_icon)


        override fun onClick(v: View) {
            val pos = adapterPosition
            val content = othersList[pos]

            val returnIntent = Intent()
            returnIntent.putExtra("value", content.data)
            returnIntent.putExtra("name", content.label)
            returnIntent.putExtra("type", WidgetType.ACTION.ordinal)
            returnIntent.putExtra("index", index)

            activity.setResult(REQUEST_CHOOSE_APP, returnIntent)
            activity.finish()
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val otherLabel = othersList[i].label.toString()
        val icon = othersList[i].icon

        viewHolder.textView.text = otherLabel
        viewHolder.iconView.setImageDrawable(setIconTintPrimary(activity, icon))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.list_other_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return othersList.size
    }


    init {
        othersList = ArrayList()
        othersList.add(
            OtherInfo(
                activity.getString(R.string.silenzioso),
                ACTION_MUTE,
                R.drawable.ic_baseline_notifications_off_24
            )
        )
        othersList.add(
            OtherInfo(
                activity.getString(R.string.flash),
                ACTION_FLASH,
                R.drawable.ic_baseline_flashlight_on_24
            )
        )
        othersList.add(
            OtherInfo(
                activity.getString(R.string.call),
                ACTION_CALL,
                R.drawable.ic_baseline_call_240
            )
        )
    }
}