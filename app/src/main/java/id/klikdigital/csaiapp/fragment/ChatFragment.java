package id.klikdigital.csaiapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.adapter.CustomListAdapter;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.model.ChatModel;
import id.klikdigital.csaiapp.chat.model.NotifModel;
import id.klikdigital.csaiapp.chat.response.ChatResponse;
import id.klikdigital.csaiapp.chat.response.NotifResponse;
import id.klikdigital.csaiapp.chat.response.SendChatResponse;
import id.klikdigital.csaiapp.chat.response.SendImageResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.config.ConfigPrivate;
import id.klikdigital.csaiapp.contact.interfaces.ContactService;
import id.klikdigital.csaiapp.contact.models.ContactModels;
import id.klikdigital.csaiapp.contact.response.ContactResponse;
import id.klikdigital.csaiapp.home.Home;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.session.SessionManage;
import id.klikdigital.csaiapp.util.RealPathUtility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ChatFragment extends Fragment {

    private MaterialDialogs materialDialogs;
    private Uri uri;
    private  String filePath;
    private Context context;
    private Dialog dialog;
    private Pusher pusher;
    private ArrayList<ChatModel> chatModels;
    private String perangkat,member,session,message,imagePath,nomorwa,notujuan;
    private final String getWhatsapp = "0";
    private CustomListAdapter adapter;
    private int limit;
    private View view;
    private AppCompatButton btn_chat_new;
    private String whatsapp;
    private int pengirim;
    private FloatingActionButton fab;
    private ActivityResultLauncher<String>launcher;
    String[] count = {"All Contact","10","25","50","100"};
    String [] jenis = {"--pilih jenis--","text","image"};
    private List<String>dataContact = new ArrayList<>();
    private Spinner spinner,spinercontact,spinnerJenis;
    private SwipeRefreshLayout swipeRefreshLayout;
   private String app_id = "1578008";
    private String key = "560f792226b7069d0cd9";
   private String secret = "e8b3af6069e8cc5db7fb";
   private ListView listView;
   private String cluster = "ap1";
   private EditText nomor;
   private ImageView img;
   private HashMap<String, String> contactMap = new HashMap<>();
   final int PICK_IMAGE = 1;
   ProgressDialog pd;



    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        if (getActivity() instanceof Home) {
            Home mainActivity = (Home) getActivity();
            if (mainActivity != null) {
                mainActivity.showToolbar();
            }
        }
        context = getActivity();
        fab = view.findViewById(R.id.fab);
        listView = view.findViewById(R.id.listView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        SessionManage sessionManage = SessionManage.getInstance(getActivity());
        pengirim = Integer.parseInt(sessionManage.getUserData().getMemberKode());
        UserItem userItem = sessionManage.getUserData();
        perangkat = userItem.getMemberKode();
        member = userItem.getMemberKode();
        session = userItem.getKdUser();
        limit =0;
        chatModels = new ArrayList<>();
        adapter = new CustomListAdapter(getActivity(),new ArrayList<>());
        btn_chat_new = view.findViewById(R.id.btn_new_chat);
//        spinner = view.findViewById(R.id.spinner_chat_list);
        pd = new ProgressDialog(getContext());

        dialog = new Dialog(getContext());
        //start dropdown
//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, count);
//        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(stringArrayAdapter);


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Log.d("DEBUG","URI"+result);
            if (result != null) {
                try {
                    img.setImageURI(result);
                    img.setVisibility(View.VISIBLE);
//                    filePath = RealPathUtility.getRealPath(getContext(), result);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "GAGAL" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Gambar kosong", Toast.LENGTH_SHORT).show();
            }
        });

        //end dropdown
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            ChatModel chatModel = chatModels.get(i);
            String nomor = chatModel.getNomorWhatsapp();
            String nama = chatModel.getNama();
            if (getContext() instanceof FragmentActivity) {
                pusher.disconnect();
                    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
                    Bundle bundle = new Bundle();
//                    String wa = Arrays.toString(nomor.split("@"));
                    bundle.putString("nomor",nomor);
                    bundle.putString("nama", nama);
                    chatRoomFragment.setArguments(bundle);
                    transaction.replace(R.id.navHostFragment, chatRoomFragment);

                    transaction.addToBackStack(null);
                    transaction.commit();
                }else {
                    Toast.makeText(getContext(),"GAGAL MENUTUP TOOLBAR",Toast.LENGTH_SHORT).show();
                }
        });
        getDataFromServer();
