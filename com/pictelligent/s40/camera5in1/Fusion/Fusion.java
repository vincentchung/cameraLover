
package com.pictelligent.s40.camera5in1.Fusion;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.ModeExitListener;
import com.pictelligent.s40.camera5in1.Rectangle;
import com.pictelligent.s40.camera5in1.Fusion.FusionCaptureCanvas.FusionCaptureListener;
import com.pictelligent.s40.camera5in1.Fusion.FusionModesCanvas.FusionModesListener;

public class Fusion implements FusionModesListener, FusionCaptureListener, CommandListener {

    public static final int ID_FUSION_MODE1 = 0;
    public static final int ID_FUSION_MODE2 = 1;
    public static final int ID_FUSION_MODE3 = 2;
    public static final int ID_FUSION_MODE4 = 3;
    public static final int ID_FUSION_MODE5 = 4;
    public static final int ID_FUSION_MODE6 = 5;

    private Display mDisplay;
    private FusionModesCanvas mFusionModesCanvas;
    private ModeExitListener mModeExitListener;
    private FusionCaptureCanvas mFusionCaptureCanvas;

    public static final int CAPTURING_WIDTH = 480 / 2;
    public static final int CAPTURING_HEIGHT = 640 / 2;
    
    public static final int PREVIEW_WIDTH = 174;
    public static final int PREVIEW_HEIGHT = 233;

    final static Rectangle[] mode1_cell = {
            new Rectangle(0, 0, CAPTURING_WIDTH, CAPTURING_HEIGHT / 2),
            new Rectangle(0, CAPTURING_HEIGHT / 2, CAPTURING_WIDTH, CAPTURING_HEIGHT / 2)
    };
    final static Rectangle[] mode1_hint = {
            new Rectangle(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT/2),
            new Rectangle(0, PREVIEW_HEIGHT/2, PREVIEW_WIDTH, PREVIEW_HEIGHT/2)
    };
    // instruction size for image is 162x216, so...
    final static Rectangle[] mode1_tip_cell = {
            new Rectangle(0, 0, 162, 108),
            new Rectangle(0, 108, 162, 108)
    };
    FusionMode mode1;

    final static Rectangle[] mode2_cell = {
            new Rectangle(0, 0, CAPTURING_WIDTH / 2, CAPTURING_HEIGHT),
            new Rectangle(CAPTURING_WIDTH / 2, 0, CAPTURING_WIDTH / 2, CAPTURING_HEIGHT)
    };
    final static Rectangle[] mode2_hint = {
            new Rectangle(0, 0, PREVIEW_WIDTH/2, PREVIEW_HEIGHT),
            new Rectangle(PREVIEW_WIDTH/2, 0, PREVIEW_WIDTH/2, PREVIEW_HEIGHT)
    };
    final static Rectangle[] mode2_tip_cell = {
            new Rectangle(0, 0, 81, 216),
            new Rectangle(81, 0, 81, 216)
    };
    FusionMode mode2;

    final static Rectangle[] mode3_cell = {
            new Rectangle(0, 0, CAPTURING_WIDTH, CAPTURING_HEIGHT / 3),
            new Rectangle(0, CAPTURING_HEIGHT / 3, CAPTURING_WIDTH, (CAPTURING_HEIGHT / 3) * 2)
    };
    final static Rectangle[] mode3_hint = {
            new Rectangle(0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT/3),
            new Rectangle(0, PREVIEW_HEIGHT/3, PREVIEW_WIDTH, (PREVIEW_HEIGHT/3)*2)
    };
    final static Rectangle[] mode3_tip_cell = {
            new Rectangle(0, 0, 162, 82),
            new Rectangle(0, 82, 162, 134)
    };
    FusionMode mode3;

    final static Rectangle[] mode4_cell = {
            new Rectangle(0, 0, CAPTURING_WIDTH, (CAPTURING_HEIGHT / 3) * 2),
            new Rectangle(0, (CAPTURING_HEIGHT / 3) * 2, CAPTURING_WIDTH, (CAPTURING_HEIGHT / 3))
    };
    final static Rectangle[] mode4_hint = {
            new Rectangle(0, 0, PREVIEW_WIDTH, (PREVIEW_HEIGHT/3)*2),
            new Rectangle(0, (PREVIEW_HEIGHT/3)*2, PREVIEW_WIDTH, (PREVIEW_HEIGHT/3))
    };
    final static Rectangle[] mode4_tip_cell = {
            new Rectangle(0, 0, 162, 134),
            new Rectangle(0, 134, 162, 82)
    };
    FusionMode mode4;

    final static Rectangle[] mode5_cell = {
            new Rectangle(0, 0, CAPTURING_WIDTH / 3, CAPTURING_HEIGHT),
            new Rectangle(CAPTURING_WIDTH / 3, 0, (CAPTURING_WIDTH / 3) * 2, CAPTURING_HEIGHT)
    };
    final static Rectangle[] mode5_hint = {
            new Rectangle(0, 0, (PREVIEW_WIDTH)/3, PREVIEW_HEIGHT),
            new Rectangle((PREVIEW_WIDTH)/3, 0, (PREVIEW_WIDTH*2)/3, PREVIEW_HEIGHT)
    };
    final static Rectangle[] mode5_tip_cell = {
            new Rectangle(0, 0, 54, 216),
            new Rectangle(54, 0, 108, 216)
    };
    FusionMode mode5;

