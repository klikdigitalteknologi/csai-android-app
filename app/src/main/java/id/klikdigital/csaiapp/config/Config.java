package id.klikdigital.csaiapp.config;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {
    private static Retrofit retro;
//    private static final String URL ="https://engine.csai.id";
    private static final String URL = "https://testpublic.laminhdatau.online";
//        private static final String URL ="https://ac46-2001-448a-7067-21de-76e5-8df2-8da5-79c.ngrok-free.app";
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
