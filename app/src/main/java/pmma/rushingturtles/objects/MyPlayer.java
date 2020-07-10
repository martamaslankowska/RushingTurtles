package pmma.rushingturtles.objects;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.enums.TurtleColor;

public class MyPlayer {
    private int id;
    private String name;
    private List<Card> cards;
    private TurtleColor turtle;
    private int activeCardPosition;

    public MyPlayer(int id, String name) {
        this.id = id;
        this.name = name;
        this.cards = new ArrayList<>();
        this.turtle = null;
        this.activeCardPosition = -1;
    }

    public MyPlayer(int id, String name, List<Card> cards, TurtleColor turtle) {
        this(id, name);
        this.cards = cards;
        this.turtle = turtle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public TurtleColor getTurtle() {
        return turtle;
    }

    public void setTurtle(TurtleColor turtle) {
        this.turtle = turtle;
    }

    public int getActiveCardPosition() {
        return activeCardPosition;
    }

    public void setActiveCardPosition(int activeCardPosition) {
        this.activeCardPosition = activeCardPosition;
    }
}
