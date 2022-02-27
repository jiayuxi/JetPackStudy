package com.jiayx.jetpackstudy.ui.main

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

    fun getAllStudents(): LiveData<List<StudentBean>>? {
        return repository2?.queryAll()
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

    fun deleteStudent(vararg words: StudentBean?) {
        viewModelScope.launch {
            async {
                repository2?.deleteStudent(*words)

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