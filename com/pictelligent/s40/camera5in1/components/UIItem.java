package com.pictelligent.s40.camera5in1.components;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class UIItem {
    protected Image pressed=null;
    protected Image unpressed=null;
    protected int mDisplayWidth;
    protected int mDisplayHeight;
    protected int mDisplayX = 0;
    protected int mDisplayY = 0;
    protected boolean isPressed = false;

    public UIItem() {
        
    }

    public void setImages(String pressed_image_url, String unpressed_image_url) {
        try {
            pressed = Image.createImage(pressed_image_url);
            unpressed = Image.createImage(unpressed_image_url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDisplayWidth = pressed.getWidth();
        mDisplayHeight = pressed.getHeight();
    }

    public void render(Graphics g) {
        g.translate(-g.getTranslateX(), -g.getTranslateY());
        if (isPressed) {
        	if(pressed!=null)
            g.drawImage(pressed, mDisplayX, mDisplayY, Graphics.TOP | Graphics.LEFT);
        }
        else {
        	if(unpressed!=null)
            g.drawImage(unpressed, mDisplayX, mDisplayY, Graphics.TOP | Graphics.LEFT);
        }
    }

    public void setPosition(int x, int y) {
        this.mDisplayX = x;
        this.mDisplayY = y;
    }

    public void onPressed(int x, int y) {
        if (x >= this.mDisplayX && x <= (this.mDisplayX + mDisplayWidth)) {
            if (y >= this.mDisplayY && y <= this.mDisplayY + mDisplayHeight) {
                isPressed = true;
            }
        }
    }

    public void onUnpressed(int x, int y) {
        if (isPressed) {
            isPressed = false;
            if (x >= this.mDisplayX && x <= (this.mDisplayX + mDisplayWidth)) {
                if (y >= this.mDisplayY && y <= this.mDisplayY + mDisplayHeight) {
                	onItemUnPress();
                }
            }
        }
    }
    
    public abstract void onItemPress();
    public abstract void onItemUnPress();
    public abstract void onItemTouchPress(int x,int y);
    public abstract void onItemTouchRelease(int x,int y);
    public abstract void onItemTouchDragged(int x,int y);
    public abstract void onItemTouchPinich(int getPinchDistanceChange);

    public int getWidth() {
        return mDisplayWidth;
    }

    public int getHeight() {
        return mDisplayHeight;
    }
}

