/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.pictelligent.s40.camera5in1.JPEGDecoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.SystemUtil;
import com.pictelligent.s40.camera5in1.JPEGDecoder.DqtSegment.QuantizationTable;
import com.pictelligent.s40.camera5in1.JPEGDecoder.JpegConstants;


public class JpegDecoder extends BinaryFileParser  implements JpegConstants{
    /*
     * JPEG is an advanced image format that takes significant computation to
     * decode. Keep decoding fast: - Don't allocate memory inside loops,
     * allocate it once and reuse. - Minimize calculations per pixel and per
     * block (using lookup tables for YCbCr->RGB conversion doubled
     * performance). - Math.round() is slow, use (int)(x+0.5f) instead for
     * positive numbers.
     */

    //private final DqtSegment.QuantizationTable[] quantizationTables = new DqtSegment.QuantizationTable[4];
    private final DhtSegment.HuffmanTable[] huffmanDCTables = new DhtSegment.HuffmanTable[4];
    private final DhtSegment.HuffmanTable[] huffmanACTables = new DhtSegment.HuffmanTable[4];
    private SofnSegment sofnSegment;
    private SosSegment sosSegment;
    //private final float[][] scaledQuantizationTables = new float[4][];
    private final int[][] scaledQuantizationTables = new int[4][];
    private short[] mOutputRGB565 = null;
    private int mHeight=0;
    private int mWidth=0;
    private int mRotate=0;//0,1:right 90, 2:degree: 180, 3:degree: 270
    
    private ImageReadException imageReadException = null;
    private IOException ioException = null;
   
    
    public void setRoate(int r)
    {
    	mRotate=r;
    }
    
    public boolean beginSOS() {
        return true;
    }

