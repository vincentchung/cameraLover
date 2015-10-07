package com.pictelligent.s40.camera5in1.effects;

import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.JPEGDecoder.ImageRGB;
import com.pictelligent.s40.camera5in1.JPEGDecoder.JpegDecoder;

public class PicImageProcess {
	
	//render information
	private int[] render_buffer=null;
	private int mRenderHeight=0;
	private int mRenderWidth=0;
	//source information
	private int mSourceHeight=0;
	private int mSourceWidth=0;
	private short[] fullImage16bits = null; // 16bits, original size images
	//scaling information
	private int mScalx=0,mScaly=0,mScalW=0,mScalH=0;
	//touch support
	public final static int PICROTATION_TO_0=1;
	public final static int PICROTATION_TO_90=2;
	public final static int PICROTATION_TO_180=3;
	public final static int PICROTATION_TO_270=4;
	
	private int mRotate=0;
	
	
	
	public PicImageProcess(short[] raw,int w, int h)
	{
		fullImage16bits=raw;
		mSourceHeight=h;
		mSourceWidth=w;
		setZoomRect(0,0,mSourceWidth,mSourceHeight);
	}
	
	public PicImageProcess(byte[] jpegbuffer)
	{
		
		ImageRGB output = new ImageRGB();
        JpegDecoder jpgdecoder = new JpegDecoder();
        try {
        	jpgdecoder.setRoate(1);
            jpgdecoder.decode(jpegbuffer, output);
        } catch (Exception e) {
        	e.printStackTrace();
            // TODO Auto-generated catch block
            Camera5in1.display_error(e.getMessage());
        }
        
        fullImage16bits = output.RGB565;
        mSourceHeight=output.Height;
        mSourceWidth=output.Width;
        //System.out.println("w:"+mSourceWidth+",h:"+mSourceHeight);
        
        setZoomRect(0,0,mSourceWidth,mSourceHeight);
	}
	
	public void setZoomRect(int x,int y, int w, int h)
	{
		mScalx=x;
		mScaly=y;
		mScalW=w;
		mScalH=h;
	}
	
	public int getSourceWidth()
	{
		return mSourceWidth;
	}
	
	public int getSourceHeight()
	{
		return mSourceHeight;
	}
	
	public void setScreenSize(int w, int h)
	{
		if(mRenderHeight!=h || mRenderWidth!=w)
		{
			mRenderHeight=h;
			mRenderWidth=w;
			render_buffer=null;
		}
		
		if(render_buffer==null)
		{
			render_buffer=new int[mRenderWidth*mRenderHeight];
		}
	}
	
	//it will return null without doing any rendering.
	public int[] getCurrentScreen()
	{
		return render_buffer;
	}
	
	private void checkRange()
	{
		if(mScalx<0)
    		mScalx=0;
    	if(mScaly<0)
    		mScaly=0;
    	
    	if((mScalx+mScalW)>mSourceWidth)
    	{
    		mScalx=mSourceWidth-mScalW;
    	}
    	
    	if((mScaly+mScalH)>mSourceHeight)
    	{
    		mScaly=mSourceHeight-mScalH;
    	}
	}
	
	public void setPan(int deltaX,int deltaY)
	{
		mScalx-=deltaX;
		mScaly-=deltaY;
    	
		checkRange();
		////System.out.println("pan:x"+mScalx+",y:"+mScaly);
	}
	
	public void setZoom(float zoomRate)
	{
		mScalW=(int)(mSourceWidth*zoomRate);
		mScalH=(int)(mSourceHeight*zoomRate);
	
		if(mScalx==0 && mScaly==0)
		{
			mScalx=(mSourceWidth-mScalW)>>1;
			mScaly=(mSourceHeight-mScalH)>>1;
		}else
		{
			checkRange();
		}
	}
	
	//(x,y) is the center of zoom box
	public void setZoomPoint(int x,int y)
	{
		mScalx=x-(mScalW>>1);
		mScaly=y-(mScalH>>1);		
		checkRange();
	}
	
	public int[] render()
	{
		return render(mRenderWidth,mRenderHeight);
	}
	
