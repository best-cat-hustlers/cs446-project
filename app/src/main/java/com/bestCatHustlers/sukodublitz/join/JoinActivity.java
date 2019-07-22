package com.bestCatHustlers.sukodublitz.join;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothService;
import com.bestCatHustlers.sukodublitz.bluetooth.DeviceListActivity;
import com.bestCatHustlers.sukodublitz.game.GameActivity;
import com.bestCatHustlers.sukodublitz.lobby.LobbyActivity;
import com.bestCatHustlers.sukodublitz.R;

public class JoinActivity extends AppCompatActivity implements JoinContract.View {

    public static final String TAG = "JoinActivity";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mBluetoothService = null;
    boolean mBounded;

    private JoinContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);

        presenter = new JoinPresenter(this);

        presenter.handleViewCreated();
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.handleViewStarted();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(JoinActivity.this, "Bluetooth Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            mBluetoothService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(JoinActivity.this, "Bluetooth Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            BluetoothService.LocalBinder mLocalBinder = (BluetoothService.LocalBinder)service;
            mBluetoothService = mLocalBinder.getServerInstance();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        presenter.handleViewStopped();
    };

    public void onBackPressed(View view) {
        presenter.handleOnBackPressed();

        super.onBackPressed();
    }

    public void onFindDevicePressed(View view) {
        presenter.handleFindDevicePressed();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                    Log.d(TAG, "Bluetooth device connected");
                    openLobbyActivity();
                }
                break;
        }
    }

    public void openLobbyActivity() {
        Intent intent = new Intent(this, LobbyActivity.class);

        presenter.prepareOpenLobbyActivity(intent);

        startActivity(intent);
    }

    /**
     * Establish connection with other device
     */
    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device);
    }


    // Contract functions

    @Override
    public void checkBluetoothSupport() {
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setupBluetoothSession() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the bluetooth session
        } else if (mBluetoothService == null) {
            Intent btServiceIntent = new Intent(this, BluetoothService.class);
            bindService(btServiceIntent, mConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void stopBluetoothService() {
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    @Override
    public void findDevices() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }
}
