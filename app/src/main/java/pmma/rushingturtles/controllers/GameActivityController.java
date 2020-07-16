package pmma.rushingturtles.controllers;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.enums.CardAction;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.objects.Board;
import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.objects.Game;
import pmma.rushingturtles.objects.MyPlayer;
import pmma.rushingturtles.objects.TurtleOnBoardPosition;

public class GameActivityController {
    public Game game;

    private static final GameActivityController instance = new GameActivityController();
    public static GameActivityController getInstance() {
        return instance;
    }

    public GameActivityController() {

        List<List<TurtleColor>> turtlesOnStartPositions = new ArrayList<>();
        List<List<TurtleColor>> turtlesInGamePositions = new ArrayList<>();
        for (int i=0; i<9; i++)
            turtlesInGamePositions.add(new ArrayList<TurtleColor>());
        turtlesInGamePositions.set(3, Arrays.asList(TurtleColor.YELLOW));
        turtlesInGamePositions.set(4, Arrays.asList(TurtleColor.RED, TurtleColor.BLUE));
        turtlesInGamePositions.set(5, Arrays.asList(TurtleColor.PURPLE));
        turtlesInGamePositions.set(6, Arrays.asList(TurtleColor.GREEN));
        Board board = new Board(turtlesOnStartPositions, turtlesInGamePositions);

        List<Card> cards = Arrays.asList(new Card(1, TurtleColor.BLUE, CardAction.PLUS_PLUS),
                new Card(33, TurtleColor.GREEN, CardAction.MINUS),
                new Card(21, TurtleColor.RAINBOW, CardAction.ARROW_ARROW),
                new Card(13, TurtleColor.YELLOW, CardAction.PLUS),
                new Card(11, TurtleColor.RAINBOW, CardAction.MINUS));
        MyPlayer player = new MyPlayer(0, "Marta1", cards, TurtleColor.YELLOW);

        game = new Game(board, Arrays.asList("Marta1", "Marta2", "Marta3"), 0, player);
    }

    public GameActivityController(Board board, ArrayList<String> playersNames, int activePlayerIdx, int myPlayerId, String myPlayerName, List<Card> cards, TurtleColor turtleColor) {
        MyPlayer player = new MyPlayer(myPlayerId, myPlayerName, cards, turtleColor);
        this.game = new Game(board, playersNames, activePlayerIdx, player);
    }


    public boolean isPlayerAnActivePlayer() {
        return game.getActivePlayerIdx() == game.getMyPlayer().getIdx();
    }

    public TurtleColor getCardColor(int cardIdx) {
        return game.getMyPlayer().getCards().get(cardIdx).getColor();
    }

    public int getCardId(int cardIdx) {
        return game.getMyPlayer().getCards().get(cardIdx).getCardId();
    }

    public String getCurrentPlayerName() {
        return game.getPlayersNames().get(game.getActivePlayerIdx());
    }

    public String getNextPlayerName() {
        int currentPlayerIdx = game.getActivePlayerIdx();
        int nextPlayerIdx = game.getActivePlayerIdx() == game.getPlayersNames().size() - 1 ? 0 : currentPlayerIdx + 1;
        return game.getPlayersNames().get(nextPlayerIdx);
    }

    public List<TurtleOnBoardPosition> getTurtlesOnBoardPositions(List<TurtleColor> turtleColors) {
        game.getBoard().setTurtlePositions();
        List<TurtleOnBoardPosition> turtlePositions = game.getBoard().getTurtlePositions();
        List<TurtleOnBoardPosition> turtleOnBoardPositions = new ArrayList<>();
        for (TurtleColor turtleColor : turtleColors)
            turtleOnBoardPositions.add(turtlePositions.get(findTurtlePositionInTurtlesPositions(turtleColor, turtlePositions)));
        return turtleOnBoardPositions;
    }

    public TurtleOnBoardPosition getTurtleOnBoardPosition(TurtleColor turtleColor) {
        game.getBoard().setTurtlePositions();
        List<TurtleOnBoardPosition> turtlePositions = game.getBoard().getTurtlePositions();
        return turtlePositions.get(findTurtlePositionInTurtlesPositions(turtleColor, turtlePositions));
    }

    private int findTurtlePositionInTurtlesPositions(TurtleColor turtleColor, List<TurtleOnBoardPosition> turtlePositions) {
        int turtlePosition = -1;
        for (int i=0; i<turtlePositions.size(); i++)
            if (turtlePositions.get(i).getTurtleColor() == turtleColor)
                turtlePosition = i;
        return turtlePosition;
    }

    public void getTurtleImageViewSortedByStackPositions(List<TurtleColor> turtleColors, List<ImageView> turtleImageViews) {


    }

}
