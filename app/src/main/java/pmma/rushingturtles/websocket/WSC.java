package pmma.rushingturtles.websocket;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.controllers.MainActivityController;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.messages.BasicMsgFromServer;
import pmma.rushingturtles.websocket.messages.BasicMsgToServer;
import pmma.rushingturtles.websocket.messages.ErrorMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameStateUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameWonMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.CardsUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.FullGameStateMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.toserver.PlayCardMsg;
import tech.gusavila92.websocketclient.WebSocketClient;

public class WSC {
    private String playerName;
    private int playerId;
    private String ipAddress;
    private boolean isAlreadyConnected;

    private MainActivityController mainController;
    private GameActivityController gameController;

    private WebSocketClient webSocketClient;
    private static WSC instance = new WSC();

    public static WSC getInstance() {
        return instance;
    }

    public static void resetInstance() {
        instance = new WSC();
    }

    public void setClassVariables(Activity currentActivity, MainActivityController mainActivityController) {
        SharedPreferences preferences = currentActivity.getSharedPreferences("settingsPreferences", 0);
        this.playerName = preferences.getString("player_name", "Twoja Stara");
        this.playerId = preferences.getInt("player_id", -1);
        this.ipAddress = preferences.getString("ip_address", "127.0.0.1");
        this.mainController = mainActivityController;
    }

    private void setClassVariables(Activity currentActivity, MainActivityController mainActivityController, String newIpAddress) {
        SharedPreferences preferences = currentActivity.getSharedPreferences("settingsPreferences", 0);
        this.playerName = preferences.getString("player_name", "Twoja Stara");
        this.playerId = preferences.getInt("player_id", -1);
        this.ipAddress = newIpAddress;
        this.mainController = mainActivityController;
    }

    public void connect(Activity currentActivity) {
        if (!isAlreadyConnected) {
            initializeServerConnection(currentActivity);
            webSocketClient.connect();
            isAlreadyConnected = true;
            Log.i("WebSocket", "Connected with server :)");
            Toast.makeText(currentActivity, currentActivity.getResources().getString(pmma.rushingturtles.R.string.websocket_connection) + " " + ipAddress, Toast.LENGTH_SHORT).show();
        }
    }

    public void reconnect(Activity currentActivity, String newIpAddress) {
        resetInstance();
        setClassVariables(currentActivity, MainActivityController.getInstance(), newIpAddress);
        connect(currentActivity);
    }

    public void initializeServerConnection(Activity currentActivity) {
        URI uri;
        try {
            uri = new URI("ws://" + ipAddress + ":8000/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                sendHelloServerMessage();
            }

            @Override
            public void onTextReceived(String msg) {
                Log.i("WebSocket", "Message received");
                String message = getMessageFromMsg(msg);
                Log.i("WebSocket", message);

                switch (message) {
                    case "full game state":
                        FullGameStateMsg fullGameState = JsonObjectMapper.getObjectFromJson(msg, FullGameStateMsg.class);
                        gameController.receiveAndUpdateFullGameState(fullGameState);
                        break;
                    case "player cards updated":
                        CardsUpdatedMsg cardsUpdated = JsonObjectMapper.getObjectFromJson(msg, CardsUpdatedMsg.class);
                        gameController.updateCardsOnDeck(cardsUpdated);
                        break;
                    case "game state updated":
                        GameStateUpdatedMsg gameStateUpdated = JsonObjectMapper.getObjectFromJson(msg, GameStateUpdatedMsg.class);
                        gameController.receiveAndUpdateGameState(gameStateUpdated);
                        break;
                    case "game won":
                        GameWonMsg gameWon = JsonObjectMapper.getObjectFromJson(msg, GameWonMsg.class);
                        gameController.showGameWonMessage(gameWon);
                        break;
                    case "error":
                        ErrorMsg error = JsonObjectMapper.getObjectFromJson(msg, ErrorMsg.class);
                        gameController.errorMessage(error);
                        break;
                }

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            TextView textView = findViewById(R.id.animalSound);
//                            textView.setText(message);
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Server closed connection");
                isAlreadyConnected = false;
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
    }

    public WSC() {
        isAlreadyConnected = false;
    }

    private void sendHelloServerMessage() {
        String msg = "hello server";
        BasicMsgToServer helloSeverMsg = new BasicMsgToServer(msg, playerId);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(helloSeverMsg);
        webSocketClient.send(jsonMessage);
    }

    public void sendPlayCardMessage(int cardIdx, TurtleColor cardColor) {
        int cardId = gameController.getCardId(cardIdx);
        PlayCardMsg playCardMessage = new PlayCardMsg(playerId, cardId, cardColor);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(playCardMessage);
        webSocketClient.send(jsonMessage);
    }

    public String getMessageFromMsg(String msg) {
        int messageIdx = msg.indexOf("message");
        msg = msg.substring(messageIdx + 11);
        String[] msgParts = msg.split("\"");
        return msgParts[0];
    }


    public WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public void setWebSocketClient(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public boolean isAlreadyConnected() {
        return isAlreadyConnected;
    }

    public void setAlreadyConnected(boolean alreadyConnected) {
        isAlreadyConnected = alreadyConnected;
    }

    public void setGameController(GameActivityController gameController) {
        this.gameController = gameController;
    }
}
