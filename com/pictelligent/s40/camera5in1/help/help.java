
package com.pictelligent.s40.camera5in1.help;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;

import com.pictelligent.s40.camera5in1.language.Language;
import com.pictelligent.s40.camera5in1.DeviceKeycode;
import com.pictelligent.s40.camera5in1.ModeExitListener;
import com.pictelligent.s40.camera5in1.Camera5in1Canvas.MainListListener;
import com.pictelligent.s40.camera5in1.Camera5in1;
import com.pictelligent.s40.camera5in1.DebugUtil;

public final class help implements CommandListener {

    private Display mDisplay;
    private ModeExitListener mModeExitListener = null;
    private MainListListener mModeScreenListener = null;
    private List openMenu;
    private Command backcmd;
    private Command selcmd;
    private static final int mHelpIdx = 0;
    private static final int mAboutIdx = 1;
    private static final int mStoreIdx = 2;
    private static final int mEULAIdx = 3;
    private static final String mPicstr = "";
    private static final String ovistoreurl = "http://store.nokia.mobi";
    private static final String pictelligenturl = "http://www.pictelligent.com/EULA/S40Camera5in1-"+System.getProperty("microedition.locale")+".html";
    private int mCurrent=-1;

    private Form textBox;

    public help(Display display) {
        mDisplay = display;
        if (Camera5in1.mDebug)
            DebugUtil.Log("help");
    }

    public void showModeList() {
        backcmd = new Command(Language.get(Language.TEXT_MENU_BACK), Command.BACK, 0);
        selcmd = new Command(Language.get(Language.TEXT_MENU_SELECT), Command.OK, 0);
        openMenu = new List(Language.get(Language.TEXT_APPNAME), 3);
        openMenu.deleteAll();
        openMenu.append(Language.get(Language.TEXT_MENU_HELP), null);
        openMenu.append(Language.get(Language.TEXT_MENU_ABOUT), null);
        openMenu.append(Language.get(Language.TEXT_MENU_NOKIASTORE), null);
        openMenu.append(Language.get(Language.TEXT_MENU_EULA), null);
        //openMenu.append(mPicstr, null);
        openMenu.addCommand(backcmd);
        openMenu.addCommand(selcmd);
        openMenu.setSelectCommand(selcmd);
        openMenu.setCommandListener(this);
        mDisplay.setCurrent(openMenu);
    }

    protected void keyPressed(int keyCode) {
        switch (keyCode)
        {
            case DeviceKeycode.KEY_CODE_2:

                if ((openMenu.getSelectedIndex()) == 0)
                {
                    openMenu.setSelectedIndex(openMenu.size() - 1, true);
                } else
                {
                    openMenu.setSelectedIndex(openMenu.getSelectedIndex() - 1, true);
                }
                break;
            // fucking back key
            case DeviceKeycode.KEY_CODE_3:
                commandAction(backcmd, openMenu);
                break;
            case DeviceKeycode.KEY_CODE_5:
                commandAction(selcmd, openMenu);
                break;
            case DeviceKeycode.KEY_CODE_8:
                if ((openMenu.getSelectedIndex() + 1) == openMenu.size())
                {
                    openMenu.setSelectedIndex(0, true);
                } else
                {
                    openMenu.setSelectedIndex(openMenu.getSelectedIndex() + 1, true);
                }
                break;
            default:
                break;
        }
    }

    public void display() {
        if (mModeScreenListener == null) // when does this happen?
        {
            showHelpCanvas(Language.get(Language.TEXT_MAIN_HELP));
        }
        else
        {
            showModeList();
        }

    }

    public void Canvasback() {
        if (mModeScreenListener == null)
        {
            if (mModeExitListener != null) {
                mModeExitListener.onModeExit();
            }
        }
        else
        {
            showModeList();
        }
    }

    public void setModeExitListener(ModeExitListener listener) {
        mModeExitListener = listener;
    }

    public void setModeScreenListener(MainListListener listener) {
        mModeScreenListener = listener;
    }

    public void onBackButtonPressed() {
        // if (mModeExitListener != null) {
        // mModeExitListener.onModeExit();
        // }
        // exit();
        mDisplay.setCurrent(openMenu);
    }

    private void launchURL(String url) {
//        mModeScreenListener.launchURL(url);
    }
    

    private void showHelpCanvas(String content) {
    	StringItem temp=new StringItem("", content,Item.HYPERLINK);
        textBox = new Form(Language.get(Language.TEXT_APPNAME));
        textBox.append(temp);
        textBox.addCommand(backcmd);
        textBox.setCommandListener(this);
        mDisplay.setCurrent(textBox);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == selcmd && d == openMenu) {
        	mCurrent = openMenu.getSelectedIndex();
            switch (mCurrent)
            {
                case mHelpIdx:
                    showHelpCanvas(Language.get(Language.TEXT_MAIN_HELP));
                    break;
                case mAboutIdx:
                    showHelpCanvas(Language.get(Language.TEXT_ABOUT) + "\n" + Camera5in1.mVersionNum
                            + "\n" + Camera5in1.mVersionDate + Language.get(Language.TEXT_ABOUT2));
                    break;
                case mStoreIdx:
                	launchURL(ovistoreurl);
                    mCurrent=-1;
                    break;
                case mEULAIdx:
                	launchURL(pictelligenturl);
                    mCurrent=-1;
                    break;

            }
        } else if (c == backcmd)
        {	
        	if(mCurrent==-1)
        	{
        		mModeExitListener.onModeExit();
        	}else
        	{
        		mCurrent=-1;
        		mDisplay.setCurrent(openMenu);
        	}
            
        }
    }

    public void onOkButtonPressed() {
        // TODO Auto-generated method stub
        mDisplay.setCurrent(openMenu);
    }
}
