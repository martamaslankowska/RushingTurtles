package pmma.rushingturtles.websocket.messages.gameactivity.toserver;

import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.messages.BasicMsgToServer;

public class PlayCardMsg extends BasicMsgToServer {

    private static final String MSG = "play card";
    private int cardId;
    private TurtleColor cardColor;

    public PlayCardMsg(int playerId, int cardId, TurtleColor cardColor) {
        super(MSG, playerId);
        this.cardId = cardId;
        this.cardColor = cardColor;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public TurtleColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(TurtleColor cardColor) {
        this.cardColor = cardColor;
    }
}
