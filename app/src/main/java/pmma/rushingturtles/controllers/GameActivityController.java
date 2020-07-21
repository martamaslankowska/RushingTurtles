package pmma.rushingturtles.controllers;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.enums.CardAction;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.objects.Board;
import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.objects.Game;
import pmma.rushingturtles.objects.MyPlayer;
import pmma.rushingturtles.objects.TurtleOnBoardPosition;
import pmma.rushingturtles.websocket.messages.ErrorMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameStateUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameWonMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.CardsUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.FullGameStateMsg;

public class GameActivityController {
    public Game game;
    private int myPlayerIdx;
    private String myPlayerName;
    private GameActivity gameActivity;
    private boolean gameEnded;

    private static final GameActivityController instance = new GameActivityController();
    public static GameActivityController getInstance() {
        return instance;
    }

    public void setGameActivityVariables(int myPlayerIdx, String myPlayerName, GameActivity gameActivity) {
        this.myPlayerIdx = myPlayerIdx;
        this.myPlayerName = myPlayerName;
        this.gameActivity = gameActivity;
    }

    public GameActivityController() {
        List<List<TurtleColor>> turtlesOnStartPositions = new ArrayList<>();
        List<List<TurtleColor>> turtlesInGamePositions = new ArrayList<>();
        turtlesOnStartPositions.add(Arrays.asList(TurtleColor.RED));
        turtlesOnStartPositions.add(Arrays.asList(TurtleColor.GREEN));
        turtlesOnStartPositions.add(Arrays.asList(TurtleColor.YELLOW));
        turtlesOnStartPositions.add(Arrays.asList(TurtleColor.PURPLE));
        turtlesOnStartPositions.add(Arrays.asList(TurtleColor.BLUE));

        Board board = new Board(turtlesOnStartPositions, turtlesInGamePositions);
        game = new Game(board);
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

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public Card getCard(int cardIdx) {
        return game.getMyPlayer().getCards().get(cardIdx);
    }

    public String getNextPlayerName() {
        int currentPlayerIdx = game.getActivePlayerIdx();
        int nextPlayerIdx = game.getActivePlayerIdx() == game.getPlayersNames().size() - 1 ? 0 : currentPlayerIdx + 1;
        return game.getPlayersNames().get(nextPlayerIdx);
    }

    public boolean isPickedCardRainbow(int cardIdx) {
        return getCardColor(cardIdx) == TurtleColor.RAINBOW;
    }

    public List<TurtleOnBoardPosition> getTurtlesOnBoardPositions(List<TurtleColor> turtleColors) {
        game.getBoard().setTurtlePositions();
        List<TurtleOnBoardPosition> turtlePositions = game.getBoard().getTurtlePositions();
        List<TurtleOnBoardPosition> turtleOnBoardPositions = new ArrayList<>();
        for (TurtleColor turtleColor : turtleColors)
            turtleOnBoardPositions.add(turtlePositions.get(findTurtlePositionInTurtlesPositions(turtleColor, turtlePositions)));
        return turtleOnBoardPositions;
    }

    public int getTurtleOnBoardPositionRock(TurtleColor turtleColor) {
        game.getBoard().setTurtlePositions();
        List<TurtleOnBoardPosition> turtlePositions = game.getBoard().getTurtlePositions();
        try {
            TurtleOnBoardPosition turtlePosition = turtlePositions.get(findTurtlePositionInTurtlesPositions(turtleColor, turtlePositions));
            return turtlePosition.getRock();
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public boolean isTurtleGoingBackFromStart(int pickedIdx) {
        TurtleColor pickedColor = getCardColor(pickedIdx);
        return isTurtleGoingBackFromStart(pickedIdx, pickedColor);
    }

    public boolean isTurtleGoingBackFromStart(int pickedIdx, TurtleColor pickedColor) {
        boolean firstCondition = getTurtleOnBoardPositionRock(pickedColor) == 0;
        boolean secondCondition = getCard(pickedIdx).getAction() == CardAction.MINUS;
        return firstCondition && secondCondition;
    }

    private int findTurtlePositionInTurtlesPositions(TurtleColor turtleColor, List<TurtleOnBoardPosition> turtlePositions) {
        int turtlePosition = -1;
        for (int i=0; i<turtlePositions.size(); i++)
            if (turtlePositions.get(i).getTurtleColor() == turtleColor)
                turtlePosition = i;
        return turtlePosition;
    }

    public List<ImageView> getTurtleImageViewSortedByStackPositionsOnStartRock(List<ImageView> turtleImageViews, List<TurtleOnBoardPosition> turtlePositions) {
        int noOfRocks = 5;
        int noOfPositions = 5;
        Object[][] turtlePositionStack = new Object[noOfRocks][noOfPositions];
        for (int i=0; i<turtlePositions.size(); i++) {
            TurtleOnBoardPosition pos = turtlePositions.get(i);
            if (pos.getRock() == 0)
                turtlePositionStack[pos.getPositionOnTheStartRock()[0]][pos.getPositionOnTheStartRock()[1]] = turtleImageViews.get(i);
        }
        List<ImageView> turtlesOrder = new ArrayList<>();
        for (int i=0; i<noOfPositions; i++)
            for (int j=0; j<noOfRocks; j++)
                if (turtlePositionStack[j][i] != null)
                    turtlesOrder.add((ImageView) turtlePositionStack[j][i]);

        return turtlesOrder;
    }

    public List<ImageView> getTurtleImageViewSortedByStackPositionsInGame(List<ImageView> turtleImageViews, List<TurtleOnBoardPosition> turtlePositions) {
        int noOfRocks = 9;
        int noOfPositions = 5;
        Object[][] turtlePositionStack = new Object[noOfRocks][noOfPositions];
        for (int i=0; i<turtlePositions.size(); i++) {
            TurtleOnBoardPosition pos = turtlePositions.get(i);
            if (pos.getRock() != 0)
                turtlePositionStack[pos.getRock() - 1][pos.getPosition()] = turtleImageViews.get(i);
        }
        List<ImageView> turtlesOrder = new ArrayList<>();
        for (int i=0; i<noOfPositions; i++)
            for (int j=0; j<noOfRocks; j++)
                if (turtlePositionStack[j][i] != null)
                    turtlesOrder.add((ImageView) turtlePositionStack[j][i]);

        return turtlesOrder;
    }

    public int getNumberOfTurtleStacksOnStartRock() {
        game.getBoard().setTurtlePositions();
        List<TurtleOnBoardPosition> turtlePositions = game.getBoard().getTurtlePositions();
        Set<Integer> turtleRocks = new HashSet();
        for (TurtleOnBoardPosition turtlePosition : turtlePositions)
            if (turtlePosition.getRock() == 0)
                turtleRocks.add(turtlePosition.getPositionOnTheStartRock()[0]);
        return turtleRocks.size();
    }

    /* WEB SOCKET MESSAGES */

    private void receiveFullGameState(final FullGameStateMsg fullGameState) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                game.setBoard(fullGameState.getBoard());
                game.setPlayersNames(fullGameState.getPlayersNames());
                game.setActivePlayerIdx(fullGameState.getActivePlayerIdx());
                game.setRecentlyPlayedCard(fullGameState.getRecentlyPlayedCard());
                MyPlayer player = new MyPlayer(myPlayerIdx, myPlayerName, fullGameState.getPlayerCards(), fullGameState.getTurtleColor());
                game.setMyPlayer(player);
            }
        });
    }

    private void receiveGameState(final GameStateUpdatedMsg gameStateUpdated) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                game.setBoard(gameStateUpdated.getBoard());
                game.setActivePlayerIdx(gameStateUpdated.getActivePlayerIdx());
                game.setRecentlyPlayedCard(gameStateUpdated.getRecentlyPlayedCard());
            }
        });
    }

    public void receiveAndUpdateFullGameState(final FullGameStateMsg fullGameState) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveFullGameState(fullGameState);
                gameActivity.updateFullGameStateWithSound();
            }
        });
    }

    public void updateCardsOnDeck(final CardsUpdatedMsg cardsUpdatedMsg) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                game.getMyPlayer().setCards(cardsUpdatedMsg.getPlayerCards());
                gameActivity.cardDeckViewController.updateCardImagesWithSound(game.getMyPlayer().getCards());
            }
        });
    }

    public void receiveAndUpdateGameState(final GameStateUpdatedMsg gameStateUpdatedMsg) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveGameState(gameStateUpdatedMsg);
                gameActivity.updateGameState();
            }
        });
    }

    public void showGameWonMessage(final GameWonMsg gameWon) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameActivity.winnerPopupViewController.manageWinnerPopupWindow(gameWon.getWinnerName(), gameWon.getPlayersNamesPlaces(), gameWon.getPlayersTurtleColors());
                gameEnded = true;
                game.setActivePlayerIdx(-1);
            }
        });
    }

    public void errorMessage(final ErrorMsg error) {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("WebSocket ErrorMsg", error.getDescription());
                Toast.makeText(gameActivity, gameActivity.getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
