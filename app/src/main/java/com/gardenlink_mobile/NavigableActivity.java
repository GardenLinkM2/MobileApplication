package com.gardenlink_mobile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/*
Every Activities need to extends NavigableActivity
Every Layouts need to include @layout/menu_hamburger
    <include
      layout="@layout/menu_hamburger"
       app:layout_constraintStart_toStartOf="parent"
       app:layout_constraintTop_toTopOf="parent"/>
Need to call initMenu(id) in onCreate methode */
public abstract class NavigableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CURRENT_ACTIVITY_ID = "CurrentActivityID";

    //Menu
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    //User Data
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initMenu() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.header);
        createHeader(headerView);
    }

    protected void initMenu(int idCurrentActivity) {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.header);
        createHeader(headerView);

        navigationView.setCheckedItem(idCurrentActivity);
    }


    private void createHeader(View headerView) {
        //TODO: Update with real UserData
        //Todo:getUserAvatar()
        CircleImageView userAvatar = headerView.findViewById(R.id.user_avatar);
        userAvatar.setImageDrawable(getResources().getDrawable(R.drawable.sample_avatar));

        //Todo: getUserName()
        TextView userName = headerView.findViewById(R.id.user_name);
        userName.setText("Pouglou Denis");

        //TODO: Currency ?
        //Todo:getWalletAmount()
        TextView userWallet = headerView.findViewById(R.id.user_wallet);
        String amount = NumberFormat.getInstance().format(346546.5);
        Locale locale = getResources().getConfiguration().locale;
        Currency currency = Currency.getInstance(locale);
        userWallet.setText(amount + currency.getSymbol());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent = new Intent();

        switch (id) {
            case R.id.home:
                intent = new Intent(this, HomeActivity.class);
                break;
            case R.id.myAccount:
                intent = new Intent(this, MyAccountActivity.class);
                break;
            case R.id.wallet:
                intent = new Intent(this, WalletActivity.class);
                break;
            case R.id.messaging:
                intent = new Intent(this, MessagingActivity.class);
                break;
            case R.id.myLeasing:
                intent = new Intent(this, MyLeasingActivity.class);
                break;
            case R.id.myLands:
                intent = new Intent(this, MyLandsActivity.class);
                break;
            case R.id.signOut:
                intent = new Intent(this, SignOutActivity.class);
                break;
        }

        createBundle(intent, id);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createBundle(Intent intent, int id) {
        Bundle newBundle = new Bundle();
        newBundle.putInt(CURRENT_ACTIVITY_ID, id);
        intent.putExtras(newBundle);
    }
}
