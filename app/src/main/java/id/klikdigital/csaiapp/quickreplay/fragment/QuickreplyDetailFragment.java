package id.klikdigital.csaiapp.quickreplay.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.home.Home;


public class QuickreplyDetailFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quickreply_detail, container, false);
        if (getActivity() instanceof Home) {
            Home mainActivity = (Home) getActivity();
            if (mainActivity != null) {
                mainActivity.hideToolbar();
            }
        }
        return view;
    }
}