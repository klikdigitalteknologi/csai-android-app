package id.klikdigital.csaiapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.contact.adapter.ContactTableAdapter;
import id.klikdigital.csaiapp.contact.interfaces.ContactService;
import id.klikdigital.csaiapp.contact.models.ContactModels;
import id.klikdigital.csaiapp.contact.response.ContactResponse;
import id.klikdigital.csaiapp.home.Home;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFragment extends Fragment {
    private Spinner spinner1, spinner2;
    private String member, whatsapp;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TableView<ContactModels> tableView;
    private List<ContactModels> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        if (getActivity() instanceof Home) {
            Home mainActivity = (Home) getActivity();
            if (mainActivity != null) {
                mainActivity.showToolbar();
            }
        }
        swipeRefreshLayout = view.findViewById(R.id.sw_contact);
        spinner1 = view.findViewById(R.id.s_contact);
        spinner2 = view.findViewById(R.id.contact_s);
        tableView = view.findViewById(R.id.table_contact);
        String[] selectItem = {"All Contact", "10", "25", "50", "100"};
        String[] filter = {"filter", "text", "image"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, selectItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, filter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(arrayAdapter);
        member = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        whatsapp = "0";

        getDataContact();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getDataContact();
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    private void getDataContact() {
        ContactService contactService = Config.htppclient().create(ContactService.class);
        Call<ContactResponse> call = contactService.getContact(member, whatsapp);
        call.enqueue(new Callback<ContactResponse>() {
            @Override
            public void onResponse(@NonNull Call<ContactResponse> call, @NonNull Response<ContactResponse> response) {
                Log.d("REQUEST", response.toString());
                if (response.isSuccessful()) {
                    ContactResponse contactResponse = response.body();
                    if (contactResponse != null && contactResponse.isStatus()) {
                        data = contactResponse.getData();
                        getData(data);
                    }else {
                        Toast.makeText(getContext(),"GAGAL MENAMPILKAN DATA", Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE DATA", "MSG: " +response.code());
                    }
                }else {
                    Toast.makeText(getContext(),"SERVER TIME OUT",Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE DATA", "MSG: " +response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<ContactResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(List<ContactModels> datalist) {
        String[] header = {"Nama", "Nomor", "Label"};
        String[][] data = new String[datalist.size()][4];
        for (int i = 0; i < datalist.size(); i++) {
            ContactModels contactModels = datalist.get(i);
            data[i][0] = contactModels.getNamaLengkap();
            data[i][1] = contactModels.getWhatsapp();
            data[i][2] = contactModels.getNameLabel();
            data[i][3] = "";
        }
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),header));
        tableView.setDataAdapter(new ContactTableAdapter(getContext(),datalist, getChildFragmentManager()));

    }
}