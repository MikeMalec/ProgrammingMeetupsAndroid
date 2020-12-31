package com.example.programmingmeetups.framework.utils.permissions

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.programmingmeetups.framework.utils.LOCATION_RATIONALE
import com.example.programmingmeetups.framework.utils.LOCATION_REQUEST
import com.example.programmingmeetups.framework.utils.STORAGE_REQUEST
import dagger.hilt.android.qualifiers.ApplicationContext
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(@ApplicationContext val context: Context) {
    fun hasLocationPermissions(): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun requestLocationPermissions(fragment: Fragment) {
        if (!hasLocationPermissions()) {
            EasyPermissions.requestPermissions(
                fragment,
                LOCATION_RATIONALE,
                LOCATION_REQUEST,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    fun hasStoragePermissions(): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    fun requestStoragePermission(fragment: Fragment) {
        if (!hasStoragePermissions()) {
            EasyPermissions.requestPermissions(
                fragment,
                "",
                STORAGE_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }
}