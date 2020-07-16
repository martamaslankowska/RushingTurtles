package pmma.rushingturtles.objects;

import pmma.rushingturtles.enums.TurtleColor;

public class TurtleOnBoardPosition implements Comparable<TurtleOnBoardPosition> {
    private TurtleColor turtleColor;
    private int rock;
    private int position;  // zero means he is the lowest turtle
    private int[] positionOnTheStartRock;

    public TurtleOnBoardPosition(TurtleColor turtleColor, int[] positionOnTheStartRock) {
        this.turtleColor = turtleColor;
        this.rock = 0;
        this.positionOnTheStartRock = positionOnTheStartRock;
    }

    public TurtleOnBoardPosition(TurtleColor turtleColor, int rock, int position) {
        this.turtleColor = turtleColor;
        this.rock = rock;
        this.position = position;
        this.positionOnTheStartRock = new int[0];
    }

    public TurtleColor getTurtleColor() {
        return turtleColor;
    }

    public void setTurtleColor(TurtleColor turtleColor) {
        this.turtleColor = turtleColor;
    }

    public int getRock() {
        return rock;
    }

    public void setRock(int rock) {
        this.rock = rock;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int[] getPositionOnTheStartRock() {
        return positionOnTheStartRock;
    }

    public void setPositionOnTheStartRock(int[] positionOnTheStartRock) {
        this.positionOnTheStartRock = positionOnTheStartRock;
    }

    @Override
    public int compareTo(TurtleOnBoardPosition turtleOnBoardPosition) {
        return this.getTurtleColor().compareTo(turtleOnBoardPosition.getTurtleColor());
    }
}
