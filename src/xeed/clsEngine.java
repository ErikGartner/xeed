package xeed;

import java.awt.Desktop;
import java.awt.Image;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.zip.CRC32;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author SmoiZ
 */

/*
 *    
 *
 * Todo: 
 * Drag'n'droping 
 * Bug reportning 
 * Förbättra graphs 
 * Error loggin to HDD.
 * Clean-up/refine XDF-class
 * Clean-up/refine template related code. Less duplicate code etc...
 *
 */
public class clsEngine {

    /*
     * Constants
     */
    public static final boolean DeveloperMode = true;
    public static final String szUpdateURL = "http://xeed.smoiz.com/static/xeed/updates/info";
    public static final String szDevUpdateURL = "http://xeed.smoiz.com/static/xeed/updates/beta/info";
    public static final String szTemplateListURL = "http://xeed.smoiz.com/static/xeed/templates/info";
    public static final String szTemplateUploadURL = "http://xeed.smoiz.com/upload/templates/uploader.php";
    public static final long lngBuild = 46;
    public static final String szVersion = "2.0 Beta 8";
    public static final String szCompiledOn = "2015-01-10";
    public static final String szHomePage = "http://smoiz.com";
    public static final String[] szCredits = {"All registered trademarks belong to their respective owners.", "Most icons come from famfamfam.com.", "Uses the JUNG library.", "Uses several libraries from Apache Commons.", "Uses the iText library.", "Beta testers:", "Sagemaster", "Kuslix"};
    public static String szArguments[] = null;      //Inte en konstant per se, men den ska inte ändras efter den har deklarerats
    /*
     * Databaser
     */
    public static ArrayList<clsGroup> groupDB = new ArrayList(0);                      // Lista med grupper
    public static ArrayList<clsTemplate> templateDB = new ArrayList(0);                //Alla template som är laddade in i settingen.
    public static ArrayList<clsCharacter> charDB = new ArrayList(0);
    /*
     * Options
     */
    public static boolean boolUpdate = false;
    public static boolean boolBackup = true;
    public static String szDefaultDirectory = GetCurrentDirectory();                    //The default directory for opening settings
    public static boolean Associated = false;
    public static boolean ReAssociate = false;
    public static boolean boolCustomRepository = false;
    public static boolean boolUpdateToBeta = DeveloperMode;
    public static boolean boolResize = true;
    public static boolean boolCustomTemplateFolder = false;
    public static String szCustomReposityURL = "";
    public static String szTemplateFolder = GetCurrentDirectory() + File.separator + "Templates";
    /*
     * Setting Data
     */
    public static String szPath = "";                                                   // Den öppna settingfilen.
    public static clsNote rootNode = null;                                              //Root noden till setting filerna
    public static String szSettingName = "";                                            //Namnet på settingen
    public static String szThemes = "";
    public static String szOutline = "";
    public static String szLastSaved = "";
    public static long lngNoteID = 0;                                                   //Används vi export av nodes för att skapa unika idn
    public static long lngSettingRevision = 0;                                          //Setting Revision

    /*
     * Handles till objekt
     */
    public static frmAbout hwndAbout = null;                                            //About Handle
    public static frmNotes hwndNotes = null;                                        //Setting Handle
    public static frmOptions hwndOption = null;                                         //Options
    public static frmColumn hwndColumns = null;
    public static frmMain hwndMain = null;
    public static frmGroup hwndGroup = null;
    public static frmRelations hwndRelations = null;
    public static frmTemplateCreator hwndTemplateCreator = null;
    public static frmTemplateManager hwndTemplateManager = null;
    public static clsNotifier hwndNotifier = null;
    public static clsFileLocker hwndFileLock = null;
    public static frmCharacterExporter hwndCharacterExporter = null;
    public static frmGraph hwndGraph = null;
    public static frmCharacterConverter hwndCharacterConverter = null;
    public static frmSettingInfo hwndSettingInfo = null;

