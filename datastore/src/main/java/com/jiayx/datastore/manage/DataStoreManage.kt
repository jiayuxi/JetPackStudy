package com.jiayx.datastore.manage

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jiayx.datastore.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 *Created by yuxi_
on 2022/5/16
 */
class DataStoreManage(private val context: Context) {

    companion object {
        const val TAG = "preferencesStore"
    }

    //    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data",
//        produceMigrations = { context -> // 迁移 sharedPreferences
//            listOf(SharedPreferencesMigration(context,""))
//        })
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data")

    private val nameKey = stringPreferencesKey("NAME")
    private val emailKey = stringPreferencesKey("EMAIL")

    /**
     *   将内容写入 Preferences DataStore
     */
    suspend fun saveData(model: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[nameKey] = model.name
            preferences[emailKey] = model.email
        }
    }

    /**
     *  从 Preferences DataStore 读取内容
     *  可以进行异常处理
     */
    val userModel: Flow<UserModel> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading preference. $exception ")
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
//        if(preferences[nameKey].isNullOrBlank()) return@map null
        UserModel(preferences[nameKey] ?: "known", preferences[emailKey] ?: "null")
    }

    /**
     * 清空 Preferences DataStore 保存值
     */
    suspend fun logout() {
        context.dataStore.edit { preference ->
            preference.remove(nameKey)
            preference.remove(emailKey)
            // 清空所有
            preference.clear()
        }
    }
}