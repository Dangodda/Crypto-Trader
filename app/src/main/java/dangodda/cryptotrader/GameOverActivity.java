package dangodda.cryptotrader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class GameOverActivity extends Activity
{
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        MobileAds.initialize(this, "ca-app-pub-5511295888256056~9812700015");

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-5511295888256056/6665143566");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int finalScore = preferences.getInt("sent_score",0);

        String scoreText = ("Score: " + finalScore + "mBTC");

        final TextView mTextView = findViewById(R.id.uiScoreTextView);
        mTextView.setText(scoreText);
    }

    public void gameActivityClick(View view)
    {
        //Removes current score and starts game again
        SharedPreferences preferences = getSharedPreferences("sent_score", 0);
        preferences.edit().remove("shared_pref_key").apply();

        startActivity(new Intent(this, GameActivity.class));
    }

    public void mainActivityClick(View view)
    {
        //Removes current score and returns to main activity
        SharedPreferences preferences = getSharedPreferences("sent_score", 0);
        preferences.edit().remove("shared_pref_key").apply();

        startActivity(new Intent(this, MainActivity.class));
    }
}
