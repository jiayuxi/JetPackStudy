package com.jiayx.datastore.manage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jiayx.datastore.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 *Created by yuxi_
on 2022/5/16
 */
class DataStoreManage(private val context: Context) {

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
     */
    val userModel: Flow<UserModel> = context.dataStore.data.map { preferences ->
        UserModel(preferences[nameKey] ?: "known", preferences[emailKey] ?: "null")
    }
}