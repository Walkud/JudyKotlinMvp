package com.walkud.app.utils

import android.Manifest

/**
 * 权限请求Code
 * Created by Walkud on 2018/3/8 0008.
 */

object PermissionHelper {

    val WRITE_EXTERNAL_STORAGE_CODE = 0x1 shl 1//外置存储写权限
    val ACCESS_COARSE_LOCATION_CODE = 0x1 shl 2//粗略定位权限

    val WES_ACL_CODE = WRITE_EXTERNAL_STORAGE_CODE or ACCESS_COARSE_LOCATION_CODE//外置存储写权限和粗略定位权限Code


    /**
     * 获取粗略定位及外置存储写权限标识
     *
     * @return
     */
    val wesAclPermission: Array<String>
        get() = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION)

}