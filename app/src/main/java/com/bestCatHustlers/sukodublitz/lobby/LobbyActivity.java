package com.bestCatHustlers.sukodublitz.lobby;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothService;
import com.bestCatHustlers.sukodublitz.bluetooth.DeviceListActivity;
import com.bestCatHustlers.sukodublitz.game.GameActivity;
import com.bestCatHustlers.sukodublitz.join.JoinActivity;
import com.bestCatHustlers.sukodublitz.utils.SerializableUtils;

public class LobbyActivity extends AppCompatActivity implements LobbyContract.View {
    private LobbyContract.Presenter presenter;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mBluetoothService = null;

    private TextView textStatus;
    private Button buttonStartGame;

    boolean mBounded;

    private Bundle extras;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        private String mConnectedDeviceName = null;

        @Override
        public void handleMessage(Message msg) {
            presenter.handleBluetoothMessageReceived(msg);
//            switch (msg.what) {
//                case BluetoothConstants.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) {
//                        case BluetoothConstants.STATE_CONNECTED:
//                            textStatus.setText(getString(R.string.lobby_title_connected, mConnectedDeviceName));
//                            break;
//                        case BluetoothConstants.STATE_CONNECTING:
//                            textStatus.setText(getString(R.string.lobby_title_connecting));
//                            break;
//                        case BluetoothConstants.STATE_LISTEN:
//                            textStatus.setText(getString(R.string.lobby_title_listen));
//                        case BluetoothConstants.STATE_NONE:
//                            textStatus.setText(getString(R.string.lobby_title_none));
//                            break;
//                    }
//                    break;
//                case BluetoothConstants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    // construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
//                    if (writeMessage.equals(START_GAME)) {
//                        openGameActivity();
//                    }
//                    break;
//                case BluetoothConstants.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    // construct a string from the valid bytes in the buffer
//                    Object obj = SerializableUtils.deserialize(readBuf);
//                    String readMessage = (String) obj;
//                    if (readMessage.equals(START_GAME)) {
//                        openGameActivity();
//                    }
//                    break;
//                case BluetoothConstants.MESSAGE_DEVICE_NAME:
//                    mConnectedDeviceName = msg.getData()
//                            .getString(BluetoothConstants.DEVICE_NAME);
//                    Log.d(TAG, "Connected to " + mConnectedDeviceName);
//                    break;
//            }
        }
    };

    //region LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);


        extras = getIntent().getExtras();
        presenter = new LobbyPresenter(this, extras);

        checkBluetoothSupport();

        textStatus = findViewById(R.id.text_show_lobby_status);
        buttonStartGame = findViewById(R.id.button_lobby_start_game);

        presenter.handleViewCreated();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mBluetoothService == null) {
            Intent btServiceIntent = new Intent(this, BluetoothService.class);
            bindService(btServiceIntent, mConnection, BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(LobbyActivity.this, "Bluetooth Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            mBluetoothService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(LobbyActivity.this, "Bluetooth Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            BluetoothService.LocalBinder mLocalBinder = (BluetoothService.LocalBinder)service;
            mBluetoothService = mLocalBinder.getServerInstance();

            mBluetoothService.attachNewHandler(mHandler);

            presenter.handleOnBluetoothServiceConnected();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        stopBluetoothService();
    };

    public void onBackPressed(View view) {
        stopBluetoothService();
        super.onBackPressed();
    }

    public void onStartGamePressed(View view) {
        presenter.handleStartGamePressed();
    }

    public void checkBluetoothSupport() {
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
    }

    public void stopBluetoothService() {
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    //endregion

    //region Contract

    @Override
    public void sendBluetoothMessage(byte[] message) {
        mBluetoothService.write(message);
    }

    @Override
    public void setStatusText(String text) {
        textStatus.setText(text);
    }

    @Override
    public void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);

        presenter.prepareOpenGameActivity(intent);

        startActivity(intent);
    }

    @Override
    public void hostBluetoothService() {
        mBluetoothService.host();
    }

    @Override
    public void setStartGameVisibility(int visibility) {
        buttonStartGame.setVisibility(visibility);
    }

    @Override
    public void openDiscoverableAlert(int durationSeconds) {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    //endregion

    //region Private

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothConstants.STATE_CONNECTED) {
            Toast.makeText(this, "Not connected to a device", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = SerializableUtils.serialize(message);
            mBluetoothService.write(send);
        }
    }

    //endregion
}
