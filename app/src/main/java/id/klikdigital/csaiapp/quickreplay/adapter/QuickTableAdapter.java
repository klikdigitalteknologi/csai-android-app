package id.klikdigital.csaiapp.quickreplay.adapter;

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
import id.klikdigital.csaiapp.quickreplay.fragment.QuickreplyDetailFragment;
import id.klikdigital.csaiapp.quickreplay.models.QuickReplayModels;

public class QuickTableAdapter extends TableDataAdapter<QuickReplayModels> {
    private FragmentManager fragmentManager;
    private Context mct;
    private String kdAutoReplay,pesan,key,jenis;
    public QuickTableAdapter(Context context, List<QuickReplayModels> data, FragmentManager fragmentManager) {
        super(context,data);
        this.fragmentManager = fragmentManager;
        this.mct = context;
    }
    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        QuickReplayModels replayModels = getRowData(rowIndex);
        kdAutoReplay = replayModels.getKdReplyFast();
        key = replayModels.getKeyword();
        pesan = replayModels.getPesan();
        jenis = replayModels.getType();
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderString(replayModels.getKeyword());
                break;
            case 1:
                renderedView = renderString(replayModels.getPesan());
                break;
            case 2:
                renderedView = renderString(replayModels.getType());
                break;
            case 3:
                // Renderkan tombol di sini
                renderedView = renderButton("View",replayModels.getKdReplyFast());
                break;
        }
        return renderedView;
    }

    private View renderString(String value) {
        TextView textView = new TextView(getContext());
        String truncatedValue = value.length() > 10 ? value.substring(0, 5) + "..." : value;
        textView.setText(truncatedValue);
        textView.setTextSize(16);
        textView.setPadding(20, 20, 20, 20);
        return textView;
    }
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
            QuickreplyDetailFragment detailFragment = new QuickreplyDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("kd_autoreplay", rowData);
            detailFragment.setArguments(bundle);
            transaction.replace(R.id.navHostFragment, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}

