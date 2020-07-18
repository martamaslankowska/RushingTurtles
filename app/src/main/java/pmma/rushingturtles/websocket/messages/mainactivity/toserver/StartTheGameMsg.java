package pmma.rushingturtles.websocket.messages.mainactivity.toserver;

import pmma.rushingturtles.websocket.messages.BasicMsgToServer;

public class StartTheGameMsg extends BasicMsgToServer {

    private static final String MSG = "start the game";

    public StartTheGameMsg(int playerId) {
        super(MSG, playerId);
    }
}
