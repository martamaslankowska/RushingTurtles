package pmma.rushingturtles.websocket.messages.gameactivity.broadcast;

import java.util.List;

import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class GameWonMsg extends BasicMsgFromServer {

    private String winnerName;
    private List<Integer> playersPlaces;
    private List<TurtleColor> playersTurtleColors;

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
