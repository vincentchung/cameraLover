/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pictelligent.s40.camera5in1.JPEGDecoder;

import java.io.IOException;
import java.io.InputStream;

public class BinaryFileParser extends BinaryFileFunctions {
    public BinaryFileParser() {
    }
/*
    protected  int convertByteArrayToInt( String name,  int start,
             byte bytes[]) {
        return convertByteArrayToInt(name, bytes, start);
    }

    protected  int convertByteArrayToInt( String name,  byte bytes[]) {
        return convertByteArrayToInt(name, bytes);
    }

    public  int convertByteArrayToShort( String name,  byte bytes[])
            throws ImageReadException {
    	System.out.println("convertByteArrayToShort 1");
        return convertByteArrayToShort(name, 0, bytes);
    }

    public  int convertByteArrayToShort(final String name,final  int start,
    		final byte bytes[]) throws ImageReadException {
        return convertByteArrayToShort(name, start, bytes);
    }

    public  int read4Bytes(final String name, final InputStream is,final  String exception)
            throws IOException {
        return read4Bytes(name, is, exception);
    }

    public final int read3Bytes(final String name,final  InputStream is,final  String exception)
            throws IOException {
        return read3Bytes(name, is, exception);
    }

    public  int read2Bytes( String name,  InputStream is,  String exception)
            throws ImageReadException, IOException {
        return read2Bytes(name, is, exception);
    }
*/
    public static boolean byteArrayHasPrefix( byte bytes[],  byte prefix[]) {
        if ((bytes == null) || (bytes.length < prefix.length)) {
            return false;
        }

        for (int i = 0; i < prefix.length; i++) {
            if (bytes[i] != prefix[i]) {
                return false;
            }
        }

        return true;
    }
/*
    protected  byte[] int2ToByteArray( int value) {
        return int2ToByteArray(value);
    }
*/
}
