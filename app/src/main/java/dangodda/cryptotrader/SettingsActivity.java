package dangodda.cryptotrader;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Random;

import es.dmoral.toasty.Toasty;

public class SettingsActivity extends Activity {
    SharedPreferences confirmation;
    Switch confirmSwitch;
    Boolean confCheck;
    String versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        confirmSwitch = findViewById(R.id.uiSwitchSwitch);
        confirmation = getSharedPreferences("confirm", Context.MODE_PRIVATE);

        confCheck = confirmation.getBoolean("confirm", false);
        confirmSwitch.setChecked(confCheck);

        confirmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = confirmation.edit();
                    editor.putBoolean("confirm", true);
                    editor.apply();
                    Toasty.info(SettingsActivity.this, "Button prompts on.", Toast.LENGTH_SHORT, true).show();
                    //Toasty.info(SettingsActivity.this, "Button prompts on.", Toast.LENGTH_SHORT, true).show();
                } else {
                    SharedPreferences.Editor editor = confirmation.edit();
                    editor.putBoolean("confirm", false);
                    editor.apply();
                    Toasty.info(SettingsActivity.this, "Button prompts off.", Toast.LENGTH_SHORT, true).show();
                    //Toasty.info(SettingsActivity.this, "Button prompts off.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }
    public void resetHighscores(View view)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Are you sure you want to delete the High-scores?");

        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {

                SharedPreferences eName = getSharedPreferences("easyUser", 0);
                SharedPreferences eScore = getSharedPreferences("easyHighscore",0);
                SharedPreferences mName = getSharedPreferences("mediumUser", 0);
                SharedPreferences mScore = getSharedPreferences("mediumHighscore",0);
                SharedPreferences hName = getSharedPreferences("hardUser", 0);
                SharedPreferences hScore = getSharedPreferences("hardHighscore",0);

                eName.edit().remove("easyUser").apply();
                eScore.edit().remove("easyHighscore").apply();
                mName.edit().remove("mediumUser").apply();
                mScore.edit().remove("mediumHighscore").apply();
                hName.edit().remove("hardUser").apply();
                hScore.edit().remove("hardHighscore").apply();

                Toast.makeText(getApplicationContext(), "Highscores removed successfully.", Toast.LENGTH_SHORT).show();
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
    public void mainActivityClick(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void versionClick(View view) {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCode = BuildConfig.VERSION_NAME;
        Toast.makeText(this, "App Version " + versionCode, Toast.LENGTH_SHORT).show();
    }
}
