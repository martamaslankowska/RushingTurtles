package pmma.rushingturtles.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import pmma.rushingturtles.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences preferences;
    String playerName;

    Button editSaveButton;
    EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarSetting(toolbar);

        nameEditText = findViewById(R.id.editTextPlayerName);
        nameEditText.setEnabled(false);
        editSaveButton = findViewById(R.id.buttonNameChange);
        editSaveButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences = getSharedPreferences("settingsPreferences", MODE_PRIVATE);
        playerName = preferences.getString("name", "Marta <3");
        nameEditText.setText(playerName);
    }

    private void toolbarSetting(Toolbar toolbar) {
        getSupportActionBar().setTitle(getResources().getString(R.string.settings_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.whiteTextColor), PorterDuff.Mode.SRC_ATOP);
    }


    @Override
    public void onClick(View view) {
        if (!nameEditText.isEnabled()) {
            nameEditText.setEnabled(true);
            editSaveButton.setText(getResources().getString(R.string.name_save_button_text));
        } else {
            nameEditText.setEnabled(false);
            editSaveButton.setText(getResources().getString(R.string.name_edit_button_text));
            preferences.edit().putString("name", nameEditText.getText().toString()).apply();
        }
    }
}
