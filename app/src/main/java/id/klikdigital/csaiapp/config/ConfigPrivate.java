package id.klikdigital.csaiapp.config;

import com.google.gson.Gson;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigPrivate {
    private static Retrofit retro;
    private static final String URL ="https://188.166.186.222";
//    private static final String URL="https://98f1-2001-448a-7069-2d92-c536-849c-4121-98a4.ngrok-free.app";
    public static Retrofit htppclient(){
        if (retro == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().hostnameVerifier((URL, sslSession) ->true).build();
            retro = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
        }
        return retro;
    }
}
