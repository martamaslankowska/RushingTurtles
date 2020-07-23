package pmma.rushingturtles.websocket.messages.mainactivity.toserver;

import pmma.rushingturtles.websocket.messages.BasicMsgToServer;

public class WantToJoinGameMsg extends BasicMsgToServer {

    private static final String MSG = "want to join the game";

    public WantToJoinGameMsg(int playerId) {
        super(MSG, playerId);
    }

}
