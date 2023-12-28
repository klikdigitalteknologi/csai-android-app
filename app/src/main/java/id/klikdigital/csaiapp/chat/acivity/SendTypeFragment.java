package id.klikdigital.csaiapp.chat.acivity;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.response.SendImageResponse;
import id.klikdigital.csaiapp.config.ConfigPrivate;
import id.klikdigital.csaiapp.fragment.ChatRoomFragment;
import id.klikdigital.csaiapp.session.SessionManage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendTypeFragment extends Fragment {

    private ImageView imageSender,bck;
    private String gambar,whatsapp,sender,message,nama;
    private EditText chat;
    private AppCompatImageView btnSend;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_type, container, false);
        imageSender =view.findViewById(R.id.terimagambar);
        bck =view.findViewById(R.id.backtoChatroom);
        chat = view.findViewById(R.id.chatEditext);
        btnSend = view.findViewById(R.id.btnSendImage);
        SessionManage sessionManage = new SessionManage(getContext());
        sender = sessionManage.getUserData().getMemberKode();
       Bundle bundle = getArguments();
       gambar = bundle.getString("image");
       String no = bundle.getString("wa");
       nama = bundle.getString("nama");

        Log.d("RESPONSE GAMBAR","msg:" +gambar);
        Log.d("RESPONSE NOMOR DAN NAMA ","msg:" +no+nama);

       whatsapp = Arrays.toString(no.split("@"));
        if (gambar != null){
//            File file = new File(gambar);
//            Log.d("JALUR GAMBAR", "msg: " + file.getAbsolutePath());

//            Uri uri = Uri.fromFile(file);
//            imageSender.setImageURI(uri);
            Picasso.get().load(gambar).into(imageSender);
           imageSender.setVisibility(View.VISIBLE);
       }else {
           Toast.makeText(getContext(),"GAMBAR KOSONG",Toast.LENGTH_SHORT).show();
       }
        bck.setOnClickListener(v -> {
            backtoChatRoom();
        });
        btnSend.setOnClickListener(view1 -> {
            message = chat.toString();
            sendType();
        });
        return view;
    }
    private void sendType() {
        String aa = "/" + gambar;
        RequestBody senderBody = RequestBody.create(MediaType.parse("text/plain"), sender);
        RequestBody waBody = RequestBody.create(MediaType.parse("text/plain"), whatsapp);
        RequestBody pesanBody = RequestBody.create(MediaType.parse("text/plain"), message);
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), gambar);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file",gambar, imageBody);
        ChatService chatService = ConfigPrivate.htppclient().create(ChatService.class);
        Call<SendImageResponse> call = chatService.sendImage(senderBody,waBody,imagePart,pesanBody);
        call.enqueue(new Callback<SendImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendImageResponse> call, @NonNull Response<SendImageResponse> response) {
                if (response.isSuccessful()){
                    if (getActivity() instanceof FragmentActivity){
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("nomor",whatsapp);
                        bundle.putString("nama", nama);
                        chatRoomFragment.setArguments(bundle);
                        transaction.replace(R.id.navHostFragment, chatRoomFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Toast.makeText(getContext(),"GAMBAR SEDANG DI KIRIM...",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(),"GAGAL PINDAH LAYOUT",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SendImageResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void backtoChatRoom() {
    }
}