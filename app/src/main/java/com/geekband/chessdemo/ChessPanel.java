package com.geekband.chessdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyper on 2016/5/7.
 */
public class ChessPanel extends View {
    private int mPanelWidth;
    private float mLineHeight;
    private static final int MAX_LINE=10;

    private Paint mPaint;

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float radioPieceOfLineHeight=3*1.0f/4;

    private Boolean isBlack=true;

    private Boolean isOver=false;
    private Boolean isBlackWin=false;

    private int MAXSTEP=5;
    private ArrayList<Point> mWhiteArray=new ArrayList<>();
    private ArrayList<Point> mBlackArray=new ArrayList<>();

    private static String INSTANCE="instance";
    private static String GAME_OVER="game_over";
    private static String INSTANCRZ_WHITE_ARRAY="instance_white_array";
    private static String INSTANCRZ_BLACK_ARRAY="instance_black_array";
    private static String INSTANCRZ_IS_BLACK="instance_is_black";


    public ChessPanel(Context context) {
        this(context,null);
    }

    public ChessPanel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChessPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mPaint=new Paint();

        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.mipmap.stone_w2);
        mBlackPiece= BitmapFactory.decodeResource(getResources(),R.mipmap.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heigthtSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int width=Math.min(widthSize,heigthtSize);

        if (widthMode==MeasureSpec.UNSPECIFIED){
            width=heigthtSize;
        }else if (heightMode==MeasureSpec.UNSPECIFIED){
            width=widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth=w;
        mLineHeight=mPanelWidth*1.0f/MAX_LINE;

        int PieceSize= (int) (mLineHeight*radioPieceOfLineHeight);
        mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,PieceSize,PieceSize,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,PieceSize,PieceSize,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        drawBroad(canvas);
        drawPieces(canvas);
        gameOverCheck();
    }

    public void gameOverCheck() {
        boolean whiteWin=checkFiveInLine(mWhiteArray);
        boolean blackWin=checkFiveInLine(mBlackArray);

        if (whiteWin||blackWin){
            isOver=true;
            isBlackWin=blackWin;

            String text=isBlackWin?"黑子勝":"白子勝";

            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> Points) {

        for(Point p:Points){
            int x=p.x;
            int y=p.y;
            boolean win=checkHorizontal(x,y,Points);
            if (win)return true;
            win=checkVertical(x,y,Points);
            if (win)return true;
            win=checkLeft(x,y,Points);
            if (win)return true;
            win=checkRight(x,y,Points);
            if (win)return true;
        }
        return false;
    }

    public boolean checkLeft(int x, int y, List<Point> points) {
        int cnt=1;
        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x-i,y+i))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }

        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x+i,y-i))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }
        return false;
    }

    public boolean checkRight(int x, int y, List<Point> points) {
        int cnt=1;
        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x-i,y-i))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }

        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x+i,y+i))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }
        return false;
    }

    public boolean checkVertical(int x, int y, List<Point> points) {
        int cnt=1;
        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x,y-i))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }

        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x,y+i))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }
        return false;
    }

    public boolean checkHorizontal(int x, int y, List<Point> points) {
        int cnt=1;
        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x-i,y))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }

        for (int i = 1; i <MAXSTEP ; i++) {
            if(points.contains(new Point(x+i,y))){
                cnt++;
            }else {
                break;
            }
        }
        if (cnt==MAXSTEP){
            return true;
        }
        return false;
    }

    public void drawPieces(Canvas canvas) {

        Log.i("mBlackArray.size()=",String.valueOf(mBlackArray.size()));
        Log.i("mWhiteArray.size()=",String.valueOf(mWhiteArray.size()));

        for (int i = 0; i < mBlackArray.size(); i++) {
            Point blackPoint=mBlackArray.get(i);

            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x+(1-radioPieceOfLineHeight)/2)*mLineHeight,
                    (blackPoint.y+(1-radioPieceOfLineHeight)/2)*mLineHeight,null);
            Log.i("mBlackArray.size()=",String.valueOf(mBlackArray.size()));
        }

        for (int i = 0; i < mWhiteArray.size(); i++) {
            Point whitePoint=mWhiteArray.get(i);

            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x+(1-radioPieceOfLineHeight)/2)*mLineHeight,
                    (whitePoint.y+(1-radioPieceOfLineHeight)/2)*mLineHeight,null);
            Log.i("mWhiteArray.size()=",String.valueOf(mWhiteArray.size()));
        }

        Log.i("DrawPieces:","final");
    }

    public void drawBroad(Canvas canvas) {
        int w=mPanelWidth;
        float LineHeight=mLineHeight;

        for (int i = 0; i <MAX_LINE ; i++) {
            int startX= (int) (LineHeight/2);
            int endX=(int)(w-LineHeight/2);

            int Y= (int) ((0.5+i)*LineHeight);

            canvas.drawLine(startX,Y,endX,Y,mPaint);
            canvas.drawLine(Y,startX,Y,endX,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        if (isOver){
            return false;
        }
        if (action==MotionEvent.ACTION_UP){

            int X= (int) event.getX();
            int Y= (int) event.getY();
            Point point=getValidPoint(X,Y);
            if (mWhiteArray.contains(point)||mBlackArray.contains(point)){
                return false;
            }
            Log.i("isBlack=",String.valueOf(isBlack));
            if (isBlack){
                mBlackArray.add(point);
            }else {
                mWhiteArray.add(point);
            }
            invalidate();
            isBlack=!isBlack;
        }
        return true;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCRZ_IS_BLACK,isBlack);
        bundle.putBoolean(GAME_OVER,isOver);
        bundle.putParcelableArrayList(INSTANCRZ_BLACK_ARRAY,mBlackArray);
        bundle.putParcelableArrayList(INSTANCRZ_WHITE_ARRAY,mWhiteArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle= (Bundle) state;
            isBlack=bundle.getBoolean(INSTANCRZ_IS_BLACK);
            isOver=bundle.getBoolean(GAME_OVER);
            mBlackArray=bundle.getParcelableArrayList(INSTANCRZ_BLACK_ARRAY);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCRZ_WHITE_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x/mLineHeight),(int)(y/mLineHeight));
    }
}
