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

public class DctI {
    /*
     * The book "JPEG still image data compression standard", by Pennebaker and
     * Mitchell, Chapter 4, discusses a number of approaches to the fast DCT.
     * Here's the cost, exluding modified (de)quantization, for transforming an
     * 8x8 block:
     * 
     * Algorithm Adds Multiplies RightShifts Total Naive 896 1024 0 1920
     * "Symmetries" 448 224 0 672 Vetterli and 464 208 0 672 Ligtenberg Arai,
     * Agui and 464 80 0 544 Nakajima (AA&N) Feig 8x8 462 54 6 522 Fused mul/add
     * 416 (a pipe dream)
     * 
     * IJG's libjpeg, FFmpeg, and a number of others use AA&N.
     * 
     * It would appear that Feig does 4-5% less operations, and multiplications
     * are reduced from 80 in AA&N to only 54. But in practice:
     * 
     * Benchmarks, Intel Core i3 @ 2.93 GHz in long mode, 4 GB RAM Time taken to
     * do 100 million IDCTs (less is better): Rene' St�ckel's Feig, int: 45.07
     * seconds My Feig, inting point: 36.252 seconds AA&N, unrolled loops,
     * double[][] -> double[][]: 25.167 seconds
     * 
     * Clearly Feig is hopeless. I suspect the performance killer is simply the
     * weight of the algorithm: massive number of local variables, large code
     * size, and lots of random array accesses.
     * 
     * Also, AA&N can be optimized a lot: AA&N, rolled loops, double[][] ->
     * double[][]: 21.162 seconds AA&N, rolled loops, int[][] -> int[][]: no
     * improvement, but at some stage Hotspot might start doing SIMD, so let's
     * use int AA&N, rolled loops, int[] -> int[][]: 19.979 seconds
     * apparently 2D arrays are slow! AA&N, rolled loops, inlined 1D AA&N
     * transform, int[] transformed in-place: 18.5 seconds AA&N, previous
     * version rewritten in C and compiled with "gcc -O3" takes: 8.5 seconds
     * (probably due to heavy use of SIMD)
     * 
     * Other brave attempts: AA&N, best int version converted to 16:16 fixed
     * point: 23.923 seconds
     * 
     * Anyway the best int version stays. 18.5 seconds = 5.4 million
     * transforms per second per core :-)
     */

    //public static final int intShift=1000;
    
    
    private final static int IDCT_P[] ={
        0,   5,  40,  16,  45,   2,   7,  42,
       21,  56,   8,  61,  18,  47,   1,   4,
       41,  23,  58,  13,  32,  24,  37,  10,
       63,  17,  44,   3,   6,  43,  20,  57,
       15,  34,  29,  48,  53,  26,  39,   9,
       60,  19,  46,  22,  59,  12,  33,  31,
       50,  55,  25,  36,  11,  62,  14,  35,
       28,  49,  52,  27,  38,  30,  51,  54    
     } ;
    
    public final static void scaleQuantizationMatrix(final int matirx[],final int[] QuantizationMatrix,int matrixOut[]) {
    	for (int j = 0; j < 64; j++) {
            //block[j] = blockInt[j] * scaledQuantizationTable[j];
        	//blockInt[j] *=scaledQuantizationTable[j];
    		matrixOut[IDCT_P[j]] = 
        			matirx[j]*QuantizationMatrix[j];
        }
    }
    
public final static void EnhanceQuantizationTable( int qtab[] ){

        int i ;
        for( i = 0 ; i < 8 ; i ++ )
        {
            qtab[ZigZag.zigZag[0*8+i]] *= 90  ;
            qtab[ZigZag.zigZag[4*8+i]] *= 90  ;
            qtab[ZigZag.zigZag[2*8+i]] *= 118 ;
            qtab[ZigZag.zigZag[6*8+i]] *= 49  ;
            qtab[ZigZag.zigZag[5*8+i]] *= 71  ;
            qtab[ZigZag.zigZag[1*8+i]] *= 126 ;
            qtab[ZigZag.zigZag[7*8+i]] *= 25  ;
            qtab[ZigZag.zigZag[3*8+i]] *= 106 ;
        }
        for( i = 0 ; i < 8 ; i ++ )
        {
            qtab[ZigZag.zigZag[0+8*i]] *= 90  ;
            qtab[ZigZag.zigZag[4+8*i]] *= 90  ;
            qtab[ZigZag.zigZag[2+8*i]] *= 118 ;
            qtab[ZigZag.zigZag[6+8*i]] *= 49  ;
            qtab[ZigZag.zigZag[5+8*i]] *= 71  ;
            qtab[ZigZag.zigZag[1+8*i]] *= 126 ;
            qtab[ZigZag.zigZag[7+8*i]] *= 25  ;
            qtab[ZigZag.zigZag[3+8*i]] *= 106 ;
        }
        for( i = 0 ; i < 64 ; i++ ) {
            qtab[i] >>= 6 ;
        }
    }

private static final int A1 = (int)480;


