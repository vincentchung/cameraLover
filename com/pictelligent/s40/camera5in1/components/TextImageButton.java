package com.pictelligent.s40.camera5in1.components;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.help.ImageLoader;

public class TextImageButton extends Button {
	public static final int GAP = -5;
    
    protected Image pressed;
    protected Image unpressed;
    protected int width;
    protected int height;
    protected int x = 0;
    protected int y = 0;
    protected boolean isPressed = false;
    private String mButtonText;
    private ButtonListener mListener;
    private int mTextPosy  =10;
    private static int mTextFontType=Font.STYLE_BOLD;
    private static int mTextFontSize=Font.SIZE_SMALL;
    private static int TextmFontFace=Font.FACE_SYSTEM;
    
	public TextImageButton(String pressed_image_url, String unpressed_image_url, String text) {
		super(pressed_image_url, unpressed_image_url);
        setImages(pressed_image_url, unpressed_image_url, "");
		 mButtonText = text;
	}
	
	
    public void setImages(String pressed_image_url, String unpressed_image_url, String text) {
        ImageLoader loader = ImageLoader.getInstance();
        pressed = loader.loadImage(pressed_image_url);
        unpressed = loader.loadImage(unpressed_image_url);
		mButtonText = text;

    }
    public void setFontType(int face,int type,int size)
    {
    	TextmFontFace=face;
    	mTextFontType=type;
    	mTextFontSize=size;
    }

    public void setListener(ButtonListener listener) {
        mListener = listener;
    }
    public void render(Graphics g) {
//        int posy = y + height - mTextPosy;
        g.setFont(Font.getFont(TextmFontFace, mTextFontType, mTextFontSize));

        int imageX = x + (width - pressed.getWidth()) / 2;
        int textX = x + (width - pressed.getWidth()) / 2;
        if (textX < 0) {
            textX = 0;
        }
        
        g.drawImage(unpressed, imageX, y, Graphics.TOP | Graphics.LEFT);
        //draw shadow effect
        g.setColor(0, 0, 0);
        g.drawString(mButtonText, textX + 1, y + pressed.getHeight() + GAP + 1, 0);
        if (isPressed) {
            g.setColor(255, 127, 0);
        }
        else {
            g.setColor(255, 255, 255);
        }
        g.drawString(mButtonText, textX, y + pressed.getHeight() + GAP, 0);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(int w, int h) {
        width = w;
        height = h;
    }

    /**
     * Calls the onSelected method if x and y are inside the tool area.
     * 
     * @param x
     * @param y
     */
    public void pressed(int x, int y) {
        if (x >= this.x && x <= (this.x + width)) {
            if (y >= this.y && y <= this.y + height) {
                isPressed = true;
            }
            System.out.println("pressed x =" + x + " y = " +y );
        }
    }

    public void unpressed(int x, int y) {
        isPressed = false;
        if (x >= this.x && x <= (this.x + width)) {
            if (y >= this.y && y <= this.y + height) {
                if (mListener != null) {
                    mListener.buttonClicked(this);
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
    //for adajust space between layout icon
    public void  setStringPosy( int pos) {
    	mTextPosy = pos;
    }
    //for adajust space between layout icon
    public int getStringPosy() {
    	return mTextPosy;
    }
}
