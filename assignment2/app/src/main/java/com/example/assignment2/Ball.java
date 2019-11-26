package com.example.assignment2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;


public class Ball{
    public int[] direction = new int[]{1,1}; //direction modifier (-1,1)
    public int x,y,size;
    public int speed = 10;
    public Paint paint;
    public RectF oval;

    public Ball(int x, int y, int size, int color){
        this.x = x;
        this.y = y;
        this.size = size;
        this.paint = new Paint();
        this.paint.setColor(color);
    }

    public void move(Canvas canvas) {
        this.x += speed*direction[0];
        this.y += speed*direction[1];
        this.oval = new RectF(x-size/2,y-size/2,x+size/2,y+size/2);

        //Do we need to bounce next time?
        Rect bounds = new Rect();
        this.oval.roundOut(bounds); ///store our int bounds

        //This is what you're looking for ▼
        if(!canvas.getClipBounds().contains(bounds)){
            if(this.x-size<0 || this.x+size > canvas.getWidth()){
                direction[0] = direction[0]*-1;
            }
            if(this.y-size<0 || this.y+size > canvas.getHeight()){
                direction[1] = direction[1]*-1;
            }
        }
    }
}