    public static void inverseDCT8x8(int matrix[]){
        int p[][] = new int[8][8] ;
        int t0 , t1 , t2 , t3, i ;
        int src0, src1, src2, src3, src4, src5, src6, src7;
        int det0, det1, det2, det3, det4, det5, det6, det7;
            int mindex=0;

        for( i = 0 ; i < 8 ; i++ )
        {
        	/*
            src0 = matrix[0*8+i] ;
            src1 = matrix[1*8+i] ;
            src2 = matrix[2*8+i] - matrix[3*8+i] ;
            src3 = matrix[3*8+i] + matrix[2*8+i] ;
            src4 = matrix[4*8+i] - matrix[7*8+i] ;
            src6 = matrix[5*8+i] - matrix[6*8+i] ;
            t0   = matrix[5*8+i] + matrix[6*8+i] ;
            t1   = matrix[4*8+i] + matrix[7*8+i] ;*/
            src0 = matrix[0+i] ;
            src1 = matrix[8+i] ;
            src2 = matrix[16+i] - matrix[24+i] ;
            src3 = matrix[24+i] + matrix[16+i] ;
            src4 = matrix[32+i] - matrix[56+i] ;
            src6 = matrix[40+i] - matrix[48+i] ;
            t0   = matrix[40+i] + matrix[48+i] ;
            t1   = matrix[32+i] + matrix[56+i] ;
            src5 = t0 - t1 ;
            src7 = t0 + t1 ;
            //
            det4 =-src4 * A1 - src6 * 192 ;
            det5 = src5 * 384 ;
            det6 = src6 * A1 - src4 * 192 ;
            det7 = src7<<8;// * 256 ;
            t0   = src0<<8;// * 256 ;
            t1   = src1<<8;// * 256 ;
            t2   = src2 * 384 ;
            t3   = src3<<8;// * 256 ;
            det3 = t3      ;
            det0 = t0 + t1 ;
            det1 = t0 - t1 ;
            det2 = t2 - t3 ;
            //
            src0 = det0 + det3 ;
            src1 = det1 + det2 ;
            src2 = det1 - det2 ;
            src3 = det0 - det3 ;
            src4 = det6 - det4 - det5  - det7 ;
            src5 = det5 - det6 + det7 ;
            src6 = det6 - det7 ;
            src7 = det7 ;
            //
            p[0][i] = ( src0 + src7 +(1<<12))>>13 ;
            p[1][i] = ( src1 + src6 +(1<<12))>>13 ;
            p[2][i] = ( src2 + src5 +(1<<12))>>13 ;
            p[3][i] = ( src3 + src4 +(1<<12))>>13 ;
            p[4][i] = ( src3 - src4 +(1<<12))>>13 ;
            p[5][i] = ( src2 - src5 +(1<<12))>>13 ;
            p[6][i] = ( src1 - src6 +(1<<12))>>13 ;
            p[7][i] = ( src0 - src7 +(1<<12))>>13 ;
        }
        //
        for( i = 0 ; i < 8 ; i++ )
        {
            src0 = p[i][0] ;
            src1 = p[i][1] ;
            src2 = p[i][2] - p[i][3] ;
            src3 = p[i][3] + p[i][2] ;
            src4 = p[i][4] - p[i][7] ;
            src6 = p[i][5] - p[i][6] ;
            t0   = p[i][5] + p[i][6] ;
            t1   = p[i][4] + p[i][7] ;
            src5 = t0 - t1 ;
            src7 = t0 + t1 ;
            //
            det4 =-src4 * A1 - src6 * 192 ;
            det5 = src5 * 384 ;
            det6 = src6 * A1 - src4 * 192 ;
            det7 = src7<<8;// * 256 ;
            t0   = src0<<8;// * 256 ;
            t1   = src1<<8;// * 256 ;
            t2   = src2 * 384 ;
            t3   = src3<<8;// * 256 ;
            det3 = t3      ;
            det0 = t0 + t1 ;
            det1 = t0 - t1 ;
            det2 = t2 - t3 ;
            //
            src0 = det0 + det3 ;
            src1 = det1 + det2 ;
            src2 = det1 - det2 ;
            src3 = det0 - det3 ;
            src4 = det6 - det4 - det5  - det7 ;
            src5 = det5 - det6 + det7 ;
            src6 = det6 - det7 ;
            src7 = det7 ;
            //
            matrix[mindex++] = ( src0 + src7 +(1<<12))>>13 ;
            matrix[mindex++] = ( src1 + src6 +(1<<12))>>13 ;
            matrix[mindex++] = ( src2 + src5 +(1<<12))>>13 ;
            matrix[mindex++] = ( src3 + src4 +(1<<12))>>13 ;
            matrix[mindex++] = ( src3 - src4 +(1<<12))>>13 ;
            matrix[mindex++] = ( src2 - src5 +(1<<12))>>13 ;
            matrix[mindex++] = ( src1 - src6 +(1<<12))>>13 ;
            matrix[mindex++] = ( src0 - src7 +(1<<12))>>13 ;
        }
       }   


}
