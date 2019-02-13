/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Erik
 */
public class XDF {

    /*
     * Options
     */
    public boolean UpdateCurrentDirectory = true;
    public boolean AutoSaves = false;
    private boolean ValidateMode = false; //Don't save anything loaded, just look for errors!
    /*
     * Handle
     */
    private String szXDFPath = "";
    private ZipOutputStream out = null;
    private ZipFile zf = null;

    /*
     * Error reporting
     */
    private int intFailedCharacters = 0;
    private int intFailedGroups = 0;
    private int intFailedNotes = 0;
    private int intFailedRelations = 0;
    private int intFailedTemplates = 0;
    /*
     * Data about active setting
     */
    private int intBuild = -1;

    /*
    * Notes
     */
    public XDF(String szFilePath) {
        szXDFPath = szFilePath;
    }

    private String ReadStringFromZIPEntry(ZipEntry ze) throws Exception {

        BufferedInputStream bis = new BufferedInputStream(zf.getInputStream(ze));

        byte[] bytes = new byte[4096];

        String szComp = "";
        while (bis.read(bytes) > 0) {
            szComp += new String(bytes, 0, bytes.length, "UTF-8");
        }

        bis.close();
        return szComp;
    }

    private boolean WriteTemplates() {

        String szManifest = "";
        for (int x = 0; x < XEED.templateDB.size(); x++) {
            szManifest += XEED.CreateElement(XEED.templateDB.get(x).GetTemplateID(), Constants.XDF_MANIFEST_ID, false);
        }
        WriteStringToZIPEntry(szManifest, "templates/manifest");

        for (int x = 0; x < XEED.templateDB.size(); x++) {
            try {
                String szTemplates = XEED.templateDB.get(x).CompileTemplate();
                if (!WriteStringToZIPEntry(szTemplates, "templates/" + XEED.templateDB.get(x).GetTemplateID() + "/info")) {
                    return false;
                }
                WriteStringToZIPEntry(XEED.templateDB.get(x).GetColumnConfig(), "templates/"
                        + XEED.templateDB.get(x).GetTemplateID() + "/columns_pc"); //Not so important, allow failure.

            } catch (Exception e) {
                return false;
            }
        }

        return true;

    }

