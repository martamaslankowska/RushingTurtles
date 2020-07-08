package pmma.rushingturtles.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pmma.rushingturtles.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoMenu:
//                Toast.makeText(this, getResources().getString(R.string.info_toolbar) + " selected", Toast.LENGTH_SHORT).show();
                Intent intentInfo = new Intent(this, InfoActivity.class);
                startActivity(intentInfo);
                break;

            case R.id.settingsMenu:
//                Toast.makeText(this, getResources().getString(R.string.settings_toolbar) + " selected", Toast.LENGTH_SHORT).show();
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                intentSettings.putExtra("message", "Heeey :)");
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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.test_string), Toast.LENGTH_SHORT).show();
        }
    }

}