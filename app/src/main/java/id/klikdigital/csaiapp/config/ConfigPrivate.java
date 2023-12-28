package id.klikdigital.csaiapp.config;

import com.google.gson.Gson;


import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigPrivate {
    private static Retrofit retro;
//    private static final String URL ="https://188.166.186.222";
    private static final String URL="https://abe1-36-85-222-245.ngrok-free.app";
    public static Retrofit htppclient(){
        if (retro == null) {

            try {
                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                };

                // Install the all-trusting trust manager
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new SecureRandom());

                // Create an OkHttpClient that trusts all certificates
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                        .hostnameVerifier((hostname, session) -> true)
                        .build();

                // Build Retrofit with the OkHttpClient
                retro = new Retrofit.Builder()
                        .baseUrl(URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retro;
    }
}
