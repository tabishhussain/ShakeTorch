package com.example.tabish.shakedetector;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean flashAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashAvailable = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (flashAvailable) {
            startService(new Intent(this, ShowTorchService.class));
        } else {
            Toast.makeText(MainActivity.this, "Flash not available", Toast.LENGTH_SHORT).show();
        }

    }
}
