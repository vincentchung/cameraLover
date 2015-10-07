/*
 * JPEG encoder algorithm for J2ME
 * 
 * (c) Pictelligent 2012, All rights reserved
 * 
 * 
 * Based on Independent JPEG group's JPEG algorithm http://www.ijg.org/
 * 
 * This encoding algorithm makes several (dangerous) assumptions:
 *   - input buffer must have three color components (i.e. RGB input)
 *   - JPEG is downsampled to 4:2:0 YCbCr
 *  
 * No animals were harmed in making of this fine piece of source code. Humans... well, that's a different story
 *
 */

package com.pictelligent.s40.camera5in1.JPEGEncoder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.pictelligent.s40.camera5in1.DebugUtil;

public class JpegEncoder
{
    private DCT dct;
    private Quantize quant;
    private Huffman huffman;

    private JpegCompressInfo cInfo;

    // JPEG encoder debug options are independent from calling application
    // Note that if debug is on, DebugUtil must have been initialized by caller
    final static boolean mDebug = false;
    final static boolean mDebugVerbose = false;

    // Image components in YCbCr space
    private final short CS_Y = 0;
    private final short CS_Cb = 1;
    private final short CS_Cr = 2;

    // variables and constants for fixed point RGB2YCC conversion
    int rgb_ycc_tab[];

    static final short SCALEBITS = 16;
    static final int ONE_HALF = (1 << (SCALEBITS - 1));
    static final short MAXJSAMPLE = 256;

    static final int R_Y_OFF = 0;
    static final int G_Y_OFF = (1 * MAXJSAMPLE); // offset to G => Y section
    static final int B_Y_OFF = (2 * MAXJSAMPLE); // etc.
    static final int R_CB_OFF = (3 * MAXJSAMPLE);
    static final int G_CB_OFF = (4 * MAXJSAMPLE);
    static final int B_CB_OFF = (5 * MAXJSAMPLE);
    static final int R_CR_OFF = B_CB_OFF; // B=>Cb, R=>Cr are the same
    static final int G_CR_OFF = (6 * MAXJSAMPLE);
    static final int B_CR_OFF = (7 * MAXJSAMPLE);
    static final int TABLE_SIZE = (8 * MAXJSAMPLE);

