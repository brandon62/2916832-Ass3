package com.example.brandon.defachelleclouetbrandon_2916832_ass3_journeytracker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 *Created by Brandon on 20/04/2016.
 */
public class GPSView extends View
{
    boolean mIsActive;
    private Paint rectPaint, mPaint, averagePaint, speedPaint;
    int  sideSize;
     CircularQueue queue;
     float previousSpeed;
     int i;
     CircularQueue.Node _head;

    public GPSView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mIsActive=false;
        previousSpeed=0;
        init();
    }

    public boolean isActive()
    {
        return mIsActive;
    }
    public void TurnOnOff()
    {
        mIsActive = !mIsActive;
    }

    private void init()
    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(50f);

        averagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        averagePaint.setStrokeWidth(1);
        averagePaint.setColor(Color.RED);

        speedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        speedPaint.setStrokeWidth(1);
        speedPaint.setColor(Color.GREEN);

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setStrokeWidth(1);
        rectPaint.setColor(Color.BLACK);
    }
    public void onSizeChanged(int w, int h, int oldw, int oldh)
    {

        if(w>h)
        {
            sideSize=h;
        }
        else
        {
            sideSize=w;
        }
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //BlackRect
        canvas.drawRect(0, 0, sideSize, sideSize, rectPaint);
        //whiteLines
        canvas.drawLine(0, sideSize - (sideSize / 6), sideSize, sideSize - (sideSize / 6), mPaint);
        canvas.drawLine(0, sideSize - 2 * (sideSize / 6), sideSize, sideSize - 2 * (sideSize / 6), mPaint);
        canvas.drawLine(0, sideSize - 3 * (sideSize / 6), sideSize, sideSize - 3 * (sideSize / 6), mPaint);
        canvas.drawLine(0, sideSize - 4 * (sideSize / 6), sideSize, sideSize - 4 * (sideSize / 6), mPaint);
        canvas.drawLine(0, sideSize - 5 * (sideSize / 6), sideSize, sideSize - 5 * (sideSize / 6), mPaint);

       if(mIsActive)
       {
           queue = MainActivity.queue;
           Log.d("onDraw", "Printing samples");
           _head = queue.head;
           i = 0;
           if(queue.getTime()<30)
           {
               previousSpeed=0;
           }
           while(_head!=queue.tail)
           {
               //draw i-th sample
               canvas.drawLine((sideSize / 30) * i, sideSize - previousSpeed * (sideSize / 60),
                       (sideSize / 30) * (++i), sideSize - 3.6f * _head.key.getSpeed() * (sideSize / 60), speedPaint);
               //Update variables

               previousSpeed = _head.key.getSpeed() * 3.6f;
               _head=_head.next;
           }
            //Special case for the Tail
           canvas.drawLine((sideSize / 30) * i, sideSize - previousSpeed * (sideSize / 60),
                   (sideSize / 30) * (++i), sideSize - 3.6f * _head.key.getSpeed() * (sideSize / 60), speedPaint);
           //Update then print AverageLine
           previousSpeed = queue.head.key.getSpeed()*3.6f;
           canvas.drawLine(0, sideSize - queue.getAverage() * (sideSize / 60), sideSize, sideSize - queue.getAverage() * (sideSize / 60), averagePaint);
       }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }
}
