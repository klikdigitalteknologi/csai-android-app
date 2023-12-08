package id.klikdigital.csaiapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.login.activity.LoginActivity;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.session.SessionManage;

public class LogoutPopup {
    private Context mContext;

    private AlertDialog alertDialog;

    public LogoutPopup(Context context){
        mContext =context;
        createPopup();
    }

    private void createPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup, null);
        builder.setView(view);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        AppCompatButton btnYes = view.findViewById(R.id.btn_yes);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        AppCompatButton btnNo = view.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil tindakan saat user klik Ya
                // Misalnya, menghapus data sesi atau menghentikan layanan
                // Kemudian tutup popup
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                SessionManage.getInstance(mContext).clearUserData();
                Toast.makeText(mContext, "Anda telah logout", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil tindakan saat user klik Tidak
                // Kemudian tutup popup
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
    }
}
