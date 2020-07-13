package pmma.rushingturtles.websocket.messages.gameactivity.fromserver;

import java.util.List;

import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class CardsUpdatedMsg extends BasicMsgFromServer {

    private List<Card> playerCards;

    public CardsUpdatedMsg(String message, List<Card> playerCards) {
        super(message);
        this.playerCards = playerCards;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
    }
}
