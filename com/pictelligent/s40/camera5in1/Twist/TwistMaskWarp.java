
package com.pictelligent.s40.camera5in1.Twist;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.pictelligent.s40.camera5in1.Camera5in1;

public class TwistMaskWarp {

    protected int interpolation = 1;

    int width, height;
    float[] preCacheDataX = null;
    float[] preCacheDataY = null;
    DataInputStream cacheStream = null;
    String cachefilename = "";
    private boolean precache = false;
    protected boolean enablecache = false;

    private int mProgress = 0;

    public TwistMaskWarp() {
    }

    public void setPrecache(boolean c)
    {
        precache = c;
    }

    public void setcache(boolean c)
    {
        enablecache = c;
    }

    public void setCachefile(String file)
    {
        cachefilename = file;
    }

    public void setSize(int w, int h,boolean VF)
    {
        width = w;
        height = h;
        if (VF)
        {
            mCacheW = mCacheVFW;
            mCacheH = mCacheVFH;
        }else
        {
        	mCacheW = mCacheFullW;
            mCacheH = mCacheFullH;
        }
    }

    // cache size is 125x174
    private final static int mCacheFullW = 125;
    private final static int mCacheFullH = 174;
    private final static int mCacheVFW = 50;//125;
    private final static int mCacheVFH = 70;//174;
    private int mCacheW = 50;//125;
    private int mCacheH = 70;//174;
    int tempScaleRatioWidth = 0;
    int tempScaleRatioHeight = 0;

    private int cacheMaping(int x, int y)
    {
        int mapX = 0;
        int mapY = 0;
        // int tempScaleRatioWidth=0;
        // int tempScaleRatioHeight=0;
        // calculations and bit shift operations to optimize the for loop
        if (tempScaleRatioWidth == 0)
        {
            tempScaleRatioWidth = ((mCacheW << 16) / width);
        }
        if (tempScaleRatioHeight == 0)
        {
            tempScaleRatioHeight = ((mCacheH << 16) / height);
        }

        mapX = ((x * tempScaleRatioWidth) >> 16);
        mapY = ((y * tempScaleRatioHeight) >> 16);
        // System.out.println("x1:"+mapX+",y1:"+mapY+",x2:"+x+",y2:"+y);
        return (mapY * mCacheW + mapX);
    }

    float x_ratio = 0.0f;
    float y_ratio = 0.0f;

    private void cacheMapingInter(int x, int y, float out[])
    {
        int mapX = 0;
        int mapY = 0;
        float A, B, C, D;
        int xt, yt, index;
        float x_diff, y_diff;

        if (x_ratio == 0.0f)
            x_ratio = (float) (mCacheW - 1) / width;
        if (y_ratio == 0.0f)
            y_ratio = (float) (mCacheH - 1) / height;

        // Find the color values of the 4 pixels around the desired point to
        // interpolate

        xt = (int) (x_ratio * x);
        yt = (int) (y_ratio * y);
        x_diff = (x_ratio * x) - xt;
        y_diff = (y_ratio * y) - yt;
        index = yt * mCacheW + xt;

        A = preCacheData[index * 2];
        B = preCacheData[(index + 1) * 2];
        C = preCacheData[(index + mCacheW) * 2];
        D = preCacheData[(index + mCacheW + 1) * 2];
        out[0] = (
                A * (1 - x_diff) * (1 - y_diff) + B * (x_diff) * (1 - y_diff) +
                        C * (y_diff) * (1 - x_diff) + D * (x_diff * y_diff)
                );
        A = preCacheData[index * 2 + 1];
        B = preCacheData[(index + 1) * 2 + 1];
        C = preCacheData[(index + mCacheW) * 2 + 1];
        D = preCacheData[(index + mCacheW + 1) * 2 + 1];
        out[1] = (
                A * (1 - x_diff) * (1 - y_diff) + B * (x_diff) * (1 - y_diff) +
                        C * (y_diff) * (1 - x_diff) + D * (x_diff * y_diff)
                );

        // int tempScaleRatioWidth=0;
        // int tempScaleRatioHeight=0;
        // calculations and bit shift operations to optimize the for loop
        /*
         * if(tempScaleRatioWidth==0) { tempScaleRatioWidth = ((mCacheW << 16) /
         * width); } if(tempScaleRatioHeight==0) { tempScaleRatioHeight =
         * ((mCacheH<< 16) / height); } mapX=((x*tempScaleRatioWidth)>>16);
         * mapY=((y*tempScaleRatioHeight)>>16);
         * //System.out.println("x1:"+mapX+",y1:"+mapY+",x2:"+x+",y2:"+y);
         * if(mapX==0) { int s=mapY*mCacheW; out[0]=preCacheData[(s+mapX)*2];
         * out[1]=preCacheData[(s+mapX)*2+1]; }else { int s=mapY*mCacheW; float
         * a1=preCacheData[(s+(mapX))*2]; float b1=preCacheData[(s+(mapX))*2+1];
         * float a2=preCacheData[(s+(mapX-1))*2]; float
         * b2=preCacheData[(s+(mapX-1))*2+1]; out[0]=ImageMath.lerp(
         * interpolation, a1, a2 ); out[1]=ImageMath.lerp( interpolation, b1, b2
         * ); }
         */
    }

