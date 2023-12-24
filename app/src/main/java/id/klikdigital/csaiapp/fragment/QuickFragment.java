package id.klikdigital.csaiapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;
import id.klikdigital.csaiapp.chatBot.response.ChatBotResponse;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.home.Home;
import id.klikdigital.csaiapp.quickreplay.adapter.QuickTableAdapter;
import id.klikdigital.csaiapp.quickreplay.interfaces.QuickReplayService;
import id.klikdigital.csaiapp.quickreplay.models.QuickReplayModels;
import id.klikdigital.csaiapp.quickreplay.response.QuickReplayResponse;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickFragment extends Fragment {

    private Spinner spinner1,spinner2;
    private String member,perangkat;
    private static final String replyfast = "0";
    private SwipeRefreshLayout swipeRefreshLayout;
    private TableView<QuickReplayModels> tableView;
    private List<QuickReplayModels>data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick, container, false);
        if (getActivity() instanceof Home) {
            Home mainActivity = (Home) getActivity();
            if (mainActivity != null) {
                mainActivity.showToolbar();
            }
        }
        tableView = view.findViewById(R.id.table_quickreply);
        spinner1 = view.findViewById(R.id.s_couint);
        spinner2 = view.findViewById(R.id.s_count);
        swipeRefreshLayout = view.findViewById(R.id.sw_quick);
        String[] selectItem = {"All Contact","10","25","50","100"};
        String[] filter = {"filter","text","image"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, filter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(arrayAdapter);

        member = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        perangkat = SessionManage.getInstance(getContext()).getUserData().getMemberKode();

        getQuickReply();
        swipeRefreshLayout.setOnRefreshListener(()->{
            swipeRefreshLayout.setRefreshing(true);
            getQuickReply(); //get data from server
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    private void getQuickReply() {
        QuickReplayService quickReplayService = Config.htppclient().create(QuickReplayService.class);
        Call<QuickReplayResponse>call = quickReplayService.getReplay(member,perangkat,replyfast);
        call.enqueue(new Callback<QuickReplayResponse>() {
            @Override
            public void onResponse(Call<QuickReplayResponse> call, Response<QuickReplayResponse> response) {
                Log.d("Response server", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    QuickReplayResponse replayResponse = response.body();
                    if (replayResponse != null && replayResponse.isStatus()) {
                        data = replayResponse.getData();
                        getData(data);
                    }else {
                        Toast.makeText(getContext(),"not responding",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuickReplayResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(List<QuickReplayModels> data) {
        String[] header = {"Keyword","Pesan","Type"};
        String[][] quick = new String[data.size()][4];
        for (int i = 0; i < data.size(); i++){
            QuickReplayModels replayModels = data.get(i);
            quick[i][0] = replayModels.getKeyword();
            quick[i][1] = replayModels.getPesan();
            quick[i][2] = replayModels.getType();
            quick[i][3] = "";
        }
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),header));
        tableView.setDataAdapter(new QuickTableAdapter(getContext(),data, getChildFragmentManager()));
    }
}