    //public void visitSOS(final int marker, final byte markerBytes[], final byte imageData[]) {
    public void visitSOS(final int marker, final byte markerBytes[], final ByteArrayInputStream is) {
        //final ByteArrayInputStream is = new ByteArrayInputStream(imageData);
        try {
            final int segmentLength = read2Bytes("segmentLength", is,
                    "Not a Valid JPEG File",true);
            DebugUtil.Log("segmentLength:"+segmentLength);
            final byte[] sosSegmentBytes = readByteArray("SosSegment",
                    segmentLength - 2, is, "Not a Valid JPEG File");
            sosSegment = new SosSegment(marker, sosSegmentBytes);

            int hMax = 0;
            int vMax = 0;
            for (int i = 0; i < sofnSegment.numberOfComponents; i++) {
                hMax = Math.max(hMax,
                        sofnSegment.components[i].horizontalSamplingFactor);
                vMax = Math.max(vMax,
                        sofnSegment.components[i].verticalSamplingFactor);
            }
            final int hSize = 8 * hMax;
            final int vSize = 8 * vMax;
            
            final JpegInputStream bitInputStream = new JpegInputStream(is);
            final int xMCUs = (sofnSegment.width + hSize - 1) / hSize;
            final int yMCUs = (sofnSegment.height + vSize - 1) / vSize;
            final Block[] mcu = allocateMCUMemory();
            //optimize: take off scaled buffer
            //final Block[] scaledMCU = new Block[mcu.length];
            //for (int i = 0; i < scaledMCU.length; i++) {
            //    scaledMCU[i] = new Block(hSize, vSize);
            //}
            
            final int[] preds = new int[sofnSegment.numberOfComponents];
            
            if(mOutputRGB565==null)
            	mOutputRGB565=new short[sofnSegment.width*sofnSegment.height];
            
            mHeight=sofnSegment.height;
            mWidth=sofnSegment.width;

            
            DebugUtil.Log("mcu decode");
            for (int y1 = 0; y1 < vSize * yMCUs; y1 += vSize) {
                for (int x1 = 0; x1 < hSize * xMCUs; x1 += hSize) {
                    readMCU(bitInputStream, preds, mcu);
                    //optimize: take off scaled calculate
                    //rescaleMCU(mcu, hSize, vSize, scaledMCU);
                    int srcRowOffset = 0;
                    int dstRowOffset = y1 * sofnSegment.width + x1;
                    int dstRotateCol=x1;//dstRowOffset%sofnSegment.width;//optimize
                    int dstRotateRow=y1;//(dstRowOffset/sofnSegment.width);//optimize
                    for (int y2 = 0; y2 < vSize && y1 + y2 < sofnSegment.height; y2++) {
                    	int srcMCURowOffect=(y2>>1)*mcu[1].width;//optimize
                        for (int x2 = 0; x2 < hSize
                                && x1 + x2 < sofnSegment.width; x2++) {
                        	
                        	//System.out.println("rgb get:"+srcRowOffset +",x:"+ x2);
                            if (mcu.length == 3) {
                                //final int Y = scaledMCU[0].samples[srcRowOffset + x2];
                                //final int Cb = scaledMCU[1].samples[srcRowOffset + x2];
                                //final int Cr = scaledMCU[2].samples[srcRowOffset + x2];
                            	final int Y = mcu[0].samples[srcRowOffset + x2];//Y is the same size
                            	//optimize: only for 2x2 case
                                final int Cb = mcu[1].samples[srcMCURowOffect + (x2 >> 1)];
                                final int Cr = mcu[2].samples[srcMCURowOffect + (x2 >> 1)];
                                //final int rgb = YCbCrConverter.convertYCbCrToRGB(Y,
                                //        Cb, Cr);
                                //dataBuffer.setElem(dstRowOffset + x2, rgb);
                                //short rgb= YCbCr_to_BGR16(Y,Cb, Cr);
                                short rgb= YUV_to_BGR16(Y+128,Cb, Cr);
                                //short rgb=YCbCrConverter.convertYCbCrToRGB565(Y,Cb, Cr);
                                switch(mRotate)
                                {
                                    case 0:
                                	//rotate 0 degree
                                    mOutputRGB565[dstRowOffset + x2]=rgb;
                                	break;
                                    case 1:
                                    //rotate 90 degree
                                    //[x,y]->[(H-y-1),x]
                                    //x->x2+dstRowOffset%sofnSegment.width
                                    //y->dstRowOffset/sofnSegment.width
                                    int x=x2+(dstRotateCol);
                                    int y=(dstRotateRow);
                                    mOutputRGB565[(x)*(sofnSegment.height)+(sofnSegment.height-y-1)]=rgb;
                                    break;
                                }
                            } else if (mcu.length == 1) {
                                final int Y = mcu[0].samples[srcRowOffset + x2];
                                //dataBuffer.setElem(dstRowOffset + x2, (Y << 16)
                                //        | (Y << 8) | Y);
                                
                                mOutputRGB565[dstRowOffset + x2]=(short)(((Y>>3) << 16) | ((Y>>2) << 8) | (Y>>3));
                            } else {
                                throw new ImageReadException(
                                        "Unsupported JPEG with " + mcu.length
                                                + " components");
                            }
                        }
                        srcRowOffset += hSize;//<-scalesMCU
                        //srcRowOffset += mcu[1].width;//<-MCU
                        //srcRowOffset2 += hSize;//<-MCU
                        dstRowOffset += sofnSegment.width;
                        //dstRotateCol=x1;//dstRowOffset%sofnSegment.width;//optimize
                        dstRotateRow+=1;//(dstRowOffset/sofnSegment.width);//optimize
                    }
                }
            }
        } catch (final ImageReadException imageReadEx) {
            imageReadException = imageReadEx;
        } catch (final IOException ioEx) {
            ioException = ioEx;
        } catch (final RuntimeException ex) {
            // Corrupt images can throw NPE and IOOBE
            imageReadException = new ImageReadException("Error parsing JPEG",
                    ex);
            imageReadException.printStackTrace();
        }
    }

    private final static int[] sofnSegments = { SOF0Marker, SOF1Marker, SOF2Marker,
            SOF3Marker, SOF5Marker, SOF6Marker, SOF7Marker, SOF9Marker,
            SOF10Marker, SOF11Marker, SOF13Marker, SOF14Marker,
            SOF15Marker, };
    
