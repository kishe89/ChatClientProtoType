package com.example.kjw.chatclientprototype;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by kjw on 16. 8. 21..
 */
public class PreferenceManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private String nick;
    private int FILEnubmer;

    public PreferenceManager(Context context) {
        this.context = context;
        pref=context.getSharedPreferences("datdatsdasfw",context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setUserName(String userName){
        editor.putString("mUserName",userName);
        editor.commit();
    }
    public String getUserName(){
        return pref.getString("mUserName",null);
    }

}
