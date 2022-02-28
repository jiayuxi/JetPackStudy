package com.jiayx.jetpackstudy.room.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jiayx.jetpackstudy.room.bean.StudentBean
import com.jiayx.jetpackstudy.room.dao.StudentDao
import com.jiayx.jetpackstudy.room.database.StudentDatabase
import kotlinx.coroutines.flow.Flow

/**
 *Created by yuxi_
on 2022/2/27
 */
class StudentRepository(private val context: Context) {
    private var studentDao: StudentDao? = null

    init {
        studentDao = StudentDatabase.getInstance(context).getStudentDao()
    }

    suspend fun insertStudent(vararg student: StudentBean?) {
        studentDao?.insertStudent(*student)
    }

    suspend fun updateStudent(vararg student: StudentBean?) {
        studentDao?.updateStudent(*student)
    }

    suspend fun deleteStudent(vararg student: StudentBean?) {
        studentDao?.deleteStudent(* student)
    }

    suspend fun deleteAll() {
        studentDao?.deleteAll()
    }

    fun queryAll(): LiveData<List<StudentBean>>? {
        return studentDao?.getAllStudent()
    }

    fun queryFlowAll(): Flow<List<StudentBean>>? = studentDao?.getFlowStudent()
}