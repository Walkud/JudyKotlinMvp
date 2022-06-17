package com.walkud.app.mvp.presenter

import com.walkud.app.mvp.base.BasePresenter
import com.walkud.app.mvp.model.MainModel
import com.walkud.app.mvp.model.bean.HomeBean
import com.walkud.app.mvp.ui.fragment.FollowFragment
import com.walkud.app.net.space.bindUi
import com.walkud.app.view.ProgressView

/**
 * 发现-关注Presenter
 * Created by Zhuliya on 2018/11/29
 */
class FollowPresenter : BasePresenter<FollowFragment, MainModel>() {

    private var issue: HomeBean.Issue? = null
    var nextPageUrl: String? = null

    /**
     * 是否为首页
     */
    private fun isFirstPage() = (issue == null)

    /**
     *  获取关注数据
     */
    fun queryFollowList() {

        val lemonSpace = if (nextPageUrl == null) {
            model.getFollowInfo()
        } else {
            model.getFollowIssueData(nextPageUrl!!)
        }

        val progressView =
            if (isFirstPage()) view.getMultipleStatusProgressView() else ProgressView.EMPTY

        //如果出现返回的数据类型不匹配，可以取消注释,也可以封装为事务
//                    //由于接口不能保证返回的类型，所以在这里对源数据进行处理，当然，也可以将该代码放在Model中进行处理
//                    val iterator = result.itemList.iterator()
//                    while (iterator.hasNext()) {
//                        if (iterator.next().type != "videoCollectionWithBrief") {
//                            iterator.remove()
//                        }
//                    }
        lemonSpace.bindUi(progressView, view)
            .doError { view.showExceptionToast(it) }
            .request {
                if (issue == null) {//首页
                    issue = it
                } else {//加载更多
                    issue!!.itemList.addAll(it.itemList)
                }

                nextPageUrl = it.nextPageUrl
                view.updateListUi(issue!!)
            }
    }

}