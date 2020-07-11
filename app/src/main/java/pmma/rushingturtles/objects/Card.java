package pmma.rushingturtles.objects;

import pmma.rushingturtles.enums.CardAction;
import pmma.rushingturtles.enums.TurtleColor;

public class Card {
    private int cardId;
    private TurtleColor color;
    private CardAction action;

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
