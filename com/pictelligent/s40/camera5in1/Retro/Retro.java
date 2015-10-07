
package com.pictelligent.s40.camera5in1.Retro;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Display;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.EffectCaptureCanvas.CaptureListener;
import com.pictelligent.s40.camera5in1.EffectPaintCanvas.EffectListener;
import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.ModeExitListener;
import com.pictelligent.s40.camera5in1.SystemUtil;

/**
 * To handle operations that about the Retro mode. Including Capturing, applying
 * effect, and saving.
 */
public final class Retro implements CaptureListener,
        EffectListener {

    private Display mDisplay;
    private Player mPlayer=null;
    private VideoControl mVideoControl;
    private CaptureThread mCaptureThread;


    private ModeExitListener mModeExitListener;
    private RetroCaptureCanvas mRetroCaptureCanvas;
    private RetroEffectCanvas mRetroEffectCanvas;
    public static final int CAPTURING_WIDTH = 480 / 2;// (int) (480 * 0.7);//96
    public static final int CAPTURING_HEIGHT = 640 / 2;// (int) (640 *
                                                       // 0.7);//128
    Timer mTimer = null;
    public Retro(Display display) {
        if (Camera5in1.mDebug)
            System.out.println("Retro");
        mDisplay = display;
    }

    /**
     * Enter the Retro mode, and start the preview screen.
     */
    public void display() {
        //if (mModeExitListener.onStorageCheck())
    	

        startPreview();
    }

    public void startPreview() {
        try {
            if (mRetroCaptureCanvas == null) {
                mRetroCaptureCanvas = new RetroCaptureCanvas();
                mRetroCaptureCanvas.setListener(this);
            }
            mDisplay.setCurrent(mRetroCaptureCanvas);
            
            mPlayer = Manager.createPlayer("capture://image");
            mPlayer.realize();
            mVideoControl = (VideoControl) mPlayer.getControl("VideoControl");
            mVideoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, mRetroCaptureCanvas);
            mRetroCaptureCanvas.startPreview(mVideoControl);

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
            	if (!mModeExitListener.onStorageCheck())
            		return;
            	
                if (!Camera5in1.mEmulator) {
                    // raw = mVideoControl.getSnapshot("width="
                    // + Collage.CAPTURING_WIDTH + "&height="
                    // + Collage.CAPTURING_HEIGHT);
                	
                    raw = mVideoControl.getSnapshot(null);
                } else {
                	
                    InputStream file = getClass().getResourceAsStream("/test.jpg");
                    DataInputStream in = new DataInputStream(file);
                    try {
                        raw = new byte[in.available()];
                        in.readFully(raw);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
                
                //SystemUtil.save_file("test.jpg", raw);

                if (raw == null) {
                    mRetroCaptureCanvas.setKeyLock(false);
                    if (mModeExitListener.onStorageCheck())
                        startPreview();
                    System.gc();
                    return;
                }

                mRetroEffectCanvas = new RetroEffectCanvas(raw, CAPTURING_WIDTH,
                        CAPTURING_HEIGHT);
                mRetroEffectCanvas.setListener(Retro.this);
                mDisplay.setCurrent(mRetroEffectCanvas);
                mPlayer.close();
                mPlayer = null;
                mVideoControl = null;
            }
             catch (SecurityException e) {
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

    private int mTimercounter=0;
    public void onModeExit() {
        if (mPlayer != null) {
            mPlayer.close();
            mPlayer = null;
        }
        
        if(mVideoControl!=null)
        	mVideoControl=null;
        
        if (mModeExitListener != null) {
            mModeExitListener.onModeExit();
        }
    }

    public boolean onCaptureCameraButtonPressed() {
        if (mVideoControl == null)
            return false;

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
        mModeExitListener.onStorageCheck();
        startPreview();
    }
}
