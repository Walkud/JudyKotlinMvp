package com.walkud.app.api

import com.lemon.core.Api
import com.lemon.core.ApiField
import com.lemon.core.ApiPath
import com.lemon.core.request.HttpMethod
import com.walkud.app.net.space.LemonSpace
import com.walkud.app.mvp.model.bean.AuthorInfoBean
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.model.bean.TabInfoBean

/**
 * 后端 Api 接口
 * Created by xuhao on 2017/11/16.
 *
 */
interface ApiService {

    /**
     * 首页精选
     * @param num
     */
    @Api("v2/feed?", method = HttpMethod.GET)
    fun getFirstHomeData(@ApiField("num") num: Int): HomeBean

    /**
     * 根据 nextPageUrl 请求数据下一页数据
     * @param url
     */
    @Api("{url}", method = HttpMethod.GET)
    fun getMoreHomeData(@ApiPath("url") url: String): HomeBean

    /**
     * 根据item id获取相关视频
     * @param id
     */
    @Api("v4/video/related?", method = HttpMethod.GET)
    fun getRelatedData(@ApiField("id") id: Long): LemonSpace<HomeBean.Issue>

    /**
     * 获取分类
     */
    @Api("v4/categories", method = HttpMethod.GET)
    fun getCategory(): LemonSpace<ArrayList<CategoryBean>>

    /**
     * 获取分类详情List
     * @param id
     */
    @Api("v4/categories/videoList?", method = HttpMethod.GET)
    fun getCategoryDetailList(@ApiField("id") id: Long): LemonSpace<HomeBean.Issue>

    /**
     * 获取更多的 Issue
     * @param url
     */
    @Api("{url}", method = HttpMethod.GET)
    fun getIssueData(@ApiPath("url") url: String): LemonSpace<HomeBean.Issue>

    /**
     * 获取全部排行榜的Info（包括，title 和 Url）
     */
    @Api("v4/rankList", method = HttpMethod.GET)
    fun getRankList(): LemonSpace<TabInfoBean>

    /**
     * 获取搜索信息
     * @param words
     */
    @Api("v1/search?&num=10&start=10", method = HttpMethod.GET)
    fun getSearchData(@ApiField("query") words: String): LemonSpace<HomeBean.Issue>

    /**
     * 热门搜索词
     */
    @Api("v3/queries/hot", method = HttpMethod.GET)
    fun getHotWord(): LemonSpace<ArrayList<String>>

    /**
     * 关注
     */
    @Api("v4/tabs/follow", method = HttpMethod.GET)
    fun getFollowInfo(): LemonSpace<HomeBean.Issue>

    /**
     * 作者信息
     * @param id
     */
    @Api("v4/pgcs/detail/tab?", method = HttpMethod.GET)
    fun getAuthorInfo(@ApiField("id") id: Long): LemonSpace<AuthorInfoBean>


}