
package com.pictelligent.s40.camera5in1.Collage;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.ModeExitListener;
import com.pictelligent.s40.camera5in1.Rectangle;
import com.pictelligent.s40.camera5in1.Collage.CollageCaptureCanvas.CollageCaptureListener;
import com.pictelligent.s40.camera5in1.Collage.CollageModesList.CollageModeListener;

public class Collage implements CollageModeListener, CollageCaptureListener, CommandListener {

    public static final int ID_COLLAGE_MODE1 = 0;
    public static final int ID_COLLAGE_MODE2 = 1;
    public static final int ID_COLLAGE_MODE3 = 2;
    public static final int ID_COLLAGE_MODE4 = 3;
    public static final int ID_COLLAGE_MODE5 = 4;
    public static final int ID_COLLAGE_MODE6 = 5;

    private Display mDisplay;
    private ModeExitListener mModeExitListener;
    private CollageModesList mCollageModesCanvas;
    private CollageCaptureCanvas mCaptureCanvas;
    public static final int CAPTURING_WIDTH = 480 / 2;
    public static final int CAPTURING_HEIGHT = 640 / 2;

    // Declare all collage modes here
    final static Rectangle[] mode1_hint = {
            new Rectangle(29, 94, 68, 90),
            new Rectangle(138, 18, 64, 85),
            new Rectangle(137, 142, 78, 104)
    };
    final static Rectangle[] mode1_cell = {
    	new Rectangle(29, 94, 68, 90),
        new Rectangle(138, 18, 64, 85),
        new Rectangle(137, 142, 78, 104)
    };
    final static CollageMode mode1 = new CollageMode("/collage_bg", 240, 320, 3,
            mode1_cell,
            mode1_hint, new Rectangle(1, 1, 147, 315),"/collage_borders.01_out.jpg",7);

    final static Rectangle[] mode2_hint = {
            new Rectangle(41, 27, 74, 98),
            new Rectangle(60, 164, 60, 80),
            new Rectangle(156, 68, 66, 88)
    };
    final static Rectangle[] mode2_cell = {
    	new Rectangle(41, 27, 74, 98),
        new Rectangle(60, 164, 60, 80),
        new Rectangle(156, 68, 66, 88)
    };
    final static CollageMode mode2 = new CollageMode("/collage_bg", 240, 320, 3,
            mode2_cell,
            mode2_hint, new Rectangle(1, 1, 238, 185),"",7);

    final static Rectangle[] mode3_hint = {
            new Rectangle(50, 25, 66, 88),
            new Rectangle(150, 26, 53, 70),
            new Rectangle(50, 152, 66, 88),
            new Rectangle(148, 136, 64, 85)
    };
    final static Rectangle[] mode3_cell = {
    	new Rectangle(50, 25, 65, 88),
        new Rectangle(150, 26, 52, 70),
        new Rectangle(50, 152, 66, 88),
        new Rectangle(148, 136, 64, 85)
    };
    final static CollageMode mode3 = new CollageMode("/collage_bg", 240, 320, 4,
            mode3_cell,
            mode3_hint, new Rectangle(1, 1, 119, 157),"",7);

    private Command mCommandBack = new Command("Back", Command.BACK, 1);

    public Collage(Display display) {
        mDisplay = display;
    }

    private void showModeList() {
        if (mCollageModesCanvas == null) {
            mCollageModesCanvas = new CollageModesList();
            mCollageModesCanvas.setListener(this);
            mCollageModesCanvas.addCommand(mCommandBack);
            mCollageModesCanvas.setCommandListener(this);
        }
        mDisplay.setCurrent(mCollageModesCanvas);
    }

    public void display() {
        showModeList();
    }

    public void setModeExitListener(ModeExitListener listener) {
        mModeExitListener = listener;
    }

    public void onItemClicked(int mode) {
        if (Camera5in1.mDebug) {
            DebugUtil.Log("onItemClicked:mode=" + mode);
        }
        switch (mode) {
            case ID_COLLAGE_MODE1:
                mCaptureCanvas = new CollageCaptureCanvas(mode1);
                break;
              
            case ID_COLLAGE_MODE2:
                mCaptureCanvas = new CollageCaptureCanvas(mode2);
                break;
                
            case ID_COLLAGE_MODE3:
                mCaptureCanvas = new CollageCaptureCanvas(mode3);
                break;
        }
        mCaptureCanvas.setExistListener(mModeExitListener);
        mCaptureCanvas.setListener(this);
        mDisplay.setCurrent(mCaptureCanvas);
        mCaptureCanvas.startPreview();
    }

    public void onModesBackButtonPressed() {
        if (mModeExitListener != null) {
            mModeExitListener.onModeExit();
        }
    }

    public void onCaptureBackButtonPressed() {
        mDisplay.setCurrent(mCollageModesCanvas);
    }

    public void onEffectWriteFaild() {
        mModeExitListener.onStorageCheck();
    }

    public void commandAction(Command cmd, Displayable display) {
        if (cmd == mCommandBack && display == mCollageModesCanvas) {
            if (mModeExitListener != null) {
                mModeExitListener.onModeExit();
            }
        }
    }
}
