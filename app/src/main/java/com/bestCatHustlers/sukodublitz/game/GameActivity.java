package com.bestCatHustlers.sukodublitz.game;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.GlobalSettingsInterface;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.main.MainActivity;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothService;
import com.bestCatHustlers.sukodublitz.results.ResultsActivity;
import com.bestCatHustlers.sukodublitz.settings.MainSettingsActivity;
import com.bestCatHustlers.sukodublitz.settings.MainSettingsModel;
import com.bestCatHustlers.sukodublitz.utils.ParcelableByteUtil;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements GameContract.View, GameBoardView.Delegate {
    //region Properties
    public static final String TAG = "GameActivity";

    private CardView scoreCardView;
    private TextView playerScore1TextView;
    private TextView playerScore2TextView;
    private Chronometer chronometer;
    private GameBoardView boardView;
    private TextView[] numberEntryButtons;

    private GameContract.Presenter presenter;

    private Constants constants;

    private BluetoothService mBluetoothService = null;
    private boolean mBounded;

    private class Constants {
        final float numberEntrySelectedAlpha = 0.6f;
        final float numberEntryDefaultAlpha = 0.9f;
    }

    //endregion

    //region Bluetooth

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(GameActivity.this, "Bluetooth Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            mBluetoothService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(GameActivity.this, "Bluetooth Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            BluetoothService.LocalBinder mLocalBinder = (BluetoothService.LocalBinder)service;
            mBluetoothService = mLocalBinder.getServerInstance();
            mBluetoothService.attachNewHandler(mHandler);
        }
    };

    // TODO: polish this part
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            presenter.handleBluetoothMessageReceived(msg);
        }
    };

    //endregion

    //region LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        scoreCardView = findViewById(R.id.scoreCardView);
        playerScore1TextView = findViewById(R.id.playerScore1TextView);
        playerScore2TextView = findViewById(R.id.playerScore2TextView);

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();

        numberEntryButtons = new TextView[9];
        numberEntryButtons[0] = findViewById(R.id.numberEntry1);
        numberEntryButtons[1] = findViewById(R.id.numberEntry2);
        numberEntryButtons[2] = findViewById(R.id.numberEntry3);
        numberEntryButtons[3] = findViewById(R.id.numberEntry4);
        numberEntryButtons[4] = findViewById(R.id.numberEntry5);
        numberEntryButtons[5] = findViewById(R.id.numberEntry6);
        numberEntryButtons[6] = findViewById(R.id.numberEntry7);
        numberEntryButtons[7] = findViewById(R.id.numberEntry8);
        numberEntryButtons[8] = findViewById(R.id.numberEntry9);

        constants = new Constants();

        boardView = (GameBoardView) findViewById(R.id.boardLayout);
        boardView.delegate = this;
        
        presenter = new GamePresenter(this, getIntent().getExtras());

        presenter.handleViewCreated();
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.handleViewStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.handleViewDestroyed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void onBackPressed(View view) {
        presenter.handleOnBackPressed();
    }

    public void openResultsActivity() {
        Intent intent = new Intent(this, ResultsActivity.class);

        presenter.prepareOpenResultsActivity(intent);

        startActivity(intent);
    }

    public void onClickNumberEntry(View view) {
        TextView button = (TextView) view;
        int value = Integer.parseInt(button.getText().toString());

        if (value < 0 || value > 9) return;

        presenter.handleNumberClick(value);
    }

    public void onSettingsPressed(View view) {
        Intent intent = new Intent(this, MainSettingsActivity.class);

        startActivity(intent);
    }

    //endregion

    //region Contract

    @Override
    public void bindBluetoothService() {
        Intent mIntent = new Intent(this, BluetoothService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void selectCell(final int row, final int column) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boardView.selectCell(row, column);
            }
        });
    }

    @Override
    public void selectNumber(int value) {
        if (value < 1 || value > 9) {
            for (TextView button : numberEntryButtons) {
                button.setAlpha(constants.numberEntryDefaultAlpha);
            }
        } else {
            for (int i = 0; i < numberEntryButtons.length; ++i) {
                float alpha = (i + 1 == value)
                        ? constants.numberEntrySelectedAlpha
                        : constants.numberEntryDefaultAlpha;

                numberEntryButtons[i].setAlpha(alpha);
            }
        }
    }

    @Override
    public void printScores(final int playerScore1, final int playerScore2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Set to strings file.
                playerScore1TextView.setText("Team Blue: " + playerScore1);
                playerScore2TextView.setText("Team Red: " + playerScore2);
            }
        });
    }

    @Override
    public void printBoard(final int[][] board, final String[][] cellOwners, final ArrayList<Player> bluePlayers, final ArrayList<Player> redPlayers) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               boardView.printBoard(board, cellOwners,redPlayers, bluePlayers);
            }
        });
    }

    @Override
    public void alertBackToMenu() {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        // TODO: Set to strings file.
        alert.setTitle("LEAVE GAME");
        alert.setMessage("Are you sure you wish to leave this game? All progress will be lost.");
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "STAY",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                    }
                });

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "LEAVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.handleLeaveGame();
                        dialogInterface.dismiss();

                        Context baseContext = getBaseContext();
                        Intent intent = new Intent(baseContext, MainActivity.class);

                        baseContext.startActivity(intent);

                        finish();
                    }
                });

        alert.show();
    }

    @Override
    public void alertEndOfGame(final String message) {
        final Context activityContext = this;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alert = new AlertDialog.Builder(activityContext).create();

                // TODO: Set to strings file.
                alert.setTitle("GAME OVER");
                alert.setMessage(message);
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                openResultsActivity();

                                finish();
                            }
                        });

                chronometer.stop();
                alert.show();
            }
        });

    }

    @Override
    public void playSound(int soundID) {
        GlobalSettingsInterface globalSettingsInterface = MainSettingsModel.getInstance();
        // Don't play sound if the sound is not enabled
        if (!globalSettingsInterface.isSoundEnabled()) return;
        final MediaPlayer soundPlayer = MediaPlayer.create(this, soundID);

        soundPlayer.start();
        soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                soundPlayer.release();
            }
        });
    }

    @Override
    public void showPoints(boolean shouldShowPoints) {
        scoreCardView.setVisibility(shouldShowPoints ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showTimer(boolean shouldShowTimer) {
        chronometer.setVisibility(shouldShowTimer ? View.VISIBLE : View.INVISIBLE);
    }

    //endregion

    //region BoardGameView.Delegate

    @Override
    public void gameBoardViewDidClick(int row, int column) {
        presenter.handleCellClick(row, column);
    }

    @Override
    public void sendBluetoothMessage(byte[] message) {
        if (mBluetoothService.getState() != BluetoothConstants.STATE_CONNECTED) {
            // TODO: Add to strings.
            Toast.makeText(this, "There was a problem with the bluetooth connection", Toast.LENGTH_SHORT).show();
            return;
        }

        mBluetoothService.write(message);
    }

    //endregion
}
