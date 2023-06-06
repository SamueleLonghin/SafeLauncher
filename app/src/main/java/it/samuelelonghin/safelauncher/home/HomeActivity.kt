package it.samuelelonghin.safelauncher.home


import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import it.samuelelonghin.safelauncher.databinding.HomeBinding
import it.samuelelonghin.safelauncher.support.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeActivity : BaseActivity() {
    /**
     * View
     */
    private lateinit var binding: HomeBinding
    private lateinit var view: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        println("HOME :: OnCreate")
        binding = HomeBinding.inflate(layoutInflater)
        view = binding.root

        // Initialise globals
        loadPreferences(this)

        if (!launcherPreferences.getBoolean(IS_TUTORIAL_FINISHED, false)) {
//            startActivity(Intent(TutorialActivity))
            //todo Creare un Tutorial fatto bene
            println("Inizio Tutorial")
        }

        // Preload apps to speed up the Apps Recycler
        lifecycleScope.launch(Dispatchers.IO) {
            loadApps(packageManager)
        }

        // Initialise layout
        setContentView(view)
    }


    override fun onResume() {
        super.onResume()
        println("HOME :: OnResume")
        checkDefaultLauncher()
    }


    private fun checkDefaultLauncher() {
        if (!isMyAppLauncherDefault(this)) {
            askForChangeLauncher(this)
        }
    }


}
