package com.gardenlink_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

/*
Every Activities need to extends NavigableActivity
Every Layouts need to include @layout/menu_hamburger
    <include
      layout="@layout/menu_hamburger"
       app:layout_constraintStart_toStartOf="parent"
       app:layout_constraintTop_toTopOf="parent"/>
Need to call initMenu() in onCreate methode */
public abstract class NavigableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Layout Menu
    private DrawerLayout drawerLayout;
    //Button Menu
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initMenu() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.home:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.myAccount:
                startActivity(new Intent(this, MyAccountActivity.class));
                break;
            case R.id.wallet:
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.messaging:
                startActivity(new Intent(this, MessagingActivity.class));
                break;
            case R.id.myLeasing:
                startActivity(new Intent(this, MyLeasingActivity.class));
                break;
            case R.id.myLands:
                startActivity(new Intent(this, MyLandsActivity.class));
                break;
            case R.id.signOut:
                startActivity(new Intent(this, SignOutActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
