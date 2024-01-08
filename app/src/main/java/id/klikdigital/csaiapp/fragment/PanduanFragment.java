package id.klikdigital.csaiapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chatBot.interfaces.ChatBotService;
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;
import id.klikdigital.csaiapp.chatBot.response.ChatBotResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.panduan.adapter.PanduanAdapter;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanduanFragment extends Fragment {
    private TableView tableView;
    private SwipeRefreshLayout sw;
    private Pagination pagination;
    private ArrayList<ChatModelBot> chatModelBotList;
    private ListView listView;
    private String perangkat, member;
    private PanduanAdapter panduanAdapter;
    private final String autoreplay = "0";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panduan, container, false);
        member = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        perangkat = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        listView = view.findViewById(R.id.listPanduan);
        chatModelBotList = new ArrayList<>();
        sw = view.findViewById(R.id.swPanduan);
        panduanAdapter = new PanduanAdapter(getContext(),new ArrayList<>());

        sw.setOnRefreshListener(() -> {
            sw.setRefreshing(true);
            getDataFormServwr();
            sw.setRefreshing(false);
        });
        getDataFormServwr();
        return view;
    }

    private void getDataFormServwr() {
        ChatBotService chatBotService = Config.htppclient().create(ChatBotService.class);
        Call<ChatBotResponse> chatBotResponseCall = chatBotService.getChatbot(member,perangkat,autoreplay);
        chatBotResponseCall.enqueue(new Callback<ChatBotResponse>() {
            @Override
            public void onResponse(Call<ChatBotResponse> call, Response<ChatBotResponse> response) {
                if (response.isSuccessful()) {
                    ChatBotResponse chatBotResponse = response.body();
                    if (chatBotResponse !=null && chatBotResponse.isStatus() && chatBotResponse.getData() != null){
                        chatModelBotList = (ArrayList<ChatModelBot>) chatBotResponse.getData();
                        panduanAdapter.clear();
                        panduanAdapter.addAll(chatModelBotList);
                        panduanAdapter.notifyDataSetChanged();
                        listView.setAdapter(panduanAdapter);
                    }else {
                        Toast.makeText(getContext(), "GAGAL", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "GAGAL", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ChatBotResponse> call, Throwable t) {
                Toast.makeText(getContext(), "GAGAL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}