package pmma.rushingturtles.websocket.messages.gameactivity.fromserver;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class CardsUpdatedMsg extends BasicMsgFromServer {

    @JsonProperty("player_cards")
    private List<Card> playerCards;

    public CardsUpdatedMsg() {
    }

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