    // hash table
    private float[] hash = new float[2];
    private float[] preCacheData = null;
    int cacheindex = 0;
    int counter = 0;

    protected void cacheTable(int x, int y, float[] out)
    {
        loadCacheFull();
        /*
         * int c=cacheMaping(x,y); //interpolate out[0]=preCacheData[c*2];
         * out[1]=preCacheData[c*2+1];
         */
        if (mCacheW == width)
        {
            // System.out.println("the same");
            int c = y * width + x;
            out[0] = preCacheData[c * 2];
            out[1] = preCacheData[c * 2 + 1];
        } else
        {
            cacheMapingInter(x, y, out);
        }
    }

    protected void readCache(float[] out)
    {
        try {
            out[0] = cacheStream.readFloat();
            out[1] = cacheStream.readFloat();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if (Camera5in1.mDebug)
                System.out.println("cache index:" + cacheindex);
            e.printStackTrace();
        }
    }

    // ignores the higher 16 bits
    public static float toFloat(int hbits)
    {
        int mant = hbits & 0x03ff; // 10 bits mantissa
        int exp = hbits & 0x7c00; // 5 bits exponent
        if (exp == 0x7c00) // NaN/Inf
            exp = 0x3fc00; // -> NaN/Inf
        else if (exp != 0) // normalized value
        {
            exp += 0x1c000; // exp - 15 + 127
            if (mant == 0 && exp > 0x1c400) // smooth transition
                return Float.intBitsToFloat((hbits & 0x8000) << 16
                        | exp << 13 | 0x3ff);
        }
        else if (mant != 0) // && exp==0 -> subnormal
        {
            exp = 0x1c400; // make it normal
            do {
                mant <<= 1; // mantissa * 2
                exp -= 0x400; // decrease exp by 1
            } while ((mant & 0x400) == 0); // while not normal
            mant &= 0x3ff; // discard subnormal bit
        } // else +/-0 -> +/-0
        return Float.intBitsToFloat( // combine all parts
                (hbits & 0x8000) << 16 // sign << ( 31 - 15 )
                        | (exp | mant) << 13); // value << ( 23 - 10 )
    }

    private void loadCacheFull()
    {
        if (preCacheData == null)
        {
            int cacheSize = mCacheW * mCacheH * 2;
            preCacheData = new float[cacheSize];
            for (int i = 0; i < cacheSize; i++)
            {
                try {
                    // loading 32bits float
                    // preCacheData[i]=cacheStream.readFloat();
                    // loading 16bits float
                    byte[] testb = new byte[2];
                    testb[0] = cacheStream.readByte();
                    testb[1] = cacheStream.readByte();
                    int temp = 0;
                    // temp= (testb[0] & 0xFF) << 24 |(testb[1] & 0xFF) << 16 |
                    // (testb[2] & 0xFF) << 8| (testb[3] & 0xFF);
                    temp = (00 & 0xFF) << 24 | (00 & 0xFF) << 16 | (testb[0] & 0xFF) << 8
                            | (testb[1] & 0xFF);

                    float test3 = toFloat(temp);
                    preCacheData[i] = test3;

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    if (Camera5in1.mDebug)
                        System.out.println("cache index:" + cacheindex);
                    e.printStackTrace();
                }
            }
        }

    }

