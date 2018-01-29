package com.pkproject.server;

import com.pkproject.objects.Question;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    public List<Question> getQuestion() {
        List<Question>questions = new ArrayList<Question>();

        Question q = new Question();
        q.setIdQuestion(1);
        String s [] = new  String[4];
        s[0] = "aa";
        s[1] = "b";
        s[2] = "c";
        s[3] = "d";
        q.setAnswer(s);
        q.setCorrectAnswer('a');
        q.setQuestion("aaaaaaaaaaaaaa");

        questions.add(q);
        System.out.println(q);

        q = new Question();
        q.setIdQuestion(2);
        s[0] = "a";
        s[1] = "bb";
        s[2] = "c";
        s[3] = "d";
        q.setAnswer(s);
        q.setCorrectAnswer('b');
        q.setQuestion("bbbbbb");
        questions.add(q);


        q = new Question();
        q.setIdQuestion(1);
        s[0] = "a";
        s[1] = "b";
        s[2] = "cc";
        s[3] = "d";
        q.setAnswer(s);
        q.setCorrectAnswer('c');
        q.setQuestion("ccccccccc");
        questions.add(q);


        q = new Question();
        q.setIdQuestion(3);
        s[0] = "a";
        s[1] = "b";
        s[2] = "c";
        s[3] = "d";
        q.setAnswer(s);
        q.setCorrectAnswer('d');
        q.setQuestion("ddddddddd");
        questions.add(q);

        return questions;
    }
}
