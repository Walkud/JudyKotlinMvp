package com.walkud.app.mvp.ui.activity

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hazz.kotlinmvp.glide.GlideApp
import com.orhanobut.logger.Logger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.walkud.app.R
import com.walkud.app.common.ExtraKey
import com.walkud.app.mvp.base.MvpActivity
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.presenter.VideoDetailPresenter
import com.walkud.app.mvp.ui.adapter.VideoDetailAdapter
import com.walkud.app.rx.transformer.SmartRefreshTransformer
import com.walkud.app.utils.CleanLeakUtils
import com.walkud.app.utils.MLog
import com.walkud.app.utils.StatusBarUtil
import com.walkud.app.view.VideoListener
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.activity_video_detail.*
import java.util.*

/**
 * 视频详情(播放)UI
 * Created by Zhuliya on 2018/11/27
 */
class VideoDetailActivity : MvpActivity<VideoDetailPresenter>() {

    private var transition: Transition? = null
    private var orientationUtils: OrientationUtils? = null
    private val videoDetailAdapter: VideoDetailAdapter = VideoDetailAdapter()

    private var isTransition: Boolean = false//是否执行进入动画
    private var isPlay: Boolean = false//是否为播放中
    private var isPause: Boolean = false//是否为暂停中


    companion object {

        /**
         * 跳转到视频详情页面播放
         *
         * @param activity
         * @param view
         * @param itemData
         */
        fun startActivity(activity: Activity, view: View, itemData: HomeBean.Issue.Item) {
            val intent = Intent(activity, VideoDetailActivity::class.java)
            intent.putExtra(ExtraKey.VIDEO_DATA, itemData)
            intent.putExtra(ExtraKey.TRANSITION, true)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val pair = Pair(view, ExtraKey.IMG_TRANSITION)
                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, pair)
                ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
            } else {
                activity.startActivity(intent)
                activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
            }
        }
    }

    override fun getP() = VideoDetailPresenter().apply { view = this@VideoDetailActivity }

    override fun getLayoutId(): Int = R.layout.activity_video_detail

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = videoDetailAdapter

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, mVideoView)

        /***  下拉刷新  ***/
        //内容跟随偏移
        mRefreshLayout.setOnRefreshListener {
            presenter.refreshVideoInfo()
        }

        presenter.init()
    }

    override fun <VT> getSmartRefreshTransformer(): ObservableTransformer<VT, VT> = SmartRefreshTransformer(mRefreshLayout)

    /**
     * 添加事件
     */
    override fun addListener() {
        super.addListener()

        //设置相关视频 Item 的点击事件
        videoDetailAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
            val itemData = adapter.getItem(position) as HomeBean.Issue.Item
            if (itemData.itemType == HomeBean.Issue.Item.ITEM_TYPE_VIDEO_SMALL_CARD) {
                //加载相关视频
                presenter.loadVideoInfo(itemData)
            }
        }

        //当前视频 子Item点击事件
        videoDetailAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_action_favorites -> showToast("喜欢")
                R.id.tv_action_share -> showToast("分享")
                R.id.tv_action_reply -> showToast("评论")
                R.id.tv_action_offline -> showToast("缓存")
                R.id.tv_attention -> showToast("关注")
            }
        }

        mVideoView.setStandardVideoAllCallBack(object : VideoListener {

            override fun onPrepared(url: String, vararg objects: Any) {
                super.onPrepared(url, *objects)
                //开始播放了才能旋转和全屏
                orientationUtils?.isEnable = true
                isPlay = true
            }

            override fun onAutoComplete(url: String, vararg objects: Any) {
                super.onAutoComplete(url, *objects)
                MLog.d("***** onAutoPlayComplete **** ")
            }

            override fun onPlayError(url: String, vararg objects: Any) {
                super.onPlayError(url, *objects)
                showToast("播放失败")
            }

            override fun onEnterFullscreen(url: String, vararg objects: Any) {
                super.onEnterFullscreen(url, *objects)
                MLog.d("***** onEnterFullscreen **** ")
            }

            override fun onQuitFullscreen(url: String, vararg objects: Any) {
                super.onQuitFullscreen(url, *objects)
                MLog.d("***** onQuitFullscreen **** ")
                //列表返回的样式判断
                orientationUtils?.backToProtVideo()
            }
        })
        //设置返回按键功能
        mVideoView.backButton.setOnClickListener { onBackPressed() }
        //设置全屏按键功能
        mVideoView.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils?.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            mVideoView.startWindowFullscreen(this, true, true)
        }
        //锁屏事件
        mVideoView.setLockClickListener { view, lock ->
            //配合下方的onConfigurationChanged
            orientationUtils?.isEnable = !lock
        }
    }

    /**
     * 初始化 VideoView 的配置
     */
    fun initVideoViewConfig(itemData: HomeBean.Issue.Item) {
        //设置旋转
        orientationUtils = OrientationUtils(this, mVideoView)
        //是否旋转
        mVideoView.isRotateViewAuto = false
        //是否可以滑动调整
        mVideoView.setIsTouchWiget(true)

        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        GlideApp.with(this)
                .load(itemData.data?.cover?.feed)
                .centerCrop()
                .into(imageView)
        mVideoView.thumbImageView = imageView
    }

    /**
     * 初始化动画
     */
    fun initTransition(isTransition: Boolean) {
        this.isTransition = isTransition
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(mVideoView, ExtraKey.IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            presenter.refreshVideoInfo()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener() {
        transition = window.sharedElementEnterTransition
        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
            }

            override fun onTransitionEnd(p0: Transition?) {
                MLog.d("onTransitionEnd()------")

                presenter.refreshVideoInfo()
                transition?.removeListener(this)
            }

        })
    }


    /**
     * 设置播放视频 URL
     */
    fun setVideo(url: String) {
        MLog.d("playUrl:$url")
        mVideoView.setUp(url, false, "")
        //开始自动播放
        mVideoView.startPlayLogic()
    }

    /**
     * 更新相关的数据视频
     */
    fun updateRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        videoDetailAdapter.setNewData(itemList)
    }

    /**
     * 更新背景颜色
     */
    fun updateBackgroundUi(url: String) {
        GlideApp.with(this)
                .load(url)
                .centerCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .transition(DrawableTransitionOptions().crossFade())
                .into(mVideoBackground)
    }

    /**
     * 横竖屏切换
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            mVideoView.onConfigurationChanged(this, newConfig, orientationUtils)
        }
    }

    /**
     * 监听返回键
     */
    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (StandardGSYVideoPlayer.backFromWindowFull(this))
            return
        //释放所有
        mVideoView.setStandardVideoAllCallBack(null)
        GSYVideoPlayer.releaseAllVideos()
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) run {
            super.onBackPressed()
        } else {
            backward()
            overridePendingTransition(R.anim.anim_out, R.anim.anim_in)
        }
    }

    /**
     * 恢复
     */
    override fun onResume() {
        super.onResume()
        getCurPlay().onVideoResume()
        isPause = false
    }

    /**
     * 暂停
     */
    override fun onPause() {
        super.onPause()
        getCurPlay().onVideoPause()
        isPause = true
    }

    /**
     * 销毁
     */
    override fun onDestroy() {
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.releaseListener()
    }

    /**
     * 获取当前播放控件
     */
    private fun getCurPlay(): GSYVideoPlayer {
        return if (mVideoView.fullWindowPlayer != null) {
            mVideoView.fullWindowPlayer
        } else mVideoView
    }
}