    public boolean visitSegment(final int marker, final byte[] markerBytes,
            final int segmentLength, final byte[] segmentLengthBytes, final byte[] segmentData)
            throws ImageReadException, IOException {
        

        if (binarySearch(sofnSegments, marker) >= 0) {
            if (marker != SOF0Marker) {
                throw new ImageReadException("Only sequential, baseline JPEGs "
                        + "are supported at the moment");
            }
            sofnSegment = new SofnSegment(marker, segmentData);
        } else if (marker == DQTMarker) {
            final DqtSegment dqtSegment = new DqtSegment(marker, segmentData);
            for (int i = 0; i < dqtSegment.quantizationTables.size(); i++) {
                final DqtSegment.QuantizationTable table = (QuantizationTable)dqtSegment.quantizationTables.elementAt(i);
                /*
                if (0 > table.destinationIdentifier
                        || table.destinationIdentifier >= quantizationTables.length) {
                    throw new ImageReadException(
                            "Invalid quantization table identifier "
                                    + table.destinationIdentifier);
                }
                quantizationTables[table.destinationIdentifier] = table;
                */
                final int[] quantizationMatrixInt = new int[64];
                System.arraycopy(table.elements, 0, quantizationMatrixInt, 0, 64);
                //ZigZag.zigZagToBlock(table.elements, quantizationMatrixInt);
                /*
                final float[] quantizationMatrixFloat = new float[64];
                for (int j = 0; j < 64; j++) {
                    quantizationMatrixFloat[j] = quantizationMatrixInt[j];
                }
                Dct.scaleDequantizationMatrix(quantizationMatrixFloat);
                scaledQuantizationTables[table.destinationIdentifier] = quantizationMatrixFloat;
                */
                //DctI.scaleDequantizationMatrix(quantizationMatrixInt);
                //scaledQuantizationTables[table.destinationIdentifier] = quantizationMatrixInt;
                
                DctI.EnhanceQuantizationTable(quantizationMatrixInt);
                scaledQuantizationTables[table.destinationIdentifier] = quantizationMatrixInt;
            }
        } else if (marker == DHTMarker) {
            final DhtSegment dhtSegment = new DhtSegment(marker, segmentData);
            for (int i = 0; i < dhtSegment.huffmanTables.size(); i++) {
                final DhtSegment.HuffmanTable table = (DhtSegment.HuffmanTable) dhtSegment.huffmanTables.elementAt(i);
                DhtSegment.HuffmanTable[] tables;
                if (table.tableClass == 0) {
                    tables = huffmanDCTables;
                } else if (table.tableClass == 1) {
                    tables = huffmanACTables;
                } else {
                    throw new ImageReadException("Invalid huffman table class "
                            + table.tableClass);
                }
                if (0 > table.destinationIdentifier
                        || table.destinationIdentifier >= tables.length) {
                    throw new ImageReadException(
                            "Invalid huffman table identifier "
                                    + table.destinationIdentifier);
                }
                tables[table.destinationIdentifier] = table;
            }
        }
        return true;
    }
  
    private Block[] allocateMCUMemory() throws ImageReadException {
        final Block[] mcu = new Block[sosSegment.numberOfComponents];
        
        for (int i = 0; i < sosSegment.numberOfComponents; i++) {
            final SosSegment.Component scanComponent = sosSegment.components[i];
            SofnSegment.Component frameComponent = null;
            for (int j = 0; j < sofnSegment.numberOfComponents; j++) {
                if (sofnSegment.components[j].componentIdentifier == scanComponent.scanComponentSelector) {
                    frameComponent = sofnSegment.components[j];
                    break;
                }
            }
            if (frameComponent == null) {
                throw new ImageReadException("Invalid component");
            }
            final Block fullBlock = new Block(
                    8 * frameComponent.horizontalSamplingFactor,
                    8 * frameComponent.verticalSamplingFactor);
            DebugUtil.Log("h:"+frameComponent.horizontalSamplingFactor);
            DebugUtil.Log(",v:"+frameComponent.verticalSamplingFactor);
            //System.out.println("Block:"+(8 * frameComponent.horizontalSamplingFactor*8 * frameComponent.verticalSamplingFactor));
            mcu[i] = fullBlock;
        }
        return mcu;
    }

