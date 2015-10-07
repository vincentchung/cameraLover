/*
 * JPEG encoder algorithm for J2ME
 * 
 * (c) Pictelligent 2012, All rights reserved 
 * 
 * Based on Independent JPEG group's JPEG algorithm http://www.ijg.org/
 * 
 * This encoding algorithm makes several (dangerous) assumptions:
 *   - input buffer must have three color components in RGB
 *
 * No animals were harmed in making of this fine piece of source code. Humans... well, that's a different story
 *   
 */

package com.pictelligent.s40.camera5in1.JPEGEncoder;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Vector;
import com.pictelligent.s40.camera5in1.DebugUtil;

class Huffman {
    int bufferPutBits, bufferPutBuffer;
    public int DC_matrix0[][];
    public int AC_matrix0[][];
    public int DC_matrix1[][];
    public int AC_matrix1[][];
    public Object DC_matrix[];
    public Object AC_matrix[];
    public int code;
    public int NumOfDCTables;
    public int NumOfACTables;
    public Vector bits;
    public Vector val;

    public Huffman(JpegCompressInfo cInfo) {

        if (JpegEncoder.mDebug)
            DebugUtil.Log("Initializing Huffman");

        bits = new Vector();
        bits.addElement(Constants.bits_dc_luminance);
        bits.addElement(Constants.bits_ac_luminance);
        bits.addElement(Constants.bits_dc_chrominance);
        bits.addElement(Constants.bits_ac_chrominance);
        val = new Vector();
        val.addElement(Constants.val_dc_luminance);
        val.addElement(Constants.val_ac_luminance);
        val.addElement(Constants.val_dc_chrominance);
        val.addElement(Constants.val_ac_chrominance);
        initHuffman();
    }

    private void initHuffman() {

        DC_matrix0 = new int[12][2];
        DC_matrix1 = new int[12][2];
        AC_matrix0 = new int[255][2];
        AC_matrix1 = new int[255][2];
        DC_matrix = new Object[2];
        AC_matrix = new Object[2];
        int p, l, i, lastp, si, code;
        int[] huffsize = new int[257];
        int[] huffcode = new int[257];

        /*
         * init of the DC values for the chrominance [][0] is the code [][1] is
         * the number of bit
         */

        p = 0;
        for (l = 1; l <= 16; l++) {
            for (i = 1; i <= Constants.bits_dc_chrominance[l]; i++) {
                huffsize[p++] = l;
            }
        }

        huffsize[p] = 0;
        lastp = p;
        code = 0;
        si = huffsize[0];
        p = 0;

        while (huffsize[p] != 0) {
            while (huffsize[p] == si) {
                huffcode[p++] = code;
                code++;
            }
            code <<= 1;
            si++;
        }

        for (p = 0; p < lastp; p++) {
            DC_matrix1[Constants.val_dc_chrominance[p]][0] = huffcode[p];
            DC_matrix1[Constants.val_dc_chrominance[p]][1] = huffsize[p];
        }

        /*
         * Init of the AC hufmann code for the chrominance matrix [][][0] is the
         * code & matrix[][][1] is the number of bit needed
         */

        p = 0;
        for (l = 1; l <= 16; l++) {
            for (i = 1; i <= Constants.bits_ac_chrominance[l]; i++) {
                huffsize[p++] = l;
            }
        }

        huffsize[p] = 0;
        lastp = p;
        code = 0;
        si = huffsize[0];
        p = 0;

        while (huffsize[p] != 0) {
            while (huffsize[p] == si) {
                huffcode[p++] = code;
                code++;
            }
            code <<= 1;
            si++;
        }

        for (p = 0; p < lastp; p++) {
            AC_matrix1[Constants.val_ac_chrominance[p]][0] = huffcode[p];
            AC_matrix1[Constants.val_ac_chrominance[p]][1] = huffsize[p];
        }

        /*
         * init of the DC values for the luminance [][0] is the code [][1] is
         * the number of bit
         */
        p = 0;
        for (l = 1; l <= 16; l++) {
            for (i = 1; i <= Constants.bits_dc_luminance[l]; i++) {
                huffsize[p++] = l;
            }
        }

        huffsize[p] = 0;
        lastp = p;
        code = 0;
        si = huffsize[0];
        p = 0;

        while (huffsize[p] != 0) {
            while (huffsize[p] == si) {
                huffcode[p++] = code;
                code++;
            }
            code <<= 1;
            si++;
        }

        for (p = 0; p < lastp; p++) {
            DC_matrix0[Constants.val_dc_luminance[p]][0] = huffcode[p];
            DC_matrix0[Constants.val_dc_luminance[p]][1] = huffsize[p];
        }

        /*
         * Init of the AC hufmann code for luminance matrix [][][0] is the code
         * & matrix[][][1] is the number of bit
         */

        p = 0;
        for (l = 1; l <= 16; l++) {
            for (i = 1; i <= Constants.bits_ac_luminance[l]; i++) {
                huffsize[p++] = l;
            }
        }

        huffsize[p] = 0;
        lastp = p;
        code = 0;
        si = huffsize[0];
        p = 0;

        while (huffsize[p] != 0) {
            while (huffsize[p] == si) {
                huffcode[p++] = code;
                code++;
            }
            code <<= 1;
            si++;
        }
        for (int q = 0; q < lastp; q++) {
            AC_matrix0[Constants.val_ac_luminance[q]][0] = huffcode[q];
            AC_matrix0[Constants.val_ac_luminance[q]][1] = huffsize[q];
        }

        DC_matrix[0] = DC_matrix0;
        DC_matrix[1] = DC_matrix1;
        AC_matrix[0] = AC_matrix0;
        AC_matrix[1] = AC_matrix1;
    }

