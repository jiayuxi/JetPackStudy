package com.jiayx.jetpackstudy.room.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jiayx.jetpackstudy.room.bean.JStudentBean;
import com.jiayx.jetpackstudy.room.dao.JStudentDao;
import com.jiayx.jetpackstudy.room.database.JStudentDatabase;

import java.util.List;

/**
 * Created by yuxi_
 * on 2022/2/27
 */
public class JStudentRepository {
    public JStudentDao jStudentDao;
    public JStudentRepository(Context context) {
        jStudentDao = JStudentDatabase.getInstance(context).getJStudentDao();
    }

    public void insert(JStudentBean... studentBeans) {
        new InsertAsyncTask(jStudentDao).execute(studentBeans);
    }

    public void update(JStudentBean... studentBeans) {
        new UpdateAsyncTask(jStudentDao).execute(studentBeans);
    }

    public void delete(JStudentBean... studentBeans) {
        new DeleteAsyncTask(jStudentDao).execute(studentBeans);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(jStudentDao).execute();
    }

    public LiveData<List<JStudentBean>> getAllJStudentLive() {
        return jStudentDao.getStudentAll();
    }
    static class InsertAsyncTask extends AsyncTask<JStudentBean, Void, Void> {
        private JStudentDao wordDao;

        InsertAsyncTask(JStudentDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(JStudentBean... words) {
            wordDao.insertStudent(words);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<JStudentBean, Void, Void> {
        private JStudentDao wordDao;

        UpdateAsyncTask(JStudentDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(JStudentBean... words) {
            wordDao.updateStudent(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<JStudentBean, Void, Void> {
        private JStudentDao wordDao;

        DeleteAsyncTask(JStudentDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(JStudentBean... words) {
            wordDao.deleteStudent(words);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private JStudentDao wordDao;

        DeleteAllAsyncTask(JStudentDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteStudentAll();
            return null;
        }
    }
}
