package pmma.rushingturtles.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import pmma.rushingturtles.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarSetting(toolbar);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("message");
        TextView testText = findViewById(R.id.textView);
        testText.setText(msg);
    }

    private void toolbarSetting(Toolbar toolbar) {
        getSupportActionBar().setTitle(getResources().getString(R.string.settings_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.whiteTextColor), PorterDuff.Mode.SRC_ATOP);
    }


}
