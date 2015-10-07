package com.pictelligent.s40.camera5in1.components;

import javax.microedition.lcdui.Graphics;

import com.pictelligent.s40.camera5in1.effects.PicImageProcess;

//support displaying jpeg
//pan and zoom UI 
public class ImageItem extends UIItem {

	private PicImageProcess mIpp=null;
	private boolean mPanMode=false;
	private int[] mDisplayImage = null; // 32bits, small size for displaying
	private float mZoomf=1.0f;
    private int mLastX=0;
    private int mLastY=0;
    private boolean mTouchMode=false;
    
	
	public ImageItem(PicImageProcess ipp)
	{
		mIpp=ipp;
	}
	
	public ImageItem(PicImageProcess ipp,int x,int y, int w, int h)
	{
		mIpp=ipp;
		mIpp.setScreenSize(w, h);
		mDisplayWidth=w;
		mDisplayHeight=h;
		mDisplayX=x;
		mDisplayY=y;
		mDisplayImage=mIpp.render();
	}

	public void onItemPress() {
		// TODO Auto-generated method stub
		
	}

	public void onItemUnPress() {
		// TODO Auto-generated method stub
		
	}

	public void onItemTouchDragged(int x,int y) {
		if(mPanMode)
    	{
    		int dx=0;
        	int dy=0;
        	//System.out.println("Dragged:x:"+x+",y:"+y);
        	if(mLastX==0)
        		mLastX=x;
        	if(mLastY==0)
        		mLastY=y;
        	dx=x-mLastX;
        	dy=y-mLastY;
        	
        	mIpp.setPan(dx, dy);
        	mLastX=x;
        	mLastY=y;
        	mDisplayImage=mIpp.render();
    	}
		
	}

	public void onItemTouchPinich(int getPinchDistanceChange) {
		if(!mTouchMode)
			return;
		
		if(Math.abs(getPinchDistanceChange)<2)
		return;
		
		//System.out.println("Pinich:"+getPinchDistanceChange);
		
		if (getPinchDistanceChange < 0) {
        	
        	mZoomf+=0.01f;
        	//System.out.println("Pinich:"+getPinchDistanceChange);
        	if(mZoomf>1.0f)
        		mZoomf=1.0f;
        	mIpp.setZoom(mZoomf);
        } else if (getPinchDistanceChange > 0) {
        	//System.out.println(">0:"+mZoomf);
        	mZoomf-=0.01f;
        	if(mZoomf<0.1f)
        		mZoomf=0.1f;
        	mIpp.setZoom(mZoomf);
        }
		mDisplayImage=mIpp.render();
	}
	
	public void render(Graphics g) {
        g.translate(-g.getTranslateX(), -g.getTranslateY());
        if (mDisplayImage != null) {
        	//System.out.println("mDisplayY:"+mDisplayY+"mDisplayXy:"+mDisplayX+",mDisplayWidth:"+mDisplayWidth+",mDisplayHeight:"+mDisplayHeight);
            g.drawRGB(mDisplayImage, 0, mIpp.getRenderWidth(), mDisplayX, mDisplayY, mDisplayWidth, mDisplayHeight, true);
        }
    }

	public void onItemTouchPress(int x, int y) {
		if(!mTouchMode)
			return;
		if(x>mDisplayX && y>mDisplayY)
    	{
    		if(x<(mDisplayX+mDisplayWidth) && y<(mDisplayY+mDisplayHeight))
    		{
    			if(!mPanMode)
    			{
    				mLastX=x;
        			mLastY=y;
        			mPanMode=true;
    			}
    			
    			
    			int dx=0;
            	int dy=0;
            	//System.out.println("press:"+x+",y:"+y+",lastX:"+mLastX+",mLastY:"+mLastY);
            	if(mLastX==0)
            		mLastX=x;
            	if(mLastY==0)
            		mLastY=y;
            	dx=x-mLastX;
            	dy=y-mLastY;
            	mIpp.setPan(dx, dy);
            	mLastX=x;
            	mLastY=y;
            	mDisplayImage=mIpp.render();
    		}
    	}
	}

	public void onItemTouchRelease(int x, int y) {
		//System.out.println("release x:"+x+",y:"+y);
		if(!mTouchMode)
			return;
		
		if(mPanMode)
    	{
			//System.out.println("release x:"+x+",y:"+y);
    		mLastX=0;
    		mLastY=0;
    		mPanMode=false;
    	}
	}
	
	public int[] getDisplayImageBuffer()
	{
		return mDisplayImage;
	}
	
	public void setTouchMode(boolean b)
	{
		mTouchMode=b;
	}
}