    public static void main(final String args[]) {

        szArguments = args;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                InitXEED();
                hwndNotifier = new clsNotifier();
            }
        });

    }

    /*
     * Global inits, not class related
     */
    private static void InitXEED() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        clsEngine.ReadWriteOptions(false);

        if (clsEngine.ReAssociate) {
            clsEngine.UpdateAssociation(true);
        }

        if (clsEngine.boolUpdate) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    frmUpdate u = new frmUpdate();
                    u.CheckForUpdates(false);

                }
            }).start();

        }

        hwndMain = new frmMain();       //Måste få handle till frmMain INNAN man kallar initProgram.
        hwndMain.initMain();

    }

    public static String GetElement(String szData, String szElement, boolean isUnescaped) {

        String szRet[] = szData.split("<" + szElement + ">");
        if (szRet.length < 2) {
            return "";
        }
        int intEndPos = szRet[1].indexOf("</" + szElement + ">");
        if (intEndPos < 0) {
            return "";
        }

        if (isUnescaped) {
            return HandleUnsafeCharacters(szRet[1].substring(0, intEndPos), false);
        } else {
            return szRet[1].substring(0, intEndPos);
        }


    }

    public static String[] GetElements(String szData, String szElement, boolean isUnescaped) {

        int intEndPos;
        ArrayList<String> szElements = new ArrayList(0);
        String szRet[] = szData.split("<" + szElement + ">");
        for (int x = 1; x < szRet.length; x++) {
            intEndPos = szRet[x].indexOf("</" + szElement + ">");
            if (intEndPos != -1) {
                if (isUnescaped) {
                    szElements.add(HandleUnsafeCharacters(szRet[x].substring(0, intEndPos), false));
                } else {
                    szElements.add(szRet[x].substring(0, intEndPos));
                }
            }
        }
        String str[] = new String[szElements.size()];
        szElements.toArray(str);
        return str;
    }

    public static String CreateElement(String szData, String szElement, boolean unescape) {

        if (unescape) {
            szData = HandleUnsafeCharacters(szData, true);
        }
        return "<" + szElement + ">" + szData + "</" + szElement + ">";
    }

    public static String DownloadURLToString(String szURL) {

        try {

            InputStream in = new java.net.URL(szURL).openStream();
            BufferedInputStream bin = new java.io.BufferedInputStream(in);

            byte data[] = new byte[4096];
            String szData = "";

            while ((bin.read(data)) != -1) {
                szData += new String(data, "UTF-8");
            }

            bin.close();
            in.close();
            return szData;

        } catch (Exception e) {
            return "";
        }

    }

    /**
     * A highly optimised download method.
     */
    public static boolean DownloadURLToFile(String szURL, String FilePath) {

        try {

            new File(FilePath).delete();
            URL download = new URL(szURL);
            ReadableByteChannel rbc = Channels.newChannel(download.openStream());
            FileOutputStream fos = new FileOutputStream(FilePath);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            fos.close();

        } catch (Exception e) {
            new File(FilePath).delete();
            return false;
        }

        return true;

    }

    public static String GetCurrentDirectory() {
        try {
            File f = new File(frmMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return f.getParent();
        } catch (Exception e) {
        }
        return "";
    }

    public static String HandleUnsafeCharacters(String szData, boolean unescape) {

        if (szData == null) {
            return szData;
        }

        if (szData.isEmpty()) {
            return szData;
        }

        if (unescape) {
            szData = szData.replaceAll("<", "\'<\'");
            szData = szData.replaceAll("</", "\'</\'");
            szData = szData.replaceAll(">", "\'>\'");
        } else {
            szData = szData.replaceAll("\'<\'", "<");
            szData = szData.replaceAll("\'</\'", "</");
            szData = szData.replaceAll("\'>\'", ">");
        }
        return szData;
    }

    public static boolean ReadWriteOptions(boolean write) {

        if (write) {

            try {

                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(GetCurrentDirectory() + File.separator + "options.ini"), "UTF-8");

                out.write(clsEngine.CreateElement(Long.toString(clsEngine.lngBuild), clsConstants.OPTIONS_BUILD, false));
                out.write(clsEngine.CreateElement(Boolean.toString(clsEngine.boolUpdate), clsConstants.OPTIONS_UPDATEONSTART, false));
                out.write(clsEngine.CreateElement(Boolean.toString(clsEngine.boolBackup), clsConstants.OPTIONS_BACKUP, false));
                out.write(clsEngine.CreateElement(Boolean.toString(clsEngine.Associated), clsConstants.OPTIONS_ASSOCIATED, false));
                out.write(clsEngine.CreateElement(Boolean.toString(clsEngine.ReAssociate), clsConstants.OPTIONS_REASSOCIATE, false));
                out.write(clsEngine.CreateElement(szDefaultDirectory, clsConstants.OPTIONS_DEFAULTDIRECTORY, false));
                out.write(clsEngine.CreateElement(Boolean.toString(clsEngine.boolCustomRepository), clsConstants.OPTIONS_CUSTOMREPOSITORY, false));
                out.write(clsEngine.CreateElement(Boolean.toString(clsEngine.boolUpdateToBeta), clsConstants.OPTIONS_UPDATETOBETA, false));
                out.write(clsEngine.CreateElement(Boolean.toString(clsEngine.boolResize), clsConstants.OPTIONS_RESIZEIMAGES, false));
                out.write(clsEngine.CreateElement(clsEngine.szCustomReposityURL, clsConstants.OPTIONS_CUSTOMREPOSITORY_URL, false));
                out.write(clsEngine.CreateElement(clsEngine.szTemplateFolder, clsConstants.OPTIONS_CUSTOMTEMPLATEFOLDER, false));

                out.close();

            } catch (Exception e) {
                return false;
            }

        } else {

            try {

                String szDatabase = "";

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(GetCurrentDirectory() + File.separator + "options.ini"), "UTF-8"));

                while (br.ready()) {
                    char cbuf[] = new char[1024];
                    br.read(cbuf);
                    szDatabase += String.copyValueOf(cbuf);
                }

                br.close();

                clsEngine.boolUpdate = Boolean.parseBoolean(clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_UPDATEONSTART, false));
                clsEngine.boolBackup = Boolean.parseBoolean(clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_BACKUP, false));
                clsEngine.Associated = Boolean.parseBoolean(clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_ASSOCIATED, false));
                clsEngine.ReAssociate = Boolean.parseBoolean(clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_REASSOCIATE, false));
                clsEngine.szDefaultDirectory = clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_DEFAULTDIRECTORY, false);
                clsEngine.boolCustomRepository = Boolean.parseBoolean(clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_CUSTOMREPOSITORY, false));
                clsEngine.boolUpdateToBeta = Boolean.parseBoolean(clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_UPDATETOBETA, false));
                clsEngine.boolResize = Boolean.parseBoolean(clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_RESIZEIMAGES, false));
                clsEngine.szCustomReposityURL = clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_CUSTOMREPOSITORY_URL, false);
                clsEngine.szTemplateFolder = clsEngine.GetElement(szDatabase, clsConstants.OPTIONS_CUSTOMTEMPLATEFOLDER, false);

            } catch (Exception e) {

                if (!clsEngine.boolUpdate) {
                    int intRet = JOptionPane.showOptionDialog(null, "Do you want xeed to check for updates everytime xeed starts up?\nThis option can be changed from File>Options.", "Check for updates?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (intRet == 0) {
                        clsEngine.boolUpdate = true;
                    } else {
                        clsEngine.boolUpdate = false;
                    }
                }
                return false;
            }

        }
        return true;
    }

    public static void CloseWindows() {

        for (int x = 0; x < charDB.size(); x++) {
            charDB.get(x).CloseForm();
        }

        if (clsEngine.hwndNotes != null) {
            clsEngine.hwndNotes.dispose();
            clsEngine.hwndNotes = null;
        }
        if (clsEngine.hwndGroup != null) {
            clsEngine.hwndGroup.dispose();
            clsEngine.hwndGroup = null;
        }

        if (clsEngine.hwndRelations != null) {
            clsEngine.hwndRelations.dispose();
            clsEngine.hwndRelations = null;
        }


        if (clsEngine.hwndGraph != null) {
            clsEngine.hwndGraph.dispose();
            clsEngine.hwndGraph = null;
        }

        if (clsEngine.hwndCharacterConverter != null) {
            clsEngine.hwndCharacterConverter.dispose();
            clsEngine.hwndCharacterConverter = null;
        }

        if (clsEngine.hwndSettingInfo != null) {
            clsEngine.hwndSettingInfo.dispose();
            clsEngine.hwndSettingInfo = null;
        }

        if (hwndColumns != null) {
            hwndColumns.dispose();
            hwndColumns = null;
        }

        if (hwndTemplateManager != null) {
            hwndTemplateManager.dispose();
            hwndTemplateManager = null;
        }

    }

    public static long CreateUniqueCharacterID() {

        //Ej multitråds säker, så precis innan den läggs till!
        long lngNewID = 0;
        for (int x = 0; x < clsEngine.charDB.size(); x++) {

            if (clsEngine.charDB.get(x).characterID >= lngNewID) {
                lngNewID = clsEngine.charDB.get(x).characterID + 1;
            }
        }
        return lngNewID;
    }

    public static long CreateUniqueGroupID() {


        //Ej multitråds säker, så precis innan den läggs till!
        long lngNewID = 0;
        for (int x = 0; x < clsEngine.groupDB.size(); x++) {
            if (clsEngine.groupDB.get(x).lngID >= lngNewID) {
                lngNewID = clsEngine.groupDB.get(x).lngID + 1;
            }
        }
        return lngNewID;
    }

    public static void ExportNodesToFiles(clsNote[] s, long Parent, String szDir) throws Exception {

        if (s == null) {
            return;
        }
        for (int x = 0; x < s.length; x++) {

            if (s[x].boolFolder) {
                File d = new File(szDir + File.separator + s[x].szTitle);
                d.mkdirs();
                clsEngine.lngNoteID++;
                ExportNodesToFiles(s[x].GetChildren(), clsEngine.lngNoteID, d.getAbsolutePath());
            } else {
                File f = new File(szDir + File.separator + s[x].szTitle + ".txt");
                try {
                    FileWriter fw = new FileWriter(f, false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.print(s[x].szData.replace("\n", System.getProperty("line.separator")));
                    pw.close();
                    bw.close();
                    fw.close();
                    ExportNodesToFiles(s[x].GetChildren(), clsEngine.lngNoteID, szDir);

                } catch (Exception e) {
                    throw new Exception("Error occured when exporting: " + s[x].szTitle);
                }

            }
        }
        return;

    }

    public static void LoadSetting(boolean ask_save, boolean ask_location) {

        CloseWindows();
        if (ask_save && !IsSettingEmpty()) {
            if (JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == 0) {
                SaveSetting(false, true);
            }
        }

        if (ask_location) {
            if (!SetActiveSetting(true)) {
                return;
            }
        }

        if (hwndFileLock != null) {
            hwndFileLock.ReleaseLock();
        }

        hwndFileLock = new clsFileLocker(szPath);
        if (!hwndFileLock.IsLocked()) {
            hwndFileLock.ReleaseLock();
            NewSetting(false);
            JOptionPane.showMessageDialog(null, "This setting is already being used by another instance of XEED.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String szAutoSaves[] = hwndFileLock.GetOldBackupPaths();
        if (szAutoSaves.length > 0) {

            hwndFileLock.ReleaseLock();                //Close down file properly.
            hwndFileLock = null;

            hwndMain.setEnabled(false);
            JOptionPane.showMessageDialog(null, "It appears as though a previous instance of XEED wasn't shutdown properly while handling the selected setting.\nYou may now restore autosaves from that setting in order to prevent data loss.", "Autosaves found!", JOptionPane.INFORMATION_MESSAGE);
            new frmRestorer(szAutoSaves, szPath);    //Handle list of backups            
            return;
        }

        clsXDF x = new clsXDF(szPath);
        if (!x.ReadSetting()) {
            JOptionPane.showMessageDialog(null, "An error occured while opening the setting", "Error", JOptionPane.ERROR_MESSAGE);
            NewSetting(false);
        }
        x = null;

        hwndFileLock.GetAutoSaverInstance().ManulSave();

    }

    public static boolean SaveSetting(boolean ask, boolean SetNewLocation) {

        if (ask) {
            int intRet = JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (intRet != 0) {
                return false;
            }
        }

        if (SetNewLocation || szPath.isEmpty()) {
            if (!SetActiveSetting(false)) {
                return false;
            }
        }

        clsXDF x = new clsXDF(szPath);
        if (!x.WriteSetting()) {

            JOptionPane.showMessageDialog(null, "An error occured while saving the setting", "Error", JOptionPane.ERROR_MESSAGE);
            szPath = "";
            return false;

        } else {

            if (hwndFileLock != null) {
                if (!hwndFileLock.GetActiveSave().equalsIgnoreCase(szPath)) {
                    hwndFileLock.ReleaseLock();
                    hwndFileLock = new clsFileLocker(szPath);
                }
            } else {
                hwndFileLock = new clsFileLocker(szPath);
            }
            JOptionPane.showMessageDialog(null, "Save complete!", "Done", JOptionPane.INFORMATION_MESSAGE);
        }

        return true;
    }

    /**
     * Makes XEED open a new save. Possibly unload all templates and ask user if
     * they want to name their setting and load templates.
     *
     * @param ask If true, the user is asked to save the current setting and
     * asked if he he wishes to name the new setting and load templates. If
     * false, all these question defaults to no.
     */
    public static void NewSetting(boolean ask) {

        CloseWindows();
        if (ask && !IsSettingEmpty()) {
            if (JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == 0) {
                if (!SaveSetting(false, true)) {
                    return;
                }
            }
        }

        ClearSetting();

        if (ask) {
            clsEngine.hwndTemplateManager = new frmTemplateManager();
            clsEngine.hwndTemplateManager.setVisible(true);

            clsEngine.hwndSettingInfo = new frmSettingInfo();
            clsEngine.hwndSettingInfo.setVisible(true);
        }

    }

    public static boolean SetActiveSetting(boolean Open) {

        JFileChooser fc = new JFileChooser();
        fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
        fc.addChoosableFileFilter(new XDFFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        if (!clsEngine.szDefaultDirectory.isEmpty()) {
            fc.setCurrentDirectory(new File(clsEngine.szDefaultDirectory));
        }

        if (Open) {

            fc.setDialogTitle("Select the file containing your setting.");
            int intRet = fc.showOpenDialog(null);

            if (intRet != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            clsEngine.szPath = fc.getSelectedFile().getAbsolutePath();
            clsEngine.szDefaultDirectory = fc.getSelectedFile().getParent();

        } else {

            fc.setDialogTitle("Select were to save your setting.");
            int intRet = fc.showSaveDialog(null);

            if (intRet != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            clsEngine.szPath = fc.getSelectedFile().getAbsolutePath();
            if (!clsEngine.szPath.contains(".")) {
                clsEngine.szPath += ".xdf";
            }

            clsEngine.szDefaultDirectory = fc.getSelectedFile().getParent();
        }

        return true;
    }

    public static boolean IsSettingEmpty() {

        if (!clsEngine.charDB.isEmpty()) {
            return false;
        }

        if (!clsEngine.groupDB.isEmpty()) {
            return false;
        }

        if (rootNode != null) {
            clsNote[] s = clsEngine.rootNode.GetChildren();
            if (s != null) {
                return false;
            }
        }

        if (!szSettingName.isEmpty()) {
            return false;
        }

        if (!szThemes.isEmpty()) {
            return false;
        }

        if (!szOutline.isEmpty()) {
            return false;
        }

        return true;
    }

    public static clsCharacter GetCharacterByID(long id) {
        for (int x = 0; x < charDB.size(); x++) {
            if (charDB.get(x).characterID == id) {
                return charDB.get(x);
            }
        }
        return null;
    }

    public static String GetXEEDCRC32() {
        CRC32 ce = new CRC32();
        try {
            File f = new File(frmMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            InputStream is = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(is);

            byte[] b = new byte[4096];

            int intRead;
            while ((intRead = bis.read(b)) > 0) {
                ce.update(b, 0, intRead);
            }

            bis.close();
            is.close();
        } catch (Exception e) {
        }
        return (Long.toHexString(ce.getValue()).toUpperCase());
    }

    public static boolean UpdateAssociation(boolean add) {

        Runtime RT = Runtime.getRuntime();

        if (add) {
            try {
                RT.exec("cmd /c assoc .xdf=xeed.Data.File");
                RT.exec("cmd /c ftype  XEED.Data.File=" + System.getProperties().getProperty("java.home") + "\\bin\\javaw.exe" + " -jar \"" + new File(frmMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + "\" /l:%1 %*");
            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        } else {
            try {
                RT.exec("cmd /c assoc .xdf=");
                RT.exec("cmd /c ftype  XEED.Data.File=");
            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        }

        return true;
    }

    public static ImageIcon RescaleImageIcon(ImageIcon i, int ImageMaxWidth, int ImageMaxHeight) {

        if (i == null) {
            return null;
        }

        if (i.getIconWidth() > ImageMaxWidth || i.getIconHeight() > ImageMaxHeight) {
            if (i.getIconWidth() > i.getIconHeight()) {
                return new ImageIcon(i.getImage().getScaledInstance(ImageMaxWidth, -1, Image.SCALE_SMOOTH));
            } else {
                return new ImageIcon(i.getImage().getScaledInstance(-1, ImageMaxHeight, Image.SCALE_SMOOTH));
            }
        }

        return i;
    }

    public static clsCharacter AddCharacter(clsTemplate t) {

        if (t == null) {
            return null;
        }

        clsCharacter c = new clsCharacter(t);
        charDB.add(c);
        hwndMain.AddListItem(c);

        clsCharacter[] affectedcharacters = new clsCharacter[charDB.size()];
        charDB.toArray(affectedcharacters);
        hwndNotifier.FireUpdate(affectedcharacters, true, true, true, true, false, false, true, false, false);
        return c;

    }

    public static void DeleteCharacter(clsCharacter c) {

        if (c == null) {
            return;
        }

        c.CloseForm();
        if (!charDB.contains(c)) {
            return;
        }


        for (int x = 0; x < charDB.size(); x++) {

            //Removes the characters relations to others.
            charDB.get(x).DeleteRelation(c.characterID, 0);

            //Removes all refs in other characters to the character
            if (charDB.get(x).chrData.containsValue(c)) {
                String keys[] = charDB.get(x).GetChrKeys();
                for (int y = 0; y < keys.length; y++) {
                    if (charDB.get(x).chrData.get(keys[y]) == c) {
                        charDB.get(x).chrData.put(keys[y], null);
                    }
                }
            }

        }

        for (int x = 0; x < groupDB.size(); x++) {
            groupDB.get(x).DeleteMember(c.characterID);
            groupDB.get(x).DeleteRelation(c.characterID, 0);
        }

        if (hwndCharacterConverter != null) {
            hwndCharacterConverter.CharacterRemoved(c);
        }

        charDB.remove(c);
        hwndMain.UpdateListItem(c);

        clsCharacter[] affectedcharacters = new clsCharacter[charDB.size()];
        charDB.toArray(affectedcharacters);
        hwndNotifier.FireUpdate(affectedcharacters, true, true, true, true, false, false, true, false, false);

    }

    public static boolean IsTemplateLoaded(clsTemplate t) {

        if (t == null) {
            return false;
        }

        for (int x = 0; x < templateDB.size(); x++) {
            if (templateDB.get(x).GetTemplateID().equalsIgnoreCase(t.GetTemplateID())) {
                return true;
            }
        }

        return false;

    }

    public static boolean LoadTemplate(clsTemplate t) {

        if (IsTemplateLoaded(t)) {
            JOptionPane.showMessageDialog(null, "A template (" + t.GetName() + ") with the same ID is already loaded.\nTherefore the new template wasn't loaded.", "Error while loading: " + t.GetName(), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        templateDB.add(t);
        if (hwndMain.currentTemplate == null) {
            hwndMain.currentTemplate = t;
            hwndMain.PurgeThenPrintCharacters();
            hwndMain.AddCharacterMenuItem(t, true);
        } else {
            hwndMain.AddCharacterMenuItem(t, false);
        }

        if (clsEngine.hwndCharacterConverter != null) {
            clsEngine.hwndCharacterConverter.LoadTemplateList();
        }

        if (hwndColumns != null) {
            hwndColumns.LoadTemplates();
        }

        return true;

    }

    public static void UnloadTemplate(clsTemplate t) {

        if (!IsTemplateLoaded(t)) {
            return;
        }

        for (int x = clsEngine.charDB.size() - 1; x >= 0; x--) {
            if (charDB.get(x).templateIdentifier.equalsIgnoreCase(t.GetTemplateID())) {
                DeleteCharacter(charDB.get(x));
            }
        }

        templateDB.remove(t);
        for (int x = 0; x < templateDB.size(); x++) {
            if (templateDB.get(x).GetTemplateID().equalsIgnoreCase(t.GetTemplateID())) {
                templateDB.remove(x);
            }
        }

        if (hwndMain.currentTemplate.GetTemplateID().equalsIgnoreCase(t.GetTemplateID())) {
            if (templateDB.isEmpty()) {
                hwndMain.currentTemplate = null;
            } else {
                hwndMain.currentTemplate = templateDB.get(0);
            }
            hwndMain.PurgeThenPrintCharacters();
        }

        hwndMain.RemoveCharacterMenuItem(t);

        if (clsEngine.hwndCharacterConverter != null) {
            clsEngine.hwndCharacterConverter.LoadTemplateList();
        }

        if (hwndColumns != null) {
            hwndColumns.LoadTemplates();
        }

    }

    public static void UnloadAllTemplates() {

        for (int x = templateDB.size() - 1; x >= 0; x--) {
            UnloadTemplate(templateDB.get(x));
        }
    }

    public static clsTemplate GetTemplateByID(String ID) {
        for (int x = 0; x < templateDB.size(); x++) {
            if (templateDB.get(x).GetTemplateID().equalsIgnoreCase(ID)) {
                return templateDB.get(x);
            }
        }
        return null;
    }

    public static void SetSettingTitle(String s) {

        szSettingName = s;
        if (DeveloperMode) {
            hwndMain.setTitle("[Beta]" + s);
        } else {
            hwndMain.setTitle(s);
        }

    }

    public static void ClearSetting() {

        clsEngine.lngSettingRevision = 0;
        clsEngine.szLastSaved = "";
        clsEngine.szThemes = "";
        clsEngine.szOutline = "";
        clsEngine.szPath = "";

        clsEngine.rootNode = new clsNote();
        clsEngine.rootNode.szTitle = "Notes";
        clsEngine.rootNode.boolFolder = true;
        clsEngine.rootNode.lngID = 0;

        clsEngine.charDB.clear();
        clsEngine.groupDB.clear();
        hwndMain.PurgeThenPrintCharacters();
        SetSettingTitle("");

        UnloadAllTemplates();

        if (hwndFileLock != null) {
            hwndFileLock.ReleaseLock();
        }

        hwndFileLock = new clsFileLocker();
        hwndFileLock.GetAutoSaverInstance().ManulSave();

    }

    public static void DisplayAsyncMessageBox(final Object msg, final String title, final int Type) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, msg, title, Type);
            }
        }).start();
    }

    public static boolean TestRepository(String szURL) {

        try {

            String szData = clsEngine.DownloadURLToString(szURL);
            String szRepositoryHeader = clsEngine.GetElement(szData, clsConstants.TEMPALTE_REPOSITORY, false);
            if (szRepositoryHeader.isEmpty()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    public static boolean ExecuteLink(String s) {

        try {
            Desktop.getDesktop().browse(URI.create(s));
        } catch (Exception e) {
            return false;
        }
        return true;

    }
}

class XDFFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".xdf");
    }

    @Override
    public String getDescription() {
        return ".xdf (xeed Data File)";
    }
}
