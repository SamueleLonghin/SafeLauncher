package it.samuelelonghin.safelauncher.list.other

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.list.forApp
import it.samuelelonghin.safelauncher.support.REQUEST_CHOOSE_APP

/**
 * The [OtherRecyclerAdapter] will only be displayed in the ListActivity,
 * if an app / intent / etc. is picked to be launched when an action is recognized.
 *
 * It lists `other` things to be launched that are not really represented by a URI,
 * rather by Launcher- internal conventions.
 */
class OtherRecyclerAdapter(val activity: Activity):
    RecyclerView.Adapter<OtherRecyclerAdapter.ViewHolder>() {

    private val othersList: MutableList<OtherInfo>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.list_other_row_name)
        var iconView: Button = itemView.findViewById(R.id.list_other_row_icon)


        override fun onClick(v: View) {
            val pos = adapterPosition
            val content = othersList[pos]

            returnChoiceIntent(forApp, content.data.toString())
        }

        init { itemView.setOnClickListener(this) }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val otherLabel = othersList[i].label.toString()
        val icon = othersList[i].icon.toString()

        viewHolder.textView.text = otherLabel
        viewHolder.iconView.text = icon
    }

    override fun getItemCount(): Int { return othersList.size }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.list_other_row, parent, false)
        return ViewHolder(view)
    }

    init {
        othersList = ArrayList()
        othersList.add(
            OtherInfo(activity.getString(R.string.list_other_settings),
            "launcher:settings",
                activity.getString(R.string.fas_settings)))
        othersList.add(
            OtherInfo(activity.getString(R.string.list_other_list),
                "launcher:choose",
                activity.getString(R.string.fas_bars)))
    }

    private fun returnChoiceIntent(forAction: String, value: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("value", value)
        returnIntent.putExtra("forApp", forApp)
        activity.setResult(REQUEST_CHOOSE_APP, returnIntent)
        activity.finish()
    }
}