package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ExamActivity extends Activity {
    public final String TAG = "Answer";
    private int count;
    private int questionNumber = 1;
    private int current;
    private int mode = 0; // 单选0 多选1 判断2
    private boolean wrongMode;//标志变量，判断是否进入错题模式
    private boolean flagAnswer;

    private int timeNum;
    private int singleNum;
    private int judgeNum;
    private int mutichoiceNum;


    public TextView tv_question;
    public TextView tv_remaining_time;
    public TextView user_answer;

    public String nowAnswer = "";

    public RadioButton[] radioButtons = new RadioButton[4];
    public CheckBox checkBox1;
    public CheckBox checkBox2;
    public CheckBox checkBox3;
    public CheckBox checkBox4;
    private boolean[] checkedArray = new boolean[]{false, false, false, false};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        tv_remaining_time = findViewById(R.id.tv_remaining_time);
        Intent intent = getIntent();
        timeNum = intent.getIntExtra("time", 3600);
        singleNum = intent.getIntExtra("single", 90);
        judgeNum = intent.getIntExtra("judge", 65);
        mutichoiceNum = intent.getIntExtra("mutichoice", 45);

        CountDownTimer timer = new CountDownTimer(timeNum * 1000, 1000) {
            /**
             * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
             * @param millisUntilFinished
             */
            @Override
            public void onTick(long millisUntilFinished) {
                tv_remaining_time.setText("剩余时间：" + formatTime(millisUntilFinished));
            }

            /**
             * 倒计时完成时被调用
             */
            @Override
            public void onFinish() {
                tv_remaining_time.setText("时间到：00:00");
                new AlertDialog.Builder(ExamActivity.this)
                        .setTitle("提示")
                        .setMessage("时间到！ 请点击 “下一题” 查看错题")
                        .setPositiveButton("确定", (dialogInterface, which) -> {
                            current = count - 1;
                        }).show();
            }
        };
        timer.start();

        timerStart();
        DBService dbService = new DBService();
        final List<Question> list = dbService.getRandomQuestion(singleNum, judgeNum, mutichoiceNum);

        count = list.size();
        current = 0;
        mode = 0;
        wrongMode = false;//默认情况
        flagAnswer = false;//默认情况
        Log.i(TAG, "onCreate: " + "Time: " + timeNum);
//        final TextView tv_question = findViewById(R.id.question);
        tv_question = findViewById(R.id.question);
        final TextView questionNum = findViewById(R.id.questionNum);
        radioButtons[0] = findViewById(R.id.answerA);
        radioButtons[1] = findViewById(R.id.answerB);
        radioButtons[2] = findViewById(R.id.answerC);
        radioButtons[3] = findViewById(R.id.answerD);
        checkBox1 = findViewById(R.id.c1);
        checkBox2 = findViewById(R.id.c2);
        checkBox3 = findViewById(R.id.c3);
        checkBox4 = findViewById(R.id.c4);

        Button btn_previous = findViewById(R.id.btn_previous);
        Button btn_next = findViewById(R.id.btn_next);

        Button btn_answer = findViewById(R.id.btn_answer);

        final TextView tv_explaination = findViewById(R.id.explaination);
        final TextView user_answer = findViewById(R.id.text_answer);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        //为控件赋值
        Question q = list.get(0);
        nowAnswer = q.explaination;
        questionNum.setText("当前第1道题");
        tv_question.setText(q.question);
//        tv_explaination.setText(q.explaination);
        radioButtons[0].setText(q.answerA);
        radioButtons[1].setText(q.answerB);
        radioButtons[2].setText(q.answerC);
        radioButtons[3].setText(q.answerD);
        checkBox1.setVisibility(View.GONE);
        checkBox2.setVisibility(View.GONE);
        checkBox3.setVisibility(View.GONE);
        checkBox4.setVisibility(View.GONE);

        btn_next.setOnClickListener(view -> {
            flagAnswer=false;
            tv_explaination.setVisibility(View.GONE);
            if (current < count - 1) {//若当前题目不为最后一题，点击next按钮跳转到下一题；否则不响应
                current++;
                questionNumber = current + 1;
                Question q12 = list.get(current);
                mode = q12.mode;

                nowAnswer = q12.explaination;

                if (mode == 0) {
                    Log.i(TAG, "NextMode: 0");
                    // 单选题
                    //更新题目
                    questionNum.setText("当前第" + questionNumber + "道题");
                    tv_question.setText(q12.question);
                    radioButtons[0].setText(q12.answerA);
                    radioButtons[1].setText(q12.answerB);
                    radioButtons[2].setText(q12.answerC);
                    radioButtons[3].setText(q12.answerD);
                    radioButtons[2].setVisibility(View.VISIBLE);
                    radioButtons[3].setVisibility(View.VISIBLE);
                    checkBox1.setVisibility(View.GONE);
                    checkBox2.setVisibility(View.GONE);
                    checkBox3.setVisibility(View.GONE);
                    checkBox4.setVisibility(View.GONE);
                    if (wrongMode) {
                        tv_explaination.setText(q12.explaination);
                        tv_explaination.setVisibility(View.VISIBLE);
                        user_answer.setText(getUserAnswer(q12.selectedAnswer));
                        user_answer.setVisibility(View.VISIBLE);
                        Log.i(TAG, "next" + " " + q12.explaination);
                    }
                    // 若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q12.selectedAnswer != -1) {
                        radioButtons[q12.selectedAnswer].setChecked(true);
                    }

                } else if (mode == 1) {
                    Log.i(TAG, "NextMode: 1");
                    questionNum.setText("当前第" + questionNumber + "道题");
                    tv_question.setText(q12.question);
                    radioButtons[0].setText(q12.answerA);
                    radioButtons[1].setText(q12.answerB);
                    radioButtons[2].setVisibility(View.GONE);
                    radioButtons[3].setVisibility(View.GONE);
                    checkBox1.setVisibility(View.GONE);
                    checkBox2.setVisibility(View.GONE);
                    checkBox3.setVisibility(View.GONE);
                    checkBox4.setVisibility(View.GONE);
                    if (wrongMode) {
                        tv_explaination.setText(q12.explaination);
                        tv_explaination.setVisibility(View.VISIBLE);
                        user_answer.setText(getUserAnswer(q12.selectedAnswer));
                        user_answer.setVisibility(View.VISIBLE);
                        Log.i(TAG, "next" + " " + q12.explaination);
                    }
                    //若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q12.selectedAnswer != -1) {
                        radioButtons[q12.selectedAnswer].setChecked(true);
                    }
                } else if (mode == 2) {
                    Log.i(TAG, "Judge: " + JudgeCheckButton(checkedArray));
                    questionNum.setText("当前第" + questionNumber + "道题");
                    tv_question.setText(q12.question);
                    user_answer.setVisibility(View.GONE);
                    radioButtons[0].setVisibility(View.GONE);
                    radioButtons[1].setVisibility(View.GONE);
                    radioButtons[2].setVisibility(View.GONE);
                    radioButtons[3].setVisibility(View.GONE);

                    checkBox1.setText(q12.answerA);
                    checkBox2.setText(q12.answerB);
                    checkBox3.setText(q12.answerC);
                    checkBox4.setText(q12.answerD);

                    checkBox1.setVisibility(View.VISIBLE);
                    checkBox2.setVisibility(View.VISIBLE);
                    checkBox3.setVisibility(View.VISIBLE);
                    checkBox4.setVisibility(View.VISIBLE);
                    if (wrongMode) {
                        tv_explaination.setText(q12.explaination);
                        tv_explaination.setVisibility(View.VISIBLE);
                        user_answer.setText(getUserAnswer(q12.selectedAnswer));
                        user_answer.setVisibility(View.VISIBLE);
                        Log.i(TAG, "next" + " " + q12.explaination);
                    }
                    //若之前已经选择过，则应记录选择
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                    if (q12.selectedAnswer != -1) {

                        if (q12.selectedAnswer == 4) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(false);
                        } else if (q12.selectedAnswer == 5) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(false);
                        } else if (q12.selectedAnswer == 6) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(true);
                        } else if (q12.selectedAnswer == 7) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(false);
                        } else if (q12.selectedAnswer == 8) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(true);
                        } else if (q12.selectedAnswer == 9) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        } else if (q12.selectedAnswer == 10) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(false);
                        } else if (q12.selectedAnswer == 11) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(true);
                        } else if (q12.selectedAnswer == 12) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        } else if (q12.selectedAnswer == 13) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        } else if (q12.selectedAnswer == 14) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        }
                    }
                }
            }

            //错题模式的最后一题
            else if (current == count - 1 && wrongMode == true) {
                new AlertDialog.Builder(ExamActivity.this)
                        .setTitle("提示")
                        .setMessage("已经到达最后一题，是否退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ExamActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            } else {
                //当前题目为最后一题时，告知用户作答正确的数量和作答错误的数量，并询问用户是否要查看错题
                final List<Integer> wrongList = checkAnswer(list);
                //作对所有题目
                if (wrongList.size() == 0) {
                    timer.cancel();
                    timerCancel();
                    new AlertDialog.Builder(ExamActivity.this)
                            .setTitle("提示")
                            .setMessage("恭喜你全部回答正确！")
                            .setPositiveButton("确定", (dialogInterface, i) -> {
                                ExamActivity.this.finish();
                            }).show();


                } else
                    new AlertDialog.Builder(ExamActivity.this)
                            .setTitle("提示")
                            .setMessage("您答对了" + (list.size() - wrongList.size()) +
                                    "道题目；答错了" + wrongList.size() + "道题目。是否查看错题？")
                            .setPositiveButton("确定", (dialogInterface, which) -> {
                                timer.cancel();
                                timerCancel();
                                //判断进入错题模式
                                questionNum.setText("当前第" + 1 + "道题");
                                wrongMode = true;
                                List<Question> newList = new ArrayList<>();
                                //将错误题目复制到newList中
                                for (int i = 0; i < wrongList.size(); i++) {
                                    newList.add(list.get(wrongList.get(i)));
                                }
                                //将原来的list清空
                                list.clear();
                                //将错误题目添加到原来的list中
                                for (int i = 0; i < newList.size(); i++) {
                                    list.add(newList.get(i));
                                }
                                current = 0;
                                count = list.size();
                                //更新显示时的内容
                                Question q12 = list.get(current);
                                tv_explaination.setText(q12.explaination);
                                //显示解析
                                tv_explaination.setVisibility(View.VISIBLE);
                                if (q12.mode == 0) {
                                    Log.i(TAG, "Mode: 0");
                                    user_answer.setText(getUserAnswer(q12.selectedAnswer));
                                    user_answer.setVisibility(View.VISIBLE);
                                    tv_question.setText(q12.question);
                                    radioButtons[0].setText(q12.answerA);
                                    radioButtons[1].setText(q12.answerB);
                                    radioButtons[2].setText(q12.answerC);
                                    radioButtons[3].setText(q12.answerD);
                                    radioButtons[0].setVisibility(View.VISIBLE);
                                    radioButtons[1].setVisibility(View.VISIBLE);
                                    radioButtons[2].setVisibility(View.VISIBLE);
                                    radioButtons[3].setVisibility(View.VISIBLE);

                                    checkBox1.setVisibility(View.GONE);
                                    checkBox2.setVisibility(View.GONE);
                                    checkBox3.setVisibility(View.GONE);
                                    checkBox4.setVisibility(View.GONE);

                                } else if (q12.mode == 1) {
                                    user_answer.setText(getUserAnswer(q12.selectedAnswer));
                                    user_answer.setVisibility(View.VISIBLE);
                                    Log.i(TAG, "Mode: 1");
                                    tv_question.setText(q12.question);
                                    radioButtons[0].setText(q12.answerA);
                                    radioButtons[1].setText(q12.answerB);
                                    radioButtons[0].setVisibility(View.VISIBLE);
                                    radioButtons[1].setVisibility(View.VISIBLE);
                                    radioButtons[2].setVisibility(View.GONE);
                                    radioButtons[3].setVisibility(View.GONE);
                                    checkBox1.setVisibility(View.GONE);
                                    checkBox2.setVisibility(View.GONE);
                                    checkBox3.setVisibility(View.GONE);
                                    checkBox4.setVisibility(View.GONE);
                                } else if (mode == 2) {
                                    questionNum.setText("当前第" + questionNumber + "道题");
                                    tv_question.setText(q12.question);
                                    user_answer.setVisibility(View.GONE);
                                    radioButtons[0].setVisibility(View.GONE);
                                    radioButtons[1].setVisibility(View.GONE);
                                    radioButtons[2].setVisibility(View.GONE);
                                    radioButtons[3].setVisibility(View.GONE);

                                    checkBox1.setText(q12.answerA);
                                    checkBox2.setText(q12.answerB);
                                    checkBox3.setText(q12.answerC);
                                    checkBox4.setText(q12.answerD);

                                    checkBox1.setVisibility(View.VISIBLE);
                                    checkBox2.setVisibility(View.VISIBLE);
                                    checkBox3.setVisibility(View.VISIBLE);
                                    checkBox4.setVisibility(View.VISIBLE);

                                }

                            })
                            .setNegativeButton("取消", (dialogInterface, which) -> {
                                //点击取消时，关闭当前activity
                                ExamActivity.this.finish();
                            }).show();
            }
        });
        btn_previous.setOnClickListener(view -> {
            tv_explaination.setVisibility(View.GONE);
            if (current > 0)//若当前题目不为第一题，点击previous按钮跳转到上一题；否则不响应
            {
                current--;
                questionNumber = current + 1;
                Question q1 = list.get(current);
                tv_question.setText(q1.question);
                questionNum.setText("当前第" + questionNumber + "道题");

                nowAnswer = q1.explaination;

                if (q1.mode == 0) {
                    Log.i(TAG, "LastMode: 0");
                    radioButtons[0].setText(q1.answerA);
                    radioButtons[1].setText(q1.answerB);
                    radioButtons[2].setText(q1.answerC);
                    radioButtons[3].setText(q1.answerD);
                    radioButtons[0].setVisibility(View.VISIBLE);
                    radioButtons[1].setVisibility(View.VISIBLE);
                    radioButtons[2].setVisibility(View.VISIBLE);
                    radioButtons[3].setVisibility(View.VISIBLE);
                    checkBox1.setVisibility(View.GONE);
                    checkBox2.setVisibility(View.GONE);
                    checkBox3.setVisibility(View.GONE);
                    checkBox4.setVisibility(View.GONE);
                    if (wrongMode) {
                        tv_explaination.setText(q1.explaination);
                        tv_explaination.setVisibility(View.VISIBLE);
                        user_answer.setText(getUserAnswer(q1.selectedAnswer));
                        user_answer.setVisibility(View.VISIBLE);
                        Log.i(TAG, "last" + " " + q.explaination);
                    }

                    //若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q1.selectedAnswer != -1) {
                        radioButtons[q1.selectedAnswer].setChecked(true);
                    }

                } else if (q1.mode == 1) {
                    Log.i(TAG, "LastMode: 1");
                    radioButtons[0].setText(q1.answerA);
                    radioButtons[1].setText(q1.answerB);
                    radioButtons[2].setText(q1.answerB);
                    radioButtons[3].setText(q1.answerB);
                    radioButtons[0].setVisibility(View.VISIBLE);
                    radioButtons[1].setVisibility(View.VISIBLE);
                    radioButtons[2].setVisibility(View.GONE);
                    radioButtons[3].setVisibility(View.GONE);
                    checkBox1.setVisibility(View.GONE);
                    checkBox2.setVisibility(View.GONE);
                    checkBox3.setVisibility(View.GONE);
                    checkBox4.setVisibility(View.GONE);
                    if (wrongMode) {
                        tv_explaination.setText(q1.explaination);
                        tv_explaination.setVisibility(View.VISIBLE);
                        user_answer.setText(getUserAnswer(q1.selectedAnswer));
                        user_answer.setVisibility(View.VISIBLE);
                        Log.i(TAG, "last" + " " + q.explaination);
                    }

                    //若之前已经选择过，则应记录选择
                    radioGroup.clearCheck();
                    if (q1.selectedAnswer != -1) {
                        radioButtons[q1.selectedAnswer].setChecked(true);
                    }
                } else if (mode == 2) {
                    Log.i(TAG, "Judge: " + JudgeCheckButton(checkedArray));
                    questionNum.setText("当前第" + questionNumber + "道题");
                    tv_question.setText(q1.question);
                    user_answer.setVisibility(View.GONE);
                    radioButtons[0].setVisibility(View.GONE);
                    radioButtons[1].setVisibility(View.GONE);
                    radioButtons[2].setVisibility(View.GONE);
                    radioButtons[3].setVisibility(View.GONE);

                    checkBox1.setText(q1.answerA);
                    checkBox2.setText(q1.answerB);
                    checkBox3.setText(q1.answerC);
                    checkBox4.setText(q1.answerD);

                    checkBox1.setVisibility(View.VISIBLE);
                    checkBox2.setVisibility(View.VISIBLE);
                    checkBox3.setVisibility(View.VISIBLE);
                    checkBox4.setVisibility(View.VISIBLE);
                    if (wrongMode) {
                        tv_explaination.setText(q1.explaination);
                        tv_explaination.setVisibility(View.VISIBLE);
                        user_answer.setText(getUserAnswer(q1.selectedAnswer));
                        user_answer.setVisibility(View.VISIBLE);
                        Log.i(TAG, "next" + " " + q1.explaination);
                    }

                    if (q1.selectedAnswer != -1) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        if (q1.selectedAnswer == 4) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(false);
                        } else if (q1.selectedAnswer == 5) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(false);
                        } else if (q1.selectedAnswer == 6) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(true);
                        } else if (q1.selectedAnswer == 7) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(false);
                        } else if (q1.selectedAnswer == 8) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(true);
                        } else if (q1.selectedAnswer == 9) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        } else if (q1.selectedAnswer == 10) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(false);
                        } else if (q1.selectedAnswer == 11) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(false);
                            checkBox4.setChecked(true);
                        } else if (q1.selectedAnswer == 12) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(false);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        } else if (q1.selectedAnswer == 13) {
                            checkBox1.setChecked(false);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        } else if (q1.selectedAnswer == 14) {
                            checkBox1.setChecked(true);
                            checkBox2.setChecked(true);
                            checkBox3.setChecked(true);
                            checkBox4.setChecked(true);
                        }
                    }
