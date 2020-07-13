package pmma.rushingturtles.objects;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.enums.TurtleColor;

public class MyPlayer {
    private int idx;
    private String name;
    private List<Card> cards;
    private TurtleColor turtle;
    private int activeCardPosition;

    public MyPlayer(int idx, String name) {
        this.idx = idx;
        this.name = name;
        this.cards = new ArrayList<>();
        this.turtle = null;
        this.activeCardPosition = -1;
    }

    public MyPlayer(int idx, String name, List<Card> cards, TurtleColor turtle) {
        this(idx, name);
        this.cards = cards;
        this.turtle = turtle;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
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
