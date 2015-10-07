package com.pictelligent.s40.camera5in1.components;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.language.Language;

public class List extends UIItem{

	private int mX=0;
    private int mY=0;
	private Vector mListItemVector;
	private int mGridwidth=0;
	private ListItemClickListener mListener=null;
	
	public List()
	{
		mListItemVector=new Vector();
	}
	
	public void setGridlineWidth(int gw)
	{
		mGridwidth=gw;
	}
	
	public void addItem(int id, String imagePath, String text)
	{
		ListItem item = new ListItem(id, imagePath,text);
		addItem(item);
	}
	
	public void addItem(int id, String imagePath, String text,int unpress_color,int press_color)
	{
		ListItem item = new ListItem(id, imagePath,text);
		item.SetUnPressedColor(unpress_color);
		item.SetPressedColor(press_color);
		if (Language.LANGUAGUE_ARABIC.equalsIgnoreCase(Language.getLanguage()))
		{
			item.setRTL();
		}
		addItem(item);
	}
	
	public void addItem(ListItem item)
	{
		int index=0;
		int y=mY;
		if(mListItemVector.size()!=0)
		{
			index = mListItemVector.size()-1;
			y=(((ListItem) mListItemVector.elementAt(index)).getHeight()+((ListItem) mListItemVector.elementAt(index)).getY());
			y+=mGridwidth;
		}
		item.setPosition(mX, y);
		if(mListener!=null)
			item.setOnClickListener(mListener);
		
		mListItemVector.addElement(item);
	}
	
	public void onItemPress() {
		// TODO Auto-generated method stub
		
	}
	
	public void setPosition(int x, int y) {
        mX = x;
        mY = y;
    }

	public void onItemUnPress() {
		// TODO Auto-generated method stub
		
	}

	public void onItemTouchPress(int x, int y) {
		int size = mListItemVector.size();
        for (int i = 0; i < size; i++) {
            ((ListItem) mListItemVector.elementAt(i)).onPressed(x, y);
        }
	}

	public void onItemTouchRelease(int x, int y) {
		int size = mListItemVector.size();
        for (int i = 0; i < size; i++) {
            ((ListItem) mListItemVector.elementAt(i)).onUnPressed(x, y);
        }
	}

	public void onItemTouchDragged(int x, int y) {
		if (x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
            int size = mListItemVector.size();
            for (int i = 0; i < size; i++) {
                ((ListItem) mListItemVector.elementAt(i)).onUnPressed(x, y);
            }
        } else {
            int size = mListItemVector.size();
            for (int i = 0; i < size; i++) {
                ((ListItem) mListItemVector.elementAt(i)).onPressed(x, y);
            }
        }
	}

	public void onItemTouchPinich(int getPinchDistanceChange) {
		// TODO Auto-generated method stub
		
	}

	public void render(Graphics g) {
		for (int i = 0; i < mListItemVector.size(); i++) {
            ((ListItem) mListItemVector.elementAt(i)).render(g);
        }
	}
	
	public void setOnClickListener(ListItemClickListener listener) {
        mListener = listener;
    }
}
