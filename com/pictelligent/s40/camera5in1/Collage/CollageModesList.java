
package com.pictelligent.s40.camera5in1.Collage;

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

public class CollageModesList extends Canvas implements GridItemClickListener {

    public interface CollageModeListener {
        void onItemClicked(int callerId);
    }

    private Vector mGridItemsVector;
    private CollageModeListener mCollageModeListener;
    private Image mTitleBar;

    public CollageModesList() {
        setFullScreenMode(false);
        initItem();
    }

    private void initItem() {
        try {
            mTitleBar = Image.createImage("/ic_collage_titlebar.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGridItemsVector = new Vector();
        mGridItemsVector
                .addElement(new GridItem(Collage.ID_COLLAGE_MODE1, "/ic_collage_mode_1.png", Camera5in1.COLLAGE_COLOR, Camera5in1.COLLAGE_DARK_COLOR));
        mGridItemsVector
                .addElement(new GridItem(Collage.ID_COLLAGE_MODE2, "/ic_collage_mode_2.png", Camera5in1.COLLAGE_COLOR, Camera5in1.COLLAGE_DARK_COLOR));
        mGridItemsVector
                .addElement(new GridItem(Collage.ID_COLLAGE_MODE3, "/ic_collage_mode_3.png", Camera5in1.COLLAGE_COLOR, Camera5in1.COLLAGE_DARK_COLOR));
        
        int size = mGridItemsVector.size();

        for (int i = 0; i < 3; i++) {
            GridItem item = (GridItem) mGridItemsVector.elementAt(i);
            item.setOnClickListener(this);
            item.setPosition(i % 3 * (getWidth() / 3), 100,
                    getWidth() / 3, 110);
            item.setString(Language.get(Language.TEXT_COLLAGE_MODES_1 + i));
        }
        
        repaint();
    }

    protected void paint(Graphics g) {
        if (Camera5in1.mDebug) {
            DebugUtil.Log("paint");
        }

        // Draw background
        g.setColor(Camera5in1.COLLAGE_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Title Bar
        g.drawImage(mTitleBar, 0, 0, Graphics.LEFT | Graphics.TOP);
        
        g.setColor(0, 0, 0);
        Font font = Font.getFont(Camera5in1.mFontFace ,Camera5in1.mFontType, Camera5in1.mFontSize);
        g.setColor(0xEEEEEE);
        g.setFont(font);
        int x = (getWidth() - g.getFont().stringWidth(Language.get(Language.TEXT_COLLAGE_MODES))) / 2;
        int y = ( mTitleBar.getHeight() - g.getFont().getHeight()) / 2;
        g.drawString(Language.get(Language.TEXT_COLLAGE_MODES), x, y, 0);

        // Items
        for (int i = 0; i < mGridItemsVector.size(); i++) {
            ((GridItem) mGridItemsVector.elementAt(i)).render(g);
        }
    }

    public void setListener(CollageModeListener listener) {
        mCollageModeListener = listener;
    }

    protected void pointerPressed(int x, int y) {
        System.out.println("_pointerPressed");
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
        if (mCollageModeListener != null) {
            mCollageModeListener.onItemClicked(callerId);
        }
    }
}
