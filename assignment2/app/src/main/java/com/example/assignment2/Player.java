package com.example.assignment2;

import android.graphics.Paint;
import android.graphics.RectF;

public class Player {


    private int score;
    private int collision;
    private RectF rec;
    private Paint paint;


    public Player(RectF rec, Paint paint){
        this.rec = rec;
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

    public RectF getRec() {
        return rec;
    }

    public void setRec(RectF rec) {
        this.rec = rec;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public int getCollision() {
        return collision;
    }

    public void setCollision(int collision) {
        this.collision = collision;
    }
}
