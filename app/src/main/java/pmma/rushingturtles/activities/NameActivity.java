package pmma.rushingturtles.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import pmma.rushingturtles.R;

public class NameActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName;
    Button nameButton;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        preferences = getSharedPreferences("settingsPreferences", MODE_PRIVATE);
        if (preferences.getBoolean("firstrun", true)) {
            preferences.edit().putBoolean("firstrun", false).apply();
        } else {
            startActivity(new Intent(NameActivity.this , MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        editTextName = findViewById(R.id.editTextName);
        nameButton = findViewById(R.id.buttonNameAccept);
        nameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (editTextName.getText().toString().length() < 3)
            Toast.makeText(this, getResources().getString(R.string.name_validation_text), Toast.LENGTH_SHORT).show();
        else {
            String playerName = editTextName.getText().toString();
            preferences.edit().putString("name", playerName).apply();
            startActivity(new Intent(NameActivity.this , MainActivity.class));
            finish();
        }
    }
}