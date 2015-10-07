/*
 * ARGB8888 effect algorithms.
 * 
 * Heavily optimized with fixed-point integer calculations. 
 * 
 * For the record: I hate ARGB8888!
 * 
 */

package com.pictelligent.s40.camera5in1.effects;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;


public class EffectAlgorithms {
	
	private static final int VIGNETTE_MASK_WIDTH = 240;
	private static final int VIGNETTE_MASK_HEIGHT = 320;
	
	private static final int FUTURO_OVERLAY_WIDTH = 240;
	private static final int FUTURO_OVERLAY_HEIGHT = 320;
	
public static int RGB16to32bit(short input)
{
	int rgb=(int)input;
	  int r = ((rgb >> 11) & 0x1f);
    int g = ((rgb >> 5) & 0x3f);
    int b = (rgb & 0x1f);
 
    // convert from 5 bits to 8 bits
    r = (r << 3) ;
    g = (g << 2) ;
    b = (b << 3) ;
    return( 0xff000000 | (r << 16) | (g << 8) | b);
	
}

public static short RGB32to16bit(int input)
{
	
    return (short) (((((input>> 16) & 0x0FF)>>3)<<11) | 
    		((((input>> 8) & 0x0FF)>>2) << 5) | 
    		((input & 0x0FF)>>3));
	
}
	public static void RGB32to16bits(int[] input, short[] output,int w,int h)
	{
		int index=0;
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				//int rgb = input[index];
				
				//int red =   ((input[index]>> 16) & 0x0FF);
                //int green = ((input[index]>> 8) & 0x0FF);
                //int blue =   (input[index] & 0x0FF);
                
                //red = red >> 3;
                //green = green >> 2;
                //blue = blue >> 3;     
                
                output[index]    =(short) (((((input[index]>> 16) & 0x0FF)>>3)<<11) | 
                		((((input[index]>> 8) & 0x0FF)>>2) << 5) | 
                		((input[index] & 0x0FF)>>3));
                index++;
			}
		}
	}
	
	public static void RGB16to32bits(short[] input, int[] output,int w,int h)
	{
		int index=0;
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				int rgb=(int)input[index];
			  int r = ((rgb >> 11) & 0x1f);
	          int g = ((rgb >> 5) & 0x3f);
	          int b = (rgb & 0x1f);
	       
	          // convert from 5 bits to 8 bits
	          r = (r << 3) ;
	          g = (g << 2) ;
	          b = (b << 3) ;
	          output[index] = 0xff000000 | (r << 16) | (g << 8) | b;
	          index++;
			}
		}
	}

	private static void applyColorCurve16(short[] inputBuffer, short[] outputBuffer,
			int width, int height, byte[] curveR, byte[] curveG, byte[] curveB) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		System.out.println("applying effectColorCurve");
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
/*				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
*/
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
		        	   // lookup new color values
				r = ((int) curveR[r]) & 0xff;
				g = ((int) curveG[g]) & 0xff;
				b = ((int) curveB[b]) & 0xff;
				
				r = r >> 3;
                g = g >> 2;
                b = b >> 3;     				
				
				outputBuffer[index]=(short) ((r << 11) | (g << 5) | b);
				index++;
			}
		}
	}
	
	private static void applyColorCurve16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height, byte[] curveR, byte[] curveG, byte[] curveB) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		System.out.println("applying effectColorCurve");
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
/*				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
*/
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
		        	   // lookup new color values
		        r = ((int) curveR[r]) & 0xff;
				g = ((int) curveG[g]) & 0xff;
				b = ((int) curveB[b]) & 0xff;

				outputBuffer[index] = 0xff000000 | (r << 16) | (g << 8) | b;
				index++;
			}
		}
	}
	
	public static void effectXray(int[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb, bw;
		int i, j;
		

		if (Camera5in1.mDebug)
			System.out.println("effect x-ray, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// neg
				r = 255 - r;
				g = 255 - g;
				b = 255 - b;
			
				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
							
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;
				
				outputBuffer[index] = 0xff000000 | (bw << 16) | (bw << 8) | bw;
				index++;
			}
		}
	}

	public static void effectXray16(short[] inputBuffer, short[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		short rgb;
		int bw;
		int i, j;
		

		if (Camera5in1.mDebug)
			System.out.println("effect x-ray, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				
				rgb = inputBuffer[index];
				
				//for get the RGB from 8888 
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// neg
				r = 255 - r;
				g = 255 - g;
				b = 255 - b;
			
				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
							
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;
				
				
				//int rgb32 = 0xff000000 | (bw << 16) | (bw << 8) | bw;
				//outputBuffer[index]    =(short) (((((rgb32>> 16) & 0x0FF)>>3)<<11) | 
	            //    		((((rgb32>> 8) & 0x0FF)>>2) << 5) | 
	            //    		((rgb32 & 0x0FF)>>3));
				r = bw >> 3;
                g = bw >> 2;
                b = bw >> 3;     				
				
				outputBuffer[index]=(short) ((r << 11) | (g << 5) | b);
				index++;
			}
		}
	}
	
	public static void effectXray16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb, bw;
		int i, j;
		

		if (Camera5in1.mDebug)
			System.out.println("effect x-ray, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				
				rgb = inputBuffer[index];
				
				//for get the RGB from 8888 
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// neg
				r = 255 - r;
				g = 255 - g;
				b = 255 - b;
			
				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
							
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;
				
				outputBuffer[index] = 0xff000000 | (bw << 16) | (bw << 8) | bw;
				//outputBuffer[index]=(short) ((bw << 11) | (bw << 5) | bw);
				index++;
			}
		}
	}
	
	public static void effectBW(int[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb, bw;
		int i, j;
		int adjustB;
		int adjustR;

		if (Camera5in1.mDebug)
			System.out.println("effect Black&White, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
				
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;
				
				adjustR = bw + (width-j)*50/width - 25;
				adjustR = adjustR > 255 ? 255 : adjustR;
				adjustR = adjustR <0 ? 0 : adjustR;
				adjustB = bw + j*50/width;
				adjustB = adjustB > 255 ? 255 : adjustB;
				
				outputBuffer[index] = 0xff000000 | (adjustR << 16) | (bw << 8) | adjustB;
				index++;
			}
		}
	}

	public static void effectBW16(short[] inputBuffer, short[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		short rgb;
		int bw;
		int i, j;
		int adjustB;
		int adjustR;

		if (Camera5in1.mDebug)
			System.out.println("effect Black&White, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				
				rgb = inputBuffer[index];
/*
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
	*/			
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
				
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;
				
				adjustR = bw + (width-j)*50/width - 25;
				adjustR = adjustR > 255 ? 255 : adjustR;
				adjustR = adjustR <0 ? 0 : adjustR;
				adjustB = bw + j*50/width;
				adjustB = adjustB > 255 ? 255 : adjustB;
				
				r = adjustR >> 3;
                g = bw >> 2;
                b = adjustB >> 3;     
                
				outputBuffer[index]=(short) ((r << 11) | (g << 5) | b);
				index++;
			}
		}
	}
	
	public static void effectBW16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb, bw;
		int i, j;
		int adjustB;
		int adjustR;

		if (Camera5in1.mDebug)
			System.out.println("effect Black&White, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				
				rgb = inputBuffer[index];
/*
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
	*/			
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
				
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;
				
				adjustR = bw + (width-j)*50/width - 25;
				adjustR = adjustR > 255 ? 255 : adjustR;
				adjustR = adjustR <0 ? 0 : adjustR;
				adjustB = bw + j*50/width;
				adjustB = adjustB > 255 ? 255 : adjustB;
				
				outputBuffer[index] = 0xff000000 | (adjustR << 16) | (bw << 8) | adjustB;
				
				index++;
			}
		}
	}
	
	public static void effectTripping(int[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb, rgb2, bw;
		int spread = 2; // for controlling blur region
		int i, j;
		int offset, ofsx, ofsy;

		if (Camera5in1.mDebug)
			System.out.println("effect Tripping, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				// r = g = b = 0;
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// diagonal sampling window
				for (offset = -5 * spread; offset < 6 * spread; offset += (1 * spread)) {
					
					// clamping to input buffer edges
					if ((offset + j < 0) || (offset + j > width - 1))
						ofsx = 0;
					else
						ofsx = offset;

					if ((offset + i < 0) || (offset + i > height - 1))
						ofsy = 0;
					else
						ofsy = offset;

					rgb2 = inputBuffer[index + ofsx + ofsy * width];
					r += (rgb2 >> 16) & 0xff;
					g += (rgb2 >> 8) & 0xff;
					b += rgb2 & 0xff;
				}

				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;

				bw /= 10; // divide brightness because we still hold the sum of
							// all pixels sampled
				if (bw > 255)
					bw = 255; // saturate; this might happen because we're
								// dividing by less than actual pixels sampled
								// to get an overwashed effect

				outputBuffer[index] = 0xff000000 | (bw << 16) | (bw << 8) | bw;
				index++;
			}
		}
	}
	
	//problem
	public static void effectTripping16(short[] inputBuffer, short[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		short rgb, rgb2;
		int bw;
		int spread = 2; // for controlling blur region
		int i, j;
		int offset, ofsx, ofsy;

		if (Camera5in1.mDebug)
			System.out.println("effect Tripping, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				// r = g = b = 0;
				rgb = inputBuffer[index];
//				r = (rgb >> 16) & 0xff;
//				g = (rgb >> 8) & 0xff;
//				b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
				// diagonal sampling window
				for (offset = -5 * spread; offset < 6 * spread; offset += (1 * spread)) {
					
					// clamping to input buffer edges
					if ((offset + j < 0) || (offset + j > width - 1))
						ofsx = 0;
					else
						ofsx = offset;

					if ((offset + i < 0) || (offset + i > height - 1))
						ofsy = 0;
					else
						ofsy = offset;

					rgb2 = inputBuffer[index + ofsx + ofsy * width];
					r += (rgb2 >> 16) & 0xff;
					g += (rgb2 >> 8) & 0xff;
					b += rgb2 & 0xff;
				}

				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;

				bw /= 10; // divide brightness because we still hold the sum of
							// all pixels sampled
				if (bw > 255)
					bw = 255; // saturate; this might happen because we're
								// dividing by less than actual pixels sampled
								// to get an overwashed effect

				//outputBuffer[index] = 0xff000000 | (bw << 16) | (bw << 8) | bw;
				r = bw >> 3;
                g = bw >> 2;
                b = bw >> 3;     				
				
				outputBuffer[index]=(short) ((r << 11) | (g << 5) | b);
				index++;
			}
		}
	}

	public static void effectTripping16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input and output buffers
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb, rgb2, bw;
		int spread = 2; // for controlling blur region
		int i, j;
		int offset, ofsx, ofsy;

		if (Camera5in1.mDebug)
			System.out.println("effect Tripping, buffer size " + width + "x" + height);
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				// r = g = b = 0;
				rgb = inputBuffer[index];
