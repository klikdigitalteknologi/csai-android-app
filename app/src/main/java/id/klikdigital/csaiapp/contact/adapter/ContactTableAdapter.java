package id.klikdigital.csaiapp.contact.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chatBot.fragment.DetailChatBotFragment;
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;
import id.klikdigital.csaiapp.contact.fragment.ContactDetailsFragment;
import id.klikdigital.csaiapp.contact.models.ContactModels;

public class ContactTableAdapter extends TableDataAdapter<ContactModels> {
    private FragmentManager fragmentManager;
    private Context mct;
    private String kdsavewa,nama,nomor,label;
    public ContactTableAdapter(Context context, List<ContactModels> data, FragmentManager fragmentManager) {
        super(context,data);
        this.fragmentManager = fragmentManager;
        this.mct = context;
    }
    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ContactModels contactModels = getRowData(rowIndex);
        kdsavewa = contactModels.getKdSaveWa();
        nomor = contactModels.getWhatsapp();
        nama = contactModels.getNamaLengkap();
        label = contactModels.getNameLabel();

        View renderedview = null;
        switch (columnIndex) {
            case 0:
                renderedview = renderString(contactModels.getNamaLengkap());
                break;
            case 1:
                renderedview = renderString(contactModels.getWhatsapp());
                break;
            case 2:
                renderedview = renderString(contactModels.getNameLabel());
                break;
            case 3:
                // Renderkan tombol di sini
                renderedview = renderButton("View",contactModels.getWhatsapp());
                break;
        }
        return renderedview;
    }
    private View renderString(String value) {
        TextView textView = new TextView(getContext());
        String truncatedValue = value.length() > 10 ? value.substring(0, 5) + "..." : value;
        textView.setText(truncatedValue);
        textView.setTextSize(16);
        textView.setPadding(20, 20, 20, 20);
        return textView;
    }

    @SuppressLint("SetTextI18n")
    private View renderButton(String value, String rowData) {
        TextView textView = new TextView(getContext());
        textView.setText(value);
        int color = getResources().getColor(R.color.colorLink);
        textView.setTextColor(color);
        textView.setOnClickListener(view -> {
            showDetailFragment(rowData);
        });
        return textView;
    }
    private void showDetailFragment(String rowData) {
        if (getContext() instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("nomor", rowData);
            contactDetailsFragment.setArguments(bundle);
            transaction.replace(R.id.navHostFragment, contactDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
