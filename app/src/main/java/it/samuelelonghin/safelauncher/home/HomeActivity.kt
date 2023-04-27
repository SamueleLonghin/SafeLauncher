package it.samuelelonghin.safelauncher.home


import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.HomeBinding
import it.samuelelonghin.safelauncher.support.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HomeActivity : UIObject, AppCompatActivity() {
    /**
     * View
     */
    private lateinit var binding: HomeBinding
    private lateinit var view: View

    // timers
    private var clockTimer = Timer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("HOME :: OnCreate")
        binding = HomeBinding.inflate(layoutInflater)
        view = binding.root

        // Initialise globals
        loadPreferences(this)

        if (!launcherPreferences.getBoolean(IS_TUTORIAL_FINISHED, false)) {
//            startActivity(Intent(TutorialActivity))
            println("Inizio Tutorial")
        }

        // Preload apps to speed up the Apps Recycler
        lifecycleScope.launch(Dispatchers.IO) {
            loadApps(packageManager)
        }
        // Initialise layout
        setContentView(view)
    }


    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
        println("HOME :: onStart")
    }


    override fun onResume() {
        super.onResume()
        println("HOME :: OnResume")

        if (getSavedTheme(this) == "custom")
            binding.homeBackgroundImage.setImageBitmap(background)

        // Applying the date / time format (changeable in settings)
        val dFormat = launcherPreferences.getInt(PREF_DATE_FORMAT, 0)
        val upperFMT = resources.getStringArray(R.array.settings_launcher_time_formats_upper)
        val lowerFMT = resources.getStringArray(R.array.settings_launcher_time_formats_lower)

        val dateFormat = SimpleDateFormat(upperFMT[dFormat], Locale.getDefault())
        val timeFormat = SimpleDateFormat(lowerFMT[dFormat], Locale.getDefault())

        clockTimer = fixedRateTimer("clockTimer", true, 0L, 100) {
            this@HomeActivity.runOnUiThread {
                val t = timeFormat.format(Date())
                if (binding.clockFrame.textViewClock.text != t)
                    binding.clockFrame.textViewClock.text = t

                val d = dateFormat.format(Date())
                if (binding.clockFrame.textViewDate.text != d)
                    binding.clockFrame.textViewDate.text = d
            }
        }
        checkDefaultLauncher()

    }

    override fun onPause() {
        super.onPause()
        println("HOME :: OnPause")

        clockTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("HOME :: onDestroy")
    }


    fun getDefaultLauncher(): String {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo: ResolveInfo =
            this.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)!!
        val currentLauncherPackageName: String = resolveInfo.activityInfo.packageName
        val currentLauncherClassName: String = resolveInfo.activityInfo.name
        return currentLauncherClassName
    }


    private fun isMyAppLauncherDefault(): Boolean {
        val filter = IntentFilter(Intent.ACTION_MAIN)
        filter.addCategory(Intent.CATEGORY_HOME)
        val filters: MutableList<IntentFilter> = ArrayList()
        filters.add(filter)
        val myPackageName = packageName
        val activities: List<ComponentName> = ArrayList()
        val packageManager = packageManager as PackageManager
        packageManager.getPreferredActivities(filters, activities, null)
        for (activity in activities) {
            if (myPackageName == activity.packageName) {
                return true
            }
        }
        return false
    }

    private fun checkDefaultLauncher() {


        if (!isMyAppLauncherDefault()) {

            println("CHIEDO DI METTERLO COME DEFAULT")

            val packageManager: PackageManager = this.getPackageManager()
            val componentName =
                ComponentName(this, FakeActivity::class.java)
            packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            val selector = Intent(Intent.ACTION_MAIN)
            selector.addCategory(Intent.CATEGORY_HOME)
            selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this.startActivity(selector)

            packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP
            )


//            val packageManager: PackageManager = this.packageManager
//            val componentName = ComponentName(this, HomeActivity::class.java)
//            packageManager.setComponentEnabledSetting(
//                componentName,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP
//            )
//
//            val selector: Intent = Intent(Intent.ACTION_MAIN);
//            selector.addCategory(Intent.CATEGORY_HOME);
//            selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
//            startActivity(selector)
//
//            packageManager.setComponentEnabledSetting(
//                componentName,
//                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
//                PackageManager.DONT_KILL_APP
//            )
        }
//
    }
}
