package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;
import android.os.Message;

import com.bestCatHustlers.sukodublitz.Player;

import java.util.ArrayList;

public class GameContract {
    interface View {
        void bindBluetoothService();

        void selectCell(int row, int column);

        void selectNumber(int value);

        void printScores(int playerScore1, int playerScore2);

        void printBoard(int[][] board, String[][] cellOwners, ArrayList<Player> bluePlayers, ArrayList<Player> redPlayers);

        void alertBackToMenu();

        void alertEndOfGame(String message);

        void playSound(int soundID);

        void showPoints(boolean shouldShowPoints);

        void showTimer(boolean shouldShowTimer);

        void sendBluetoothMessage(byte[] message);

        void setTeamColor(int color);
    }

    interface Presenter {
        void handleViewCreated();
      
        void handleViewStarted();

        void handleViewDestroyed();

        void handleCellClick(int row, int column);

        void handleNumberClick(int number);

        void handleOnBackPressed();

        void handleLeaveGame();

        void prepareOpenResultsActivity(Intent intent);

        void handleBluetoothMessageReceived(Message message);

        void handleOpenSettings(boolean isPause);
    }
}
