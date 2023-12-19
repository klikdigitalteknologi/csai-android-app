package id.klikdigital.csaiapp.chatBot.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chatBot.interfaces.ChatBotService;
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;
import id.klikdigital.csaiapp.chatBot.response.ChatBotResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailChatBotFragment extends Fragment {
    private EditText keyword,pesan,type;
    private String kdReplay,member,perangkat,key,message,jenis,url,baseUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_chat_bot, container, false);
        url = "https://engine.csai.id/file/";
        keyword = view.findViewById(R.id.textKey);
        pesan = view.findViewById(R.id.textPesan);
        type =view.findViewById(R.id.texttypee);
    kdReplay  =   getArguments().getString("kd_autoreplay");
    Log.d("Data kd","data"+ kdReplay);
     member = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
     perangkat = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
   Log.d("Data member","data"+ member);
        Log.d("Data perangkat","data"+ perangkat);
        keyword.setEnabled(false);
        pesan.setEnabled(false);
        type.setEnabled(false);
        getData();
        return view;
    }

    private void getData() {
        ChatBotService chatBotService = Config.htppclient().create(ChatBotService.class);
        Call<ChatBotResponse>call = chatBotService.getChatbot(member,perangkat,kdReplay);
        call.enqueue(new Callback<ChatBotResponse>() {
            @Override
            public void onResponse(Call<ChatBotResponse> call, retrofit2.Response<ChatBotResponse> response) {
                Log.d("Data response","data"+ response.code());

              ChatBotResponse chatBotResponse = response.body();
              boolean status = chatBotResponse.isStatus();
              if (status){
                  List<ChatModelBot>chatModelBotList = chatBotResponse.getData();
                  for (int i = 0; i< chatModelBotList.size(); i++){
                      ChatModelBot chatModelBot = chatModelBotList.get(i);
                      key = chatModelBot.getKeyword();
                      message = chatModelBot.getPesan();
                      jenis = chatModelBot.getType();
                      Log.d("DATA KEY","DATA "+key);
                      Log.d("DATA MESSAGE","DATA "+message);
                      Log.d("DATA JENIS","DATA "+jenis);
                      keyword.setText(key);
                      pesan.setText(message);
                      type.setText(jenis);
                  }
              }else {
                  Toast.makeText(getContext(),chatBotResponse.getResponse(),Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onFailure(@NonNull Call<ChatBotResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}