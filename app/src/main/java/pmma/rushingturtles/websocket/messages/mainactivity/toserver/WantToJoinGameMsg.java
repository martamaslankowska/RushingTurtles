package pmma.rushingturtles.websocket.messages.mainactivity.toserver;

import pmma.rushingturtles.websocket.messages.BasicMsgToServer;

public class WantToJoinGameMsg extends BasicMsgToServer {

    private static final String MSG = "want to join the game";
    private String status;

    public WantToJoinGameMsg(int playerId, String status) {
        super(MSG, playerId);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
