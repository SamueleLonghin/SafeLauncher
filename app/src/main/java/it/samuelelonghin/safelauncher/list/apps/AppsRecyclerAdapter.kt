package it.samuelelonghin.safelauncher.list.apps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo
import it.samuelelonghin.safelauncher.settings.SettingsActivity
import it.samuelelonghin.safelauncher.support.*
import java.util.*


/**
 * A [RecyclerView] (efficient scrollable list) containing all apps on the users device.
 * The apps details are represented by [AppInfo].
 *
 * @param activity - the activity this is in
 * @param intention - why the list is displayed ("view", "pick")
 * @param index -
 */
class AppsRecyclerAdapter(
    val activity: Activity,
    val intention: String? = "view",
    val index: Int = DEFAULT_INDEX,
    var view: Int = R.layout.list_apps_row
) :
    RecyclerView.Adapter<AppsRecyclerAdapter.ViewHolder>() {

    private val appsListDisplayed: MutableList<AppInfo>


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val appLabel = appsListDisplayed[i].label.toString()
        val appIcon = appsListDisplayed[i].icon
        val isSystemApp = appsListDisplayed[i].isSystemApp

        viewHolder.textView.text = appLabel
        viewHolder.img.setImageDrawable(appIcon)


        // decide when to show the options popup menu about
        if (!isSystemApp && intention != "pick") {
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
        val view: View = inflater.inflate(view, parent, false)
        return ViewHolder(view)
    }

    private fun prepareApps() {
        appsListDisplayed.clear()
        if (intention == "view") {
            val app = AppInfo()
            app.label = activity.getString(R.string.settings_title)
            app.activity = SettingsActivity::class.java
            app.icon = ResourcesCompat.getDrawable(
                activity.resources,
                R.drawable.ic_baseline_settings_24,
                activity.theme
            )
            appsListDisplayed.add(app)
        }

        appsListDisplayed.addAll(appsList)
    }

    init {
        // Load the apps
        if (appsList.size == 0)
            loadApps(activity.packageManager)
        appsListDisplayed = ArrayList()
        prepareApps()
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
                if (item.label.toString().lowercase(Locale.ROOT)
                        .contains(text.lowercase(Locale.ROOT))
                ) {
                    appsListDisplayed.add(item)
                }
            }
        }

        // Launch apps automatically if only one result is found and the user wants it
        // Disabled at the moment. The Setting 'PREF_SEARCH_AUTO_LAUNCH' may be
        // modifyable at some later point.
        if (appsListDisplayed.size == 1 && intention == "view"
            && launcherPreferences.getBoolean(
                OPEN_APP_ON_ONLY_ONE_RESULTS,
                DRAWER_SEARCH_AT_LAUNCH_PREF
            )
        ) {
            launch(appsListDisplayed[0].packageName.toString(), activity)

            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(View(activity).windowToken, 0)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.list_apps_row_name)
        var img: ImageView = itemView.findViewById(R.id.list_apps_row_icon) as ImageView

        override fun onClick(v: View) {
            val pos = adapterPosition
            val context: Context = v.context
            val appPackageName = appsListDisplayed[pos].packageName.toString()
            val appName = appsListDisplayed[pos].label.toString()

            when (intention) {
                "view" -> {
                    val appActivity = appsListDisplayed[pos].activity
                    var launchIntent: Intent
                    appActivity?.let {
                        launchIntent = Intent(context, appActivity)
                        context.startActivity(launchIntent)
                    } ?: run {
                        launchApp(appPackageName, context)
                    }
                }
                "pick" -> {
                    val returnIntent = Intent()
                    returnIntent.putExtra("value", appPackageName)
                    returnIntent.putExtra("name", appName)
                    returnIntent.putExtra("type", WidgetInfo.WidgetType.APP.ordinal)
                    returnIntent.putExtra("index", index)

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
