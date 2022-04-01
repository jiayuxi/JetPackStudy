package com.jiayx.flow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jiayx.flow.retrofit.Article
import com.jiayx.flow.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/1
 */
class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * 定义 liveData 数据类型
     */
    var articles = MutableLiveData<List<Article>>()
    val data by lazy {
        var temp = arrayListOf<String>()
        temp.add("Refactored versions of the Android APIs that are not bundled with the operating system.");
        temp.add("Jetpack Compose is a modern toolkit for building native Android UI. Jetpack Compose simplifies and accelerates UI development on Android with less code, powerful tools, and intuitive Kotlin APIs.");
        temp.add("Includes APIs for testing your Android app, including Espresso, JUnit Runner, JUnit4 rules, and UI Automator.");
        temp.add("Includes ConstraintLayout and related APIs for building constraint-based layouts.");
        temp.add("Includes APIs to help you write declarative layouts and minimize the glue code necessary to bind your application logic and layouts.");
        temp.add("Provides APIs for building Android Automotive apps.");
        temp.add("A library for building Android Auto apps. This library is currently in beta. You can design, develop, and test navigation, parking, and charging apps for Android Auto, but you can't distribute these apps through the Google Play Store yet. We will make announcements in the future when you can distribute these apps through the Google Play Store.");
        temp.add("Provides APIs to build apps for wearable devices running Wear OS by Google.");
        temp.add("Material Components for Android (MDC-Android) help developers execute Material Design to build beautiful and functional Android apps.");
        temp.add("The Android NDK is a toolset that lets you implement parts of your app in native code, using languages such as C and C++.");
        temp.add("The Android Gradle Plugin (AGP) is the supported build system for Android applications and includes support for compiling many different types of sources and linking them together into an application that you can run on a physical Android device or an emulator.");
        return@lazy temp
    }

    fun searchArticle(key: String) {
        viewModelScope.launch {
            flow {
                //这里就是通过Retrofit从服务器拿到对应key过滤后的文章内容 ，模拟从服务器获取数据
//                val list = RetrofitClient.articleApi.searchArticles(key)
                emit(getData(key))
            }.flowOn(Dispatchers.IO)
                .catch { e -> e.printStackTrace() }
                .collect {
                    articles.value = it
                }
        }
    }

    private fun getData(key: String): List<Article> {
        val list = arrayListOf<Article>()
        data.forEachIndexed { index, s ->
            if (s.contains(key)) {
                list.add(Article(index, s))
            }
        }
        return list
    }
}