package com.walkud.app.mvp.ui.activity

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.walkud.app.R
import com.walkud.app.mvp.base.MvcActivity
import com.walkud.app.utils.AppUtils
import com.walkud.app.utils.ContextUtil
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by Zhuliya on 2018/11/8
 */
class SplashActivity : MvcActivity() {

    companion object {
        const val PERMISSION_CODE = 101//权限Code
    }

    private var textTypeface: Typeface? = null
    private var descTypeFace: Typeface? = null
    private var alphaAnimation: AlphaAnimation? = null

    init {
        textTypeface =
            Typeface.createFromAsset(ContextUtil.getContext().assets, "fonts/Lobster-1.4.otf")
        descTypeFace = Typeface.createFromAsset(
            ContextUtil.getContext().assets,
            "fonts/FZLanTingHeiS-L-GB-Regular.TTF"
        )
    }

    override fun getLayoutId() = R.layout.activity_splash

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {
        tv_app_name.typeface = textTypeface
        tv_splash_desc.typeface = descTypeFace
        tv_version_name.text = "v${AppUtils.getVerName(applicationContext)}"

        //渐变展示启动屏
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(arg0: Animation) {
                forwardAndFinish(MainActivity::class.java)
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {}

        })

        recheckPermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        super.onPermissionsGranted(requestCode, perms)
        iv_web_icon.startAnimation(alphaAnimation)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        super.onPermissionsDenied(requestCode, perms)
        showPermissionDialog(getString(R.string.p_read_external_storage))
    }

    /**
     * 重新检查权限
     */
    override fun recheckPermissions() {
        super.recheckPermissions()
        requestPermissions(
            PERMISSION_CODE, getString(R.string.p_read_external_storage),
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}