    public byte[] encode(final int imageData[], final int width, final int height,
            final short quality)
    {
        if (mDebug)
            DebugUtil.Log("Starting JPEG encoder, input in ARGB8888 int[] format, size " + width
                    + "x" + height + ", quality " + quality);

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();

            cInfo = new JpegCompressInfo(imageData, width, height, quality); // after
                                                                             // this,
                                                                             // cInfo
                                                                             // is
                                                                             // properly
                                                                             // initialized
            dct = new DCT();
            quant = new Quantize(cInfo.quality);
            huffman = new Huffman(cInfo);

            JpegHeader.WriteHeaders(cInfo, quant, huffman, outputStream);
            encodeJPEGImageDataFP(imageData, outputStream);
            JpegHeader.WriteEndOfImage(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            if (mDebug)
                DebugUtil.Log("Caught exception while encoding: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public byte[] encode16(final short imageData[], final int width, final int height,
            final short quality)
    {
        if (mDebug)
            DebugUtil.Log("Starting JPEG encoder, input in ARGB8888 int[] format, size " + width
                    + "x" + height + ", quality " + quality);

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();

            cInfo = new JpegCompressInfo(imageData, width, height, quality); // after
                                                                             // this,
                                                                             // cInfo
                                                                             // is
                                                                             // properly
                                                                             // initialized
            dct = new DCT();
            quant = new Quantize(cInfo.quality);
            huffman = new Huffman(cInfo);

            JpegHeader.WriteHeaders(cInfo, quant, huffman, outputStream);
            encodeJPEGImageDataFP16bits(imageData, outputStream);
            JpegHeader.WriteEndOfImage(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            if (mDebug)
                DebugUtil.Log("Caught exception while encoding: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void deinit() {
        cInfo = null;
        dct = null;
        quant = null;
        huffman = null;
    }

    // Main encoding routine FIXED POINT VERSION
    private void encodeJPEGImageDataFP(final int imageData[], OutputStream outStream) {

        // we hold all temporary buffers in here so that no extra allocations
        // are needed during processing
        int dctBuffer[][] = new int[cInfo.DCTSIZE][cInfo.DCTSIZE]; // result of
                                                                   // DCT
                                                                   // forward
                                                                   // encoding
        int quantBuffer[] = new int[cInfo.DCTSIZE * cInfo.DCTSIZE]; // result of
                                                                    // quantized
                                                                    // dctBuffer
        int previousDC[] = new int[cInfo.num_components]; // Y Cr Cb

        // index variables for loops
        short MCUrowCounter, blockNo, YCCcomp, i, j;

        // array for a single YCrCb MCU row of 16 x 16 Blocks pixels
        short[][][] MCUrow = new short[cInfo.num_components][cInfo.width_in_cosine_domain][cInfo.DCTSIZE
                * cInfo.max_h_samp_factor];
        short[][] MCUrowSingleComponent; // "pointer" to a single Y/Cr/Cb
                                         // component of MCUrow

        initFastRGB2YUV();

        // Let's go!
        for (MCUrowCounter = 0; MCUrowCounter < cInfo.MCU_row_count; MCUrowCounter++) { // for
                                                                                        // each
                                                                                        // MCU
                                                                                        // block
                                                                                        // row
            if (mDebugVerbose)
                DebugUtil.Log("Starting encoding on MCU row " + MCUrowCounter * cInfo.DCTSIZE
                        * cInfo.max_h_samp_factor);

            readSingleMCURow(MCUrowCounter, imageData, MCUrow); // read MCU row,
                                                                // returns data
                                                                // in YCbCr
                                                                // format

            // hardcoded 4:2:0 downsampling for Cb and Cr components, overwrites
            // input with downsampled values
            downsampleBy2(cInfo.width_in_cosine_domain / 2, cInfo.DCTSIZE, MCUrow[1], MCUrow[1]);
            downsampleBy2(cInfo.width_in_cosine_domain / 2, cInfo.DCTSIZE, MCUrow[2], MCUrow[2]);

            for (blockNo = 0; blockNo < cInfo.MCUs_per_row; blockNo++) { // for
                                                                         // each
                                                                         // MCU
                                                                         // block
                                                                         // in
                                                                         // MCU
                                                                         // block
                                                                         // row

                for (YCCcomp = 0; YCCcomp < cInfo.num_components; YCCcomp++) { // for
                                                                               // each
                                                                               // YCbCr
                                                                               // component
                                                                               // in
                                                                               // MCU
                                                                               // block
                    MCUrowSingleComponent = MCUrow[YCCcomp];

                    for (i = 0; i < cInfo.comp_info[YCCcomp].h_samp_factor; i++) { // i&j:
                                                                                   // for
                                                                                   // each
                                                                                   // DCT
                                                                                   // block
                                                                                   // inside
                                                                                   // MCU
                                                                                   // block
                        for (j = 0; j < cInfo.comp_info[YCCcomp].v_samp_factor; j++) {

                            if (mDebugVerbose)
                                DebugUtil.Log("Processing component " + YCCcomp + ", MCU #"
                                        + blockNo + ", subblock (" + j + "," + i + ")");

                            // per-block operations: YCC input -> DCT ->
                            // quantize -> ZigZag -> Huffman -> file output
                            dct.forwardDCT(MCUrowSingleComponent, dctBuffer, j * cInfo.DCTSIZE
                                    + blockNo * cInfo.comp_info[YCCcomp].h_samp_factor
                                    * cInfo.DCTSIZE, i * cInfo.DCTSIZE, cInfo.MCUs_per_row
                                    * cInfo.DCTSIZE * cInfo.max_h_samp_factor); // DCT
                                                                                // encoding,
                                                                                // save
                                                                                // result
                                                                                // in
                                                                                // dctArray
                            quant.quantizeBlock(dctBuffer, cInfo.comp_info[YCCcomp].quant_tbl_no,
                                    quantBuffer); // quantize, save result in
                                                  // quantArray
                            huffman.EncodeBlock(outStream, quantBuffer, previousDC[YCCcomp],
                                    cInfo.comp_info[YCCcomp].dc_tbl_no,
                                    cInfo.comp_info[YCCcomp].ac_tbl_no); // Huffman
                                                                         // encoding
                            previousDC[YCCcomp] = quantBuffer[0]; // DC
                                                                  // component
                                                                  // is needed
                                                                  // on the next
                                                                  // encoding
                                                                  // round
                        }
                    }
                }
            }
        }

        huffman.flushBuffer(outStream);
    }

    //support 16bits
    
    private void encodeJPEGImageDataFP16bits(final short imageData[], OutputStream outStream) {

        // we hold all temporary buffers in here so that no extra allocations
        // are needed during processing
        int dctBuffer[][] = new int[cInfo.DCTSIZE][cInfo.DCTSIZE]; // result of
                                                                   // DCT
                                                                   // forward
                                                                   // encoding
        int quantBuffer[] = new int[cInfo.DCTSIZE * cInfo.DCTSIZE]; // result of
                                                                    // quantized
                                                                    // dctBuffer
        int previousDC[] = new int[cInfo.num_components]; // Y Cr Cb

        // index variables for loops
        short MCUrowCounter, blockNo, YCCcomp, i, j;

        // array for a single YCrCb MCU row of 16 x 16 Blocks pixels
        short[][][] MCUrow = new short[cInfo.num_components][cInfo.width_in_cosine_domain][cInfo.DCTSIZE
                * cInfo.max_h_samp_factor];
        short[][] MCUrowSingleComponent; // "pointer" to a single Y/Cr/Cb
                                         // component of MCUrow

        initFastRGB2YUV();

        // Let's go!
        for (MCUrowCounter = 0; MCUrowCounter < cInfo.MCU_row_count; MCUrowCounter++) { // for
                                                                                        // each
                                                                                        // MCU
                                                                                        // block
                                                                                        // row
            if (mDebugVerbose)
                DebugUtil.Log("Starting encoding on MCU row " + MCUrowCounter * cInfo.DCTSIZE
                        * cInfo.max_h_samp_factor);

            readSingleMCURow16bits(MCUrowCounter, imageData, MCUrow); // read MCU row,
                                                                // returns data
                                                                // in YCbCr
                                                                // format

            // hardcoded 4:2:0 downsampling for Cb and Cr components, overwrites
            // input with downsampled values
            downsampleBy2(cInfo.width_in_cosine_domain / 2, cInfo.DCTSIZE, MCUrow[1], MCUrow[1]);
            downsampleBy2(cInfo.width_in_cosine_domain / 2, cInfo.DCTSIZE, MCUrow[2], MCUrow[2]);

            for (blockNo = 0; blockNo < cInfo.MCUs_per_row; blockNo++) { // for
                                                                         // each
                                                                         // MCU
                                                                         // block
                                                                         // in
                                                                         // MCU
                                                                         // block
                                                                         // row

                for (YCCcomp = 0; YCCcomp < cInfo.num_components; YCCcomp++) { // for
                                                                               // each
                                                                               // YCbCr
                                                                               // component
                                                                               // in
                                                                               // MCU
                                                                               // block
                    MCUrowSingleComponent = MCUrow[YCCcomp];

                    for (i = 0; i < cInfo.comp_info[YCCcomp].h_samp_factor; i++) { // i&j:
                                                                                   // for
                                                                                   // each
                                                                                   // DCT
                                                                                   // block
                                                                                   // inside
                                                                                   // MCU
                                                                                   // block
                        for (j = 0; j < cInfo.comp_info[YCCcomp].v_samp_factor; j++) {

                            if (mDebugVerbose)
                                DebugUtil.Log("Processing component " + YCCcomp + ", MCU #"
                                        + blockNo + ", subblock (" + j + "," + i + ")");

                            // per-block operations: YCC input -> DCT ->
                            // quantize -> ZigZag -> Huffman -> file output
                            dct.forwardDCT(MCUrowSingleComponent, dctBuffer, j * cInfo.DCTSIZE
                                    + blockNo * cInfo.comp_info[YCCcomp].h_samp_factor
                                    * cInfo.DCTSIZE, i * cInfo.DCTSIZE, cInfo.MCUs_per_row
                                    * cInfo.DCTSIZE * cInfo.max_h_samp_factor); // DCT
                                                                                // encoding,
                                                                                // save
                                                                                // result
                                                                                // in
                                                                                // dctArray
                            quant.quantizeBlock(dctBuffer, cInfo.comp_info[YCCcomp].quant_tbl_no,
                                    quantBuffer); // quantize, save result in
                                                  // quantArray
                            huffman.EncodeBlock(outStream, quantBuffer, previousDC[YCCcomp],
                                    cInfo.comp_info[YCCcomp].dc_tbl_no,
                                    cInfo.comp_info[YCCcomp].ac_tbl_no); // Huffman
                                                                         // encoding
                            previousDC[YCCcomp] = quantBuffer[0]; // DC
                                                                  // component
                                                                  // is needed
                                                                  // on the next
                                                                  // encoding
                                                                  // round
                        }
                    }
                }
            }
        }

        huffman.flushBuffer(outStream);
    }

    //
    // downsample with 1:2 ratio in both dimensions
    // outData needs to be 2*inData in dimensions, otherwise weird things will
    // happen
    private void downsampleBy2(int targetX, int targetY, float inData[][], float outData[][]) {

        int inX = 0, inY = 0, outX = 0, outY = 0;

        if (mDebugVerbose)
            DebugUtil.Log("Downscaling to " + targetX + "x" + targetY);

        for (outY = 0; outY < targetY; outY++, inY += 2) {
            for (outX = 0, inX = 0; outX < targetX; outX++, inX += 2) {
                outData[outX][outY] = ((inData[inX][inY] + inData[inX][inY + 1]
                        + inData[inX + 1][inY] + inData[inX + 1][inY + 1] + 2) / 4);
            }
        }
    }

    // the dummiest way to downscale...
    private void downsampleBy2(int targetX, int targetY, short inData[][], short outData[][]) {

        int inX = 0, inY = 0, outX = 0, outY = 0;

        if (mDebugVerbose)
            DebugUtil.Log("Downscaling to " + targetX + "x" + targetY);

        for (outY = 0; outY < targetY; outY++, inY += 2) {
            for (outX = 0, inX = 0; outX < targetX; outX++, inX += 2) {
                outData[outX][outY] = (short) ((inData[inX][inY] + inData[inX][inY + 1]
                        + inData[inX + 1][inY] + inData[inX + 1][inY + 1] + 2) / 4);
            }
        }
    }

    // FIXED POINT VERSION
    private void readSingleMCURow(final int lineIndex, final int imageData[], short output[][][]) {
        final int ROWS_IN_MCU = 16;
        int i, j;
        int r, g, b;
        int index = lineIndex * ROWS_IN_MCU * cInfo.image_width;
        int line = 0;
        int linesToRead = ROWS_IN_MCU;
        boolean clampX = false, clampY = false;

        if (mDebugVerbose)
            DebugUtil
                    .Log("Decoding ARGB8888 MCU row " + lineIndex + ", width " + cInfo.image_width);

        // if image height is not % 16, we need to adjust the number of rows we
        // read into the last MCUrow
        if (((lineIndex * ROWS_IN_MCU) + ROWS_IN_MCU) > cInfo.image_height) {
            linesToRead = cInfo.image_height - lineIndex * ROWS_IN_MCU;
            if (mDebug)
                DebugUtil.Log("On last MCU block row, adjusting to " + linesToRead + " lines");
            clampY = true;
        }

        if (cInfo.image_width < cInfo.width_in_cosine_domain) {
            clampX = true;
        }

        for (j = lineIndex * ROWS_IN_MCU; j < (lineIndex * ROWS_IN_MCU) + linesToRead; ++j) { // y
            for (i = 0; i < cInfo.image_width; ++i) { // x
                // read RGB components from ARGB8888 int, discard alpha channel
                r = ((imageData[index] >> 16) & 0xff);
                g = ((imageData[index] >> 8) & 0xff);
                b = (imageData[index] & 0xff);

                output[CS_Y][i][line] = (short) ((rgb_ycc_tab[r + R_Y_OFF]
                        + rgb_ycc_tab[g + G_Y_OFF] + rgb_ycc_tab[b + B_Y_OFF]) >> SCALEBITS);
                output[CS_Cb][i][line] = (short) ((rgb_ycc_tab[r + R_CB_OFF]
                        + rgb_ycc_tab[g + G_CB_OFF] + rgb_ycc_tab[b + B_CB_OFF]) >> SCALEBITS);
                output[CS_Cr][i][line] = (short) ((rgb_ycc_tab[r + R_CR_OFF]
                        + rgb_ycc_tab[g + G_CR_OFF] + rgb_ycc_tab[b + B_CR_OFF]) >> SCALEBITS);

                index++;
            }
            line++;
        }

        if (clampY) {
            for (j = line; j < ROWS_IN_MCU; j++) {
                if (mDebugVerbose)
                    DebugUtil.Log("Clamping Y line " + j + " to values from MCU line " + line
                            + " until line " + cInfo.height_in_cosine_domain);
                for (i = 0; i < cInfo.image_width; ++i) { // x
                    output[CS_Y][i][j] = output[CS_Y][i][line];
                    output[CS_Cb][i][j] = output[CS_Cb][i][line];
                    output[CS_Cr][i][j] = output[CS_Cr][i][line];
                }
            }
        }

        if (clampX) {
            if (mDebugVerbose)
                DebugUtil.Log("Clamping X column from " + cInfo.image_width + " to "
                        + cInfo.width_in_cosine_domain);
            for (j = 0; j < ROWS_IN_MCU; j++) {
                for (i = cInfo.image_width; i < cInfo.width_in_cosine_domain; i++) {
                    output[CS_Y][i][j] = output[CS_Y][cInfo.image_width - 1][j];
                    output[CS_Cb][i][j] = output[CS_Cb][cInfo.image_width - 1][j];
                    output[CS_Cr][i][j] = output[CS_Cr][cInfo.image_width - 1][j];
                }
            }
        }

    }

    //for 16bits
    private void readSingleMCURow16bits(final int lineIndex, final short imageData[], short output[][][]) {
        final int ROWS_IN_MCU = 16;
        int i, j;
        int r, g, b;
        int index = lineIndex * ROWS_IN_MCU * cInfo.image_width;
        int line = 0;
        int linesToRead = ROWS_IN_MCU;
        boolean clampX = false, clampY = false;

        if (mDebugVerbose)
            DebugUtil
                    .Log("Decoding ARGB8888 MCU row " + lineIndex + ", width " + cInfo.image_width);

        // if image height is not % 16, we need to adjust the number of rows we
        // read into the last MCUrow
        if (((lineIndex * ROWS_IN_MCU) + ROWS_IN_MCU) > cInfo.image_height) {
            linesToRead = cInfo.image_height - lineIndex * ROWS_IN_MCU;
            if (mDebug)
                DebugUtil.Log("On last MCU block row, adjusting to " + linesToRead + " lines");
            clampY = true;
        }

        if (cInfo.image_width < cInfo.width_in_cosine_domain) {
            clampX = true;
        }

        for (j = lineIndex * ROWS_IN_MCU; j < (lineIndex * ROWS_IN_MCU) + linesToRead; ++j) { // y
            for (i = 0; i < cInfo.image_width; ++i) { // x
                // read RGB components from RGB565 int, discard alpha channel
            	r = ((imageData[index] >> 11) & 0x1f);
		        g = ((imageData[index] >> 5) & 0x3f);
		        b = (imageData[index] & 0x1f);
	
		        r = (r << 3) | ( r >> 2); 
		        g = (g << 2) | ( g >> 4); 
		        b = (b << 3) | ( b >> 2);

                output[CS_Y][i][line] = (short) ((rgb_ycc_tab[r + R_Y_OFF]
                        + rgb_ycc_tab[g + G_Y_OFF] + rgb_ycc_tab[b + B_Y_OFF]) >> SCALEBITS);
                output[CS_Cb][i][line] = (short) ((rgb_ycc_tab[r + R_CB_OFF]
                        + rgb_ycc_tab[g + G_CB_OFF] + rgb_ycc_tab[b + B_CB_OFF]) >> SCALEBITS);
                output[CS_Cr][i][line] = (short) ((rgb_ycc_tab[r + R_CR_OFF]
                        + rgb_ycc_tab[g + G_CR_OFF] + rgb_ycc_tab[b + B_CR_OFF]) >> SCALEBITS);

                index++;
            }
            line++;
        }

        if (clampY) {
            for (j = line; j < ROWS_IN_MCU; j++) {
                if (mDebugVerbose)
                    DebugUtil.Log("Clamping Y line " + j + " to values from MCU line " + line
                            + " until line " + cInfo.height_in_cosine_domain);
                for (i = 0; i < cInfo.image_width; ++i) { // x
                    output[CS_Y][i][j] = output[CS_Y][i][line];
                    output[CS_Cb][i][j] = output[CS_Cb][i][line];
                    output[CS_Cr][i][j] = output[CS_Cr][i][line];
                }
            }
        }

        if (clampX) {
            if (mDebugVerbose)
                DebugUtil.Log("Clamping X column from " + cInfo.image_width + " to "
                        + cInfo.width_in_cosine_domain);
            for (j = 0; j < ROWS_IN_MCU; j++) {
                for (i = cInfo.image_width; i < cInfo.width_in_cosine_domain; i++) {
                    output[CS_Y][i][j] = output[CS_Y][cInfo.image_width - 1][j];
                    output[CS_Cb][i][j] = output[CS_Cb][cInfo.image_width - 1][j];
                    output[CS_Cr][i][j] = output[CS_Cr][cInfo.image_width - 1][j];
                }
            }
        }

    }

    // source: jccolor.c / rgb_ycc_start()
    private void initFastRGB2YUV() {
        rgb_ycc_tab = new int[TABLE_SIZE];

        for (int i = 0; i < MAXJSAMPLE; i++) {
            rgb_ycc_tab[i + R_Y_OFF] = 19595 * i;
            rgb_ycc_tab[i + G_Y_OFF] = 38470 * i;
            rgb_ycc_tab[i + B_Y_OFF] = 7471 * i + ONE_HALF;
            rgb_ycc_tab[i + R_CB_OFF] = -11059 * i;
            rgb_ycc_tab[i + G_CB_OFF] = -21710 * i;
            rgb_ycc_tab[i + B_CB_OFF] = 32768 * i + ONE_HALF * MAXJSAMPLE;
            rgb_ycc_tab[i + G_CR_OFF] = -27439 * i;
            rgb_ycc_tab[i + B_CR_OFF] = -5329 * i;
        }
    }

    /*
     * RGB565 data access patterns r = ((imageData[index] >> 11) & 0x1f); g =
     * ((imageData[index] >> 5) & 0x3f); b = (imageData[index] & 0x1f); //
     * convert from 5 bits to 8 bits r = (r << 3) | ( r >> 2); g = (g << 2) | (
     * g >> 4); b = (b << 3) | ( b >> 2);
     */

}
