package com.chamodex.enterprises.loano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.chamodex.enterprises.loano.databinding.ActivityMainBinding;
import com.chamodex.enterprises.loano.fragments.ContactUsFragment;
import com.chamodex.enterprises.loano.fragments.HomeFragment;
import com.chamodex.enterprises.loano.fragments.MyInstallmentFragment;
import com.chamodex.enterprises.loano.fragments.MyProfileFragment;
import com.chamodex.enterprises.loano.utils.OnFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment(auth);
        fragmentManager
                .beginTransaction()
                .add(R.id.parentView, homeFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // option menu preparing
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Menu subMenu = menu.findItem(R.id.action).getSubMenu();
        for (int i = 0; i < subMenu.size()-1; i++) {
            subMenu.getItem(i).setOnMenuItemClickListener(item -> {
                menu.findItem(R.id.titleSelector).setTitle(item.getTitle().toString().toUpperCase());
                menu.findItem(R.id.titleSelector).setIcon(item.getIcon());
                // functions
                if (item.getItemId() == R.id.mainHome) {
                    HomeFragment homeFragment = new HomeFragment(auth);
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.parentView, homeFragment)
                            .addToBackStack(null)
                            .commit();
                } else if (item.getItemId() == R.id.profile_detail) {
                    MyProfileFragment profileFragment = new MyProfileFragment(auth);
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.parentView, profileFragment)
                            .addToBackStack(null)
                            .commit();
                } else if (item.getItemId() == R.id.support) {
                    ContactUsFragment contactFragment = new ContactUsFragment(auth);
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.parentView, contactFragment)
                            .addToBackStack(null)
                            .commit();
                } else if (item.getItemId() == R.id.installment) {
                    MyInstallmentFragment installmentFragment = new MyInstallmentFragment(auth);
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.parentView, installmentFragment)
                            .addToBackStack(null)
                            .commit();
                } else if (item.getItemId() == R.id.titleSelector) {

                }
                return true;
            });
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void changeFragment(int id) {
        if (id == R.id.mainHome) {
            HomeFragment homeFragment = new HomeFragment(auth);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.parentView, homeFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.profile_detail) {
            MyProfileFragment profileFragment = new MyProfileFragment(auth);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.parentView, profileFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.support) {
            ContactUsFragment contactFragment = new ContactUsFragment(auth);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.parentView, contactFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.installment) {
            MyInstallmentFragment installmentFragment = new MyInstallmentFragment(auth);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.parentView, installmentFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    // logout handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}