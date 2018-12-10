package com.walkud.app.mvp.presenter

import com.walkud.app.common.ExtraKey
import com.walkud.app.common.extensions.dataFormat
import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.activity.VideoDetailActivity
import com.walkud.app.rx.RxSubscribe
import com.walkud.app.rx.transformer.NetTransformer
import com.walkud.app.utils.DisplayManager
import com.walkud.app.utils.NetworkUtil

/**
 * 视频详情Presenter
 * Created by Zhuliya on 2018/11/27
 */
class VideoDetailPresenter : BasePresenter<VideoDetailActivity, MainModel>() {

    /**
     * Item 详细数据
     */
    private lateinit var itemData: HomeBean.Issue.Item

    private var isTransition: Boolean = false//是否执行进入动画

    /**
     * 初始化
     */
    fun init() {
        itemData = view.intent.getSerializableExtra(ExtraKey.VIDEO_DATA) as HomeBean.Issue.Item
        isTransition = view.intent.getBooleanExtra(ExtraKey.TRANSITION, false)

        if (itemData.data == null) {
            view.showToast("数据错误")
            view.backward()
            return
        }


        //过渡动画
        view.initTransition(isTransition)
        view.initVideoViewConfig(itemData)
    }


    /**
     * 刷新视频及相关数据
     */
    fun refreshVideoInfo() {
        loadVideoInfo(itemData)
    }

    /**
     * 加载视频及相关数据
     */
    fun loadVideoInfo(itemData: HomeBean.Issue.Item) {
        //缓存当前观看记录
        model.saveWatchVideoHistory(itemData)

        val playInfo = itemData.data!!.playInfo

        if (playInfo!!.size > 1) {
            // 当前网络是 Wifi环境下选择高清的视频
            val netType = NetworkUtil.isWifi(view.applicationContext)
            val type = if (netType) "high" else "normal"
            for (i in playInfo) {
                if (i.type == type) {
                    val playUrl = i.url
                    view.setVideo(playUrl)

                    //移动流量提示
                    if (!netType && NetworkUtil.isNetworkAvailable(view.applicationContext)) {
                        //Todo 待完善弹框提示
                        view.showToast("本次消耗${view.dataFormat(i.urlList[0].size)}流量")
                    }
                    break
                }
            }
        } else {
            view.setVideo(itemData.data.playUrl)
        }

        //设置背景
        val backgroundUrl = itemData.data.cover.blurred + "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
        view.updateBackgroundUi(backgroundUrl)

        //加载相关数据
        loadRelatedVideo()
    }

    /**
     * 加载相关的视频数据
     */
    private fun loadRelatedVideo() {
        val id = itemData.data?.id ?: 0
        model.getRelatedData(id)
                .compose(NetTransformer())
                .compose(view.getSmartRefreshTransformer())
                .subscribe(object : RxSubscribe<HomeBean.Issue>() {
                    override fun call(result: HomeBean.Issue) {
                        //添加当前视频相关数据
                        result.itemList.add(0, itemData)
                        view.updateRecentRelatedVideo(result.itemList)
                    }
                })

    }

}