package com.duman.weatherlogger

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import kotlin.math.roundToInt

fun FragmentActivity.checkLocationPermission(): Boolean {
    return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

fun getNavOptions(backId: Int): NavOptions {
    return NavOptions.Builder()
        .setPopUpTo(backId, false)
        .build()
}

fun Activity.navigateDestination(@IdRes id: Int, @IdRes backId: Int = R.id.listFragment) {
    Navigation.findNavController(this, R.id.host).navigate(id, null, getNavOptions(backId))
}

fun Float.toCelcisus():String{
    return ""+ minus(272.15f).roundToInt() + "\u2103"
}