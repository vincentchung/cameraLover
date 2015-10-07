
package com.pictelligent.s40.camera5in1.effects;

public class Scaling {

    public Scaling() {
    }

    public static void scaleImage(int[] rgbImageData, int srcWidth, int srcHeight, int outWidth,
            int outHeight) {

        try {

            int newImageWidth = outWidth;
            int newImageHeight = outHeight;
            int tempScaleRatioWidth = 0;
            int tempScaleRatioHeight = 0;

            tempScaleRatioWidth = ((srcWidth << 16) / newImageWidth);
            tempScaleRatioHeight = ((srcHeight << 16) / newImageHeight);

            int i = 0;

            // If outWidth*outHeight > srcWidth*srcHeight, here will throws an
            // OutOfIndexException.
            for (int y = 0; y < newImageHeight; y++) {
                for (int x = 0; x < newImageWidth; x++) {
                    rgbImageData[i++] = rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16))
                            + ((x * tempScaleRatioWidth) >> 16)];
                }
            }
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void scaleImage(int[] rgbImageData, int srcWidth,
            int srcHeight, int[] rgbOutImageData, int outWidth, int outHeight) {

        try {

            int newImageWidth = outWidth;
            int newImageHeight = outHeight;

            int tempScaleRatioWidth = ((srcWidth << 16) / newImageWidth);
            int tempScaleRatioHeight = ((srcHeight << 16) / newImageHeight);

            int i = 0;

            for (int y = 0; y < newImageHeight; y++) {
                for (int x = 0; x < newImageWidth; x++) {
                    rgbOutImageData[i++] = rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16))
                            + ((x * tempScaleRatioWidth) >> 16)];
                }
            }
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void cropImage(int[] rbgDataIn, int[] rbgDataOut, int srcWidth, int srcHeight,
            int x, int y, int destWidth, int destHeight) {
        for (int i = y; i < (destHeight + y); i++) {
            for (int j = x; j < (destWidth + x); j++) {
                rbgDataOut[(i - y) * destWidth + (j - x)] = rbgDataIn[i * srcWidth + j];
            }
        }
    }

    //support 16bits color format RGB565
    

    public static void scaleImage16(short[] rgbImageData, int srcWidth, int srcHeight, int outWidth,
            int outHeight) {

        try {

            int newImageWidth = outWidth;
            int newImageHeight = outHeight;
            int tempScaleRatioWidth = 0;
            int tempScaleRatioHeight = 0;

            tempScaleRatioWidth = ((srcWidth << 16) / newImageWidth);
            tempScaleRatioHeight = ((srcHeight << 16) / newImageHeight);

            int i = 0;

            // If outWidth*outHeight > srcWidth*srcHeight, here will throws an
            // OutOfIndexException.
            for (int y = 0; y < newImageHeight; y++) {
                for (int x = 0; x < newImageWidth; x++) {
                    rgbImageData[i++] = rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16))
                            + ((x * tempScaleRatioWidth) >> 16)];
                }
            }
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void scaleImage16(short[] rgbImageData, int srcWidth,
            int srcHeight, short[] rgbOutImageData, int outWidth, int outHeight) {

        try {

            int newImageWidth = outWidth;
            int newImageHeight = outHeight;

            int tempScaleRatioWidth = ((srcWidth << 16) / newImageWidth);
            int tempScaleRatioHeight = ((srcHeight << 16) / newImageHeight);

            int i = 0;

            for (int y = 0; y < newImageHeight; y++) {
                for (int x = 0; x < newImageWidth; x++) {
                    rgbOutImageData[i++] = rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16))
                            + ((x * tempScaleRatioWidth) >> 16)];
                }
            }
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void scaleImage16to32(short[] rgbImageData, int srcWidth,
            int srcHeight, int[] rgbOutImageData, int outWidth, int outHeight) {

        try {

            int newImageWidth = outWidth;
            int newImageHeight = outHeight;

            int tempScaleRatioWidth = ((srcWidth << 16) / newImageWidth);
            int tempScaleRatioHeight = ((srcHeight << 16) / newImageHeight);

            int i = 0;

            for (int y = 0; y < newImageHeight; y++) {
                for (int x = 0; x < newImageWidth; x++) {
                	
                	rgbOutImageData[i++] = EffectAlgorithms.RGB16to32bit(rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16))
                           + ((x * tempScaleRatioWidth) >> 16)]);
                	
                }
            }
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void scaleImage32to16(int[] rgbImageData, int srcWidth,
            int srcHeight, short[] rgbOutImageData, int outWidth, int outHeight) {

        try {

            int newImageWidth = outWidth;
            int newImageHeight = outHeight;

            int tempScaleRatioWidth = ((srcWidth << 16) / newImageWidth);
            int tempScaleRatioHeight = ((srcHeight << 16) / newImageHeight);

            int i = 0;

            for (int y = 0; y < newImageHeight; y++) {
                for (int x = 0; x < newImageWidth; x++) {
                	
                	rgbOutImageData[i++] = EffectAlgorithms.RGB32to16bit(rgbImageData[(srcWidth * ((y * tempScaleRatioHeight) >> 16))
                	                                            + ((x * tempScaleRatioWidth) >> 16)]);
                	
                }
            }
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void cropImage16(short[] rbgDataIn, short[] rbgDataOut, int srcWidth, int srcHeight,
            int x, int y, int destWidth, int destHeight) {
        for (int i = y; i < (destHeight + y); i++) {
            for (int j = x; j < (destWidth + x); j++) {
                rbgDataOut[(i - y) * destWidth + (j - x)] = rbgDataIn[i * srcWidth + j];
            }
        }
    }

    public static void fillimage16(short[] dstData, int dstWidth,int dstHeight, 
    		short[] fillData,int fillX,int fillY, int fillWidth, int fillHeight) 
    {
    	int scanH=fillY+fillHeight;
    	int scanX=fillX+fillWidth;
    	int fill_index=0;
    	
        for(int j=fillY;j<scanH;j++)
        {
        	for(int i=fillX;i<scanX;i++)
        	{
        		dstData[j*dstWidth+i]=fillData[fill_index];
        		fill_index++;
        	}
        }
    }
}
