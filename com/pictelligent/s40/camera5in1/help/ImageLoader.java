
package com.pictelligent.s40.camera5in1.help;

import java.io.InputStream;

import javax.microedition.lcdui.Image;

public class ImageLoader {

    private static ImageLoader self;

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        if (self == null) {
            self = new ImageLoader();
        }
        return self;
    }

    public Image loadImage(String imagepath) throws RuntimeException {
        Image image;
        try {
            InputStream in = getClass().getResourceAsStream(imagepath);
            image = Image.createImage(in);
        } catch (Exception e) {
            throw new RuntimeException("ImageLoader failed to load image:" + imagepath + " "
                    + e.getMessage());
        }
        return image;
    }

}
