/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Erik
 */
public class clsAutoSaver implements Runnable {

    /*Settings*/
    private int intSaveInterval = 600;  //Seconds - 300 = 5 min, 600 = 10 min
    private Thread WorkerThread = null;
    private boolean Working = false;
    private String szPath = "";         //Were the last save was stored
    private boolean boolWork = true;
    private clsFileLocker hwndFileLocker = null;

    public clsAutoSaver(clsFileLocker fl) {
        hwndFileLocker = fl;            //Use local handle just incase
        WorkerThread = new Thread(this);
        WorkerThread.start();
    }

    @Override
    public void run() {

        while (boolWork) {

            try {
                Thread.sleep(1000 * intSaveInterval);
            } catch (Exception e) {
                if (clsEngine.DeveloperMode) {
                    System.out.println("Sleep interrupted in save thread.");
                }
            }

            if (!Working) {

                Working = true;
                DoSave();
                Working = false;

            }

        }

    }

    private void DoSave() {

        try {
            
            File f = File.createTempFile("xeed_autosaves", ".xdf");
            szPath = f.getAbsolutePath();
            
            Thread.sleep(250);      //Gives the os time to create the file.
            
            clsXDF XDF = new clsXDF(szPath);
            XDF.UpdateCurrentDirectory = false;
            XDF.AutoSaves = true;

            if (!XDF.WriteSetting()) {
                clsEngine.DisplayAsyncMessageBox("Error while performing autosave.", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (hwndFileLocker.IsLocked()) {
                if (!hwndFileLocker.AddSavePath(szPath)) {
                    clsEngine.DisplayAsyncMessageBox("Error while indexing autosave.", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            XDF = null;

        } catch (Exception e) {
            clsEngine.DisplayAsyncMessageBox("Error while creating autosave\n" + e, "Error!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void ManulSave() {

        if (Working) {
            return;
        }

        Working = true;
        DoSave();
        Working = false;

    }

    public void stop() {

        boolWork = false;
        Working = true; //prevents it from saving one last time.
        if (Thread.currentThread() != WorkerThread) {
            WorkerThread.interrupt();
        }

        new File(szPath).delete();
        hwndFileLocker = null;
        WorkerThread = null;

    }

    public String GetPath() {
        return szPath;
    }
}