//        getDataFromPusher();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getDataFromServer();
            swipeRefreshLayout.setRefreshing(false);
        });
        fab.setOnClickListener(view ->{
            dialog.setContentView(R.layout.send_new_chat);
            getContact();
            spinercontact = dialog.findViewById(R.id.spinnerKontak);
            Spinner spiner = dialog.findViewById(R.id.spinnerJeniss);
             nomor = dialog.findViewById(R.id.textNomorWa);
            EditText pesan = dialog.findViewById(R.id.eTextPesan);
            Button btnSend = dialog.findViewById(R.id.btnSendNewChat);
            Button btnOpen = dialog.findViewById(R.id.btnOpenFolder);
            ImageView keluar = dialog.findViewById(R.id.iconkeluaraja);
             img  = dialog.findViewById(R.id.imgSendNewChat);
            img.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,jenis);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiner.setAdapter(adapter1);
            spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                    if (item.equals("text")) {
                        Log.d("RESPONSE","JENIS: "+item);
                        btnOpen.setVisibility(View.GONE);
                        img.setVisibility(View.GONE);
                    } else if (item.equals("image")){
                        btnOpen.setVisibility(View.VISIBLE);
                    }else {
                        Log.d("RESPONSE","JENIS: "+item);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("RESPONSE","JENIS: "+parent.getItemAtPosition(0).toString());
                }
            });
            spinercontact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedNamaLengkap = dataContact.get(position);
                    String selectedNomor = contactMap.get(selectedNamaLengkap);
                    String[] data = selectedNomor.split("@");
                    nomor.setText(data[0]);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("RESPONSE","JENIS: "+parent.getItemAtPosition(0).toString());
                }
            });


            btnSend.setOnClickListener(view1 -> {
                message = pesan.getText().toString().trim();
                nomorwa = nomor.getText().toString().trim();
               if (uri == null){
                   Log.d("RESPONSE","imageURl "+uri);
                   if (message.equals("")){
                       pesan.setError("Pesan Wajib di isi");
                   }else {
                       sendMessage(message,nomorwa);
                   }
               }else {
                   Log.d("RESPONSE","image: "+uri);
                   sendTypeImage(uri);
                   Toast.makeText(getContext(),"Pesan Sedang Diikirim",Toast.LENGTH_SHORT).show();
               }
               dialog.dismiss();
            });
            btnOpen.setOnClickListener(view1 -> {
//                launcher.launch("image/*");
//                launcher.launch(new PickVisualMediaRequest.Builder()
//                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
//                        .build().toString());
                ImagePicker
                        .with(this)
                        .galleryOnly()
                        .crop()
                        .compress(512)
                        .maxResultSize(512, 512)
                        .start(PICK_IMAGE);
            });
            keluar.setOnClickListener(view1 -> dialog.dismiss());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            dialog.getWindow().setLayout((6 * width) / 7, (6 * height) / 7);
            dialog.show();
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        });
        return view;
    }

    private File createFileFromInputStream(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("temp_image", ".jpg", requireActivity().getCacheDir());
        // Salin data dari InputStream ke file sementara
        OutputStream outputStream =new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();

        return tempFile;
    }

    private void getContact() {
        ContactService contactService = Config.htppclient().create(ContactService.class);
        Call<ContactResponse> contactResponseCall = contactService.getContact(member,getWhatsapp);
        contactResponseCall.enqueue(new Callback<ContactResponse>() {
            @Override
            public void onResponse(@NonNull Call<ContactResponse> call, @NonNull Response<ContactResponse> response) {
                if (response.isSuccessful()) {
                    ContactResponse contactResponse = response.body();
                    if (contactResponse != null && contactResponse.isStatus() && contactResponse.getData() != null) {
                        dataContact.clear();
                        for (ContactModels contact : contactResponse.getData()) {
                            dataContact.add(contact.getNamaLengkap());
                            Log.d("DATA KONTAK","MSG:"+dataContact);
                            contactMap.put(contact.getNamaLengkap(), contact.getWhatsapp());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,dataContact);
                        spinercontact.setAdapter(adapter);
                        Log.d("RESPONSE","DATA: "+dataContact);
                    }
                }else {
                    Log.d("RESPONSE SERVER",response.toString());
                    Toast.makeText(getContext(),"SERVER NOT RESPONDING",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ContactResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTypeImage(Uri filePath) {
        try {
            File file = new File(filePath.getPath());
            Log.d("URI RESPONSE","URI: "+file);
            RequestBody senderBody = RequestBody.create(MediaType.parse("text/plain"),member);
            RequestBody waBody = RequestBody.create(MediaType.parse("text/plain"), nomorwa);
            RequestBody pesanBody = RequestBody.create(MediaType.parse("text/plain"), message);
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), imageBody);
            ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
            Call<SendImageResponse> chatResponseCall = chatService.sendImage(senderBody,waBody,imagePart,pesanBody);
            chatResponseCall.enqueue(new Callback<SendImageResponse>() {
                @Override
                public void onResponse(@NonNull Call<SendImageResponse> call, @NonNull Response<SendImageResponse> response) {
                    if (response.isSuccessful()) {
                        SendImageResponse sendImageResponse = response.body();
                        if (sendImageResponse != null && sendImageResponse.isStatus()) {
                            Toast.makeText(getContext(), "Pesan Berhasil Terkirim", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Pesan Gagal Terkirim", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("RESPONSE SERVER", response.toString());
                        Toast.makeText(getContext(), "SERVER NOT RESPONDING", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<SendImageResponse> call, @NonNull Throwable t) {
                    Toast.makeText(context, "SERVER ERROR"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE ERROR IMAGE",t.getMessage());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage(String pesan, String penerima) {
        try {
            Log.d("PENGIRIM",member);
            Log.d("PESAN", this.message);
            Log.d("PENERIMA", this.nomorwa);
            ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
            Call<SendChatResponse> chatResponseCall = chatService.sendMessage(pengirim,penerima,pesan);
            chatResponseCall.enqueue(new Callback<SendChatResponse>() {
                @Override
                public void onResponse(@NonNull Call<SendChatResponse> call, @NonNull Response<SendChatResponse> response) {
                    if (response.isSuccessful()) {
                        SendChatResponse sendChatResponse = response.body();
                        if (sendChatResponse != null && sendChatResponse.isStatus()) {
                            Toast.makeText(getContext(), "Pesan Berhasil Terkirim", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Pesan Gagal Terkirim", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("RESPONSE SERVER", response.toString());
                        Toast.makeText(getContext(), "SERVER NOT RESPONDING", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<SendChatResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("PRINT ERROR",e.getMessage());
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataFromServer() {
        try {
            ChatService chatService = Config.htppclient().create(ChatService.class);
            Call<ChatResponse> chatResponseCall = chatService.getChatData(perangkat, member, session, limit);
            chatResponseCall.enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                    if (response.isSuccessful()) {
                        ChatResponse chatResponse = response.body();
                        if (chatResponse != null && chatResponse.isStatus() && chatResponse.getData() != null) {
                            chatModels = chatResponse.getData();
                            if (!chatModels.isEmpty()) {
                                whatsapp = chatModels.get(0).getNomorWhatsapp();
                            }
                            for (int i = 0; i < chatModels.size(); i++) {
                                ChatModel chatModel = chatModels.get(i);
                                chatModel.setDisplayMessage(chatModel.getShortMessage());
                            }
                            adapter.clear();
                            adapter.addAll(chatModels);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
//                        getCountNotif();
                        } else {
                            Log.d("response debug", "data kosong");
                        }
                    } else {
                        Toast.makeText(getContext(), "SERVER ERROR", Toast.LENGTH_SHORT).show();
                        Log.d("Response debug", "server not responding");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(),"ERROR"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void getCountNotif() {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<NotifResponse> call = chatService.getNotif(perangkat,member,session,limit);
        call.enqueue(new Callback<NotifResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotifResponse> call, @NonNull Response<NotifResponse> response) {
                Log.d("RESPONSE URL","msg:"+response.body());
                if (response.isSuccessful()) {
                    List<NotifModel> chatResponse = response.body().getData();
                    if (response.body().isStatus()){
                        for (int i = 0; i < chatResponse.size(); i++) {
                            NotifModel notifModel = chatResponse.get(i);
                            int count = notifModel.getNotif();
                            Log.d("NOTIF", "WD " + count);
                            // Temukan TextView di layout_item_chatlist.xml
                            TextView textCountMessage = view.findViewById(R.id.textCountMessage);
                            // Pastikan TextView tidak null sebelum memanggil setText
                            if (textCountMessage != null) {
                                // Set nilai notif ke dalam textCountMessage
                                textCountMessage.setText(String.valueOf(count));
                                // Tampilkan textCountMessage jika notif > 0
                                if (count > 0) {
                                    textCountMessage.setVisibility(View.VISIBLE);
                                } else {
                                    textCountMessage.setVisibility(View.GONE);
                                }
                            } else {
                                Log.e("ERROR", "textCountMessage is null");
                            }
                        }
                    }else {
                        Log.d("RESPONSE","BELUM ADA PESAN MASUK");
                    }

                } else {
//                    Toast.makeText(getContext(), "SERVER ERROR", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE CODE COUNT MESSAGE", "CODE \n" + response.code());
                }
            }
            @Override
            public void onFailure(Call<NotifResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataFromPusher() {
        PusherOptions options = new PusherOptions().setCluster(cluster);
         pusher = new Pusher(key, options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d("Pusher CHATLIST", "State: " + change.getCurrentState());
            }
            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("Pusher CHATLIST", "Error: " + message);
            }
        }, ConnectionState.ALL);
        pusher.connect();
        Channel channel = pusher.subscribe(member + "-messages");
        channel.bind(member, event -> {
            getDataFromServer();
        });
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        getDataFromPusher();
        super.onStart();
    }

    @Override
    public void onStop() {
        pusher.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (pusher !=null && pusher.getConnection() != null){
            pusher.disconnect();
        }
        super.onDestroy();
    }
    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        if (uri != null) {
            try {
                if (DocumentsContract.isDocumentUri(getContext(), uri)) {
                    // Jika URI adalah dokumen, gunakan DocumentFile untuk mendapatkan path
                    DocumentFile documentFile = DocumentFile.fromSingleUri(getContext(), uri);
                    filePath = documentFile.getUri().getPath();
                } else {
                    // Jika URI adalah media biasa, gunakan pendekatan lain
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        filePath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE){
                Uri dataImage = data.getData();
                getPathUtil(dataImage);
                img.setImageURI(dataImage);
                img.setVisibility(View.VISIBLE);
                this.uri = dataImage;
                Log.d("LOCATE","PATH:"+filePath);
            }
        }
    }

    private void getPathUtil(Uri uri) {
        String[] imageProjection = {MediaStore.Images.Media.DATA};
        @SuppressLint("Recycle")
        Cursor cursor = getContext().getContentResolver().query(uri, imageProjection, null, null, null);
        if (cursor != null) {
           cursor.moveToFirst();
           int indexImage = cursor.getColumnIndex(imageProjection[0]);
           filePath = cursor.getString(indexImage);
        }
    }
}

