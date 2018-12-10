package com.walkud.app.common

/**
 * Created by Zhuliya on 2018/12/4
 */
class ExtraKey private constructor() {

    companion object {

        //共有title Key
        const val COMMON_TITLE = "title"

        //分类详情页
        const val CATEGORY_DATA = "category_data"//分类类型数据

        //视频播放详情页
        const val VIDEO_DATA = "video_data"//视频实数
        const val IMG_TRANSITION = "img_transition"//图片过渡标记
        const val TRANSITION = "transition"//是否过渡标记

        //热门排行榜
        const val API_URL = "apiUrl"// 请求ApiUrl
    }
}