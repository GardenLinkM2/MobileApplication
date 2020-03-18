package com.gardenlink_mobile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.PreferenceUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class NavigableActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CURRENT_ACTIVITY_ID = "CurrentActivityID";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initMenu() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                ((View) drawerLayout).bringToFront();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                final ViewGroup parent = ((ViewGroup) drawerView.getParent().getParent());
                final ViewGroup lInclude = (ViewGroup) drawerView.getParent();
                if (parent != null) {
                    parent.removeViewInLayout(lInclude);
                    parent.addView(lInclude, 0);
                }
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.header);
        createHeader(headerView);
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
                new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                        .setTitle(getResources().getString(R.string.signout_dialog))
                        .setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> doSignOut())
                        .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                        }).show();
                return true;
        }

        createBundle(intent, id);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void doSignOut() {
        Session.getInstance().flush();
        PreferenceUtils.savePassword(null, this);
        PreferenceUtils.saveEmail(null, this);
        Intent intent = new Intent(this, ConnectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void createBundle(Intent intent, int id) {
        Bundle newBundle = new Bundle();
        newBundle.putInt(CURRENT_ACTIVITY_ID, id);
        intent.putExtras(newBundle);
    }

    @Override
    public void onBackPressed() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (!this.getClass().getSimpleName().contains("MainActivity")) {
            Bundle currentBundle = getIntent().getExtras();
            if (currentBundle != null) {
                int id = currentBundle.getInt(CURRENT_ACTIVITY_ID);
                navigationView.setCheckedItem(id);
            }
            super.onResume();
        } else {
            int size = navigationView.getMenu().size();
            for (int i = 0; i < size; i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
            }
            super.onResume();
        }
    }
}
