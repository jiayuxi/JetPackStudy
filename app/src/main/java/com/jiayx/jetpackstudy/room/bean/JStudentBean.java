package com.jiayx.jetpackstudy.room.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by yuxi_
 * on 2022/2/27
 */
@Entity(tableName = "j_student")
public class JStudentBean {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    public int id;
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    public String name;
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    public int age;

    public JStudentBean(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Ignore
    public JStudentBean(int id) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public JStudentBean(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
