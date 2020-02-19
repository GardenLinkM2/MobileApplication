package com.gardenlink_mobile;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

public class SignOutActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signout_activity);

        initMenu();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void SignOut(View view){
        Toast.makeText(this, "Au revoir !", Toast.LENGTH_SHORT).show();
        finishAffinity();
        finish();
    }
}
