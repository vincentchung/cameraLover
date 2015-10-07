
package com.pictelligent.s40.camera5in1.components;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Button {
    protected Image pressed;
    protected Image unpressed;
    protected int width;
    protected int height;
    protected int x = 0;
    protected int y = 0;
    protected boolean isPressed = false;
    private ButtonListener mListener;
    private boolean mVisable=true;

    public Button(String pressed_image_url, String unpressed_image_url) {
        setImages(pressed_image_url, unpressed_image_url);
    }

    private void setImages(String pressed_image_url, String unpressed_image_url) {
        try {
            pressed = Image.createImage(pressed_image_url);
            unpressed = Image.createImage(unpressed_image_url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        width = pressed.getWidth();
        height = pressed.getHeight();
    }

    public void setListener(ButtonListener listener) {
        mListener = listener;
    }

    public void render(Graphics g) {
    	if(!mVisable)
    		return;
    	
        g.translate(-g.getTranslateX(), -g.getTranslateY());
        if (isPressed) {
            g.drawImage(pressed, x, y, Graphics.TOP | Graphics.LEFT);
        }
        else {
            g.drawImage(unpressed, x, y, Graphics.TOP | Graphics.LEFT);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void onPressed(int x, int y) {
    	if(!mVisable)
    		return;
    	
        if (x >= this.x && x <= (this.x + width)) {
            if (y >= this.y && y <= this.y + height) {
                isPressed = true;
            }
        }
    }

    public void onUnpressed(int x, int y) {
    	if(!mVisable)
    		return;
    	
        if (isPressed) {
            isPressed = false;
            if (x >= this.x && x <= (this.x + width)) {
                if (y >= this.y && y <= this.y + height) {
                    if (mListener != null) {
                        mListener.buttonClicked(this);
                    }
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int getPositionX() {
        return x;
    }

    public int getPositionY() {
        return y;
    }
    
    public void setVisable(boolean v)
    {
    	mVisable=v;
    }
}
