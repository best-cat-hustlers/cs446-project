package com.bestCatHustlers.sukodublitz.lobby;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bestCatHustlers.sukodublitz.GameSetupActivity;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothService;
import com.bestCatHustlers.sukodublitz.bluetooth.DeviceListActivity;
import com.bestCatHustlers.sukodublitz.game.GameActivity;
import com.bestCatHustlers.sukodublitz.join.JoinActivity;

import static com.bestCatHustlers.sukodublitz.GameSetupActivity.EXTRAS_KEY_AI_DIFFICULTY;
import static com.bestCatHustlers.sukodublitz.GameSetupActivity.EXTRAS_KEY_IS_HOST;
import static com.bestCatHustlers.sukodublitz.GameSetupActivity.EXTRAS_KEY_PENALTY_ON;
import static com.bestCatHustlers.sukodublitz.GameSetupActivity.EXTRAS_KEY_SHOW_POINTS;
import static com.bestCatHustlers.sukodublitz.GameSetupActivity.EXTRAS_KEY_SHOW_TIMER;

public class LobbyActivity extends AppCompatActivity {

    public static final String TAG = "LobbyActivity";

    public static final String EXTRAS_KEY_IS_MULTI = "isMultiplayer";

    private static final String START_GAME = "Start Game";

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mBluetoothService = null;

    private TextView textStatus;
    private Button buttonStartGame;

    private boolean isHost = false;
    boolean mBounded;
    private Bundle extras;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        private String mConnectedDeviceName = null;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothConstants.STATE_CONNECTED:
                            Log.d(TAG, "BluetoothService state is CONNECTED");
                            textStatus.setText(getString(R.string.lobby_title_connected, mConnectedDeviceName));
                            break;
                        case BluetoothConstants.STATE_CONNECTING:
                            Log.d(TAG, "BluetoothService state is CONNECTING");
                            textStatus.setText(getString(R.string.lobby_title_connecting));
                            break;
                        case BluetoothConstants.STATE_LISTEN:
                            Log.d(TAG, "BluetoothService state is LISTEN");
                            textStatus.setText(getString(R.string.lobby_title_listen));
                        case BluetoothConstants.STATE_NONE:
                            Log.d(TAG, "BluetoothService state is NONE");
                            textStatus.setText(getString(R.string.lobby_title_none));
                            break;
                    }
                    break;
                case BluetoothConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    if (writeMessage.equals(START_GAME)) {
                        openGameActivity();
                    }
                    break;
                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if (readMessage.equals(START_GAME)) {
                        openGameActivity();
                    }
                    break;
                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData()
                            .getString(BluetoothConstants.DEVICE_NAME);
                    Log.d(TAG, "Connected to " + mConnectedDeviceName);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        extras = getIntent().getExtras();

        isHost = extras.getBoolean(EXTRAS_KEY_IS_HOST);

        checkBluetoothSupport();

        textStatus = findViewById(R.id.text_show_lobby_status);
        buttonStartGame = findViewById(R.id.button_lobby_start_game);

        if (isHost) {
            ensureDiscoverable();
        } else {
            buttonStartGame.setVisibility(View.INVISIBLE);
        }
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

            if (isHost) {
                mBluetoothService.host();
            }
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
        if (isHost) {
            sendMessage(START_GAME);
        }
    }

    public void openGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRAS_KEY_IS_MULTI,true);
        intent.putExtra(EXTRAS_KEY_SHOW_POINTS, extras.getBoolean(EXTRAS_KEY_SHOW_POINTS));
        intent.putExtra(EXTRAS_KEY_SHOW_TIMER, extras.getBoolean(EXTRAS_KEY_SHOW_TIMER));
        intent.putExtra(EXTRAS_KEY_PENALTY_ON, extras.getBoolean(EXTRAS_KEY_PENALTY_ON));
        intent.putExtra(EXTRAS_KEY_AI_DIFFICULTY, extras.getInt(EXTRAS_KEY_AI_DIFFICULTY));
        // TODO: add other activities
        startActivity(intent);
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
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

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothConstants.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send, BluetoothConstants.MESSAGE_WRITE);
        }
    }
}