    final static Rectangle[] mode6_cell = {
            new Rectangle(0, 0, (CAPTURING_WIDTH / 3) * 2, CAPTURING_HEIGHT),
            new Rectangle((CAPTURING_WIDTH / 3) * 2, 0, (CAPTURING_WIDTH / 3), CAPTURING_HEIGHT)
    };
    final static Rectangle[] mode6_hint = {
            new Rectangle(0, 0, (PREVIEW_WIDTH*2)/3, PREVIEW_HEIGHT),
            new Rectangle((PREVIEW_WIDTH*2)/3, 0, (PREVIEW_WIDTH)/3, PREVIEW_HEIGHT)
    };
    final static Rectangle[] mode6_tip_cell = {
            new Rectangle(0, 0, 108, 216),
            new Rectangle(108, 0, 54, 216)
    };
    FusionMode mode6;
    private Command mCommandBack = new Command("Back", Command.BACK, 1);

    public Fusion(Display display) {
        mDisplay = display;
        try {
            Image mMode1Tip1 = Image.createImage("/fusion_instruction_1_1.png");
            Image mMode1Tip2 = Image.createImage("/fusion_instruction_1_2.png");
            Image mMode2Tip1 = Image.createImage("/fusion_instruction_2_1.png");
            Image mMode2Tip2 = Image.createImage("/fusion_instruction_2_2.png");
            Image mMode3Tip1 = Image.createImage("/fusion_instruction_3_1.png");
            Image mMode3Tip2 = Image.createImage("/fusion_instruction_3_2.png");
            Image mMode4Tip1 = Image.createImage("/fusion_instruction_4_1.png");
            Image mMode4Tip2 = Image.createImage("/fusion_instruction_4_2.png");
            Image mMode5Tip1 = Image.createImage("/fusion_instruction_5_1.png");
            Image mMode5Tip2 = Image.createImage("/fusion_instruction_5_2.png");
            Image mMode6Tip1 = Image.createImage("/fusion_instruction_6_1.png");
            Image mMode6Tip2 = Image.createImage("/fusion_instruction_6_2.png");
            mode1 = new FusionMode(null, PREVIEW_WIDTH, PREVIEW_HEIGHT, CAPTURING_WIDTH, CAPTURING_HEIGHT, 2,
                    mode1_cell,
                    mode1_hint, new Image[] {
                            mMode1Tip1, mMode1Tip2
                    }, mode1_tip_cell);
            mode2 = new FusionMode(null, PREVIEW_WIDTH, PREVIEW_HEIGHT, CAPTURING_WIDTH, CAPTURING_HEIGHT, 2,
                    mode2_cell,
                    mode2_hint, new Image[] {
                            mMode2Tip1, mMode2Tip2
                    }, mode2_tip_cell);
            mode3 = new FusionMode(null, PREVIEW_WIDTH, PREVIEW_HEIGHT, CAPTURING_WIDTH, CAPTURING_HEIGHT, 2,
                    mode3_cell,
                    mode3_hint, new Image[] {
                            mMode3Tip1, mMode3Tip2
                    }, mode3_tip_cell);
            mode4 = new FusionMode(null, PREVIEW_WIDTH, PREVIEW_HEIGHT, CAPTURING_WIDTH, CAPTURING_HEIGHT, 2,
                    mode4_cell,
                    mode4_hint, new Image[] {
                            mMode4Tip1, mMode4Tip2
                    }, mode4_tip_cell);
            mode5 = new FusionMode(null, PREVIEW_WIDTH, PREVIEW_HEIGHT, CAPTURING_WIDTH, CAPTURING_HEIGHT, 2,
                    mode5_cell,
                    mode5_hint, new Image[] {
                            mMode5Tip1, mMode5Tip2
                    }, mode5_tip_cell);
            mode6 = new FusionMode(null, PREVIEW_WIDTH, PREVIEW_HEIGHT, CAPTURING_WIDTH, CAPTURING_HEIGHT, 2,
                    mode6_cell,
                    mode6_hint, new Image[] {
                            mMode6Tip1, mMode6Tip2
                    }, mode6_tip_cell);
            System.out.println("fusion init");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display() {
        showModesList();
    }

    public void showModesList() {
    	System.out.println("showModesList");
        if (mFusionModesCanvas == null) {
            mFusionModesCanvas = new FusionModesCanvas();
            mFusionModesCanvas.setListener(this);
            mFusionModesCanvas.addCommand(mCommandBack);
            mFusionModesCanvas.setCommandListener(this);
        }
        mDisplay.setCurrent(mFusionModesCanvas);
    }

    public void setModeExitListener(ModeExitListener listener) {
        mModeExitListener = listener;
    }

    public void startPreview(FusionMode mode) {
        if (mFusionCaptureCanvas == null) {
            mFusionCaptureCanvas = new FusionCaptureCanvas();
            mFusionCaptureCanvas.setListener(this);
            mFusionCaptureCanvas.setExistListener(mModeExitListener);
        }
        mFusionCaptureCanvas.setMode(mode);
        mDisplay.setCurrent(mFusionCaptureCanvas);
    }

    public void onItemClicked(int item) {
        switch (item) {
            case ID_FUSION_MODE1:
                startPreview(mode1);
                break;
            case ID_FUSION_MODE2:
                startPreview(mode2);
                break;
            case ID_FUSION_MODE3:
                startPreview(mode3);
                break;
            case ID_FUSION_MODE4:
                startPreview(mode4);
                break;
            case ID_FUSION_MODE5:
                startPreview(mode5);
                break;
            case ID_FUSION_MODE6:
                startPreview(mode6);
                break;
        }
    }

    public void onCaptureBackButtonPressed() {
        mDisplay.setCurrent(mFusionModesCanvas);
    }

    

    public void commandAction(Command c, Displayable d) {
        if (c == mCommandBack) {
            if (mModeExitListener != null) {
            	mFusionModesCanvas=null;
                mModeExitListener.onModeExit();
            }
        }
    }
}
