
package com.pictelligent.s40.camera5in1;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import com.pictelligent.s40.camera5in1.JPEGDecoder.ImageRGB;
import com.pictelligent.s40.camera5in1.JPEGDecoder.JpegDecoder;
import com.pictelligent.s40.camera5in1.JPEGEncoder.JpegFile;
import com.pictelligent.s40.camera5in1.components.Button;
import com.pictelligent.s40.camera5in1.components.ButtonListener;
import com.pictelligent.s40.camera5in1.components.ImageItem;
import com.pictelligent.s40.camera5in1.effects.EffectAlgorithms;
import com.pictelligent.s40.camera5in1.effects.PicImageProcess;
import com.pictelligent.s40.camera5in1.effects.Scaling;
import com.pictelligent.s40.camera5in1.language.Language;

public abstract class EffectPaintCanvas extends Canvas implements CommandListener, ButtonListener,GestureListener {

    public interface EffectListener {
        void onEffectBackButtonPressed();

        void onEffectOkButtonPressed();

        void onEffectWriteFaild();
    }

    protected int mEffectsNumber;
    protected int mPreviewWidth;
    protected int mPreviewHeight;
    protected int mCapturedWidth;
    protected int mCapturedHeight;
    protected int mBackgroundColor;

    private ProgressBar mProgressBar;
    protected String mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
    private EffectListener mEffectListener;

    protected boolean b16bits = true;
    protected int[] fullImage = null; // 32bits, original size images source,
                                      // allocate at parent class
    protected int[] fullImageOutput = null; // 32bits, original size images
                                            // output, allocate at child class
    protected int[] orgImage = null; // 32bits, small size for being source to
                                     // applying effect, allocate at parent
                                     // class
    protected int[] previewImage = null; // 32bits, small size for displaying
                                         // preview result, allocate at parent
                                         // class
                                         // notice: only support 32bits RGB
                                         // displaying

    protected short[] fullImage16bits = null; // 16bits, original size images
                                              // source, allocate at parent
                                              // class
    protected short[] orgImage16bits = null; // 16bits, small size for being
                                             // source to applying effect,
                                             // allocate at parent class
    protected short[] fullImage16bitsOutput = null; // 32bits, original size
                                                    // images output, allocate
                                                    // at child class

    protected int mEffectIndex = 0;
    private boolean mBusy = false;
    private boolean mSaving = false;
    private Timer mTimer;
    protected String filename_title = "pic_";

    private Button mLeftArrowBtn;
    private Button mSaveBtn;
    private Button mRightArrowBtn;
    private Command mBackCommand;
    private ImageItem mDisplayImage = null;
    private PicImageProcess mIpp = null;
    
    
    public void initializeGesture() {

        /**
         * Set the listener to register events for ImageCanvas (this,) and
         * register the GestureListener for the UI element (,this)
         */
        GestureRegistrationManager.setListener(this, this);

        /**
         * Create an interactive zone and set it to receive taps
         */
        GestureInteractiveZone myGestureZone = new GestureInteractiveZone(GestureInteractiveZone.GESTURE_PINCH);

        /**
         * Set the interactive zone to also receive other events
         */
        //myGestureZone.setGestures(GestureInteractiveZone.GESTURE_ALL);
        /**
         * Set the location (relative to the container) and size of the
         * interactive zone:
         */
        myGestureZone.setRectangle(0, 0, getWidth(), getHeight());

        /**
         * Register the interactive zone for GestureCanvas (this)
         */
        GestureRegistrationManager.register(this, myGestureZone);
    }


