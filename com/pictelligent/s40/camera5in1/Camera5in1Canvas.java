
package com.pictelligent.s40.camera5in1;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
//import javax.microedition.lcdui.List;

import com.pictelligent.s40.camera5in1.components.List;
import com.pictelligent.s40.camera5in1.components.ListItem;
import com.pictelligent.s40.camera5in1.components.ListItemClickListener;
import com.pictelligent.s40.camera5in1.language.Language;

/**
 * Canvas for the first screen. From this screen, we can choose different modes:
 * Retro, Collage, Twist, Fusion and Futuro.
 */
public class Camera5in1Canvas extends Canvas implements ListItemClickListener {

    /**
     * Listener for this canvas.
     */
    public interface MainListListener {
        void onItemClicked(int itemId);
    }


    private Image mTitleBar;
    private MainListListener mListener;
    //private Vector mListItemVector;
    private List mList;

    public Camera5in1Canvas() {
        setFullScreenMode(true);
        if (Camera5in1.mDebug) {
            DebugUtil.Log("Camera5in1Canvas");
        }
        init();
    }

    private void init() {
        try {
            mTitleBar = Image.createImage("/titlebars_main.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mList=new List();
        mList.setPosition(0, mTitleBar.getHeight());
        mList.setGridlineWidth(0);
        mList.setOnClickListener(this);
       
        mList.addItem(Camera5in1.ID_RETRO, "/ic_retro_mainmenu.png",
                Language.get(Language.TEXT_RETRO),Camera5in1.RETRO_COLOR,Camera5in1.RETRO_DARK_COLOR);
        mList.addItem(Camera5in1.ID_COLLAGE, "/ic_collage_mainmenu.png",
                Language.get(Language.TEXT_COLLAGE),Camera5in1.COLLAGE_COLOR,Camera5in1.COLLAGE_DARK_COLOR);
        mList.addItem(Camera5in1.ID_TWIST, "/ic_twist_mainmenu.png",
                Language.get(Language.TEXT_TWIST),Camera5in1.TWIST_COLOR,Camera5in1.TWIST_DARK_COLOR);
        mList.addItem(Camera5in1.ID_FUSION, "/ic_fusion_mainmenu.png",
                Language.get(Language.TEXT_FUSION),Camera5in1.FUSION_COLOR,Camera5in1.FUSION_DARK_COLOR);
        mList.addItem(Camera5in1.ID_FUTURO, "/ic_futuro_mainmenu.png",
                Language.get(Language.TEXT_FUTURO),Camera5in1.FUTURO_COLOR,Camera5in1.FUTURO_DARK_COLOR);
        repaint();
    }

    protected void paint(Graphics g) {
        if (Camera5in1.mDebug) {
            DebugUtil.Log("paint, canvas size: w=" + getWidth() + ",h=" + getHeight());
        }

        // Draw background
        //R:7,G:172,B:187
        //g.setColor(0x07ACBB);
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Title Bar
        g.drawImage(mTitleBar, 0, 0, 0);
        mList.render(g);
    }

    public void setListener(MainListListener mainScreenListener) {
        mListener = mainScreenListener;
    }

    protected void pointerPressed(int x, int y) {
    	mList.onItemTouchPress(x, y);
        repaint();
    }

    protected void pointerReleased(int x, int y) {
    	mList.onItemTouchRelease(x, y);
        repaint();
    }

    protected void pointerDragged(int x, int y) {
    	mList.onItemTouchDragged(x, y);
    	repaint();
    }

    public void onClick(final int callerId) {
        switch (callerId) {
            case Camera5in1.ID_RETRO:
            case Camera5in1.ID_COLLAGE:
            case Camera5in1.ID_TWIST:
            case Camera5in1.ID_FUSION:
            case Camera5in1.ID_FUTURO:
            {
            	Timer StorageCheckTimer = new Timer();
            	StorageCheckTimer.schedule(new TimerTask() {

                    public void run() {
                    	mListener.onItemClicked(callerId);
                    }
                }, 100);
            }
                
                break;
            default:
                break;
        }
    }
}
