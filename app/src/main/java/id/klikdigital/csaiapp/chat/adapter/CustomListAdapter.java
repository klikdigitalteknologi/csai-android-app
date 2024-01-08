package id.klikdigital.csaiapp.chat.adapter;
import static com.amulyakhare.textdrawable.TextDrawable.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.model.ChatModel;
import id.klikdigital.csaiapp.chat.model.NotifModel;
import id.klikdigital.csaiapp.chat.response.NotifResponse;

public class CustomListAdapter extends ArrayAdapter<ChatModel> {
private ArrayList<NotifModel>notifModels;

    public CustomListAdapter(Context context, ArrayList<ChatModel> chats) {
        super(context, 0, chats);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatModel chat = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.imageChat);
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textMessage = convertView.findViewById(R.id.textMessage);
        TextView textTime = convertView.findViewById(R.id.textTimeList);
        TextView textCountMessage = convertView.findViewById(R.id.textCountMessage);

       if (chat != null) {
           String nama = chat.getNama();
           int cut = 1;
           String cutName = nama.substring(0, cut);
           String up = cutName.toUpperCase();
           TextDrawable drawable = new TextDrawable.Builder()
                   .setColor(Color.rgb(88, 128, 192))
                   .setRadius(2)
                   .setShape(SHAPE_ROUND)
                   .setBold()
                   .setText(up)
                   .build();
           imageView.setImageDrawable(drawable);
           String waktu = chat.getTime();
           String time = waktu.substring(0, 5);
           textTime.setText(time);
           String a = chat.getPesan();
           String[] words = a.split("\\s+");
           int numberOfWordsToDisplay = 5;
           StringBuilder truncatedMessage = new StringBuilder();
           for (int i = 0; i < Math.min(numberOfWordsToDisplay, words.length); i++) {
               truncatedMessage.append(words[i]).append(" ");
           }
           textName.setText(chat.getNama());
           if (chat.getPesan().isEmpty()){
               textMessage.setText("FILE");
           }
           textMessage.setText(truncatedMessage.toString().trim() + "...");
           if (Objects.equals(chat.getStatus(), "0")) {

                   textCountMessage.setVisibility(View.VISIBLE);
                   textMessage.setTypeface(null, Typeface.BOLD);
                   textName.setTypeface(null, Typeface.BOLD);

           } else {
               textCountMessage.setVisibility(View.GONE);
           }
       }

        return convertView;
    }

}


