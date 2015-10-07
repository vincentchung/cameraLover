
package com.pictelligent.s40.camera5in1.Retro;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.EffectCaptureCanvas;

/**
 * View finder screen of Retro.
 */
public class RetroCaptureCanvas extends EffectCaptureCanvas {

    public RetroCaptureCanvas() {
        super();
	}
	
    protected static int BAR_GAP_V = 10;
    protected static int BAR_ITEM_COUNT = 3;
    protected static int VF_BORDER_STROKE_WIDTH_LEFT = 8;
    protected static int VF_BORDER_STROKE_WIDTH_TOP = 8;
    protected static int VF_BORDER_STROKE_WIDTH_RIGHT = 8;
    protected static int VF_BORDER_STROKE_WIDTH_BOTTOM = 10;
    protected static int CANVAS_Y_OFFSET = 3;
	public void init() {
		// TODO Auto-generated method stub
		mBackgroundColor = Camera5in1.RETRO_COLOR;
	}

}
