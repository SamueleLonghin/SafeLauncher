package it.samuelelonghin.safelauncher.home.widgets.actions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.TorchCallback
import android.os.Build
import android.widget.Toast
import it.samuelelonghin.safelauncher.R


class ActionFlash : Action("Flash", 0) {
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    override val states: List<String>
        get() = listOf("Flash on", "Flash Of")
    override val icons: List<Int>
        get() = listOf(
            R.drawable.ic_baseline_flashlight_on_24, R.drawable.ic_baseline_flashlight_on_mia_24
        )
    override val texts: List<Int>
        get() = listOf(
            R.string.unmute,
            R.string.mute,
        )


    lateinit var torchCallback: TorchCallback
    lateinit var manager: CameraManager
    override fun toggle(context: Context) {
        println("Togglato $name")
        state = 1 - state
        if (!setFlashMode()) {
            Toast.makeText(context, "Non sono riuscito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getStatus(context: Context) {
        val isFlashAvailable =
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        if (!isFlashAvailable) {
            showNoFlashError(context)
        }
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun createListener(context: Context) {
        torchCallback = object : TorchCallback() {
            override fun onTorchModeChanged(cameraId: String, enabled: Boolean) {
                super.onTorchModeChanged(cameraId, enabled)
                state = if (enabled) 1 else 0
                reloadLayout(context)
            }
        }
        manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    override fun registerListener(context: Context) {
        manager.registerTorchCallback(torchCallback, null) // (callback, handler)
    }


    override fun unRegisterListener(context: Context) {
        manager.unregisterTorchCallback(torchCallback)
    }

    private fun showNoFlashError(context: Context) {
        val alert = AlertDialog.Builder(context).create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ -> }
        alert.show()
    }

    private fun setFlashMode(): Boolean {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ::cameraId.isInitialized) {
                cameraManager.setTorchMode(cameraId, state == 1)
                return true
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return false
    }
}