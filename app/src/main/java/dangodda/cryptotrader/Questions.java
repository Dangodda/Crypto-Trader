package dangodda.cryptotrader;

import java.util.ArrayList;

public class Questions
{
    private int questionId;
    private String question;
    private String correctAns;

    public ArrayList<String> getIncorrectAns() {
        return incorrectAns;
    }

    public void setIncorrectAns(ArrayList<String> incorrectAns) {
        this.incorrectAns = incorrectAns;
    }

    private ArrayList<String> incorrectAns;

    public Questions() {
        this.questionId = questionId++;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }


}
