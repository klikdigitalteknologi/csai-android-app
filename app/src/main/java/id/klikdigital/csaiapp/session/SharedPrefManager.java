package id.klikdigital.csaiapp.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

        private static SharedPrefManager instance;
        private static Context mCtx;

        private  static final String SHARED_PREF_NAME = "data";
        private static final  String KEY_ID = "kd_user";
        private static final String KEY_MEMBER = "member_kode";
        private static final  String KEY_NAME = "nama_lengkap";
        private static final  String KEY_LEVEL = "id_level";

        private static final String KEY_STATUS = "status";
        private static final String KEY_USERNAME ="username";
        private static final String KEY_AMEMBER ="amember_kode";
        private static final String KEY_EXPIRED = "expired";
        String channelnotif = "channelA" ;
        String channelid = "default" ;


        private SharedPrefManager(Context context) {
            mCtx = context;
        }

        public static synchronized SharedPrefManager getInstance(Context context) {
            if (instance == null) {
                instance = new SharedPrefManager(context);
            }
            return instance;
        }


        public String getKeyName(){
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_NAME,null);
        }

        public  boolean session(String kd_user, String nama_lengkap, String username, String member_kode, String id_level,String status, String amember_kode, String expired ){

            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_ID, kd_user);
            editor.putString(KEY_NAME, nama_lengkap);
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_MEMBER,member_kode);
            editor.putString(KEY_AMEMBER,amember_kode);
            editor.putString(KEY_LEVEL,id_level);
            editor.putString(KEY_STATUS,status);
            editor.putString(KEY_EXPIRED,expired);
            editor.apply();
            return true;
        }

        public boolean isLoggedIn(){
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
            if (sharedPreferences.getString(KEY_ID, "0") != "0"){
                return true;
            }
            return false;
        }

        public  boolean logout() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            return true;
        }

        public String getKeyId(){
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_ID, null);
        }
        public String getKeyUsername(){
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_USERNAME, null);
        }
        public String getKeyLevel(){
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_LEVEL, null);
        }
        public String getKeyMember(){
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_MEMBER,null);
        }
    }

