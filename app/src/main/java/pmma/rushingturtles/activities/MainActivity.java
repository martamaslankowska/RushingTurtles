package pmma.rushingturtles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.controllers.MainActivityController;
import pmma.rushingturtles.enums.ButtonState;
import pmma.rushingturtles.websocket.WSC;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView playersInTheRoomTextView;
    List<String> playersInTheRoomNames;
    ListView listView;
    ArrayAdapter<String> adapter;

    Button mainButton;
    ButtonState buttonState;

    WSC wsc;
    MainActivityController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainController = MainActivityController.getInstance();
        mainController.initializeMainController(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);
        buttonState = ButtonState.OTHER;
        initializeListView();
        setListViewVisibility();

        wsc = WSC.getInstance();
        if (!wsc.isAlreadyConnected()) {
            wsc.setClassVariables(mainController.getMainActivity(), mainController);
            wsc.connect(mainController.getMainActivity());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoMenu:
                wsc.getWebSocketClient().send("HALOOOO xD");
                Intent intentInfo = new Intent(this, InfoActivity.class);
                startActivity(intentInfo);
                break;

            case R.id.settingsMenu:
//                Toast.makeText(this, getResources().getString(R.string.settings_toolbar) + " selected", Toast.LENGTH_SHORT).show();
                Intent intentSettings = new Intent(this, SettingsActivity.class);
//                intentSettings.putExtra("message", "Heeey :)");
                startActivity(intentSettings);
                break;

            default:
                break;
        }
        return true;
    }

    private void initializeListView() {
        playersInTheRoomTextView = findViewById(R.id.textViewWaitingRoomPlayers);
        playersInTheRoomNames = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.item_listview_waiting_players, R.id.textViewListView, playersInTheRoomNames);
        listView = findViewById(R.id.listViewOfPlayersInTheWaitingRoom);
        listView.setAdapter(adapter);
        listView.setEnabled(false);
    }

    public void setListViewVisibility() {
        if (playersInTheRoomNames.isEmpty()) {
            listView.setVisibility(View.GONE);
            playersInTheRoomTextView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            playersInTheRoomTextView.setVisibility(View.VISIBLE);
        }
    }

    public void updateRoomWithPlayers(List<String> playersNames) {
        playersInTheRoomNames = playersNames;
        adapter.clear();
        adapter.addAll(playersInTheRoomNames);
        adapter.notifyDataSetChanged();
        Log.i("WebSocket", playersInTheRoomNames.toString());
        if (buttonState == ButtonState.ALMOST_START_GAME || buttonState == ButtonState.START_GAME)
            setButtonForStartTheGame();
    }

    public void setButtonForStart() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorStart));
        mainButton.setText(getResources().getString(R.string.main_button_start));
        buttonState = ButtonState.START;
    }

    public void setButtonForJoin() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorJoin));
        mainButton.setText(getResources().getString(R.string.main_button_join));
        buttonState = ButtonState.JOIN;
    }

    private void setButtonForAfterJoin() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
        mainButton.setText(getResources().getString(R.string.main_button_after_join));
        mainButton.setEnabled(false);
    }

    public void setButtonForInactive(ButtonState state) {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
        mainButton.setText(getResources().getString(R.string.main_button_join));
        buttonState = state;
    }

    private void setButtonForStartTheGame() {
        mainButton.setText(getResources().getString(R.string.main_button_start_the_game));
        if (playersInTheRoomNames.size() < 2) {
            mainButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
            buttonState = ButtonState.ALMOST_START_GAME;
        } else {
            mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorStartTheGame));
            buttonState = ButtonState.START_GAME;
        }
    }

    private void setButtonForWaitingFromServerForGameStart() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
        mainButton.setText(getResources().getString(R.string.main_button_wait_for_start_the_game));
    }

    public void startTheGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("my_player_name", mainController.getPlayerName());
        intent.putExtra("my_player_idx", mainController.getPlayerIdx());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (buttonState) {
            case START_GAME:
                WSC.getInstance().sendStartTheGameMsg();
                setButtonForWaitingFromServerForGameStart();
//                startTheGame();
                break;
            case ALMOST_START_GAME:
                Toast.makeText(this, getResources().getString(R.string.main_button_toast_almost_start_the_game), Toast.LENGTH_SHORT).show();
                break;
            case START:
                WSC.getInstance().sendWantToJoinTheGameMsg("create the game");
                setButtonForStartTheGame();
                break;
            case JOIN:
                WSC.getInstance().sendWantToJoinTheGameMsg("join the game");
                setButtonForAfterJoin();
                break;
            case RESUME:
                Toast.makeText(this, "RESUME THE GAME <?>", Toast.LENGTH_SHORT).show();
                break;
            case LIMIT:
                Toast.makeText(this, getResources().getString(R.string.main_button_toast_limit), Toast.LENGTH_SHORT).show();
                break;
            case ONGOING:
                Toast.makeText(this, getResources().getString(R.string.main_button_toast_ongoing), Toast.LENGTH_SHORT).show();
                break;
            case OTHER:
                startTheGame();
                break;

        }
    }

}