    private final int[] zz = new int[64];
    private final int[] blockInt = new int[64];
    //private final float[] block = new float[64];
    private final static int[] zztemp = new int[64];

    private void readMCU(final JpegInputStream is, final int[] preds, final Block[] mcu)
            throws IOException, ImageReadException {
    	//SystemUtil.Log_current_time_s("readMCU S");
        for (int i = 0; i < sosSegment.numberOfComponents; i++) {
            final SosSegment.Component scanComponent = sosSegment.components[i];
            SofnSegment.Component frameComponent = null;
            for (int j = 0; j < sofnSegment.numberOfComponents; j++) {
                if (sofnSegment.components[j].componentIdentifier == scanComponent.scanComponentSelector) {
                    frameComponent = sofnSegment.components[j];
                    break;
                }
            }
            if (frameComponent == null) {
                throw new ImageReadException("Invalid component");
            }
            final Block fullBlock = mcu[i];
            for (int y = 0; y < frameComponent.verticalSamplingFactor; y++) {
                for (int x = 0; x < frameComponent.horizontalSamplingFactor; x++) {
                    //fill(zz, 0);
                    System.arraycopy(zztemp, 0, zz, 0, zz.length);
                    // page 104 of T.81
                    //for(int k=0; k<IDCT_Source.length; k++) 
                    //    IDCT_Source[k]=0;
                    
                    final int t = decode(
                            is,
                            huffmanDCTables[scanComponent.dcCodingTableSelector]);
                    int diff = receive(t, is);
                    diff = extend(diff, t);
                    zz[0] = preds[i] + diff;
                    preds[i] = zz[0];

                    // "Decode_AC_coefficients", figure F.13, page 106 of T.81
                    int k = 1;
                    while (true) {
                        final int rs = decode(
                                is,
                                huffmanACTables[scanComponent.acCodingTableSelector]);
                        final int ssss = rs & 0xf;
                        final int rrrr = rs >> 4;
                        final int r = rrrr;

                        if (ssss == 0) {
                            if (r == 15) {
                                k += 16;
                            } else {
                                break;
                            }
                        } else {
                            k += r;

                            // "Decode_ZZ(k)", figure F.14, page 107 of T.81
                            zz[k] = receive(ssss, is);
                            zz[k] = extend(zz[k], ssss);

                            if (k == 63) {
                                break;
                            } else {
                                k++;
                            }
                        }
                    }

                    //final int shift = (1 << (sofnSegment.precision - 1));
                    //final int max = (1 << sofnSegment.precision) - 1;

                    //final float[] scaledQuantizationTable = scaledQuantizationTables[frameComponent.quantTabDestSelector];
                    final int[] scaledQuantizationTable = scaledQuantizationTables[frameComponent.quantTabDestSelector];
                    //ZigZag.zigZagToBlock(zz, blockInt);
                    /*
                      for (int j = 0; j < 64; j++) {
                     
                        //block[j] = blockInt[j] * scaledQuantizationTable[j];
                    	//blockInt[j] *=scaledQuantizationTable[j];
                    	blockInt[IDCT_P[j]] = 
                    			zz[j]*scaledQuantizationTable[j];
                    }
                    */
                    DctI.scaleQuantizationMatrix(zz, scaledQuantizationTable, blockInt);
                    //Dct.inverseDCT8x8(block);
                    DctI.inverseDCT8x8(blockInt);
                    //ScaleIDCT(blockInt);

                    int dstRowOffset = 8 * y * 8
                            * frameComponent.horizontalSamplingFactor + 8 * x;
                    int srcNext = 0;
                    for (int yy = 0; yy < 8; yy++) {
                        for (int xx = 0; xx < 8; xx++) {
                        	/*
                            float sample = block[srcNext++];
                            sample += shift;
                            int result;
                            if (sample < 0) {
                                result = 0;
                            } else if (sample > max) {
                                result = max;
                            } else {
                                result = fastRound(sample);
                            }
                            */
                        	//int result=(int) ((int)((float)blockInt[srcNext++]/1000+0.5));
                        	int result=blockInt[srcNext++];
                            fullBlock.samples[dstRowOffset + xx] = result;
                        }
                        dstRowOffset += 8 * frameComponent.horizontalSamplingFactor;
                    }
                }
            }
        }
        //SystemUtil.Log_current_time_s("readMCU E");
    }

