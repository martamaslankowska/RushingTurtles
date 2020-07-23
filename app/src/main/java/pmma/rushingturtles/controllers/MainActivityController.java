package pmma.rushingturtles.controllers;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.MainActivity;
import pmma.rushingturtles.enums.ButtonState;
import pmma.rushingturtles.enums.PlayerState;
import pmma.rushingturtles.websocket.WSC;
import pmma.rushingturtles.websocket.messages.ErrorMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.broadcast.GameReadyToStartMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.broadcast.RoomUpdateMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.fromserver.HelloClientMsg;

public class MainActivityController {
    private String playerName;
    private int playerId;
    private int playerIdx;
    private ButtonState buttonState;
    List<String> playersInTheRoomNames;

    private MainActivity mainActivity;
    private WSC wsc;

    private static final MainActivityController instance = new MainActivityController();
    public static MainActivityController getInstance() {
        return instance;
    }

    public MainActivityController() {
        this.playerName = null;
        this.mainActivity = null;
    }

    public void initializeMainController(MainActivity activity) {
        if (playerName == null) {
            wsc = WSC.getInstance();
            mainActivity = activity;
            buttonState = ButtonState.OTHER;
            SharedPreferences preferences = mainActivity.getSharedPreferences("settingsPreferences", 0);
            playerName = preferences.getString("player_name", "Marta xD");
            playerId = preferences.getInt("player_id", -1);
            playersInTheRoomNames = new ArrayList<>();
        }
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ButtonState getButtonState() {
        return buttonState;
    }

    public void setButtonState(ButtonState buttonState) {
        this.buttonState = buttonState;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public WSC getWsc() {
        return wsc;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(int playerIdx) {
        this.playerIdx = playerIdx;
    }

    public List<String> getPlayersInTheRoomNames() {
        return playersInTheRoomNames;
    }

    public void setPlayersInTheRoomNames(List<String> playersInTheRoomNames) {
        this.playersInTheRoomNames = playersInTheRoomNames;
    }

    /* WEB SOCKET MESSAGES */

    public void manageHelloClientMsg(final HelloClientMsg helloClientMsg) {
        playersInTheRoomNames = helloClientMsg.getListOfPlayersInTheRoom();
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (helloClientMsg.getStatus()) {
                    case "can create":
                        mainActivity.setButtonForStart();
                        buttonState = ButtonState.START;
                        break;
                    case "can join":
                        mainActivity.setButtonForJoin();
                        mainActivity.updateRoomWithPlayers(helloClientMsg.getListOfPlayersInTheRoom());
                        mainActivity.setListViewVisibility();
                        buttonState = ButtonState.JOIN;
                        break;
                    case "limit":
                        mainActivity.setButtonForInactive(ButtonState.LIMIT);
                        mainActivity.updateRoomWithPlayers(helloClientMsg.getListOfPlayersInTheRoom());
                        mainActivity.setListViewVisibility();
                        buttonState = ButtonState.LIMIT;
                        break;
                    case "ongoing":
                        mainActivity.setButtonForInactive(ButtonState.ONGOING);
                        mainActivity.updateRoomWithPlayers(helloClientMsg.getListOfPlayersInTheRoom());
                        mainActivity.setListViewVisibility();
                        buttonState = ButtonState.ONGOING;
                        break;
                    case "can resume":
                        mainActivity.setButtonForResume();
                        mainActivity.updateRoomWithPlayers(helloClientMsg.getListOfPlayersInTheRoom());
                        mainActivity.setListViewVisibility();
                        buttonState = ButtonState.RESUME;
                }
            }
        });
    }

    public void receiveGameReadyToStart(final GameReadyToStartMsg gameReadyToStartMsg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerIdx = gameReadyToStartMsg.getPlayerIdx();
                mainActivity.startTheGame();
            }
        });
    }

    public void receiveRoomUpdateMsg(final RoomUpdateMsg roomUpdateMsg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playersInTheRoomNames = roomUpdateMsg.getListOfPlayersInTheRoom();
                mainActivity.updateRoomWithPlayers();
                mainActivity.setListViewVisibility();
            }
        });
    }

    public void errorMessage(final ErrorMsg error) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("WebSocket ErrorMsg", error.getDescription());
                Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.toast_error) + "\n" + error.getDescription(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
