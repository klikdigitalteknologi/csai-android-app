package id.klikdigital.csaiapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.acivity.ChatRoom;
import id.klikdigital.csaiapp.chat.adapter.CustomListAdapter;
import id.klikdigital.csaiapp.chat.interfaces.ChatService;
import id.klikdigital.csaiapp.chat.model.ChatModel;
import id.klikdigital.csaiapp.chat.response.ChatResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private Context context;
    private ArrayList<ChatModel> chatModels;
    private String perangkat,member,session;
    private CustomListAdapter adapter;
    private int limit;
    private View view;
    private AppCompatButton btn_chat_new;
    private String whatsapp;
    String[] count = {"All Contact","10","25","50","100"};
    private Spinner spinner;
    private SwipeRefreshLayout swipeRefreshLayout;
    final Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        context = getActivity();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        SessionManage sessionManage = SessionManage.getInstance(getActivity());
        UserItem userItem = sessionManage.getUserData();
        perangkat = userItem.getMemberKode();
        member = userItem.getMemberKode();
        session = userItem.getKdUser();
        limit =0;
        adapter = new CustomListAdapter(getActivity(),new ArrayList<>());
        btn_chat_new = view.findViewById(R.id.btn_new_chat);
        spinner = view.findViewById(R.id.spinner_chat_list);
        //start dropdown
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, count);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);
     //end dropdown
        ListView listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            ChatModel chatModel = chatModels.get(i);
            Intent intent = new Intent(getActivity(), ChatRoom.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("nomor_wa",chatModel.getNomorWhatsapp());
            intent.putExtra("nama_user",chatModel.getNama());
            startActivity(intent);
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getDataFromServer(listView);
            swipeRefreshLayout.setRefreshing(false);
        });
        final int delay = 1000; // milidetik

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               getDataFromServer(listView);
                handler.postDelayed(this, delay);
            }
        }, delay);
        btn_chat_new.setOnClickListener(view ->
                Toast.makeText(context,"ini adalah button chat new",Toast.LENGTH_SHORT).show());
        return view;
    }
    private void getDataFromServer(ListView listView) {
        ChatService chatService = Config.htppclient().create(ChatService.class);
        Call<ChatResponse>chatResponseCall = chatService.getChatData(perangkat,member,session,limit);
        chatResponseCall.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                if (response.isSuccessful()){
                    ChatResponse chatResponse = response.body();
                    if (chatResponse != null && chatResponse.isStatus() && chatResponse.getData() != null){
                        chatModels = chatResponse.getData();
                        if (!chatModels.isEmpty()){
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
                    } else {
                        Log.d("response debug","data kosong");
                    }
                }else {
                    Log.d("Response debug","server not responding");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}