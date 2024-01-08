package id.klikdigital.csaiapp.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import id.klikdigital.csaiapp.login.model.UserItem;

public class SessionManage {

    private static final String SHARED_PREF_NAME = "MySharedPref";
    private static final String KEY_USER_DATA = "data";
    private static final String KEY_SESI = "isLoggedIn";

    private static SessionManage instance;
    private final SharedPreferences sharedPreferences;

    public SessionManage(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SessionManage getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManage(context);
        }
        return instance;
    }

    public void saveUserData(UserItem userItem) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userItem);
        editor.putString(KEY_USER_DATA, json);
        editor.putBoolean(KEY_SESI, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_SESI, false);
    }

    public UserItem getUserData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_USER_DATA, null);
        return gson.fromJson(json, UserItem.class);
    }

    public boolean clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
