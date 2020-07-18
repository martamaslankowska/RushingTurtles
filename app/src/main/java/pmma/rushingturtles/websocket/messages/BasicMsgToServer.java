package pmma.rushingturtles.websocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasicMsgToServer {
    protected String message;
    @JsonProperty("player_id")
    protected int playerId;

    public BasicMsgToServer(String message, int playerId) {
        this.message = message;
        this.playerId = playerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

}
