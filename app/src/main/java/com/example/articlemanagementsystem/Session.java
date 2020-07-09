package com.example.articlemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.articlemanagementsystem.Activity.ArticleDashboard;
import com.example.articlemanagementsystem.Activity.LoginActivity;

import java.util.HashMap;

public class Session {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private static final String ID = "ID";

    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }

    public void createSession(String id){
        editor.putBoolean(LOGIN, true);
        editor.putString("ID", id);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }
    public HashMap<String, String> getUserId(){
        HashMap<String, String> userId = new HashMap<>();
        userId.put(ID, sharedPreferences.getString(ID, null));
        return  userId;
    }

    @Override
    public String toString() {
        return  getUserId().get(ID) ;
    }

    public void checkLogin(){

        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((ArticleDashboard) context).finish();
        }
    }

    public void logout(){
        editor.clear();
        editor.commit();


    }

}
