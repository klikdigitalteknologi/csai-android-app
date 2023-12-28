package id.klikdigital.csaiapp.fragment;

import static com.amulyakhare.textdrawable.TextDrawable.SHAPE_ROUND;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import id.klikdigital.csaiapp.MainActivity;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.acivity.SendTypeFragment;
import id.klikdigital.csaiapp.chat.adapter.ChatAdapter;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.model.ChatModelPrivate;
import id.klikdigital.csaiapp.chat.response.ChatRoomResponse;
import id.klikdigital.csaiapp.chat.response.ResponseDto;
import id.klikdigital.csaiapp.chat.response.SendChatResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.config.ConfigPrivate;
import id.klikdigital.csaiapp.home.Home;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.quickreplay.interfaces.QuickReplayService;
import id.klikdigital.csaiapp.quickreplay.models.QuickReplayModels;
import id.klikdigital.csaiapp.quickreplay.response.QuickReplayResponse;
import id.klikdigital.csaiapp.session.SessionManage;
import id.klikdigital.csaiapp.util.RealPathUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomFragment extends Fragment {

    private String perangkat,member,whatsapp,session,message,path,pengguna,client,replayfast;
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
    private ImageView kirimgambar,btnquick,imageProfileChat,emojiBtn,showImageSender,btnshowcard,btnOpenImage,btnOpenFile,btnOpenAudio,btnOpenVideo;
    private AppCompatImageView btnsend;
    private CardView cardView;
    private Spinner spinner;
    private boolean isSelectedItem = false;
    private List<String> replayOptions = new ArrayList<>();
    private FrameLayout frameLayout;
    private ConstraintLayout layout;
    private ActivityResultLauncher<Intent> launcher;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
        if (getActivity() instanceof Home){
            Home mainActivity = (Home) getActivity();
            if (mainActivity != null) {
                mainActivity.hideToolbar();
            }
        }
        //start ImageView
        showImageSender =view.findViewById(R.id.imageSender);
        btnshowcard =view.findViewById(R.id.btnShowCardview);
        btnOpenImage = view.findViewById(R.id.btnfoto);
        btnOpenFile = view.findViewById(R.id.btnfile);
        btnOpenAudio = view.findViewById(R.id.btnaudio);
        btnOpenVideo = view.findViewById(R.id.btnvideo);
        btnsend = view.findViewById(R.id.btnsend);
        imageProfileChat = view.findViewById(R.id.imageProfileChat);
        emojiBtn = view.findViewById(R.id.btnEmoji);
        btnquick = view.findViewById(R.id.btnShowQuick);
        kirimgambar = view.findViewById(R.id.terimagambar);
        //End ImageView


        //Start Component
        progressBar = view.findViewById(R.id.pbChat);
        recyclerView = view.findViewById(R.id.r_chat);
        user = view.findViewById(R.id.textUserChat);
        emessage = view.findViewById(R.id.eChat);
        toolbar = view.findViewById(R.id.toolbaeer);
        cardView = view.findViewById(R.id.cardPopup);
        spinner = view.findViewById(R.id.spinner);
        frameLayout = view.findViewById(R.id.layoutframechatroom);
        //End Component

        linearLayoutManager = new LinearLayoutManager(getContext());
        layout = view.findViewById(R.id.nt);

        //set quickreplay recyleview
        //end set quickreplay
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity !=null){
            activity.setSupportActionBar(toolbar);
        }

        chatList = new ArrayList<>();
        SessionManage sessionManage = SessionManage.getInstance(getContext());
        UserItem userItem = sessionManage.getUserData();
        perangkat = userItem.getMemberKode();
        member = userItem.getMemberKode();
        session = userItem.getKdUser();
        sender = Integer.parseInt(userItem.getMemberKode());
        page = 1;
        limit = 10;
        replayfast = "0";
        Bundle bundle = getArguments();
        String a = bundle.getString("nomor");
        whatsapp = Arrays.toString(a.split("@"));
        pengguna = bundle.getString("nama");
        Log.d("WA","NOMOR "+ whatsapp);
        Log.d("NAMA","NAMA"+pengguna);
        int cut = 1;
        String cutName = pengguna.substring(0,cut);
        TextDrawable drawable = new  TextDrawable.Builder()
                .setColor(Color.rgb(88, 128, 192))
                .setRadius(2)
                .setBold()
                .setShape(SHAPE_ROUND)
                .setText(cutName)
                .build();
        imageProfileChat.setImageDrawable(drawable);
        user.setText(pengguna);

        if (whatsapp != null && !whatsapp.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            readChat();
            loadDataChat();
            setChatToPusher();
        } else {
            Toast.makeText(getContext(), "Nomor WhatsApp tidak valid", Toast.LENGTH_SHORT).show();
        }

        btnsend.setOnClickListener(v -> {
            message = emessage.getText().toString().trim();
            Log.d("RESPONSE ANJAY","pesan: " +message );
            Log.d("RESPONSE NOMOR", "nomor: " + whatsapp );
            Log.d("RESPONSE PENGIRIM", "kd: " + perangkat);
           sendMessage();
        });

        emojiBtn.setOnClickListener(v -> {

        });

        frameLayout.setOnClickListener(v -> cardView.setVisibility(View.INVISIBLE));
        btnshowcard.setOnClickListener(v -> {
            if (cardView.getVisibility() == View.INVISIBLE){
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.show_cardview);
                cardView.setAnimation(animation);
                cardView.setVisibility(View.VISIBLE);
            }else {
                cardView.setVisibility(View.INVISIBLE);
            }
        });
        btnOpenFile.setOnClickListener(v -> new  LoadFileTask().execute());
        btnOpenImage.setOnClickListener(v -> new LoadImageTask().execute());
        btnOpenAudio.setOnClickListener(v -> new LoadAudioTask().execute());
        btnOpenVideo.setOnClickListener(v -> new LoadVideTask().execute());

        btnquick.setOnClickListener(v -> {
            if (spinner.getVisibility() == View.VISIBLE) {
                spinner.setVisibility(View.INVISIBLE);
            } else {
                getDataReplay();
                spinner.performClick();// Fetch data for the Spinner
                spinner.setVisibility(View.VISIBLE);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner.setVisibility(View.INVISIBLE);
                message = spinner.getSelectedItem().toString();
                emessage.setText(message);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });
        return view;
    }



    //Start get Quickreplay
    private void getDataReplay() {
        QuickReplayService quickReplayService = Config.htppclient().create(QuickReplayService.class);
        Call<QuickReplayResponse>call = quickReplayService.getReplay(member,perangkat,replayfast);
        call.enqueue(new Callback<QuickReplayResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<QuickReplayResponse> call, @NonNull Response<QuickReplayResponse> response) {
                if (response.isSuccessful()){
                    QuickReplayResponse replayResponse = response.body();
                    if (replayResponse != null && replayResponse.isStatus() && replayResponse.getData() != null){
                        replayOptions.clear();
                        for (QuickReplayModels replayModels : replayResponse.getData()){
                            replayOptions.add(replayModels.getPesan());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, replayOptions);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }else {
                        Log.d("REPSONSE QUICK SERVER",response.toString());
                        Toast.makeText(getContext(),"GAGAL MENAMPILKAN DATA",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"SERVER NOT RESPONDING",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<QuickReplayResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    //end quickreplay

    //Start Load data ChatRoom
    private void loadDataChat() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<ChatRoomResponse> call = chatService.getChats(perangkat, whatsapp, page, limit);
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // End Load Data ChatRoom

    //Start void ReadChat
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
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    //End void ReadChat

    //Start void NewChat
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
    //End void Newchat

    //Start SendMessage
    private void sendMessage() {
        ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
        Call<SendChatResponse>call = chatService.sendMessage(sender,whatsapp,message);
        call.enqueue(new Callback<SendChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendChatResponse> call, @NonNull Response<SendChatResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().isStatus()) {
                        emessage.setText("");
                        setChatToPusher();
                    } else {
                        Toast.makeText(getContext(), "gagal terkirim", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"server error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<SendChatResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("RESPONSE ERROR", "msg:"+t.getMessage());
            }
        });
    }
    //End SendMEssage

    //Start Load Chat From Pusher
    private void setChatToPusher() {
        PusherOptions options = new PusherOptions().setCluster(CLUSTER);
        pusher = new Pusher(KEY, options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("Pusher CHATROOM", "State: " + change.getCurrentState());
            }
            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("Pusher CHATROOM", "Error: " + message);
            }
        }, ConnectionState.ALL);
        pusher.connect();
        Channel channel = pusher.subscribe(member + "-messages");
        channel.bind(member,event -> {
            newChat();
        });
    }
    //End Load Chat From Pusher

    //Start CloseChat
    private void closeChat() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<ResponseDto>call = chatService.closeChat(member,whatsapp,session);
        call.enqueue(new Callback<ResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDto> call, @NonNull Response<ResponseDto> response) {
                if (response.isSuccessful()){
                    startActivity(new Intent(getContext(),Home.class));
                            Toast.makeText(getContext(), "CLOSE CHAT SUKSES", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"SERVER NOT RESPONDING",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseDto> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    //End CloseChat

    //Start Void Send Type
    private void showImageFromStorage() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,10);
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private void showFileFromStorage() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            String[] mimeTypes = {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/pdf"};
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,10);
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private void showAudioFromStorage() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            String[] mimeTypes = {"audio/mpeg"};
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,10);
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private void showVideoFromStorage() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            String[] mimeTypes = {"video/mp4"};
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,10);
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    //End void SendType
    //End OFf Void

    //Start Superclass
    @SuppressLint("StaticFieldLeak")
    private class LoadImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            showImageFromStorage();
            return null;
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class LoadFileTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            showFileFromStorage();
            return null;
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class LoadAudioTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            showAudioFromStorage();
            return null;
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class LoadVideTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            showVideoFromStorage();
            return null;
        }
    }

    //End SuperClass


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.forward_chat, menu);
        inflater.inflate(R.menu.close_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.btnclosechat){
            closeChat();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();

//            File file = new File(getActivity().getExternalFilesDir(null), fileName);
//            File file = new File(String.valueOf(uri));
            if (getActivity() instanceof FragmentActivity){
                pusher.disconnect();
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("wa",whatsapp);
                bundle.putString("image", String.valueOf(uri));
                bundle.putString("nama",pengguna);
                SendTypeFragment sendTypeFragment = new SendTypeFragment();
                sendTypeFragment.setArguments(bundle);
                transaction.replace(R.id.navHostFragment, sendTypeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }


        }
    }
    @Override
    public void onDestroy() {
        if (pusher !=null && pusher.getConnection() != null){
            pusher.disconnect();
        }
        super.onDestroy();
    }
}