    private void transformImageRGB565(float[] src, float[] des,
            int w, int h, float[] matrix, int j, int i) {
        // two bytes per pixel.

        float src_x = 0;

        float src_x_nearby_right = 0;

        float src_x_nearby_bottom = 0;

        float src_x_nearby_right_bottom = 0;

        float des_x = 0;

        float x, y;
        float xxx, yyy, kkk;

        // for (int i = 0; i < h; ++i) {
        // for (int j = 0; j < w; ++j) {
        kkk = j * matrix[6] + i * matrix[7] + matrix[8];
        xxx = j * matrix[0] + i * matrix[1] + matrix[2];
        yyy = j * matrix[3] + i * matrix[4] + matrix[5];
        x = xxx / kkk;
        y = yyy / kkk;
        int xl = (int) Math.floor(x);
        int xr = xl + 1;
        int yl = (int) Math.floor(y);
        int yr = yl + 1;
        float ol_x = x - xl;
        float or_x = xr - x;
        float ol_y = y - yl;
        float or_y = yr - y;
        if (-1 == xl)
            xl = 0;
        if (w == xr)
            xr = w - 1;
        if (-1 == yl)
            yl = 0;
        if (h == yr)
            yr = h - 1;

        if (yl >= 0 && yr < h && xl >= 0 && xr < w) {

            src_x_nearby_right = (src[yl * w + xr]);
            src_x_nearby_bottom = (src[yr * w + xl]);
            src_x_nearby_right_bottom = (src[yr * w + xr]);

            des_x = (src_x * or_x + src_x_nearby_right * ol_x) * or_y
                    + (src_x_nearby_bottom * or_x
                    + src_x_nearby_right_bottom * ol_x) * ol_y; // set the pixel
                                                                // value.
            // des_565[i * w + j] = (des_r5 << 11) | (des_g6 << 5) | des_b5;

        }
    }

