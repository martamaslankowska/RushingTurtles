package pmma.rushingturtles.objects;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private List<String> playersNames;
    private int activePlayerIdx;
    private Card recentlyPlayedCard;
    private MyPlayer myPlayer;

    public Game(Board board, List<String> playersNames, int activePlayerIdx, MyPlayer myPlayer) {
        this.board = board;
        this.playersNames = playersNames;
        this.activePlayerIdx = activePlayerIdx;
        this.myPlayer = myPlayer;
        this.recentlyPlayedCard = null;
    }

    public Game(Board board) {
        this.board = board;
        this.recentlyPlayedCard = null;
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