//
                }

            }

        });

        btn_answer.setOnClickListener(view -> {
//            if(flg)
            tv_explaination.setText(nowAnswer);
            tv_explaination.setVisibility(View.VISIBLE);
//            user_answer.setVisibility(View.VISIBLE);
//            user_answer.setText(nowAnswer);
            Log.i(TAG, "BTN_Answer : " + nowAnswer);
        });

        //选择选项时更新选择
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            for (int i = 0; i < 4; i++) {
                if (radioButtons[i].isChecked() == true) {
                    list.get(current).selectedAnswer = i;
                    break;
                }
            }

        });
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedArray[0] = isChecked;

            list.get(current).selectedAnswer = JudgeCheckButton(checkedArray);
            Log.i(TAG, "C4: T" + " " + checkedArray[0] + " " + list.get(current).selectedAnswer);
        });
        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedArray[1] = isChecked;

            list.get(current).selectedAnswer = JudgeCheckButton(checkedArray);
            Log.i(TAG, "C4: T" + " " + checkedArray[1] + " " + list.get(current).selectedAnswer);
        });
        checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedArray[2] = isChecked;

            list.get(current).selectedAnswer = JudgeCheckButton(checkedArray);
            Log.i(TAG, "C4: T" + " " + checkedArray[2] + " " + list.get(current).selectedAnswer);
        });
        checkBox4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedArray[3] = isChecked;
            list.get(current).selectedAnswer = JudgeCheckButton(checkedArray);
            Log.i(TAG, "C4: T" + " " + checkedArray[3] + " " + list.get(current).selectedAnswer);
        });

        Log.i(TAG, "Judge: " + JudgeCheckButton(checkedArray));
    }

    public int JudgeCheckButton(boolean[] checkedArray) {
        int select = 0;
        if (checkedArray[0] && checkedArray[1] && !checkedArray[2] && !checkedArray[3]) { //AB
            select = 4;
        } else if (checkedArray[0] && checkedArray[2] && !checkedArray[1] && !checkedArray[3]) { //AC
            select = 5;
        } else if (checkedArray[0] && checkedArray[3] && !checkedArray[1] && !checkedArray[2]) { //AD
            select = 6;
        } else if (checkedArray[1] && checkedArray[2] && !checkedArray[0] && !checkedArray[3]) { //BC
            select = 7;
        } else if (checkedArray[1] && checkedArray[3] && !checkedArray[0] && !checkedArray[2]) { //BD
            select = 8;
        } else if (checkedArray[2] && checkedArray[3] && !checkedArray[0] && !checkedArray[1]) { //CD
            select = 9;
        } else if (checkedArray[0] && checkedArray[1] && checkedArray[2] && !checkedArray[3]) { //ABC
            select = 10;
        } else if (checkedArray[0] && checkedArray[1] && checkedArray[3] && !checkedArray[2]) { //ABD
            select = 11;
        } else if (checkedArray[0] && checkedArray[2] && checkedArray[3] && !checkedArray[1]) { //ACD
            select = 12;
        } else if (checkedArray[1] && checkedArray[2] && checkedArray[3] && !checkedArray[0]) { //BCD
            select = 13;
        } else if (checkedArray[0] && checkedArray[1] && checkedArray[2] && checkedArray[3]) { //BCD
            select = 14;
        }
        Log.i(TAG, "JudgeCheckButton: " + select);
        return select;
    }

    public String getUserAnswer(int answer) {
        if (answer == 0)
            return "你的答案： A";
        else if (answer == 1)
            return "你的答案： B";
        else if (answer == 2)
            return "你的答案： C";
        else if (answer == 3)
            return "你的答案： D";
        else if (answer == 4)
            return "你的答案： AB";
        else if (answer == 5)
            return "你的答案： AC";
        else if (answer == 6)
            return "你的答案： AD";
        else if (answer == 7)
            return "你的答案： BC";
        else if (answer == 8)
            return "你的答案： BD";
        else if (answer == 9)
            return "你的答案： CD";
        else if (answer == 10)
            return "你的答案： ABC";
        else if (answer == 11)
            return "你的答案： ABD";
        else if (answer == 12)
            return "你的答案： ACD";
        else if (answer == 13)
            return "你的答案： BCD";
        else if (answer == 14)
            return "你的答案： ABCD";
        return "你的答案：空";
    }

    /*
判断用户作答是否正确，并将作答错误题目的下标生成list,返回给调用者
 */
    private List<Integer> checkAnswer(List<Question> list) {
        List<Integer> wrongList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i).mode!=2){
            if (true) {
                if (list.get(i).answer != list.get(i).selectedAnswer) {
                    wrongList.add(i);
                    Log.i(TAG, "checkAnswer: " + list.get(i).question + " " + list.get(i).selectedAnswer);
                }
            }
        }
        return wrongList;
    }

//    private CountDownTimer timer = new CountDownTimer(timeNum * 1000 + 1000 * 10, 1000) {
//        /**
//         * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
//         * @param millisUntilFinished
//         */
//        @Override
//        public void onTick(long millisUntilFinished) {
//            tv_remaining_time.setText("剩余时间：" + formatTime(millisUntilFinished));
//        }
//
//        /**
//         * 倒计时完成时被调用
//         */
//        @Override
//        public void onFinish() {
//            tv_remaining_time.setText("时间到：00:00");
//            new AlertDialog.Builder(ExamActivity.this)
//                    .setTitle("提示")
//                    .setMessage("时间到！ 请点击 “下一题” 查看错题")
//                    .setPositiveButton("确定", (dialogInterface, which) -> {
//                        current = count - 1;
//                    }).show();
//        }
//    };

    public String formatTime(long millisecond) {
        int minute;//分钟
        int second;//秒数
        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return "0" + minute + ":" + "0" + second;
            } else {
                return "0" + minute + ":" + second;
            }
        } else {
            if (second < 10) {
                return minute + ":" + "0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }

    public void timerCancel() {
//        timer.cancel();
    }

    public void timerStart() {
//        timer.start();
    }
}
