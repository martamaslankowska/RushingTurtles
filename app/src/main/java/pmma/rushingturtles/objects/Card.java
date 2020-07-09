package pmma.rushingturtles.objects;

import pmma.rushingturtles.enums.CardAction;
import pmma.rushingturtles.enums.TurtleColor;

public class Card {
    private TurtleColor color;
    private CardAction action;

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

}
