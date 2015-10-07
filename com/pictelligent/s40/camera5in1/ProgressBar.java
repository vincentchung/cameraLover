
package com.pictelligent.s40.camera5in1;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.language.Language;

public class ProgressBar {

    private static final String IMAGE_PATH_PROGRESS = "/ic_progress.png";

    private Image mIconProgress;
    private int mCanvasWidth;
    private int mCanvasHeight;
    private int mStringIndex;
    private int mProgressBgW;
    private int mProgressBgH = 60;
    private String[] mDisplayString;

    public ProgressBar(int canvasWidth, int canvasHeight) {

        String save = Language.get(Language.TEXT_SAVING);
        mDisplayString = new String[] {
                save, save + ".", save + "..", save + "..."
        };
        mCanvasWidth = canvasWidth;
        mCanvasHeight = canvasHeight;

        try {
            mIconProgress = Image.createImage(IMAGE_PATH_PROGRESS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void reset() {
        mStringIndex = 0;
    }

    public void update(Graphics g) {
        Font font = g.getFont();

        int stringMax = font.stringWidth(mDisplayString[mDisplayString.length - 1]);
        mProgressBgW = stringMax + mIconProgress.getWidth() + 30;
        mProgressBgH = 60;

        g.translate(-g.getTranslateX(), -g.getTranslateY());
        g.setColor(0, 0, 0);

        // If the progress bar is not wider than the screen.
        if (mProgressBgW <= mCanvasWidth) {
            int bg_x = (mCanvasWidth - mProgressBgW) / 2;
            int bg_y = (mCanvasHeight - mProgressBgH) / 2;
            g.fillRoundRect(bg_x, bg_y, mProgressBgW, mProgressBgH, 10, 10);

            g.translate(bg_x, bg_y);
            int image_w = mIconProgress.getWidth();
            int image_h = mIconProgress.getHeight();
            int image_offset_x = 10;
            g.drawImage(mIconProgress, 10, (mProgressBgH - image_h) / 2, 0);

            g.translate(image_w + image_offset_x, 0);
            g.setColor(255, 255, 255);
            g.drawString(mDisplayString[mStringIndex++ % mDisplayString.length], 10,
                    (mProgressBgH - font.getHeight()) / 2, 0);
        } else { // If the progress bar is wider than the screen, we split it to
                 // two lines.
            mProgressBgH = Math.max(mIconProgress.getHeight() + 20, font.getHeight() * 2 + 15);
            mProgressBgW = 10 + mIconProgress.getWidth() + 10 + stringMax / 2 + 10;

            int bg_x = (mCanvasWidth - mProgressBgW) / 2;
            int bg_y = (mCanvasHeight - mProgressBgH) / 2;
            g.fillRoundRect(bg_x, bg_y, mProgressBgW, mProgressBgH, 10, 10);

            g.translate(bg_x, bg_y);
            int image_w = mIconProgress.getWidth();
            int image_h = mIconProgress.getHeight();
            int image_offset_x = 10;
            g.drawImage(mIconProgress, 10, (mProgressBgH - image_h) / 2, 0);

            int length = mDisplayString[mDisplayString.length - 1].length();
            String string = mDisplayString[mStringIndex++ % mDisplayString.length];
            int yoffset = (mProgressBgH - font.getHeight() * 2) / 3;
            g.translate(image_w + image_offset_x, 0);
            g.setColor(255, 255, 255);
            g.drawString(string.substring(0, length / 2), 10, yoffset, 0);
            g.drawString(string.substring(length / 2, string.length()), 10,
                    yoffset * 2 + font.getHeight(), 0);
        }

    }
}
