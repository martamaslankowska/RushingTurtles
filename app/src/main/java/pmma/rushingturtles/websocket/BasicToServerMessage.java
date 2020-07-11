package pmma.rushingturtles.websocket;

public class BasicToServerMessage {
    private String message;
    private int playerId;

    public BasicToServerMessage(String message, int playerId) {
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
