/*
 *  JPEG file header functions
 */

package com.pictelligent.s40.camera5in1.JPEGEncoder;

import java.io.IOException;
import java.io.OutputStream;

public class JpegHeader {

    private static boolean mDebug = false;
    private static boolean mDebugVerbose = false;

    public static void WriteEndOfImage(OutputStream out) {
        byte[] EndOfImage = {
                (byte) 0xFF, (byte) 0xD9
        };
        Write2Bytes(EndOfImage, out);
    }

    public static void WriteHeaders(JpegCompressInfo cInfo, Quantize quant, Huffman huffman,
            OutputStream out) {

        if (mDebugVerbose)
            System.out.println("Starting JPEG header writing");

        WriteSOI(out);
        WriteJFIF(out);
        WriteComment(cInfo.comment, out);
        WriteQuantTables(quant, out);
        WriteSOF(cInfo, out);
        if (mDebug)
            System.out.println("JPEG resolution as saved in header data: " + cInfo.image_width
                    + "x" + cInfo.image_height);
        WriteHuffmanTables(huffman, out);
        WriteSOS(cInfo, out);
    }

    private static void WriteSOI(OutputStream out) {
        byte[] SOI = {
                (byte) 0xFF, (byte) 0xD8
        };
        Write2Bytes(SOI, out);
    }

    private static void WriteJFIF(OutputStream out) {
        byte JFIF[] = new byte[18];
        JFIF[0] = (byte) 0xFF; // APP0 (0xFFE0)
        JFIF[1] = (byte) 0xE0; // APP0
        JFIF[2] = (byte) 0x00; // len
        JFIF[3] = (byte) 0x10; // len
        JFIF[4] = (byte) 0x4A; // J
        JFIF[5] = (byte) 0x46; // F
        JFIF[6] = (byte) 0x49; // I
        JFIF[7] = (byte) 0x46; // F
        JFIF[8] = (byte) 0x00;
        JFIF[9] = (byte) 0x01;
        JFIF[10] = (byte) 0x00;
        JFIF[11] = (byte) 0x00;
        JFIF[12] = (byte) 0x00;
        JFIF[13] = (byte) 0x01;
        JFIF[14] = (byte) 0x00;
        JFIF[15] = (byte) 0x01;
        JFIF[16] = (byte) 0x00;
        JFIF[17] = (byte) 0x00;
        WriteData(JFIF, out);
    }

    private static void WriteComment(String comment, OutputStream out) {
        int commentLength = comment.length();
        byte COM[] = new byte[commentLength + 4];
        COM[0] = (byte) 0xFF; // 0xFFFE
        COM[1] = (byte) 0xFE;
        COM[2] = (byte) (((commentLength + 2) >> 8) & 0xFF);
        COM[3] = (byte) ((commentLength + 2) & 0xFF);
        java.lang.System.arraycopy(comment.getBytes(), 0, COM, 4, commentLength);
        WriteData(COM, out);
    }

    private static void WriteQuantTables(Quantize quant, OutputStream out) {
        int offset = 4;
        int i, j;
        int tempArray[];
        byte DQT[] = new byte[4 + 2 * (1 + 64) + 1];

        DQT[0] = (byte) 0xFF; // 0xFFDB
        DQT[1] = (byte) 0xDB;
        DQT[2] = (byte) 0x00;
        DQT[3] = (byte) 0x84; // 132 bytes

        for (i = 0; i < 2; i++) {
            DQT[offset++] = (byte) ((0 << 4) + i);
            tempArray = (int[]) quant.tables[i];
            for (j = 0; j < 64; j++) {
                DQT[offset++] = (byte) tempArray[Constants.jpeg_natural_order[j]]; // zigzag
            }
        }
        WriteData(DQT, out);
    }

