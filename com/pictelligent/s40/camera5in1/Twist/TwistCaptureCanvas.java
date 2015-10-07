
package com.pictelligent.s40.camera5in1.Twist;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.EffectCaptureCanvas;

/**
 * View finder screen of Twist.
 */
public class TwistCaptureCanvas extends EffectCaptureCanvas {

    public TwistCaptureCanvas() {
		super();
	}

	public void init() {
		mBackgroundColor = Camera5in1.TWIST_COLOR;
	}
}
