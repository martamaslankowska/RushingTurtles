package pmma.rushingturtles.activities;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import pmma.rushingturtles.R;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String playerName;
    String ipAddress;

    Button saveNameButton, saveEditIpAddressButton;
    EditText nameEditText, ipEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarSetting(toolbar);

        initializeXmlViews();
    }

    private void initializeXmlViews() {
        nameEditText = findViewById(R.id.editTextPlayerName);
        nameEditText.clearFocus();
        saveNameButton = findViewById(R.id.buttonNameChange);
        saveNameButton.setOnClickListener(changeNameClickListener);

        ipEditText = findViewById(R.id.editTextIpAddress);
        ipEditText.setEnabled(false);
        saveEditIpAddressButton = findViewById(R.id.buttonIpChange);
        saveEditIpAddressButton.setOnClickListener(changeIpAddressClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences = getSharedPreferences("settingsPreferences", MODE_PRIVATE);
        playerName = preferences.getString("player_name", "Marta <3");
        nameEditText.setText(playerName);
        ipAddress = preferences.getString("ip_address", "127.0.0.1");
        ipEditText.setText(ipAddress);
    }

    private void toolbarSetting(Toolbar toolbar) {
        getSupportActionBar().setTitle(getResources().getString(R.string.settings_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.whiteTextColor), PorterDuff.Mode.SRC_ATOP);
    }

    private View.OnClickListener changeNameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeNameOnClick();
        }
    };

    private void changeNameOnClick() {
        String newName = nameEditText.getText().toString();
        if (isNewNameValid(newName)) {
            preferences.edit().putString("player_name", newName).apply();
            Toast.makeText(this, getResources().getString(R.string.toast_save_name_part_1) + " '" + newName + "' " + getResources().getString(R.string.toast_has_been_saved), Toast.LENGTH_SHORT).show();
            nameEditText.clearFocus();
            try { /* Hide keyboard */
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) { }
        }
    }

    private boolean isNewNameValid(String newName) {
        boolean valid = false;
        if (newName.length() < 3)
            Toast.makeText(this, getResources().getString(R.string.name_length_validation_text), Toast.LENGTH_SHORT).show();
        else if (newName.contains("\n"))
            Toast.makeText(this, getResources().getString(R.string.name_enter_validation_text), Toast.LENGTH_SHORT).show();
        else
            valid = true;
        return valid;
    }

    private View.OnClickListener changeIpAddressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeIpAddressOnClick();
        }
    };

    private void changeIpAddressOnClick() {
        if (!ipEditText.isEnabled()) {
            ipEditText.setEnabled(true);
            saveEditIpAddressButton.setText(getResources().getString(R.string.save_button_text));
        } else {
            ipEditText.setEnabled(false);
            saveEditIpAddressButton.setText(getResources().getString(R.string.edit_button_text));
            String newIpAddress = ipEditText.getText().toString();
            getSharedPreferences("settingsPreferences", MODE_PRIVATE).edit().putString("ip_address", newIpAddress).apply();
            Toast.makeText(this, getResources().getString(R.string.toast_new_ip_address) + " " + newIpAddress + " " + getResources().getString(R.string.toast_has_been_saved), Toast.LENGTH_SHORT).show();
        }
    }
}
