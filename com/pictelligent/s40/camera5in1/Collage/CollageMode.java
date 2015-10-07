
package com.pictelligent.s40.camera5in1.Collage;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.Rectangle;

public class CollageMode {
    String frame_filename=null;
    String frame_filename_out=null;
    int image_num;
    int frame_height;
    int frame_width;
    Rectangle[] cell = new Rectangle[5];// limit is 5
    Rectangle[] mHint = new Rectangle[5];// limit is 5
    Rectangle viewfinder;
    int frameNum;
    int frameBorderColor;
    int frameFillColor;
    int frameBorderWidth;

    public CollageMode(String frameName, int w, int h, int frames, Rectangle[] block,
            Rectangle[] hint, Rectangle vf)
    {
        if (Camera5in1.mDebug)
            System.out.println("CollageMode");
        frame_width = w;
        frame_height = h;
        frame_filename = frameName;
        image_num = frames;

        for (int i = 0; i < image_num; i++)
        {
            if (Camera5in1.mDebug)
                System.out.println("blocks" + i);
            cell[i] = new Rectangle(block[i].x, block[i].y, block[i].width, block[i].height);
            mHint[i] = new Rectangle(hint[i].x, hint[i].y, hint[i].width, hint[i].height);
            // cell[i].setBounds(block[i]);
            if (Camera5in1.mDebug)
                System.out.println("block x" + cell[i].x + "y" + cell[i].y + "w" + cell[i].width
                        + "h" + cell[i].height);
        }
        if (Camera5in1.mDebug)
            System.out.println("x" + vf.x + "y" + vf.y + "w" + vf.width + "h" + vf.height);
        // viewfinder.setBounds(vf);
        viewfinder = new Rectangle(vf.x, vf.y, vf.width, vf.height);
        if (Camera5in1.mDebug)
            System.out.println("x" + viewfinder.x + "y" + viewfinder.y + "w" + viewfinder.width
                    + "h" + viewfinder.height);
        
        frameBorderColor = 0xFFFFFF; // white
        frameFillColor = 0xCCCCCC;
        frameBorderWidth = 5;
    }

    public CollageMode(String frameName, int w, int h, int frames, Rectangle[] block,
            Rectangle[] hint, Rectangle vf,String frame_out,int num)
    {
        if (Camera5in1.mDebug)
            System.out.println("CollageMode");
        frame_width = w;
        frame_height = h;
        frame_filename = frameName;
        image_num = frames;

        for (int i = 0; i < image_num; i++)
        {
            if (Camera5in1.mDebug)
                System.out.println("blocks" + i);
            cell[i] = new Rectangle(block[i].x, block[i].y, block[i].width, block[i].height);
            mHint[i] = new Rectangle(hint[i].x, hint[i].y, hint[i].width, hint[i].height);
            // cell[i].setBounds(block[i]);
            if (Camera5in1.mDebug)
                System.out.println("block x" + cell[i].x + "y" + cell[i].y + "w" + cell[i].width
                        + "h" + cell[i].height);
        }
        if (Camera5in1.mDebug)
            System.out.println("x" + vf.x + "y" + vf.y + "w" + vf.width + "h" + vf.height);
        // viewfinder.setBounds(vf);
        viewfinder = new Rectangle(vf.x, vf.y, vf.width, vf.height);
        if (Camera5in1.mDebug)
            System.out.println("x" + viewfinder.x + "y" + viewfinder.y + "w" + viewfinder.width
                    + "h" + viewfinder.height);
        frame_filename_out=frame_out;

        frameNum=num;
        frameBorderColor = 0xFFFFFF; // white
        frameFillColor = 0xCCCCCC;
        frameBorderWidth = 5;
    }

}
