
package com.pictelligent.s40.camera5in1.JPEGEncoder;

// Quantization routines
// source: libjpeg jcdctmgr.c

public class Quantize {

    public int luminance_quant_tbl[];
    public int chrominance_quant_tbl[];
    public Object tables[] = new Object[2]; // public because JPEG header writer
                                            // needs access

    public Quantize(int quality) {

        if (JpegEncoder.mDebug)
            System.out.println("Initializing quantization tables to quality level " + quality);

        int i;
        int temp;
        int qualityLevel = quality;

        luminance_quant_tbl = new int[64];
        chrominance_quant_tbl = new int[64];

        qualityLevel = quality;

        // calculate quality level as defined in JPEG standard
        if (qualityLevel <= 0)
            qualityLevel = 1;
        else if (qualityLevel > 100)
            qualityLevel = 100;
        else if (qualityLevel < 50)
            qualityLevel = 5000 / qualityLevel;
        else
            qualityLevel = 200 - qualityLevel * 2;

        // create final quantization tables based on quality level
        for (i = 0; i < 64; i++) {
            temp = (Constants.std_luminance_quant_tbl[i] * qualityLevel + 50) / 100;
            if (temp <= 0)
                temp = 1;
            if (temp > 255)
                temp = 255;
            luminance_quant_tbl[i] = temp;

            temp = (Constants.std_chrominance_quant_tbl[i] * qualityLevel + 50) / 100;
            if (temp <= 0)
                temp = 1;
            if (temp >= 255)
                temp = 255;
            chrominance_quant_tbl[i] = temp;
        }

        tables[0] = luminance_quant_tbl;
        tables[1] = chrominance_quant_tbl;
    }

    public void quantizeBlock(int inputData[][], int code, int outputData[]) {
        int i, j;
        int index = 0;
        int temp;
        int quanttbl[];

        quanttbl = (int[]) tables[code]; // fetch correct quantization table

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                temp = inputData[i][j];
                if (temp < 0) {
                    temp = -temp;
                    temp += (int) (quanttbl[index] >> 1);
                    temp /= (int) (quanttbl[index]);
                    temp = -temp;
                } else {
                    temp += quanttbl[index] >> 1;
                    temp /= quanttbl[index];
                }
                outputData[index++] = temp;
            }
        }
    }

}
