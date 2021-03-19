package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class DBService {
    private SQLiteDatabase db;

    //在构造函数中打开指定数据库，并把它的引用指向db
    public DBService() {
        db = SQLiteDatabase.openDatabase(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/databases/question.db" +
                "", null, SQLiteDatabase.OPEN_READWRITE);
    }

    //按顺序获取数据库中的问题
    public List<Question> getQuestion() {
        List<Question> list = new ArrayList<Question>();
        /*
        Cursor是结果集游标，用于对结果集进行随机访问,其实Cursor与JDBC中的ResultSet作用很相似。
        rawQuery()方法的第一个参数为select语句；第二个参数为select语句中占位符参数的值，
        如果select语句没有使用占位符，该参数可以设置为null。*/
        // 单选 判断 多选     question表
        Cursor cursor = db.rawQuery("select * from Single", null);
        Cursor cursor2 = db.rawQuery("select * from Judge", null);
        Cursor cursor3 = db.rawQuery("select * from MutiChoice", null);

        //单选
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();//将cursor移动到第一个光标上
            int count = cursor.getCount();
            //将cursor中的每一条记录生成一个question对象，并将该question对象添加到list中
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                Question question = new Question();
                question.ID = cursor.getInt(cursor.getColumnIndex("ID"));
                question.mode = 0;
                question.question = cursor.getString(cursor.getColumnIndex("question"));
                question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
                question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
                question.answer = cursor.getInt(cursor.getColumnIndex("answer"));
                question.explaination = cursor.getString(cursor.getColumnIndex("explanations"));
                //表示没有选择任何选项
                question.selectedAnswer = -1;
                list.add(question);
            }
        }

        //判断
        if (cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            int count = cursor2.getCount();

            for (int i = 0; i < count; i++) {
                cursor2.moveToPosition(i);
                Question question = new Question();
                question.ID = cursor2.getInt(cursor2.getColumnIndex("ID"));
                question.mode = 1;
                question.question = cursor2.getString(cursor2.getColumnIndex("question"));
                question.answerA = cursor2.getString(cursor2.getColumnIndex("answerA"));
                question.answerB = cursor2.getString(cursor2.getColumnIndex("answerB"));
                question.answer = cursor2.getInt(cursor2.getColumnIndex("answer"));
                question.explaination = cursor2.getString(cursor2.getColumnIndex("explanations"));
                //表示没有选择任何选项
                question.selectedAnswer = -1;
                list.add(question);
            }
        }

        //  多选
        if (cursor3.getCount() > 0) {
            cursor3.moveToFirst();
            int count = cursor3.getCount();
            for (int i = 0; i < count; i++) {
                cursor3.moveToPosition(i);
                Question question = new Question();
                question.ID = cursor3.getInt(cursor3.getColumnIndex("ID"));
                question.mode = 2;
                question.question = cursor3.getString(cursor3.getColumnIndex("question"));
                question.answerA = cursor3.getString(cursor3.getColumnIndex("answerA"));
                question.answerB = cursor3.getString(cursor3.getColumnIndex("answerB"));
                question.answerC = cursor3.getString(cursor3.getColumnIndex("answerC"));
                question.answerD = cursor3.getString(cursor3.getColumnIndex("answerD"));
                question.answer = cursor3.getInt(cursor3.getColumnIndex("answer"));
                question.explaination = cursor3.getString(cursor3.getColumnIndex("explanations"));
                //表示没有选择任何选项
                question.selectedAnswer = -1;
                list.add(question);
            }
        }
        return list;
    }

    // 生成随机数 查找数据库内容并添加

    public List<Question> getRandomQuestion(int singleNum, int judgeNum, int mutichoiceNum) {
        List<Question> list = new ArrayList<Question>();
        int flag = 0;
        int flag2 = 0;
        int flag3 = 0;

        int[] array = new int[999];
        int[] array2 = new int[999];
        int[] array3 = new int[999];
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
            array2[i] = 0;
            array3[i] = 0;
        }
        // 单选
        for (int i = 0; flag < singleNum; i++) {
            int number = (int) (1 + Math.random() * (712));
            if (array[number] == 0) {
                array[number] = 1;
                Cursor cursor = db.rawQuery("select * from Single where ID= " + number, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();//将cursor移动到第一个光标上
                    int count = cursor.getCount();
                    //将cursor中的每一条记录生成一个question对象，并将该question对象添加到list中
                    for (int j = 0; j < count; j++) {
                        cursor.moveToPosition(j);
                        Question question = new Question();
                        question.mode = 0;
                        question.question = cursor.getString(cursor.getColumnIndex("question"));
                        question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                        question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                        question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
                        question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
                        question.answer = cursor.getInt(cursor.getColumnIndex("answer"));
                        question.explaination = cursor.getString(cursor.getColumnIndex("explainations"));
                        //表示没有选择任何选项
                        question.selectedAnswer = -1;
                        list.add(question);
                    }
                }
                flag++;
            }
        }

        // 判断
        for (int i = 0; flag2 < judgeNum; i++) {
            int number = (int) (1 + Math.random() * (274));
            if (array2[number] == 0) {
                array2[number] = 1;
                Cursor cursor = db.rawQuery("select * from Judge where ID= " + number, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int count = cursor.getCount();

                    for (int j = 0; j < count; j++) {
                        cursor.moveToPosition(j);
                        Question question = new Question();
                        question.mode = 1;
                        question.question = cursor.getString(cursor.getColumnIndex("question"));
                        question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                        question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                        question.answer = cursor.getInt(cursor.getColumnIndex("answer"));
                        question.explaination = cursor.getString(cursor.getColumnIndex("explainations"));
                        //表示没有选择任何选项
                        question.selectedAnswer = -1;
                        list.add(question);
                    }
                }
                flag2++;
            }
        }

        // 多选
        for (int i = 0; flag3 < mutichoiceNum; i++) {
            int number = (int) (1 + Math.random() * (306));
            if (array3[number] == 0) {
                array3[number] = 1;
                Cursor cursor = db.rawQuery("select * from MutiChoice where ID= " + number, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();//将cursor移动到第一个光标上
                    int count = cursor.getCount();
                    //将cursor中的每一条记录生成一个question对象，并将该question对象添加到list中
                    for (int j = 0; j < count; j++) {
                        cursor.moveToPosition(j);
                        Question question = new Question();
                        question.mode = 2;
                        question.question = cursor.getString(cursor.getColumnIndex("question"));
                        question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
                        question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
                        question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
                        question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
                        question.answer = cursor.getInt(cursor.getColumnIndex("answer"));
                        question.explaination = cursor.getString(cursor.getColumnIndex("explainations"));
                        //表示没有选择任何选项
                        question.selectedAnswer = -1;
                        list.add(question);
                    }
                }
                flag3++;
            }
        }
        return list;
    }
}

