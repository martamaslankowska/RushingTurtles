package pmma.rushingturtles.controllers;

import android.content.SharedPreferences;

import pmma.rushingturtles.activities.MainActivity;
import pmma.rushingturtles.enums.PlayerState;
import pmma.rushingturtles.websocket.WSC;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.CardsUpdatedMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.fromserver.HelloClientMsg;

public class MainActivityController {
    private String playerName;
    private int playerId;
    private int playerIdx;
    private PlayerState playerState;
    private MainActivity mainActivity;
    private WSC wsc;

    private static final MainActivityController instance = new MainActivityController();
    public static MainActivityController getInstance() {
        return instance;
    }

    public MainActivityController() {
        this.playerName = null;
    }

    public void initializeMainController(MainActivity activity) {
        if (playerName == null) {
            wsc = WSC.getInstance();
            mainActivity = activity;
            playerState = PlayerState.OTHER;
            SharedPreferences preferences = mainActivity.getSharedPreferences("settingsPreferences", 0);
            playerName = preferences.getString("player_name", "Marta xD");
            playerId = preferences.getInt("player_id", -1);
        }
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
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

    /* WEB SOCKET MESSAGES */

    public void manageHelloClientMessage(HelloClientMsg helloClientMsg) {
        switch(helloClientMsg.getStatus()) {
            case "can create":

        }
    }



}
