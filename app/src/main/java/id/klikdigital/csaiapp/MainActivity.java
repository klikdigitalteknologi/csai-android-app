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

    private ImageView imageSender;
    private String gambar,whatsapp,sender,message;
    private EditText chat;
    private AppCompatImageView btnSend;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSender = findViewById(R.id.terimagambar);
        chat = findViewById(R.id.chatEditext);
        btnSend = findViewById(R.id.btnSendImage);
        SessionManage sessionManage = new SessionManage(this);
        sender = sessionManage.getUserData().getMemberKode();
        gambar = getIntent().getStringExtra("imagePath");
        whatsapp = getIntent().getStringExtra("whatsapp");
       Log.d("RESPONSE",gambar);
       if (gambar != null){
           Picasso.get().load("file://"+gambar).into(imageSender);
           imageSender.setVisibility(View.VISIBLE);
       }else {
           Toast.makeText(getApplicationContext(),"GAMBAR KOSONG",Toast.LENGTH_SHORT).show();
       }
       btnSend.setOnClickListener(view -> {
           message = chat.getText().toString();
           sendIamge();
       });
    }
    private void sendIamge() {
        String aa = "/" + gambar;
        RequestBody senderBody = RequestBody.create(MediaType.parse("text/plain"), sender);
        RequestBody waBody = RequestBody.create(MediaType.parse("text/plain"), whatsapp);
        RequestBody pesanBody = RequestBody.create(MediaType.parse("text/plain"), message);

        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), gambar);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file",gambar, imageBody);
        ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
        Call<SendImageResponse>call = chatService.sendImage(senderBody,waBody,imagePart,pesanBody);
        call.enqueue(new Callback<SendImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendImageResponse> call, @NonNull Response<SendImageResponse> response) {
                if (response.isSuccessful()){
                    SendImageResponse sendImageResponse = response.body();
                    if (sendImageResponse.isStatus()){
                        Toast.makeText(getApplicationContext(),"sedang mengirim",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ChatRoom.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("nomor_wa",whatsapp);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"Gagal mengirim gambar",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"server not responding",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SendImageResponse> call, @NonNull Throwable t) {
                Log.d("Response Send Image",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChatRoom.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("nomor_wa",whatsapp);
        startActivity(intent);
        super.onBackPressed();
    }
}
