package it.samuelelonghin.safelauncher.support

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import it.samuelelonghin.safelauncher.R


fun getIconFromPackage(context: Context, pk: String): Drawable? {
    try {
        return context.packageManager.getApplicationIcon(pk)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.ic_launcher_background,
        context.theme
    )
}