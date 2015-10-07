
package com.pictelligent.s40.camera5in1.Futuro;

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
 * To handle operations that about the Futuro mode. Including Capturing,
 * applying effect, and saving.
 */
public final class Futuro implements CaptureListener,
        EffectListener {

    private Display mDisplay;
    private Player mPlayer=null;
    private VideoControl mVideoControl;
    private CaptureThread mCaptureThread;

    private ModeExitListener mModeExitListener;
    private FuturoCaptureCanvas mFuturoCaptureCanvas;
    private FuturoEffectCanvas mFuturoEffectCanvas;

    public static final int CAPTURING_WIDTH = 480 / 2;// (int) (480 * 0.7);
    public static final int CAPTURING_HEIGHT = 640 / 2;// (int) (640 * 0.7);

    public Futuro(Display display) {
        if (Camera5in1.mDebug)
            System.out.println("Futuro");
        mDisplay = display;
    }

    /**
     * Enter the Futuro mode, and start the preview screen.
     */
    public void display() {
            startPreview();
    }

    public void startPreview() {
        try {
            if (mFuturoCaptureCanvas == null) {
                mFuturoCaptureCanvas = new FuturoCaptureCanvas();
                mFuturoCaptureCanvas.setListener(this);
            }
            mDisplay.setCurrent(mFuturoCaptureCanvas);
            mPlayer = Manager.createPlayer("capture://image");
            mPlayer.realize();
            mVideoControl = (VideoControl) mPlayer.getControl("VideoControl");
            mVideoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, mFuturoCaptureCanvas);
            mFuturoCaptureCanvas.startPreview(mVideoControl);
            mPlayer.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (MediaException me) {
            me.printStackTrace();
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
            byte[] raw;
            try {
//                if (!Camera5in1.mEmulator) {
//                    raw = mVideoControl.getSnapshot("width=" + CAPTURING_WIDTH + "&height="
//                            + CAPTURING_HEIGHT);// VGA
//                } else {
//                    raw = mVideoControl.getSnapshot(null);// for emulator.
//                }
                
                // Currently we only can take picture by passing 'null'.
                raw = mVideoControl.getSnapshot(null);
                
                if (raw == null) {
                    mFuturoCaptureCanvas.setKeyLock(false);
                    if (mModeExitListener.onStorageCheck())
                        startPreview();
                    System.gc();
                    return;
                }
                
                if (Camera5in1.mEmulator) {
                    InputStream file = getClass().getResourceAsStream("/test_01.jpg");
                    DataInputStream in = new DataInputStream(file);
                    try {
                        raw = new byte[in.available()];
                        in.readFully(raw);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    mFuturoEffectCanvas = new FuturoEffectCanvas(raw, CAPTURING_WIDTH,
                            CAPTURING_HEIGHT);
                } else {
                    mFuturoEffectCanvas = new FuturoEffectCanvas(raw, CAPTURING_WIDTH,
                            CAPTURING_HEIGHT);
                }
                
                mFuturoEffectCanvas.setListener(Futuro.this);
                mDisplay.setCurrent(mFuturoEffectCanvas);
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
        if (mVideoControl == null)
        {
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
        if (mModeExitListener.onStorageCheck())
            startPreview();
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
        // TODO Auto-generated method stub
        mModeExitListener.onStorageCheck();
        startPreview();
    }
}
