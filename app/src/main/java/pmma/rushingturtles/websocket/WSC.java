package pmma.rushingturtles.websocket;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.activities.MainActivity;
import pmma.rushingturtles.controllers.MainActivityController;
import tech.gusavila92.websocketclient.WebSocketClient;

public class WSC {
    private String playerName;
    private int playerId;
    private String ipAddress;
    private boolean isAlreadyConnected;

    private MainActivityController mainController;

    private WebSocketClient webSocketClient;
    private static final WSC instance = new WSC();

    public static WSC getInstance() {
        return instance;
    }

    public void connect(Activity currentActivity, MainActivityController mainActivityController) {
        SharedPreferences preferences = currentActivity.getSharedPreferences("settingsPreferences", 0);
        playerName = preferences.getString("player_name", "Twoja Stara");
        playerId = preferences.getInt("player_id", -1);
        ipAddress = preferences.getString("ip_address", "127.0.0.1");

        this.mainController = mainActivityController;

        initializeServerConnection(currentActivity);
        webSocketClient.connect();
        Log.i("WebSocket", "Connected with server :)");
        isAlreadyConnected = true;
        Toast.makeText(currentActivity, currentActivity.getResources().getString(pmma.rushingturtles.R.string.websocket_connection) + " " + ipAddress, Toast.LENGTH_SHORT).show();
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
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                Log.i("WebSocket", message);

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
//                });
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
        BasicToServerMessage helloSeverMsg = new BasicToServerMessage(msg, playerId);
        String jsonMessage = JsonObjectMapper.getJsonFromObject(helloSeverMsg);
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

}
