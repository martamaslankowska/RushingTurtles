package pmma.rushingturtles.controllers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.enums.CardAction;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.objects.Board;
import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.objects.Game;
import pmma.rushingturtles.objects.MyPlayer;

public class GameActivityController {
    public Game game;

    private static final GameActivityController instance = new GameActivityController();
    public static GameActivityController getInstance() {
        return instance;
    }

    public GameActivityController() {
        Board board = new Board();
        List<Card> cards = Arrays.asList(new Card(1, TurtleColor.BLUE, CardAction.ARROW),
                new Card(33, TurtleColor.GREEN, CardAction.MINUS),
                new Card(21, TurtleColor.RAINBOW, CardAction.PLUS),
                new Card(13, TurtleColor.YELLOW, CardAction.ARROW_ARROW),
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



}
