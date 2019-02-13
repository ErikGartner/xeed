package xeed;

import forms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.zip.CRC32;

/**
 * @author SmoiZ
 */
public class XEED {

    /*
     * Constants
     */
    public static final boolean DeveloperMode = false;
    public static boolean boolUpdateToBeta = DeveloperMode;
    public static final String szUpdateURL = "https://raw.githubusercontent.com/xeed-org/update-database/master/xeed2/info";
    public static final String szDevUpdateURL = "https://raw.githubusercontent.com/xeed-org/update-database/master/xeed2/beta/info";
    public static final String szTemplateListURL = "https://raw.githubusercontent.com/xeed-org/template-database/master/xeed2/info";
    public static final String szTemplateUploadURL = ""; //http://xeed.smoiz.com/upload/templates/uploader.php";
    public static final long lngBuild = 53;
    public static final String szVersion = XEED.class.getPackage().getImplementationVersion();
    public static final String szCompiledOn = "2019-02-10";
    public static final String szHomePage = "https://gartner.io";
    public static final String[] szCredits = {
        "All registered trademarks belong to their respective owners.",
        "Most icons come from famfamfam.com.", "Uses the JUNG library.",
        "Uses several libraries from Apache Commons.", "Uses the iText library.", "Beta testers:", "Sagemaster",
        "Kuslix"
    };
    public static String szArguments[] = null;
    /*
     * Databaser
     */
    public static ArrayList<Group> groupDB = new ArrayList(0); // Lista med grupper
    public static ArrayList<Template> templateDB = new ArrayList(0); //Alla template som är laddade in i settingen.
    public static ArrayList<Character> charDB = new ArrayList(0);
    /*
     * Options
     */
    public static boolean boolUpdate = false;
    public static boolean boolBackup = true;
    public static String szDefaultDirectory = GetCurrentDirectory();
    public static boolean Associated = false;
    public static boolean ReAssociate = false;
    public static boolean boolCustomRepository = false;
    public static boolean boolResize = true;
    public static boolean boolCustomTemplateFolder = false;
    public static String szCustomReposityURL = "";
    public static String szTemplateFolder = GetCurrentDirectory() + File.separator + "Templates";
    /*
     * Setting Data
     */
    public static String szPath = "";
    public static Note rootNode = null;
    public static String szSettingName = "";
    public static String szThemes = "";
    public static String szOutline = "";
    public static String szLastSaved = "";
    public static long lngNoteID = 0;
    public static long lngSettingRevision = 0;

    /*
     * Handles for windows
     */
    public static AboutForm hwndAbout = null; //About Handle
    public static NotesForm hwndNotes = null; //Setting Handle
    public static OptionsForm hwndOption = null; //Options
    public static ColumnForm hwndColumns = null;
    public static MainForm hwndMain = null;
    public static GroupForm hwndGroup = null;
    public static RelationsForm hwndRelations = null;
    public static TemplateCreatorForm hwndTemplateCreator = null;
    public static TemplateManagerForm hwndTemplateManager = null;
    public static Notifier hwndNotifier = null;
    public static FileLocker hwndFileLock = null;
    public static CharacterExporterForm hwndCharacterExporter = null;
    public static GraphForm hwndGraph = null;
    public static CharacterConverterForm hwndCharacterConverter = null;
    public static SettingInfoForm hwndSettingInfo = null;

