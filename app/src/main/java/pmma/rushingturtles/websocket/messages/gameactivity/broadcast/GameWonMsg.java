package pmma.rushingturtles.websocket.messages.gameactivity.broadcast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class GameWonMsg extends BasicMsgFromServer {

    @JsonProperty("winner_name")
    private String winnerName;
    @JsonProperty("players_places")
    private List<Integer> playersPlaces;
    @JsonProperty("players_turtle_colors")
    private List<TurtleColor> playersTurtleColors;

    public GameWonMsg() {
    }

    public GameWonMsg(String message, String winnerName, List<Integer> playersPlaces, List<TurtleColor> playersTurtleColors) {
        super(message);
        this.winnerName = winnerName;
        this.playersPlaces = playersPlaces;
        this.playersTurtleColors = playersTurtleColors;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public List<Integer> getPlayersPlaces() {
        return playersPlaces;
    }

    public void setPlayersPlaces(List<Integer> playersPlaces) {
        this.playersPlaces = playersPlaces;
    }

    public List<TurtleColor> getPlayersTurtleColors() {
        return playersTurtleColors;
    }

    public void setPlayersTurtleColors(List<TurtleColor> playersTurtleColors) {
        this.playersTurtleColors = playersTurtleColors;
    }

}
