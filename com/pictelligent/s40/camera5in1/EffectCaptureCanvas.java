
package com.pictelligent.s40.camera5in1;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.control.VideoControl;

import com.pictelligent.s40.camera5in1.components.Button;
import com.pictelligent.s40.camera5in1.components.ButtonListener;

/**
 * View finder screen of Capture.
 */
public abstract class EffectCaptureCanvas extends Canvas implements CommandListener, ButtonListener {

    public interface CaptureListener {
        boolean onCaptureCameraButtonPressed();

        void onCaptureBackButtonPressed();

        void resume_camera();
    }


    private CaptureListener mListener;
    private Image mIconVfBorders;
    private Image mIconCanvasBg;
    protected int mBackgroundColor = 0;

    private boolean mBusy = false;
    private boolean mCpaturing = false;
    private Command mBackCommand;

    private Button mShutterBtn;

    private boolean mbIncomingCallflag = false;
    private boolean minit = false;


    public EffectCaptureCanvas() {
        //setFullScreenMode(true);
        init();
        
        try {
            mShutterBtn = new Button("/ic_new_capture_pressed.png", "/ic_new_capture.png");
            mShutterBtn.setPosition((240-mShutterBtn.getWidth())/2, 260);
            mShutterBtn.setListener(this);
            mBackCommand = new Command("", Command.BACK, 0);
            addCommand(mBackCommand);
            setCommandListener(this);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    public void paint(Graphics g) {
        int x = 0;
        int y = 0;

        g.setColor(mBackgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        mShutterBtn.render(g);
    }

    public void startPreview(VideoControl control) {
        if (control != null) {
            try {
            	int x = 25;
            	int y = 5;
            	int vfWidth = 190;
            	int vfHeight = 253;
            	
                control.setDisplayLocation(x, y);
                control.setDisplaySize(vfWidth, vfHeight);
            } catch (Exception e) {
                Camera5in1.display_error(e.getMessage());
            }
            control.setVisible(true);
            setKeyLock(false);
            repaint();
        }
    }

    public void setListener(CaptureListener listener) {
        mListener = listener;
    }

    protected void pointerPressed(int x, int y) {
    	if(mBusy)
    		return;
        mShutterBtn.onPressed(x, y);
        repaint();
    }

    protected void pointerReleased(int x, int y) {
    	if(mBusy)
    		return;
        mShutterBtn.onUnpressed(x, y);
        repaint();
    }

    public void setKeyLock(boolean b) {
        mBusy = b;
        mCpaturing = b;
    }

    protected void hideNotify() {
    	/*
        if (Camera5in1.mDebug)
            DebugUtil.Log("hideNotify" + mbIncomingCallflag);
        if (!minit) {
            minit = true;
            return;
        }

        if (!mCpaturing) {
            mbIncomingCallflag = true;
        }
        */
    	System.out.println("hideNotify");
    }

    protected void showNotify() {
        /*
    	if (Camera5in1.mDebug)
            DebugUtil.Log("showNotify" + mbIncomingCallflag);
        // System.out.println("showNotify");
        if (mbIncomingCallflag) {
            mbIncomingCallflag = false;
            minit = false;
            mListener.resume_camera();
        }
        */
    	System.out.println("showNotify");
        mListener.resume_camera();
    }

    public void buttonClicked(Button caller) {
        // TODO: add busy checking here when taking picture is ready.
        if (caller == mShutterBtn) {
            if (!mBusy) {
                // capture button clicked:
                if (mListener != null) {
                    mBusy = true;
                    mCpaturing = true;
                    
                    //play sound
                    SystemUtil.playSound(0, getClass());
                    
                    if (!mListener.onCaptureCameraButtonPressed())
                        mBusy = false;
                }
            }
        }
    }

    public void commandAction(Command c, Displayable d) {
    	if(mBusy)
    		return;
        //System.out.println("command:" + c.getLabel());
        if (c == mBackCommand) {
            //System.out.println("back");
            if (mListener != null) {
                mListener.onCaptureBackButtonPressed();
            }
        }
    }
    
    public abstract void init();
}
