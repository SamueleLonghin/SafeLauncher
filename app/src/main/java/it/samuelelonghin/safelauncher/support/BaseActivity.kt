package it.samuelelonghin.safelauncher.support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors

open class BaseActivity : AppCompatActivity(), UIObject {
    open val forceFullScreen = true
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        // Disable Pull-down menù
//        disablePullDownMenu();
        // Set display as fullscreen
        if (forceFullScreen)
            setFullScreen()
        DynamicColors.applyToActivityIfAvailable(this)
    }

    override fun onStart() {
        super<AppCompatActivity>.onStart()
        super<UIObject>.onStart()
    }


    protected fun disablePullDownMenu() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Please give my app this permission!", Toast.LENGTH_SHORT).show()
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                    "package:$packageName"
                )
            )
            startActivity(intent)
        } else {
            preventStatusBarExpansion(this)
        }

    }

    private fun preventStatusBarExpansion(context: Context) {
        val manager = context.applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        val activity = context as Activity
        val localLayoutParams = WindowManager.LayoutParams()
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        localLayoutParams.gravity = Gravity.TOP
        localLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or  // this is to enable the notification to recieve touch events
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or  // Draws over status bar
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        val resId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        var result = 0
        if (resId > 0) {
            result = activity.resources.getDimensionPixelSize(resId)
        }
        localLayoutParams.height = result
        localLayoutParams.format = PixelFormat.TRANSPARENT
        val view = customViewGroup(context)
        manager.addView(view, localLayoutParams)
    }


    class customViewGroup(context: Context?) : ViewGroup(context) {
        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            Log.v("customViewGroup", "**********Intercepted")
            return true
        }
    }

    open fun setFullScreen() {
        //Not necessary because API level must be >23. In any case for future target extend...
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }
}