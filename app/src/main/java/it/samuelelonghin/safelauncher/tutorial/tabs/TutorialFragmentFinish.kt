package it.samuelelonghin.safelauncher.tutorial.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finnmglas.launcher.*
import com.finnmglas.launcher.BuildConfig.VERSION_NAME
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.support.UIObject
import it.samuelelonghin.safelauncher.support.launcherPreferences
import kotlinx.android.synthetic.main.tutorial_finish.*

/**
 * The [TutorialFragmentFinish] is a used as a tab in the TutorialActivity.
 *
 * It is used to display further resources and let the user start Launcher
 */
class TutorialFragmentFinish(): Fragment(), UIObject {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tutorial_finish, container, false)
    }

    override fun onStart() {
        super<Fragment>.onStart()
        super<UIObject>.onStart()
    }

    override fun applyTheme() {
        tutorial_finish_container.setBackgroundColor(dominantColor)
        setButtonColor(tutorial_finish_button_start, vibrantColor)
        tutorial_finish_button_start.blink()
    }

    override fun setOnClicks() {
        super.setOnClicks()
        tutorial_finish_button_start.setOnClickListener{ finishTutorial() }
    }

    private fun finishTutorial() {
        if (!launcherPreferences.getBoolean(PREF_STARTED, false)){
            launcherPreferences.edit()
                .putBoolean(PREF_STARTED, true) // never auto run this again
                .putLong(PREF_STARTED_TIME, System.currentTimeMillis() / 1000L) // record first startup timestamp
                .putString(PREF_VERSION, VERSION_NAME) // save current launcher version
                .apply()
        }
        activity!!.finish()
    }
}