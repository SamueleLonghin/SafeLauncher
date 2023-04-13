package it.samuelelonghin.safelauncher.home;


import android.content.Context;
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import android.os.Bundle;
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import it.samuelelonghin.safelauncher.BuildConfig.VERSION_NAME

import it.samuelelonghin.safelauncher.R;
import it.samuelelonghin.safelauncher.support.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlinx.android.synthetic.main.home.*


class HomeActivity : UIObject, BaseFullActivity() {

    // timers
    private var clockTimer = Timer()
    private var tooltipTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise globals
        launcherPreferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        windowManager.defaultDisplay.getMetrics(displayMetrics)

        loadSettings()

        /**
         * Disabilito temporaneamente il Tutorial
         */
        if (false) {
            // First time opening the app: show Tutorial, else: check versions
            if (!launcherPreferences.getBoolean(PREF_STARTED, false)) {
                //todo uncomment for tutorial
//                startActivity(Intent(this, TutorialActivity::class.java))
            } else when (launcherPreferences.getString(PREF_VERSION, "")) {
                // Check versions, make sure transitions between versions go well

                VERSION_NAME -> { /* the version installed and used previously are the same */
                }
                "" -> { /* The version used before was pre- v1.3.0,
                        as version tracking started then */

                    /*
                 * before, the dominant and vibrant color of the `finn` and `dark` theme
                 * were not stored anywhere. Now they have to be stored:
                 * -> we just reset them using newly implemented functions
                 */
                    when (getSavedTheme(this)) {
                        "light" -> resetToDefaultTheme(this)
                        "dark" -> resetToDarkTheme(this)
                    }

                    launcherPreferences.edit()
                            .putString(PREF_VERSION, VERSION_NAME) // save new version
                            .apply()

                    // show the new tutorial
                    //todo uncomment for tutorial
//                    startActivity(Intent(this, TutorialActivity::class.java))
                }
            }
        }

        // Preload apps to speed up the Apps Recycler
        AsyncTask.execute { loadApps(packageManager) }

        // Initialise layout
        setContentView(R.layout.home)
    }

    override fun onStart() {
        super<BaseFullActivity>.onStart()

//        mDetector = GestureDetectorCompat(this, this)
//        mDetector.setOnDoubleTapListener(this)

        // for if the settings changed
        loadSettings()
        super<UIObject>.onStart()
    }

    override fun onResume() {
        super.onResume()
//        val home_background_image = findViewById<ImageView>(R.id.home_background_image)
        if (home_background_image != null && getSavedTheme(this) == "custom")
            home_background_image.setImageBitmap(background)

        // Applying the date / time format (changeable in settings)
        val dFormat = launcherPreferences.getInt(PREF_DATE_FORMAT, 0)
        val upperFMT = resources.getStringArray(R.array.settings_launcher_time_formats_upper)
        val lowerFMT = resources.getStringArray(R.array.settings_launcher_time_formats_lower)

        val dateFormat = SimpleDateFormat(upperFMT[dFormat], Locale.getDefault())
        val timeFormat = SimpleDateFormat(lowerFMT[dFormat], Locale.getDefault())

        clockTimer = fixedRateTimer("clockTimer", true, 0L, 100) {
            this@HomeActivity.runOnUiThread {
                val t = timeFormat.format(Date())
                if (home_lower_view.text != t)
                    home_lower_view.text = t

                val d = dateFormat.format(Date())
                if (home_upper_view.text != d)
                    home_upper_view.text = d
            }
        }
    }

    override fun onPause() {
        super.onPause()
        clockTimer.cancel()
    }
}
