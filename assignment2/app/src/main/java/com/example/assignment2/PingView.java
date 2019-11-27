package com.example.assignment2;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class PingView extends View {


    private AttributeSet attrs;

    private Paint p1_paint;
    private Paint p2_paint;

    private Player player1;
    private Player player2;

    private int recW;
    private int recH;
    private int w;
    private int h;

    private Ball theBall;

    private Canvas myCanvas;
    private enum Status {
        NEW,
        RUNNING
    }

    Status gameStatus;

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

        theBall = new Ball(50,50,60,Color.BLACK);

        gameStatus = Status.NEW;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        h = resolveSizeAndState(MeasureSpec.getSize(w) , heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
        int middle = (w/2) - (recW/2);

        RectF rec1 = new RectF(middle, 0,(middle+recW),recH);
        player1 = new Player(rec1,p1_paint);

        RectF rec2 = new RectF(middle, h , (middle+recW), (h-recH));
        player2 = new Player(rec2,p2_paint);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        myCanvas = canvas;

        canvas.drawRect(player1.getRec(), player1.getPaint());
        canvas.drawRect(player2.getRec(), player2.getPaint());

        if(gameStatus == Status.NEW){
            theBall.setOval(new RectF(w/2,h/2,w/2+theBall.getSize(),h/2+theBall.getSize()));
        }else{
            moveBall(canvas,player1.getRec());
        }
        canvas.drawOval(theBall.getOval(),theBall.getPaint());

        invalidate();
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void movePaddle(float moveValue, Player p){

        float newLeft = p.getRec().left + moveValue;
        if(newLeft <= 0) {
            newLeft = 0;
        }else if(newLeft >= w){
            newLeft = (w-recW -4);
        }

        p.getRec().offsetTo(newLeft, p.getRec().top);
    }


    public void moveBall(Canvas canvas, RectF player1) {

        gameStatus = Status.RUNNING;

        int x= theBall.getX();
        int y = theBall.getY();
        int speed = theBall.getSpeed();
        int[] direction = theBall.getDirection();
        int size = theBall.getSize();

        x += speed*direction[0];
        theBall.setX(x);
        y += speed*direction[1];
        theBall.setY(y);

        theBall.setOval(new RectF(x-size/2,y-size/2,x+size/2,y+size/2));

        Rect bounds = new Rect();
        theBall.getOval().roundOut(bounds);

        if(!canvas.getClipBounds().contains(bounds)){
            if(x-size<0 || x+size > canvas.getWidth()){
                direction[0] = direction[0]*-1;
            }
            if(y-size<0 || y+size > canvas.getHeight()){
                if(theBall.getOval().intersect(player1)){
                    Log.i("collid", "BANG");
                }else{
                    Log.i("collid", "LOSE");
                }
                direction[1] = direction[1]*-1;
            }

            theBall.setDirection(direction);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(myCanvas !=null){
            moveBall(myCanvas, player1.getRec());
            Log.i("CANN", "heyy");
        }
        return true;
    }

}