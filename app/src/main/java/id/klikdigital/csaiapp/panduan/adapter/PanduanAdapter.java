package id.klikdigital.csaiapp.panduan.adapter;

import static com.amulyakhare.textdrawable.TextDrawable.SHAPE_ROUND;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.chatBot.model.ChatModelBot;

public class PanduanAdapter extends ArrayAdapter<ChatModelBot> {
    public PanduanAdapter(@NonNull Context context, ArrayList<ChatModelBot> chatModelBots) {
        super(context, 0, chatModelBots);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       ChatModelBot chatModelBot = getItem(position);
       if (convertView == null) {
           convertView = View.inflate(getContext(), R.layout.list_chat_bot, null);
       }
        TextView textKey = convertView.findViewById(R.id.txtKeyiu);
       TextView textPesan = convertView.findViewById(R.id.txtPs);
       TextView Type = convertView.findViewById(R.id.txtTy);

       if (chatModelBot !=null){
           textKey.setText(chatModelBot.getKeyword());
           textPesan.setText(chatModelBot.getPesan());


           if (chatModelBot.getType().equals("text")){
              Type.setText(chatModelBot.getType());
           }else {
             Type.setText("Image");
           }

       }
       return convertView;
    }
}
