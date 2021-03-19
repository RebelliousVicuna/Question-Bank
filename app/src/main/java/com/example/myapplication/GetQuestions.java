package com.example.myapplication;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class GetQuestions {
    public List<Question> getQuestion() {
        List<Question> list = new ArrayList<Question>();
        Question question = new Question();
        question.ID = 1;
        question.mode = 0;
        question.question = "单选1";
        question.answerA = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        question.answerB = "B";
        question.answerC = "C";
        question.answerD = "D";
        question.answer = 0;

        question.explaination = "答案：A";
        //表示没有选择任何选项
        question.selectedAnswer = -1;
        list.add(question);

        Question question1 = new Question();
        question1.ID = 1;
        question.mode = 0;
        question1.question = "单选2";
        question1.answerA = "A";
        question1.answerB = "BBBBBBBBBBBBBBBBB";
        question1.answerC = "C";
        question1.answerD = "D";
        question1.answer = 1;

        question1.explaination = "答案：B";
        //表示没有选择任何选项
        question1.selectedAnswer = -1;
        list.add(question1);

        Question question2 = new Question();
        question2.ID = 1;
        question.mode = 0;
        question2.question = "单选3";
        question2.answerA = "A";
        question2.answerB = "B";
        question2.answerC = "CCCCCCCCCCCCCCCCCCCCCCC";
        question2.answerD = "D";
        question2.answer = 2;

        question2.explaination = "答案：C";
        //表示没有选择任何选项
        question2.selectedAnswer = -1;
        list.add(question2);

        Question question3 = new Question();
        question3.ID = 1;
        question3.mode = 1;
        question3.question = "判断1";
        question3.answerA = "A";
        question3.answerB = "B";
        question3.answer = 0;

        question3.explaination = "答案：A";
        //表示没有选择任何选项
        question3.selectedAnswer = -1;
        list.add(question3);

        Question question4 = new Question();
        question4.ID = 1;
        question4.mode = 1;
        question4.question = "判断2";
        question4.answerA = "A";
        question4.answerB = "B";

        question4.answer = 1;

        question4.explaination = "答案：B";
        //表示没有选择任何选项
        question4.selectedAnswer = -1;
        list.add(question4);

        Question question5 = new Question();
        question5.ID = 1;
        question5.mode = 1;
        question5.question = "判断3";
        question5.answerA = "A";
        question5.answerB = "B";
        question5.answer = 0;

        question5.explaination = "答案：A";
        //表示没有选择任何选项
        question5.selectedAnswer = -1;
        list.add(question5);

        Question question6 = new Question();
        question6.ID = 1;
        question6.mode = 2;
        question6.question = "多选1！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！";
        question6.answerA = "A";
        question6.answerB = "B";
        question6.answerC = "C";
        question6.answerD = "D";
        question6.answer = getAnswer("ABCD");

        question6.explaination = "答案：ABCD";
        //表示没有选择任何选项
        question6.selectedAnswer = -1;
        list.add(question6);

        Question question7 = new Question();
        question7.ID = 1;
        question7.mode = 2;
        question7.question = "多选2";
        question7.answerA = "A";
        question7.answerB = "B";
        question7.answerC = "C";
        question7.answerD = "D";
        question7.answer = getAnswer("BC");

        question7.explaination = "答案：BC";
        //表示没有选择任何选项
        question7.selectedAnswer = -1;
        list.add(question7);

        Question question8 = new Question();
        question8.ID = 1;
        question8.mode = 2;
        question8.question = "多选3";
        question8.answerA = "A";
        question8.answerB = "B";
        question8.answerC = "C";
        question8.answerD = "D";
        question8.answer = getAnswer("AB");

        question8.explaination = "答案：AB";
        //表示没有选择任何选项
        question8.selectedAnswer = -1;
        list.add(question8);


        return list;
    }

    public int getAnswer(String string) {
        int answer = 0;
        if(string.equals("AB"))
            answer = 4;
        else if (string.equals("AC"))
            answer = 5;
         else if (string.equals("AD"))
            answer = 6;
         else if (string.equals("BC"))
            answer = 7;
         else if (string.equals("BD"))
            answer = 8;
         else if (string.equals("CD"))
            answer = 9;
         else if (string.equals("ABC"))
            answer = 10;
         else if (string.equals("ABD"))
            answer = 11;
         else if (string.equals("ACD"))
            answer = 12;
         else if (string.equals("BCD"))
            answer = 13;
         else if (string.equals("ABCD"))
            answer = 14;
        return answer;
    }
}
