
package com.pictelligent.s40.camera5in1;

/**
 * Listener that when we return from the certain mode.
 */
public interface ModeExitListener {
    void onModeExit();

    boolean onStorageCheck();
}
