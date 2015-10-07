
package com.pictelligent.s40.camera5in1.Fusion;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Timer;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.DeviceKeycode;
import com.pictelligent.s40.camera5in1.ModeExitListener;
import com.pictelligent.s40.camera5in1.SystemUtil;
import com.pictelligent.s40.camera5in1.Collage.Collage;
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

/**
 * Fusion mode 1 view finder screen.
 */
public class FusionCaptureCanvas extends Canvas implements ButtonListener, CommandListener {

    public interface FusionCaptureListener {
        void onCaptureBackButtonPressed();
    }

    private static final int PADDING = 5;

    private FusionCaptureListener mListener;
    private ModeExitListener mModeExitListener;
    private VideoControl mVideoControl;
    private Player mPlayer;
    private Image[] mSnapshots = new Image[2];
    private short[][] mCaptureData = null;
    private int mCapStep = 0;
    /*
     * 2: after took 2 shot
     * 3: editor mode
     * */
    private boolean mBusy = false;
    private boolean mPreviewing = false;
    FusionMode mMode;
    private Image mScreenBg;
    private Image mIconFusion;

    private Button mReadyCapture;
    private Button mCapture;
    private Button mSave;

    private Command mCommandBack = new Command("Back", Command.BACK, 1);
    private boolean mbIncomingCallflag = false;
    private boolean mCapturing = false;
    
    //
    private ImageItem mEditModeImage=null;
    private String tempfilename="collage_temp_";
    private PicImageProcess mIpp=null;
    private byte[][] mCapturingRaw = null;
    private int mEditModeDisplayX=0;
    private int mEditModeDisplayY=0;
    private int mEditModeDisplayW=0;
    private int mEditModeDisplayH=0;
    private int mCurrentEditFrame=-1;