    public EffectPaintCanvas(byte[] jpegdata, int width, int height) {
        setFullScreenMode(false);
        init();
        
        //initializeGesture();

        mCapturedHeight = height;
        mCapturedWidth = width;

        // jpeg decoding
        // previewImage = new int[PREVIEW_WIDTH * PREVIEW_HEIGHT];//for
        // displaying
        if (b16bits) {
            SystemUtil.Log_current_time_init();
            SystemUtil.Log_current_time_s("Jpegdecode s 1");

            mIpp=new PicImageProcess(jpegdata);
            SystemUtil.Log_current_time_e("Jpegdecode");

            System.gc();
            
            mDisplayImage=new ImageItem(mIpp,42,45,mPreviewWidth, mPreviewHeight);
            orgImage16bits=mIpp.getResizeSourceBuffer(mPreviewWidth, mPreviewHeight);
            previewImage=mDisplayImage.getDisplayImageBuffer();
            fullImage16bits=mIpp.getSourceBuffer();
        } 
        
        int button_x=0;
        int button_y=0;

        try {
            mProgressBar = new ProgressBar(getWidth(), getHeight());
            mSaveBtn = new Button("/ic_new_save_pressed.png", "/ic_new_save.png");
            mSaveBtn.setListener(this);
            
            mLeftArrowBtn = new Button("/ic_new_left_pressed.png", "/ic_new_left.png");
            mLeftArrowBtn.setListener(this);
            
            mRightArrowBtn = new Button("/ic_new_right_pressed.png", "/ic_new_right.png");
            mRightArrowBtn.setListener(this);
            
            mBackCommand = new Command("", Command.BACK, 0);
            
            //set up the 3 button's location
            button_x = (getWidth()-mSaveBtn.getWidth())/2;
            button_y = 260;
            mSaveBtn.setPosition(button_x, button_y);

            button_x = 5;
            button_y = getHeight()/2 - mLeftArrowBtn.getHeight()/2;
            mLeftArrowBtn.setPosition(button_x, button_y);
            
            button_x = getWidth() - mRightArrowBtn.getWidth() - 5;		
            mRightArrowBtn.setPosition(button_x, button_y);
            
            addCommand(mBackCommand);
            setCommandListener(this);
            mTimer = new Timer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // mBusy=false;
        if (Camera5in1.mDebug) {
            System.out.println("EffectpaintCanvas");
            System.out.println("PREVIEW_WIDTH:" + mPreviewWidth);
        }
    }

    protected void pointerPressed(int x, int y) {
    	
    	if(mBusy)
    		return;
    	
    	mDisplayImage.onItemTouchPress(x, y);
        mSaveBtn.onPressed(x, y);
        mLeftArrowBtn.onPressed(x, y);
        mRightArrowBtn.onPressed(x, y);
        repaint();
    }

    protected void pointerReleased(int x, int y) {
    	if(mBusy)
    		return;
    	
    	mDisplayImage.onItemTouchRelease(x, y);
        mSaveBtn.onUnpressed(x, y);
        mLeftArrowBtn.onUnpressed(x, y);
        mRightArrowBtn.onUnpressed(x, y);
        repaint();
    }
    
    


    private void saveFullSizeImage() {
        new Thread() {
            public void run() {
                mProgressBar.reset();
                
                mSaving = true;
                mTimer.schedule(mRepaintTask, 0, 1000);
                switchEffectFull();
                Calendar rightNow = Calendar.getInstance();
                String filename = filename_title + rightNow.get(Calendar.YEAR)
                        + rightNow.get(Calendar.MONTH) + rightNow.get(Calendar.DAY_OF_MONTH)
                        + rightNow.get(Calendar.HOUR_OF_DAY) + rightNow.get(Calendar.HOUR_OF_DAY)
                        + rightNow.get(Calendar.MINUTE) + rightNow.get(Calendar.SECOND) + ".jpg";
                boolean result = false;
                if (b16bits) {
                    result = JpegFile.saveImage16(filename, fullImage16bitsOutput,
                            mCapturedWidth, mCapturedHeight);
                } else {
                    result = JpegFile.saveImage(filename, fullImage,
                            mCapturedWidth, mCapturedHeight);
                }

                recycle();

                mSaving = false;
                mTimer.cancel();
                if (result) {
                    if (mEffectListener != null) {
                        mEffectListener.onEffectOkButtonPressed();
                    }
                } else {
                    if (mEffectListener != null) {
                        mEffectListener.onEffectWriteFaild();
                    }
                }
            };
        }.start();
    }

    private void recycle() {
        fullImage = null;
        orgImage = null;

        fullImage16bitsOutput = null;
        fullImage16bits = null;
        orgImage16bits = null;
    }

    public void setListener(EffectListener listener) {
        mEffectListener = listener;
    }

    protected void paint(Graphics g) {
        if (Camera5in1.mDebug) {
            System.out.println("EffectpaintCanvas:paint");
        }
        // Draw background.
        g.setColor(mBackgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw effect name in the title.
        g.setColor(0xEEEEEE);
        g.setFont(Font.getFont(Camera5in1.mFontFace ,Camera5in1.mFontType, Camera5in1.mFontSize));
        int x = (getWidth() - g.getFont().stringWidth(mEffectName)) / 2;
        int y = 10;
        g.drawString(mEffectName, x, y,0);

        x = (getWidth() - mPreviewWidth) / 2;
        y = 40;
        mDisplayImage.render(g);

        mLeftArrowBtn.render(g);
        mRightArrowBtn.render(g);
        mSaveBtn.render(g);

        // Saving progress bar.
        if (mSaving) {
            mProgressBar.update(g);
        }
    }

    public void commandAction(Command c, Displayable d) {
    	if (mBusy) {
            return;
        }
    	
        if (c == mBackCommand) {
            recycle();
            if (mEffectListener != null) {
                mEffectListener.onEffectBackButtonPressed();
            }
        }
    }

    public void buttonClicked(Button caller) {
        if (mBusy) {
            return;
        }
        mBusy = true;
        if (caller == mSaveBtn) {
        	
        	
            saveFullSizeImage();
        } else if (caller == mLeftArrowBtn) {
            if (mEffectIndex != 0) {
                mEffectIndex--;
            } else {
                mEffectIndex = mEffectsNumber;
            }
            switchEffect();
            repaint();
            mBusy = false;
        } else if (caller == mRightArrowBtn) {
            if (mEffectIndex != mEffectsNumber) {
                mEffectIndex++;
            } else {
                mEffectIndex = 0;
            }
            switchEffect();
            repaint();
            mBusy = false;
        }
        
    }


    private TimerTask mRepaintTask = new TimerTask() {

        public void run() {
            repaint();
        }
    };
    
    
//touch UI support
//zoom
    public void gestureAction(Object arg0, GestureInteractiveZone arg1,
			GestureEvent gestureEvent) {
    	switch (gestureEvent.getType()) {

        /**
         * This gesture event is supported from Java Runtime 2.0.0 for
         * Series 40 onwards. Receives pinch events and check the pinch
         * distance change to scale the current image.
         */
        case GestureInteractiveZone.GESTURE_PINCH:
        	mDisplayImage.onItemTouchPinich(gestureEvent.getPinchDistanceChange());
            repaint();
            break;
    }
	}
//pan    
    protected void pointerDragged(int x, int y)
    {
    	if(mBusy)
    		return;
    	mDisplayImage.onItemTouchDragged(x, y);
    	repaint();
    }
    
    public abstract void switchEffect();

    public abstract void switchEffectFull();

    public abstract void init();
}