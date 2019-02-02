package dangodda.cryptotrader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
public class HighScoreActivity extends Activity
{
    TextView easyTextView;
    TextView mediumTextView;
    TextView hardTextView;
    int finaldiff;
    String easyName;
    String medName;
    String hardName;
    int easyScore;
    int medScore;
    int hardScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        easyTextView = findViewById(R.id.uiEasyTextView);
        mediumTextView  = findViewById(R.id.uiMediumTextView);
        hardTextView  = findViewById(R.id.uiHardTextView);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences eName = getSharedPreferences("easyUser", 0);
        SharedPreferences eScore = getSharedPreferences("easyHighscore",0);
        SharedPreferences mName = getSharedPreferences("mediumUser", 0);
        SharedPreferences mScore = getSharedPreferences("mediumHighscore",0);
        SharedPreferences hName = getSharedPreferences("hardUser", 0);
        SharedPreferences hScore = getSharedPreferences("hardHighscore",0);

        finaldiff = preferences.getInt("diff",0);

        easyName = eName.getString("easyUser", "No one");
        easyScore = eScore.getInt("easyHighscore", 0);
        String easyText = ("Easy Highscore by " + easyName + ", is: " + easyScore + " TRX");
        easyTextView.setText(easyText);

        medName = mName.getString("mediumUser", "No one");
        medScore = mScore.getInt("mediumHighscore", 0);
        String medText = ("Medium Highscore by " + medName + ", is: " + medScore + " ETH");
        mediumTextView.setText(medText);

        hardName = hName.getString("hardUser", "No one");
        hardScore = hScore.getInt("hardHighscore", 0);
        String hardText = ("Hard Highscore by " + hardName + ", is: " + hardScore + " mBTC");
        hardTextView.setText(hardText);
    }

    public void mainActivityClick(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}