    private static int fastRound(final float x) {
        return (int) (x + 0.5f);
    }

    private int extend(int v, final int t) {
        // "EXTEND", section F.2.2.1, figure F.12, page 105 of T.81
        int vt = (1 << (t - 1));
        while (v < vt) {
            vt = (-1 << t) + 1;
            v += vt;
        }
        return v;
    }

    private int receive(final int ssss, final JpegInputStream is) throws IOException,
            ImageReadException {
        // "RECEIVE", section F.2.2.4, figure F.17, page 110 of T.81
        int i = 0;
        int v = 0;
        while (i != ssss) {
            i++;
            v = (v << 1) + is.nextBit();
        }
        return v;
    }

    private int decode(final JpegInputStream is, final DhtSegment.HuffmanTable huffmanTable)
            throws IOException, ImageReadException {
        // "DECODE", section F.2.2.3, figure F.16, page 109 of T.81
        int i = 1;
        int code = is.nextBit();
        while (code > huffmanTable.maxCode[i]) {
            i++;
            code = (code << 1) | is.nextBit();
        }
        int j = huffmanTable.valPtr[i];
        j += code - huffmanTable.minCode[i];
        final int value = huffmanTable.huffVal[j];
        return value;
    }

    public void decode(byte[] Jpegbyte,ImageRGB RAWoutput) throws IOException {
        
    	traverseJFIF(Jpegbyte);
        if (imageReadException != null) {
            try {
				throw imageReadException;
			} catch (ImageReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if (ioException != null) {
            throw ioException;
        }
        RAWoutput.RGB565=mOutputRGB565;
        
        
        switch(mRotate)
        {
            case 0:
            	RAWoutput.Height=mHeight;
                RAWoutput.Width=mWidth;
        	break;
            case 1:
            	RAWoutput.Height=mWidth;
                RAWoutput.Width=mHeight;
            break;
        }
        //return imageb;
    }
    
    public void traverseJFIF(byte[] byteSource)
            throws IOException {
        InputStream is = null;
        is = new ByteArrayInputStream(byteSource);
        
        DebugUtil.Log("bytes len:"+byteSource.length);
/*
        byte temp=readByte("marker", is,
                "Could not read marker");
        
        System.out.println("0x"+temp);
        temp=readByte("marker", is,
                "Could not read marker");
        System.out.println("0x"+temp);
  */      
        try {
			readAndVerifyBytes(null, is, SOI,
			        "Not a Valid JPEG File: doesn't begin with 0xffd8");
		} catch (ImageReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            int markerCount;
            for (markerCount = 0; true; markerCount++) {
                byte[] markerBytes = new byte[2];
                do {
                    markerBytes[0] = markerBytes[1];
                    markerBytes[1] = readByte("marker", is,
                            "Could not read marker");
                } while ((0xff & markerBytes[0]) != 0xff
                        || (0xff & markerBytes[1]) == 0xff);
                final int marker = ((0xff & markerBytes[0]) << 8)
                        | (0xff & markerBytes[1]);

                // Debug.debug("marker", marker + " (0x" +
                // Integer.toHexString(marker) + ")");
                // Debug.debug("markerBytes", markerBytes);
                DebugUtil.Log("marker :"+ marker + " (0x" + Integer.toHexString(marker) + ")");
                
                if (marker == EOIMarker || marker == SOS_Marker) {
                	DebugUtil.Log("visitSOS");
                	//final byte imageData[] = getStreamBytes(is);
                	//final ByteArrayInputStream is = new ByteArrayInputStream(imageData);
                	SystemUtil.Log_current_time_s("visitSOS");
                    visitSOS(marker, markerBytes, (ByteArrayInputStream) is);
                    SystemUtil.Log_current_time_s("visitSOS");
                    break;
                }
                
                final byte segmentLengthBytes[] = readByteArray("segmentLengthBytes",
                        2, is, "segmentLengthBytes");
                
                int segmentLength = 0;
                
				try {
					segmentLength = convertByteArrayToShort("segmentLength",
					        segmentLengthBytes,true);
				} catch (ImageReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Debug.debug("segmentLength", segmentLength + " (0x" +
                // Integer.toHexString(segmentLength) + ")");
                // Debug.debug("segmentLengthBytes", segmentLengthBytes);

                byte segmentData[] = readByteArray("Segment Data",
                        segmentLength - 2, is,
                        "Invalid Segment: insufficient data");

                // Debug.debug("segmentLength", segmentLength);
                
                try {
                	SystemUtil.Log_current_time_s("visitSegment");
					if (!visitSegment(marker, markerBytes, segmentLength,
					        segmentLengthBytes, segmentData)) {
					    return;
					}
					SystemUtil.Log_current_time_s("visitSegment");
				} catch (ImageReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                segmentData=null;
            }
            
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (final Exception e) {
            }
        }
    }

    //binary search
    
    //public static int binarySearch(int[] a, int low, int hi, int key)
    public static int binarySearch(int[] a, int key)
       {
    	int low=0;
    	int hi=a.length-1;
    	
    	if (low > hi)
           throw new IllegalArgumentException("The start index is higher than " +
                          "the finish index.");
         if (low < 0 || hi > a.length)
           throw new ArrayIndexOutOfBoundsException("One of the indices is out " +
                                "of bounds.");
         int mid = 0;
         while (low <= hi)
           {
             mid = (low + hi) >>> 1;
             
             final int d = a[mid];
             if (d == key)
               return mid;
             else if (d > key)
               hi = mid - 1;
             else
               // This gets the insertion point right on the last loop.
               low = ++mid;
           }
         return -mid - 1;
       }
    
  
    public static void fill(int[] a, int val)
       {
        for(int i=0;i<a.length;i++)
        {
        	a[i]=val;
        }
       }


    private short YUV_to_BGR16(int Y,int u,int v){
  	    if(Y<0) Y=0;
  	    int tempB,tempG,tempR;
  	    tempB=Y+((116130*u)>>16);
  	    if(tempB<0) tempB=0;
  	    else if(tempB>255) tempB=255;
  	 
  	    tempG=Y-((22554*u+46802*v)>>16);
  	    if(tempG<0) tempG=0;
  	    else if(tempG>255) tempG=255;
  	    
  	    tempR=Y+((91881*v)>>16);
  	    if(tempR<0) tempR=0;
  	    else if(tempR>255) tempR=255;

  	    //return  0xff000000 | ((tempR<<16) + (tempG<<8) + tempB);
  	    tempR = tempR >> 3;
  	    tempG = tempG >> 2;
  	    tempB = tempB >> 3;     				
  		
  		return (short) ((tempR << 11) | (tempG << 5) | tempB);
  	}

    private short YCbCr_to_BGR16(final int y,final int u,final int v){
    	final double Y = (double) y;
    	final double Cb = (double) u;
    	final double Cr = (double) v;

    	 int r = (int) (Y + 1.40200 * (Cr - 0x80));
    	 int g = (int) (Y - 0.34414 * (Cb - 0x80) - 0.71414 * (Cr - 0x80));
    	 int b = (int) (Y + 1.77200 * (Cb - 0x80));

    	 r = Math.max(0, Math.min(255, r));
    	 g = Math.max(0, Math.min(255, g));
    	 b = Math.max(0, Math.min(255, b));


	    //return  0xff000000 | ((tempR<<16) + (tempG<<8) + tempB);
	    r = r >> 3;
	    g = g >> 2;
	    b = b >> 3;     				
		
		return (short) ((r << 11) | (g << 5) | b);
	}
      
}
