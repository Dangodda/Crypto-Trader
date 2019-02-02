package dangodda.cryptotrader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameWinActivity extends Activity
{
    String nameInput;
    int finalScore;
    int finaldiff;
    int highscores;
    String[] cryptoDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_win);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        finalScore = preferences.getInt("sent_score",0);
        finaldiff = preferences.getInt("diff",0);

        cryptoDiff = new String[] {" TRX"," ETH"," mBTC"};

        String scoreText = ("Score: " + finalScore + cryptoDiff[finaldiff]);
        TextView mTextView = findViewById(R.id.uiScoreTextView);
        mTextView.setText(scoreText);

        checkHighscore();
    }

    public void checkHighscore()
    {
        if (finaldiff == 0)
        {
            SharedPreferences eScore = getSharedPreferences("easyHighscore",0);
            highscores = eScore.getInt("easyHighscore",0);
        }
        else if (finaldiff == 1)
        {
            SharedPreferences mScore = getSharedPreferences("mediumHighscore",0);
            highscores = mScore.getInt("mediumHighscore",0);
        }
        else
        {
            SharedPreferences hScore = getSharedPreferences("hardHighscore",0);
            highscores = hScore.getInt("hardHighscore",0);
        }

        if (finalScore > highscores)
        {
            addName();
        }
        else
        {
            Toast.makeText(this, "Sorry, you didn't get a highscore.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addName()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Please enter your name in the below")
                .setTitle("Enter your name");
        final EditText editText = new EditText(this);
        dialogBuilder.setView(editText);
        dialogBuilder.setPositiveButton("Submit name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nameInput = editText.getText().toString();
                highscores = finalScore;
                addHighscoreName();
            }
        });
        dialogBuilder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Toast.makeText(GameWinActivity.this, "Score not saved.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void addHighscoreName()
    {
        if (finaldiff == 0)
        {
            SharedPreferences eName = getSharedPreferences("easyUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = eName.edit();
            editor.putString("easyUser", nameInput);
            editor.apply();
            addScore();
        }
        else if (finaldiff == 1)
        {
            SharedPreferences mName = getSharedPreferences("mediumUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mName.edit();
            editor.putString("mediumUser", nameInput);
            editor.apply();
            addScore();
        }
        else if (finaldiff == 2)
        {
            SharedPreferences hName = getSharedPreferences("hardUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = hName.edit();
            editor.putString("hardUser", nameInput);
            editor.apply();
            addScore();
        }
        Toast.makeText(this, "Score of: " + highscores + " added, " + nameInput, Toast.LENGTH_SHORT).show();
    }

    public void addScore()
    {
        if (finaldiff == 0)
        {
            SharedPreferences eScore = getSharedPreferences("easyHighscore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = eScore.edit();
            editor.putInt("easyHighscore",highscores);
            editor.apply();
        }
        else if (finaldiff == 1)
        {
            SharedPreferences mScore = getSharedPreferences("mediumHighscore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mScore.edit();
            editor.putInt("mediumHighscore",highscores);
            editor.apply();
        }
        else if (finaldiff == 2)
        {
            SharedPreferences hScore = getSharedPreferences("hardHighscore", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = hScore.edit();
            editor.putInt("hardHighscore",highscores);
            editor.apply();
        }
    }


    public void gameActivityClick(View view)
    {
        startActivity(new Intent(this, GameActivity.class));
        SharedPreferences preferences = getSharedPreferences("sent_score", 0);
        preferences.edit().remove("shared_pref_key").apply();
    }

    public void mainActivityClick(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
        SharedPreferences preferences = getSharedPreferences("sent_score", 0);
        preferences.edit().remove("shared_pref_key").apply();
    }
}