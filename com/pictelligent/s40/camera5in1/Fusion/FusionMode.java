
package com.pictelligent.s40.camera5in1.Fusion;

import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.Rectangle;

public class FusionMode {
    String frame_filename;
    int image_num;
    int image_height;
    int image_width;
    int preview_frame_width;
    int preview_frame_height;
    Rectangle[] cell = new Rectangle[2];// limit is 5
    Rectangle[] hintcell = new Rectangle[2];// limit is 5
    Image[] mTipImages;
    Rectangle[] mTipCells = new Rectangle[2];

    public FusionMode(String frameName, int preview_width, int preview_height,
            int imageWidth, int imageHeight, int frames, Rectangle[] block,
            Rectangle[] hints, Image[] tipImages, Rectangle[] tipCells) {
        if (Camera5in1.mDebug)
            System.out.println("FusionMode");
        image_width = imageWidth;
        image_height = imageHeight;
        frame_filename = frameName;
        image_num = frames;
        preview_frame_width = preview_width;
        preview_frame_height = preview_height;

        for (int i = 0; i < image_num; i++) {
            if (Camera5in1.mDebug)
                System.out.println("blocks" + i);
            cell[i] = new Rectangle(block[i].x, block[i].y, block[i].width,
                    block[i].height);
            hintcell[i] = new Rectangle(hints[i].x, hints[i].y, hints[i].width,
                    hints[i].height);
            mTipCells[i] = new Rectangle(tipCells[i].x, tipCells[i].y, tipCells[i].width,
                    tipCells[i].height);
            if (Camera5in1.mDebug)
                System.out.println("block x" + cell[i].x + "y" + cell[i].y + "w"
                        + cell[i].width + "h" + cell[i].height);
        }
        int imageCount = tipImages.length;
        mTipImages = new Image[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mTipImages[i] = tipImages[i];
        }
    }
}
