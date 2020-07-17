package pmma.rushingturtles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.controllers.MainActivityController;
import pmma.rushingturtles.enums.ButtonState;
import pmma.rushingturtles.websocket.WSC;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        wsc = WSC.getInstance();
        if (!wsc.isAlreadyConnected()) {
            wsc.setClassVariables(this, mainController);
            wsc.connect(this);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);
        buttonState = ButtonState.START;

        initializeListView();
        setListViewVisibility();
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
        playersInTheRoomNames = new ArrayList<>();
        playersInTheRoomNames.add("Marta");
        playersInTheRoomNames.add("Piotr");
        playersInTheRoomNames.add("Maciek");

        adapter = new ArrayAdapter<>(this, R.layout.item_listview_waiting_players, R.id.textViewListView, playersInTheRoomNames);
        listView = findViewById(R.id.listViewOfPlayersInTheWaitingRoom);
        listView.setAdapter(adapter);
        listView.setEnabled(false);
    }

    private void setListViewVisibility() {
        if (playersInTheRoomNames.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
        }
    }

    public void updateRoomWithPlayers(List<String> playersNames) {
        playersInTheRoomNames = playersNames;
        adapter.notifyDataSetChanged();
    }

    public void setButtonForStart() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorStart));
        mainButton.setText(getResources().getString(R.string.main_button_start));
        buttonState = ButtonState.START;
    }

    public void setButtonForJoin() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorJoin));
        mainButton.setText(getResources().getString(R.string.main_button_join));

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

    @Override
    public void onClick(View view) {
        switch (buttonState) {
            case START_GAME:
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("my_player_name", mainController.getPlayerName());
                intent.putExtra("my_player_idx", mainController.getPlayerIdx());
                startActivity(intent);
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
                break;

        }
    }

}