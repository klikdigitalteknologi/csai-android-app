package id.klikdigital.csaiapp.fragment;

import static com.amulyakhare.textdrawable.TextDrawable.SHAPE_ROUND;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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
import android.widget.VideoView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.adapter.ChatAdapter;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.model.ChatModelPrivate;
import id.klikdigital.csaiapp.chat.response.ChatRoomResponse;
import id.klikdigital.csaiapp.chat.response.ResponseDto;
import id.klikdigital.csaiapp.chat.response.SendChatResponse;
import id.klikdigital.csaiapp.chat.response.SendImageResponse;
import id.klikdigital.csaiapp.chat.response.SendVideoResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.config.ConfigPrivate;
import id.klikdigital.csaiapp.home.Home;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.quickreplay.interfaces.QuickReplayService;
import id.klikdigital.csaiapp.quickreplay.models.QuickReplayModels;
import id.klikdigital.csaiapp.quickreplay.response.QuickReplayResponse;
import id.klikdigital.csaiapp.session.SessionManage;
import id.klikdigital.csaiapp.util.RealPathUtility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomFragment extends Fragment {

    private String perangkat,member,whatsapp,session,message,path,pengguna,client,replayfast,selectPath;
    private String[] wa;
    private Pusher pusher;
    private static final String APP_ID="1578008";
    private static final String KEY ="560f792226b7069d0cd9";
    private static final String SECRET = "e8b3af6069e8cc5db7fb";
    private static final String CLUSTER ="ap1";
    private static final int SELECTED_VIDEO = 1;
    private static final int SELECTED_IMAGE = 2;
    private static final int SELECTED_FILE = 3;
    private static final int SELECTED_AUDIO = 4;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ChatAdapter chatAdapter;
    private List<ChatModelPrivate> chatList;
    private List<ChatModelPrivate> list;
    private LinearLayoutManager linearLayoutManager;
    private EditText emessage;
    private TextView user;
    private Handler handler;
    private File file;
    private int page,limit,sender;
    private ImageView kirimgambar,btnquick,imageProfileChat,emojiBtn,showImageSender,btnshowcard,btnOpenImage,btnOpenFile,btnOpenAudio,btnOpenVideo;
    private AppCompatImageView btnsend;
    private CardView cardView;
    private Spinner spinner;
    private boolean isSelectedItem = false;
    private List<String> replayOptions = new ArrayList<>();
    private FrameLayout frameLayout;
    private ConstraintLayout layout,lc;
    private BottomSheetDialog bottomSheetDialog;
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
        lc = view.findViewById(R.id.constraintoid);
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
        list = new ArrayList<>();
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
        whatsapp = bundle.getString("nomor");
//        whatsapp = Arrays.toString(a.split("@"));
        wa = whatsapp.split("@");
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
//            setChatToPusher();
        } else {
            Toast.makeText(getContext(), "Nomor WhatsApp tidak valid", Toast.LENGTH_SHORT).show();
        }


        emojiBtn.setOnClickListener(v -> {

        });

        btnsend.setOnClickListener(v -> {
            message = emessage.getText().toString().trim();
            Log.d("RESPONSE ANJAY","pesan: " +message );
            Log.d("RESPONSE NOMOR", "nomor: " + whatsapp );
            Log.d("RESPONSE PENGIRIM", "kd: " + perangkat);
          sendMessage();

        });
        lc.setOnClickListener(v->cardView.setVisibility(View.INVISIBLE));
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
                        readChat();
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
        Call<ChatRoomResponse>call = chatService.readChat(perangkat,wa[0]);
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
//                            chatList = chatRoomResponse.getData();
                            chatAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(chatAdapter);
                            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                            Log.d("RESPONSE NEW CHAT","msg:"+newChatlist.get(0).getText());
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
        String[] nomor =(whatsapp.split("@"));
        Log.d("RESPONSE NOMORR SEND","MSG:"+nomor[0]);
        ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
        Call<SendChatResponse>call = chatService.sendMessage(sender,nomor[0],message);
        call.enqueue(new Callback<SendChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendChatResponse> call, @NonNull Response<SendChatResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().isStatus()) {
                        emessage.setText("");
//                        setChatToPusher();
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
    @SuppressLint("StaticFieldLeak")
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
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    newChat();
                    return null;
                }
            }.execute();
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
            startActivityForResult(Intent.createChooser(intent, "Select Image"),SELECTED_IMAGE);
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
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECTED_FILE);
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private void showAudioFromStorage() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
//            String[] mimeTypes = {"audio/mpeg"};
            intent.setType("audio/*");
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Audio"),SELECTED_AUDIO);
        }else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private void showVideoFromStorage() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"),SELECTED_VIDEO);
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
        if (resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            switch (requestCode){
                case SELECTED_VIDEO:
                   Uri uriVideo = data.getData();
                   openDropDownVideo(uriVideo);
                    break;
                case SELECTED_AUDIO:
                    openDropDownAudio(uri);
                    break;
                case SELECTED_FILE:
                    openDropDownFile(uri);
                    break;
                case SELECTED_IMAGE:
                    openDropdown(uri);
                    break;
                default:
                    break;
            }
        }
    }

    private void openDropDownFile(Uri uri) {
    }
    private void openDropDownAudio(Uri uri) {
    }
    private void openDropDownVideo(Uri video) {
        try {
            if (video != null) {
                selectPath = getPath(video);
//                String select = RealPathUtility.getRealPath(getContext(),video);
                Log.d("RESPONSE VIDEO","msg:" +video);
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.send_video);
                ImageView imageView = dialog.findViewById(R.id.keluarImage);
                VideoView videoView = dialog.findViewById(R.id.video);
                EditText editText = dialog.findViewById(R.id.eVideo);
                AppCompatImageView btnSend = dialog.findViewById(R.id.btnSendImage);
                imageView.setOnClickListener(v -> dialog.dismiss());
                btnSend.setOnClickListener(v -> {
                    uploadVideoToServer(editText,selectPath);
                    dialog.dismiss();
                });
                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout((int) (width*.9), (int) (height*.9));
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(video);
                videoView.start();
                dialog.show();
            }else {
                Toast.makeText(getContext(),"video kosong",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(),"GAGAL mengambil video"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void openDropdown(Uri uri) {

        String imagePath = RealPathUtility.getRealPath(getContext(), uri);
      Log.d("RESPONSE",imagePath);
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.send_image);
        ImageView imageView = dialog.findViewById(R.id.imageKeluar);
        ImageView img = dialog.findViewById(R.id.gambarkirim);
        EditText editText = dialog.findViewById(R.id.eImage);
        AppCompatImageView btnsend = dialog.findViewById(R.id.btnSendImage);
        imageView.setOnClickListener(v -> dialog.dismiss());
        btnsend.setOnClickListener(view -> {
            sendImageToServer(editText,imagePath);
            dialog.dismiss();
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((int) (width*.9), (int) (height*.9));
        img.setVisibility(View.VISIBLE);
        Picasso.get().load(uri).into(img);
        dialog.show();
    }

    public String getPath(Uri uri) {
        Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = requireActivity().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

    private void sendImageToServer(EditText editText, String imagePath) {
        message = editText.getText().toString();
        File data = new File(imagePath);
        RequestBody senderBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sender));
        RequestBody waBody = RequestBody.create(MediaType.parse("text/plain"), wa[0]);
        RequestBody pesanBody = RequestBody.create(MediaType.parse("text/plain"), message);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), data);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",data.getName(), imageBody);
        ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
        Call<SendImageResponse>call = chatService.sendImage(senderBody,waBody,imagePart,pesanBody);
        call.enqueue(new Callback<SendImageResponse>() {
            @Override
            public void onResponse(Call<SendImageResponse> call, Response<SendImageResponse> response) {

                if (response.isSuccessful()){
                    Toast.makeText(getContext(),"Image Sedang di kirim..",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Gagal Mengirim Image",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendImageResponse> call, Throwable t) {

            }
        });
    }
    private void uploadVideoToServer(EditText editText, String uri) {
        message = editText.getText().toString();
        File file1 = new File(uri);
        Log.d("PATH",selectPath);
        RequestBody senderBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sender));
        RequestBody waBody = RequestBody.create(MediaType.parse("text/plain"), wa[0]);
        RequestBody pesanBody = RequestBody.create(MediaType.parse("text/plain"), message);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), file1);
        MultipartBody.Part videPart = MultipartBody.Part.createFormData("video",file1.getName(), videoBody);
        ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
        Call<SendVideoResponse>call = chatService.sendVideo(senderBody,waBody,pesanBody,videPart);
        call.enqueue(new Callback<SendVideoResponse>() {
            @Override
            public void onResponse(Call<SendVideoResponse> call, Response<SendVideoResponse> response) {

                if (response.isSuccessful()){
                    Toast.makeText(getContext(),"Video Sedang di kirim..",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Gagal Mengirim Video",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SendVideoResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Gagal Mengirim Video"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        pusher.disconnect();
        super.onStop();
    }

    @Override
    public void onStart() {
        setChatToPusher();
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (pusher !=null && pusher.getConnection() != null){
            pusher.disconnect();
        }
        super.onDestroy();
    }
}
