
package com.pictelligent.s40.camera5in1.JPEGEncoder;

// DCT forward transform, fixed point only

class DCT
{

    static final int DCTSIZE = 8;
    static final int CONST_BITS = 13;
    static final short PASS1_BITS = 2;
    static final short CENTERJSAMPLE = 128;

    static final int FIX_0_298631336 = 2446; // FIX(0.298631336)
    static final int FIX_0_390180644 = 3196; // FIX(0.390180644)
    static final int FIX_0_541196100 = 4433; // FIX(0.541196100)
    static final int FIX_0_765366865 = 6270; // FIX(0.765366865)
    static final int FIX_0_899976223 = 7373; // FIX(0.899976223)
    static final int FIX_1_175875602 = 9633; // FIX(1.175875602)
    static final int FIX_1_501321110 = 12299; // FIX(1.501321110)
    static final int FIX_1_847759065 = 15137; // FIX(1.847759065)
    static final int FIX_1_961570560 = 16069; // FIX(1.961570560)
    static final int FIX_2_053119869 = 16819; // FIX(2.053119869)
    static final int FIX_2_562915447 = 20995; // FIX(2.562915447)
    static final int FIX_3_072711026 = 25172; // FIX(3.072711026)

    // jdct.h and jpegint.h:
    // #define DESCALE(x,n) RIGHT_SHIFT((x) + (ONE << ((n)-1)), n)
    private static int RIGHT_SHIFT(int x, int n) {
        return (int) ((x + (1 << (n - 1))) >> n);
    }

