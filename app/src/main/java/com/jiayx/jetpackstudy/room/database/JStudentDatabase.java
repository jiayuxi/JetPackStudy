package com.jiayx.jetpackstudy.room.database;

/**
 * Created by yuxi_
 * on 2022/2/27
 */

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jiayx.jetpackstudy.room.bean.JStudentBean;
import com.jiayx.jetpackstudy.room.dao.JStudentDao;

@Database(entities = {JStudentBean.class}, version = 2, exportSchema = false)
public abstract class JStudentDatabase extends RoomDatabase {
    public static JStudentDatabase instance;
    public static final String DATABASE_NAME = "my_db.db";

    public static synchronized JStudentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), JStudentDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract JStudentDao getJStudentDao();
}
