package com.jiayx.component.binderdemo.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jiayx.component.binderdemo.bean.Person;
import com.jiayx.component.binderdemo.common.Stub;

import java.util.ArrayList;
import java.util.List;

public class RemoteService extends Service {

    private ArrayList<Person> persons;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        persons = new ArrayList<>();
        Log.e("jia_binder", "binder service success onBind");
        return iBinder;
    }

    private IBinder iBinder = new Stub() {

        @Override
        public void addPerson(Person person) throws RemoteException {
            persons.add(person);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return persons;
        }
    };
}
