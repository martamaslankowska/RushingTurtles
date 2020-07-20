package pmma.rushingturtles.websocket;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import pmma.rushingturtles.R;
import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.controllers.MainActivityController;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.messages.BasicMsgToServer;
import pmma.rushingturtles.websocket.messages.ErrorMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameStateUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.broadcast.GameWonMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.CardsUpdatedMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.fromserver.FullGameStateMsg;
import pmma.rushingturtles.websocket.messages.gameactivity.toserver.PlayCardMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.broadcast.GameReadyToStartMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.broadcast.RoomUpdateMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.fromserver.HelloClientMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.toserver.HelloServerMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.toserver.StartTheGameMsg;
import pmma.rushingturtles.websocket.messages.mainactivity.toserver.WantToJoinGameMsg;
import tech.gusavila92.websocketclient.WebSocketClient;

public class WSC {
    private String playerName;
    private int playerId;
    private String ipAddress;

    private boolean isAlreadyConnected;
    private boolean hasShowedConnectionStatus;

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
        }
    }

    public void reconnect(Activity currentActivity, String newIpAddress) {
        resetInstance();
        setClassVariables(currentActivity, MainActivityController.getInstance(), newIpAddress);
        connect(currentActivity);
    }

    public void initializeServerConnection(final Activity currentActivity) {
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
                sendHelloServerMsg();
            }

            @Override
            public void onTextReceived(String msg) {
                Log.i("WebSocket", "Message received");
                String message = getMessageFromMsg(msg);
                Log.i("WebSocket msg", message);
                Log.i("WebSocket json", msg);

//                mainController.getMainActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        switch (message) {
//                            case "hello client":
//                                HelloClientMsg helloClient = JsonObjectMapper.getObjectFromJson(msg, HelloClientMsg.class);
//                                mainController.manageHelloClientMsg(helloClient);
//                                break;
//                            case "room update":
//                                RoomUpdateMsg roomUpdateMsg = JsonObjectMapper.getObjectFromJson(msg, RoomUpdateMsg.class);
//                                mainController.receiveRoomUpdateMsg(roomUpdateMsg);
//                                break;
//                            case "game ready to start":
//                                GameReadyToStartMsg gameReadyToStartMsg = JsonObjectMapper.getObjectFromJson(msg, GameReadyToStartMsg.class);
//                                mainController.receiveGameReadyToStart(gameReadyToStartMsg);
//                                break;
//                            case "error":
//                                ErrorMsg error = JsonObjectMapper.getObjectFromJson(msg, ErrorMsg.class);
//                                try {
//                                    mainController.errorMessage(error);
//                                } catch (Exception e2) {
//                                    Log.i("WebSocket error msg", e2.toString());
//                                }
//                                break;
//                        }
//                    }
//                });
//
//                gameController.getGameActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        switch (message) {
//                            case "full game state":
//                                FullGameStateMsg fullGameState = JsonObjectMapper.getObjectFromJson(msg, FullGameStateMsg.class);
//                                gameController.receiveAndUpdateFullGameState(fullGameState);
//                                break;
//                            case "player cards updated":
//                                CardsUpdatedMsg cardsUpdated = JsonObjectMapper.getObjectFromJson(msg, CardsUpdatedMsg.class);
//                                gameController.updateCardsOnDeck(cardsUpdated);
//                                break;
//                            case "game state updated":
//                                GameStateUpdatedMsg gameStateUpdated = JsonObjectMapper.getObjectFromJson(msg, GameStateUpdatedMsg.class);
//                                gameController.receiveAndUpdateGameState(gameStateUpdated);
//                                break;
//                            case "game won":
//                                GameWonMsg gameWon = JsonObjectMapper.getObjectFromJson(msg, GameWonMsg.class);
//                                gameController.showGameWonMessage(gameWon);
//                                break;
//                            case "error":
//                                ErrorMsg error = JsonObjectMapper.getObjectFromJson(msg, ErrorMsg.class);
//                                try {
//                                    gameController.errorMessage(error);
//                                } catch (Exception e1) {
//                                    Log.i("WebSocket error msg", e1.toString());
//                                }
//                                break;
//                        }
//                    }
//                });

                switch (message) {
                    case "hello client":
                        HelloClientMsg helloClient = JsonObjectMapper.getObjectFromJson(msg, HelloClientMsg.class);
                        mainController.manageHelloClientMsg(helloClient);

                        isAlreadyConnected = true;
                        hasShowedConnectionStatus = true;
                        Log.i("WebSocket", "Connected with server :)");
                        mainController.getMainActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(currentActivity, currentActivity.getResources().getString(pmma.rushingturtles.R.string.toast_websocket_connection) + " " + ipAddress, Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case "room update":
                        RoomUpdateMsg roomUpdateMsg = JsonObjectMapper.getObjectFromJson(msg, RoomUpdateMsg.class);
                        mainController.receiveRoomUpdateMsg(roomUpdateMsg);
                        break;
                    case "game ready to start":
                        GameReadyToStartMsg gameReadyToStartMsg = JsonObjectMapper.getObjectFromJson(msg, GameReadyToStartMsg.class);
                        mainController.receiveGameReadyToStart(gameReadyToStartMsg);
                        break;
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
                        try {
                            gameController.errorMessage(error);
                        } catch (Exception e1) {
                            try {
                                mainController.errorMessage(error);
                            } catch (Exception e2) {
                                Log.i("WebSocket error msg", e2.toString());
                            }
                        }
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

            }

            @Override
            public void onBinaryReceived(byte[] data) {
//                Log.i("WebSocket", "onBinaryReceived");
            }
            @Override
            public void onPingReceived(byte[] data) {
//                Log.i("WebSocket", "onPingReceived");
            }
            @Override
            public void onPongReceived(byte[] data) {
//                Log.i("WebSocket", "onPongReceived");
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
                Log.i("WebSocket", "onException - " + e.toString());
                if (e instanceof ConnectException && !hasShowedConnectionStatus) {
                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(currentActivity, currentActivity.getResources().getString(R.string.toast_no_connection), Toast.LENGTH_SHORT).show();
                            hasShowedConnectionStatus = true;
                        }
                    });
                }
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

    private void sendHelloServerMsg() {
        HelloServerMsg helloSeverMsg = new HelloServerMsg(playerId, playerName);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(helloSeverMsg);
        webSocketClient.send(jsonMessage);
    }

    public void sendPlayCardMsg(int cardIdx, TurtleColor cardColor) {
        int cardId = gameController.getCardId(cardIdx);
        PlayCardMsg playCardMessage = new PlayCardMsg(playerId, cardId, cardColor);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(playCardMessage);
        webSocketClient.send(jsonMessage);
    }

    public void sendWantToJoinTheGameMsg(String status) {
        WantToJoinGameMsg wantToJoinGameMsg = new WantToJoinGameMsg(playerId, status);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(wantToJoinGameMsg);
        webSocketClient.send(jsonMessage);
    }

    public void sendStartTheGameMsg() {
        StartTheGameMsg startTheGameMsg = new StartTheGameMsg(playerId);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(startTheGameMsg);
        webSocketClient.send(jsonMessage);
    }

    public String getMessageFromMsg(String msg) {
        int messageIdx = msg.indexOf("message");
        msg = msg.substring(messageIdx + 11);
        String[] msgParts = msg.split("\"");
        return msgParts[0];
    }

    public void sendReadyToReceiveGameStateMsg() {
        BasicMsgToServer readyToReceiveGameStateMsg = new BasicMsgToServer("ready to receive game state", playerId);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(readyToReceiveGameStateMsg);
        webSocketClient.send(jsonMessage);
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
