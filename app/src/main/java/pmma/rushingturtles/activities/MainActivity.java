package pmma.rushingturtles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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

    final static Integer REQUEST_CODE = 123;

    TextView playersInTheRoomTextView, initialTextInfo;
    List<String> playersInTheRoomNames;
    ListView listView;
    ArrayAdapter<String> adapter;

    Button mainButton;
    ButtonState buttonState;

    WSC wsc;
    MainActivityController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainController = MainActivityController.getInstance();
        mainController.initializeMainController(this);

        initializeXmlViews();
        initializeListView();

        wsc = WSC.getInstance();
        if (!wsc.isAlreadyConnected()) {
            wsc.setClassVariables(mainController.getMainActivity(), mainController);
            wsc.connect(mainController.getMainActivity());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume()");

        buttonState = mainController.getButtonState();
        updateButtonBasedOnButtonState();
        updateRoomWithPlayers();
        setListViewVisibility();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MainActivity", "onActivityResult()");

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("play_again_msg", false)) {
                WSC.getInstance().sendWantToJoinTheGameMsg();
                mainController.setButtonState(ButtonState.AFTER_JOIN);
                mainController.setPlayersInTheRoomNames(new ArrayList<String>());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("playersState", buttonState.toString());
        outState.putStringArrayList("playersInTheRoomNames", (ArrayList<String>) playersInTheRoomNames);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String playersState = savedInstanceState.getString("playersState");
        buttonState = ButtonState.valueOf(playersState);
        updateViewsAfterOrientationChange(savedInstanceState);
    }

    private void updateButtonBasedOnButtonState() {
        switch (buttonState) {
            case OTHER:
                setViewForOther();
                break;
            case START:
                setButtonForStart();
                break;
            case JOIN:
                setButtonForJoin();
                break;
            case AFTER_JOIN:
                setButtonForAfterJoinOrResume();
                break;
            case ALMOST_START_GAME:
            case START_GAME:
                setButtonForStartTheGame();
                break;
            case LIMIT:
                setButtonForInactive(ButtonState.LIMIT);
                break;
            case ONGOING:
                setButtonForInactive(ButtonState.ONGOING);
                break;
            case RESUME:
                setButtonForResume();
                break;
        }
    }

    private void updateViewsAfterOrientationChange(Bundle savedInstanceState) {
        updateRoomWithPlayers(savedInstanceState.getStringArrayList("playersInTheRoomNames"));
        updateButtonBasedOnButtonState();
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

    private void initializeXmlViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);
        initialTextInfo = findViewById(R.id.textViewInfo);
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
        playersInTheRoomNames = mainController.getPlayersInTheRoomNames();
        if (playersInTheRoomNames.isEmpty()) {
            listView.setVisibility(View.GONE);
            playersInTheRoomTextView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            playersInTheRoomTextView.setVisibility(View.VISIBLE);
        }
    }

    public void updateRoomWithPlayers(List<String> playersNames) {
        mainController.setPlayersInTheRoomNames(playersNames);
        updateRoomWithPlayers();
    }

    public void updateRoomWithPlayers() {
        playersInTheRoomNames = mainController.getPlayersInTheRoomNames();
        adapter.clear();
        adapter.addAll(playersInTheRoomNames);
        adapter.notifyDataSetChanged();
        Log.i("WebSocket", playersInTheRoomNames.toString());

        if (buttonState == ButtonState.ALMOST_START_GAME || buttonState == ButtonState.START_GAME || isPlayerFirstOnTheList())
            setButtonForStartTheGame();
    }

    private boolean isPlayerFirstOnTheList() {
        return !playersInTheRoomNames.isEmpty() && playersInTheRoomNames.get(0).equals(mainController.getPlayerName());
    }

    private void setViewForOther() {
        mainButton.setVisibility(View.INVISIBLE);
        initialTextInfo.setText(getResources().getString(R.string.initial_info));
        initialTextInfo.setVisibility(View.VISIBLE);
    }

    private void setButtonVisibility(boolean shouldBeEnabled) {
        mainButton.setVisibility(View.VISIBLE);
        initialTextInfo.setVisibility(View.GONE);
        mainButton.setEnabled(shouldBeEnabled);
    }

    private void setButtonVisibility() {
        setButtonVisibility(true);
    }

    public void setButtonForStart() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorStart));
        mainButton.setText(getResources().getString(R.string.main_button_start));
        buttonState = ButtonState.START;
        setButtonVisibility();
    }

    public void setButtonForJoin() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorJoin));
        mainButton.setText(getResources().getString(R.string.main_button_join));
        buttonState = ButtonState.JOIN;
        setButtonVisibility();
    }

    public void setButtonForResume() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonColorJoin));
        mainButton.setText(getResources().getString(R.string.main_button_resume));
        buttonState = ButtonState.RESUME;
        setButtonVisibility();
    }

    private void setButtonForAfterJoinOrResume() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
        mainButton.setText(getResources().getString(R.string.main_button_after_join));
        buttonState = ButtonState.AFTER_JOIN;
        setButtonVisibility(false);
    }

    public void setButtonForInactive(ButtonState state) {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
        mainButton.setText(getResources().getString(R.string.main_button_join));
        buttonState = state;
        setButtonVisibility();
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
        mainController.setButtonState(buttonState);
        setButtonVisibility();
    }

    private void setButtonForWaitingFromServerForGameStart() {
        mainButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
        mainButton.setText(getResources().getString(R.string.main_button_wait_for_start_the_game));
        setButtonVisibility();
    }

    public void startTheGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("my_player_name", mainController.getPlayerName());
        intent.putExtra("my_player_idx", mainController.getPlayerIdx());
        startActivityForResult(intent, REQUEST_CODE);
//        finish();
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
                WSC.getInstance().sendWantToJoinTheGameMsg();
                setButtonForStartTheGame();
                break;
            case JOIN:
            case RESUME:
                WSC.getInstance().sendWantToJoinTheGameMsg();
                setButtonForAfterJoinOrResume();
                break;
//            case RESUME:
//                Toast.makeText(this, "RESUME THE GAME <?>", Toast.LENGTH_SHORT).show();
//                WSC.getInstance().sendWantToJoinTheGameMsg();
//                setButtonForAfterJoinOrResume();
//                break;
            case LIMIT:
                Toast.makeText(this, getResources().getString(R.string.main_button_toast_limit), Toast.LENGTH_SHORT).show();
                break;
            case ONGOING:
                Toast.makeText(this, getResources().getString(R.string.main_button_toast_ongoing), Toast.LENGTH_SHORT).show();
                break;
            case OTHER:
//                startTheGame();
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "onDestroy()");
    }
}