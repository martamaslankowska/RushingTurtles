package pmma.rushingturtles.websocket.messages.mainactivity.toserver;

import java.util.Random;

import pmma.rushingturtles.websocket.messages.BasicMsgToServer;

public class HelloServerMsg extends BasicMsgToServer {
    private static final String MSG = "hello server";
    private String playerName;

    public HelloServerMsg(int playerId, String playerName) {
        super(MSG, playerId);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
