package id.klikdigital.csaiapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chatBot.adapter.ChatBotTableDataAdapter;
import id.klikdigital.csaiapp.chatBot.interfaces.ChatBotService;
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;
import id.klikdigital.csaiapp.chatBot.response.ChatBotResponse;
import id.klikdigital.csaiapp.config.ChatRetrofit;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotFragment extends Fragment {
    private Spinner spinner1,spinner2;
    private String member,perangkat;
    private static final String autoreplay = "0";
    private SwipeRefreshLayout swipeRefreshLayout;
    private TableView<ChatModelBot> tableView;
    private List<ChatModelBot> data;
final Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_bot, container, false);
        tableView = view.findViewById(R.id.table_chatbott);
        //start spinner
        spinner1 = view.findViewById(R.id.spinner);
        spinner2 = view.findViewById(R.id.spinner2);
        String[] selectItem = {"All Contact","10","25","50","100"};
        String[] filter = {"filter","text","file"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, filter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(arrayAdapter);
        //end spinner

        //session
        member = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        perangkat = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        //end session
        getDataChatBot(); //get data from server
        //swipRefresh layout
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutChatbot);
        swipeRefreshLayout.setOnRefreshListener(()->{
            swipeRefreshLayout.setRefreshing(true);
            getDataChatBot(); //get data from server
            swipeRefreshLayout.setRefreshing(false);
        });
        //end of swiprefresh layout
        final int delay =1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataChatBot();
            }
        },delay);
        return view;
    }
    private void getDataChatBot() {
        ChatBotService chatBotService = Config.htppclient().create(ChatBotService.class);
        Call<ChatBotResponse>call = chatBotService.getChatbot(member,perangkat,autoreplay);
        call.enqueue(new Callback<ChatBotResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ChatBotResponse> call, @NonNull Response<ChatBotResponse> response) {
                Log.d("Response server", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    ChatBotResponse chatBotResponse = response.body();
                    if (chatBotResponse != null && chatBotResponse.isStatus()) {
                        data = chatBotResponse.getData();
                        getData(data);
                    }else {
                        Toast.makeText(getContext(),"not responding",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),response.message(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ChatBotResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(List<ChatModelBot> chatModelBotList) {
        String[] header = {"Keyword","Pesan","Type","aksi"};
        String[][] data = new String[chatModelBotList.size()][4];
        for (int i = 0; i < chatModelBotList.size(); i++){
            ChatModelBot chatModelBot = chatModelBotList.get(i);
            data[i][0] = chatModelBot.getKeyword();
            data[i][1] = chatModelBot.getPesan();
            data[i][2] = chatModelBot.getType();
        }
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),header));
        tableView.setDataAdapter(new ChatBotTableDataAdapter(getContext(),chatModelBotList));
    }
    private void setHandle(){
    }
    }
