package id.klikdigital.csaiapp.config;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {
    private static Retrofit retro;
    private static final String URL ="https://engine.csai.id";
//    private static final String URL = "https://10f1-2001-448a-7069-2d92-c536-849c-4121-98a4.ngrok-free.app";
    public static Retrofit htppclient(){
        if (retro == null) {
            retro = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
        }
        return retro;
    }
}
