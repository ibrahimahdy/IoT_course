package com.example.assignment2;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;



public class PingView extends View {


    private AttributeSet attrs;
    private Paint p1_paint;
    private Paint p2_paint;
    private Paint ball;

    private Player player1;
    private Player player2;
    private int recW;
    private int recH;

    private Ball theBall;
    private int ballSize;

    Context myContext;

    public PingView(Context context) {
        super(context);
        myContext = context;
        init();
    }


    private void init() {

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PingView, 0, 0);

        try {
            recW = a.getInteger(R.styleable.PingView_recWidth, 200);
            recH = a.getInteger(R.styleable.PingView_recHeight, 35);
            ballSize = a.getInteger(R.styleable.PingView_ballRadius, 20);
        } finally {
            a.recycle();
        }

        p1_paint = new Paint(0);
        p1_paint.setColor(Color.BLUE);
        p1_paint.setAntiAlias(true);
        p1_paint.setStrokeWidth(80f);

        p2_paint = new Paint(0);
        p2_paint.setColor(Color.RED);
        p2_paint.setAntiAlias(true);
        p2_paint.setStrokeWidth(80f);

        theBall = new Ball(50,50,100,Color.BLACK);

    }

    int w;
    int h;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        h = resolveSizeAndState(MeasureSpec.getSize(w) , heightMeasureSpec, 0);
        setMeasuredDimension(w, h);

        RectF rec1 = new RectF(0, 0,recW,recH);
        player1 = new Player(rec1,p1_paint);

        RectF rec2 = new RectF(0, h , recW, (h-recH));
        player2 = new Player(rec2,p2_paint);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(player1.getRec(), player1.getPaint());
        canvas.drawRect(player2.getRec(), player2.getPaint());
        theBall.move(canvas);
        canvas.drawOval(theBall.oval,theBall.paint);

        invalidate();
    }

    public void movePaddle(float moveValue){
        float newLeft = player1.getRec().left + moveValue;
        if(newLeft <= 0) {
            newLeft = 0;
        }else if(newLeft >= w){
            newLeft = (w-recW -4);
        }

        player1.getRec().offsetTo(newLeft, player1.getRec().top);

    }




}