
package com.pictelligent.s40.camera5in1;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import com.pictelligent.s40.camera5in1.JPEGEncoder.JpegEncoder;

public class SystemUtil {
    private static FileConnection con = null;
    private static OutputStream os = null;
    private static OutputStreamWriter log = null;
    private static boolean mPerf = Camera5in1.mDebug;

    public static void Log_current_time_init()
    {
        if (!mPerf)
            return;

        String filename = "perf_" + System.currentTimeMillis() + ".txt";
        try {
            con = (FileConnection) Connector.open(System
                    .getProperty("fileconn.dir.photos") + filename);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (con != null) {
            if (con.exists()) {
                try {
                    con.delete();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                con.create();
                os = con.openOutputStream();
                log = new OutputStreamWriter(os);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void Log_current_time_s(String str)
    {
        if (!mPerf)
            return;

        String perf = str + ":" + System.currentTimeMillis() + "\n";
        try {
            log.write(perf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void Log_current_time_e(String str)
    {
        if (!mPerf)
            return;
        Log_current_time_s(str);
        try {
            if (log != null) {
                log.close();
            }
            if (os != null) {
                os.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (IOException io) {
        }
    }
    
	public static void save_file(final String filename,final byte[] raw)
	{
		new Thread(new Runnable() {
	        public void run() {
	        	FileConnection con = null;
	            OutputStream out = null;
	            try {
	                con = (FileConnection) Connector.open(System
	                        .getProperty("fileconn.dir.photos") + filename);
	                if (con != null) {
	                    if (con.exists()) {
	                        con.delete();
	                    }
	                    con.create();
	                    out = con.openOutputStream();
	                    out.write(raw);
	                    out.flush();
	                    }
	            } catch (Exception e) {
	                System.out.println(e.getMessage());
	            } finally {
	                try {
	                    if (out != null) {
	                        out.close();
	                    }
	                    if (con != null) {
	                        con.close();
	                    }
	                } catch (IOException io) {
	                }
	                System.gc();
	            }	
	        }
	    }).start();
		
	}
	
	public static byte[] read_file(String filename)
	{
		FileConnection con = null;
	    DataInputStream out = null;
	    byte[] raw=null;
	    try {
	        con = (FileConnection) Connector.open(System
	                .getProperty("fileconn.dir.photos") + filename);
	        if (con != null) {
	            if (con.exists()) {
	                con.delete();
	            }
	            con.create();
	            out = con.openDataInputStream();
	            raw=new byte[out.available()];
	            out.read(raw);
	            }
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    } finally {
	        try {
	            if (out != null) {
	                out.close();
	            }
	            if (con != null) {
	                con.close();
	            }
	        } catch (IOException io) {
	        }
	        
	    }	
	    return raw;
	}
	
	private static final Vector prefetchedPlayers = new Vector();
	private static int maxPrefetchedPlayers = 8;
	private static final Vector startedPlayers = new Vector();
	private static int maxStartedPlayers = 1;
	
	private static class SoundPlayer {
	
	    public final int sound;
	    public final Player player;
	
	    public SoundPlayer(int sound, Player player) {
	        this.sound = sound;
	        this.player = player;
	    }
	}
	
	private static void stop(Player player) {
	    synchronized (startedPlayers) {
	        startedPlayers.removeElement(player);
	        try {
	            if (player.getState() == Player.STARTED) {
	                try {
	                    player.stop();
	                }
	                catch (Exception e) {
	                }
	            }
	        }
	        catch (Exception e) {
	        }
	    }
	}
	
	private static boolean restart(int sound) {
	    synchronized (prefetchedPlayers) {
	        for (int i = 0; i < prefetchedPlayers.size(); i++) {
	            SoundPlayer sp = (SoundPlayer) prefetchedPlayers.elementAt(i);
	            if (sp.sound == sound) {
	                prefetchedPlayers.removeElement(sp);
	                prefetchedPlayers.addElement(sp);
	                stop(sp.player);
	                try {
	                    sp.player.setMediaTime(0);
	                }
	                catch (Exception e) {
	                }
	                start(sp.player);
	                return true;
	            }
	        }
	        return false;
	    }
	}
	
	
	private static void clean(SoundPlayer sp) {
	    synchronized (prefetchedPlayers) {
	        prefetchedPlayers.removeElement(sp);
	        stop(sp.player);
	        try {
	            sp.player.deallocate();
	            sp.player.close();
	        }
	        catch (Exception e) {
	        }
	    }
	}
	
	
	private static void limitPrefetchedPlayers() {
	    synchronized (prefetchedPlayers) {
	        try {
	            while (prefetchedPlayers.size() >= maxPrefetchedPlayers) {
	                SoundPlayer sp = (SoundPlayer) prefetchedPlayers.firstElement();
	                clean(sp);
	            }
	        }
	        catch (Exception e) {
	        }
	    }
	}
	
	
	private static void start(Player player) {
	    synchronized (startedPlayers) {
	        try {
	            while (startedPlayers.size() >= maxStartedPlayers) {
	                Player p = (Player) startedPlayers.firstElement();
	                startedPlayers.removeElementAt(0);
	                stop(p);
	            }
	        }
	        catch (Exception e) {
	        }
	        startedPlayers.addElement(player);
	        try {
	            player.start();
	        }
	        catch (Exception e) {
	        }
	    }
	}
	
	
	
	private static void start(int sound, Player player) {
	    synchronized (prefetchedPlayers) {
	        prefetchedPlayers.addElement(new SoundPlayer(sound, player));
	        start(player);
	    }
	}
	
	public static void playSound(int sound,Class c) {
		String res="/camerashutter.wav";
			
		if (restart(sound)) {
			return;
		}
		
		Player player = null;
		InputStream stream = c.getResourceAsStream(res);
		try {
			limitPrefetchedPlayers();
		    player = Manager.createPlayer(stream, "audio/wav");
		    player.realize();
		    player.prefetch();
		    start(sound, player);
		}
		catch (Exception e) {
		    // Either the device is in silent mode...
		    if (prefetchedPlayers.isEmpty()) {
		    }
		    // ...or does not support having all 8 players prefetched
		    else if (maxPrefetchedPlayers > 1) {
		        // Reduce amount of players and try again
		        maxPrefetchedPlayers--;
		        playSound(sound,c);
		    }
		}
	 
	}
}
