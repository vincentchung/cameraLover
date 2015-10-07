
package com.pictelligent.s40.camera5in1.JPEGEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;

public class JpegFile {

    //private final static String ImageSavePath = System.getProperty("fileconn.dir.photos");
	private final static String ImageSavePath = "file:///MemoryCard/_my_pictures/";

    public static boolean saveImage(final String filename,
            final int[] imageRGB, final int width, final int height) {

        boolean saveOK = false;

        if (imageRGB == null)
            return saveOK;

        // Tero: test RGB565 saving
        // short[] rgb565buf = new short[width*height];
        // Scaling.convertARGB8888toRGB565(imageRGB, rgb565buf, width, height);

        if (filename.length() > 0) {

            if (Camera5in1.mDebug)
                DebugUtil.Log("Saving JPEG file "
                        + System.getProperty(ImageSavePath) + filename);

            FileConnection con = null;
            OutputStream out = null;
            try {
                con = (FileConnection) Connector.open( ImageSavePath+ filename);
                if (con != null) {
                    if (con.exists()) {
                        con.delete();
                    }
                    con.create();
                    out = con.openOutputStream();
                    JpegEncoder enc = new JpegEncoder();
                    if (Camera5in1.mDebug) {
                        long startTime = System.currentTimeMillis();
                        byte[] encodedImage = enc.encode(imageRGB, width, height, (short) 90);
                        long endTime = System.currentTimeMillis();
                        DebugUtil.Log("JPEG encoding done in " + (endTime - startTime)
                                + " ms, size " + width + "x" + height);
                        out.write(encodedImage);
                        saveOK = true;
                    }
                    else {
                        byte[] encodedImage = enc.encode(imageRGB, width, height, (short) 90);
                        out.write(encodedImage);
                        saveOK = true;
                    }
                    //out.flush();
                    enc.deinit();
                    enc = null;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (IOException io) {
                }
                System.gc();
            }

        } else {
        }
        return saveOK;
    }
    
    
    private static void save_file(String path,byte[] buffer)
    {
    	try {
			FileConnection fc = (FileConnection)Connector.open(path);
			if (fc.exists()){
				System.out.println("picture exists.");
				return;
			}
			fc.create();
			OutputStream os = fc.openOutputStream();
		    
			//while (bytesRead > 0) 
			{
		    	os.write(buffer);
		        //bytesRead = is.read(a);
		    }
		    os.close();
		    fc.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
    }
    public static boolean saveImage162(final String filename,
            final short[] imageRGB, final int width, final int height) {

        boolean saveOK = false;

        if (imageRGB == null)
            return saveOK;

        // Tero: test RGB565 saving
        // short[] rgb565buf = new short[width*height];
        // Scaling.convertARGB8888toRGB565(imageRGB, rgb565buf, width, height);

        //System.out.println(ImageSavePath + filename);
        if (filename.length() > 0) {

            if (Camera5in1.mDebug)
                DebugUtil.Log("Saving JPEG file "
                        + System.getProperty(ImageSavePath) + filename);
            
            JpegEncoder enc = new JpegEncoder();
            byte[] encodedImage = enc.encode16(imageRGB, width, height, (short) 90);
            save_file(ImageSavePath + filename,encodedImage);
            System.gc();

        } else {
        }
        return saveOK;
    }
    public static boolean saveImage16(final String filename,
            final short[] imageRGB, final int width, final int height) {

        boolean saveOK = false;

        if (imageRGB == null)
            return saveOK;

        // Tero: test RGB565 saving
        // short[] rgb565buf = new short[width*height];
        // Scaling.convertARGB8888toRGB565(imageRGB, rgb565buf, width, height);

        //System.out.println(ImageSavePath + filename);
        if (filename.length() > 0) {

            if (Camera5in1.mDebug)
                DebugUtil.Log("Saving JPEG file "
                        + System.getProperty(ImageSavePath) + filename);

            //FileConnection con = null;
            //OutputStream out = null;
            try {
                //con = (FileConnection) Connector.open(System
                //        .getProperty(ImageSavePath) + filename);
            	FileConnection con = (FileConnection) Connector.open(ImageSavePath + filename);
            	//con = (FileConnection) Connector.open(svaing_path);
                if (con != null) {
                	/*
                    if (con.exists()) {
                        con.delete();
                    }*/
                    con.create();
                    OutputStream out = con.openOutputStream();
                    JpegEncoder enc = new JpegEncoder();
                    if (Camera5in1.mDebug) {
                        long startTime = System.currentTimeMillis();
                        byte[] encodedImage = enc.encode16(imageRGB, width, height, (short) 90);
                        long endTime = System.currentTimeMillis();
                        DebugUtil.Log("JPEG encoding done in " + (endTime - startTime)
                                + " ms, size " + width + "x" + height);
                        out.write(encodedImage);
                        saveOK = true;
                    }
                    else {
                        byte[] encodedImage = enc.encode16(imageRGB, width, height, (short) 90);
                        
                        out.write(encodedImage);
                        
                        saveOK = true;
                    }
                    //out.flush();
                    enc.deinit();
                    enc = null;
                    con.close();
                    out.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                
                System.gc();
            }

        } else {
        }
        return saveOK;
    }

}
