package com.walkud.app.mvp.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 * Created by xuhao on 2017/11/21.
 * desc: 首页 Bean（视频详情，相关等）
 */
data class HomeBean(
    val issueList: ArrayList<Issue>,
    val nextPageUrl: String,
    val nextPublishTime: Long,
    val newestIssueType: String,
    val dialog: Any,
    var bannerData: HomeBean?
) {

    data class Issue(
        val releaseTime: Long,
        val type: String,
        val date: Long,
        val total: Int,
        val publishTime: Long,
        val itemList: ArrayList<Item>,
        var count: Int,
        val nextPageUrl: String
    ) {

        data class Item(val type: String, val data: Data?, val tag: String) : Serializable,
            MultiItemEntity {

            companion object {
                //默认
                const val ITEM_TYPE_CONTENT = 0    //item

                //Home
                const val ITEM_TYPE_TEXT_HEADER = 1   //textHeader

                //详情
                const val ITEM_TYPE_TEXT_CARD = 11    //textCard
                const val ITEM_TYPE_VIDEO_SMALL_CARD = 12    //textCard
            }

            override fun getItemType(): Int {
                var itype = ITEM_TYPE_CONTENT
                when (type) {
                    "textHeader" -> itype = ITEM_TYPE_TEXT_HEADER
                    "textCard" -> itype = ITEM_TYPE_TEXT_CARD
                    "videoSmallCard" -> itype = ITEM_TYPE_VIDEO_SMALL_CARD
                }

                return itype
            }


            data class Data(
                val dataType: String,
                val text: String,
                val videoTitle: String,
                val id: Long,
                val title: String,
                val slogan: String?,
                val description: String,
                val actionUrl: String,
                val provider: Provider,
                val category: String,
                val parentReply: ParentReply,
                val author: Author,
                val cover: Cover,
                val likeCount: Int,
                val playUrl: String,
                val thumbPlayUrl: String,
                val duration: Long,
                val message: String,
                val createTime: Long,
                val webUrl: WebUrl,
                val library: String,
                val user: User,
                val playInfo: ArrayList<PlayInfo>?,
                val consumption: Consumption,
                val campaign: Any,
                val waterMarks: Any,
                val adTrack: Any,
                val tags: ArrayList<Tag>,
                val type: String,
                val titlePgc: Any,
                val descriptionPgc: Any,
                val remark: String,
                val idx: Int,
                val shareAdTrack: Any,
                val favoriteAdTrack: Any,
                val webAdTrack: Any,
                val date: Long,
                val promotion: Any,
                val label: Any,
                val labelList: Any,
                val descriptionEditor: String,
                val collected: Boolean,
                val played: Boolean,
                val subtitles: Any,
                val lastViewTime: Any,
                val playlists: Any,
                val header: Header,
                val itemList: ArrayList<Item>
            ) : Serializable {
                data class Tag(
                    val id: Int,
                    val name: String,
                    val actionUrl: String,
                    val adTrack: Any
                ) : Serializable

                data class Author(val icon: String, val name: String, val description: String) :
                    Serializable

                data class Provider(val name: String, val alias: String, val icon: String) :
                    Serializable

                data class Cover(
                    val feed: String, val detail: String,
                    val blurred: String, val sharing: String, val homepage: String
                ) : Serializable

                data class WebUrl(val raw: String, val forWeibo: String) : Serializable

                data class PlayInfo(
                    val name: String,
                    val url: String,
                    val type: String,
                    val urlList: ArrayList<Url>
                ) : Serializable

                data class Consumption(
                    val collectionCount: Int,
                    val shareCount: Int,
                    val replyCount: Int
                ) : Serializable

                data class User(
                    val uid: Long,
                    val nickname: String,
                    val avatar: String,
                    val userType: String,
                    val ifPgc: Boolean
                ) : Serializable

                data class ParentReply(val user: User, val message: String) : Serializable

                data class Url(val size: Long) : Serializable

                data class Header(
                    val id: Int,
                    val icon: String,
                    val iconType: String,
                    val description: String,
                    val title: String,
                    val font: String,
                    val cover: String,
                    val label: Label,
                    val actionUrl: String,
                    val subtitle: String,
                    val labelList: ArrayList<Label>
                ) : Serializable {
                    data class Label(
                        val text: String,
                        val card: String,
                        val detial: Any,
                        val actionUrl: Any
                    )
                }

            }
        }


    }

    fun filterAdFun() = apply {
        //过滤掉 Banner2(包含广告,等不需要的 Type), 具体查看接口分析
        val bannerItemList = issueList[0].itemList

        bannerItemList.filter { item ->
            item.type == "banner2" || item.type == "horizontalScrollCard"
        }.forEach { item ->
            //移除 item
            bannerItemList.remove(item)
        }
    }

}


//    "issueList": [],
//    "nextPageUrl": "http://baobab.kaiyanapp.com/api/v2/feed?date=1503104400000&num=1",
//    "nextPublishTime": 1503277200000,
//    "newestIssueType": "morning",
//    "dialog": null