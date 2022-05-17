# ProtoBuf 基本类型数据

.proto type java/kotlin type

double double float float int32 int int64 long bool boolean string string bytes byteString

## PreferencesDataStore

//初始化 PreferenceDataStore private val Context.dataStore: DataStore<Preferences> by
preferencesDataStore("data")

// 定义键值 stringPreferencesKey,intPreferencesKey 等 private val nameKey = stringPreferencesKey("NAME")
private val emailKey = stringPreferencesKey("EMAIL")

/**

* 将内容写入 Preferences DataStore
  */ suspend fun saveData(model: UserModel) { context.dataStore.edit { preferences ->
  preferences[nameKey] = model.name preferences[emailKey] = model.email } }

/**

* 从 Preferences DataStore 读取内容
* 可以进行异常处理
  */ val userModel: Flow<UserModel> = context.dataStore.data.catch { exception ->
  if (exception is IOException) { Log.e(TAG, "Error reading preference. $exception ")
  emit(emptyPreferences())
  } else { throw exception } }.map { preferences ->
  // if(preferences[nameKey].isNullOrBlank()) return@map null UserModel(preferences[nameKey] ?: "
  known", preferences[emailKey] ?: "null")
  }

  /**
    * 清空 Preferences DataStore 保存值
      */ suspend fun logout() { context.dataStore.edit { preference ->
      preference.remove(nameKey)
      preference.remove(emailKey)
      // 清空所有 preference.clear()
      } }

小结： 1、缓存 ：使用StateFlow 的特性，缓存 value - downstreamFlow 
      2、文件读取 ： 通过 Serializer ，扩展高
      3、一致性：
              3.1、先存盘，在更新内存，通知外部
              3.2、SimpleActor 利用 Channel 实现了协程并发的顺序执行 
      4、异步型：通过 Flow 便于页面的监听（数据驱动UI） 