    public void forwardDCT(short input[][], int output[][], int offsetX, int offsetY, int width) {

        int tmp0, tmp1, tmp2, tmp3;
        int tmp10, tmp11, tmp12, tmp13;
        int z1;

        int rowctr;
        int row, col;
        int i, j;

        if (JpegEncoder.mDebugVerbose)
            System.out.println("DCT FP encoding, image width " + width + ", offsets " + offsetX
                    + " " + offsetY);

        // TODO: remove this extra data copy step
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                output[j][i] = input[i + offsetX][j + offsetY] - CENTERJSAMPLE;
            }
        }

        /* Pass 1: process rows. */
        /* Note results are scaled up by sqrt(8) compared to a true DCT; */
        /* furthermore, we scale the results by 2**PASS1_BITS. */

        row = 0;
        for (rowctr = DCTSIZE; --rowctr >= 0;) {

            /*
             * Even part per LL&M figure 1 --- note that published figure is
             * faulty; rotator "sqrt(2)*c1" should be "sqrt(2)*c6".
             */

            tmp0 = output[0][row] + output[7][row];
            tmp1 = output[1][row] + output[6][row];
            tmp2 = output[2][row] + output[5][row];
            tmp3 = output[3][row] + output[4][row];

            tmp10 = tmp0 + tmp3;
            tmp12 = tmp0 - tmp3;
            tmp11 = tmp1 + tmp2;
            tmp13 = tmp1 - tmp2;

            tmp0 = output[0][row] - output[7][row];
            tmp1 = output[1][row] - output[6][row];
            tmp2 = output[2][row] - output[5][row];
            tmp3 = output[3][row] - output[4][row];

            /* Apply unsigned->signed conversion */
            output[0][row] = (tmp10 + tmp11) << PASS1_BITS;
            output[4][row] = (tmp10 - tmp11) << PASS1_BITS;

            z1 = (tmp12 + tmp13) * FIX_0_541196100;
            output[2][row] = RIGHT_SHIFT(z1 + (tmp12 * FIX_0_765366865), CONST_BITS - PASS1_BITS);
            output[6][row] = RIGHT_SHIFT(z1 + (tmp13 * (-1) * FIX_1_847759065), CONST_BITS
                    - PASS1_BITS);

            /*
             * Odd part per figure 8 --- note paper omits factor of sqrt(2). cK
             * represents sqrt(2) * cos(K*pi/16). i0..i3 in the paper are
             * tmp0..tmp3 here.
             */

            tmp10 = tmp0 + tmp3;
            tmp11 = tmp1 + tmp2;
            tmp12 = tmp0 + tmp2;
            tmp13 = tmp1 + tmp3;

            z1 = (tmp12 + tmp13) * FIX_1_175875602; // c3

            tmp0 *= FIX_1_501321110;
            tmp1 *= FIX_3_072711026;
            tmp2 *= FIX_2_053119869;
            tmp3 *= FIX_0_298631336;
            tmp10 = tmp10 * (-1) * FIX_0_899976223;
            tmp11 = tmp11 * (-1) * FIX_2_562915447;
            tmp12 = tmp12 * (-1) * FIX_0_390180644;
            tmp13 = tmp13 * (-1) * FIX_1_961570560;

            tmp12 += z1;
            tmp13 += z1;

            output[1][row] = RIGHT_SHIFT(tmp0 + tmp10 + tmp12, CONST_BITS - PASS1_BITS);
            output[3][row] = RIGHT_SHIFT(tmp1 + tmp11 + tmp13, CONST_BITS - PASS1_BITS);
            output[5][row] = RIGHT_SHIFT(tmp2 + tmp11 + tmp12, CONST_BITS - PASS1_BITS);
            output[7][row] = RIGHT_SHIFT(tmp3 + tmp10 + tmp13, CONST_BITS - PASS1_BITS);

            row++;
        }

        /*
         * Pass 2: process columns. We remove the PASS1_BITS scaling, but leave
         * the results scaled up by an overall factor of 8.
         */

        col = 0;
        for (rowctr = DCTSIZE; --rowctr >= 0;) {
            /*
             * Even part per LL&M figure 1 --- note that published figure is
             * faulty; rotator "sqrt(2)*c1" should be "sqrt(2)*c6".
             */

            tmp0 = output[col][0] + output[col][7];
            tmp1 = output[col][1] + output[col][6];
            tmp2 = output[col][2] + output[col][5];
            tmp3 = output[col][3] + output[col][4];

            tmp10 = tmp0 + tmp3;
            tmp12 = tmp0 - tmp3;
            tmp11 = tmp1 + tmp2;
            tmp13 = tmp1 - tmp2;

            tmp0 = output[col][0] - output[col][7];
            tmp1 = output[col][1] - output[col][6];
            tmp2 = output[col][2] - output[col][5];
            tmp3 = output[col][3] - output[col][4];

            output[col][0] = RIGHT_SHIFT(tmp10 + tmp11, PASS1_BITS + 3);
            output[col][4] = RIGHT_SHIFT(tmp10 - tmp11, PASS1_BITS + 3);

            z1 = (tmp12 + tmp13) * FIX_0_541196100;

            output[col][2] = RIGHT_SHIFT(z1 + tmp12 * FIX_0_765366865, CONST_BITS + PASS1_BITS + 3);
            output[col][6] = RIGHT_SHIFT(z1 + tmp13 * (-1) * FIX_1_847759065, CONST_BITS
                    + PASS1_BITS + 3);

            /*
             * Odd part per figure 8 --- note paper omits factor of sqrt(2). cK
             * represents sqrt(2) * cos(K*pi/16). i0..i3 in the paper are
             * tmp0..tmp3 here.
             */

            tmp10 = tmp0 + tmp3;
            tmp11 = tmp1 + tmp2;
            tmp12 = tmp0 + tmp2;
            tmp13 = tmp1 + tmp3;

            z1 = (tmp12 + tmp13) * FIX_1_175875602;

            tmp0 *= FIX_1_501321110;
            tmp1 *= FIX_3_072711026;
            tmp2 *= FIX_2_053119869;
            tmp3 *= FIX_0_298631336;
            tmp10 = tmp10 * (-1) * FIX_0_899976223;
            tmp11 = tmp11 * (-1) * FIX_2_562915447;
            tmp12 = tmp12 * (-1) * FIX_0_390180644;
            tmp13 = tmp13 * (-1) * FIX_1_961570560;

            tmp12 += z1;
            tmp13 += z1;

            output[col][1] = RIGHT_SHIFT(tmp0 + tmp10 + tmp12, CONST_BITS + PASS1_BITS + 3);
            output[col][3] = RIGHT_SHIFT(tmp1 + tmp11 + tmp13, CONST_BITS + PASS1_BITS + 3);
            output[col][5] = RIGHT_SHIFT(tmp2 + tmp11 + tmp12, CONST_BITS + PASS1_BITS + 3);
            output[col][7] = RIGHT_SHIFT(tmp3 + tmp10 + tmp13, CONST_BITS + PASS1_BITS + 3);

            col++; // advance pointer to next column
        }
    }
}
