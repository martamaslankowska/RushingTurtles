package pmma.rushingturtles.websocket.messages.mainactivity.broadcast;

import com.fasterxml.jackson.annotation.JsonProperty;

import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class GameReadyToStartMsg extends BasicMsgFromServer {
    @JsonProperty("player_idx")
    private int playerIdx;

    public GameReadyToStartMsg() {
    }

    public GameReadyToStartMsg(String message, int playerIdx) {
        super(message);
        this.playerIdx = playerIdx;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public void setPlayerIdx(int playerIdx) {
        this.playerIdx = playerIdx;
    }
}
