
package com.pictelligent.s40.camera5in1.Futuro;

import java.io.IOException;

import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.EffectPaintCanvas;
import com.pictelligent.s40.camera5in1.effects.EffectAlgorithms;
import com.pictelligent.s40.camera5in1.language.Language;

/**
 * Canvas to preview all effects here.
 */
class FuturoEffectCanvas extends EffectPaintCanvas {

    public FuturoEffectCanvas(byte[] raw, int capturingWidth, int capturingHeight) {
        super(raw, capturingWidth, capturingHeight);
    }

    public void switchEffect() {
        if (b16bits) {
            switchEffect16();
        } else {
            switchEffect32();
        }
    }

    public void switchEffect16() {
        switch (mEffectIndex) {
            case 0:
                EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro1_16to32(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT1);
                break;
            case 2:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro2_16to32(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT2);
                break;
            case 3:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro3_16to32(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT3);
                break;
            case 4:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro4_16to32(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT4);
                break;
            case 5:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro5_16to32(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT5);
                break;

        }
    }

    public void switchEffect32() {
        switch (mEffectIndex) {
            case 0:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro1(previewImage, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT1);
                break;
            case 2:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro2(previewImage, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT2);
                break;
            case 3:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro3(previewImage, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT3);
                break;
            case 4:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro4(previewImage, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT4);
                break;
            case 5:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro5(previewImage, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT5);
                break;

        }
    }

    public void switchEffectFull() {
        if (b16bits)
        {
            switchEffectFull16();
        } else
        {
            switchEffectFull32();
        }
    }

    public void switchEffectFull32() {
        System.gc();
        switch (mEffectIndex) {
            case 0:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro1(fullImage, fullImage, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT1);
                break;
            case 2:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro2(fullImage, fullImage, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT2);
                break;
            case 3:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro3(fullImage, fullImage, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT3);
                break;
            case 4:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro4(fullImage, fullImage, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT4);
                break;
            case 5:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro5(fullImage, fullImage, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT5);
                break;

        }
    }

    public void switchEffectFull16() {
        System.gc();
        switch (mEffectIndex) {
            case 0:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro1_16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT1);
                break;
            case 2:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro2_16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT2);
                break;
            case 3:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro3_16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT3);
                break;
            case 4:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro4_16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT4);
                break;
            case 5:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                futuro5_16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_FUTURO_EFFECT5);
                break;
        }
        fullImage16bitsOutput = fullImage16bits;
    }

    private void futuro1(int[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectXray(src, out, w, h);
    }

    private void futuro1_16(short[] src, short[] out, int w, int h) {
        EffectAlgorithms.effectXray16(src, out, w, h);
    }

    private void futuro1_16to32(short[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectXray16to32(src, out, w, h);
    }

    private void futuro2(int[] src, int[] out, int w, int h) {

        EffectAlgorithms.effectBW(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro1-b1.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, 0, 0, w / 2, h / 2, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b2.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, w / 2, 0, w, h / 2, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b3.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, 0, h / 2, w / 2, h, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b4.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, w / 2, h / 2, w, h, FuturoBlock);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void futuro2_16(short[] src, short[] out, int w, int h) {

        EffectAlgorithms.effectBW16(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro1-b1.jpg");
            EffectAlgorithms.effectFuturoOverlay16(out, out, w, h, 0, 0, w / 2, h / 2, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b2.jpg");
            EffectAlgorithms.effectFuturoOverlay16(out, out, w, h, w / 2, 0, w, h / 2, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b3.jpg");
            EffectAlgorithms.effectFuturoOverlay16(out, out, w, h, 0, h / 2, w / 2, h, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b4.jpg");
            EffectAlgorithms.effectFuturoOverlay16(out, out, w, h, w / 2, h / 2, w, h, FuturoBlock);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void futuro2_16to32(short[] src, int[] out, int w, int h) {

        EffectAlgorithms.effectBW16to32(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro1-b1.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, 0, 0, w / 2, h / 2, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b2.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, w / 2, 0, w, h / 2, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b3.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, 0, h / 2, w / 2, h, FuturoBlock);
            FuturoBlock = Image.createImage("/futuro1-b4.jpg");
            EffectAlgorithms.effectFuturoOverlay(out, out, w, h, w / 2, h / 2, w, h, FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro3(int[] src, int[] out, int w, int h) {

        try {
            Image FuturoBlock = Image.createImage("/futuro2-b1.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b2.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b3.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b4.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro3_16(short[] src, short[] out, int w, int h) {
        EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);

        try {
            Image FuturoBlock = Image.createImage("/futuro2-b1.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b2.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b3.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b4.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro3_16to32(short[] src, int[] out, int w, int h) {
        EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
        try {
            Image FuturoBlock = Image.createImage("/futuro2-b1.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b2.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b3.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro2-b4.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void futuro4(int[] src, int[] out, int w, int h) {

        EffectAlgorithms.effectBW(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro3-b1.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b2.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b3.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b4.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro4_16(short[] src, short[] out, int w, int h) {

        EffectAlgorithms.effectBW16(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro3-b1.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha16(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b2.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha16(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b3.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha16(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b4.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha16(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro4_16to32(short[] src, int[] out, int w, int h) {

        EffectAlgorithms.effectBW16to32(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro3-b1.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b2.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b3.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro3-b4.png");
            EffectAlgorithms.effectFuturoOverlayWithAlpha(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro5(int[] src, int[] out, int w, int h) {

        EffectAlgorithms.effectBW(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro4-b1.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b2.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b3.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b4.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro5_16(short[] src, short[] out, int w, int h) {

        EffectAlgorithms.effectBW16(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro4-b1.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b2.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b3.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b4.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha16(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    private void futuro5_16to32(short[] src, int[] out, int w, int h) {

        EffectAlgorithms.effectBW16to32(src, out, w, h);

        try {
            Image FuturoBlock = Image.createImage("/futuro4-b1.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, 0, w / 2, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b2.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, 0, w, h / 2,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b3.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, 0, h / 2, w / 2, h,
                    FuturoBlock);
            FuturoBlock = Image.createImage("/futuro4-b4.jpg");
            EffectAlgorithms.effectFuturoOverlayWhiteIsAlpha(out, out, w, h, w / 2, h / 2, w, h,
                    FuturoBlock);
        } catch (Exception e) {
            Camera5in1.display_error(e.getMessage());
        }
    }

    public void init() {
        mEffectsNumber = 5;
        mPreviewWidth = 156;
        mPreviewHeight = 208;
        mCapturedWidth = Futuro.CAPTURING_WIDTH;
        mCapturedHeight = Futuro.CAPTURING_HEIGHT;
        mBackgroundColor = Camera5in1.FUTURO_COLOR;
        filename_title = "pic_futuro_";
    }

}
