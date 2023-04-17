package it.samuelelonghin.safelauncher.home;


import android.Manifest
import android.R.id
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import it.samuelelonghin.safelauncher.BuildConfig.VERSION_NAME
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.HomeBinding
import it.samuelelonghin.safelauncher.support.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
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

    lateinit var cursor: Cursor
    private var adapter: ContactCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.out.println("HOME :: OnCreate")
        binding = HomeBinding.inflate(layoutInflater)
        view = binding.root

        // Initialise globals
        launcherPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

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
        lifecycleScope.launch(Dispatchers.IO) {
            loadApps(packageManager)
        }
        // Initialise layout
        setContentView(view)
    }


    override fun onStart() {
        super<AppCompatActivity>.onStart()
        System.out.println("HOME :: onStart")

//        getContacts()

        // for if the settings changed
        loadSettings()
        super<UIObject>.onStart()
    }

    override fun onResume() {
        super.onResume()
        System.out.println("HOME :: OnResume")

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

    }

    override fun onPause() {
        super.onPause()
        System.out.println("HOME :: OnPause")

        clockTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        System.out.println("HOME :: onDestroy")
    }

//    private fun getContacts() {
//        // Check the SDK version and whether the permission is already granted or not.
//        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(
//                arrayOf(Manifest.permission.READ_CONTACTS),
//                PERMISSIONS_REQUEST_READ_CONTACTS
//            )
//            return
//        }
//        // create cursor and query the data
//        cursor = contentResolver.query(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//            null,
//            "starred=?",
//             arrayOf("1"),
//            null
//        )!!
//        // creation of adapter using ContactCursorAdapter class
//        adapter = ContactCursorAdapter(this, cursor, view)
//        // Calling setAdaptor() method to set created adapter
//        binding.contactsFrame.listViewContacts.adapter = adapter
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission is granted
//                getContacts()
//            } else {
//                Toast.makeText(
//                    this,
//                    "Until you grant the permission, we cannot display the names",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
}
