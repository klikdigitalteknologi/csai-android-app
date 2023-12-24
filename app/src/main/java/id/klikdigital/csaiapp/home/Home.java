package id.klikdigital.csaiapp.home;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import id.klikdigital.csaiapp.R;
import id.klikdigital.csaiapp.login.activity.LoginActivity;
import id.klikdigital.csaiapp.session.SessionManage;
public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageView,btn_close;
    private CardView layoutoolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        layoutoolbar = findViewById(R.id.layoutToolBarHome);
        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        imageView = findViewById(R.id.image_home);

        btn_close =navigationView.getHeaderView(0).findViewById(R.id.btnClose);
        imageView.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        btn_close.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.START));

        // Temukan NavHostFragment dan dapatkan NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        NavController navController = navHostFragment.getNavController();

        // Tambahkan Listener untuk OnBackStackChanged
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> navigationView.setCheckedItem(destination.getId()));
    }
    @Override
    public void onBackStackChanged() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        int currentDestinationId = navController.getCurrentDestination().getId();
        navigationView.setCheckedItem(currentDestinationId);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home){
          navigateToFragment(R.id.nav_home);
            return true;
        } else if (itemId == R.id.nav_chat) {
            navigateToFragment(R.id.nav_chat);
            return true;
        } else if (itemId == R.id.nav_chatBot) {
            navigateToFragment(R.id.nav_chatBot);
            return true;
        } else if (itemId == R.id.nav_quick) {
            navigateToFragment(R.id.nav_quick);
            return true;
        } else if (itemId == R.id.nav_contact) {
            navigateToFragment(R.id.nav_contact);
            return true;
        } else if (itemId == R.id.nav_panduan) {
            navigateToFragment(R.id.nav_panduan);
            return true;
                    }
        return false;
    }
    private void navigateToFragment(int fragmentId) {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        navController.navigate(fragmentId);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.popup, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        AppCompatButton btnYes = dialogView.findViewById(R.id.btn_yes);
        AppCompatButton btnNo = dialogView.findViewById(R.id.btn_no);
        btnYes.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            SessionManage sessionManage = new SessionManage(getApplicationContext());
            sessionManage.clearUserData();
            Toast.makeText(getApplicationContext(), "Logout Success", Toast.LENGTH_SHORT).show();
            finish();
            alertDialog.dismiss();
        });

        btnNo.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }
    public void showProfile(View view) {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.pic_profile));
        popupMenu.getMenuInflater().inflate(R.menu.menu_profile, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.logoutId){
                showLogoutConfirmationDialog();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    public void hideToolbar() {
        if (layoutoolbar != null) {
            layoutoolbar.setVisibility(View.GONE);
        }
    }
    public void showToolbar(){
        if (layoutoolbar != null){
            layoutoolbar.setVisibility(View.VISIBLE);
        }
    }


}