//				r = (rgb >> 16) & 0xff;
//				g = (rgb >> 8) & 0xff;
//				b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
				// diagonal sampling window
				for (offset = -5 * spread; offset < 6 * spread; offset += (1 * spread)) {
					
					// clamping to input buffer edges
					if ((offset + j < 0) || (offset + j > width - 1))
						ofsx = 0;
					else
						ofsx = offset;

					if ((offset + i < 0) || (offset + i > height - 1))
						ofsy = 0;
					else
						ofsy = offset;

					rgb2 = inputBuffer[index + ofsx + ofsy * width];
					r += (rgb2 >> 16) & 0xff;
					g += (rgb2 >> 8) & 0xff;
					b += rgb2 & 0xff;
				}

				// convert to B&W with fixed point scaler Q10: 0.33->306, 0.59
				// -> 601, 0.11 -> 117, I assume we don't lose MSBs with this
				// precision level
				r = (r * 306);
				g = (g * 601);
				b = (b * 117);
				bw = (r + g + b) >>> 10;

				bw /= 10; // divide brightness because we still hold the sum of
							// all pixels sampled
				if (bw > 255)
					bw = 255; // saturate; this might happen because we're
								// dividing by less than actual pixels sampled
								// to get an overwashed effect

				outputBuffer[index] = 0xff000000 | (bw << 16) | (bw << 8) | bw;
				
				index++;
			}
		}
	}

	public static void effectVignette(int[] inputBuffer, int[] outputBuffer,
			int width, int height, DataInputStream vignetteStream) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		
		if (Camera5in1.mDebug)
			System.out.println("effect Vintage, buffer size " + width + "x" + height);
		
		int index = 0;
		int vignetteIndex = 0;
		int vignetteYIndex = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;
		
	    byte[] vignetteData = new byte[VIGNETTE_MASK_WIDTH*VIGNETTE_MASK_HEIGHT];
	    try {
            vignetteStream.readFully(vignetteData);
	    }
	    catch (IOException e) {
	    }
	    int vgn;
	    
	    index = 0;
		for (i=0; i<height; i++) { // y
			vignetteYIndex = i*VIGNETTE_MASK_HEIGHT/height;
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// vignette mask is 240x320 (i.e. portrait), and it's scaled to output buffer size on the fly
				vignetteIndex = j*VIGNETTE_MASK_WIDTH/width + vignetteYIndex*VIGNETTE_MASK_WIDTH;
				vgn = vignetteData[vignetteIndex];
				if (vgn<0) vgn+=255; // unsigned byte
				
				//r = (vgn*r)>>>8;
				//g = (vgn*g)>>>8;
				//b = (vgn*b)>>>8;	
				//outputBuffer[index] = 0xff000000 | (r << 16) | (g << 8) | b;
				
				outputBuffer[index] = 0xff000000 | (((r*vgn) << 8) & 0xff0000) | ((g*vgn) & 0xff00) | (((b*vgn) >>> 8) & 0xff);
				
				index++;
			}
		}
	}
	
	public static void effectVignette16(short[] inputBuffer, short[] outputBuffer,
			int width, int height, DataInputStream vignetteStream) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		
		if (Camera5in1.mDebug)
			System.out.println("effect Vintage, buffer size " + width + "x" + height);
		
		int index = 0;
		int vignetteIndex = 0;
		int vignetteYIndex = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;
		
	    byte[] vignetteData = new byte[VIGNETTE_MASK_WIDTH*VIGNETTE_MASK_HEIGHT];
	    try {
            vignetteStream.readFully(vignetteData);
	    }
	    catch (IOException e) {
	    }
	    int vgn;
	    
	    index = 0;
		for (i=0; i<height; i++) { // y
			vignetteYIndex = i*VIGNETTE_MASK_HEIGHT/height;
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				/*
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
*/
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
				// vignette mask is 240x320 (i.e. portrait), and it's scaled to output buffer size on the fly
				vignetteIndex = j*VIGNETTE_MASK_WIDTH/width + vignetteYIndex*VIGNETTE_MASK_WIDTH;
				vgn = vignetteData[vignetteIndex];
				if (vgn<0) vgn+=255; // unsigned byte
				
				//r = (vgn*r)>>>8;
				//g = (vgn*g)>>>8;
				//b = (vgn*b)>>>8;	
				//outputBuffer[index] = 0xff000000 | (r << 16) | (g << 8) | b;
				
				r = r >> 3;
                g = g >> 2;
                b = b >> 3;     				
				
                outputBuffer[index] = (short) ((((r*vgn) << 3) & 0xF800) | (((g*vgn) >> 3) & 0x07E0) | (((b*vgn) >>> 8) & 0x001F));
				
				index++;
			}
		}
	}

	public static void effectVignette16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height, DataInputStream vignetteStream) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		
		if (Camera5in1.mDebug)
			System.out.println("effect Vintage, buffer size " + width + "x" + height);
		
		int index = 0;
		int vignetteIndex = 0;
		int vignetteYIndex = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;
		
	    byte[] vignetteData = new byte[VIGNETTE_MASK_WIDTH*VIGNETTE_MASK_HEIGHT];
	    try {
            vignetteStream.readFully(vignetteData);
	    }
	    catch (IOException e) {
	    }
	    int vgn;
	    
	    index = 0;
		for (i=0; i<height; i++) { // y
			vignetteYIndex = i*VIGNETTE_MASK_HEIGHT/height;
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				/*
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;
*/
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
				// vignette mask is 240x320 (i.e. portrait), and it's scaled to output buffer size on the fly
				vignetteIndex = j*VIGNETTE_MASK_WIDTH/width + vignetteYIndex*VIGNETTE_MASK_WIDTH;
				vgn = vignetteData[vignetteIndex];
				if (vgn<0) vgn+=255; // unsigned byte
				
				outputBuffer[index] = 0xff000000 | (((r*vgn) << 8) & 0xff0000) | ((g*vgn) & 0xff00) | (((b*vgn) >>> 8) & 0xff);
				index++;
			}
		}
	}

	public static void effectFuturoOverlay(int[] inputBuffer, int[] outputBuffer,
			int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		
		if (Camera5in1.mDebug)
			System.out.println("effect FuturoOverlay (with mask multiply), buffer size " + width + "x" + height);
		
		int inputIndex = 0;
		int overlayIndex = 0;
		int overlayYIndex = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;
			    
	    int overlayRGB;
	    int overlayR, overlayG, overlayB;
	    
	    int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
	    overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
	    		overlayImage.getHeight());
	    
	    inputIndex = y1*width + x1;
		for (i=y1; i<y2; i++) {
			overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			for (j=x1; j<x2; j++) {
				rgb = inputBuffer[inputIndex];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
				overlayRGB = overlayData[overlayIndex];
								
				overlayR = (overlayRGB >> 16) & 0xff;
				overlayG = (overlayRGB >> 8) & 0xff;
				overlayB = overlayRGB & 0xff;
				
				//r = (r + overlayR) / 2;
				//g = (g + overlayG) / 2;
				//b = (b + overlayB) / 2;
				
				r = (overlayR*r)>>>8;
				g = (overlayG*g)>>>8;
				b = (overlayB*b)>>>8;	
				
				outputBuffer[inputIndex] = 0xff000000 | (r << 16) | (g << 8) | b;	
				
				inputIndex++;
			}
			inputIndex += width - (x2-x1);
		}
	}

	public static void effectFuturoOverlay16(short[] inputBuffer, short[] outputBuffer,
			int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		
		if (Camera5in1.mDebug)
			System.out.println("effect FuturoOverlay (with mask multiply), buffer size " + width + "x" + height);
		
		int inputIndex = 0;
		int overlayIndex = 0;
		int overlayYIndex = 0;
		int r = 0, g = 0, b = 0;
		short rgb;
		int i, j;
			    
	    int overlayRGB;
	    int overlayR, overlayG, overlayB;
	    
	    int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
	    overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
	    		overlayImage.getHeight());
	    
	    inputIndex = y1*width + x1;
		for (i=y1; i<y2; i++) {
			overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			for (j=x1; j<x2; j++) {
				rgb = inputBuffer[inputIndex];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
				overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
				overlayRGB = overlayData[overlayIndex];
								
				overlayR = (overlayRGB >> 16) & 0xff;
				overlayG = (overlayRGB >> 8) & 0xff;
				overlayB = overlayRGB & 0xff;
				
				//r = (r + overlayR) / 2;
				//g = (g + overlayG) / 2;
				//b = (b + overlayB) / 2;
				
				r = (overlayR*r)>>>8;
				g = (overlayG*g)>>>8;
				b = (overlayB*b)>>>8;	
				
				r = (r >> 3) ;
		        g = (g >> 2) ;
		        b = (b >> 3) ;
				
				//outputBuffer[inputIndex] = 0xff000000 | (r << 16) | (g << 8) | b;	
				outputBuffer[inputIndex]=(short) ((r << 11) | (g << 5) | b);
				
				inputIndex++;
			}
			inputIndex += width - (x2-x1);
		}
	}

	public static void effectFuturoOverlay16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		
		if (Camera5in1.mDebug)
			System.out.println("effect FuturoOverlay (with mask multiply), buffer size " + width + "x" + height);
		
		int inputIndex = 0;
		int overlayIndex = 0;
		int overlayYIndex = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;
			    
	    int overlayRGB;
	    int overlayR, overlayG, overlayB;
	    
	    int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
	    overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
	    		overlayImage.getHeight());
	    
	    inputIndex = y1*width + x1;
		for (i=y1; i<y2; i++) {
			overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			for (j=x1; j<x2; j++) {
				rgb = inputBuffer[inputIndex];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
				overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
				overlayRGB = overlayData[overlayIndex];
								
				overlayR = (overlayRGB >> 16) & 0xff;
				overlayG = (overlayRGB >> 8) & 0xff;
				overlayB = overlayRGB & 0xff;
				
				//r = (r + overlayR) / 2;
				//g = (g + overlayG) / 2;
				//b = (b + overlayB) / 2;
				
				r = (overlayR*r)>>>8;
				g = (overlayG*g)>>>8;
				b = (overlayB*b)>>>8;	
				
				outputBuffer[inputIndex] = 0xff000000 | (r << 16) | (g << 8) | b;	
				//outputBuffer[inputIndex]=(short) ((r << 11) | (g << 5) | b);
				
				inputIndex++;
			}
			inputIndex += width - (x2-x1);
		}
	}

	public static void effectTechnicolor1(int[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect Technicolor1, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;
		int bluegreenfilter_g, bluegreenfilter_b, bluegreennegative, bgoutput_g, bgoutput_b;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// redfilter: vec4(1.0, 0.0, 0.0, 0.0)
				// bluegreenfilter: vec4(0.0, 1.0, 0.7, 0.0)

				bluegreenfilter_g = g;
				bluegreenfilter_b = (b * 178) >> 8;

				bluegreennegative = (bluegreenfilter_g + bluegreenfilter_b) / 2;

				bgoutput_g = bluegreennegative;
				bgoutput_b = (bluegreennegative * 178) >> 8;

				outputBuffer[index] = 0xff000000 | (r << 16)
						| (bgoutput_g << 8) | bgoutput_b;

				index++;
			}
		}
	}

	public static void effectTechnicolor116(short[] inputBuffer, short[] outputBuffer,
			int width, int height) {
		// input and output buffers in RGB565 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect Technicolor1, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		short rgb;
		int i, j;
		int bluegreenfilter_g, bluegreenfilter_b, bluegreennegative, bgoutput_g, bgoutput_b;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// redfilter: vec4(1.0, 0.0, 0.0, 0.0)
				// bluegreenfilter: vec4(0.0, 1.0, 0.7, 0.0)

				bluegreenfilter_g = g;
				bluegreenfilter_b = (b * 178) >> 8;

				bluegreennegative = (bluegreenfilter_g + bluegreenfilter_b) / 2;

				bgoutput_g = bluegreennegative;
				bgoutput_b = (bluegreennegative * 178) >> 8;

				//outputBuffer[index] = 0xff000000 | (r << 16) | (bgoutput_g << 8) | bgoutput_b;
				
				r = r >> 3;
				bgoutput_g = bgoutput_g >> 2;
				bgoutput_b = bgoutput_b >> 3;
				
				outputBuffer[index]=(short) ((r << 11) | (bgoutput_g << 5) | bgoutput_b);

				index++;
			}
		}
	}

	public static void effectTechnicolor116to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect Technicolor1, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;
		int bluegreenfilter_g, bluegreenfilter_b, bluegreennegative, bgoutput_g, bgoutput_b;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// redfilter: vec4(1.0, 0.0, 0.0, 0.0)
				// bluegreenfilter: vec4(0.0, 1.0, 0.7, 0.0)

				bluegreenfilter_g = g;
				bluegreenfilter_b = (b * 178) >> 8;

				bluegreennegative = (bluegreenfilter_g + bluegreenfilter_b) / 2;

				bgoutput_g = bluegreennegative;
				bgoutput_b = (bluegreennegative * 178) >> 8;

				outputBuffer[index] = 0xff000000 | (r << 16) | (bgoutput_g << 8) | bgoutput_b;

				index++;
			}
		}
	}

	public static void effectTechnicolor2(int[] inputBuffer, int[] outputBuffer,
			int width, int height, int mixlevel) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect Technicolor2, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int or, og, ob;
		int rgb;
		int i, j;
		int bluegreennegative_rgb, redoutput_r, redoutput_g, redoutput_b, bgoutput_r, bgoutput_g, bgoutput_b;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// redfilter: vec4(1.0, 0.0, 0.0, 0.0)
				// bluegreenfilter: vec4(0.0, 1.0, 1.0, 0.0)
				// cyanfilter: vec4(0.0, 1.0, 0.5, 0.0)
				// magentafilter: vec4(1.0, 0.0, 0.25, 0.0)

				bluegreennegative_rgb = (g + b) / 2;

				redoutput_r = r;
				redoutput_g = (r + 255 > 255) ? 255 : r + 255;
				redoutput_b = (r + 127 > 255) ? 255 : r + 127;

				bgoutput_r = (bluegreennegative_rgb + 255 > 255) ? 255
						: bluegreennegative_rgb + 255;
				bgoutput_g = bluegreennegative_rgb;
				bgoutput_b = (bluegreennegative_rgb + 64 > 255) ? 255
						: bluegreennegative_rgb + 64;

				or = ((redoutput_r * bgoutput_r) >> 8) & 0xff;
				og = ((redoutput_g * bgoutput_g) >> 8) & 0xff;
				ob = ((redoutput_b * bgoutput_b) >> 8) & 0xff;

				outputBuffer[index] = 0xff000000
						| ((r + ((mixlevel * (or - r)) >> 8) & 0xff) << 16)
						| ((g + ((mixlevel * (og - g)) >> 8) & 0xff) << 8)
						| (b + ((mixlevel * (ob - b)) >> 8) & 0xff);

				index++;
			}
		}
	}

	public static void effectTechnicolor216(short[] inputBuffer, short[] outputBuffer,
			int width, int height, int mixlevel) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect Technicolor2, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int or, og, ob;
		int rgb;
		int i, j;
		int bluegreennegative_rgb, redoutput_r, redoutput_g, redoutput_b, bgoutput_r, bgoutput_g, bgoutput_b;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// redfilter: vec4(1.0, 0.0, 0.0, 0.0)
				// bluegreenfilter: vec4(0.0, 1.0, 1.0, 0.0)
				// cyanfilter: vec4(0.0, 1.0, 0.5, 0.0)
				// magentafilter: vec4(1.0, 0.0, 0.25, 0.0)

				bluegreennegative_rgb = (g + b) / 2;

				redoutput_r = r;
				redoutput_g = (r + 255 > 255) ? 255 : r + 255;
				redoutput_b = (r + 127 > 255) ? 255 : r + 127;

				bgoutput_r = (bluegreennegative_rgb + 255 > 255) ? 255
						: bluegreennegative_rgb + 255;
				bgoutput_g = bluegreennegative_rgb;
				bgoutput_b = (bluegreennegative_rgb + 64 > 255) ? 255
						: bluegreennegative_rgb + 64;

				or = ((redoutput_r * bgoutput_r) >> 8) & 0xff;
				og = ((redoutput_g * bgoutput_g) >> 8) & 0xff;
				ob = ((redoutput_b * bgoutput_b) >> 8) & 0xff;

				//outputBuffer[index] = 0xff000000
				//		| ((r + ((mixlevel * (or - r)) >> 8) & 0xff) << 16)
				//		| ((g + ((mixlevel * (og - g)) >> 8) & 0xff) << 8)
				//		| (b + ((mixlevel * (ob - b)) >> 8) & 0xff);
				//or = or >> 3;
				//og = og >> 2;
				//ob = ob >> 3;     				
				
				outputBuffer[index]=(short) ((((r + ((mixlevel * (or - r)) >> 8) & 0xff)>>3) << 11) 
						| (((g + ((mixlevel * (og - g)) >> 8) & 0xff)>>2) << 5) 
						| ((b + ((mixlevel * (ob - b)) >> 8) & 0xff)>>3));
				index++;
			}
		}
	}

	public static void effectTechnicolor216to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height, int mixlevel) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect Technicolor2, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int or, og, ob;
		int rgb;
		int i, j;
		int bluegreennegative_rgb, redoutput_r, redoutput_g, redoutput_b, bgoutput_r, bgoutput_g, bgoutput_b;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// redfilter: vec4(1.0, 0.0, 0.0, 0.0)
				// bluegreenfilter: vec4(0.0, 1.0, 1.0, 0.0)
				// cyanfilter: vec4(0.0, 1.0, 0.5, 0.0)
				// magentafilter: vec4(1.0, 0.0, 0.25, 0.0)

				bluegreennegative_rgb = (g + b) / 2;

				redoutput_r = r;
				redoutput_g = (r + 255 > 255) ? 255 : r + 255;
				redoutput_b = (r + 127 > 255) ? 255 : r + 127;

				bgoutput_r = (bluegreennegative_rgb + 255 > 255) ? 255
						: bluegreennegative_rgb + 255;
				bgoutput_g = bluegreennegative_rgb;
				bgoutput_b = (bluegreennegative_rgb + 64 > 255) ? 255
						: bluegreennegative_rgb + 64;

				or = ((redoutput_r * bgoutput_r) >> 8) & 0xff;
				og = ((redoutput_g * bgoutput_g) >> 8) & 0xff;
				ob = ((redoutput_b * bgoutput_b) >> 8) & 0xff;

				outputBuffer[index] = 0xff000000
						| ((r + ((mixlevel * (or - r)) >> 8) & 0xff) << 16)
						| ((g + ((mixlevel * (og - g)) >> 8) & 0xff) << 8)
						| (b + ((mixlevel * (ob - b)) >> 8) & 0xff);
				
				index++;
			}
		}
	}

	public static void effectSingleColor(int[] inputBuffer, int[] outputBuffer,
			int width, int height, int targetr, int targetg, int targetb,
			int mixlevel) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect SingleColor, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int or = 0, og = 0, ob = 0;
		int rgb;
		int i, j;
		int luminance;
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// luminance weights: 0.2125, 0.7154, 0.0721; Q10: 218, 733, 74
				luminance = (r * 218 + g * 733 + b * 74) >> 10;

				if (luminance < 127) {
					or = (512 * luminance * targetr) >> 16;
					og = (512 * luminance * targetg) >> 16;
					ob = (512 * luminance * targetb) >> 16;
				} else {
					or = 256 - ((512 * (256 - luminance) * (256 - targetr)) >> 16 & 0xffff);
					og = 256 - ((512 * (256 - luminance) * (256 - targetg)) >> 16 & 0xffff);
					ob = 256 - ((512 * (256 - luminance) * (256 - targetb)) >> 16 & 0xffff);
				}
		
				outputBuffer[index] = 0xff000000
						| ((r + ((mixlevel * (or - r)) >> 8) & 0xff) << 16)
						| ((g + ((mixlevel * (og - g)) >> 8) & 0xff) << 8)
						| (b + ((mixlevel * (ob - b)) >> 8) & 0xff);

				index++;
			}
		}
	}

	public static void effectSingleColor16(short[] inputBuffer, short[] outputBuffer,
			int width, int height, int targetr, int targetg, int targetb,
			int mixlevel) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect SingleColor, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int or = 0, og = 0, ob = 0;
		int rgb;
		int i, j;
		int luminance;
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
				// luminance weights: 0.2125, 0.7154, 0.0721; Q10: 218, 733, 74
				luminance = (r * 218 + g * 733 + b * 74) >> 10;

				if (luminance < 127) {
					or = (512 * luminance * targetr) >> 16;
					og = (512 * luminance * targetg) >> 16;
					ob = (512 * luminance * targetb) >> 16;
				} else {
					or = 256 - ((512 * (256 - luminance) * (256 - targetr)) >> 16 & 0xffff);
					og = 256 - ((512 * (256 - luminance) * (256 - targetg)) >> 16 & 0xffff);
					ob = 256 - ((512 * (256 - luminance) * (256 - targetb)) >> 16 & 0xffff);
				}
		
				//outputBuffer[index] = 0xff000000
				//		| ((r + ((mixlevel * (or - r)) >> 8) & 0xff) << 16)
				//		| ((g + ((mixlevel * (og - g)) >> 8) & 0xff) << 8)
				//		| (b + ((mixlevel * (ob - b)) >> 8) & 0xff);
				outputBuffer[index]=(short) ((((r + ((mixlevel * (or - r)) >> 8) & 0xff)>>3) << 11) 
						| (((g + ((mixlevel * (og - g)) >> 8) & 0xff)>>2) << 5) 
						| ((b + ((mixlevel * (ob - b)) >> 8) & 0xff)>>3));
				
				index++;
			}
		}
	}
	
	public static void effectSingleColor16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height, int targetr, int targetg, int targetb,
			int mixlevel) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		if (Camera5in1.mDebug)
			System.out.println("effect SingleColor, buffer size " + width + "x" + height);
		
		int index = 0;
		int r = 0, g = 0, b = 0;
		int or = 0, og = 0, ob = 0;
		int rgb;
		int i, j;
		int luminance;
		
		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
		        
				// luminance weights: 0.2125, 0.7154, 0.0721; Q10: 218, 733, 74
				luminance = (r * 218 + g * 733 + b * 74) >> 10;

				if (luminance < 127) {
					or = (512 * luminance * targetr) >> 16;
					og = (512 * luminance * targetg) >> 16;
					ob = (512 * luminance * targetb) >> 16;
				} else {
					or = 256 - ((512 * (256 - luminance) * (256 - targetr)) >> 16 & 0xffff);
					og = 256 - ((512 * (256 - luminance) * (256 - targetg)) >> 16 & 0xffff);
					ob = 256 - ((512 * (256 - luminance) * (256 - targetb)) >> 16 & 0xffff);
				}
		
				outputBuffer[index] = 0xff000000
						| ((r + ((mixlevel * (or - r)) >> 8) & 0xff) << 16)
						| ((g + ((mixlevel * (og - g)) >> 8) & 0xff) << 8)
						| (b + ((mixlevel * (ob - b)) >> 8) & 0xff);
				
				index++;
			}
		}
	}

	public static void effectBleach(int[] inputBuffer, int[] outputBuffer,
			int width, int height, int ratio) {
		// input and output buffers in ARGB8888 format
		// ratio parameter controls effect strength, can be set between 0 - 1024
		// source: OpenGL bleach effect 
		
		if (Camera5in1.mDebug)
			System.out.println("effect Bleach, buffer size " + width + "x" + height);
		
		int index = 0;
		int rgb;
		int r = 0, g = 0, b = 0;
		int r1 = 0, g1 = 0, b1 = 0; // branch1
		int r2 = 0, g2 = 0, b2 = 0; // branch2
		int r3 = 0, g3 = 0, b3 = 0; // mix of branch1 & branch 2
		int r4 = 0, g4 = 0, b4 = 0; // mix of r3 and input -> output
		int luminance;
		int mixamount;
		int i, j;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// luminance weights: 0.2125, 0.7154, 0.0721; Q10: 218, 733, 74
				luminance = (r * 218 + g * 733 + b * 74) >> 10;

				mixamount = (luminance - 115) * 10; // Q10 0.45
				mixamount = mixamount > 255 ? 255 : mixamount;
				mixamount = mixamount < 0  ? 0 : mixamount;
				
				r1 = (512 * luminance * r) >> 16;
				g1 = (512 * luminance * g) >> 16;
				b1 = (512 * luminance * b) >> 16;
								
				r2 = 256 - ((512 * (256 - luminance) * (256 - r)) >> 16 & 0xffff);
				g2 = 256 - ((512 * (256 - luminance) * (256 - g)) >> 16 & 0xffff);
				b2 = 256 - ((512 * (256 - luminance) * (256 - b)) >> 16 & 0xffff);
								
				r3 = (r1 * (256 - mixamount) + r2 * mixamount) >> 8;
				g3 = (g1 * (256 - mixamount) + g2 * mixamount) >> 8;
				b3 = (b1 * (256 - mixamount) + b2 * mixamount) >> 8;
				
				r4 = (r * (256 - ratio) + r3 * ratio) >> 8;
				g4 = (g * (256 - ratio) + g3 * ratio) >> 8;
				b4 = (b * (256 - ratio) + b3 * ratio) >> 8;
				
				// clamping just to be on the safe side...
				r4 = r4 > 255 ? 255 : r4;
				r4 = r4 < 0  ? 0 : r4;
				g4 = g4 > 255 ? 255 : g4;
				g4 = g4 < 0  ? 0 : g4;
				b4 = b4 > 255 ? 255 : b4;
				b4 = b4 < 0  ? 0 : b4;

				outputBuffer[index] = 0xff000000 | (r4 << 16) | (g4 << 8) | b4;

				index++;
			}
		}
	}
	
	public static void effectBleach16(short[] inputBuffer, short[] outputBuffer,
			int width, int height, int ratio) {
		// input and output buffers in ARGB8888 format
		// ratio parameter controls effect strength, can be set between 0 - 1024
		// source: OpenGL bleach effect 
		
		if (Camera5in1.mDebug)
			System.out.println("effect Bleach, buffer size " + width + "x" + height);
		
		int index = 0;
		int rgb;
		int r = 0, g = 0, b = 0;
		int r1 = 0, g1 = 0, b1 = 0; // branch1
		int r2 = 0, g2 = 0, b2 = 0; // branch2
		int r3 = 0, g3 = 0, b3 = 0; // mix of branch1 & branch 2
		int r4 = 0, g4 = 0, b4 = 0; // mix of r3 and input -> output
		int luminance;
		int mixamount;
		int i, j;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// luminance weights: 0.2125, 0.7154, 0.0721; Q10: 218, 733, 74
				luminance = (r * 218 + g * 733 + b * 74) >> 10;

				mixamount = (luminance - 115) * 10; // Q10 0.45
				mixamount = mixamount > 255 ? 255 : mixamount;
				mixamount = mixamount < 0  ? 0 : mixamount;
				
				r1 = (512 * luminance * r) >> 16;
				g1 = (512 * luminance * g) >> 16;
				b1 = (512 * luminance * b) >> 16;
								
				r2 = 256 - ((512 * (256 - luminance) * (256 - r)) >> 16 & 0xffff);
				g2 = 256 - ((512 * (256 - luminance) * (256 - g)) >> 16 & 0xffff);
				b2 = 256 - ((512 * (256 - luminance) * (256 - b)) >> 16 & 0xffff);
								
				r3 = (r1 * (256 - mixamount) + r2 * mixamount) >> 8;
				g3 = (g1 * (256 - mixamount) + g2 * mixamount) >> 8;
				b3 = (b1 * (256 - mixamount) + b2 * mixamount) >> 8;
				
				r4 = (r * (256 - ratio) + r3 * ratio) >> 8;
				g4 = (g * (256 - ratio) + g3 * ratio) >> 8;
				b4 = (b * (256 - ratio) + b3 * ratio) >> 8;
				
				// clamping just to be on the safe side...
				r4 = r4 > 255 ? 255 : r4;
				r4 = r4 < 0  ? 0 : r4;
				g4 = g4 > 255 ? 255 : g4;
				g4 = g4 < 0  ? 0 : g4;
				b4 = b4 > 255 ? 255 : b4;
				b4 = b4 < 0  ? 0 : b4;

				//outputBuffer[index] = 0xff000000 | (r4 << 16) | (g4 << 8) | b4;
				r4 = r4 >> 3;
                g4 = g4 >> 2;
                b4 = b4 >> 3;     				
				
				outputBuffer[index]=(short) ((r4 << 11) | (g4 << 5) | b4);

				index++;
			}
		}
	}
	
	public static void effectBleach16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height, int ratio) {
		// input and output buffers in ARGB8888 format
		// ratio parameter controls effect strength, can be set between 0 - 1024
		// source: OpenGL bleach effect 
		
		if (Camera5in1.mDebug)
			System.out.println("effect Bleach, buffer size " + width + "x" + height);
		
		int index = 0;
		int rgb;
		int r = 0, g = 0, b = 0;
		int r1 = 0, g1 = 0, b1 = 0; // branch1
		int r2 = 0, g2 = 0, b2 = 0; // branch2
		int r3 = 0, g3 = 0, b3 = 0; // mix of branch1 & branch 2
		int r4 = 0, g4 = 0, b4 = 0; // mix of r3 and input -> output
		int luminance;
		int mixamount;
		int i, j;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				
				//r = (rgb >> 16) & 0xff;
				//g = (rgb >> 8) & 0xff;
				//b = rgb & 0xff;
				
				r = ((rgb >> 11) & 0x1f);
		        g = ((rgb >> 5) & 0x3f);
		        b = (rgb & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

				// luminance weights: 0.2125, 0.7154, 0.0721; Q10: 218, 733, 74
				luminance = (r * 218 + g * 733 + b * 74) >> 10;

				mixamount = (luminance - 115) * 10; // Q10 0.45
				mixamount = mixamount > 255 ? 255 : mixamount;
				mixamount = mixamount < 0  ? 0 : mixamount;
				
				r1 = (512 * luminance * r) >> 16;
				g1 = (512 * luminance * g) >> 16;
				b1 = (512 * luminance * b) >> 16;
								
				r2 = 256 - ((512 * (256 - luminance) * (256 - r)) >> 16 & 0xffff);
				g2 = 256 - ((512 * (256 - luminance) * (256 - g)) >> 16 & 0xffff);
				b2 = 256 - ((512 * (256 - luminance) * (256 - b)) >> 16 & 0xffff);
								
				r3 = (r1 * (256 - mixamount) + r2 * mixamount) >> 8;
				g3 = (g1 * (256 - mixamount) + g2 * mixamount) >> 8;
				b3 = (b1 * (256 - mixamount) + b2 * mixamount) >> 8;
				
				r4 = (r * (256 - ratio) + r3 * ratio) >> 8;
				g4 = (g * (256 - ratio) + g3 * ratio) >> 8;
				b4 = (b * (256 - ratio) + b3 * ratio) >> 8;
				
				// clamping just to be on the safe side...
				r4 = r4 > 255 ? 255 : r4;
				r4 = r4 < 0  ? 0 : r4;
				g4 = g4 > 255 ? 255 : g4;
				g4 = g4 < 0  ? 0 : g4;
				b4 = b4 > 255 ? 255 : b4;
				b4 = b4 < 0  ? 0 : b4;

				outputBuffer[index] = 0xff000000 | (r4 << 16) | (g4 << 8) | b4;
				index++;
			}
		}
	}
	
	public static void effectFlatColors(int[] inputBuffer, int[] outputBuffer,
			int width, int height) {
	
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect FlatColors, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) i; // flat curve, no changes
			curveG[i] = (byte) ((Math.max(0,
					Math.min(255, i + (i - 128) * 9 / 16)) + i) / 2); // saturated
																		// at
																		// top
																		// and
																		// bottom
			curveB[i] = (byte) ((Math.max(0,
					Math.min(255, i + (i - 128) * -7 / 16)) + i) / 2); // low
																		// dynamic
																		// range
		}
		applyColorCurve(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}

	public static void effectFlatColors16(short[] inputBuffer, short[] outputBuffer,
			int width, int height) {
	
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect FlatColors, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) i; // flat curve, no changes
			curveG[i] = (byte) ((Math.max(0,
					Math.min(255, i + (i - 128) * 9 / 16)) + i) / 2); // saturated
																		// at
																		// top
																		// and
																		// bottom
			curveB[i] = (byte) ((Math.max(0,
					Math.min(255, i + (i - 128) * -7 / 16)) + i) / 2); // low
																		// dynamic
																		// range
		}
		applyColorCurve16(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}

	public static void effectFlatColors16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height) {
	
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect FlatColors, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) i; // flat curve, no changes
			curveG[i] = (byte) ((Math.max(0,
					Math.min(255, i + (i - 128) * 9 / 16)) + i) / 2); // saturated
																		// at
																		// top
																		// and
																		// bottom
			curveB[i] = (byte) ((Math.max(0,
					Math.min(255, i + (i - 128) * -7 / 16)) + i) / 2); // low
																		// dynamic
																		// range
		}
		applyColorCurve16to32(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}

	public static void effectWarm(int[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// Create simple color curves
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect Warm, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) Math.min(255, Math.max(0, i + 40)); // clamp
			curveG[i] = (byte) Math.min(255, Math.max(0, i + 40)); // clamp
			curveB[i] = (byte) i;
		}
		applyColorCurve(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}

	public static void effectWarm16(short[] inputBuffer, short[] outputBuffer,
			int width, int height) {
		// Create simple color curves
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect Warm, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) Math.min(255, Math.max(0, i + 40)); // clamp
			curveG[i] = (byte) Math.min(255, Math.max(0, i + 40)); // clamp
			curveB[i] = (byte) i;
		}
		applyColorCurve16(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}

	public static void effectWarm16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// Create simple color curves
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect Warm, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) Math.min(255, Math.max(0, i + 40)); // clamp
			curveG[i] = (byte) Math.min(255, Math.max(0, i + 40)); // clamp
			curveB[i] = (byte) i;
		}
		applyColorCurve16to32(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}

	public static void effectCold(int[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// Create simple color curves
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect Cold, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) i;
			curveG[i] = (byte) i;
			curveB[i] = (byte) Math.min(255, Math.max(0, i + 45)); // clamp
		}
		applyColorCurve(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}
	
	public static void effectCold16(short[] inputBuffer, short[] outputBuffer,
			int width, int height) {
		// Create simple color curves
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect Cold, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) i;
			curveG[i] = (byte) i;
			curveB[i] = (byte) Math.min(255, Math.max(0, i + 45)); // clamp
		}
		applyColorCurve16(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}
	public static void effectCold16to32(short[] inputBuffer, int[] outputBuffer,
			int width, int height) {
		// Create simple color curves
		byte[] curveR = new byte[256];
		byte[] curveG = new byte[256];
		byte[] curveB = new byte[256];

		if (Camera5in1.mDebug)
			System.out.println("effect Cold, buffer size " + width + "x" + height);
		
		for (int i=0; i<256; i++) {
			curveR[i] = (byte) i;
			curveG[i] = (byte) i;
			curveB[i] = (byte) Math.min(255, Math.max(0, i + 45)); // clamp
		}
		applyColorCurve16to32(inputBuffer, outputBuffer, width, height, curveR,
				curveG, curveB);
	}
	
	private static void applyColorCurve(int[] inputBuffer, int[] outputBuffer,
			int width, int height, byte[] curveR, byte[] curveG, byte[] curveB) {
		// input and output buffers in ARGB8888 format
		// this effect can use a single input/output buffer and override input
		// pixels with output
		System.out.println("applying effectColorCurve");
		int index = 0;
		int r = 0, g = 0, b = 0;
		int rgb;
		int i, j;

		for (i=0; i<height; i++) { // y
			for (j=0; j<width; j++) { // x
				rgb = inputBuffer[index];
				r = (rgb >> 16) & 0xff;
				g = (rgb >> 8) & 0xff;
				b = rgb & 0xff;

				// lookup new color values
				r = ((int) curveR[r]) & 0xff;
				g = ((int) curveG[g]) & 0xff;
				b = ((int) curveB[b]) & 0xff;

				outputBuffer[index] = 0xff000000 | (r << 16) | (g << 8) | b;
				index++;
			}
		}
	}
	
	public static void effectFuturoOverlayWithAlpha(int[] inputBuffer, int[] outputBuffer,
			   int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
			  // input and output buffers in ARGB8888 format
			  // this effect can use a single input/output buffer and override input
			  // pixels with output
			  
			  if (Camera5in1.mDebug)
			   System.out.println("effect FuturoOverlay (with alpha channel), buffer size " + width + "x" + height);
			  
			  int inputIndex = 0;
			  int overlayIndex = 0;
			  int overlayYIndex = 0;
			  int r = 0, g = 0, b = 0;
			  int rgb;
			  int i, j;
			       
			     int overlayARGB;
			     int overlayA, overlayR, overlayG, overlayB;
			     
			     int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
			     overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
			       overlayImage.getHeight());
			     
			     inputIndex = y1*width + x1;
			  for (i=y1; i<y2; i++) {
			   overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			   for (j=x1; j<x2; j++) {
			    rgb = inputBuffer[inputIndex];
			    //r = (rgb >> 16) & 0xff;
			    //g = (rgb >> 8) & 0xff;
			    //b = rgb & 0xff;

			    overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
			    overlayARGB = overlayData[overlayIndex];
			        
			    overlayA = (overlayARGB >> 24) & 0xff;
			    //overlayR = (overlayARGB >> 16) & 0xff;
			    //overlayG = (overlayARGB >> 8) & 0xff;
			    //overlayB = overlayARGB & 0xff;
			    
			    if (overlayA == 0) 
			     outputBuffer[inputIndex] = rgb; // 0xff000000 | (r << 16) | (g << 8) | b; 
			    else
			     outputBuffer[inputIndex] = overlayARGB;
			    inputIndex++;
			   }
			   inputIndex += width - (x2-x1);
			  }
			 }
	
	public static void effectFuturoOverlayWithAlpha16(short[] inputBuffer, short[] outputBuffer,
			   int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
			  // input and output buffers in ARGB8888 format
			  // this effect can use a single input/output buffer and override input
			  // pixels with output
			  
			  if (Camera5in1.mDebug)
			   System.out.println("effect FuturoOverlay (with alpha channel), buffer size " + width + "x" + height);
			  
			  int inputIndex = 0;
			  int overlayIndex = 0;
			  int overlayYIndex = 0;
			  int r = 0, g = 0, b = 0;
			  int rgb;
			  int i, j;
			       
			     int overlayARGB;
			     int overlayA, overlayR, overlayG, overlayB;
			     
			     int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
			     overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
			       overlayImage.getHeight());
			     
			     inputIndex = y1*width + x1;
			  for (i=y1; i<y2; i++) {
			   overlayYIndex = (i-y1)*overlayImage.getHeight()/(y2-y1);
			   for (j=x1; j<x2; j++) {
			    rgb = inputBuffer[inputIndex];
			    //r = (rgb >> 16) & 0xff;
			    //g = (rgb >> 8) & 0xff;
			    //b = rgb & 0xff;

			    overlayIndex = (j-x1)*overlayImage.getWidth()/(x2-x1) + overlayYIndex*overlayImage.getWidth();
			    overlayARGB = overlayData[overlayIndex];
			        
			    overlayA = (overlayARGB >> 24) & 0xff;
			    //overlayR = (overlayARGB >> 16) & 0xff;
			    //overlayG = (overlayARGB >> 8) & 0xff;
			    //overlayB = overlayARGB & 0xff;
			    
			    if (overlayA == 0) 
			    {
			    	outputBuffer[inputIndex] = (short) rgb; // 0xff000000 | (r << 16) | (g << 8) | b;
			    }
			    else
			    {
			    	outputBuffer[inputIndex] =(short) (((((overlayARGB>> 16) & 0x0FF)>>3)<<11) | 
	                		((((overlayARGB>> 8) & 0x0FF)>>2) << 5) | 
	                		((overlayARGB & 0x0FF)>>3));
			    }
			    inputIndex++;
			   }
			   inputIndex += width - (x2-x1);
			  }
			 }
	
	public static void effectFuturoOverlayWithAlpha16to32(short[] inputBuffer, int[] outputBuffer,
			   int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
			  // input and output buffers in ARGB8888 format
			  // this effect can use a single input/output buffer and override input
			  // pixels with output
			  
			  if (Camera5in1.mDebug)
			   System.out.println("effect FuturoOverlay (with alpha channel), buffer size " + width + "x" + height);
			  
			  int inputIndex = 0;
			  int overlayIndex = 0;
			  int overlayYIndex = 0;
			  int r = 0, g = 0, b = 0;
			  short rgb_input;
			  int rgb_output;
			  int i, j;
			       
			     int overlayARGB;
			     int overlayA, overlayR, overlayG, overlayB;
			     
			     int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
			     overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
			       overlayImage.getHeight());
			     
			     inputIndex = y1*width + x1;
			  for (i=y1; i<y2; i++) {
			   overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			   for (j=x1; j<x2; j++) {
				   rgb_input = inputBuffer[inputIndex];
			    //r = (rgb >> 16) & 0xff;
			    //g = (rgb >> 8) & 0xff;
			    //b = rgb & 0xff;
			    r = ((rgb_input >> 11) & 0x1f);
		        g = ((rgb_input >> 5) & 0x3f);
		        b = (rgb_input & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
			    rgb_output = 0xff000000 | (r << 16) | (g << 8) | b;

			    overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
			    overlayARGB = overlayData[overlayIndex];
			        
			    overlayA = (overlayARGB >> 24) & 0xff;
			    //overlayR = (overlayARGB >> 16) & 0xff;
			    //overlayG = (overlayARGB >> 8) & 0xff;
			    //overlayB = overlayARGB & 0xff;
			    
			    if (overlayA == 0) 
			     outputBuffer[inputIndex] =  rgb_output; // 0xff000000 | (r << 16) | (g << 8) | b; 
			    else
			     outputBuffer[inputIndex] = (short) overlayARGB;
			    inputIndex++;
			   }
			   inputIndex += width - (x2-x1);
			  }
			 }
	
	public static void effectFuturoOverlayWhiteIsAlpha(int[] inputBuffer, int[] outputBuffer,
			   int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
			  // input and output buffers in ARGB8888 format
			  // this effect can use a single input/output buffer and override input
			  // pixels with output
			  
			  if (Camera5in1.mDebug)
			   System.out.println("effect FuturoOverlay (white is alpha), buffer size " + width + "x" + height);
			  
			  int inputIndex = 0;
			  int overlayIndex = 0;
			  int overlayYIndex = 0;
			  int r = 0, g = 0, b = 0;
			  int rgb;
			  int i, j;
			       
			     int overlayARGB;
			     int overlayA, overlayR, overlayG, overlayB;
			     
			     int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
			     overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
			       overlayImage.getHeight());
			     
			     inputIndex = y1*width + x1;
			  for (i=y1; i<y2; i++) {
			   overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			   for (j=x1; j<x2; j++) {
			    rgb = inputBuffer[inputIndex];
			    //r = (rgb >> 16) & 0xff;
			    //g = (rgb >> 8) & 0xff;
			    //b = rgb & 0xff;

			    overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
			    overlayARGB = overlayData[overlayIndex];
			        
//			    overlayA = (overlayARGB >> 24) & 0xff;
			    overlayR = (overlayARGB >> 16) & 0xff;
			    overlayG = (overlayARGB >> 8) & 0xff;
			    overlayB = overlayARGB & 0xff;
			    
			    if ((overlayR > 200) & (overlayB > 200) & (overlayG > 200)) // Ugly hack: all colors close to white are considered transparent
			     outputBuffer[inputIndex] = rgb;
			    else
			     outputBuffer[inputIndex] = overlayARGB;
			    inputIndex++;
			   }
			   inputIndex += width - (x2-x1);
			  }
			 }
	
	public static void effectFuturoOverlayWhiteIsAlpha16(short[] inputBuffer, short[] outputBuffer,
			   int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
			  // input and output buffers in ARGB8888 format
			  // this effect can use a single input/output buffer and override input
			  // pixels with output
			  
			  if (Camera5in1.mDebug)
			   System.out.println("effect FuturoOverlay (white is alpha), buffer size " + width + "x" + height);
			  
			  int inputIndex = 0;
			  int overlayIndex = 0;
			  int overlayYIndex = 0;
			  int r = 0, g = 0, b = 0;
			  short rgb;
			  int i, j;
			       
			     int overlayARGB;
			     int overlayA, overlayR, overlayG, overlayB;
			     
			     int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
			     overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
			       overlayImage.getHeight());
			     
			     inputIndex = y1*width + x1;
			  for (i=y1; i<y2; i++) {
			   overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			   for (j=x1; j<x2; j++) {
			    rgb = inputBuffer[inputIndex];
			    //r = (rgb >> 16) & 0xff;
			    //g = (rgb >> 8) & 0xff;
			    //b = rgb & 0xff;

			    overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
			    overlayARGB = overlayData[overlayIndex];
			        
//			    overlayA = (overlayARGB >> 24) & 0xff;
			    overlayR = (overlayARGB >> 16) & 0xff;
			    overlayG = (overlayARGB >> 8) & 0xff;
			    overlayB = overlayARGB & 0xff;
			    
			    if ((overlayR > 200) & (overlayB > 200) & (overlayG > 200)) // Ugly hack: all colors close to white are considered transparent
			    {
			    	outputBuffer[inputIndex] = (short) rgb;
			    }
			    else
			    {
			    	outputBuffer[inputIndex]    =(short) ((((overlayR)>>3)<<11) | 
	                		(((overlayG)>>2) << 5) | 
	                		((overlayB)>>3));
			    }
			    inputIndex++;
			   }
			   inputIndex += width - (x2-x1);
			  }
			 }
	
	public static void effectFuturoOverlayWhiteIsAlpha16to32(short[] inputBuffer, int[] outputBuffer,
			   int width, int height, int x1, int y1, int x2, int y2, Image overlayImage) {
			  // input and output buffers in ARGB8888 format
			  // this effect can use a single input/output buffer and override input
			  // pixels with output
			  
			  if (Camera5in1.mDebug)
			   System.out.println("effect FuturoOverlay (white is alpha), buffer size " + width + "x" + height);
			  
			  int inputIndex = 0;
			  int overlayIndex = 0;
			  int overlayYIndex = 0;
			  int r = 0, g = 0, b = 0;
			  short rgb_input;
			  int rgb_output;
			  int i, j;
			       
			     int overlayARGB;
			     int overlayA, overlayR, overlayG, overlayB;
			     
			     int[] overlayData = new int[overlayImage.getHeight() * overlayImage.getWidth()];
			     overlayImage.getRGB(overlayData, 0, overlayImage.getWidth(), 0, 0, overlayImage.getWidth(),
			       overlayImage.getHeight());
			     
			     inputIndex = y1*width + x1;
			  for (i=y1; i<y2; i++) {
			   overlayYIndex = (i-y1)*FUTURO_OVERLAY_HEIGHT/(y2-y1);
			   for (j=x1; j<x2; j++) {
				   rgb_input = inputBuffer[inputIndex];
			    //r = (rgb >> 16) & 0xff;
			    //g = (rgb >> 8) & 0xff;
			    //b = rgb & 0xff;
			    r = ((rgb_input >> 11) & 0x1f);
		        g = ((rgb_input >> 5) & 0x3f);
		        b = (rgb_input & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);
			    rgb_output = 0xff000000 | (r << 16) | (g << 8) | b;

			    overlayIndex = (j-x1)*FUTURO_OVERLAY_WIDTH/(x2-x1) + overlayYIndex*FUTURO_OVERLAY_WIDTH;
			    overlayARGB = overlayData[overlayIndex];
			        
//			    overlayA = (overlayARGB >> 24) & 0xff;
			    overlayR = (overlayARGB >> 16) & 0xff;
			    overlayG = (overlayARGB >> 8) & 0xff;
			    overlayB = overlayARGB & 0xff;
			    
			    if ((overlayR > 200) & (overlayB > 200) & (overlayG > 200)) // Ugly hack: all colors close to white are considered transparent
			     outputBuffer[inputIndex] = (int) rgb_output;
			    else
			     outputBuffer[inputIndex] = (int) overlayARGB;
			    inputIndex++;
			   }
			   inputIndex += width - (x2-x1);
			  }
			 }
	
	public static void effectWarpWithTable(int[] inputBuffer, int[] outputBuffer,
			int width, int height, DataInputStream warpStreamX, DataInputStream warpStreamY) {
		// input and output buffers in ARGB8888 format
		// this effect requires separate input/output buffers
		
		int WARPTABLE_WIDTH = 120;
		int WARPTABLE_HEIGHT = 160;
		
		if (Camera5in1.mDebug)
			System.out.println("effect Warp, buffer size " + width + "x" + height);
		
		int index = 0;
		int warpIndex = 0;
		int warpYIndex = 0;
		
		int argb;
		int i, j;
		int warpOffsetX = 0;
		int warpOffsetY = 0;
		
	    byte[] warpDataX = new byte[WARPTABLE_WIDTH*WARPTABLE_HEIGHT];
	    byte[] warpDataY = new byte[WARPTABLE_WIDTH*WARPTABLE_HEIGHT];
	    
	    
	    try {
            warpStreamX.readFully(warpDataX);
            warpStreamY.readFully(warpDataY);
	    }
	    catch (IOException e) {
	    }
	    
	
	    
	    
	    // WARP 9, engage!
	    index = 0;
		for (i=0; i<height; i++) { // y
			warpYIndex = (i*WARPTABLE_HEIGHT/height)*WARPTABLE_WIDTH;
			for (j=0; j<width; j++) { // x
			
				warpIndex = j*WARPTABLE_WIDTH/width + warpYIndex;
				warpOffsetX = warpDataX[warpIndex]*width/WARPTABLE_WIDTH;
				warpOffsetY = warpDataY[warpIndex]*height/WARPTABLE_HEIGHT;

				//System.out.println(i + " " + j + ", offset: " + warpOffsetX + " " + warpOffsetY);
				
				//if (warpfactor<0) warpfactor+=255; // unsigned byte
				
				// read input pixel from warped location and write to output
				//argb = inputBuffer[index+warpOffsetY*width+warpOffsetX];
				argb = getPixel(inputBuffer, j+warpOffsetX, i+warpOffsetY, width, height); // cannot do direct pixel access because we might be reading outside the buffer due to large warp offsets
				outputBuffer[index] = argb;
				index++;
			}
			
		}
	}
	
	private static int getPixel(int[] buffer, int x, int y, int width, int height) {
		//System.out.println("Trying to access pixel (" + x + "," + y + ")");
		if (x < 0 || x >= width || y < 0 || y >= height) { // clamp
			if (x<0) x = 0;
			else if (x >= width) x = width - 1;
			if (y<0) y = 0;
			else if (y >= height) y = height - 1;
		}
		return buffer[y*width + x];
	}
	
}
