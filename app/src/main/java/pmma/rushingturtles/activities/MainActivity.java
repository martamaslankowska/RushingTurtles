package pmma.rushingturtles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.controllers.MainActivityController;
import pmma.rushingturtles.websocket.WSC;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    List<String> playersInTheWaitingRoomNames;
    WSC wsc;
    MainActivityController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainController = MainActivityController.getInstance();
        mainController.initializeMainController(this);

        wsc = WSC.getInstance();
        if (!wsc.isAlreadyConnected())
            wsc.connect(this, mainController);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);

        playersInTheWaitingRoomNames = new ArrayList<>();
        playersInTheWaitingRoomNames.add("Marta");
        playersInTheWaitingRoomNames.add("Piotr");
        playersInTheWaitingRoomNames.add("Maciek");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_listview_waiting_players, R.id.textViewListView, playersInTheWaitingRoomNames);
        ListView listView = findViewById(R.id.listViewOfPlayersInTheWaitingRoom);
        listView.setAdapter(adapter);
        listView.setEnabled(false);
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

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.mainButton:
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.test_string), Toast.LENGTH_SHORT).show();
                Intent intentInfo = new Intent(this, GameActivity.class);
                startActivity(intentInfo);
                break;
        }
    }

}