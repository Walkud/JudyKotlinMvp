package com.walkud.app.rx

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 线程调度器封装类
 * Rxjava2 避免打断链式结构：使用.compose( )操作符 http://www.jianshu.com/p/e9e03194199e
 * Created by Zhuliya on 2018/11/8
 */
class SchedulersCompat private constructor() {

    companion object {

        fun <T> applyComputationSchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }


        fun <T> applyIoSchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyNewSchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyTrampolineSchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.trampoline())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyExecutorSchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.from(ExecutorManager.eventExecutor))
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

}