package com.jiayx.jetpackstudy.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jiayx.jetpackstudy.room.bean.JStudentBean;

import java.util.List;

/**
 * Created by yuxi_
 * on 2022/2/27
 */
@Dao
public interface JStudentDao {
    @Insert
    void insertStudent(JStudentBean... studentBeans);

    @Update
    void updateStudent(JStudentBean... studentBeans);

    @Delete
    void deleteStudent(JStudentBean... studentBeans);

    @Query("DELETE FROM j_student")
    void deleteStudentAll();

    @Query("DELETE FROM j_student WHERE id = :id")
    void deleteStudentByName(int id);

    @Query("SELECT * FROM j_student")
    LiveData<List<JStudentBean>> getStudentAll();



}
