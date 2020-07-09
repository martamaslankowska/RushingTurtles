package pmma.rushingturtles.objects;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.enums.TurtleColor;

public class Board {
    private List<List<TurtleColor>> turtlesInGamePositions;
    private List<List<TurtleColor>> turtlesOnStartPositions;

    public Board() {
        this.turtlesInGamePositions = new ArrayList<>();
        this.turtlesOnStartPositions = new ArrayList<>();
    }

    public List<List<TurtleColor>> getTurtlesInGamePositions() {
        return turtlesInGamePositions;
    }

    public void setTurtlesInGamePositions(List<List<TurtleColor>> turtlesInGamePositions) {
        this.turtlesInGamePositions = turtlesInGamePositions;
    }

    public List<List<TurtleColor>> getTurtlesOnStartPositions() {
        return turtlesOnStartPositions;
    }

    public void setTurtlesOnStartPositions(List<List<TurtleColor>> turtlesOnStartPositions) {
        this.turtlesOnStartPositions = turtlesOnStartPositions;
    }
}
