package com.kerencev.notes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.kerencev.notes.R;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.StyleOfNotes;

public class MainActivity extends AppCompatActivity implements ToolbarHolder {

    private SharedPreferences sPrefForStyle;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);

        setNavigationClicks();

        sPrefForStyle = getSharedPreferences("Store_style", MODE_PRIVATE);
        Data.loadStyle(sPrefForStyle, this);
        Data.loadIsHasDate(sPrefForStyle, this);
        Data.loadIsSaveNotes(sPrefForStyle, this);
        Data.loadDirection(sPrefForStyle, this);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

    }

    private void setNavigationClicks() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.notes:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new NotesDescriptionFragment())
                                .commit();
                        drawerLayout.close();
                        return true;

                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new SettingsFragment())
                                .commit();
                        drawerLayout.close();
                        return true;

                    case R.id.trash:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new TrashFragment())
                                .commit();
                        drawerLayout.close();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Data.saveStyle(sPrefForStyle, StyleOfNotes.getINSTANCE(this).getStyle());
        Data.saveIsHasDate(sPrefForStyle, StyleOfNotes.getINSTANCE(this).isIsHasDate());
        Data.saveIsSaveNotes(sPrefForStyle, StyleOfNotes.getINSTANCE(this).getIsSaveNotes());
        Data.saveDirection(sPrefForStyle, StyleOfNotes.getINSTANCE(this).getDirection(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Data.saveStyle(sPrefForStyle, StyleOfNotes.getINSTANCE(this).getStyle());
        Data.saveIsHasDate(sPrefForStyle, StyleOfNotes.getINSTANCE(this).isIsHasDate());
        Data.saveIsSaveNotes(sPrefForStyle, StyleOfNotes.getINSTANCE(this).getIsSaveNotes());
        Data.saveDirection(sPrefForStyle, StyleOfNotes.getINSTANCE(this).getDirection(), this);
    }
}