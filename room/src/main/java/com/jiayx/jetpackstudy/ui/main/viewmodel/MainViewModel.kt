package com.jiayx.jetpackstudy.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jiayx.jetpackstudy.room.bean.JStudentBean
import com.jiayx.jetpackstudy.room.bean.StudentBean
import com.jiayx.jetpackstudy.room.repository.JStudentRepository
import com.jiayx.jetpackstudy.room.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: JStudentRepository? = null
    private var repository2: StudentRepository? = null

    init {
        repository = JStudentRepository(application)
        repository2 = StudentRepository(application)
    }

    fun getAllStudentLive(): LiveData<List<JStudentBean>>? {
        return repository?.allJStudentLive
    }

    fun insertWords(vararg words: JStudentBean?) {
        repository?.insert(*words)
    }

    fun updateWords(vararg words: JStudentBean?) {
        repository?.update(*words)
    }

    fun deleteWords(vararg words: JStudentBean?) {
        repository?.delete(*words)
    }

    fun deleteAllWords() {
        repository?.deleteAll()
    }

    //

    fun getAllStudents(): LiveData<List<StudentBean>>? {
        return repository2?.queryAll()
    }

    fun getFlowAll(): Flow<List<StudentBean>>? {
        return repository2?.queryFlowAll()?.catch { e -> e.message }?.flowOn(Dispatchers.IO)
    }

    fun insertStudent(vararg words: StudentBean?) {
        viewModelScope.launch {
            async {
                repository2?.insertStudent(*words)
            }
        }
    }

    fun updateStudent(vararg words: StudentBean?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository2?.updateStudent(*words)

        }
    }

    fun updateStudent(timeLong: Long, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository2?.updateStudent(timeLong, id)

        }
    }

    fun updateStudent(timeLong: Long, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository2?.updateStudent(timeLong, name)

        }
    }

    fun deleteStudent(vararg words: StudentBean?) {
        viewModelScope.launch {
            async {
                repository2?.deleteStudent(*words)

            }
        }
    }

    fun deleteToName(name: String) {
        viewModelScope.launch {
            async {
                repository2?.deleteToName(name)

            }
        }
    }

    fun deleteAllStudent() {
        viewModelScope.launch {
            async {
                repository2?.deleteAll()
            }
        }
    }
}