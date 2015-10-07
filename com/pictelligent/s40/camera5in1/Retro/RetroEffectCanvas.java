
package com.pictelligent.s40.camera5in1.Retro;

import java.io.DataInputStream;
import java.io.InputStream;

import com.nokia.mid.ui.gestures.GestureEvent;
import com.nokia.mid.ui.gestures.GestureInteractiveZone;
import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.EffectPaintCanvas;
import com.pictelligent.s40.camera5in1.effects.EffectAlgorithms;
import com.pictelligent.s40.camera5in1.language.Language;

/**
 * Canvas to preview all effects here.
 */
class RetroEffectCanvas extends EffectPaintCanvas {

    public RetroEffectCanvas(byte[] raw, int capturingWidth, int capturingHeight) {
        super(raw, capturingWidth, capturingHeight);
    }

    public void switchEffect() {
        if (b16bits) {
            switchEffect16();
        } else {
            switchEffect32();
        }
    }

    private void switchEffect32() {
        switch (mEffectIndex) {
            case 0:
                if (b16bits) {
                    EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage, mPreviewWidth,
                            mCapturedHeight);
                } else {
                    System.arraycopy(orgImage, 0, previewImage, 0, orgImage.length);
                }

                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                if (b16bits) {
                    cold16(orgImage16bits, orgImage16bits, mPreviewWidth, mPreviewHeight);
                    EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage, mPreviewWidth,
                            mPreviewHeight);
                } else {
                    cold(orgImage, previewImage, mPreviewWidth, mPreviewHeight);
                }
                // cold(orgImage, previewImage, PREVIEW_WIDTH,
                // cold16(image16, image16, PREVIEW_WIDTH,
                // PREVIEW_HEIGHT);

