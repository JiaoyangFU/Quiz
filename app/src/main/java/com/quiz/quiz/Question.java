package com.quiz.quiz;

/**
 * Created by fujiaoyang1 on 10/19/16.
 */
public class Question {

    private String content; // question content
    private String opt_a, opt_b, opt_c, opt_d, answer;
    private int time; // seconds;

    public Question(String content, String opt_a, String opt_b, String opt_c,
                    String opt_d, String answer, int time ) {
        this.content = content;
        this.opt_a = opt_a;
        this.opt_b = opt_b;
        this.opt_c = opt_c;
        this.opt_d = opt_d;
        this.answer = answer;
        this.time = time;
    }

    public String getContent() {return content;}
    public String getOpt_a() {return opt_a;}
    public String getOpt_b() {return opt_b;}
    public String getOpt_c() {return opt_c;}
    public String getOpt_d() {return opt_d;}
    public String getAnswer() {return answer;}
}

