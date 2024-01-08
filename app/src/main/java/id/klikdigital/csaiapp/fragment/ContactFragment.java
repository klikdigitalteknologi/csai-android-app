package id.klikdigital.csaiapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
    private String member, whatsapp;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TableView<ContactModels> tableView;
    private AppCompatButton btnImport,btnAddContact,btnAddLabel;
    private List<ContactModels> data;
    private ImageView imageView;

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
        btnImport = view.findViewById(R.id.btnImport);
        btnAddContact = view.findViewById(R.id.btnAddContact);
        btnAddLabel = view.findViewById(R.id.btnAddlabel);


        swipeRefreshLayout = view.findViewById(R.id.sw_contact);
        tableView = view.findViewById(R.id.table_contact);
        member = SessionManage.getInstance(getContext()).getUserData().getMemberKode();
        whatsapp = "0";

        btnAddLabel.setOnClickListener(v->openDropdown());
        btnImport.setOnClickListener(v->openImportContact());
        btnAddContact.setOnClickListener(v->openAddContact());

        getDataContact();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getDataContact();
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    private void openAddContact() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_contact);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((int) (width*.9), (int) (height*.9));
        dialog.show();
    }

    private void openImportContact() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.import_contact);
        imageView = dialog.findViewById(R.id.imgclos);
        Button chosefile = dialog.findViewById(R.id.btnChoseFile);
        Button saveLabel = dialog.findViewById(R.id.btnImport);
        TextView textView = dialog.findViewById(R.id.textDownloadSample);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((int) (width*.9), (int) (height*.9));

        textView.setOnClickListener(v->openFolder());
        chosefile.setOnClickListener(v->openFile());
        imageView.setOnClickListener(v->dialog.dismiss());
        saveLabel.setOnClickListener(v->Toast.makeText(getContext(),"INISAVE",Toast.LENGTH_SHORT).show());
        dialog.show();
    }

    private void openFile() {
        Toast.makeText(getContext(),"OPENFILE",Toast.LENGTH_SHORT).show();
    }

    private void openFolder() {
        Toast.makeText(getContext(),"OPENFOLDER",Toast.LENGTH_SHORT).show();
    }

    private void openDropdown() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_label_contact);
        imageView = dialog.findViewById(R.id.imageCloses);
        EditText eLabel = dialog.findViewById(R.id.eLabel);
        Button saveLabel = dialog.findViewById(R.id.btnSendLabel);
        EditText search = dialog.findViewById(R.id.eSearch);
        Spinner spinner = dialog.findViewById(R.id.spn);

        String[] selectItem = {"Show","10","25","50","100"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(dialog.getContext(), android.R.layout.simple_spinner_dropdown_item, selectItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((int) (width*.9), (int) (height*.9));
        imageView.setOnClickListener(v->dialog.dismiss());
        saveLabel.setOnClickListener(v->Toast.makeText(getContext(),"INISAVE",Toast.LENGTH_SHORT).show());
        dialog.show();
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