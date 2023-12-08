package id.klikdigital.csaiapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chatBot.interfaces.ChatBotService;
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;
import id.klikdigital.csaiapp.chatBot.response.ChatBotResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanduanFragment extends Fragment {
    private TableView<String[]> tableView;
    private List<ChatModelBot>chatModelBotList;
    private String perangkat,member;
    private final String autoreplay = "0" ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panduan, container, false);
        tableView = view.findViewById(R.id.tableview);
        member = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        perangkat = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        getDatatotable();
        return view;
    }

    private void getDatatotable(){
        ChatBotService chatBotService = Config.htppclient().create(ChatBotService.class);
        Call<ChatBotResponse>call = chatBotService.getChatbot(member,perangkat,autoreplay);
        call.enqueue(new Callback<ChatBotResponse>() {
            @Override
            public void onResponse(Call<ChatBotResponse> call, Response<ChatBotResponse> response) {
                if (response.isSuccessful()){
                    Log.d("RESPONSE SERVER", String.valueOf(response.code()));
                    ChatBotResponse chatBotResponse = response.body();
                    chatModelBotList = chatBotResponse.getData();
                    getData(chatModelBotList);
                } else {
                    Toast.makeText(getContext(),"GAGAL",Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE DATA CHAT BOT",response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ChatBotResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(List<ChatModelBot> chatModelBotList) {
        String[] header = {"Keyword","Pesan","Type"};
        String[][] data = new String[chatModelBotList.size()][3];
        for (int i = 0; i < chatModelBotList.size(); i++){
            ChatModelBot chatModelBot = chatModelBotList.get(i);
            data[i][0] = chatModelBot.getKeyword();
            data[i][1] = chatModelBot.getPesan();
            data[i][2] = chatModelBot.getType();
        }
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),header));
        tableView.setDataAdapter(new SimpleTableDataAdapter(getContext(),data));
    }
}