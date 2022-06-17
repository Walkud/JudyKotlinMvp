package com.walkud.app.mvp.model

import android.annotation.SuppressLint
import android.widget.Space
import com.walkud.app.net.space.LemonSpace
import com.walkud.app.common.Constants
import com.walkud.app.mvp.model.bean.CategoryBean
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.model.bean.TabInfoBean
import com.walkud.app.net.LemonManager
import com.walkud.app.utils.WatchHistoryUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * 业务模型
 * Created by Zhuliya on 2018/11/7
 */
class MainModel {

    private val apiService by lazy {
        LemonManager.service
    }

    /**
     * 获取首页 Banner 数据
     */
    fun getFirstHomeData(): LemonSpace<HomeBean> {
        return LemonSpace.create {
            val bannerHomeBean = apiService.getFirstHomeData(1)//1、先请求Banner数据
            bannerHomeBean.filterAdFun()//2、过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
            val homeBean = apiService.getMoreHomeData(bannerHomeBean.nextPageUrl)
                .filterAdFun()//3、根据 nextPageUrl 请求下一页数据
            homeBean.bannerData = bannerHomeBean//4、将Banner数据设置到主页对象中
            homeBean
        }
    }

    /**
     * 获取首页列表数据
     * @param url
     */
    fun getMoreHomeData(url: String): LemonSpace<HomeBean> =
        LemonSpace.create {
            apiService.getMoreHomeData(url).filterAdFun()
        }

    /**
     * 请求热门关键词的数据
     */
    fun getHotWordData(): LemonSpace<ArrayList<String>> = apiService.getHotWord()

    /**
     * 搜索关键词返回的结果
     * @param words
     */
    fun getSearchResult(words: String): LemonSpace<HomeBean.Issue> =
        apiService.getSearchData(words)

    /**
     * 搜索结果列表加载更多数据
     * @param url
     */
    fun getSearchIssueData(url: String): LemonSpace<HomeBean.Issue> =
        apiService.getIssueData(url)

    /**
     * 根据item id获取相关视频
     * @param id
     */
    fun getRelatedData(id: Long): LemonSpace<HomeBean.Issue> =
        apiService.getRelatedData(id)

    /**
     * 获取关注信息
     */
    fun getFollowInfo(): LemonSpace<HomeBean.Issue> = apiService.getFollowInfo()

    /**
     * 关注列表加载更多数据
     * @param url
     */
    fun getFollowIssueData(url: String): LemonSpace<HomeBean.Issue> =
        apiService.getIssueData(url)

    /**
     * 获取分类信息
     */
    fun getCategoryData(): LemonSpace<ArrayList<CategoryBean>> = apiService.getCategory()


    /**
     * 获取分类详情下的List 第一页数据
     * @param id
     */
    fun getCategoryDetailList(id: Long): LemonSpace<HomeBean.Issue> =
        apiService.getCategoryDetailList(id)

    /**
     * 获取分类详情列表更多数据
     * @param url
     */
    fun getMoreCategoryData(url: String): LemonSpace<HomeBean.Issue> =
        apiService.getIssueData(url)

    /**
     * 获取热门-排行列表Issue数据
     * @param url
     */
    fun getHotRankData(url: String): LemonSpace<HomeBean.Issue> =
        apiService.getIssueData(url)

    /**
     * 获取热门 Tab 数据
     */
    fun getRankTabData(): LemonSpace<TabInfoBean> = apiService.getRankList()

    /**
     * 保存观看记录
     * @param watchItem 观看记录
     */
    @SuppressLint("SimpleDateFormat")
    fun saveWatchVideoHistory(watchItem: HomeBean.Issue.Item) {
        Thread {
            //保存之前要先查询sp中是否有该value的记录，有则删除.这样保证搜索历史记录不会有重复条目
            val historyMap =
                WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME) as Map<*, *>
            for ((key, _) in historyMap) {
                if (watchItem == WatchHistoryUtils.getObject(
                        Constants.FILE_WATCH_HISTORY_NAME,
                        key as String
                    )
                ) {
                    WatchHistoryUtils.remove(Constants.FILE_WATCH_HISTORY_NAME, key)
                }
            }

            val format = SimpleDateFormat("yyyyMMddHHmmss")
            WatchHistoryUtils.putObject(
                Constants.FILE_WATCH_HISTORY_NAME,
                watchItem,
                "" + format.format(Date())
            )
        }.start()
    }

    /**
     * 获取观看的历史记录
     */
    fun getWatchHistory(): LemonSpace<ArrayList<HomeBean.Issue.Item>> {
        return LemonSpace.create {
            val historyMax = 20//最大显示条数
            val watchList = ArrayList<HomeBean.Issue.Item>()
            val hisAll = WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME) as Map<*, *>
            //将key排序升序
            val keys = hisAll.keys.toTypedArray()
            Arrays.sort(keys)
            val keyLength = keys.size
            //这里计算 如果历史记录条数是大于 可以显示的最大条数，则用最大条数做循环条件，防止历史记录条数-最大条数为负值，数组越界
            val hisLength = if (keyLength > historyMax) historyMax else keyLength
            // 反序列化和遍历 添加观看的历史记录
            (1..hisLength).mapTo(watchList) { position ->
                WatchHistoryUtils.getObject(
                    Constants.FILE_WATCH_HISTORY_NAME,
                    keys[keyLength - position] as String
                ) as HomeBean.Issue.Item
            }
            watchList
        }
    }

}