	public Image renderAsImage(int w, int h)
	{
		if(mRenderHeight!=h || mRenderWidth!=w)
		{
			mRenderHeight=h;
			mRenderWidth=w;
			render_buffer=null;
		}
		
		if(render_buffer==null)
		{
			render_buffer=new int[mRenderWidth*mRenderHeight];
		}
		
		////System.out.println("x:"+mScalx+",y:"+mScaly+",w:"+mScalW+",h:"+mScalH+",rW:"+mRenderWidth+",rH:"+mRenderHeight);
		
		int tempScaleRatioWidth = ((mScalW << 16) / mRenderWidth);
        int tempScaleRatioHeight = ((mScalH << 16) / mRenderHeight);
        int rcounter=0;
        
        if(mScalW==mSourceWidth)
        {
        	//System.out.println("testt2w:"+mRenderWidth+",h:"+mRenderHeight);
        	Scaling.scaleImage16to32(fullImage16bits, mSourceWidth, mSourceHeight, render_buffer, mRenderWidth, mRenderHeight);
        }
        
		return Image.createRGBImage(render_buffer,
				mRenderWidth, mRenderHeight, false);
	}
	
	public int[] render(int w, int h)
	{
		//System.out.println("render:x:"+mScalx+",y:"+mScaly+",w:"+mScalW+",h:"+mScalH+",rW:"+mRenderWidth+",rH:"+mRenderHeight);
		if(mRenderHeight!=h || mRenderWidth!=w)
		{
			mRenderHeight=h;
			mRenderWidth=w;
			render_buffer=null;
		}
		
		if(render_buffer==null)
		{
			render_buffer=new int[mRenderWidth*mRenderHeight];
		}
		
		
		
		int tempScaleRatioWidth = ((mScalW << 16) / mRenderWidth);
        int tempScaleRatioHeight = ((mScalH << 16) / mRenderHeight);
        int rcounter=0;
        
        if(mScalW==mSourceWidth && mScalH==mSourceHeight)
        {
        	//System.out.println("testt2w:"+mRenderWidth+",h:"+mRenderHeight);
        	Scaling.scaleImage16to32(fullImage16bits, mSourceWidth, mSourceHeight, render_buffer, mRenderWidth, mRenderHeight);
        }else
        {
        	for (int y = 0; y < mRenderHeight; y++) {
            	
                for (int x = 0; x < mRenderWidth; x++) {
                	////System.out.println(s);
    	            	//crop
    	                //rbgDataOut[(i - mScaly) * mScalW + (j - mScalx)] = rbgDataIn[i * mScalW + j];
                	int s=(mSourceWidth * ((((y) * tempScaleRatioHeight) >> 16)+mScaly))
                            + ((((x) * tempScaleRatioWidth) >> 16)+mScalx);
                	
                	/*
                	 * rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16))
                               + ((x * tempScaleRatioWidth) >> 16)]
                	 * */
                	try
                	{
                		 render_buffer[rcounter] = EffectAlgorithms.RGB16to32bit(
     	                		fullImage16bits[s]);
                	}catch(Exception e)
                	{
                		e.printStackTrace();
                		Camera5in1.display_error(e.getMessage());
                		return null;
                	}
    	               
    	                rcounter++;
    	                
    	            }
    	        }
        }
        return render_buffer;
	}

	public short[] getSourceBuffer()
	{
		return fullImage16bits;
	}
	
	public short[] getResizeSourceBuffer(int width,int height)
	{
		short[] orgImage16bits = new short[width * height];

        Scaling.scaleImage16(fullImage16bits, mSourceWidth, mSourceHeight,
                orgImage16bits, width, height);
        return orgImage16bits;
	}
	
	
	public void setRoate(int r)
	{
		mRotate=r;
	}
	
	public int getRenderHeight()
	{
		return mRenderHeight;
	}
	
	public int getRenderWidth()
	{
		return mRenderWidth;
	}
	
	public int getPostionX()
	{
		return mScalx;
	}
	
	public int getPostionY()
	{
		return mScaly;
	}
	
	public int getPostionW()
	{
		return mScalW;
	}
	
	public int getPostionH()
	{
		return mScalH;
	}
}
