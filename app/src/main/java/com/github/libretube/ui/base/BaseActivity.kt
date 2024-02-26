package com.github.libretube.ui.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.libretube.R
import com.github.libretube.constants.PreferenceKeys
import com.github.libretube.helpers.LocaleHelper
import com.github.libretube.helpers.PreferenceHelper
import com.github.libretube.helpers.ThemeHelper
import com.github.libretube.helpers.WindowHelper

/**
 * Activity that applies the LibreTube theme and the in-app language
 */
open class BaseActivity : AppCompatActivity() {
    val screenOrientationPref by lazy {
        val orientationPref = PreferenceHelper.getString(
            PreferenceKeys.ORIENTATION,
            resources.getString(R.string.config_default_orientation_pref)
        )
        when (orientationPref) {
            "portrait" -> ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
            "landscape" -> ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
            "auto" -> ActivityInfo.SCREEN_ORIENTATION_USER
            else -> throw IllegalArgumentException()
        }
    }

    /**
     * Whether the phone of the user has a cutout like a notch or not
     */
    val hasCutout by lazy {
        WindowHelper.hasCutout(window.decorView.rootView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // set the app theme (e.g. Material You)
        ThemeHelper.updateTheme(this)

        // Set the navigation and statusBar color if SDK < 23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            ThemeHelper.setSystemBarColors(this, window, false)

        // set the apps language
        LocaleHelper.updateLanguage(this)

        requestOrientationChange()

        super.onCreate(savedInstanceState)
    }

    /**
     * Rotate the screen according to the app orientation preference
     */
    fun requestOrientationChange() {
        requestedOrientation = screenOrientationPref
    }
}
