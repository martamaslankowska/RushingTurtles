package pmma.rushingturtles.websocket.messages.gameactivity.fromserver;

import java.util.List;

import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.objects.Board;
import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class FullGameStateMsg extends BasicMsgFromServer {

    private Board board;
    private List<String> playersNames;
    private int activePlayerIdx;
    private List<Card> playerCards;
    private TurtleColor turtleColor;
    private Card recentlyPlayedCard;

    public FullGameStateMsg(String message, Board board, List<String> playersNames, int activePlayerIdx, List<Card> playerCards, TurtleColor turtleColor, Card recentlyPlayedCard) {
        super(message);
        this.board = board;
        this.playersNames = playersNames;
        this.activePlayerIdx = activePlayerIdx;
        this.playerCards = playerCards;
        this.turtleColor = turtleColor;
        this.recentlyPlayedCard = recentlyPlayedCard;
    }



    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<String> getPlayersNames() {
        return playersNames;
    }

    public void setPlayersNames(List<String> playersNames) {
        this.playersNames = playersNames;
    }

    public int getActivePlayerIdx() {
        return activePlayerIdx;
    }

    public void setActivePlayerIdx(int activePlayerIdx) {
        this.activePlayerIdx = activePlayerIdx;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public TurtleColor getTurtleColor() {
        return turtleColor;
    }

    public void setTurtleColor(TurtleColor turtleColor) {
        this.turtleColor = turtleColor;
    }

    public Card getRecentlyPlayedCard() {
        return recentlyPlayedCard;
    }

    public void setRecentlyPlayedCard(Card recentlyPlayedCard) {
        this.recentlyPlayedCard = recentlyPlayedCard;
    }


}