    public static void main(final String args[]) {

        szArguments = args;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                InitXEED();
                hwndNotifier = new Notifier();
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

        XEED.ReadWriteOptions(false);

        if (XEED.ReAssociate) {
            XEED.UpdateAssociation(true);
        }

        if (XEED.boolUpdate) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    UpdateForm u = new UpdateForm();
                    u.CheckForUpdates(false);

                }
            }).start();

        }

        hwndMain = new MainForm();
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
            File f = new File(MainForm.class.getProtectionDomain().getCodeSource().getLocation().toURI());
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

                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(GetCurrentDirectory() + File.separator
                        + "options.ini"), "UTF-8");

                out.write(XEED.CreateElement(Long.toString(XEED.lngBuild), Constants.OPTIONS_BUILD, false));
                out.write(XEED.CreateElement(Boolean.toString(XEED.boolUpdate), Constants.OPTIONS_UPDATEONSTART, false));
                out.write(XEED.CreateElement(Boolean.toString(XEED.boolBackup), Constants.OPTIONS_BACKUP, false));
                out.write(XEED.CreateElement(Boolean.toString(XEED.Associated), Constants.OPTIONS_ASSOCIATED, false));
                out.write(XEED.CreateElement(Boolean.toString(XEED.ReAssociate), Constants.OPTIONS_REASSOCIATE, false));
                out.write(XEED.CreateElement(szDefaultDirectory, Constants.OPTIONS_DEFAULTDIRECTORY, false));
                out.write(XEED.CreateElement(Boolean.toString(XEED.boolCustomRepository),
                        Constants.OPTIONS_CUSTOMREPOSITORY, false));
                out.write(XEED.CreateElement(Boolean.toString(XEED.boolUpdateToBeta), Constants.OPTIONS_UPDATETOBETA, false));
                out.write(XEED.CreateElement(Boolean.toString(XEED.boolResize), Constants.OPTIONS_RESIZEIMAGES, false));
                out.write(XEED.CreateElement(XEED.szCustomReposityURL, Constants.OPTIONS_CUSTOMREPOSITORY_URL, false));
                out.write(XEED.CreateElement(XEED.szTemplateFolder, Constants.OPTIONS_CUSTOMTEMPLATEFOLDER, false));

                out.close();

            } catch (Exception e) {
                return false;
            }

        } else {

            try {

                String szDatabase = "";

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(GetCurrentDirectory()
                        + File.separator + "options.ini"), "UTF-8"));

                while (br.ready()) {
                    char cbuf[] = new char[1024];
                    br.read(cbuf);
                    szDatabase += String.copyValueOf(cbuf);
                }

                br.close();

                XEED.boolUpdate = Boolean.parseBoolean(XEED.GetElement(szDatabase, Constants.OPTIONS_UPDATEONSTART, false));
                XEED.boolBackup = Boolean.parseBoolean(XEED.GetElement(szDatabase, Constants.OPTIONS_BACKUP, false));
                XEED.Associated = Boolean.parseBoolean(XEED.GetElement(szDatabase, Constants.OPTIONS_ASSOCIATED, false));
                XEED.ReAssociate = Boolean.parseBoolean(XEED.GetElement(szDatabase, Constants.OPTIONS_REASSOCIATE, false));
                XEED.szDefaultDirectory = XEED.GetElement(szDatabase, Constants.OPTIONS_DEFAULTDIRECTORY, false);
                XEED.boolCustomRepository = Boolean.parseBoolean(XEED.GetElement(szDatabase,
                        Constants.OPTIONS_CUSTOMREPOSITORY, false));
                XEED.boolUpdateToBeta = Boolean.parseBoolean(XEED.GetElement(szDatabase, Constants.OPTIONS_UPDATETOBETA,
                        false));
                XEED.boolResize = Boolean.parseBoolean(XEED.GetElement(szDatabase, Constants.OPTIONS_RESIZEIMAGES, false));
                XEED.szCustomReposityURL = XEED.GetElement(szDatabase, Constants.OPTIONS_CUSTOMREPOSITORY_URL, false);
                XEED.szTemplateFolder = XEED.GetElement(szDatabase, Constants.OPTIONS_CUSTOMTEMPLATEFOLDER, false);

            } catch (Exception e) {

                if (!XEED.boolUpdate) {
                    int intRet = JOptionPane
                            .showOptionDialog(
                                    null,
                                    "Do you want xeed to check for updates everytime xeed starts up?\nThis option can be changed from File>Options.",
                                    "Check for updates?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
                                    null);
                    if (intRet == 0) {
                        XEED.boolUpdate = true;
                    } else {
                        XEED.boolUpdate = false;
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

        if (XEED.hwndNotes != null) {
            XEED.hwndNotes.dispose();
            XEED.hwndNotes = null;
        }
        if (XEED.hwndGroup != null) {
            XEED.hwndGroup.dispose();
            XEED.hwndGroup = null;
        }

        if (XEED.hwndRelations != null) {
            XEED.hwndRelations.dispose();
            XEED.hwndRelations = null;
        }

        if (XEED.hwndGraph != null) {
            XEED.hwndGraph.dispose();
            XEED.hwndGraph = null;
        }

        if (XEED.hwndCharacterConverter != null) {
            XEED.hwndCharacterConverter.dispose();
            XEED.hwndCharacterConverter = null;
        }

        if (XEED.hwndSettingInfo != null) {
            XEED.hwndSettingInfo.dispose();
            XEED.hwndSettingInfo = null;
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
        for (int x = 0; x < XEED.charDB.size(); x++) {

            if (XEED.charDB.get(x).characterID >= lngNewID) {
                lngNewID = XEED.charDB.get(x).characterID + 1;
            }
        }
        return lngNewID;
    }

    public static long CreateUniqueGroupID() {

        //Ej multitråds säker, så precis innan den läggs till!
        long lngNewID = 0;
        for (int x = 0; x < XEED.groupDB.size(); x++) {
            if (XEED.groupDB.get(x).lngID >= lngNewID) {
                lngNewID = XEED.groupDB.get(x).lngID + 1;
            }
        }
        return lngNewID;
    }

    public static void ExportNodesToFiles(Note[] s, long Parent, String szDir) throws Exception {

        if (s == null) {
            return;
        }
        for (int x = 0; x < s.length; x++) {

            if (s[x].boolFolder) {
                File d = new File(szDir + File.separator + s[x].szTitle);
                d.mkdirs();
                XEED.lngNoteID++;
                ExportNodesToFiles(s[x].GetChildren(), XEED.lngNoteID, d.getAbsolutePath());
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
                    ExportNodesToFiles(s[x].GetChildren(), XEED.lngNoteID, szDir);

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
            if (JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == 0) {
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

        hwndFileLock = new FileLocker(szPath);
        if (!hwndFileLock.IsLocked()) {
            hwndFileLock.ReleaseLock();
            NewSetting(false);
            JOptionPane.showMessageDialog(null, "This setting is already being used by another instance of XEED.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String szAutoSaves[] = hwndFileLock.GetOldBackupPaths();
        if (szAutoSaves.length > 0) {

            hwndFileLock.ReleaseLock(); //Close down file properly.
            hwndFileLock = null;

            hwndMain.setEnabled(false);
            JOptionPane
                    .showMessageDialog(
                            null,
                            "It appears as though a previous instance of XEED wasn't shutdown properly while handling the selected setting.\nYou may now restore autosaves from that setting in order to prevent data loss.",
                            "Autosaves found!", JOptionPane.INFORMATION_MESSAGE);
            new RestorerForm(szAutoSaves, szPath); //Handle list of backups
            return;
        }

        XDF x = new XDF(szPath);
        if (!x.ReadSetting()) {
            JOptionPane.showMessageDialog(null, "An error occured while opening the setting", "Error",
                    JOptionPane.ERROR_MESSAGE);
            NewSetting(false);
        }
        x = null;

        hwndFileLock.GetAutoSaverInstance().ManulSave();

    }

    public static boolean SaveSetting(boolean ask, boolean SetNewLocation) {

        if (ask) {
            int intRet = JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (intRet != 0) {
                return false;
            }
        }

        if (SetNewLocation || szPath.isEmpty()) {
            if (!SetActiveSetting(false)) {
                return false;
            }
        }

        XDF x = new XDF(szPath);
        if (!x.WriteSetting()) {

            JOptionPane.showMessageDialog(null, "An error occured while saving the setting", "Error",
                    JOptionPane.ERROR_MESSAGE);
            szPath = "";
            return false;

        } else {

            if (hwndFileLock != null) {
                if (!hwndFileLock.GetActiveSave().equalsIgnoreCase(szPath)) {
                    hwndFileLock.ReleaseLock();
                    hwndFileLock = new FileLocker(szPath);
                }
            } else {
                hwndFileLock = new FileLocker(szPath);
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
            if (JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == 0) {
                if (!SaveSetting(false, true)) {
                    return;
                }
            }
        }

        ClearSetting();

        if (ask) {
            XEED.hwndTemplateManager = new TemplateManagerForm();
            XEED.hwndTemplateManager.setVisible(true);

            XEED.hwndSettingInfo = new SettingInfoForm();
            XEED.hwndSettingInfo.setVisible(true);
        }

    }

    public static boolean SetActiveSetting(boolean Open) {

        JFileChooser fc = new JFileChooser();
        fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
        fc.addChoosableFileFilter(new XDFFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        if (!XEED.szDefaultDirectory.isEmpty()) {
            fc.setCurrentDirectory(new File(XEED.szDefaultDirectory));
        }

        if (Open) {

            fc.setDialogTitle("Select the file containing your setting.");
            int intRet = fc.showOpenDialog(null);

            if (intRet != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            XEED.szPath = fc.getSelectedFile().getAbsolutePath();
            XEED.szDefaultDirectory = fc.getSelectedFile().getParent();

        } else {

            fc.setDialogTitle("Select were to save your setting.");
            int intRet = fc.showSaveDialog(null);

            if (intRet != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            XEED.szPath = fc.getSelectedFile().getAbsolutePath();
            if (!XEED.szPath.contains(".")) {
                XEED.szPath += ".xdf";
            }

            XEED.szDefaultDirectory = fc.getSelectedFile().getParent();
        }

        return true;
    }

    public static boolean IsSettingEmpty() {

        if (!XEED.charDB.isEmpty()) {
            return false;
        }

        if (!XEED.groupDB.isEmpty()) {
            return false;
        }

        if (rootNode != null) {
            Note[] s = XEED.rootNode.GetChildren();
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

    public static Character GetCharacterByID(long id) {
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
            File f = new File(MainForm.class.getProtectionDomain().getCodeSource().getLocation().toURI());
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
                RT.exec("cmd /c ftype  XEED.Data.File="
                        + System.getProperties().getProperty("java.home")
                        + "\\bin\\javaw.exe"
                        + " -jar \""
                        + new File(MainForm.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                                .getAbsolutePath() + "\" /l:%1 %*");
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

    public static Image RescaleImage(Image i, int ImageMaxWidth, int ImageMaxHeight, boolean ForceSquare) {

        if (i == null) {
            return null;
        }
        BufferedImage img = ImageToBuffered(i);

        if (ForceSquare) {
            int w = img.getWidth();
            int h = img.getHeight();
            int m = Math.max(h, w);
            BufferedImage square = new BufferedImage(m, m, BufferedImage.TYPE_INT_ARGB);
            square.getGraphics().drawImage(img, (m - w) / 2, (m - h) / 2, null);
            img = square;
        }

        if (img.getWidth() > ImageMaxWidth || img.getHeight() > ImageMaxHeight) {
            if (img.getWidth() > img.getHeight()) {
                return img.getScaledInstance(ImageMaxWidth, -1, Image.SCALE_SMOOTH);
            } else {
                return img.getScaledInstance(-1, ImageMaxHeight, Image.SCALE_SMOOTH);
            }
        }

        return img;
    }

    public static BufferedImage ImageToBuffered(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static Character AddCharacter(Template t) {

        if (t == null) {
            return null;
        }

        Character c = new Character(t);
        charDB.add(c);
        hwndMain.AddListItem(c);

        Character[] affectedcharacters = new Character[charDB.size()];
        charDB.toArray(affectedcharacters);
        hwndNotifier.FireUpdate(affectedcharacters, true, true, true, true, false, false, true, false, false);
        return c;

    }

    public static void DeleteCharacter(Character c) {

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

        Character[] affectedcharacters = new Character[charDB.size()];
        charDB.toArray(affectedcharacters);
        hwndNotifier.FireUpdate(affectedcharacters, true, true, true, true, false, false, true, false, false);

    }

    public static boolean IsTemplateLoaded(Template t) {

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

    public static boolean LoadTemplate(Template t) {

        if (IsTemplateLoaded(t)) {
            JOptionPane.showMessageDialog(null, "A template (" + t.GetName()
                    + ") with the same ID is already loaded.\nTherefore the new template wasn't loaded.",
                    "Error while loading: " + t.GetName(), JOptionPane.ERROR_MESSAGE);
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

        if (XEED.hwndCharacterConverter != null) {
            XEED.hwndCharacterConverter.LoadTemplateList();
        }

        if (hwndColumns != null) {
            hwndColumns.LoadTemplates();
        }

        return true;

    }

    public static void UnloadTemplate(Template t) {

        if (!IsTemplateLoaded(t)) {
            return;
        }

        for (int x = XEED.charDB.size() - 1; x >= 0; x--) {
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

        if (XEED.hwndCharacterConverter != null) {
            XEED.hwndCharacterConverter.LoadTemplateList();
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

    public static Template GetTemplateByID(String ID) {
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

        XEED.lngSettingRevision = 0;
        XEED.szLastSaved = "";
        XEED.szThemes = "";
        XEED.szOutline = "";
        XEED.szPath = "";

        XEED.rootNode = new Note();
        XEED.rootNode.szTitle = "Notes";
        XEED.rootNode.boolFolder = true;
        XEED.rootNode.lngID = 0;

        XEED.charDB.clear();
        XEED.groupDB.clear();
        hwndMain.PurgeThenPrintCharacters();
        SetSettingTitle("");

        UnloadAllTemplates();

        if (hwndFileLock != null) {
            hwndFileLock.ReleaseLock();
        }

        hwndFileLock = new FileLocker();
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

            String szData = XEED.DownloadURLToString(szURL);
            String szRepositoryHeader = XEED.GetElement(szData, Constants.TEMPALTE_REPOSITORY, false);
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
