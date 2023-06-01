package it.samuelelonghin.safelauncher.info

import android.app.Activity
import android.app.Notification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.*
import java.util.*


/**
 * A [RecyclerView] (efficient scrollable list) containing all apps on the users device.
 * The apps details are represented by [InfoNotification].
 *
 * @param activity - the activity this is in
 */
class NotificationsAdapter(
    val activity: Activity, private val notifications: MutableMap<String, MutableList<Notification>>
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private val notificationsListDisplayed: MutableList<InfoNotification>


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val notification = notificationsListDisplayed[i]

        //Text
        viewHolder.textViewTitle.text = notification.title

        //Content
        if (notification.content != null)
            viewHolder.textViewContent.text = notification.title
        else
            viewHolder.textViewContent.visibility = View.GONE

        //Icon
        if (notification.app != null)
            viewHolder.img.setImageDrawable(getIconFromPackage(activity, notification.app))
        else
            viewHolder.img.visibility = View.GONE


        // ensure onClicks are actually caught
        viewHolder.itemView.setOnClickListener {
            if (notification.intent != null)
                launchPendingIntent(notification.intent!!)
            else if (notification.app != null)
                launchApp(notification.app, activity)
        }
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
        notifications.forEach { (k, v) ->

            notificationsListDisplayed += InfoNotification(k, v)
        }
        if (notifications.isEmpty()) notificationsListDisplayed += InfoNotification(
            null,
            "Non hai messaggi",
        )
    }

    init {
        notificationsListDisplayed = ArrayList()
        prepareApps()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.notification_title)

        var textViewContent: TextView = itemView.findViewById(R.id.notification_content)
        var img: ImageView = itemView.findViewById(R.id.notification_icon) as ImageView
    }


}
