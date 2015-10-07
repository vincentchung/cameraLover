
package com.pictelligent.s40.camera5in1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class DebugUtil {

    private static boolean mDebugToSystemOut = true;

    private static FileConnection conDebugDump = null;
    private static OutputStream osDebugDump = null;
    private static OutputStreamWriter debugFileDump = null;

    private static FileConnection conDebugLog = null;
    private static OutputStream osDebugLog = null;
    private static OutputStreamWriter debugFileLog = null;

    // creates a new debug log to SDcard root
    public static void InitDebugLog() {
        String filename = "PicCamera_" + System.currentTimeMillis() + ".txt";
        try {
            conDebugLog = (FileConnection) Connector.open(System
                    .getProperty("fileconn.dir.memorycard") + filename);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (conDebugLog != null) {
            if (conDebugLog.exists()) {
                try {
                    conDebugLog.delete();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                conDebugLog.create();
                osDebugLog = conDebugLog.openOutputStream();
                debugFileLog = new OutputStreamWriter(osDebugLog);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log("Debug log opened");
    }

    public static void Log(String debugStr)
    {
    	if(Camera5in1.mDebug)
    	{
    		if (mDebugToSystemOut)
                System.out.println(debugStr);
            try {
                debugFileLog.write(System.currentTimeMillis() + " " + debugStr + "\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    	}
    }

    public static void CloseDebugLog()
    {
        Log("Debug log closed");

        try {
            if (debugFileLog != null) {
                debugFileLog.flush();
                debugFileLog.close();
            }
            if (osDebugLog != null) {
                osDebugLog.close();
            }
            if (conDebugLog != null) {
                conDebugLog.close();
            }
        } catch (IOException io) {
            System.out.println("Error closing debug log: " + io.getMessage());
        }
    }

    // function for dumping data arrays into files

    public static void OpenDebugFile(String filename)
    {
        try {
            conDebugDump = (FileConnection) Connector.open(System
                    .getProperty("fileconn.dir.photos") + filename);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (conDebugDump != null) {
            if (conDebugDump.exists()) {
                try {
                    conDebugDump.delete();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                conDebugDump.create();
                osDebugDump = conDebugDump.openOutputStream();
                debugFileDump = new OutputStreamWriter(osDebugDump);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void WriteToDebugFile(int data[])
    {
        char dataBuf[] = new char[data.length];
        for (int i = 0; i < data.length; i++)
            dataBuf[i] = (char) (data[i] & 0xFF);

        try {
            debugFileDump.write(dataBuf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void WriteToDebugFile(int data[][], int x, int y)
    {
        char dataBuf[] = new char[x * y];
        int index = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                dataBuf[index] = (char) (data[i][j] & 0xFF);
                index++;
            }
        }

        try {
            debugFileDump.write(dataBuf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void WriteToDebugFile(short data[][], int x, int y)
    {
        char dataBuf[] = new char[x * y];
        int index = 0;
        for (int i = 0; i < dataBuf.length; i++) {
            for (int j = 0; j < data.length; j++) {
                dataBuf[index] = (char) (data[i][j] & 0xFF);
                index++;
            }
        }

        try {
            debugFileDump.write(dataBuf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void CloseDebugFile()
    {
        try {
            if (debugFileDump != null) {
                debugFileDump.close();
            }
            if (osDebugDump != null) {
                osDebugDump.close();
            }
            if (conDebugDump != null) {
                conDebugDump.close();
            }
        } catch (IOException io) {
        }
    }

}
