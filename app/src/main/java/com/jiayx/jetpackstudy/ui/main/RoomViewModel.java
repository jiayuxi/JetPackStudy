package com.jiayx.jetpackstudy.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jiayx.jetpackstudy.room.bean.JStudentBean;
import com.jiayx.jetpackstudy.room.repository.JStudentRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by yuxi_
 * on 2022/2/27
 */
public class RoomViewModel extends AndroidViewModel {
    private JStudentRepository repository;


    public RoomViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new JStudentRepository(application);
    }

    public LiveData<List<JStudentBean>> getAllStudentLive() {
        return repository.getAllJStudentLive();
    }

    public void insertWords(JStudentBean... beans) {
        repository.insert(beans);
    }

    public void updateWords(JStudentBean... beans) {
        repository.update(beans);
    }

    public void deleteWords(JStudentBean... beans) {
        repository.delete(beans);
    }

    public void deleteAllWords() {
        repository.deleteAll();
    }
}
