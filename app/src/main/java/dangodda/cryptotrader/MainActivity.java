package dangodda.cryptotrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gameActivityClick(View view)
    {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void highscoreActivityClick(View view)
    {
        startActivity(new Intent(this, HighScoreActivity.class));
    }

    public void settingsActivityClick(View view)
    {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
