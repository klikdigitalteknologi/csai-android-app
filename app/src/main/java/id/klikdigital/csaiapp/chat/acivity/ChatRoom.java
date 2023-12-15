package id.klikdigital.csaiapp.chat.acivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import id.klikdigital.csaiapp.MainActivity;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.adapter.ChatAdapter;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.model.ChatModelPrivate;
import id.klikdigital.csaiapp.chat.response.ChatRoomResponse;
import id.klikdigital.csaiapp.chat.response.PusherResponse;
import id.klikdigital.csaiapp.chat.response.SendChatResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.config.ConfigPrivate;
import id.klikdigital.csaiapp.home.Home;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.session.SessionManage;
import id.klikdigital.csaiapp.util.RealPathUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoom extends AppCompatActivity {
    private String perangkat,member,whatsapp,session,message,path,pengguna,client;
    private String[] wa;
    private Pusher pusher;
    private static final String APP_ID="1578008";
    private static final String KEY ="560f792226b7069d0cd9";
    private static final String SECRET = "e8b3af6069e8cc5db7fb";
    private static final String CLUSTER ="ap1";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ChatAdapter chatAdapter;
    private List<ChatModelPrivate> chatList;
    private LinearLayoutManager linearLayoutManager;
    private EditText emessage;
    private TextView user;
    private Handler handler;
    private int page,limit,sender;
    private ImageView showImageSender;
    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        showImageSender = findViewById(R.id.imageSender);
        progressBar = findViewById(R.id.pbChat);
        recyclerView = findViewById(R.id.r_chat);
        user = findViewById(R.id.textUserChat);
        emessage = findViewById(R.id.eChat);
         toolbar = findViewById(R.id.toolbaeer);
        ImageView emojiBtn = findViewById(R.id.btnEmoji);
        setSupportActionBar(toolbar);
        AppCompatImageView btnsend = findViewById(R.id.btnsend);
        chatList = new ArrayList<>();
        SessionManage sessionManage = SessionManage.getInstance(this);
        UserItem userItem = sessionManage.getUserData();
        perangkat = userItem.getMemberKode();
        member = userItem.getMemberKode();
        session = userItem.getKdUser();
        sender = Integer.parseInt(userItem.getMemberKode());
        page = 1;
        limit = 10;
        linearLayoutManager = new LinearLayoutManager(this);
        Intent intent = getIntent();
//        client = intent.getStringExtra("nomor_wa");
//        wa = client.split("@");
//        whatsapp = wa[0].trim();
        whatsapp =  intent.getStringExtra("nomor_wa");
//        pengguna = intent.getStringExtra("nama_user");
//        Log.d("NAMA USER",pengguna);
//       user.setText(pengguna);
        Log.d("nomor WA","dpe nomor: " + whatsapp);
        if (whatsapp != null && !whatsapp.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
           readChat();
           loadDataChat();
        } else {
            Toast.makeText(getApplicationContext(), "Nomor WhatsApp tidak valid", Toast.LENGTH_SHORT).show();
        }
        btnsend.setOnClickListener(view -> {
//                ((Button)view).setCompoundDrawables(attahcment,null,attahcment,null);
            message = emessage.getText().toString().trim();
            Log.d("RESPONSE ANJAY","pesan: " +message );
            Log.d("RESPONSE NOMOR", "nomor: " + whatsapp );
            Log.d("RESPONSE PENGIRIM", "kd: " + perangkat);
            if (message.equals("")){
                emessage.setError("pesan wajib di isi");
            }else {
                sendMessage();
            }
        });
        emojiBtn.setOnClickListener(view -> {
        });
    }
    //func read chat
    private void readChat() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<ChatRoomResponse>call = chatService.readChat(perangkat,whatsapp);
        call.enqueue(new Callback<ChatRoomResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatRoomResponse> call, @NonNull Response<ChatRoomResponse> response) {
                if (response.isSuccessful()){
                    Log.d("RESPONSE","CHAT suskes di read");
                } else {
                    Log.d("RESPONSE","GAGAL READ");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ChatRoomResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    //end read chat

    //function load data chat
    private void loadDataChat() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<ChatRoomResponse> call = chatService.getChats(perangkat, member, whatsapp, session, page, limit);
        call.enqueue(new Callback<ChatRoomResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ChatRoomResponse> call, @NonNull Response<ChatRoomResponse> response) {
                if (response.isSuccessful()) {
                    ChatRoomResponse chatRoomResponse = response.body();
                    if (chatRoomResponse != null && chatRoomResponse.isStatus() && chatRoomResponse.getData() != null) {
                        chatList.addAll(chatRoomResponse.getData());
                        Collections.reverse(chatList);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        // Check if the adapter is already initialized
                        if (chatAdapter == null) {
                            chatAdapter = new ChatAdapter(chatList);
                            recyclerView.setAdapter(chatAdapter);
                            chatAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        }
//                        readChat();
                        setChatToPusher();
                    } else {
                        Log.d("RESPONSE DATA", "KOSONGG");
                    }
                } else {
                    Log.d("RESPONSE SERVER", "ERROR REQUEST TIMEOUT");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ChatRoomResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    // end load chat

    //start new chat
    private void newChat() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<ChatRoomResponse> callChat = chatService.newChat(perangkat, member, whatsapp, session);
        callChat.enqueue(new Callback<ChatRoomResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ChatRoomResponse> call, @NonNull Response<ChatRoomResponse> response) {
                if (response.isSuccessful()) {
                    ChatRoomResponse chatRoomResponse = response.body();
                    if (chatRoomResponse != null && chatRoomResponse.isStatus() && chatRoomResponse.getData() != null) {
                        List<ChatModelPrivate> newChatlist = chatRoomResponse.getData();
                      chatList.addAll(newChatlist);
                      chatList = chatRoomResponse.getData();
                      chatAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(chatAdapter);
                        recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        readChat();
                    } else {
                        Log.d("RESPONSE DATA", "TIDAK CHAT BARU");
                    }
                } else {
                    Log.d("RESPONSE SERVER", "ERROR REQUEST TIMEOUT");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ChatRoomResponse> call, @NonNull Throwable t) {
                Log.d("RESPONSE", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
    //end new chat

    //send message
    private void sendMessage() {
        ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
        Call<SendChatResponse>call = chatService.sendMessage(sender,whatsapp,message);
        call.enqueue(new Callback<SendChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendChatResponse> call, @NonNull Response<SendChatResponse> response) {
                if ( response.body() != null && response.body().isStatus()){
                    emessage.setText("");
                    setChatToPusher();
                } else {
                    Toast.makeText(getApplicationContext(),"gagal terkirim",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<SendChatResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
//end send message
    @SuppressLint("NotifyDataSetChanged")
    private void setChatToPusher() {
            PusherOptions options = new PusherOptions().setCluster(CLUSTER);
             pusher = new Pusher(KEY, options);
            pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("Pusher", "State: " + change.getCurrentState());
            }
            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("Pusher", "Error: " + message);
            }
        }, ConnectionState.ALL);
                pusher.connect();
                Channel channel = pusher.subscribe(member + "-messages");
                channel.bind(member,event -> {
                    newChat();
//                    try {
//           JSONObject jsonObject = new JSONObject(event.getData());
//           Gson gson = new Gson();
//           PusherResponse pusherResponse = gson.fromJson(jsonObject.toString(),PusherResponse.class);
//        String data = pusherResponse.getNomorWhatsapp();
//        String chat = pusherResponse.getPesan();
//        String[] type = data.split(",");
//        String datetime = pusherResponse.getDatetime();
//        String[] waktu = datetime.split(",");
//        String jam = waktu[1].trim();
//        String nomor = type[0].trim();
//        String jenis = type[1].trim();
//       if (Objects.equals(chat, "image")){
//         newChat();
//       } else if (Objects.equals(chat, "document")) {
//           newChat();
//       } else if (Objects.equals(chat, "audio")) {
//           newChat();
//       } else if (Objects.equals(chat, "video")) {
//           newChat();
//       }else {
//           ChatModelPrivate chatModelPrivate = new ChatModelPrivate();
//           chatModelPrivate.setText(chat);
//           chatModelPrivate.setJenis(jenis);
//           chatModelPrivate.setTime(jam);
//           runOnUiThread(() -> {
//                   chatList.add(chatModelPrivate);
//                   chatAdapter.notifyDataSetChanged();
//                   recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
//           });
//       }
//        } catch (JSONException e) {
//        throw new RuntimeException(e);
//        }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attachment_menu,menu);
        getMenuInflater().inflate(R.menu.forward_menu,menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
         startActivity(new Intent(getApplicationContext(), Home.class));
         finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    //item send file/image/video
    public void showAttachmentOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.eChat));
        popupMenu.getMenuInflater().inflate(R.menu.attachment_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuFile){
                return true;
            } else if (itemId == R.id.menuImage) {
                showStorage();
                return true;
            } else if (itemId == R.id.menuVideo){
                return true;
            } else return itemId == R.id.menuAudio;
        });
        popupMenu.show();
    }
    private void showStorage() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,10);
        }else {
            ActivityCompat.requestPermissions(ChatRoom.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Context context = ChatRoom.this;
            path = RealPathUtility.getRealPath(context,uri);
            Intent sendImageToForm = new Intent(ChatRoom.this, MainActivity.class);
            sendImageToForm.putExtra("imagePath",path);
            sendImageToForm.putExtra("whatsapp",whatsapp);
            sendImageToForm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sendImageToForm);
        } else {
            showImageSender.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        if (pusher !=null && pusher.getConnection() != null){
            pusher.disconnect();
        }
        super.onDestroy();

    }
}