    public FusionCaptureCanvas() {
        setFullScreenMode(false);
        try {
            mIconFusion = Image.createImage("/ic_fusion_titlebar.png");
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
        mCaptureData = new short[2][];
        
        this.addCommand(mCommandBack);
        this.setCommandListener(this);

        mCapture = new Button("/ic_new_capture_pressed.png", "/ic_new_capture.png");
        mCapture.setListener(this);
        
        mSave = new Button("/ic_new_save_pressed.png", "/ic_new_save.png");
        int buttonLocationX=(getWidth()-mSave.getWidth())/2;
        mSave.setListener(this);
        mSave.setPosition(buttonLocationX, 260);
        
        mCapture.setPosition(buttonLocationX, 260);
        
        mReadyCapture = new Button("/ic_new_ok_pressed.png", "/ic_new_ok.png");
        mReadyCapture.setListener(this);
        buttonLocationX=(getWidth()-mReadyCapture.getWidth())/2;
        mReadyCapture.setPosition(buttonLocationX, 260);

        //editor mode
        mCapturingRaw =new byte[2][];
        mEditModeDisplayX=(getWidth()>>3);
        mEditModeDisplayY=mIconFusion.getHeight()+10;//(getHeight()>>3);
        mEditModeDisplayW=(getWidth()>>1);
        mEditModeDisplayH=(getHeight()>>1);
    }

    public void setMode(FusionMode mode) {
        mMode = mode;
        resetSnapShots();
        mPreviewing = false;
        mbIncomingCallflag = false;
        mCapturing = false;
        mBusy = false;
    }

    public void setListener(FusionCaptureListener listener) {
        mListener = listener;
    }

    public void setExistListener(ModeExitListener listener) {
        mModeExitListener = listener;
    }

    public void startPreview() {
        try {
            mPlayer = Manager.createPlayer("capture://image");
            mPlayer.realize();
            mVideoControl = (VideoControl) mPlayer.getControl("VideoControl");
            mVideoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
            try {
            	
            	int x=(getWidth()-mMode.preview_frame_width)/2;
                mVideoControl.setDisplayLocation(x, PADDING+11);
                //System.out.println(mMode.preview_frame_width +","+
                //        mMode.preview_frame_height);
                mVideoControl.setDisplaySize(mMode.preview_frame_width ,
                                mMode.preview_frame_height );
                
            	
            } catch (Exception e) {
                Camera5in1.display_error(e.getMessage());
            }
            mVideoControl.setVisible(true);
            mPlayer.start();
            mPreviewing = true;
            repaint();
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void restartPreview() {
        startPreview();
    }

    public void paint(Graphics g) {
        //System.out.println("_paint:w=" + getWidth() + ",h=" + getHeight());

        g.setColor(Camera5in1.FUSION_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (!mPreviewing) {
            Font font = Font.getFont(Camera5in1.mFontFace ,Camera5in1.mFontType, Camera5in1.mFontSize);
            g.setColor(0xEEEEEE);
            g.setFont(font);

            // Draw title text.           
            int x = (getWidth()-font.stringWidth(Language.get(Language.TEXT_FUSION)))/2;
            int y = 10;            
            g.setColor(255, 255, 255);
            g.drawString(Language.get(Language.TEXT_FUSION), x, y, 0);

            x = (getWidth() - mMode.mTipImages[0].getWidth()) / 2;
            y = 40;
            if (mCapStep < 2) {
                if (mCapStep == 1) {
                    if (mSnapshots[0] != null) {
                        g.drawImage(mSnapshots[0], mMode.mTipCells[0].x + x,
                                mMode.mTipCells[0].y + y - 7, Graphics.LEFT | Graphics.TOP);
                    }
                }
                // draw tip background
                Image tip = mMode.mTipImages[mCapStep];
                g.drawImage(tip, x, y - 7, Graphics.LEFT | Graphics.TOP);

                mReadyCapture.render(g);
            } else if (mCapStep == 2) {
                // draw tip images.
                for (int i = 0; i < mCapStep; i++) {
                    if (mSnapshots[i] != null) {
                        g.drawImage(mSnapshots[i], mMode.mTipCells[i].x
                                + x, mMode.mTipCells[i].y + y, Graphics.LEFT | Graphics.TOP);
                    }
                }

                mSave.render(g);
            }else if (mCapStep == 3) {
            
            	//System.out.println("render 3");
            	if(mEditModeImage!=null)
                	mEditModeImage.render(g);
            	
            	mSave.render(g);
            }
        } else {
            if (mCapStep >= 2) {
                return;
            }

            int x=(getWidth()-mMode.preview_frame_width)/2-PADDING;
            int y=11;
            g.setColor(255, 255, 0);
            g.fillRect(x, y, mMode.preview_frame_width+PADDING*2, mMode.preview_frame_height+PADDING*2);

            g.setColor(0, 255, 0);
            g.fillRect(mMode.hintcell[mCapStep].x+x, mMode.hintcell[mCapStep].y+y,
                    mMode.hintcell[mCapStep].width+PADDING*2,
                    mMode.hintcell[mCapStep].height+PADDING*2);

            mCapture.render(g);
        }
    }

    public void postJpegImage(byte[] snapshotRaw) {
        // TODO: remove test coe.
        /*
         * Image image = Image.createImage(snapshotRaw, 0, snapshotRaw.length);
         * int[] rgbImageData = new int[image.getHeight() * image.getWidth()];
         * image.getRGB(rgbImageData, 0, image.getWidth(), 0, 0,
         * image.getWidth(), image.getHeight()); int captureW =
         * mMode.cell[mCapStep].width; int captureH =
         * mMode.cell[mCapStep].height; mCaptureData[mCapStep] =
         * Image.createImage(captureW, captureH); Graphics g1 =
         * mCaptureData[mCapStep].getGraphics(); if (mCapStep == 0) {
         * g1.setColor(255, 255, 0); } else { g1.setColor(0, 255, 255); }
         * g1.fillRect(0, 0, captureW, captureH); int snapW =
         * mMode.mTipCells[mCapStep].width; int snapH =
         * mMode.mTipCells[mCapStep].height; mSnapshots[mCapStep] =
         * Image.createImage(snapW, snapH); Graphics g2 =
         * mSnapshots[mCapStep].getGraphics(); if (mCapStep == 0) {
         * g2.setColor(255, 255, 0); } else { g2.setColor(0, 255, 255); }
         * g2.fillRect(0, 0, snapW, snapH);
         */
        boolean m16bits = true;

        if (!m16bits) {
            // Jpeg decode to RGB8888
            Image image = Image.createImage(snapshotRaw, 0, snapshotRaw.length);
            int[] rgbImageData = new int[image.getHeight() * image.getWidth()];
            image.getRGB(rgbImageData, 0, image.getWidth(), 0, 0,
                    image.getWidth(),
                    image.getHeight());

            int captureW = mMode.cell[mCapStep].width;
            int captureH = mMode.cell[mCapStep].height;
            int[] rgbImageData2 = new int[captureW * captureH];
            Scaling.cropImage(rgbImageData, rgbImageData2, mMode.image_width,
                    mMode.image_height, mMode.cell[mCapStep].x,
                    mMode.cell[mCapStep].y, captureW, captureH);

            // rgbImageData2 is cropped image
            // convert to 565 here
            mCaptureData[mCapStep] = new short[captureW * captureH];

            // mCaptureData[mCapStep] = Image.createRGBImage(rgbImageData2,
            // captureW,
            // captureH, false);

            EffectAlgorithms.RGB32to16bits(rgbImageData2, mCaptureData[mCapStep], captureW,
                    captureH);

            int snapW = mMode.mTipCells[mCapStep].width;
            int snapH = mMode.mTipCells[mCapStep].height;

            int[] snapData = new int[mMode.mTipCells[mCapStep].width
                    * mMode.mTipCells[mCapStep].height];
            Scaling.scaleImage(rgbImageData2, captureW, captureH, snapData,
                    snapW,
                    snapH);
            mSnapshots[mCapStep] = Image.createRGBImage(snapData, snapW, snapH,
                    false);
        } else {
            // Jpeg decode to RGB565
            ImageRGB output = new ImageRGB();
            JpegDecoder jpgdecoder = new JpegDecoder();
            try {
                jpgdecoder.setRoate(1);
                jpgdecoder.decode(snapshotRaw, output);
            } catch (Exception e) {
                Camera5in1.display_error(e.getMessage());
            }
            int captureW = mMode.cell[mCapStep].width;
            int captureH = mMode.cell[mCapStep].height;
            mCaptureData[mCapStep] = new short[captureW * captureH];
            Scaling.cropImage16(output.RGB565, mCaptureData[mCapStep], mMode.image_width,
                    mMode.image_height, mMode.cell[mCapStep].x,
                    mMode.cell[mCapStep].y, captureW, captureH);

            int snapW = mMode.mTipCells[mCapStep].width;
            int snapH = mMode.mTipCells[mCapStep].height;

            int[] snapData = new int[mMode.mTipCells[mCapStep].width
                    * mMode.mTipCells[mCapStep].height];
            Scaling.scaleImage16to32(mCaptureData[mCapStep], captureW, captureH, snapData, snapW,
                    snapH);
            mSnapshots[mCapStep] = Image.createRGBImage(snapData, snapW, snapH, false);
        }

        mCapStep++;
        // if (mCapStep == mMode.image_num) {
        // if (!saveFile()) {
        // mModeExitListener.onStorageCheck();
        // return;
        // } else {
        // if (!mModeExitListener.onStorageCheck())
        // return;
        //
        // mTimer = new Timer();
        // mTimer.schedule(new TimerTask() {
        //
        // public void run() {
        // resetSnapShots();
        // repaint();
        // }
        // }, 3000);
        // }
        // }
        mPreviewing = false;
        repaint();
    }

    private Timer mTimer = null;

    private boolean saveFile() {
        Calendar rightNow = Calendar.getInstance();
        String filename = "pic_fusion_" + rightNow.get(Calendar.YEAR)
                + rightNow.get(Calendar.MONTH)
                + rightNow.get(Calendar.DAY_OF_MONTH)
                + rightNow.get(Calendar.HOUR_OF_DAY)
                + rightNow.get(Calendar.HOUR_OF_DAY)
                + rightNow.get(Calendar.MINUTE) + rightNow.get(Calendar.SECOND)
                + ".jpg";
        // for 32bits mode
        /*
         * Image image = Image.createImage(mMode.image_width,
         * mMode.image_height); Graphics g = image.getGraphics();
         */
        short[] imagedata = new short[mMode.image_width * mMode.image_height];
        for (int i = 0; i < mMode.image_num; i++) {

            // for 32bits mode
            /*
             * g.drawImage(mCaptureData[i], mMode.cell[i].x, mMode.cell[i].y,
             * 0);
             */
            // for 16bits mode
            try
            {
                //System.out.println("mCaptureData[i]=" + mCaptureData[i]);
                Scaling.fillimage16(imagedata, mMode.image_width, mMode.image_height, mCaptureData[i],
                        mMode.cell[i].x, mMode.cell[i].y, mMode.cell[i].width, mMode.cell[i].height);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
            mCaptureData[i] = null;

        }
        //System.gc();
        // for 32bits mode
        /*
         * int[] rgbImageData = new int[image.getHeight() * image.getWidth()];
         * image.getRGB(rgbImageData, 0, image.getWidth(), 0, 0,
         * image.getWidth(),image.getHeight());return
         * JpegFile.saveImage(filename, rgbImageData,
         * image.getWidth(),image.getHeight());
         */
        // for16bits

        return JpegFile.saveImage16(filename, imagedata, mMode.image_width, mMode.image_height);
    }

    protected void keyPressed(int keyCode) {
        if (mBusy)
            return;

        switch (keyCode) {
            case KEY_NUM5:
            case DeviceKeycode.KEY_CODE_CENTER:
                if (!mPreviewing) {
                    if (mCapStep == 2) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                        }

                        resetSnapShots();
                        repaint();
                    } else {
                        restartPreview();
                    }
                    mCapturing = false;
                } else {
                    mBusy = true;
                    mCapturing = true;
                    new CaptureThread().start();
                }
                break;
            case KEY_NUM3:
            case DeviceKeycode.KEY_CODE_RIGHT_SOFT:
                mBusy = true;
                if (mPlayer != null) {
                    mPlayer.close();
                    mPlayer = null;
                }
                resetSnapShots();
                if (mListener != null) {
                    mListener.onCaptureBackButtonPressed();
                }
                mBusy = false;
                break;
        }
    }

    private void resetSnapShots() {
        for (int i = 0; i < mSnapshots.length; i++) {
            mSnapshots[i] = null;
            mCaptureData[i] = null;
        }
        mCapStep = 0;
    }

    /**
     * Thread to capture images.
     */
    class CaptureThread extends Thread {

        public CaptureThread() {
        }

        public void run() {
            capture();
        }

        public void capture() {
            try {
                byte[] raw = null;
                if (!Camera5in1.mEmulator) {
                    // data = mVideoControl.getSnapshot("width="
                    // + Fusion.CAPTURING_WIDTH + "&height="
                    // + Fusion.CAPTURING_HEIGHT);
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

                if (raw == null) {
                    if (Camera5in1.mDebug)
                        DebugUtil.Log("test2");
                    mCapturing = false;
                    mBusy = false;
                    return;
                }
                
                mCapturingRaw[mCapStep] = new byte[raw.length];
                System.arraycopy(raw, 0, mCapturingRaw[mCapStep], 0, raw.length);
                
                //System.out.println("step:"+mCapStep);
                postJpegImage(raw);
                mPlayer.close();
                mPlayer = null;
                mVideoControl = null;
                // restartPreview();
                // mCpaturing=false;
                repaint();
                mBusy = false;
            } catch (SecurityException e) {
           	 mPlayer.close();
             mPlayer = null;
             mVideoControl = null;
        	 startPreview();
        	 System.gc();
             mBusy = false;
        	 return;
        	 
        }catch (Exception e) {
                e.printStackTrace();
                if (mPlayer != null) {
                    mPlayer.close();
                    mPlayer = null;
                }
                mVideoControl = null;
                mCapturing = false;
                mBusy = false;
                restartPreview();
            }
        }
    };

    protected void hideNotify() {
//        if (Camera5in1.mDebug)
//            DebugUtil.Log("hideNotify:" + mbIncomingCallflag);
//        if (!mCpaturing) {
//            mbIncomingCallflag = true;
//        }
    }

    protected void showNotify() {
//        if (Camera5in1.mDebug)
//            DebugUtil.Log("showNotify:" + mbIncomingCallflag);
//        if (mbIncomingCallflag) {
//            if (mPlayer != null) {
//                mPlayer.close();
//                mPlayer = null;
//                mVideoControl = null;
//                restartPreview();
//                System.gc();
//            }
//            mbIncomingCallflag = false;
//        }
        if (mPlayer != null) {
            try {
                switch (mPlayer.getState()) {
                case Player.STARTED:
                    // doing nothing
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
                e.printStackTrace();
            }
        }
    }
/*
 * switch(mCapStep)
        	{
        	case 0:
        	case 1:
                if (mPlayer != null) {
                    mPlayer.close();
                    mPlayer = null;
                }
                resetSnapShots();
        	break;
        	case 2:
        		mCapStep=1;
        		restartPreview();
        		break;
        	case 3:
        		mCapStep=2;
        		repaint();
        		break;
        	default:
        		if (mListener != null) {
                    mListener.onCaptureBackButtonPressed();
                }
        		break;
        	}
 * */
    public void commandAction(Command c, Displayable d) {
    	if(mBusy)
    		return;
    	
        if (c == mCommandBack) {
            mBusy = true;
            if (mPlayer != null) {
                mPlayer.close();
                mPlayer = null;
            }
            resetSnapShots();
            if (mCapStep == 2) {
                restartPreview();
            }
            else {
                if (mListener != null) {
                    mListener.onCaptureBackButtonPressed();
                }
            }
            mBusy = false;
        }
    }

    protected void pointerPressed(int x, int y) {
    	if(mBusy)
    		return;
    	
        if (mCapStep == 2) {
        	//System.out.println(" mCapStep 2");
        	mCurrentEditFrame=selectFrame(x,y);
       		if(mCurrentEditFrame!=-1)
       		{
       			mCapStep=3;
        		mIpp=new PicImageProcess(mCapturingRaw[mCurrentEditFrame]);
        		//mEditModeDisplayW=(getWidth()>>2)*3;
        		mEditModeDisplayW=mMode.hintcell[mCurrentEditFrame].width;
        		mEditModeDisplayH=mMode.hintcell[mCurrentEditFrame].height;
        		mIpp.setZoomRect(mMode.cell[mCurrentEditFrame].x, mMode.cell[mCurrentEditFrame].y, mMode.cell[mCurrentEditFrame].width, mMode.cell[mCurrentEditFrame].height);
        		float rate=mEditModeDisplayW/mMode.cell[mCurrentEditFrame].width;
        		//mEditModeDisplayH=(int) (mMode.cell[mCurrentEditFrame].height*mEditModeDisplayW/mMode.cell[mCurrentEditFrame].width);
        		mEditModeImage=new ImageItem(mIpp,mEditModeDisplayX,mEditModeDisplayY,mEditModeDisplayW,mEditModeDisplayH);
        		mEditModeImage.setTouchMode(true);
        		//System.out.println("step 3:"+mCurrentEditFrame);
       		}
		mSave.onPressed(x, y);
        } else if(mCapStep == 3) {
        	if(mEditModeImage!=null)
            	mEditModeImage.onItemTouchPress(x, y);
       		mSave.onPressed(x, y);
       		repaint();
        }
        else if (!mPreviewing) {
            mReadyCapture.onPressed(x, y);
            repaint();
        } else {
            mCapture.onPressed(x, y);
            repaint();
        }
        //System.out.println(" pointerPressed 2");
        
    }

    protected void pointerReleased(int x, int y) {
    	if(mBusy)
    		return;
    	
        if (mCapStep == 2) {
        	
            mSave.onUnpressed(x, y);
            
        } else if(mCapStep == 3)
        {
        	if(mEditModeImage!=null)
            	mEditModeImage.onItemTouchRelease(x, y);
        	mSave.onUnpressed(x, y);
        	//System.out.println("pointerReleased:"+mCurrentEditFrame);
        	repaint();
        }else {
            if (!mPreviewing) {
                mReadyCapture.onUnpressed(x, y);
            } else {
                mCapture.onUnpressed(x, y);
            }
            repaint();
        }
    }
    
    protected void pointerDragged(int x, int y)
    {
    	if(mBusy)
    		return;
    	
    	if(mEditModeImage!=null)
    	{
    		mEditModeImage.onItemTouchDragged(x, y);
        	repaint();
    	}
    }

    public void buttonClicked(Button caller) {
        //System.out.println("button clicked");
        if (caller == mCapture) {
            mBusy = true;
            mCapturing = true;
            //play sound
            SystemUtil.playSound(0, getClass());
            
            new CaptureThread().start();

        } else if (caller == mReadyCapture) {
            if (mCapStep == 2) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                resetSnapShots();
                repaint();
            } else {
                restartPreview();
            }
            mCapturing = false;
        } else if (caller == mSave) {
        	if (mCapStep == 2)
        	{
            mBusy = true;
            new Thread(new Runnable() {
                public void run() {
                    boolean result = saveFile();
                    //System.out.println("result=" + result);
                    if (result) {
                        resetSnapShots();
                        repaint();
                    }
                    mBusy = false;
                }
            }).start();
        	}else if (mCapStep == 3)
        	{
        		mBusy = true;
        		
        		short[] sourcebuffer=mIpp.getSourceBuffer();
                Scaling.cropImage16(sourcebuffer, mCaptureData[mCurrentEditFrame], mIpp.getSourceWidth(), mIpp.getSourceHeight(),
                         mIpp.getPostionX(),mIpp.getPostionY(), mMode.cell[mCurrentEditFrame].width, mMode.cell[mCurrentEditFrame].height);

                int snapW = mMode.mTipCells[mCurrentEditFrame].width;
                int snapH = mMode.mTipCells[mCurrentEditFrame].height;
                int[] snapData = mIpp.render(snapW, snapH);
                
                mSnapshots[mCurrentEditFrame] = Image.createRGBImage(snapData, snapW, snapH, false);
                
                mMode.cell[mCurrentEditFrame].x=mIpp.getPostionX();
                mMode.cell[mCurrentEditFrame].y=mIpp.getPostionY();
                
        		mCapStep=2;
        		mBusy = false;
        		repaint();

        	}
        }
    }
    
    private int selectFrame(int x,int y)
    {
    	
    	for(int i=0;i<mMode.image_num;i++)
    	{
    		if(x>mMode.hintcell[i].x && x<(mMode.hintcell[i].x+mMode.hintcell[i].width))
    		{
    			if(y>mMode.hintcell[i].y && y<(mMode.hintcell[i].y+mMode.hintcell[i].height))
    			{
    				return i;
    			}
    		}
    	}
    	
    	return -1;
    }
}
