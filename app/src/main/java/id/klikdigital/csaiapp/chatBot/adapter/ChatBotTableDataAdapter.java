package id.klikdigital.csaiapp.chatBot.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;

public class ChatBotTableDataAdapter extends TableDataAdapter<ChatModelBot> {
    public ChatBotTableDataAdapter(Context context, List<ChatModelBot> data) {
        super(context,data);
    }
    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ChatModelBot rowData = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderString(rowData.getKeyword());
                break;
            case 1:
                renderedView = renderString(rowData.getPesan());
                break;
            case 2:
                renderedView = renderString(rowData.getType());
                break;
            case 3:
                // Renderkan tombol di sini
                renderedView = renderButton("View");
                break;
        }
        return renderedView;
    }
    private View renderString(String value) {
        // Implementasikan logika untuk nilai String
        // (Anda dapat menggunakan TextView atau tampilan lain yang sesuai)
        TextView textView = new TextView(getContext());
        String truncatedValue = value.length() > 10 ? value.substring(0, 5) + "..." : value;
        textView.setText(truncatedValue);
        textView.setText(value);
        textView.setTextSize(16);
        textView.setPadding(20, 20, 20, 20);
        return textView;
    }

    private View renderButton(String buttonText) {
       TextView textView = new TextView(getContext());
       textView.setText(buttonText);
       int color = getResources().getColor(R.color.colorLink);
       textView.setTextColor(color);
       textView.setOnClickListener(view -> {
           Toast.makeText(getContext(),"INI DATA",Toast.LENGTH_SHORT).show();
       });
       return textView;
    }
}

