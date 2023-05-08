package it.samuelelonghin.safelauncher.home


import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.color.DynamicColors
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.HomeBinding
import it.samuelelonghin.safelauncher.support.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HomeActivity : UIObject, AppCompatActivity() {
    /**
     * View
     */
    private lateinit var binding: HomeBinding
    private lateinit var view: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("HOME :: OnCreate")
//        DynamicColors.applyToActivitiesIfAvailable(application)
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




        checkDefaultLauncher()

    }


    override fun onPause() {
        super.onPause()
        println("HOME :: OnPause")

    }

    override fun onDestroy() {
        super.onDestroy()
        println("HOME :: onDestroy")
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

            val packageManager: PackageManager = this.packageManager
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
        }
    }
}
