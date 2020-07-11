package pmma.rushingturtles.controllers;

import java.util.ArrayList;
import java.util.List;

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

    public GameActivityController() {}

    public GameActivityController(Board board, ArrayList<String> playersNames, int activePlayerIdx, int myPlayerId, String myPlayerName, List<Card> cards, TurtleColor turtleColor) {
        MyPlayer player = new MyPlayer(myPlayerId, myPlayerName, cards, turtleColor);
        this.game = new Game(board, playersNames, activePlayerIdx, player);
    }

    

}