                mEffectName = Language.get(Language.TEXT_EFFECT_COLD);
                break;
            case 2:
                warm(orgImage, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_WARM);
                break;
            case 3:
                mEffectName = Language.get(Language.TEXT_EFFECT_LOMO);
                flat(orgImage, previewImage, mPreviewWidth,
                        mPreviewHeight);
                break;
            case 4:
                technicolor1(orgImage, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_OLD_FILM);
                break;
            case 5:
                bleach(orgImage, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_BLEACH);
                break;
            case 6:
                vignette(orgImage, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_VIGNETTE);
                break;
            case 7:
                singlecolor(orgImage, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_ANTIQUE);
                break;
        }
    }

    private void switchEffect16() {
        switch (mEffectIndex) {
            case 0:
                EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                // cold16(orgImage16bits, orgImage16bits,
                // PREVIEW_WIDTH,PREVIEW_HEIGHT);
                cold16to32(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_COLD);
                break;
            case 2:
                warm16to32(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_WARM);
                break;
            case 3:
                mEffectName = Language.get(Language.TEXT_EFFECT_LOMO);
                flat16to32(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                break;
            case 4:
                technicolor116to32(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_OLD_FILM);
                break;
            case 5:
                bleach16to32(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_BLEACH);
                break;
            case 6:
                vignette16to32(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_VIGNETTE);
                break;
            case 7:
                singlecolor16to32(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_ANTIQUE);
                break;

        }
        // EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage,
        // PREVIEW_WIDTH, PREVIEW_HEIGHT);
    }

    public void switchEffectFull() {
        if (b16bits) {
            switchEffectFull16();
        } else {
            switchEffectFull32();
        }
    }

    public void switchEffectFull32() {
        if (Camera5in1.mDebug)
            DebugUtil.Log("Applying full-size effect #" + mEffectIndex);
        switch (mEffectIndex) {
            case 0:
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                cold(fullImage, fullImage, mCapturedWidth,
                        mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_COLD);
                break;
            case 2:
                warm(fullImage, fullImage, mCapturedWidth,
                        mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_WARM);
                break;
            case 3:
                mEffectName = Language.get(Language.TEXT_EFFECT_LOMO);
                flat(fullImage, fullImage, mCapturedWidth,
                        mCapturedHeight);
                break;
            case 4:
                technicolor1(fullImage, fullImage, mCapturedWidth,
                        mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_OLD_FILM);
                break;
            case 5:
                bleach(fullImage, fullImage, mCapturedWidth,
                        mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_BLEACH);
                break;
            case 6:
                vignette(fullImage, fullImage, mCapturedWidth,
                        mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_VIGNETTE);
                break;
            case 7:
                singlecolor(fullImage, fullImage, mCapturedWidth,
                        mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_ANTIQUE);
                break;
        }
        if (Camera5in1.mDebug)
            DebugUtil.Log("Effect processing done");
    }

    public void switchEffectFull16() {
        if (Camera5in1.mDebug)
            DebugUtil.Log("Applying full-size effect #" + mEffectIndex);
        switch (mEffectIndex) {
            case 0:
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                break;
            case 1:
                cold16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_COLD);
                break;
            case 2:
                warm16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_WARM);
                break;
            case 3:
                mEffectName = Language.get(Language.TEXT_EFFECT_LOMO);
                flat16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                break;
            case 4:
                technicolor116(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_OLD_FILM);
                break;
            case 5:
                bleach16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_BLEACH);
                break;
            case 6:
                vignette16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_VIGNETTE);
                break;
            case 7:
                singlecolor16(fullImage16bits, fullImage16bits, mCapturedWidth, mCapturedHeight);
                mEffectName = Language.get(Language.TEXT_EFFECT_ANTIQUE);
                break;
        }
        fullImage16bitsOutput = fullImage16bits;
        if (Camera5in1.mDebug)
            DebugUtil.Log("Effect processing done");
    }

    private void cold(int[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectCold(src, out, w, h);
    }

    private void cold16(short[] src, short[] out, int w, int h) {
        EffectAlgorithms.effectCold16(src, out, w, h);
    }

    private void cold16to32(short[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectCold16to32(src, out, w, h);
    }

    private void warm(int[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectWarm(src, out, w, h);
    }

    private void flat(int[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectFlatColors(src, out, w, h);
        DataInputStream vig = openVignetteResource("/vignette240320.dat");
        EffectAlgorithms.effectVignette(out, out, w, h, vig);
    }

    private void technicolor1(int[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectTechnicolor1(src, out, w, h);
    }

    private void bleach(int[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectBleach(src, out, w, h, 400);
    }

    private void vignette(int[] src, int[] out, int w, int h) {
        DataInputStream vig = openVignetteResource("/vignette240320.dat");
        EffectAlgorithms.effectVignette(src, out, w, h, vig);
    }

    private void singlecolor(int[] src, int[] out, int w, int h) {
        // target: 0.72 0.709 0.567
        // level: 0.774
        EffectAlgorithms.effectSingleColor(src, out, w, h, 184, 181, 145, 198);
        vignette(out, out, w, h);
    }

    private void warm16(short[] src, short[] out, int w, int h) {
        EffectAlgorithms.effectWarm16(src, out, w, h);
    }

    private void warm16to32(short[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectWarm16to32(src, out, w, h);
    }

    private void flat16(short[] src, short[] out, int w, int h) {
        EffectAlgorithms.effectFlatColors16(src, out, w, h);
        DataInputStream vig = openVignetteResource("/vignette240320.dat");
        EffectAlgorithms.effectVignette16(out, out, w, h, vig);
    }

    private void flat16to32(short[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectFlatColors16to32(src, out, w, h);
        DataInputStream vig = openVignetteResource("/vignette240320.dat");
        EffectAlgorithms.effectVignette(out, out, w, h, vig);
    }

    private void technicolor116(short[] src, short[] out, int w, int h) {
        EffectAlgorithms.effectTechnicolor116(src, out, w, h);
    }

    private void technicolor116to32(short[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectTechnicolor116to32(src, out, w, h);
    }

    private void bleach16(short[] src, short[] out, int w, int h) {
        EffectAlgorithms.effectBleach16(src, out, w, h, 400);
    }

    private void bleach16to32(short[] src, int[] out, int w, int h) {
        EffectAlgorithms.effectBleach16to32(src, out, w, h, 400);
    }

    private void vignette16(short[] src, short[] out, int w, int h) {
        DataInputStream vig = openVignetteResource("/vignette240320.dat");
        EffectAlgorithms.effectVignette16(src, out, w, h, vig);
    }

    private void vignette16to32(short[] src, int[] out, int w, int h) {
        DataInputStream vig = openVignetteResource("/vignette240320.dat");
        EffectAlgorithms.effectVignette16to32(src, out, w, h, vig);
    }

    private void singlecolor16(short[] src, short[] out, int w, int h) {
        // target: 0.72 0.709 0.567
        // level: 0.774
        EffectAlgorithms.effectSingleColor16(src, out, w, h, 184, 181, 145, 198);
        vignette16(out, out, w, h);
    }

    private void singlecolor16to32(short[] src, int[] out, int w, int h) {
        // target: 0.72 0.709 0.567
        // level: 0.774
        EffectAlgorithms.effectSingleColor16to32(src, out, w, h, 184, 181, 145, 198);
        vignette(out, out, w, h);
    }

    private DataInputStream openVignetteResource(String resName) {
        InputStream file = getClass().getResourceAsStream(resName);
        DataInputStream in = new DataInputStream(file);
        if (file == null) {
            System.out.println("Error opening vignette resource");
        }
        return in;
    }

    public void init() {
        mCapturedWidth = Retro.CAPTURING_WIDTH;
        mCapturedHeight = Retro.CAPTURING_HEIGHT;
        mEffectsNumber = 7;
        mPreviewWidth = 156;
        mPreviewHeight = 208;
        mBackgroundColor = Camera5in1.RETRO_COLOR;
        filename_title = "pic_retro_";
    }

	
}
