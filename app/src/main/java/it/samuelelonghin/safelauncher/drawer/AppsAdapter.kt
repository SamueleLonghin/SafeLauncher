package it.samuelelonghin.safelauncher.drawer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.settings.SettingsActivity
import it.samuelelonghin.safelauncher.support.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A [RecyclerView] (efficient scrollable list) containing all apps on the users device.
 * The apps details are represented by [AppInfo].
 *
 * @param activity - the activity this is in
 * @param intention - why the list is displayed ("view", "pick")
 * @param forApp - the action which an app is chosen for (when the intention is "pick")
 */
class AppsAdapter(
    val activity: Activity,
    val intention: String? = "view",
    val forApp: String? = ""
) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    private val appsListDisplayed: MutableList<AppInfo>


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val appLabel = appsListDisplayed[i].label.toString()
        val appPackageName = appsListDisplayed[i].packageName.toString()
        val appIcon = appsListDisplayed[i].icon
        val isSystemApp = appsListDisplayed[i].isSystemApp

        viewHolder.textView.text = appLabel
        viewHolder.img.setImageDrawable(appIcon)

        // decide when to show the options popup menu about
        if (isSystemApp || intention == "pick") {
            viewHolder.menuDots.visibility = View.VISIBLE
        } else {
            viewHolder.menuDots.visibility = View.VISIBLE


            // ensure onClicks are actually caught
            viewHolder.textView.setOnClickListener { viewHolder.onClick(viewHolder.textView) }
            viewHolder.img.setOnClickListener { viewHolder.onClick(viewHolder.img) }
        }
    }

    override fun getItemCount(): Int {
        return appsListDisplayed.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.app_adapter, parent, false)
        return ViewHolder(view)
    }

    init {
        // Load the apps
        if (appsList.size == 0)
            loadApps(activity.packageManager)
        else {
            AsyncTask.execute { loadApps(activity.packageManager) }
            notifyDataSetChanged()
        }

        appsListDisplayed = ArrayList()
        val app = AppInfo()

        app.label = activity.getString(R.string.settings_title)
        app.activity = SettingsActivity::class.java
//        app.icon = ri.loadIcon(packageManager)
//        app.icon = R..applicationInfo.loadIcon(packageManager)
//        app.packageName = ri.activityInfo.packageName
//        app.icon = ri.activityInfo.loadIcon(packageManager)
        appsListDisplayed.add(app)
        appsListDisplayed.addAll(appsList)
    }

    /**
     * The function [filter] is used to search elements within this [RecyclerView].
     */
    fun filter(text: String) {
        appsListDisplayed.clear()
        if (text.isEmpty()) {
            appsListDisplayed.addAll(appsList)
        } else {
            for (item in appsList) {
                if (item.label.toString().toLowerCase(Locale.ROOT)
                        .contains(text.toLowerCase(Locale.ROOT))
                ) {
                    appsListDisplayed.add(item)
                }
            }
        }

        // Launch apps automatically if only one result is found and the user wants it
        // Disabled at the moment. The Setting 'PREF_SEARCH_AUTO_LAUNCH' may be
        // modifyable at some later point.
        if (appsListDisplayed.size == 1 && intention == "view"
            && launcherPreferences.getBoolean(DRAWER_SEARCH_AT_LAUNCH, DRAWER_SEARCH_AT_LAUNCH_PREF)
        ) {
            launchApp(appsListDisplayed[0].packageName.toString(), activity)

            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(View(activity).windowToken, 0)
        }

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.app_adapter_name)
        var img: ImageView = itemView.findViewById(R.id.app_adapter_icon) as ImageView
        var menuDots: Button = itemView.findViewById(R.id.app_adapter_3dots_button)

        override fun onClick(v: View) {
            val pos = adapterPosition
            val context: Context = v.context
            val appPackageName = appsListDisplayed[pos].packageName.toString()

            when (intention) {
                "view" -> {
                    val appActivity = appsListDisplayed[pos].activity
                    var launchIntent: Intent
                    appActivity?.let {
                        launchIntent = Intent(context, appActivity)
                        context.startActivity(launchIntent)
                    } ?: run {
                        launchIntent = context.packageManager
                            .getLaunchIntentForPackage(appPackageName)!!
                        context.startActivity(launchIntent)
                    }
                }
                "pick" -> {
                    val returnIntent = Intent()
                    returnIntent.putExtra("value", appPackageName)
                    returnIntent.putExtra("forApp", forApp)
                    activity.setResult(REQUEST_CHOOSE_APP, returnIntent)
                    activity.finish()
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
