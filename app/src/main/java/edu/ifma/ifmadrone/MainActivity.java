package edu.ifma.ifmadrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.common.util.CommonCallbacks;
import dji.log.DJILog;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKInitEvent;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.sdkmanager.LDMModule;
import dji.sdk.sdkmanager.LDMModuleType;
import edu.ifma.ifmadrone.views.CreateMission;
import edu.ifma.ifmadrone.views.TestMissionModel;
import edu.ifma.ifmadrone.views.VideoFeedActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();
    private static final String[] REQUIRED_PERMISSION_LIST = new String[] {
            Manifest.permission.VIBRATE, // Gimbal rotation
            Manifest.permission.INTERNET, // API requests
            Manifest.permission.ACCESS_WIFI_STATE, // WIFI connected products
            Manifest.permission.ACCESS_COARSE_LOCATION, // Maps
            Manifest.permission.ACCESS_NETWORK_STATE, // WIFI connected products
            Manifest.permission.ACCESS_FINE_LOCATION, // Maps
            Manifest.permission.CHANGE_WIFI_STATE, // Changing between WIFI and USB connection
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // Log files
            Manifest.permission.BLUETOOTH, // Bluetooth connected products
            Manifest.permission.BLUETOOTH_ADMIN, // Bluetooth connected products
            Manifest.permission.READ_EXTERNAL_STORAGE, // Log files
            Manifest.permission.READ_PHONE_STATE, // Device UUID accessed upon registration
            Manifest.permission.RECORD_AUDIO // Speaker accessory
    };
    private static final int REQUEST_PERMISSION_CODE = 12345;

    private TextView regStatus;
    private TextView regStatus2;
    private TextView sdkVersion;
    private ProgressBar regBar;
    private ProgressBar regBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        regStatus = findViewById(R.id.regStatus);
        regStatus2 = findViewById(R.id.regStatus2);
        sdkVersion = findViewById(R.id.sdkVersion);
        regBar = (ProgressBar) findViewById(R.id.progressBarReg);
        regBar2 = (ProgressBar) findViewById(R.id.progressBarReg2);
        //regBar2.setVisibility(View.INVISIBLE);

        sdkVersion.setText(DJISDKManager.getInstance().getSDKVersion());
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        // Check for permissions
        List<String> missingPermission = new ArrayList<>();
        for (String eachPermission : REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }
        // Request for missing permissions
        if (missingPermission.isEmpty()) {
            regStatus.setText("Registrando...");
            startSDKRegistration();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            regStatus.setText("Cheque as permissões do App");
            ActivityCompat.requestPermissions((Activity) this,
                    missingPermission.toArray(new String[missingPermission.size()]),
                    REQUEST_PERMISSION_CODE);
        }
    }

    public void registerApp(View view){
//        regStatus.setText("Checando permissoes...");
//        checkAndRequestPermissions();

    }
    public void abrirVideo(View view){
        Intent intent = new Intent(this, VideoFeedActivity.class);
        startActivity(intent);
    }

    public void abrirMissoes(View view){
        Intent intent = new Intent(this, CreateMission.class);
        startActivity(intent);
    }

    private void startSDKRegistration(){
        DJISDKManager.getInstance().getLDMManager().setModuleNetworkServiceEnabled(new LDMModule.Builder().moduleType(
                LDMModuleType.FIRMWARE_UPGRADE).enabled(false).build());

        DJISDKManager.getInstance().registerApp(this.getApplicationContext(), new DJISDKManager.SDKManagerCallback() {
            @Override
            public void onRegister(DJIError djiError) {
                if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                    regStatus.setText("Registrado");
                    regBar.setVisibility(View.INVISIBLE);
                    //regBar2.setVisibility(View.VISIBLE);
                    DJILog.e("App registration", DJISDKError.REGISTRATION_SUCCESS.getDescription());
                    DJISDKManager.getInstance().startConnectionToProduct();

                } else {
                    regStatus.setText(djiError.getDescription());
                }
                Log.v(TAG, djiError.getDescription());
            }
            @Override
            public void onProductDisconnect() {
                Log.d(TAG, "onProductDisconnect");
                regStatus2.setText("Desconectado");
            }
            @Override
            public void onProductConnect(BaseProduct baseProduct) {
                Log.d(TAG, String.format("onProductConnect newProduct:%s", baseProduct));

                Aircraft air = (Aircraft) baseProduct;
                air.getName(new CommonCallbacks.CompletionCallbackWith<String>() {
                    @Override
                    public void onSuccess(String s) {
                        String[] names = s.split("-");
                        String name = "UNK";
                        if(names.length >=2){
                            name = names[1];
                        }
                        regStatus2.setText("Conectado ("+name+")");
                        regBar2.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(DJIError djiError) {
                        regStatus2.setText("C8onectado - "+djiError.getDescription());
                    }
                });
            }

            @Override
            public void onProductChanged(BaseProduct baseProduct) {

            }

            @Override
            public void onComponentChange(BaseProduct.ComponentKey componentKey,
                                          BaseComponent oldComponent,
                                          BaseComponent newComponent) {
                Log.d(TAG,
                        String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s",
                                componentKey,
                                oldComponent,
                                newComponent));
            }

            @Override
            public void onInitProcess(DJISDKInitEvent djisdkInitEvent, int i) {

            }

            @Override
            public void onDatabaseDownloadProgress(long current, long total) {

            }
        });
    }

    // TEST mission
    public void onMissionControlClick(View view){
        Intent intent = new Intent(this, TestMissionModel.class);
        startActivity(intent);
    }
}