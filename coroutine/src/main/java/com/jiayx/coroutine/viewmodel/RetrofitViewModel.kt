package com.jiayx.coroutine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hencoder.coroutinescamp.model.Repo
import com.jiayx.coroutine.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *Created by yuxi_
on 2022/4/11
 */
class RetrofitViewModel : ViewModel() {
    private val repository by lazy { Repository() }

    private val stateFlow = MutableStateFlow<String?>(null)
    val resultFlow = stateFlow.asStateFlow()

    /**
     * 协程网络请求
     */
    fun coroutineRetrofit() {
        viewModelScope.launch {
            val job = async {
                repository.loadUsersKt("rengwuxian")
            }
            val job2 = async {
                repository.loadUsersKt("google")
            }
            emitMsg("${job.await()[0].name}" + "${job2.await()[0].name}")
        }
    }

    /**
     * retrofit 网络请求
     */
    fun retrofit() {
        repository.retrofit("rengwuxian").enqueue(object : Callback<List<Repo>?> {
            override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
                val name = response.body()?.get(0)?.name
                emitMsg(name)
            }

            override fun onFailure(call: Call<List<Repo>?>, t: Throwable) {
                emitMsg(t.message)
            }
        })
    }

    /**
     * Rxjava + retrofit 网络请求
     */
    fun rxjavaRetrofit() {
        Single.zip<List<Repo>, List<Repo>, String>(repository.rxjavaRetrofit("rengwuxian"),
            repository.rxjavaRetrofit("google"),
            BiFunction { t1, t2 ->
                "${t1[0].name} - ${t2[0].name}"
            }
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: String) {
                    emitMsg(t)
                }

                override fun onError(e: Throwable) {
                    emitMsg(e.message)
                }
            })
    }

    private fun emitMsg(msg: String?) {
        viewModelScope.launch {
            stateFlow.emit(msg)
        }
    }

}