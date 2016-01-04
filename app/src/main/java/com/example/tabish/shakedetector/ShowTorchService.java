package com.example.tabish.shakedetector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;

public class ShowTorchService extends Service {
    private ShakeListener mShaker;
    CameraManager mCameraManager;
    Camera cam;
    Handler mHandler;

    public ShowTorchService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(100);
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        mCameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
                        CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics("0");
                        mCameraManager.setTorchMode("0", true);
                        mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mCameraManager.setTorchMode("0", false);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 15000);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    cam = Camera.open();
                    Camera.Parameters p = cam.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                    mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (cam != null) {
                                cam.stopPreview();
                                cam.release();
                            }
                        }
                    }, 15000);

                }
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
