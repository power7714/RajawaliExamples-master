package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.monyetmabuk.rajawali.tutorials.R;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class BorderImage extends ImageView{

    protected Bitmap borderBmp;
    Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

    public void setBorder(Bitmap bBmp){
        borderBmp = bBmp;

    }

    public BorderImage(Context context) {
        super(context);
        if(!isInEditMode())
            setBorder(myBitmap);

    }

    public BorderImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            setBorder(myBitmap);
    }

    public BorderImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
            setBorder(myBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){


        if(isInEditMode())
            setBorder(myBitmap);

        canvas.drawBitmap(borderBmp, canvas.getWidth(), canvas.getHeight(), null);
    }
}
