package it.samuelelonghin.safelauncher.info

import android.app.Activity
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import it.samuelelonghin.safelauncher.support.*
import java.util.*


/**
 * A [RecyclerView] (efficient scrollable list) containing all apps on the users device.
 * The apps details are represented by [AppInfo].
 *
 * @param activity - the activity this is in
 */
class NotificationsAdapter(
    val activity: Activity,
) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private val notificationsListDisplayed: MutableList<InfoNotification>


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val notification = notificationsListDisplayed[i]


        viewHolder.textViewName.text = notification.name
        viewHolder.img.setImageIcon(notification.icon)


        // ensure onClicks are actually caught
        viewHolder.textViewName.setOnClickListener { viewHolder.onClick(viewHolder.textViewName) }
        viewHolder.img.setOnClickListener { viewHolder.onClick(viewHolder.img) }
    }

    override fun getItemCount(): Int {
        return notificationsListDisplayed.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.notification_fragment, parent, false)
        return ViewHolder(view)
    }

    private fun prepareApps() {
        notificationsListDisplayed.clear()
        notificationsListDisplayed.add(
            InfoNotification(
                "PRova1",
                "PROVA !",
                "CIAO BELLO",
                Icon.createWithResource(activity, R.drawable.ic_baseline_settings_24)
            )
        )
        notificationsListDisplayed.add(
            InfoNotification(
                "PRova2",
                "PROVA 222 !",
                "CIAO BELLO",
                Icon.createWithResource(activity, R.drawable.ic_baseline_battery_6_bar_24)
            )
        )
    }

    init {
        notificationsListDisplayed = ArrayList()
        prepareApps()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textViewName: TextView = itemView.findViewById(R.id.notification_name)
        var textViewContent: TextView = itemView.findViewById(R.id.notification_content)
        var img: ImageView = itemView.findViewById(R.id.notification_icon) as ImageView

        override fun onClick(v: View) {
        }

        init {
            itemView.setOnClickListener(this)
        }
    }


}
