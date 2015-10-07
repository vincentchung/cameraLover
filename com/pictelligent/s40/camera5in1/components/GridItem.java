
package com.pictelligent.s40.camera5in1.components;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.language.Language;

public class GridItem {
    private int mBgColorNormal = 0;
    private int mBgColorPressed = 0;
    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;
    private int mId;
    private GridItemClickListener mListener;
    private boolean mPressing = false;
    private Image mIcon;
    private String mItemStr=null;

    public GridItem(int id, String imagePath, int colorNormal , int colorPressed) {
        mId = id;
        mBgColorNormal = colorNormal;
        mBgColorPressed = colorPressed;
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

    public void setOnClickListener(GridItemClickListener listener) {
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
        x = (mWidth - mIcon.getWidth()) / 2;
        y = (mHeight - mIcon.getHeight()) / 2;
        g.drawImage(mIcon, x, y, 0);
        //x = (mWidth - mIcon.getWidth()) / 2 + 2;
        //y = (mHeight - (mIcon.getHeight()/4));
        //if(mItemStr!=null)
        
        if(mItemStr!=null)
        {	g.setColor(0xEEEEEE);
        	g.setFont(Font.getFont(Font.FACE_MONOSPACE , Font.STYLE_PLAIN, Font.SIZE_SMALL));
            //x = (mWidth) / 4;
            //y = (mHeight - g.getFont().getHeight()) / 4;
            
        	x = (mWidth - g.getFont().stringWidth(mItemStr)) / 2;
            y = (mHeight - (mIcon.getHeight()/4))-12;
            g.drawString(mItemStr, x, y, 0);
        }
    }
    public void setString(String str)
    {
    	mItemStr=str;
    }

    public void onPressed(int x, int y) {
        if ((x >= mX && x <= (mX + mWidth))
                && (y >= mY && y < mY + mHeight)) {
            mPressing = true;
        } else {
            mPressing = false;
        }
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
