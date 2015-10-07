
package com.pictelligent.s40.camera5in1;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import com.nokia.mid.ui.VirtualKeyboard;
import com.pictelligent.s40.camera5in1.Camera5in1Canvas.MainListListener;
import com.pictelligent.s40.camera5in1.Collage.Collage;
import com.pictelligent.s40.camera5in1.Fusion.Fusion;
import com.pictelligent.s40.camera5in1.Futuro.Futuro;
import com.pictelligent.s40.camera5in1.Retro.Retro;
import com.pictelligent.s40.camera5in1.Twist.Twist;
import com.pictelligent.s40.camera5in1.language.Language;

/**
 * Main and the only midlet of this application. Including all the
 * modes:Retro,Fusion,Collage,Twist.
 */
public class Camera5in1 extends MIDlet implements ModeExitListener, CommandListener,
        MainListListener {

    // CHECK AND UPDATE THESE SETTINGS BEFORE MAKING AN OFFICIAL BUILD!
    // CHECK AND UPDATE THESE SETTINGS BEFORE MAKING AN OFFICIAL BUILD!
    // CHECK AND UPDATE THESE SETTINGS BEFORE MAKING AN OFFICIAL BUILD!

    // The following three booleans should all be set to false for official
    // builds!
    public static final boolean mDebug = false; // enable debug prints
    public static final boolean mDebugVerbose = false; // enable verbose debug
                                                       // prints
    public static final boolean mEmulator = false; // set capturing size
                                                  // suitable for emulator
    public static final boolean mTestMode = false; // enable test code

    public static final String mVersionNum = "version 1.0.0";
    public static final String mVersionDate = "13 Jun 2013";
    // CHECK AND UPDATE THESE SETTINGS BEFORE MAKING AN OFFICIAL BUILD!
    // CHECK AND UPDATE THESE SETTINGS BEFORE MAKING AN OFFICIAL BUILD!
    // CHECK AND UPDATE THESE SETTINGS BEFORE MAKING AN OFFICIAL BUILD!
    public static final int mFontType=Font.STYLE_BOLD;
    public static final int mFontSize=Font.SIZE_LARGE;
    public static final int mFontFace=Font.FACE_MONOSPACE;
    
    public static final int RETRO_COLOR = 0x00df1978;
    public static final int RETRO_DARK_COLOR = 0x00ad135d;
    public static final int COLLAGE_COLOR = 0x00b01ba4;
    public static final int COLLAGE_DARK_COLOR = 0x0076126e;
	public static final int TWIST_COLOR = 0x00009ebd;
    public static final int TWIST_DARK_COLOR = 0x0000738a;
	public static final int FUSION_COLOR = 0x0084bd33;
    public static final int FUSION_DARK_COLOR = 0x00638e26;
	public static final int FUTURO_COLOR = 0x00c1d73b;
    public static final int FUTURO_DARK_COLOR = 0x0094a52d;
	
    private static final String OVI_STORE_URL = "http://store.nokia.mobi";
    private static final String PICTELLIGENT_URL = "http://www.pictelligent.com/EULA/S40Camera5in1-"
            + System.getProperty("microedition.locale") + ".html";
    public static final int ID_RETRO = 0;
    public static final int ID_COLLAGE = 1;
    public static final int ID_TWIST = 2;
    public static final int ID_FUSION = 3;
    public static final int ID_FUTURO = 4;
    
    private static Display mDisplay;
    private Retro mRetro;
    private Collage mCollage;
    private Fusion mFusion;
    private Twist mTwist;
    private Futuro mFuturo;
    private static final int mthrashhold = 300 * 1024;// 300kb
    public static final int DEFAULT_ALERT_TIMEOUT = 3000;
    static Alert Erroralert = new Alert("Security Error", "", null, AlertType.ERROR);
    static Alert Savingalert = new Alert("saving file", "", null, AlertType.INFO);
    private Command dismissAlertCmd = new Command("EXIT", Command.EXIT, 1);
    private Command continueAlertCmd = new Command("OK", Command.OK, 1);
    private static Command errorAlertCmd = new Command("Exit", Command.EXIT, 1);
    private Camera5in1Canvas mCamera5in1Canvas;

    private Command mHelpCommand;
    private Command mAboutCommand;
    private Command mStoreCommand;
    private Command mEulaCommand;
    private Command mBackCommand;
    private Form mHelpScreen;
    private Form mAboutScreen;

    public Camera5in1() {
        if (mDebug)
            DebugUtil.InitDebugLog();
        mDisplay = Display.getDisplay(this);
        initializeLanguagePack();
        initUiResources();

        try {
            Class.forName("com.nokia.mid.ui.VirtualKeyboard");
            VirtualKeyboard.hideOpenKeypadCommand(true);
        }
        catch (ClassNotFoundException e) {
        }
        
        if (mDebug) {
            DebugUtil.Log("Camera 5-in-1 startup");
        }
    }

    protected void destroyApp(boolean unconditional) {
        if (mDebug)
            DebugUtil.CloseDebugLog();
    }

    protected void pauseApp() {
        if (mDebug)
            DebugUtil.Log("pauseApp");
    }

    protected void startApp() throws MIDletStateChangeException {
        if (mDebug) {
            FileConnection con = null;
            try {
                con = (FileConnection) Connector.open(System
                        .getProperty("fileconn.dir.photos"));
                String test = "";
                test = test + "availableSize:" + con.availableSize();
                Alert alert = new Alert("Info", test, null, AlertType.CONFIRMATION);
                alert.addCommand(continueAlertCmd);
                alert.setCommandListener(this);
                alert.setTimeout(DEFAULT_ALERT_TIMEOUT);
                mDisplay.setCurrent(alert);
                con.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mDisplay.setCurrent(mCamera5in1Canvas);
        }
    }

    public void commandAction(Command command, Displayable _displayable) {

        if (command == dismissAlertCmd) {
            destroyApp(false);
            notifyDestroyed();
        } else if (command == continueAlertCmd) {
            // mDisplay.setCurrent(mMainList);
            mDisplay.setCurrent(mCamera5in1Canvas);
        } else if (command == errorAlertCmd) {
            
            
            destroyApp(false);
              super.notifyDestroyed();
              
        } else if (command == mHelpCommand) {
            mDisplay.setCurrent(mHelpScreen);
        } else if (command == mAboutCommand) {
            mDisplay.setCurrent(mAboutScreen);
        } else if (command == mStoreCommand) {
            launchURL(OVI_STORE_URL);
        } else if (command == mEulaCommand) {
            launchURL(PICTELLIGENT_URL);
        } else if (command == mBackCommand
                && (_displayable == mHelpScreen || _displayable == mAboutScreen)) {
            mDisplay.setCurrent(mCamera5in1Canvas);
        } else if (command == mBackCommand && _displayable == mCamera5in1Canvas) {
            destroyApp(false);
            notifyDestroyed();
        } else {
            // unknown command
            throw new RuntimeException("Internal error #29");
        }
    }

    private void initializeLanguagePack() {
        String lp = System.getProperty("microedition.locale");
        if (Camera5in1.mDebug)
            DebugUtil.Log("locale:" + lp);
        Language.loadLanguage(lp);
    }
    
    private void recylce()
    {
    	mRetro = null;
        mCollage = null;
        mFusion = null;
        mTwist = null;
        mFuturo = null;
        System.gc();
    }

    private void showRetro() {
        if (mDebug)
            if (Camera5in1.mDebug)
                DebugUtil.Log("_showRetroMode");

        recylce();
        if (mRetro == null) {
            mRetro = new Retro(mDisplay);
            mRetro.setModeExitListener(this);
        }
        mRetro.display();
    }

    private void showFuturo() {
        if (mDebug)
            if (Camera5in1.mDebug)
                DebugUtil.Log("_showFuturoMode");

        // cleaning other feature objects
        recylce();
        if (mFuturo == null) {
            mFuturo = new Futuro(mDisplay);
            mFuturo.setModeExitListener(this);
        }
        mFuturo.display();

    }

    public void launchURL(String url) {
        try {
            if (platformRequest(url)) {
                if (Camera5in1.mDebug)
                    DebugUtil.Log("midlet exit required");
            }
            else {
                if (Camera5in1.mDebug)
                    DebugUtil.Log("midlet exit NOT required");
            }
        } catch (ConnectionNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showCollage() {
        if (mDebug)
            DebugUtil.Log("_showCollageList");

        recylce();
        if (mCollage == null) {
            mCollage = new Collage(mDisplay);
            mCollage.setModeExitListener(this);
        }
        mCollage.display();
    }

    private void showFusion() {
        if (mDebug)
            DebugUtil.Log("_showFusionMode");

        recylce();
        if (mFusion == null) {
            mFusion = new Fusion(mDisplay);
            mFusion.setModeExitListener(this);
        }
        mFusion.display();
    }

    private void showTwist() {
        if (mDebug)
            DebugUtil.Log("_showTwistMode");

        recylce();
        if (mTwist == null) {
            mTwist = new Twist(mDisplay);
            mTwist.setModeExitListener(this);
        }
        mTwist.display();
    }

    public void onModeExit() {
    	System.out.println("onModeExit");
        mDisplay.setCurrent(mCamera5in1Canvas);
    }

    private void initUiResources() {
        // Main list screen
        String[] stringElements = new String[] {
                Language.get(Language.TEXT_RETRO), Language.get(Language.TEXT_COLLAGE),
                Language.get(Language.TEXT_TWIST), Language.get(Language.TEXT_FUSION),
                Language.get(Language.TEXT_FUTURO)
        };

        for (int i = 0; i < stringElements.length; i++) {
            System.out.println("index " + i + ":" + stringElements[i]);
        }
        mHelpCommand = new Command(Language.get(Language.TEXT_MENU_HELP), Command.ITEM, 1);
        mAboutCommand = new Command(Language.get(Language.TEXT_MENU_ABOUT), Command.ITEM, 2);
        mStoreCommand = new Command(Language.get(Language.TEXT_MENU_NOKIASTORE), Command.ITEM, 3);
        mEulaCommand = new Command(Language.get(Language.TEXT_MENU_EULA), Command.ITEM, 4);
        mBackCommand = new Command(Language.get(Language.TEXT_MENU_BACK), Command.BACK, 5);

        // Help screen
        mHelpScreen = new Form(Language.get(Language.TEXT_APPNAME));
        mHelpScreen.append(new StringItem("", Language.get(Language.TEXT_MAIN_HELP),Item.PLAIN));
        mHelpScreen.addCommand(mBackCommand);
        mHelpScreen.setCommandListener(this);

        // About screen
        mAboutScreen = new Form(Language.get(Language.TEXT_APPNAME));
        mAboutScreen.append(new StringItem("", Language.get(Language.TEXT_ABOUT) + "\n"
                + Camera5in1.mVersionNum
                + "\n" + Camera5in1.mVersionDate + Language.get(Language.TEXT_ABOUT2)));
        mAboutScreen.addCommand(mBackCommand);
        mAboutScreen.setCommandListener(this);

        // Error screen
        Erroralert.addCommand(errorAlertCmd);
        Erroralert.setCommandListener(this);
        Erroralert.setTimeout(Alert.FOREVER);

        // Main list canvas
        
        mCamera5in1Canvas = new Camera5in1Canvas();
        mCamera5in1Canvas.addCommand(mHelpCommand);
        mCamera5in1Canvas.addCommand(mAboutCommand);
        //mCamera5in1Canvas.addCommand(mStoreCommand);
        mCamera5in1Canvas.addCommand(mEulaCommand);
        mCamera5in1Canvas.addCommand(mBackCommand);
        mCamera5in1Canvas.setCommandListener(this);
        mCamera5in1Canvas.setListener(this);
    }
    
    
    public static void display_saving(String str) {
        Savingalert.setString(str);
        mDisplay.setCurrent(Savingalert);
    }
    
    public static void display_error(String error_str) {
    	System.out.println(error_str);
        Erroralert.setString(error_str);
        mDisplay.setCurrent(Erroralert);
    }

    public boolean onStorageCheck() {
    	if(mEmulator)
    	return true;
    	
        boolean check = true;
        FileConnection con = null;
        try {
            con = (FileConnection) Connector.open(System
                    .getProperty("fileconn.dir.photos"));
            if (mthrashhold > con.availableSize()) {
                check = false;
                String test = Language.get(Language.TEXT_MEMORYFULL);
                Alert alert = new Alert("Info", test, null, AlertType.ERROR);
                alert.addCommand(dismissAlertCmd);
                alert.setCommandListener(this);
                alert.setTimeout(Alert.FOREVER);
                mDisplay.setCurrent(alert);
            }
            con.close();
        } catch (Exception e) {
           Camera5in1.display_error(e.getMessage());
        }
        return check;
        
    }

    public void onItemClicked(int itemId) {
    	/*Timer StorageCheckTimer = new Timer();
    	StorageCheckTimer.schedule(new TimerTask() {

            public void run() {
            	onStorageCheck();
            }
        }, 500);
        */
    	if (onStorageCheck())
    	{
    		switch(itemId) {
            case ID_RETRO:
                showRetro();
                break;
            case ID_COLLAGE:
                showCollage();
                break;
            case ID_TWIST:
                showTwist();
                break;
            case ID_FUSION:
                showFusion();
                break;
            case ID_FUTURO:
                showFuturo();
                break;
        }
    	}
        
    }
}
