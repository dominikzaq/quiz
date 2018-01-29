package com.pkproject.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private int idQuestion;
    private String question;
    private String[] answer = new String[5];
    private char correctAnswer;
    private char userAnswer;

    public int getIdQuestion() {

        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(char correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public char getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(char userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean checkAnswer() {
        return correctAnswer == userAnswer;
    }
}