    // source: jcbuff.c encode_MCU_dc_first
    public void EncodeBlock(OutputStream out, int inputData[], int previousVal, int DCcode,
            int ACcode) {
        int temp, temp2, nbits, k, r, i;

        NumOfDCTables = 2;
        NumOfACTables = 2;

        // DC
        temp = temp2 = inputData[0] - previousVal;

        /* Encode the DC coefficient difference per section G.1.2.1 */

        if (temp < 0) {
            temp = -temp;
            /*
             * For a negative input, want temp2 = bitwise complement of
             * abs(input)
             */
            /* This code assumes we are on a two's complement machine */
            temp2--;
        }

        /* Find the number of bits needed for the magnitude of the coefficient */
        nbits = 0;
        while (temp != 0) {
            nbits++;
            temp >>= 1;
        }
        /* Count/emit the Huffman-coded symbol for the number of bits */
        emitData(out, ((int[][]) DC_matrix[DCcode])[nbits][0],
                ((int[][]) DC_matrix[DCcode])[nbits][1]);

        /* Emit that number of bits of the value, if positive, */
        /* or the complement of its magnitude, if negative. */
        if (nbits != 0) {
            emitData(out, temp2, nbits);
        }

        // AC
        r = 0;
        for (k = 1; k < 64; k++) {
            if ((temp = inputData[Constants.jpeg_natural_order[k]]) == 0) { // NOTE:
                                                                            // zigzag
                                                                            // reordering
                                                                            // in
                                                                            // here!
                r++;
            }
            else {
                while (r > 15) {
                    emitData(out, ((int[][]) AC_matrix[ACcode])[0xF0][0],
                            ((int[][]) AC_matrix[ACcode])[0xF0][1]);
                    r -= 16;
                }
                temp2 = temp;
                if (temp < 0) {
                    temp = -temp;
                    temp2--;
                }
                nbits = 1;
                while ((temp >>= 1) != 0) {
                    nbits++;
                }
                i = (r << 4) + nbits;
                emitData(out, ((int[][]) AC_matrix[ACcode])[i][0],
                        ((int[][]) AC_matrix[ACcode])[i][1]);
                emitData(out, temp2, nbits);

                r = 0;
            }
        }

        if (r > 0) {
            emitData(out, ((int[][]) AC_matrix[ACcode])[0][0], ((int[][]) AC_matrix[ACcode])[0][1]);
        }

    }

    void emitData(OutputStream out, int code, int size) {

        int PutBuffer = code;
        int PutBits = bufferPutBits;

        PutBuffer &= (1 << size) - 1;
        PutBits += size;
        PutBuffer <<= 24 - PutBits;
        PutBuffer |= bufferPutBuffer;

        while (PutBits >= 8) {
            int c = ((PutBuffer >> 16) & 0xFF);
            try {
                out.write(c);

                if (c == 0xFF) {
                    out.write(0);

                }
            } catch (IOException e) {
                if (JpegEncoder.mDebug)
                    DebugUtil.Log("IOException while Huffman coding: " + e.getMessage());
                System.out.println("IOException " + e.getMessage());
            }
            PutBuffer <<= 8;
            PutBits -= 8;
        }
        bufferPutBuffer = PutBuffer;
        bufferPutBits = PutBits;

    }

    void flushBuffer(OutputStream outStream) {
        int PutBuffer = bufferPutBuffer;
        int PutBits = bufferPutBits;
        int c;

        try {
            while (PutBits >= 8) {
                c = ((PutBuffer >> 16) & 0xFF);
                outStream.write(c);

                if (c == 0xFF)
                    outStream.write(0);

                PutBuffer <<= 8;
                PutBits -= 8;
            }
            if (PutBits > 0) {
                c = ((PutBuffer >> 16) & 0xFF);
                outStream.write(c);
            }
        } catch (IOException e) {
            if (JpegEncoder.mDebug)
                DebugUtil.Log("IOException while Huffman coding: " + e.getMessage());
            System.out.println("IOException " + e.getMessage());
        }
    }

}
