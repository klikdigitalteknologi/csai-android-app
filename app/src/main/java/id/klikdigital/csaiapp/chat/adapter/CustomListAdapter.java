package id.klikdigital.csaiapp.chat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chat.model.ChatModel;

public class CustomListAdapter extends ArrayAdapter<ChatModel> {

    public CustomListAdapter(Context context, ArrayList<ChatModel> chats) {
        super(context, 0, chats);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatModel chat = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textMessage = convertView.findViewById(R.id.textMessage);
        TextView textTime = convertView.findViewById(R.id.textTimeList);
        TextView textCountMessage = convertView.findViewById(R.id.textCountMessage);
       if (chat != null){
     // asumsi chat.getTime() mengembalikan string waktu seperti "08:14:37"
//           String phoneNumberWithDomain = chat.getNomorWhatsapp();
//           String[] parts = phoneNumberWithDomain.split("@");
//           String phoneNumber = parts[0].trim();
           String waktu = chat.getTime();
           String time = waktu.substring(0,5);
           textTime.setText(time);
           textName.setText(chat.getNama());
           textMessage.setText(chat.getPesan());
           if (Objects.equals(chat.getStatus(), "0")){
               textCountMessage.setVisibility(View.VISIBLE);
               textMessage.setTypeface(null, Typeface.BOLD);
               textName.setTypeface(null,Typeface.BOLD);
           }else {
               textCountMessage.setVisibility(View.GONE);
           }

       }
        return convertView;
    }
}