    private static void WriteSOF(JpegCompressInfo cInfo, OutputStream out) {
        int i;
        int index = 10;
        byte SOF[] = new byte[19];

        SOF[0] = (byte) 0xFF;
        SOF[1] = (byte) 0xC0;
        SOF[2] = (byte) 0x00;
        SOF[3] = (byte) 0x11; // 17 bytes
        SOF[4] = (byte) cInfo.data_precision;
        SOF[5] = (byte) ((cInfo.image_height >> 8) & 0xFF);
        SOF[6] = (byte) ((cInfo.image_height) & 0xFF);
        SOF[7] = (byte) ((cInfo.image_width >> 8) & 0xFF);
        SOF[8] = (byte) ((cInfo.image_width) & 0xFF);
        SOF[9] = (byte) cInfo.num_components;

        for (i = 0; i < cInfo.num_components; i++) {
            SOF[index++] = (byte) cInfo.comp_info[i].component_id;
            SOF[index++] = (byte) ((cInfo.comp_info[i].h_samp_factor << 4) + cInfo.comp_info[i].v_samp_factor);
            SOF[index++] = (byte) cInfo.comp_info[i].quant_tbl_no;
        }
        WriteData(SOF, out);
    }

    // TODO: cleanup
    private static void WriteHuffmanTables(Huffman huffman, OutputStream out) {
        int i, j, temp, bytes, index, oldindex, tempindex;
        byte DHT1[], DHT2[], DHT3[], DHT4[];

        index = 4;
        oldindex = 4;
        DHT1 = new byte[17];
        DHT4 = new byte[4];
        DHT4[0] = (byte) 0xFF;
        DHT4[1] = (byte) 0xC4;

        for (i = 0; i < 4; i++) {
            bytes = 0;
            DHT1[index++ - oldindex] = (byte) ((int[]) huffman.bits.elementAt(i))[0];

            for (j = 1; j < 17; j++) {
                temp = ((int[]) huffman.bits.elementAt(i))[j];
                DHT1[index++ - oldindex] = (byte) temp;
                bytes += temp;
            }

            tempindex = index;
            DHT2 = new byte[bytes];
            for (j = 0; j < bytes; j++) {
                DHT2[index++ - tempindex] = (byte) ((int[]) huffman.val.elementAt(i))[j];
            }

            DHT3 = new byte[index];
            java.lang.System.arraycopy(DHT4, 0, DHT3, 0, oldindex);
            java.lang.System.arraycopy(DHT1, 0, DHT3, oldindex, 17);
            java.lang.System.arraycopy(DHT2, 0, DHT3, oldindex + 17, bytes);
            DHT4 = DHT3;
            oldindex = index;
        }
        DHT4[2] = (byte) (((index - 2) >> 8) & 0xFF);
        DHT4[3] = (byte) ((index - 2) & 0xFF);
        WriteData(DHT4, out);
    }

    private static void WriteSOS(JpegCompressInfo cInfo, OutputStream out) {
        int i, index;
        byte SOS[] = new byte[14];

        SOS[0] = (byte) 0xFF;
        SOS[1] = (byte) 0xDA;
        SOS[2] = (byte) 0x00;
        SOS[3] = (byte) 12;
        SOS[4] = (byte) cInfo.num_components;

        index = 5;
        for (i = 0; i < cInfo.num_components; i++) {
            SOS[index++] = (byte) cInfo.comp_info[i].component_id;
            SOS[index++] = (byte) ((cInfo.comp_info[i].dc_tbl_no << 4) + cInfo.comp_info[i].ac_tbl_no);
        }

        SOS[index++] = 0;
        SOS[index++] = (byte) (cInfo.DCTSIZE * cInfo.DCTSIZE - 1);
        SOS[index++] = 0;

        WriteData(SOS, out);
    }

    // write 2 bytes into JPEG output stream
    private static void Write2Bytes(byte[] in, OutputStream out) {

        try {
            out.write(in, 0, 2);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        }
    }

    // write input array into JPEG output stream
    // input data needs to be in correct format, i.e contain length information
    private static void WriteData(byte[] in, OutputStream out) {

        int len;

        try {
            len = (((int) (in[2] & 0xFF)) << 8) + (int) (in[3] & 0xFF) + 2; // length
                                                                            // is
                                                                            // stored
                                                                            // in
                                                                            // bytes
                                                                            // 2
                                                                            // and
                                                                            // 3
            if (mDebugVerbose)
                System.out.println("Writing data array of " + len + " bytes");
            out.write(in, 0, len);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        }
    }

}
