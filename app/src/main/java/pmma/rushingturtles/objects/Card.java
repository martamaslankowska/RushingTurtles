package pmma.rushingturtles.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import pmma.rushingturtles.enums.CardAction;
import pmma.rushingturtles.enums.TurtleColor;

public class Card {

    @JsonProperty("card_id")
    private int cardId;
    private TurtleColor color;
    private CardAction action;

    public Card() {
    }

    public Card(int cardId, TurtleColor color, CardAction action) {
        this.cardId = cardId;
        this.color = color;
        this.action = action;
    }

    public TurtleColor getColor() {
        return color;
    }

    public void setColor(TurtleColor color) {
        this.color = color;
    }

    public CardAction getAction() {
        return action;
    }

    public void setAction(CardAction action) {
        this.action = action;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}
