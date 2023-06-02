package it.samuelelonghin.safelauncher.home.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import it.samuelelonghin.safelauncher.R

lateinit var permissionLauncher: ActivityResultLauncher<String>
lateinit var localActivityResult: ActivityResultLauncher<Intent>

fun canReadContacts(context: Context): Boolean {
    return context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
}