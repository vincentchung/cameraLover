
package com.pictelligent.s40.camera5in1.components;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;

public class ListItem {
    private int mBgColorNormal = 0x000000;
    private int mBgColorPressed = 0x000000;
    private int mTextColorNormal = 0xffffff;
    private int mTextColorPressed = 0x000000;
    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;
    private int mId;
    private ListItemClickListener mListener;
    private boolean mPressing = false;
    private Image mIcon;
    private String mText;
    private int mTextLocationX=0;
    private int mTextLocationY=0;

    public ListItem(int id, String imagePath, String text) {
        mId = id;
        mText = text;
        try {
            mIcon = Image.createImage(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPosition(int x, int y, int w, int h) {
        mX = x;
        mY = y;
        mWidth = w;
        mHeight = h;
    }
    
    public void setPosition(int x, int y) {
        mX = x;
        mY = y;
        mWidth = mIcon.getWidth();
        mHeight = mIcon.getHeight();
    }

    public void setOnClickListener(ListItemClickListener listener) {
        mListener = listener;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void render(Graphics g) {
        g.translate(-g.getTranslateX(), -g.getTranslateY());
        g.translate(mX, mY);

        int x = 0;
        int y = 0;

        // Background
        g.setColor(mPressing ? mBgColorPressed : mBgColorNormal);
        g.fillRect(0, 0, mWidth, mHeight);

        // Icon
        x = 0;
        y = (mHeight - mIcon.getHeight()) / 2;
        g.drawImage(mIcon, x, y, 0);

        // Text
        g.setColor(mPressing ? mTextColorPressed : mTextColorNormal);
        //left
        g.setFont(Font.getFont(Camera5in1.mFontFace ,Camera5in1.mFontType, Camera5in1.mFontSize));
        
        if(mRTL)
        {
        	x = mWidth-g.getFont().stringWidth(mText)-10;
        }else
        {
        	x = (mWidth) / 4;
        }
        
        y = (mHeight - g.getFont().getHeight()) / 2;
        g.drawString(mText, x, y, 0);
    }
    
    private boolean mRTL=false;
    public void setRTL()
    {
    	mRTL=true;
    }

    public void onPressed(int x, int y) {
        if ((x >= mX && x <= (mX + mWidth))
                && (y >= mY && y < mY + mHeight)) {
            mPressing = true;
        } else {
            mPressing = false;
        }
    }
    
    public void SetUnPressedColor(int c)
    {
    	mBgColorNormal=c;
    }
    
    public void SetPressedColor(int c)
    {
    	mBgColorPressed=c;
    }
    

    public void onUnPressed(int x, int y) {
        if (mPressing) {
            mPressing = false;
            if ((x >= mX && x <= (mX + mWidth))
                    && (y >= mY && y < mY + mHeight)
                    && (mListener != null)) {
                mListener.onClick(mId);
            }
        }
    }
}
