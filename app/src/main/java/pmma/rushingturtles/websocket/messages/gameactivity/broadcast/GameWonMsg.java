package pmma.rushingturtles.websocket.messages.gameactivity.broadcast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class GameWonMsg extends BasicMsgFromServer {

    @JsonProperty("winner_name")
    private String winnerName;
    @JsonProperty("sorted_list_of_player_places")
    private List<String> playersNamesPlaces;
    @JsonProperty("sorted_list_of_players_turtle_colors")
    private List<TurtleColor> playersTurtleColors;

    public GameWonMsg() {
    }

    public GameWonMsg(String message, String winnerName, List<String> playersNamesPlaces, List<TurtleColor> playersTurtleColors) {
        super(message);
        this.winnerName = winnerName;
        this.playersNamesPlaces = playersNamesPlaces;
        this.playersTurtleColors = playersTurtleColors;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public List<String> getPlayersNamesPlaces() {
        return playersNamesPlaces;
    }

    public void setPlayersNamesPlaces(List<String> playersNamesPlaces) {
        this.playersNamesPlaces = playersNamesPlaces;
    }

    public List<TurtleColor> getPlayersTurtleColors() {
        return playersTurtleColors;
    }

    public void setPlayersTurtleColors(List<TurtleColor> playersTurtleColors) {
        this.playersTurtleColors = playersTurtleColors;
    }

}
