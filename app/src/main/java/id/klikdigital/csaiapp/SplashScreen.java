package id.klikdigital.csaiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import id.klikdigital.csaiapp.login.activity.LoginActivity;
import id.klikdigital.csaiapp.session.SessionManage;

public class SplashScreen extends AppCompatActivity {

    private String member = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            // Mengambil pathSegments dari URI
            List<String> pathSegments = uri.getPathSegments();
            // Memeriksa apakah pathSegments tidak kosong dan memiliki elemen yang cukup
            if (pathSegments != null && pathSegments.size() > 1) {
                // Mendapatkan nilai dari elemen kedua (indeks 1)
                member = pathSegments.get(1);
                Log.d("SplashScreen", "Data: " + member);
//                Toast.makeText(getApplicationContext(), "Data: " + member, Toast.LENGTH_LONG).show();
            } else {
                Log.d("SplashScreen", "Path tidak sesuai ekspektasi.");
                Toast.makeText(getApplicationContext(), "Path tidak sesuai ekspektasi.", Toast.LENGTH_LONG).show();
            }
        }

        Handler handler = new Handler();
if (isConnected()){
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("member", member);
            startActivity(intent);
            finish();
        }
    }, 3000);
} else {
    Toast.makeText(getApplicationContext(),"TIDAK ADA KONEKSI INTERNET",Toast.LENGTH_SHORT).show();
}
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}

