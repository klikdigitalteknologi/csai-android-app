package id.klikdigital.csaiapp.login.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.List;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.config.Config;
import id.klikdigital.csaiapp.home.Home;
import id.klikdigital.csaiapp.login.interfaces.ApiService;
import id.klikdigital.csaiapp.login.model.UserItem;
import id.klikdigital.csaiapp.login.response.ApiResponse;
import id.klikdigital.csaiapp.session.SessionManage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivity extends AppCompatActivity {

    private EditText eUser,ePass,eMember;
    private AppCompatButton login,sukses;
    private ProgressBar pbLogin;
    private String username,password,member;
    private ImageView eye;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (SessionManage.getInstance(this).isLoggedIn()){
            startActivity(new Intent(this, Home.class));
            finish();
        }
        eUser = findViewById(R.id.textUsername);
        ePass = findViewById(R.id.textPassword);
        login = findViewById(R.id.btn_login);
        pbLogin = findViewById(R.id.pbLogin);
        sukses = findViewById(R.id.btn_sukseslogin);
        eMember = findViewById(R.id.textMember);
        eye = findViewById(R.id.eyeTogle);
        eye.setOnClickListener(view -> {
            if (ePass.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                Toast.makeText(getApplicationContext(),"PASSWORD TERLIHAT",Toast.LENGTH_SHORT).show();
                ePass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                eye.setImageResource(R.drawable.ic_eye_closed);
            } else {
                Toast.makeText(getApplicationContext(),"PASSWORD TERTUTUP",Toast.LENGTH_SHORT).show();
                ePass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                imageViewTogglePassword.setImageResource(R.drawable.ic_eye_open);
            }
            ePass.clearFocus();
            eye.requestFocus();
            // Posisikan kursor di akhir teks
            ePass.setSelection(ePass.length());
        });
        login.setOnClickListener(view -> {
            username = eUser.getText().toString().trim();
            password =ePass.getText().toString().trim();
            member = eMember.getText().toString().trim();
            if (username.equals("")){
                eUser.setError("username/email wajib di isi");
            } else if (password.equals("")) {
                ePass.setError("password tidak boleh kosong");
            } else if (member.equals("")) {
                eMember.setError("member wajib di isi");
            } else {
                _authLogin();
            }
        });
        }
    private void _authLogin() {
        ApiService apiService = Config.htppclient().create(ApiService.class);
        Call<ApiResponse> apiResponseCall = apiService.loginUser(username,password,member);
        apiResponseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.isStatus()) {
                        List<UserItem> dataList = apiResponse.getData();
                        System.out.println(dataList);
                        if (dataList != null && !dataList.isEmpty()) {
                            System.out.println(dataList);
                            saveDataToSession(dataList.get(0));
                            loading(true);
                        } else {
                            loading(false);
                            Log.d("RESPONSE","data kosong");
                        }
                    } else {
                        loading(false);
                        Toast.makeText(getApplicationContext(),"user tidak terdaftar",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading(false);
                    Log.d("RESPONSE ERROR LOGIN", "message" + response.code());
                    Toast.makeText(getApplicationContext(),"Gagal Login",Toast.LENGTH_SHORT).show();
                          }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loading(boolean isLoading) {
        if (isLoading){
            login.setVisibility(View.INVISIBLE);
            sukses.setVisibility(View.VISIBLE);
            pbLogin.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Berhasil Login",Toast.LENGTH_SHORT).show();
            finish();
        } else {
            login.setVisibility(View.VISIBLE);
            pbLogin.setVisibility(View.INVISIBLE);
        }

    }

    private void saveDataToSession(UserItem userItem) {
        SessionManage.getInstance(getApplicationContext()).saveUserData(userItem);
    }
}
