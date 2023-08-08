package com.example.duongndtph25724_mob403_assignment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duongndtph25724_mob403_assignment.BottomNav.Fragment1;
import com.example.duongndtph25724_mob403_assignment.BottomNav.Fragment2;
import com.example.duongndtph25724_mob403_assignment.BottomNav.HomeFragment;
import com.example.duongndtph25724_mob403_assignment.DrawerNav.Account_infor;
import com.example.duongndtph25724_mob403_assignment.DrawerNav.ChangePassword;
import com.example.duongndtph25724_mob403_assignment.DrawerNav.ChangeUsername;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    TextView username;
    private BottomNavigationView bottomNavigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        username = findViewById(R.id.userName);
//        Bundle bundle = getIntent().getExtras();
//        String title = bundle.getString("key1", "Default");
//        username.setText(title);


        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Thiết lập nút menu cho ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        // Xử lý sự kiện khi nút menu được nhấn
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_item1:
                        selectedFragment = new Account_infor();
                        break;
                    case R.id.nav_item2:
                        selectedFragment = new ChangeUsername();
                        break;
                    case R.id.nav_item3:
                        selectedFragment = new ChangePassword();
                        break;
                    case R.id.nav_item4:

                        Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        // Mặc định là HomeFragment
                        selectedFragment = new HomeFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navHome:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navBill:
                        selectedFragment = new Fragment1();
                        break;
                    case R.id.navHistory:
                        selectedFragment = new Fragment2();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            }
        });

        // Mặc định hiển thị trang Home
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void SetUserName(String name){
        username = findViewById(R.id.userName);
        username.setText(name);
    }
}