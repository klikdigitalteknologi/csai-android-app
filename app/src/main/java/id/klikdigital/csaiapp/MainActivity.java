package id.klikdigital.csaiapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import id.klikdigital.csaiapp.chat.acivity.ChatRoom;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.response.SendImageResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.config.ConfigPrivate;
import id.klikdigital.csaiapp.fragment.ChatFragment;
import id.klikdigital.csaiapp.fragment.LogoutPopup;
import id.klikdigital.csaiapp.login.activity.LoginActivity;
import id.klikdigital.csaiapp.session.SessionManage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private ImageView imageSender,bck;
    private String gambar,whatsapp,sender,message;
    private EditText chat;
    private AppCompatImageView btnSend;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSender = findViewById(R.id.terimagambar);
        bck = findViewById(R.id.backtoChatroom);
        chat = findViewById(R.id.chatEditext);
        btnSend = findViewById(R.id.btnSendImage);
        SessionManage sessionManage = new SessionManage(this);
        sender = sessionManage.getUserData().getMemberKode();
        bck.setOnClickListener(view -> {
            backtoChatlist();
        });
//        gambar = getIntent().getStringExtra("imagePath");
////        whatsapp = getIntent().getStringExtra("whatsapp");
//       if (gambar != null){
////           Picasso.get().load("file://"+gambar).into(imageSender);
////           imageSender.setVisibility(View.VISIBLE);
//       }else {
//           Toast.makeText(getApplicationContext(),"GAMBAR KOSONG",Toast.LENGTH_SHORT).show();
//       }
//       btnSend.setOnClickListener(view -> {
//           message = chat.getText().toString();
//           sendIamge();
//       });
    }

    private void backtoChatlist() {
        ChatFragment fragment = new ChatFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layoutintenttofragmentchat,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        ChatFragment fragment = new ChatFragment();
        @SuppressLint("CommitTransaction")
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutintenttofragmentchat,fragment).addToBackStack(null).commit();
        super.onBackPressed();
    }
}
