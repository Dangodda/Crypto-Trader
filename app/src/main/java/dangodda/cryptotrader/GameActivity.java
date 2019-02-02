package dangodda.cryptotrader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static java.util.Collections.shuffle;

public class GameActivity extends Activity {
    int score;
    int userCorrectAnswers;
    boolean phonebuttonPressed;
    boolean askbuttonPressed;
    boolean fiftyfiftyButtonPressed;
    String userAnswer;
    String correctAnswer;
    String question;
    private ArrayList<Questions> JSONQuestions;
    TextView Question;
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    Button phoneButton;
    Button audienceButton;
    Button fiftyfiftyButton;
    int difficulty;
    String[] apiLinks;
    TextView mScoreTextView;
    String scoreText;
    String[] cryptoDiff;
    boolean difficultySet;
    SharedPreferences confirmation;
    boolean confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Sets all variables
        apiLinks = new String[] {"https://opentdb.com/api.php?amount=30&category=9&difficulty=easy&type=multiple",
                                 "https://opentdb.com/api.php?amount=30&category=9&difficulty=medium&type=multiple",
                                 "https://opentdb.com/api.php?amount=30&category=9&difficulty=hard&type=multiple"};
        phonebuttonPressed = false;
        fiftyfiftyButtonPressed = false;
        askbuttonPressed = false;
        correctAnswer = "";
        JSONQuestions = new ArrayList<>();
        mScoreTextView = findViewById(R.id.uiScoreTextView);
        Question = findViewById(R.id.uiQuestionTextView);
        buttonA = findViewById(R.id.uiButtonSelectA);
        buttonB = findViewById(R.id.uiButtonSelectB);
        buttonC = findViewById(R.id.uiButtonSelectC);
        buttonD = findViewById(R.id.uiButtonSelectD);
        phoneButton = findViewById(R.id.uiPhoneButton);
        audienceButton = findViewById(R.id.uiAskButton);
        fiftyfiftyButton = findViewById(R.id.uiFiftyButton);
        cryptoDiff = new String[] {"TRX","ETH","mBTC"};
        scoreText = (score + cryptoDiff[difficulty]);
        confirmation = getSharedPreferences("confirm",0);
        confirm = confirmation.getBoolean("confirm", false);
        setDifficulty();
    }

    public void setDifficulty()
    {
        //Prompts user to select a difficulty level
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose your difficulty");

        dialogBuilder.setNeutralButton("Easy (TRX)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {
                difficulty = 0;
                difficultySet = true;
                getQuestion();
            }
        });
        dialogBuilder.setNegativeButton("Medium (ETH)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {
                difficulty = 1;
                difficultySet = true;
                getQuestion();
            }
        });
        dialogBuilder.setPositiveButton("Hard (mBTC)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {
                difficulty = 2;
                difficultySet = true;
                getQuestion();
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void getQuestion()
    {
        //Gets Questions from API
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String requestUrl = apiLinks[difficulty];
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONObject>()
                {
                    @Override

                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i =0; i<jsonArray.length(); i++)
                            {
                                JSONObject results = jsonArray.getJSONObject(i);
                                Questions questions = new Questions();

                                ArrayList<String> incorrectAnswers = new ArrayList<>();

                                question = results.getString("question");
                                question = checkSymbol(question);
                                questions.setQuestion(question);

                                correctAnswer = results.getString("correct_answer");
                                correctAnswer = checkSymbol(correctAnswer);
                                questions.setCorrectAns(correctAnswer);

                                JSONArray incorrect = results.getJSONArray("incorrect_answers");

                                for(int j =0; j<incorrect.length(); j++)
                                {
                                    incorrectAnswers.add(checkSymbol(incorrect.getString(j)));
                                }
                                questions.setIncorrectAns(incorrectAnswers);

                                JSONQuestions.add(questions);

                            }
                            setQuestion();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
                        dialogBuilder.setTitle("Use offline play?");

                        dialogBuilder.setNeutralButton("Retry Connection", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id)
                            {
                                getQuestion();
                            }
                        });

                        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Intent mainMenu = new Intent(GameActivity.this, MainActivity.class);
                                startActivity(mainMenu);
                            }
                        });
                        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id)
                            {
                                //Load offline questions from xml
                            }
                        });
                        AlertDialog dialog = dialogBuilder.create();
                        dialog.show();

                        Toasty.error(GameActivity.this, "Volley Error - Check connection", Toast.LENGTH_SHORT, true).show();
                    }
                });
        requestQueue.add(jsonObjRequest);

    }

    public String checkSymbol(String string) {
        //Used to check each character for special character code
        char chars[] = new char[string.length()];
        String newString;
        int index = 0;

        for (int z = 0; z < string.length(); z++) {

            if (string.charAt(z) == '&') {
                index = z;
                if (string.charAt(z + 1)=='#'||string.charAt(z+1)=='r'){
                    if(string.charAt(z + 1) == '#'){
                        z += 5;
                    } else {
                        z += 6;
                    }
                    chars[z] = '\'';
                } else if (string.charAt(z + 1) == 'A' || string.charAt(z + 1) == 'a') {
                    chars[z] = '&';
                    z += 4;
                } else if (string.charAt(z + 1) == 'q' || string.charAt(z + 1) == 'l') {
                    chars[z] = '"';
                    z += 5;
                } else if (string.charAt(z + 1) == 's') {
                    chars[z] = '-';
                    z += 4;
                } else if (string.charAt(z + 1) == 'o' || string.charAt(z + 1) == 'O') {
                    if (string.charAt(z + 1) == 'o') {
                        chars[z] = 'ó';
                    } else {
                        chars[z] = 'Ó';
                    }
                    z += 7;
                }
                else if (string.charAt(z + 1) == 'e' || string.charAt(z + 1) == 'E') {
                    if (string.charAt(z + 1) == 'e') {
                        chars[z] = 'é';
                    } else {
                        chars[z] = 'É';
                    }
                    z += 6;
                }
            }
            else
            {
                chars[index] = string.charAt(z);
            }
            index++;
        }
        newString = String.valueOf(chars);
        return newString;
    }

    public void setQuestion()
    {   //Sets Questions from the JSON function
        ArrayList<String> currentQuestion = new ArrayList<>();
        currentQuestion.addAll(JSONQuestions.get(userCorrectAnswers).getIncorrectAns());
        currentQuestion.add(JSONQuestions.get(userCorrectAnswers).getCorrectAns());
        shuffle(currentQuestion);

        correctAnswer = JSONQuestions.get(userCorrectAnswers).getCorrectAns();
        Question.setText(JSONQuestions.get(userCorrectAnswers).getQuestion());

        buttonA.setText(currentQuestion.get(0));
        buttonB.setText(currentQuestion.get(1));
        buttonC.setText(currentQuestion.get(2));
        buttonD.setText(currentQuestion.get(3));
    }

    //Each method launches a confirmation dialog after
    //changing userAnswer to the choice the user picked

    public void onAnswerClickA(View view) {
        userAnswer = buttonA.getText().toString();
        if (confirm) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Are you sure you want to select Answer: " + userAnswer);

            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    checkAnswer();
                }
            });

            dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        } else {
            checkAnswer();
        }
    }

    public void onAnswerClickB(View view) {
        userAnswer = buttonB.getText().toString();
        if (confirm) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Are you sure you want to select Answer: " + userAnswer);

            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    checkAnswer();
                }
            });

            dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        } else {
            checkAnswer();
        }
    }

    public void onAnswerClickC(View view) {
        userAnswer = buttonC.getText().toString();
        if (confirm) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Are you sure you want to select Answer: " + userAnswer);

            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    checkAnswer();
                }
            });

            dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();

        } else {
            checkAnswer();
        }
    }

    public void onAnswerClickD(View view) {
        userAnswer = buttonD.getText().toString();
        if (confirm) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Are you sure you want to select Answer: " + userAnswer);

            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    checkAnswer();
                }
            });

            dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();

        } else {
            checkAnswer();
        }
    }

    public void checkAnswer() {
        if (difficultySet)
        {
            buttonA.setVisibility(View.VISIBLE);
            buttonB.setVisibility(View.VISIBLE);
            buttonC.setVisibility(View.VISIBLE);
            buttonD.setVisibility(View.VISIBLE);

//            if (buttonA.toString().equals(correctAnswer))
//            {
//                buttonA.setBackgroundColor(Color.GREEN);
//            }
//            else if (buttonB.toString().equals(correctAnswer))
//            {
//                buttonB.setBackgroundColor(Color.GREEN);
//            }
//            else if (buttonC.toString().equals(correctAnswer))
//            {
//                buttonC.setBackgroundColor(Color.GREEN);
//            }
//            if (buttonD.toString().equals(correctAnswer))
//            {
//                buttonD.setBackgroundColor(Color.GREEN);
//            }

        if (userAnswer.equals(correctAnswer)) {
            //On Correct Answer
            Toasty.success(GameActivity.this, "You are correct!", Toast.LENGTH_SHORT, true).show();
            userCorrectAnswers++;

            if (score == 0) {
                score = (2500 * (difficulty + 1));
            } else if (score >= 20000 && score < 100000) {
                score += (25000 * (difficulty + 1));
            } else {
                score += (score * ((difficulty + 1)) / 2);
            }

            if (userCorrectAnswers == 30) {
                Toasty.success(GameActivity.this, "All questions completed, Well done", Toast.LENGTH_SHORT, true).show();
                score += (200000 * (difficulty + 1));
                gameWin();
            }
            mScoreTextView.setText(scoreText);
            scoreText = (score + cryptoDiff[difficulty]);
            mScoreTextView.setText(scoreText);

            setQuestion();

            try{Thread.sleep(500);}
            catch (InterruptedException ex){
                Toasty.error(GameActivity.this, "Pause Error", Toast.LENGTH_SHORT, true).show();
            }


        }
        else {
                //On failure
                Toasty.error(GameActivity.this, "You are incorrect!", Toast.LENGTH_SHORT, true).show();

//            if (buttonA.toString().equals(userAnswer))
//            {
//                buttonA.setBackgroundColor(Color.RED);
//            }
//            else if (buttonB.toString().equals(userAnswer))
//            {
//                buttonB.setBackgroundColor(Color.RED);
//            }
//            else if (buttonC.toString().equals(userAnswer))
//            {
//                buttonC.setBackgroundColor(Color.RED);
//            }
//            else if (buttonD.toString().equals(userAnswer))
//            {
//                buttonD.setBackgroundColor(Color.RED);
//            }

            try{Thread.sleep(500);}
            catch (InterruptedException ex){
                Toasty.error(GameActivity.this, "Pause Error", Toast.LENGTH_SHORT, true).show();
            }

            if (score >= 1000000){
                        gameWin();
                    }
                else{
                        gameOver();
                    }
            }
        }
        else {
            setDifficulty();
        }
//        buttonA.setBackgroundResource(android.R.drawable.btn_default);
//        buttonB.setBackgroundResource(android.R.drawable.btn_default);
//        buttonC.setBackgroundResource(android.R.drawable.btn_default);
//        buttonD.setBackgroundResource(android.R.drawable.btn_default);
    }

    public void gameOver() {
        //Sends user to game over screen
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("sent_score", score);
        editor.apply();

        Intent gameOver = new Intent(this, GameOverActivity.class);
        startActivity(gameOver);
    }

    public void  gameWin() {
        //Sends user to game win screen
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("sent_score", score);
            editor.putInt("diff", difficulty);
            editor.apply();

            Intent gameWin = new Intent(this, GameWinActivity.class);
            startActivity(gameWin);

    }

    public void on5050Click(View view) {
        if (difficultySet) {
        if (!fiftyfiftyButtonPressed) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Are you sure you want to use 50/50?");

            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    score = (score - (score / 20));
                    Toast.makeText(GameActivity.this, "Penalty of 5%", Toast.LENGTH_SHORT).show();
                    mScoreTextView.setText(scoreText);

                    fiftyfiftyButtonPressed = true;
                    fiftyfiftyButton.setVisibility(View.INVISIBLE);
                    if (buttonA.getText().equals(correctAnswer)) {
                        buttonB.setVisibility(View.INVISIBLE);
                        buttonD.setVisibility(View.INVISIBLE);
                    } else if (buttonB.getText().equals(correctAnswer)) {
                        buttonA.setVisibility(View.INVISIBLE);
                        buttonC.setVisibility(View.INVISIBLE);
                    } else if (buttonC.getText().equals(correctAnswer)) {
                        buttonA.setVisibility(View.INVISIBLE);
                        buttonB.setVisibility(View.INVISIBLE);
                    } else {
                        buttonB.setVisibility(View.INVISIBLE);
                        buttonC.setVisibility(View.INVISIBLE);
                    }
//                    else
//                    {
//                        Toasty.error(GameActivity.this, "Oops", Toast.LENGTH_SHORT, true).show();
//                    }
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
    }
    else{
        setDifficulty();
    }
    }

    public void onPhoneClick(View view){
        if(difficultySet){
        if (!phonebuttonPressed) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Are you sure you want to use Phone a Friend?");

            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    Toast.makeText(GameActivity.this, "Friend thinks the Answer is:  " + correctAnswer + ", Penalty of 10%", Toast.LENGTH_LONG).show();
                    phonebuttonPressed = true;
                    phoneButton.setVisibility(View.INVISIBLE);
                    score = (score - (score / 10));
                    mScoreTextView.setText(scoreText);
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
    }
    else {
        setDifficulty();
        }
    }

    public void onAskClick(View view){
        if (difficultySet) {
            if (!askbuttonPressed) {
            score = (score - (score / 10));
            mScoreTextView.setText(scoreText);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Are you sure you want to use Ask The Audience?");

            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id)
                {

                    Random r = new Random();
                    int genPercentage = r.nextInt(75 - 40) + 40;
                    Toast.makeText(GameActivity.this, genPercentage + "% of the audience thinks the answer is " + correctAnswer + ", Penalty of 10%", Toast.LENGTH_LONG).show();
                    askbuttonPressed = true;
                    audienceButton.setVisibility(View.INVISIBLE);
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
    }
    else{
            setDifficulty();
        }
    }
}