
package com.pictelligent.s40.camera5in1.Fusion;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.components.GridItem;
import com.pictelligent.s40.camera5in1.components.GridItemClickListener;
import com.pictelligent.s40.camera5in1.language.Language;

public class FusionModesCanvas extends Canvas implements GridItemClickListener {

    public interface FusionModesListener {
        void onItemClicked(int callerId);
    }
    
    private Vector mGridItemsVector;
    private Image mTitleBar;
    private FusionModesListener mFusionModeListener;

    public FusionModesCanvas() {
        setFullScreenMode(false);
        initItem();
    }

    private void initItem() {
    	System.out.println("init");
        try {
            mTitleBar = Image.createImage("/ic_fusion_titlebar.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGridItemsVector = new Vector();
        mGridItemsVector
                .addElement(new GridItem(Fusion.ID_FUSION_MODE1, "/ic_fusion_mode_1.png", Camera5in1.FUSION_COLOR, Camera5in1.FUSION_DARK_COLOR));
        mGridItemsVector
                .addElement(new GridItem(Fusion.ID_FUSION_MODE2, "/ic_fusion_mode_2.png", Camera5in1.FUSION_COLOR, Camera5in1.FUSION_DARK_COLOR));
        mGridItemsVector
                .addElement(new GridItem(Fusion.ID_FUSION_MODE3, "/ic_fusion_mode_3.png", Camera5in1.FUSION_COLOR, Camera5in1.FUSION_DARK_COLOR));
        mGridItemsVector
                .addElement(new GridItem(Fusion.ID_FUSION_MODE4, "/ic_fusion_mode_4.png", Camera5in1.FUSION_COLOR, Camera5in1.FUSION_DARK_COLOR));
        mGridItemsVector
                .addElement(new GridItem(Fusion.ID_FUSION_MODE5, "/ic_fusion_mode_5.png", Camera5in1.FUSION_COLOR, Camera5in1.FUSION_DARK_COLOR));
        mGridItemsVector
                .addElement(new GridItem(Fusion.ID_FUSION_MODE6, "/ic_fusion_mode_6.png", Camera5in1.FUSION_COLOR, Camera5in1.FUSION_DARK_COLOR));
        
        int size = mGridItemsVector.size();
        for (int i = 0; i < size; i++) {
            GridItem item = (GridItem) mGridItemsVector.elementAt(i);
            item.setOnClickListener(this);
            item.setPosition(i % 3 * (getWidth() / 3), mTitleBar.getHeight() + i / 3 * 110,
                    getWidth() / 3, 110);
            item.setString(Language.get(Language.TEXT_FUSION_MODES_1+i));
        }
        //repaint();
    }

    public void paint(Graphics g) {
        if (Camera5in1.mDebug) {
            DebugUtil.Log("paint");
        }
        // Draw background
        g.setColor(Camera5in1.FUSION_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Title Bar
        g.drawImage(mTitleBar, 0, 0, Graphics.LEFT | Graphics.TOP);       
        
        Font font = Font.getFont(Camera5in1.mFontFace ,Camera5in1.mFontType, Camera5in1.mFontSize);
        g.setColor(0xEEEEEE);
        g.setFont(font);
        int x=(getWidth()-font.stringWidth(Language.get(Language.TEXT_FUSION_MODES)))/2;
        int y = ( mTitleBar.getHeight() - g.getFont().getHeight()) / 2;
        g.drawString(Language.get(Language.TEXT_FUSION_MODES), x, y, 0);

        // Items
        for (int i = 0; i < mGridItemsVector.size(); i++) {
        	((GridItem) mGridItemsVector.elementAt(i)).render(g);
        }
    }

    public void setListener(FusionModesListener listener) {
        mFusionModeListener = listener;
    }

    protected void pointerPressed(int x, int y) {
        System.out.println("f_pointerPressed");
        int size = mGridItemsVector.size();
        for (int i = 0; i < size; i++) {
            ((GridItem) mGridItemsVector.elementAt(i)).onPressed(x, y);
        }
        repaint();
    }

    protected void pointerReleased(int x, int y) {
        System.out.println("_pointerReleased:x=" + x + ",y=" + y);
        int size = mGridItemsVector.size();
        for (int i = 0; i < size; i++) {
            ((GridItem) mGridItemsVector.elementAt(i)).onUnPressed(x, y);
        }
        repaint();
    }

    protected void pointerDragged(int x, int y) {
        System.out.println("_pointerDragged:x=" + x + ",y=" + y);
        if (x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
            int size = mGridItemsVector.size();
            for (int i = 0; i < size; i++) {
                ((GridItem) mGridItemsVector.elementAt(i)).onUnPressed(x, y);
            }
            repaint();
        } else {
            int size = mGridItemsVector.size();
            for (int i = 0; i < size; i++) {
                ((GridItem) mGridItemsVector.elementAt(i)).onPressed(x, y);
            }
            repaint();
        }
    }
    
    public void onClick(int callerId) {
        if (mFusionModeListener != null) {
            mFusionModeListener.onItemClicked(callerId);
        }
    }
}
