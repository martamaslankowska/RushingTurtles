package pmma.rushingturtles.websocket.messages.gameactivity.broadcast;

import com.fasterxml.jackson.annotation.JsonProperty;

import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.objects.Board;
import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;

public class GameStateUpdatedMsg extends BasicMsgFromServer {

    private Board board;
    @JsonProperty("active_player_idx")
    private int activePlayerIdx;
    @JsonProperty("recently_played_card")
    private Card recentlyPlayedCard;

    public GameStateUpdatedMsg() {
    }

    public GameStateUpdatedMsg(String message, Board board, int activePlayerIdx, Card recentlyPlayedCard) {
        super(message);
        this.board = board;
        this.activePlayerIdx = activePlayerIdx;
        this.recentlyPlayedCard = recentlyPlayedCard;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
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

}
