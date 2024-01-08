package id.klikdigital.csaiapp.fragment;

import android.annotation.SuppressLint;
import android.icu.util.VersionInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.BuildConfig;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.session.SessionManage;



public class HomeFragment extends Fragment {
    private TextView text;
    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView textView = view.findViewById(R.id.textCs);
        text = view.findViewById(R.id.textVerion);
        SessionManage sessionManage = SessionManage.getInstance(getContext());
        UserItem userItem = sessionManage.getUserData();
textView.setText("Selamat Datang " + userItem.getNamaLengkap() + "!");
text.setText("CSAI version : v" + BuildConfig.VERSION_NAME);
        return view;
    }
}