    public void filter(int[] inPixels, int[] outPixels) {

        int srcWidth = width;
        int srcHeight = height;
        int srcWidth1 = width - 1;
        int srcHeight1 = height - 1;
        int outWidth = srcWidth;
        int outHeight = srcHeight;
        int outX, outY;
        int index = 0;
        // int[] outTempPixels = new int[outWidth];
        int outpix = 0;
        // enable cache
        if (enablecache)
        {
            if (cacheStream == null)
            {
                // if(Runtime.getRuntime().freeMemory()>(mCacheW*mCacheH*8))
                {
                    cacheStream = openPreCacheResource(cachefilename);
                }
            }
        }

        // creaet cache
        FileConnection con = null;
        OutputStream os = null;
        if (precache)
        {
            String filename = System.currentTimeMillis() + "filter" + outWidth + "x" + outHeight
                    + ".wrp";
            try {
                con = (FileConnection) Connector.open(System
                        .getProperty("fileconn.dir.photos") + filename);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (con != null) {
                if (con.exists()) {
                    try {
                        con.delete();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                try {
                    con.create();
                    os = con.openOutputStream();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        outX = 0;
        outY = 0;
        float[] out = new float[2];
        mProgress = 0;

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                if (cacheStream != null)
                {
                    cacheTable((outX + x), (outY + y), out);
                }
                int srcX = (int) Math.floor(out[0]);
                int srcY = (int) Math.floor(out[1]);
                float xWeight = out[0] - srcX;
                float yWeight = out[1] - srcY;
                if (precache)
                {
                    /*
                     * try { os.write(float2Byte(out[0]));
                     * os.write(float2Byte(out[1])); } catch (IOException e) {
                     * // TODO Auto-generated catch block e.printStackTrace(); }
                     */
                    if (preCacheData == null)
                    {
                        int cacheSize = mCacheW * mCacheH * 2;
                        preCacheData = new float[cacheSize];
                    }
                    int c = cacheMaping(x, y);
                    preCacheData[c * 2] = out[0];
                    preCacheData[c * 2 + 1] = out[1];
                }
                // System.out.println(out[0]+","+out[1]);
                int nw, ne, sw, se;

                if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0
                        && srcY < srcHeight1) {
                    // Easy case, all corners are in the image
                    int i = srcWidth * srcY + srcX;
                    nw = inPixels[i];
                    ne = inPixels[i + 1];
                    sw = inPixels[i + srcWidth];
                    se = inPixels[i + srcWidth + 1];
                } else {
                    // Some of the corners are off the image
                    nw = getPixel(inPixels, srcX, srcY, srcWidth, srcHeight);
                    ne = getPixel(inPixels, srcX + 1, srcY, srcWidth, srcHeight);
                    sw = getPixel(inPixels, srcX, srcY + 1, srcWidth, srcHeight);
                    se = getPixel(inPixels, srcX + 1, srcY + 1, srcWidth,
                            srcHeight);
                }
                outpix = (outY + y) * width + (outX + x);
                outPixels[outpix] = bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
                // outTempPixels[x] = bilinearInterpolate(xWeight,yWeight, nw,
                // ne, sw, se);
            }
            // setRGB( dst, 0, y, transformedSpace.width, 1, outPixels );
            // System.arraycopy(outTempPixels, 0, outPixels, (outY + y) * width,
            // outWidth);
            if (y > 20)
                mProgress = (int) (((y * 100) / outHeight));
        }

        if (precache)
        {
            for (int i = 0; i < (mCacheW * mCacheH * 2); i++)
            {
                try {
                    os.write(float2Byte(preCacheData[i]));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            try {
                if (os != null) {
                    os.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (IOException io) {
            }
        }
    }

    public void filter16(short[] inPixels, short[] outPixels) {

        int srcWidth = width;
        int srcHeight = height;
        int srcWidth1 = width - 1;
        int srcHeight1 = height - 1;
        int outWidth = srcWidth;
        int outHeight = srcHeight;
        int outX, outY;
        int index = 0;
        // int[] outTempPixels = new int[outWidth];
        int outpix = 0;
        // enable cache
        if (enablecache)
        {
            if (cacheStream == null)
            {
                // if(Runtime.getRuntime().freeMemory()>(mCacheW*mCacheH*8))
                {
                    cacheStream = openPreCacheResource(cachefilename);
                }
            }
        }

        // creaet cache
        FileConnection con = null;
        OutputStream os = null;
        if (precache)
        {
            String filename = System.currentTimeMillis() + "filter" + outWidth + "x" + outHeight
                    + ".wrp";
            try {
                con = (FileConnection) Connector.open(System
                        .getProperty("fileconn.dir.photos") + filename);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (con != null) {
                if (con.exists()) {
                    try {
                        con.delete();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                try {
                    con.create();
                    os = con.openOutputStream();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        outX = 0;
        outY = 0;
        float[] out = new float[2];
        mProgress = 0;

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                if (cacheStream != null)
                {
                    cacheTable((outX + x), (outY + y), out);
                }
                int srcX = (int) Math.floor(out[0]);
                int srcY = (int) Math.floor(out[1]);
                float xWeight = out[0] - srcX;
                float yWeight = out[1] - srcY;
                if (precache)
                {
                    /*
                     * try { os.write(float2Byte(out[0]));
                     * os.write(float2Byte(out[1])); } catch (IOException e) {
                     * // TODO Auto-generated catch block e.printStackTrace(); }
                     */
                    if (preCacheData == null)
                    {
                        int cacheSize = mCacheW * mCacheH * 2;
                        preCacheData = new float[cacheSize];
                    }
                    int c = cacheMaping(x, y);
                    preCacheData[c * 2] = out[0];
                    preCacheData[c * 2 + 1] = out[1];
                }
                // System.out.println(out[0]+","+out[1]);
                short nw, ne, sw, se;

                if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0
                        && srcY < srcHeight1) {
                    // Easy case, all corners are in the image
                    int i = srcWidth * srcY + srcX;
                    nw = inPixels[i];
                    ne = inPixels[i + 1];
                    sw = inPixels[i + srcWidth];
                    se = inPixels[i + srcWidth + 1];
                } else {
                    // Some of the corners are off the image
                    nw = getPixel16(inPixels, srcX, srcY, srcWidth, srcHeight);
                    ne = getPixel16(inPixels, srcX + 1, srcY, srcWidth, srcHeight);
                    sw = getPixel16(inPixels, srcX, srcY + 1, srcWidth, srcHeight);
                    se = getPixel16(inPixels, srcX + 1, srcY + 1, srcWidth,
                            srcHeight);
                }
                outpix = (outY + y) * width + (outX + x);
                outPixels[outpix] = bilinearInterpolate16(xWeight, yWeight, nw, ne, sw, se);
                // outTempPixels[x] = bilinearInterpolate(xWeight,yWeight, nw,
                // ne, sw, se);
            }
            // setRGB( dst, 0, y, transformedSpace.width, 1, outPixels );
            // System.arraycopy(outTempPixels, 0, outPixels, (outY + y) * width,
            // outWidth);
            if (y > 20)
                mProgress = (int) (((y * 100) / outHeight));
        }

        if (precache)
        {
            for (int i = 0; i < (mCacheW * mCacheH * 2); i++)
            {
                try {
                    os.write(float2Byte(preCacheData[i]));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            try {
                if (os != null) {
                    os.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (IOException io) {
            }
        }
    }

    public void filter16to32(short[] inPixels, int[] outPixels) {

        int srcWidth = width;
        int srcHeight = height;
        int srcWidth1 = width - 1;
        int srcHeight1 = height - 1;
        int outWidth = srcWidth;
        int outHeight = srcHeight;
        int outX, outY;
        int index = 0;
        // int[] outTempPixels = new int[outWidth];
        int outpix = 0;
        // enable cache
        if (enablecache)
        {
            if (cacheStream == null)
            {
                // if(Runtime.getRuntime().freeMemory()>(mCacheW*mCacheH*8))
                {
                    cacheStream = openPreCacheResource(cachefilename);
                }
            }
        }

        // creaet cache
        FileConnection con = null;
        OutputStream os = null;
        if (precache)
        {
            String filename = System.currentTimeMillis() + "filter" + outWidth + "x" + outHeight
                    + ".wrp";
            try {
                con = (FileConnection) Connector.open(System
                        .getProperty("fileconn.dir.photos") + filename);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (con != null) {
                if (con.exists()) {
                    try {
                        con.delete();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                try {
                    con.create();
                    os = con.openOutputStream();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        outX = 0;
        outY = 0;
        float[] out = new float[2];
        mProgress = 0;

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                if (cacheStream != null)
                {
                    cacheTable((outX + x), (outY + y), out);
                }
                int srcX = (int) Math.floor(out[0]);
                int srcY = (int) Math.floor(out[1]);
                float xWeight = out[0] - srcX;
                float yWeight = out[1] - srcY;
                if (precache)
                {
                    /*
                     * try { os.write(float2Byte(out[0]));
                     * os.write(float2Byte(out[1])); } catch (IOException e) {
                     * // TODO Auto-generated catch block e.printStackTrace(); }
                     */
                    if (preCacheData == null)
                    {
                        int cacheSize = mCacheW * mCacheH * 2;
                        preCacheData = new float[cacheSize];
                    }
                    int c = cacheMaping(x, y);
                    preCacheData[c * 2] = out[0];
                    preCacheData[c * 2 + 1] = out[1];
                }
                // System.out.println(out[0]+","+out[1]);
                short nw, ne, sw, se;

                if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0
                        && srcY < srcHeight1) {
                    // Easy case, all corners are in the image
                    int i = srcWidth * srcY + srcX;
                    nw = inPixels[i];
                    ne = inPixels[i + 1];
                    sw = inPixels[i + srcWidth];
                    se = inPixels[i + srcWidth + 1];
                } else {
                    // Some of the corners are off the image
                    nw = getPixel16(inPixels, srcX, srcY, srcWidth, srcHeight);
                    ne = getPixel16(inPixels, srcX + 1, srcY, srcWidth, srcHeight);
                    sw = getPixel16(inPixels, srcX, srcY + 1, srcWidth, srcHeight);
                    se = getPixel16(inPixels, srcX + 1, srcY + 1, srcWidth,
                            srcHeight);
                }
                outpix = (outY + y) * width + (outX + x);
                outPixels[outpix] = bilinearInterpolate16to32(xWeight, yWeight, nw, ne, sw, se);
                // outTempPixels[x] = bilinearInterpolate(xWeight,yWeight, nw,
                // ne, sw, se);
            }
            // setRGB( dst, 0, y, transformedSpace.width, 1, outPixels );
            // System.arraycopy(outTempPixels, 0, outPixels, (outY + y) * width,
            // outWidth);
            if (y > 20)
                mProgress = (int) (((y * 100) / outHeight));
        }

        if (precache)
        {
            for (int i = 0; i < (mCacheW * mCacheH * 2); i++)
            {
                try {
                    os.write(float2Byte(preCacheData[i]));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            try {
                if (os != null) {
                    os.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (IOException io) {
            }
        }
    }

    private final byte[] float2Byte(float inData) {
        int j = 0;
        byte[] outData = new byte[4];
        int data = Float.floatToIntBits(inData);
        outData[j++] = (byte) (data >>> 24);
        outData[j++] = (byte) (data >>> 16);
        outData[j++] = (byte) (data >>> 8);
        outData[j++] = (byte) (data >>> 0);
        return outData;
    }

    private int getPixel(int[] pixels, int x, int y, int width, int height) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return pixels[(clamp(y, 0, height - 1) * width) + clamp(x, 0, width - 1)];
        }
        return pixels[y * width + x];
    }
    
    private short getPixel16(short[] pixels, int x, int y, int width, int height) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return pixels[(clamp(y, 0, height - 1) * width) + clamp(x, 0, width - 1)];
        }
        return pixels[y * width + x];
    }

    protected DataInputStream openPreCacheResource(String resName) {
        InputStream file = getClass().getResourceAsStream(resName);
        DataInputStream in = new DataInputStream(file);
        if (file == null) {
            System.out.println("Error opening vignette resource");
        }
        return in;
    }

    public int getProgress()
    {
        return mProgress;
    }

    private int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }

    private int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
        float m0, m1;
        int a0 = (nw >> 24) & 0xff;
        int r0 = (nw >> 16) & 0xff;
        int g0 = (nw >> 8) & 0xff;
        int b0 = nw & 0xff;
        int a1 = (ne >> 24) & 0xff;
        int r1 = (ne >> 16) & 0xff;
        int g1 = (ne >> 8) & 0xff;
        int b1 = ne & 0xff;
        int a2 = (sw >> 24) & 0xff;
        int r2 = (sw >> 16) & 0xff;
        int g2 = (sw >> 8) & 0xff;
        int b2 = sw & 0xff;
        int a3 = (se >> 24) & 0xff;
        int r3 = (se >> 16) & 0xff;
        int g3 = (se >> 8) & 0xff;
        int b3 = se & 0xff;

        float cx = 1.0f - x;
        float cy = 1.0f - y;

        m0 = cx * a0 + x * a1;
        m1 = cx * a2 + x * a3;
        int a = (int) (cy * m0 + y * m1);

        m0 = cx * r0 + x * r1;
        m1 = cx * r2 + x * r3;
        int r = (int) (cy * m0 + y * m1);

        m0 = cx * g0 + x * g1;
        m1 = cx * g2 + x * g3;
        int g = (int) (cy * m0 + y * m1);

        m0 = cx * b0 + x * b1;
        m1 = cx * b2 + x * b3;
        int b = (int) (cy * m0 + y * m1);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private short bilinearInterpolate16(float x, float y, short nw, short ne, short sw, short se) {
        float m0, m1;
        
        int r0 = ((nw >> 11) & 0x1f);
        int g0 = ((nw >> 5) & 0x3f);
        int b0 = (nw & 0x1f);

        r0 = (r0 << 3) | ( r0 >> 2); 
        g0 = (g0 << 2) | ( g0 >> 4); 
        b0 = (b0 << 3) | ( b0 >> 2);
        
        int r1 = ((ne >> 11) & 0x1f);
        int g1 = ((ne >> 5) & 0x3f);
        int b1 = (ne & 0x1f);

        r1 = (r1 << 3) | ( r1 >> 2); 
        g1 = (g1 << 2) | ( g1 >> 4); 
        b1 = (b1 << 3) | ( b1 >> 2);
        
        int r2 = ((sw >> 11) & 0x1f);
        int g2 = ((sw >> 5) & 0x3f);
        int b2 = (sw & 0x1f);

        r2 = (r2 << 3) | ( r2 >> 2); 
        g2 = (g2 << 2) | ( g2 >> 4); 
        b2 = (b2 << 3) | ( b2 >> 2);
        
        int r3 = ((se >> 11) & 0x1f);
        int g3 = ((se >> 5) & 0x3f);
        int b3 = (se & 0x1f);

        r3 = (r3 << 3) | ( r3 >> 2); 
        g3 = (g3 << 2) | ( g3 >> 4); 
        b3 = (b3 << 3) | ( b3 >> 2);

        float cx = 1.0f - x;
        float cy = 1.0f - y;
        
        m0 = cx * r0 + x * r1;
        m1 = cx * r2 + x * r3;
        int r = (int) (cy * m0 + y * m1);

        m0 = cx * g0 + x * g1;
        m1 = cx * g2 + x * g3;
        int g = (int) (cy * m0 + y * m1);

        m0 = cx * b0 + x * b1;
        m1 = cx * b2 + x * b3;
        int b = (int) (cy * m0 + y * m1);

        r = r >> 3;
        g = g >> 2;
        b = b >> 3;
        
        return (short)((r << 11) | (g << 5) | b);
    }

    private int bilinearInterpolate16to32(float x, float y, short nw, short ne, short sw, short se) {
        float m0, m1;
        
        int r0 = ((nw >> 11) & 0x1f);
        int g0 = ((nw >> 5) & 0x3f);
        int b0 = (nw & 0x1f);

        r0 = (r0 << 3) | ( r0 >> 2); 
        g0 = (g0 << 2) | ( g0 >> 4); 
        b0 = (b0 << 3) | ( b0 >> 2);
        
        int r1 = ((ne >> 11) & 0x1f);
        int g1 = ((ne >> 5) & 0x3f);
        int b1 = (ne & 0x1f);

        r1 = (r1 << 3) | ( r1 >> 2); 
        g1 = (g1 << 2) | ( g1 >> 4); 
        b1 = (b1 << 3) | ( b1 >> 2);
        
        int r2 = ((sw >> 11) & 0x1f);
        int g2 = ((sw >> 5) & 0x3f);
        int b2 = (sw & 0x1f);

        r2 = (r2 << 3) | ( r2 >> 2); 
        g2 = (g2 << 2) | ( g2 >> 4); 
        b2 = (b2 << 3) | ( b2 >> 2);
        
        int r3 = ((se >> 11) & 0x1f);
        int g3 = ((se >> 5) & 0x3f);
        int b3 = (se & 0x1f);

        r3 = (r3 << 3) | ( r3 >> 2); 
        g3 = (g3 << 2) | ( g3 >> 4); 
        b3 = (b3 << 3) | ( b3 >> 2);

        float cx = 1.0f - x;
        float cy = 1.0f - y;
        
        m0 = cx * r0 + x * r1;
        m1 = cx * r2 + x * r3;
        int r = (int) (cy * m0 + y * m1);

        m0 = cx * g0 + x * g1;
        m1 = cx * g2 + x * g3;
        int g = (int) (cy * m0 + y * m1);

        m0 = cx * b0 + x * b1;
        m1 = cx * b2 + x * b3;
        int b = (int) (cy * m0 + y * m1);
        
        return (0xFFFFFFFF << 24) | (r << 16) | (g << 8) | b;
    }

}
