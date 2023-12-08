package id.klikdigital.csaiapp.fragment;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import id.klikdigital.csaiapp.R;
public class ChatRoomFragment extends Fragment {
    private AppCompatImageView btnback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
      btnback = view.findViewById(R.id.btnBackk);
      btnback.setOnClickListener(view1 -> {
          FragmentTransaction fr = getFragmentManager().beginTransaction();
          fr.replace(R.id.id_frame,new ChatBotFragment());
          fr.commit();
      });
        return view;
    }
}