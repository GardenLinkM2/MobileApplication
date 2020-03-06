package com.gardenlink_mobile;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initMenu();
    }


    public void toConnexion(View view) {
        Intent lIntent = new Intent(this, ConnexionActivity.class);
        startActivity(lIntent);
    }
}
