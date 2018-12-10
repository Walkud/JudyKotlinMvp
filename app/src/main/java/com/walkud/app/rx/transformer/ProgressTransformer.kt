package com.walkud.app.rx.transformer

import android.app.Activity
import android.app.ProgressDialog
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * Created by Zhuliya on 2018/11/21
 */
class ProgressTransformer<T>(activity: Activity) : ObservableTransformer<T, T> {

    private val subject: Subject<Boolean> = PublishSubject.create()
    private var progressDialog: ProgressDialog? = null


    init {
        progressDialog = ProgressDialog(activity)
        progressDialog?.setOnCancelListener {
            subject.onNext(true)
        }
    }


    override fun apply(upstream: Observable<T>): ObservableSource<T> {

        return upstream.takeUntil(subject.filter {
            it
        }.take(1))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    show()
                }
                .doOnComplete {
                    dismiss()
                }
                .doOnError {
                    dismiss()
                }
    }

    private fun show() {
        progressDialog?.show()
    }

    private fun dismiss() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}