    private boolean ReadTemplates() {

        try {

            ZipEntry ze = zf.getEntry("templates/manifest");
            String szManifest = ReadStringFromZIPEntry(ze);

            String szTemplateIDs[] = XEED.GetElements(szManifest, Constants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                XEED.UnloadAllTemplates();
            }
            for (int x = 0; x < szTemplateIDs.length; x++) {

                ze = zf.getEntry("templates/" + szTemplateIDs[x] + "/info");
                if (ze == null) {
                    intFailedTemplates++;
                } else {

                    String szTemplate = ReadStringFromZIPEntry(ze);
                    szTemplate = CompatabilityEngine(szTemplate);
                    Template t = new Template();
                    if (t.ParseTemplate(szTemplate)) {
                        if (!ValidateMode) {

                            ze = zf.getEntry("templates/" + szTemplateIDs[x] + "/columns_pc");
                            if (ze != null) {
                                String columnData = ReadStringFromZIPEntry(ze);
                                t.LoadColumns(columnData);
                            }

                            XEED.LoadTemplate(t);
                        }
                    } else {
                        intFailedTemplates++;
                    }
                }
            }
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    private boolean ReadCharacters() {
        //ZipFile -> zipentry -> zipfile.getinputstream
        try {

            ZipEntry ze = zf.getEntry("characters/manifest");
            String szManifest = ReadStringFromZIPEntry(ze);

            String szCharIDs[] = XEED.GetElements(szManifest, Constants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                XEED.charDB.clear();
            }
            for (int x = 0; x < szCharIDs.length; x++) {

                ze = zf.getEntry("characters/" + szCharIDs[x] + "/info");
                if (ze == null) {
                    intFailedCharacters++;
                } else {

                    if (!ValidateMode) {
                        String szCharacter = ReadStringFromZIPEntry(ze);
                        szCharacter = CompatabilityEngine(szCharacter);
                        Character c = Character.ParseCharacter(szCharacter);
                        ReadCharacterImages(c);
                        _readrelations("characters/" + szCharIDs[x] + "/", c, null);
                        if (c != null) {
                            XEED.charDB.add(c);
                        } else {
                            intFailedCharacters++;
                        }
                    }

                }
            }

        } catch (Exception e) {
            return false;
        }

        for (int x = 0; x < XEED.templateDB.size(); x++) {

            String[] keys = XEED.templateDB.get(x).GetCharacterMapKeys();
            if (!PerformCharacterLinking(keys)) {
                return false;
            }

        }

        return true;

    }

    private boolean ReadCharacterImages(Character c) {

        if (c == null) {
            return false;
        }

        try {

            ZipEntry ze = zf.getEntry("characters/" + c.characterID + "/images/manifest");
            String szManifest = ReadStringFromZIPEntry(ze);

            String szImageKeys[] = XEED.GetElements(szManifest, Constants.XDF_MANIFEST_ID, true);
            for (int x = 0; x < szImageKeys.length; x++) {
                ZipEntry ze_img = zf.getEntry("characters/" + c.characterID + "/images/" + szImageKeys[x]);
                InputStream in = zf.getInputStream(ze_img);
                BufferedImage bi = ImageIO.read(in);
                in.close();
                ImageIcon img = new ImageIcon(bi);
                c.imgData.put(szImageKeys[x], img);
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //Some data fields contains links to characters in the form of their long id, here they are interpreted into clsCharacter.
    private boolean PerformCharacterLinking(String[] keys) {

        if (keys == null) {
            return true;
        }

        if (keys.length == 0) {
            return true;
        }

        for (int y = 0; y < XEED.charDB.size(); y++) {

            for (int x = 0; x < keys.length; x++) {

                Object o = XEED.charDB.get(y).chrData.get(keys[x]);
                if (o != null) {
                    if (o.getClass() == Long.class) {
                        XEED.charDB.get(y).chrData.put(keys[x], XEED.GetCharacterByID((Long) o));
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;

    }

    private boolean ReadSettingInfo() {

        try {

            ZipEntry ze = zf.getEntry("setting_info");
            String szSettingInfoData = ReadStringFromZIPEntry(ze);

            try {
                intBuild = Integer.parseInt(XEED.GetElement(szSettingInfoData, Constants.SETTING_BUILD, false));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Build version invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!CompatabilityChecker()) {
                return false;
            }

            String szSetting = XEED.GetElement(szSettingInfoData, Constants.SETTING_HEADER, false);
            if (szSetting.isEmpty()) {
                if (!ValidateMode) {
                    XEED.szSettingName = "";
                    XEED.rootNode = new Note();
                    XEED.rootNode.szTitle = "Notes";
                    XEED.rootNode.boolFolder = true;
                    XEED.rootNode.lngID = 0;
                    XEED.lngSettingRevision = 0;
                    XEED.szOutline = "";
                    XEED.szThemes = "";
                    XEED.szLastSaved = "";
                }
            } else {
                if (!ValidateMode) {
                    XEED.szSettingName = XEED.GetElement(szSetting, Constants.SETTING_NAME, true);
                    XEED.szThemes = XEED.GetElement(szSetting, Constants.SETTING_THEME, true);
                    XEED.szOutline = XEED.GetElement(szSetting, Constants.SETTING_OUTLINE, true);
                    XEED.szLastSaved = XEED.GetElement(szSetting, Constants.SETTING_LAST_SAVED, false);

                    try {
                        XEED.lngSettingRevision = Long.parseLong(XEED
                                .GetElement(szSetting, Constants.SETTING_REVISION, false));
                    } catch (Exception e) {
                    }
                }
                XEED.rootNode = Note.ParseNodes(szSetting);
            }

            XEED.SetSettingTitle(XEED.szSettingName);
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured while reading setting info.\n" + e, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    private boolean CompatabilityChecker() {

        if (intBuild < 35) {
            JOptionPane
                    .showMessageDialog(
                            null,
                            "This setting file was created by an obsolete version of xeed and this version of xeed lacks backward compatability with that version.\nPlease use the appropriate version (i.e. build "
                            + intBuild + ").", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (intBuild > XEED.lngBuild) {
            JOptionPane.showMessageDialog(null,
                    "This setting file was created by a newer version of xeed.\nPlease use the appropriate version (i.e. build "
                    + intBuild + ").", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /*
     * Får endast kallas 1 gång per szData och endast när datapartierna är
     * kompletta
     */
    private String CompatabilityEngine(String szData) {

        return szData;

    }

    private boolean ReadNotes() {

        try {
            ZipEntry ze = zf.getEntry("notes/manifest");
            String szManifest = ReadStringFromZIPEntry(ze);

            String szNotesIDs[] = XEED.GetElements(szManifest, Constants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                XEED.rootNode = new Note();
                XEED.rootNode.szTitle = "Notes";
                XEED.rootNode.boolFolder = true;
                XEED.rootNode.lngID = 0;
            }
            for (int x = 0; x < szNotesIDs.length; x++) {

                ze = zf.getEntry("notes/" + szNotesIDs[x]);
                String szNode = ReadStringFromZIPEntry(ze);
                szNode = CompatabilityEngine(szNode);

                try {

                    Note s = new Note();

                    s.szTitle = XEED.GetElement(szNode, Constants.NOTE_TITLE, true);
                    s.boolFolder = Boolean.parseBoolean(XEED.GetElement(szNode, Constants.NOTE_FOLDER, false));
                    long parent = Long.parseLong(XEED.GetElement(szNode, Constants.NOTE_PARENT, false));
                    s.lngID = Long.parseLong(XEED.GetElement(szNode, Constants.NOTE_ID, false));

                    if (!s.boolFolder) {
                        s.szData = XEED.GetElement(szNode, Constants.NOTE_DATA, true);
                    }

                    if (!ValidateMode) {
                        XEED.rootNode.AddNodeToParent(s, parent);
                    }

                } catch (Exception e) {
                    intFailedNotes++;
                }
            }

        } catch (Exception e) {
            return false;
        }

        return true;

    }

    public boolean ReadSetting() {

        intFailedCharacters = 0;
        intFailedGroups = 0;
        intFailedNotes = 0;
        intFailedRelations = 0;
        intFailedTemplates = 0;
        ValidateMode = false;

        try {

            zf = new ZipFile(szXDFPath);
            if (zf == null) {
                return false;
            }

            if (!ReadSettingInfo()) {
                zf.close();
                return false;
            }

            if (!ReadTemplates()) {
                zf.close();
                return false;
            }

            if (!ReadCharacters()) {
                zf.close();
                return false;
            }

            if (!ReadNotes()) {
                zf.close();
                return false;
            }

            if (!ReadGroups()) {
                zf.close();
                return false;
            }

            zf.close();
            zf = null;

            XEED.hwndMain.PurgeThenPrintCharacters();

            String szErrorMessage = "";
            if (intFailedTemplates > 0) {
                szErrorMessage += "\nFailed to read " + intFailedTemplates + " templates.";
            }

            if (intFailedCharacters > 0) {
                szErrorMessage += "\nFailed to read " + intFailedCharacters + " characters.";
            }

            if (intFailedGroups > 0) {
                szErrorMessage += "\nFailed to read " + intFailedGroups + " groups.";
            }

            if (intFailedNotes > 0) {
                szErrorMessage += "\nFailed to read " + intFailedNotes + " notes.";
            }

            if (intFailedRelations > 0) {
                szErrorMessage += "\nFailed to read " + intFailedRelations + " relations.";
            }

            if (!szErrorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Server errors occured while reading and parsing the setting file:"
                        + szErrorMessage, "Partial corruption detected!", JOptionPane.ERROR_MESSAGE);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean BackUpFile() {

        try {

            File f = new File(szXDFPath);
            if (!f.exists()) {
                return true;
            }

            File d = new File(szXDFPath + "_backups");
            d.mkdirs();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd__HH.mm.ss");

            File f2 = new File(d.getAbsolutePath() + File.separatorChar + df.format(cal.getTime()) + ".xdf");
            return (f.renameTo(f2));

        } catch (Exception e) {
            JOptionPane
                    .showMessageDialog(null, "Error while creating back-ups!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean WriteStringToZIPEntry(String szData, String szEntry) {
        try {

            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(szEntry));

            StringBuilder sb = new StringBuilder(szData);
            while (sb.length() > 0) {
                String szPart = sb.substring(0, Math.min(4096, sb.length()));
                byte[] buf = szPart.getBytes("UTF-8");
                out.write(buf, 0, buf.length);
                sb.delete(0, Math.min(4096, sb.length()));
            }

            out.flush();
            // Complete the entry
            out.closeEntry();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean WriteCharacters() {

        String szManifest = "";
        for (int x = 0; x < XEED.charDB.size(); x++) {
            szManifest += XEED.CreateElement(Long.toString(XEED.charDB.get(x).characterID), Constants.XDF_MANIFEST_ID,
                    false);
        }

        WriteStringToZIPEntry(szManifest, "characters/manifest");

        String szCharacter = "";
        for (int x = 0; x < XEED.charDB.size(); x++) {
            try {
                szCharacter = XEED.charDB.get(x).CompileCharacter();
                Relation[] cr = new Relation[XEED.charDB.get(x).relationsDB.size()];
                XEED.charDB.get(x).relationsDB.toArray(cr);
                _writerelations("characters/" + XEED.charDB.get(x).characterID + "/", cr);
                WriteCharacterImages(XEED.charDB.get(x), "characters/" + XEED.charDB.get(x).characterID + "/images/");

                if (!WriteStringToZIPEntry(szCharacter, "characters/" + XEED.charDB.get(x).characterID + "/info")) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    private boolean WriteCharacterImages(Character c, String szPath) {

        String keys[] = c.GetImgKeys();
        String manifest = "";

        for (int x = 0; x < keys.length; x++) {

            if (c.imgData.get(keys[x]) != null) {
                if (c.imgData.get(keys[x]).getClass() == ImageIcon.class) {

                    ImageIcon img = (ImageIcon) c.imgData.get(keys[x]);
                    _writeimage(img, szPath + keys[x]);
                    manifest += XEED.CreateElement(keys[x], Constants.XDF_MANIFEST_ID, true);

                }
            }
        }

        WriteStringToZIPEntry(manifest, szPath + "manifest");
        return true;

    }

    private boolean _writeimage(ImageIcon img, String szPath) {

        try {

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            BufferedImage bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(img.getImage(), 0, 0, null);
            ImageIO.write(bi, "png", byteOut);

            out.putNextEntry(new ZipEntry(szPath));
            out.write(byteOut.toByteArray());
            out.flush();
            out.closeEntry();

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean WriteSettingInfo() {

        String szInfo = "";
        szInfo += XEED.CreateElement(Long.toString(XEED.lngBuild), Constants.SETTING_BUILD, false);

        String szSetting = XEED.CreateElement(XEED.szSettingName, Constants.SETTING_NAME, true);
        szSetting += XEED.CreateElement(Long.toString(XEED.lngSettingRevision), Constants.SETTING_REVISION, false);
        szSetting += XEED.CreateElement(XEED.szThemes, Constants.SETTING_THEME, true);
        szSetting += XEED.CreateElement(XEED.szOutline, Constants.SETTING_OUTLINE, true);

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM-yy");

        if (!AutoSaves) {
            XEED.szLastSaved = formatter.format(currentDate.getTime());
        }

        szSetting += XEED.CreateElement(formatter.format(currentDate.getTime()), Constants.SETTING_LAST_SAVED, false);

        szSetting = XEED.CreateElement(szSetting, Constants.SETTING_HEADER, false);

        szInfo += szSetting;
        return WriteStringToZIPEntry(szInfo, "setting_info");
    }

    private boolean WriteNotes() {

        ArrayList<String> NotesManifest = new ArrayList(0);
        _writenotes(XEED.rootNode.GetChildren(), 0, NotesManifest);

        String szManifest = "";
        for (int x = 0; x < NotesManifest.size(); x++) {
            szManifest += XEED.CreateElement(NotesManifest.get(x), Constants.XDF_MANIFEST_ID, false);
        }

        WriteStringToZIPEntry(szManifest, "notes/manifest");
        return true;
    }

    private boolean _readrelations(String szPath, Character ch, Group gr) {

        try {

            ZipEntry ze = zf.getEntry(szPath + "relations/manifest");
            String szManifest = ReadStringFromZIPEntry(ze);

            String szRelIDs[] = XEED.GetElements(szManifest, Constants.XDF_MANIFEST_ID, false);
            for (int x = 0; x < szRelIDs.length; x++) {

                ze = zf.getEntry(szPath + "relations/" + szRelIDs[x]);
                if (ze == null) {
                    return false;
                }
                String szRelation = ReadStringFromZIPEntry(ze);
                szRelation = CompatabilityEngine(szRelation);
                Relation r = Relation.ParseRelation(szRelation);
                if (r != null) {

                    if (!ValidateMode) {
                        if (ch != null) {
                            ch.relationsDB.add(r);
                        } else if (gr != null) {
                            gr.relationsDB.add(r);
                        }
                    }
                }
            }
            return true;

        } catch (Exception e) {
            intFailedRelations++;
            return false;
        }

    }

    private long _writenotes(Note[] s, long Parent, ArrayList<String> NotesManifest) {

        if (s == null) {
            return Parent;
        }

        long lngNoteID = Parent;
        for (int x = 0; x < s.length; x++) {

            String szNote = XEED.CreateElement(s[x].szTitle, Constants.NOTE_TITLE, true);
            szNote += XEED.CreateElement(Boolean.toString(s[x].boolFolder), Constants.NOTE_FOLDER, false);
            szNote += XEED.CreateElement(Long.toString(Parent), Constants.NOTE_PARENT, false);

            lngNoteID++;
            szNote += XEED.CreateElement(Long.toString(lngNoteID), Constants.NOTE_ID, false);

            if (!s[x].boolFolder) {
                szNote += XEED.CreateElement(s[x].szData, Constants.NOTE_DATA, true);
            }
            szNote = XEED.CreateElement(szNote, Constants.NOTE, false);

            WriteStringToZIPEntry(szNote, "notes/" + lngNoteID);
            NotesManifest.add(Long.toString(lngNoteID));
            lngNoteID = _writenotes(s[x].GetChildren(), lngNoteID, NotesManifest);

        }

        return lngNoteID; //return current number of notes written, which is used as ID.

    }

    private void _writerelations(String szPath, Relation[] relations) {

        String szManifest = "";
        for (int x = 0; x < relations.length; x++) {
            szManifest += XEED.CreateElement(Integer.toString(x), Constants.XDF_MANIFEST_ID, false);
        }

        WriteStringToZIPEntry(szManifest, szPath + "relations/manifest");

        for (int x = 0; x < relations.length; x++) {
            String szCompile = XEED.CreateElement(Long.toString(relations[x].lngTargetID), Constants.RELATION_TARGET,
                    false);
            szCompile += XEED.CreateElement(Long.toString(relations[x].intType), Constants.RELATION_TYPE, false);
            szCompile += XEED.CreateElement(relations[x].szRelation, Constants.RELATION_DATA, true);
            WriteStringToZIPEntry(szCompile, szPath + "relations/" + Integer.toString(x));
        }

    }

    private boolean WriteGroups() {

        String szManifest = "";
        for (int x = 0; x < XEED.groupDB.size(); x++) {
            szManifest += XEED.CreateElement(Long.toString(XEED.groupDB.get(x).lngID), Constants.XDF_MANIFEST_ID, false);
        }

        WriteStringToZIPEntry(szManifest, "groups/manifest");

        String szGroup = "";
        for (int x = 0; x < XEED.groupDB.size(); x++) {
            try {
                szGroup = XEED.groupDB.get(x).CompileGroup();

                Relation[] cr = new Relation[XEED.groupDB.get(x).relationsDB.size()];
                XEED.groupDB.get(x).relationsDB.toArray(cr);
                _writerelations("groups/" + XEED.groupDB.get(x).lngID + "/", cr);

                if (!WriteStringToZIPEntry(szGroup, "groups/" + Long.toString(XEED.groupDB.get(x).lngID) + "/info")) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;

    }

    private boolean ReadGroups() {

        try {

            ZipEntry ze = zf.getEntry("groups/manifest");
            String szManifest = ReadStringFromZIPEntry(ze);

            String szGroupsIDs[] = XEED.GetElements(szManifest, Constants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                XEED.groupDB.clear();
            }
            for (int x = 0; x < szGroupsIDs.length; x++) {

                ze = zf.getEntry("groups/" + szGroupsIDs[x] + "/info");
                if (ze == null) {
                    return false;
                }
                String szGroup = ReadStringFromZIPEntry(ze);
                szGroup = CompatabilityEngine(szGroup);
                Group g = Group.ParseGroup(szGroup);
                _readrelations("groups/" + szGroupsIDs[x] + "/", null, g);
                if (g != null) {
                    if (!ValidateMode) {
                        XEED.groupDB.add(g);
                    }
                } else {
                    intFailedGroups++;
                }
            }

            for (int x = 0; x < XEED.charDB.size(); x++) {
                Group.UpdateCharactersGroupList(XEED.charDB.get(x).characterID);
            }

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public boolean WriteSetting() {

        try {

            if (XEED.boolBackup && !AutoSaves) {
                if (!BackUpFile()) {
                    return false;
                }
            }

            new File(szXDFPath).delete();
            out = new ZipOutputStream(new FileOutputStream(new File(szXDFPath)));
            if (out == null) {
                return false;
            }

            if (!AutoSaves) {
                XEED.lngSettingRevision++;
            }

            if (!WriteSettingInfo()) {
                return false;
            }

            if (!WriteTemplates()) {
                return false;
            }

            if (!WriteCharacters()) {
                return false;
            }

            if (!WriteNotes()) {
                return false;
            }

            if (!WriteGroups()) {
                return false;
            }

            out.close();
            out = null;
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Checks the readability of a settingfile.
     *
     * @return 0 - Working setting, >0 - Number of Minor Error, -1 - Total
     * Corruption
     */
    public int ValidateSetting() {
        intFailedCharacters = 0;
        intFailedGroups = 0;
        intFailedNotes = 0;
        intFailedRelations = 0;
        intFailedTemplates = 0;
        UpdateCurrentDirectory = false;
        ValidateMode = true;

        try {

            zf = new ZipFile(szXDFPath);
            if (zf == null) {
                return -1;
            }

            if (!ReadSettingInfo()) {
                zf.close();
                return -1;
            }

            if (!ReadTemplates()) {
                zf.close();
                return -1;
            }

            if (!ReadCharacters()) {
                zf.close();
                return -1;
            }

            if (!ReadNotes()) {
                zf.close();
                return -1;
            }

            if (!ReadGroups()) {
                zf.close();
                return -1;
            }

            zf.close();
            zf = null;

            return intFailedCharacters + intFailedGroups + intFailedNotes + intFailedRelations + intFailedTemplates;

        } catch (Exception e) {
            return -1;
        }
    }
}
