
package com.pictelligent.s40.camera5in1.Twist;

import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.EffectPaintCanvas;
import com.pictelligent.s40.camera5in1.effects.EffectAlgorithms;
import com.pictelligent.s40.camera5in1.language.Language;

/**
 * Canvas to preview all effects here.
 */
class TwistEffectCanvas extends EffectPaintCanvas {
    protected Image image;

    private boolean mPreloadCache = false;
    private boolean mEnableCache = true;
    String cachefilename = "";
    private static final int max = 100;
    // private int cur =0;
    TwistMaskWarp ff;
    // private Thread animation_th;
    // private int pcounter=0;

    private int[] fullImage2;

    public TwistEffectCanvas(byte[] raw, int capturingWidth, int capturingHeight) {
        super(raw, capturingWidth, capturingHeight);
    }

    public void switchEffect() {
        switch (mEffectIndex) {
            case 0:
                // System.arraycopy(orgImage, 0, previewImage, 0,
                // orgImage.length);
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                EffectAlgorithms.RGB16to32bits(orgImage16bits, previewImage, mPreviewWidth,
                        mPreviewHeight);
                return;
            case 1:
                cachefilename = "/waveXVF16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_WAVE) + " X";
                break;
            case 2:
                cachefilename = "/waveYVF16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_WAVE) + " Y";
                break;
            case 3:
                cachefilename = "/waterVF16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_WATERDROP);
                break;
            case 4:
                cachefilename = "/twirlVF16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_TWIRL_LEFT);
                break;
            case 5:
                cachefilename = "/twirl2VF16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_TWIRL_RIGHT);
                break;
            case 6:
                cachefilename = "/squeezeVF16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_SQUEEZE);
                break;
            case 7:
                cachefilename = "/bubbleVF16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_BUBBLE);
                break;
        }

        if (b16bits) {
            System.out.println("b16bits");
            // warpFilter16(orgImage16bits, previewImage16bits, PREVIEW_WIDTH,
            // PREVIEW_HEIGHT);
            // System.out.println("warp is done");
            // EffectAlgorithms.RGB16to32bits(previewImage16bits, previewImage,
            // PREVIEW_WIDTH, PREVIEW_HEIGHT);
            warpFilter16to32(orgImage16bits, previewImage, mPreviewWidth, mPreviewHeight);
            System.out.println("RGB16to32bits is done");
        } else {
            warpFilter(orgImage, previewImage, mPreviewWidth, mPreviewHeight);
        }

    }

    public void switchEffectFull() {
        // mEnableCache=true;
        switch (mEffectIndex) {
            case 0:
                if (b16bits)
                {
                    fullImage16bitsOutput = fullImage16bits;
                } else
                {
                    System.arraycopy(fullImage, 0, fullImage2, 0, fullImage.length);
                }
                mEffectName = Language.get(Language.TEXT_EFFECT_NONE);
                return;
            case 1:
                cachefilename = "/waveX16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_WAVE) + " X";
                break;
            case 2:
                cachefilename = "/waveY16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_WAVE) + " Y";
                break;
            case 3:
                cachefilename = "/water16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_WATERDROP);
                break;
            case 4:
                cachefilename = "/twirl16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_TWIRL_LEFT);
                break;
            case 5:
                cachefilename = "/twirl216.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_TWIRL_RIGHT);
                break;
            case 6:
                cachefilename = "/squeeze16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_SQUEEZE);
                break;
            case 7:
                cachefilename = "/bubble16.wrp";
                mEffectName = Language.get(Language.TEXT_TWIST_BUBBLE);
                break;
        }

        if (b16bits) {
            if (fullImage16bitsOutput == null)
                fullImage16bitsOutput = new short[Twist.CAPTURING_WIDTH * Twist.CAPTURING_HEIGHT];

            warpFilter16(fullImage16bits, fullImage16bitsOutput, Twist.CAPTURING_WIDTH,
                    Twist.CAPTURING_HEIGHT);
        } else {
            warpFilter(fullImage, fullImage2, Twist.CAPTURING_WIDTH, Twist.CAPTURING_HEIGHT);
        }
        // mEnableCache=false;
    }

    public void warpFilter(int[] inputBuffer, int[] outputBuffer, int width, int height) {
        ff = new TwistMaskWarp();
        ff.setSize(width, height,true);
        ff.setPrecache(mPreloadCache);
        ff.setcache(mEnableCache);
        ff.setCachefile(cachefilename);
        ff.filter(inputBuffer, outputBuffer);
    }

    public void warpFilter16(short[] inputBuffer, short[] outputBuffer, int width, int height) {
        ff = new TwistMaskWarp();
        ff.setSize(width, height,false);
        ff.setPrecache(mPreloadCache);
        ff.setcache(mEnableCache);
        ff.setCachefile(cachefilename);
        ff.filter16(inputBuffer, outputBuffer);
    }

    public void warpFilter16to32(short[] inputBuffer, int[] outputBuffer, int width, int height) {
        ff = new TwistMaskWarp();
        ff.setSize(width, height,true);
        ff.setPrecache(mPreloadCache);
        ff.setcache(mEnableCache);
        ff.setCachefile(cachefilename);
        ff.filter16to32(inputBuffer, outputBuffer);
    }

    public void init() {
        mEffectsNumber = 7;
        mPreviewWidth = 156;
        mPreviewHeight = 208;
        mCapturedWidth = Twist.CAPTURING_WIDTH;
        mCapturedHeight = Twist.CAPTURING_HEIGHT;
        mBackgroundColor = Camera5in1.TWIST_COLOR;
        filename_title = "pic_twist_";
    }
}
