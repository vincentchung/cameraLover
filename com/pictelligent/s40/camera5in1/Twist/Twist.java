
package com.pictelligent.s40.camera5in1.Twist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Display;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.EffectCaptureCanvas.CaptureListener;
import com.pictelligent.s40.camera5in1.EffectPaintCanvas.EffectListener;
import com.pictelligent.s40.camera5in1.ModeExitListener;

/**
 * To handle operations that about the Twist mode. Including Capturing, applying
 * effect, and saving.
 */
public final class Twist implements CaptureListener,
        EffectListener {

    private Display mDisplay;
    private Player mPlayer=null;
    private VideoControl mVideoControl;
    private CaptureThread mCaptureThread;

    private ModeExitListener mModeExitListener;
    private TwistCaptureCanvas mTwistCaptureCanvas;
    private TwistEffectCanvas mTwistEffectCanvas;
    public static final int CAPTURING_WIDTH = 480 / 2;// (int) (480 * 0.7);
    public static final int CAPTURING_HEIGHT = 640 / 2;// (int) (640 * 0.7);

    public Twist(Display display) {
        if (Camera5in1.mDebug)
            System.out.println("Twist");
        mDisplay = display;
    }

    /**
     * Enter the Twist mode, and start the preview screen.
     */
    public void display() {
            startPreview();
    }

    public void startPreview() {

        try {
            if (mTwistCaptureCanvas == null) {
                mTwistCaptureCanvas = new TwistCaptureCanvas();
                mTwistCaptureCanvas.setListener(this);
            }
            mDisplay.setCurrent(mTwistCaptureCanvas);
            mPlayer = Manager.createPlayer("capture://image");
            mPlayer.realize();
            mVideoControl = (VideoControl) mPlayer.getControl("VideoControl");
            mVideoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, mTwistCaptureCanvas);
            mTwistCaptureCanvas.startPreview(mVideoControl);
            mPlayer.start();
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    public void setModeExitListener(ModeExitListener listener) {
        mModeExitListener = listener;
    }

    /**
     * Thread to capture images, apply effects and save to disk.
     */
    class CaptureThread extends Thread {

        public CaptureThread() {
        }

        public void run() {
            capture();
        }

        public void capture() {
            byte[] raw = null;
            try {
//                if (!Camera5in1.mEmulator) {
//                    raw = mVideoControl.getSnapshot("width=" + CAPTURING_WIDTH + "&height="
//                            + CAPTURING_HEIGHT);// VGA
//                } else {
//                    raw = mVideoControl.getSnapshot(null);// for emulator.
//                }

                // Currently we only can take picture by passing 'null'.
               

                if (Camera5in1.mEmulator) {
                    InputStream file = getClass().getResourceAsStream("/test.jpg");
                    DataInputStream in = new DataInputStream(file);
                    // byte[] b=null;//=new byte[in.available()];
                    try {
                        raw = new byte[in.available()];
                        in.readFully(raw);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }else
                {
                	 raw = mVideoControl.getSnapshot(null);
//                   
                   if (raw == null) {
                       mTwistCaptureCanvas.setKeyLock(false);
                       if (mModeExitListener.onStorageCheck())
                           startPreview();
                       System.gc();
                       return;
                   }
                }

                mTwistEffectCanvas = new TwistEffectCanvas(raw, CAPTURING_WIDTH, CAPTURING_HEIGHT);

                /*
                 * Image image = Image.createImage(raw, 0, raw.length); if
                 * (Camera5in1.mDebug) System.out.println("w:" +
                 * image.getWidth() + ",h:" + image.getHeight());
                 * mTwistEffectCanvas = new TwistEffectCanvas(image);
                 */

                raw = null;
                System.gc();
                mTwistEffectCanvas.setListener(Twist.this);
                mDisplay.setCurrent(mTwistEffectCanvas);
                mPlayer.close();
                mPlayer = null;
                mVideoControl = null;
            } catch (SecurityException e) {
           	 mPlayer.close();
             mPlayer = null;
             mVideoControl = null;
        	 startPreview();
        	 System.gc();
        	 return;
        	 
        }catch (Exception e) {
                Camera5in1.display_error(e.getMessage());
            }
        }
    };

    public void onModeExit() {
        if (mPlayer != null) {
            mPlayer.close();
            mPlayer = null;
        }
        if (mModeExitListener != null) {
            mModeExitListener.onModeExit();
        }
    }

    public boolean onCaptureCameraButtonPressed() {
        if (mVideoControl == null) {
            return false;
        }
        mCaptureThread = new CaptureThread();
        mCaptureThread.start();
        return true;
    }

    public void onCaptureBackButtonPressed() {
        onModeExit();
    }

    public void onEffectBackButtonPressed() {
        if (mModeExitListener.onStorageCheck())
            startPreview();
    }

    public void onEffectOkButtonPressed() {
        if (Camera5in1.mDebug)
            System.out.println("onEffectOkButtonPressed");
        if (mModeExitListener.onStorageCheck())
            startPreview();
        // mDisplay.setCurrent(pb);
        /*
         * Gauge indicator = new Gauge(null, false, 50, 1);
         * indicator.setValue(Gauge.CONTINUOUS_RUNNING); Form waiting = new
         * Form("fuck"); waiting.append(indicator);
         * mDisplay.setCurrent(waiting);
         */
    }

    public void resume_camera() {
    	if (Camera5in1.mDebug)
    	DebugUtil.Log("state:"+mPlayer.getState());
    	
    	if(mPlayer==null)
    		return;
    	
    	try {
    	switch(mPlayer.getState())
    	{
    	case Player.STARTED:
    		//doing nothing
    		break;
    	case Player.PREFETCHED:
    		mPlayer.start();
    		break;
    	case Player.REALIZED:
			mPlayer.prefetch();
			mPlayer.start();
		    break;
    	case Player.UNREALIZED:
    		mPlayer.realize();
    		mPlayer.prefetch();
			mPlayer.start();
    		break;
    	}
    	} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void onEffectWriteFaild() {
        mModeExitListener.onStorageCheck();
        startPreview();
    }
}
