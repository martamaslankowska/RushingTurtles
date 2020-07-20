package pmma.rushingturtles.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.enums.TurtleColor;

public class Board {
    @JsonProperty("turtles_in_game_positions")
    private List<List<TurtleColor>> turtlesInGamePositions;
    @JsonProperty("turtles_on_start_positions")
    private List<List<TurtleColor>> turtlesOnStartPositions;
    private List<TurtleOnBoardPosition> turtlePositions;

    public Board() {
        this.turtlesInGamePositions = new ArrayList<>();
        this.turtlesOnStartPositions = new ArrayList<>();
        this.turtlePositions = new ArrayList<>();
    }

    public Board(List<List<TurtleColor>> turtlesOnStartPositions) {
        this.turtlesOnStartPositions = turtlesOnStartPositions;
        this.turtlePositions = new ArrayList<>();
    }

    public Board(List<List<TurtleColor>> turtlesOnStartPositions, List<List<TurtleColor>> turtlesInGamePositions) {
        this.turtlesInGamePositions = turtlesInGamePositions;
        this.turtlesOnStartPositions = turtlesOnStartPositions;
        this.turtlePositions = new ArrayList<>();
    }

    public void setTurtlePositions() {
        this.turtlePositions = new ArrayList<>();
        for (int i=0; i<turtlesOnStartPositions.size(); i++)
            for (int j=0; j<turtlesOnStartPositions.get(i).size(); j++) {
                TurtleColor turtle = turtlesOnStartPositions.get(i).get(j);
                turtlePositions.add(new TurtleOnBoardPosition(turtle, new int[] {i, j}));
            }
        for (int i=0; i<turtlesInGamePositions.size(); i++)
            for (int j=0; j<turtlesInGamePositions.get(i).size(); j++) {
                TurtleColor turtle = turtlesInGamePositions.get(i).get(j);
                turtlePositions.add(new TurtleOnBoardPosition(turtle, i+1, j));
            }
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

    public List<TurtleOnBoardPosition> getTurtlePositions() {
        return turtlePositions;
    }

}
