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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothService;
import com.bestCatHustlers.sukodublitz.game.GameActivity;
import com.bestCatHustlers.sukodublitz.utils.SerializableUtils;

public class LobbyActivity extends AppCompatActivity implements LobbyContract.View {
    private LobbyContract.Presenter presenter;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothService mBluetoothService = null;

    private TextView textStatus;
    private Button buttonToggleTeam;
    private Button buttonStartGame;

    boolean mBounded;

    private Bundle extras;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        private String mConnectedDeviceName = null;

        @Override
        public void handleMessage(Message msg) {
            presenter.handleBluetoothMessageReceived(msg);
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
        buttonToggleTeam = findViewById(R.id.button_lobby_toggle_team);
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

    public void onToggleTeamPressed(View view) {
        presenter.handleToggleTeamPressed();
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
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothConstants.STATE_CONNECTED) {
            Toast.makeText(this, "Not connected to a device", Toast.LENGTH_SHORT).show();
            return;
        }

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

    @Override
    public void setToggleTeamButton(int color, String text) {
        buttonToggleTeam.setBackgroundTintList(ContextCompat.getColorStateList(this, color));
        buttonToggleTeam.setText(text);
    }

    //endregion
}
