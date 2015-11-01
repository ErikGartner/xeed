/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 *
 * @author Erik
 */
public class clsFileLocker {

    private clsAutoSaver hwndAutoSaver = null;
    private String szPath = "";
    private String szOrgSavePath = "";
    private FileLock lock = null;
    private FileChannel channel = null;
    private String[] PastSavePaths;
    private boolean LockEnabled = false;

    /*
     * Locks a setting file and starts autosaver
     */
    public clsFileLocker(String szP) {
        szPath = szP+ ".lock";
        szOrgSavePath = szP;

        LockEnabled = CreateLock();
        if (LockEnabled) {
            PastSavePaths = ReadSavePaths();
            ClearFile();
            hwndAutoSaver = new clsAutoSaver(this);
        }

    }

    /*
     * Starts autosaver without file lock on a setting file
     */
    public clsFileLocker() {
        hwndAutoSaver = new clsAutoSaver(this);
    }

    private boolean CreateLock() {

        try {

            channel = new RandomAccessFile(new File(szPath), "rw").getChannel();
            lock = channel.tryLock();
            if (lock == null || !lock.isValid()) {
                channel.close();
                return false;
            }

            try {
                Runtime.getRuntime().exec("cmd.exe /c attrib +h \"" + szPath + "\"");    // Bör fixas mot mer plattforms oberoende kod, kommer i jdk7
            } catch (Exception e2) {
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean IsLocked() {
        if (lock != null && LockEnabled) {
            return true;
        } else {
            return false;
        }
    }

    public clsAutoSaver GetAutoSaverInstance() {
        return hwndAutoSaver;
    }

    public boolean Exists() {
        return new File(szPath).exists();
    }

    public boolean AddSavePath(String szPath) {

        try {
            channel.position(channel.size());
            byte[] b = clsEngine.CreateElement(szPath, "save_path",false).getBytes("UTF-8");
            ByteBuffer buf = ByteBuffer.wrap(b);
            channel.write(buf);
            channel.force(false);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    private void ClearFile() {

        try {
            channel.truncate(0);
        } catch (Exception e) {
        }
    }

    private String[] ReadSavePaths() {

        String szBuffer = "";

        try {
            channel.position(0);

            ByteBuffer buf = ByteBuffer.allocate(4096);

            while (channel.read(buf) > 0) {
                byte b[] = buf.array();
                if (buf.hasArray()) {
                    szBuffer += new String(b, 0, b.length, "UTF-8");
                }
            }

            return clsEngine.GetElements(szBuffer, "save_path",false);

        } catch (Exception e) {
            String[] sz = {};
            return sz;
        }

    }

    public String[] GetOldBackupPaths() {
        return PastSavePaths;
    }

    public String GetActiveSave() {
        return szOrgSavePath;
    }

    public void ReleaseLock() {

        try {

            if (hwndAutoSaver != null) {
                hwndAutoSaver.stop();
            }

            String szSavePaths[] = ReadSavePaths();

            if (lock != null) {
                lock.release();
            }

            if (channel != null) {
                channel.close();
            }

            new File(szPath).delete();
            for (int x = 0; x < szSavePaths.length; x++) {
                new File(szSavePaths[x]).delete();
            }

            hwndAutoSaver = null;

        } catch (Exception e) {
        }

    }
}
