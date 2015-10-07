
package com.pictelligent.s40.camera5in1.Collage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.nokia.mid.ui.gestures.GestureListener;
import com.nokia.mid.ui.gestures.GestureRegistrationManager;
import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.ModeExitListener;
import com.pictelligent.s40.camera5in1.SystemUtil;
import com.pictelligent.s40.camera5in1.JPEGDecoder.ImageRGB;
import com.pictelligent.s40.camera5in1.JPEGDecoder.JpegDecoder;
import com.pictelligent.s40.camera5in1.JPEGEncoder.JpegFile;
import com.pictelligent.s40.camera5in1.components.Button;
import com.pictelligent.s40.camera5in1.components.ButtonListener;
import com.pictelligent.s40.camera5in1.components.ImageItem;
import com.pictelligent.s40.camera5in1.effects.EffectAlgorithms;
import com.pictelligent.s40.camera5in1.effects.PicImageProcess;
import com.pictelligent.s40.camera5in1.effects.Scaling;

public class CollageCaptureCanvas extends Canvas implements
        ButtonListener, CommandListener,GestureListener {

    public interface CollageCaptureListener {
        void onCaptureBackButtonPressed();

        void onEffectWriteFaild();
    }

    protected CollageCaptureListener mListener;
    private ModeExitListener mModeExitListener;
    protected int mVCX;
    protected int mVCY;
    protected int mVCWidth;
    protected int mVCHeight;
    protected VideoControl mVideoControl;
    protected Player mPlayer;
    private boolean mBusy;
    private Button mCapture;
    private Button mSave;
    private Button mLeftArrowBtn;
    private Button mRightArrowBtn;
    private Command mCommandBack = new Command("Back", Command.BACK, 1);
    private CollageMode mCmode;
    private boolean m16bits = true;
    private Image mIconCollageFrame;
    private Image[] mSnapshotsPreview;
    private short[][] mSnapshotsdata = null;
    private short[] mIconScaledCollageFrameData = null;
    private int mCapStep = 0;
    private boolean mbIncomingCallflag = false;
    private boolean mCpaturing = false;
    
    //private boolean mCaptureDone;
    /*
     * 0:camera preview
     * 1:camera capturing
     * 2:after done capturing, review the result
     * 3: editing selected picture mode
     * 4:saving the result
     * */
    private int mProcessMode=0;

    private ImageItem mEditModeImage=null;
    private String tempfilename="collage_temp_";
    private PicImageProcess mIpp=null;
    private byte[][] mCapturingRaw = null;
    private int mEditModeDisplayX=0;
    private int mEditModeDisplayY=0;
    private int mEditModeDisplayW=0;
    private int mEditModeDisplayH=0;
    private int mCurrentEditFrame=-1;
    private int mFramefileIndex=0;
    
    public CollageCaptureCanvas(CollageMode mode) {
        setFullScreenMode(true);
        initializeGesture();
        int buttonLocationX;
        int buttonLocationY;

        mCmode = mode;        
        mVCX = mode.mHint[0].x;
        mVCY = mode.mHint[0].y;
        mVCWidth = mode.mHint[0].width;
        mVCHeight = mode.mHint[0].height;
        // add back command.
        this.addCommand(mCommandBack);
        this.setCommandListener(this);
        
        if (Camera5in1.mDebug)
            System.out.println("x" + mVCX + "y" + mVCY + "w" + mVCWidth + "h" + mVCHeight);
        
        try {
            mIconCollageFrame = Image.createImage(mode.frame_filename+mFramefileIndex+".jpg");
            mIconScaledCollageFrameData = new short[Collage.CAPTURING_HEIGHT
                    * Collage.CAPTURING_WIDTH];
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] rgbImageData = new int[mIconCollageFrame.getHeight() * mIconCollageFrame.getWidth()];
        mIconCollageFrame.getRGB(rgbImageData, 0, mIconCollageFrame.getWidth(), 0, 0, mIconCollageFrame.getWidth(),
				mIconCollageFrame.getHeight());
        
        EffectAlgorithms.RGB32to16bits(rgbImageData,mIconScaledCollageFrameData,mIconCollageFrame.getWidth(),
				mIconCollageFrame.getHeight());
        
        mCapture = new Button("/ic_new_capture_pressed.png", "/ic_new_capture.png");
        mCapture.setListener(this);
        buttonLocationX=(getWidth()-mCapture.getWidth())/2;
        mCapture.setPosition(buttonLocationX, 280);
        
        mSave = new Button("/ic_new_save_pressed.png", "/ic_new_save.png");
        mSave.setListener(this);
        buttonLocationX=(getWidth()-mSave.getWidth())/2;
        mSave.setPosition(buttonLocationX, 280);
        mSave.setVisable(false);
        
        mLeftArrowBtn = new Button("/ic_new_left_pressed.png", "/ic_new_left.png");
        mLeftArrowBtn.setListener(this);
        
        mRightArrowBtn = new Button("/ic_new_right_pressed.png", "/ic_new_right.png");
        mRightArrowBtn.setListener(this);
        
        buttonLocationX = 5;        
        buttonLocationY = getHeight()/2-mLeftArrowBtn.getHeight()/2;
        mLeftArrowBtn.setPosition(buttonLocationX, buttonLocationY);
        
        buttonLocationX = getWidth()-mRightArrowBtn.getWidth() - 5;		
        mRightArrowBtn.setPosition(buttonLocationX, buttonLocationY);
        
        mSnapshotsdata = new short[mCmode.image_num][];
        mCapturingRaw =new byte[mCmode.image_num][];
        
        mSnapshotsPreview = new Image[mCmode.image_num];
        
        mEditModeDisplayX=(getWidth()>>3);
        mEditModeDisplayY=(getHeight()>>3);
        mEditModeDisplayW=(getWidth()>>1);
        mEditModeDisplayH=(getHeight()>>1);

        if (Camera5in1.mDebug)
            System.out.println("test2:" + mCmode.image_num);
    }
    
    private void SwitchBackground(String path)
    {
    	try {
            mIconCollageFrame = Image.createImage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	int[] rgbImageData = new int[mIconCollageFrame.getHeight() * mIconCollageFrame.getWidth()];
        mIconCollageFrame.getRGB(rgbImageData, 0, mIconCollageFrame.getWidth(), 0, 0, mIconCollageFrame.getWidth(),
				mIconCollageFrame.getHeight());
        EffectAlgorithms.RGB32to16bits(rgbImageData,mIconScaledCollageFrameData,mIconCollageFrame.getWidth(),
				mIconCollageFrame.getHeight());
    }

    private void update_preview(int x, int y, int w, int h) {
        mVCX = x;
        mVCY = y;
        mVCWidth = w;
        mVCHeight = h;
    }

    protected void restartPreview() {
    	mProcessMode=0;
        startPreview();
    }

    protected void startPreview() {
        try {
            mPlayer = Manager.createPlayer("capture://image");
            mPlayer.realize();
            mVideoControl = (VideoControl) mPlayer.getControl("VideoControl");
            mVideoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
            try {
                mVideoControl.setDisplayLocation(mVCX, mVCY);
                mVideoControl.setDisplaySize(mVCWidth, mVCHeight);
            } catch (MediaException me) {
            }
            mVideoControl.setVisible(true);
            mPlayer.start();
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    protected void paint(Graphics g) {
        if (mIconCollageFrame != null) {
        	int bordersize = mCmode.frameBorderWidth;
            g.drawImage(mIconCollageFrame, 0, 0, 0);
            // draw frame placeholders to indicate where they will be drawn
        	for (int k=0; k<mCmode.image_num; k++) {
        		g.setColor(mCmode.frameBorderColor);
        		g.fillRect(mCmode.mHint[k].x-bordersize, mCmode.mHint[k].y-bordersize, mCmode.mHint[k].width+2*bordersize, mCmode.mHint[k].height+2*bordersize);
        		g.setColor(mCmode.frameFillColor);
        		g.fillRect(mCmode.mHint[k].x, mCmode.mHint[k].y, mCmode.mHint[k].width, mCmode.mHint[k].height);
        	}
        }
        
        for (int i = 0; i < mCapStep; i++) {
            if (mSnapshotsPreview[i] != null) {
                g.drawImage(mSnapshotsPreview[i], mCmode.mHint[i].x,
                        mCmode.mHint[i].y, 0);
            }
        }

        if (Camera5in1.mDebug) {
            System.out.println("paint2:" + mCapStep);
        }

        if(mEditModeImage!=null)
        	mEditModeImage.render(g);
        mSave.render(g);
        mCapture.render(g);
        mRightArrowBtn.render(g);
        mLeftArrowBtn.render(g);
    }

    public void setListener(CollageCaptureListener listener) {
        mListener = listener;
    }

    public void setExistListener(ModeExitListener listener) {
        mModeExitListener = listener;
    }

    protected void resetSnapShots() {

    	//System.out.println("resetSnapShots");
        for (int i = 0; i < mCmode.image_num; i++) {
            mSnapshotsdata[i] = null;
        }

        for (int i = 0; i < mSnapshotsPreview.length; i++) {
            mSnapshotsPreview[i] = null;
        }
        mCapStep = 0;
    }

    private boolean saveFile() {
        // Date d=new Date();
        Calendar rightNow = Calendar.getInstance();
        String filename = "pic_collage_" + rightNow.get(Calendar.YEAR)
                + rightNow.get(Calendar.MONTH) + rightNow.get(Calendar.DAY_OF_MONTH)
                + rightNow.get(Calendar.HOUR_OF_DAY) + rightNow.get(Calendar.HOUR_OF_DAY)
                + rightNow.get(Calendar.MINUTE) + rightNow.get(Calendar.SECOND) + ".jpg";
        System.gc();
        //return JpegFile.saveImage16(filename, mIconScaledCollageFrameData, Collage.CAPTURING_WIDTH,
        //        Collage.CAPTURING_HEIGHT);
        return JpegFile.saveImage16(filename, mIconScaledCollageFrameData, mCmode.frame_width,
        		mCmode.frame_height);
    }

    protected boolean postJpegImage(byte[] snapshotRaw) {
        if (m16bits) {
            ImageRGB output = new ImageRGB();
            JpegDecoder jpgdecoder = new JpegDecoder();
            try {
                jpgdecoder.setRoate(1);
                jpgdecoder.decode(snapshotRaw, output);
            } catch (Exception e) {
                Camera5in1.display_error(e.getMessage());
            }
            
            if(mCapStep==0)
            {
            	int[] rgbImageData = new int[mIconCollageFrame.getHeight() * mIconCollageFrame.getWidth()];
                mIconCollageFrame.getRGB(rgbImageData, 0, mIconCollageFrame.getWidth(), 0, 0, mIconCollageFrame.getWidth(),
        				mIconCollageFrame.getHeight());
                drawFrames8888(rgbImageData, mIconCollageFrame.getWidth(), mIconCollageFrame.getHeight());
                EffectAlgorithms.RGB32to16bits(rgbImageData,mIconScaledCollageFrameData,mIconCollageFrame.getWidth(),
        				mIconCollageFrame.getHeight());
            }
            
            mSnapshotsdata[mCapStep] = new short[mCmode.cell[mCapStep].width
                    * mCmode.cell[mCapStep].height];

            Scaling.scaleImage16(output.RGB565, Collage.CAPTURING_WIDTH, Collage.CAPTURING_HEIGHT,
                    mSnapshotsdata[mCapStep], mCmode.cell[mCapStep].width,
                    mCmode.cell[mCapStep].height);
            output.RGB565 = null;
            int[] rgbImageData = new int[mCmode.mHint[mCapStep].width
                    * mCmode.mHint[mCapStep].height];
            Scaling.scaleImage16to32(mSnapshotsdata[mCapStep], mCmode.cell[mCapStep].width,
                    mCmode.cell[mCapStep].height,
                    rgbImageData, mCmode.mHint[mCapStep].width, mCmode.mHint[mCapStep].height);
            mSnapshotsPreview[mCapStep] = Image.createRGBImage(rgbImageData,
                    mCmode.mHint[mCapStep].width, mCmode.mHint[mCapStep].height, false);

            Scaling.fillimage16(mIconScaledCollageFrameData, Collage.CAPTURING_WIDTH,
                    Collage.CAPTURING_HEIGHT,
                    mSnapshotsdata[mCapStep], mCmode.cell[mCapStep].x, mCmode.cell[mCapStep].y,
                    mCmode.cell[mCapStep].width, mCmode.cell[mCapStep].height);
            mSnapshotsdata[mCapStep] = null;
            jpgdecoder = null;
            System.gc();
        } else {
            Image image = Image.createImage(snapshotRaw, 0, snapshotRaw.length);
            int[] rgbImageData = new int[image.getHeight() * image.getWidth()];
            image.getRGB(rgbImageData, 0, image.getWidth(), 0, 0, image.getWidth(),
                    image.getHeight());
            if (mCapStep > mCmode.image_num - 1) {
                //System.out.print("_postJpegImage: mCapStep=" + mCapStep);
                return true;
            }

            if (Camera5in1.mDebug) {
                System.out.println("_postJpegImage: mCapStep=" + mCapStep);
                System.out.println("_postJpegImage: " + mCmode.cell[mCapStep].width + ","
                        + mCmode.cell[mCapStep].height);
            }

            Scaling.scaleImage(rgbImageData, image.getWidth(),
                    image.getHeight(), mCmode.cell[mCapStep].width, mCmode.cell[mCapStep].height);
            /*
             * mSnapshots[mCapStep] = Image.createRGBImage(rgbImageData,
             * mCmode.cell[mCapStep].width, mCmode.cell[mCapStep].height,
             * false);
             */

            mSnapshotsdata[mCapStep] = new short[mCmode.cell[mCapStep].width
                    * mCmode.cell[mCapStep].height];

            mSnapshotsPreview[mCapStep] = Image.createRGBImage(rgbImageData,
                    mCmode.mHint[mCapStep].width, mCmode.mHint[mCapStep].height, false);

            EffectAlgorithms.RGB32to16bits(rgbImageData, mSnapshotsdata[mCapStep],
                    mCmode.cell[mCapStep].width, mCmode.cell[mCapStep].height);
        }

        if (Camera5in1.mDebug)
            System.out.println("_postJpegImage: mCapStep=" + mCapStep);
        mCapStep++;
        boolean result = true;
        if (mCapStep == mCmode.image_num) {
            //mCaptureDone = true;
        	switchButton(true);
        	mProcessMode=2;
            update_preview(mCmode.mHint[0].x, mCmode.mHint[0].y, mCmode.mHint[0].width,
                    mCmode.mHint[0].height);
        } else {
            update_preview(mCmode.mHint[mCapStep].x, mCmode.mHint[mCapStep].y,
                    mCmode.mHint[mCapStep].width, mCmode.mHint[mCapStep].height);
        }
        repaint();
        return result;
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
            byte[] raw = null;
            if (mVideoControl == null) {
                if (Camera5in1.mDebug)
                    DebugUtil.Log("test");
                mCpaturing = false;
                mBusy = false;
                return;
            }

            try {
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
                    
                	//raw = mVideoControl.getSnapshot(null);
                }

                if (raw == null) {
                    if (Camera5in1.mDebug)
                        DebugUtil.Log("test2");
                    mCpaturing = false;
                    mBusy = false;
                    return;
                }
                
                //SystemUtil.save_file(tempfilename+mCapStep+".jpg", raw);
                mCapturingRaw[mCapStep] = new byte[raw.length];
                System.arraycopy(raw, 0, mCapturingRaw[mCapStep], 0, raw.length);
                
                boolean result = postJpegImage(raw);
                mPlayer.close();
                mPlayer = null;
                mVideoControl = null;
                if (!result) {
                    mListener.onEffectWriteFaild();
                } else {
                    if (mProcessMode!=2) {
                        mModeExitListener.onStorageCheck();
                        restartPreview();
                    }
                }
                mCpaturing = false;
                mBusy = false;
            }catch (SecurityException e) {
            	//System.out.println("security");
           	 mPlayer.close();
             mPlayer = null;
             mVideoControl = null;
        	 startPreview();
        	 System.gc();
        	 mCpaturing = false;
             mBusy = false;
             mProcessMode=0;
        	 return;
        	 
        } catch (Exception e) {
                Camera5in1.display_error(e.getMessage());
            }
        }
    };

    protected void hideNotify() {
        // if (Camera5in1.mDebug)
        // DebugUtil.Log("hideNotify:" + mbIncomingCallflag);
        // if (!mCpaturing) {
        // mbIncomingCallflag = true;
        // }
    }

    protected void showNotify() {
        // if (Camera5in1.mDebug)
        // DebugUtil.Log("showNotify:" + mbIncomingCallflag);
        // if (mbIncomingCallflag) {
        // mPlayer.close();
        // mPlayer = null;
        // mVideoControl = null;
        // restartPreview();
        // System.gc();
        // mbIncomingCallflag = false;
        // }

        if (mPlayer != null)
        {
            try {
                switch (mPlayer.getState())
                {
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
    
    protected void pointerPressed(int x, int y) {
    	if(mBusy)
    		return;
    	//entering selecting mode
    	switch(mProcessMode)
    	{
    	case 0:
    		mCapture.onPressed(x, y);
    		mLeftArrowBtn.onPressed(x, y);
    		mRightArrowBtn.onPressed(x, y);
    	break;
           	case 2:
           		mCurrentEditFrame=selectFrame(x,y);
           		if(mCurrentEditFrame!=-1)
           		{
           			mProcessMode=3;
            		mIpp=new PicImageProcess(mCapturingRaw[mCurrentEditFrame]);
            		mEditModeDisplayW=(getWidth()>>2)*3;
            		if(mEditModeDisplayW<mCmode.cell[mCurrentEditFrame].width)
            		{
            			mEditModeDisplayW=mCmode.cell[mCurrentEditFrame].width;
            			mEditModeDisplayX=0;
            		}
            		
            		//float rate=mEditModeDisplayW/mCmode.cell[mCurrentEditFrame].width;
            		mEditModeDisplayH=(int) (mCmode.cell[mCurrentEditFrame].height*mEditModeDisplayW/mCmode.cell[mCurrentEditFrame].width);
            		
            		mEditModeImage=new ImageItem(mIpp,mEditModeDisplayX,mEditModeDisplayY,mEditModeDisplayW,mEditModeDisplayH);
            		mEditModeImage.setTouchMode(true);
           		}
    		mSave.onPressed(x, y);
    		break;
           	case 3:
           		if(mEditModeImage!=null)
                	mEditModeImage.onItemTouchPress(x, y);
           		mSave.onPressed(x, y);
           	break;
    	}
    	
    	repaint();
    }

    protected void pointerReleased(int x, int y) {
    	if(mBusy)
    		return;
    	mRightArrowBtn.onUnpressed(x, y);
    	mLeftArrowBtn.onUnpressed(x, y);
    	mSave.onUnpressed(x, y);
        mCapture.onUnpressed(x, y);
        if(mEditModeImage!=null)
        	mEditModeImage.onItemTouchRelease(x, y);
        
        repaint();
    }
    
    private void switchButton(boolean s)
    {
    	if(s)
    	{
    		mSave.setVisable(true);
    		mLeftArrowBtn.setVisable(false);
    		mRightArrowBtn.setVisable(false);
        	mCapture.setVisable(false);
    	}else
    	{
    		mSave.setVisable(false);
    		mLeftArrowBtn.setVisable(true);
    		mRightArrowBtn.setVisable(true);
        	mCapture.setVisable(true);
    	}
    }

    public void commandAction(Command cmd, Displayable d) {
    	if(mBusy)
    		return;

        if (cmd == mCommandBack) {
            if (mProcessMode==2) {
            	switchButton(false);
                resetSnapShots();
                restartPreview();
                repaint();
            } else {
            	//System.out.println("command");
                mBusy = true;
                if (mPlayer != null) {
                    mPlayer.close();
                    mPlayer = null;
                }
                resetSnapShots();
                if (mListener != null) {
                    mListener.onCaptureBackButtonPressed();
                }
            }
        }
    }

    public void buttonClicked(Button caller) {
    
        if (caller == mCapture) {
        	//System.out.println("capture");
        	 //play sound
            SystemUtil.playSound(0, getClass());
            
        	mProcessMode=1;//capturing
            mBusy = true;
            mCpaturing = true;
            if (Camera5in1.mDebug)
                DebugUtil.Log("test3");
            mRightArrowBtn.setVisable(false);
            mLeftArrowBtn.setVisable(false);
            
            new CaptureThread().start();
        }else if(caller == mLeftArrowBtn)
    	{
        	if(mFramefileIndex==0)
        		mFramefileIndex=(mCmode.frameNum-1);
        	else
        	mFramefileIndex--;
        	
        	SwitchBackground(mCmode.frame_filename+mFramefileIndex+".jpg");
        	repaint();
        }
    else if(caller == mRightArrowBtn)
    {
    	if(mFramefileIndex==(mCmode.frameNum-1))
    		mFramefileIndex=0;
    	else
    	mFramefileIndex++;
    	
    	SwitchBackground(mCmode.frame_filename+mFramefileIndex+".jpg");
    	repaint();
    }else if (caller == mSave) {
        	//System.out.println("save");
        	if(mProcessMode==3)
        	{
        		mProcessMode=2;
        		if(mIpp!=null)
        		{
        			//System.out.println("save");
        			mBusy = true;
        			try
        			{
        				short[] rgbOutImageData=new short[mCmode.cell[mCurrentEditFrame].width* mCmode.cell[mCurrentEditFrame].height];
            			int[] displaybuffer=mIpp.getCurrentScreen();
            			
            			
            			Scaling.scaleImage32to16(displaybuffer, mIpp.getRenderWidth(), mIpp.getRenderHeight(), rgbOutImageData, mCmode.cell[mCurrentEditFrame].width, mCmode.cell[mCurrentEditFrame].height);		
            			Scaling.fillimage16(mIconScaledCollageFrameData, Collage.CAPTURING_WIDTH,
                                Collage.CAPTURING_HEIGHT,
                                rgbOutImageData, mCmode.cell[mCurrentEditFrame].x, mCmode.cell[mCurrentEditFrame].y,
                                mCmode.cell[mCurrentEditFrame].width, mCmode.cell[mCurrentEditFrame].height);

            			//System.out.println("w:"+mIpp.getRenderWidth()+"h:"+ mIpp.getRenderHeight()+"="+
            			//		"w:"+mCmode.mHint[mCurrentEditFrame].width+"h:"+ mCmode.mHint[mCurrentEditFrame].height);
            			{
            				Scaling.scaleImage(displaybuffer, mIpp.getRenderWidth(), mIpp.getRenderHeight(),
                            		mCmode.mHint[mCurrentEditFrame].width, mCmode.mHint[mCurrentEditFrame].height);
                			
                            
                            
                			mSnapshotsPreview[mCurrentEditFrame]=Image.createRGBImage(displaybuffer,
                                    mCmode.mHint[mCurrentEditFrame].width, mCmode.mHint[mCurrentEditFrame].height, false);	
            			}
                        
            			
            			rgbOutImageData=null;
            			mEditModeImage=null;
            			mCurrentEditFrame=-1;
            			System.gc();
            			repaint();
        			}catch(Exception e)
        			{
        				e.printStackTrace();
        			}
        			mBusy = false;
        			
        		}
        	}else
        	{
        		mBusy = true;
        		//System.out.println("saving4");
                mProcessMode=4;//saving file
                switchButton(false);
                new Thread(new Runnable() {
                    public void run() {
                    	
                    	//fill the frame data
                    	//EffectAlgorithms.effectFuturoOverlayWithAlpha16(mIconScaledCollageFrameData,mIconScaledCollageFrameData, Collage.CAPTURING_WIDTH,
                        //        Collage.CAPTURING_HEIGHT,0,0,mIconCollageFrame.getWidth(),mIconCollageFrame.getHeight(),mIconCollageFrame);
                    	
                        boolean result = saveFile();
                        //System.out.println("result=" + result);
                        if (result) {
                            resetSnapShots();
                            restartPreview();
                            repaint();
                        }
                        mBusy = false;
                    }
                }).start();
        	}
            
        }
    }

    public void gestureAction(Object arg0, GestureInteractiveZone arg1,
			GestureEvent gestureEvent) {
		switch (gestureEvent.getType()) {

        /**
         * This gesture event is supported from Java Runtime 2.0.0 for
         * Series 40 onwards. Receives pinch events and check the pinch
         * distance change to scale the current image.
         */
        case GestureInteractiveZone.GESTURE_PINCH:
        	if(mEditModeImage!=null)
        	{
        		mEditModeImage.onItemTouchPinich(gestureEvent.getPinchDistanceChange());
                repaint();
        	}
            break;
    }
	}

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

	private int selectFrame(int x,int y)
	{
		
		for(int i=0;i<mCmode.image_num;i++)
		{
			if(x>mCmode.mHint[i].x && x<(mCmode.mHint[i].x+mCmode.mHint[i].width))
			{
				if(y>mCmode.mHint[i].y && y<(mCmode.mHint[i].y+mCmode.mHint[i].height))
				{
					return i;
				}
			}
		}
		
		return -1;
	}

	private void drawFrames8888(int[] imageBuffer, int width, int height) {
		// draw borders around collage slots
		int i, j, k;
		int bordersize = mCmode.frameBorderWidth;
		System.out.println("drawFrames8888, size " + width + "x" + height + ", fill color " + mCmode.frameBorderColor);
		for (k=0; k<mCmode.image_num; k++) {
			for (i=mCmode.mHint[k].y-bordersize; i<mCmode.mHint[k].y+mCmode.mHint[k].height+bordersize; i++) {
				for (j=mCmode.mHint[k].x-bordersize; j<mCmode.mHint[k].x+mCmode.mHint[k].width+bordersize; j++) {
					imageBuffer[i*width+j] = mCmode.frameBorderColor;
				}
			}
		}
			
	}

}
