package com.app.mcworlduser.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.mcworlduser.Fragment.HomeFragment;
import com.app.mcworlduser.Fragment.MenuVendorFragment;
import com.app.mcworlduser.Fragment.MyAccountFragment;
import com.app.mcworlduser.Fragment.SearchFragment;
import com.app.mcworlduser.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setItemIconTintList(null);
      /*  bottomNav.getMenu().findItem(R.id.nav_mc_world).setChecked(true);
        bottomNav.getMenu().findItem(R.id.nav_mc_world).setChecked(true);
*/

        bottomNav.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();


        }


        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CircularHomeFragment()).commit();
        Animatoo.animateZoom(MainActivity.this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_mc_world:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).addToBackStack(null).commit();



                Animatoo.animateFade(MainActivity.this);


                break;


           /* case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).addToBackStack(null).commit();
                Animatoo.animateFade(MainActivity.this);
                break;*/

            case R.id.nav_menu:
                /*getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuVendorFragment()).addToBackStack(null).commit();
                Animatoo.animateFade(MainActivity.this);*/
                startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
                Animatoo.animateFade(MainActivity.this);
                break;

                case R.id.nav_cart:
                startActivity(new Intent(MainActivity.this,MyCartActivity.class));
                Animatoo.animateFade(MainActivity.this);
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyAccountFragment()).addToBackStack(null).commit();
                Animatoo.animateFade(MainActivity.this);
                break;

        }
        return true;
    }
}
