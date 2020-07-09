package pmma.rushingturtles.objects;

import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<String> playersNames;
    private int activePlayerPosition;
    private Card recentlyPlayedCard;
    private MyPlayer myPlayer;

    public Game(Board board, ArrayList<String> playersNames, int activePlayerPosition, MyPlayer myPlayer) {
        this.board = board;
        this.playersNames = playersNames;
        this.activePlayerPosition = activePlayerPosition;
        this.myPlayer = myPlayer;
        this.recentlyPlayedCard = null;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<String> getPlayersNames() {
        return playersNames;
    }

    public void setPlayersNames(ArrayList<String> playersNames) {
        this.playersNames = playersNames;
    }

    public int getActivePlayerPosition() {
        return activePlayerPosition;
    }

    public void setActivePlayerPosition(int activePlayerPosition) {
        this.activePlayerPosition = activePlayerPosition;
    }

    public Card getRecentlyPlayedCard() {
        return recentlyPlayedCard;
    }

    public void setRecentlyPlayedCard(Card recentlyPlayedCard) {
        this.recentlyPlayedCard = recentlyPlayedCard;
    }

    public MyPlayer getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(MyPlayer myPlayer) {
        this.myPlayer = myPlayer;
    }
}
