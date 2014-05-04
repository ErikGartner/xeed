/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Erik
 */
public class clsXDF {

    /*
     * Setting
     */
    public boolean UpdateCurrentDirectory = true;
    public boolean AutoSaves = false;
    private boolean ValidateMode = false;                                       //Don't save anything loaded, just look for errors!
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
    private long lngNoteID = 0;
    private ArrayList<String> NotesManifest = new ArrayList(0);
    /*
     * Post-reading linking
     */
    private ArrayList<String> KeysToBeCharacterLinked = new ArrayList(0);

    public clsXDF(String szFilePath) {
        szXDFPath = szFilePath;
    }

    private String ReadDataToString(InputStream in) throws Exception {

        BufferedInputStream bis = new BufferedInputStream(in);

        byte[] bytes = new byte[4096];

        String szComp = "";
        while (bis.read(bytes) > 0) {
            szComp += new String(bytes, 0, bytes.length, "UTF-8");
        }

        bis.close();
        in.close();
        return szComp;
    }

    private boolean WriteTemplates() {


        String szManifest = "";
        for (int x = 0; x < clsEngine.templateDB.size(); x++) {
            szManifest += clsEngine.CreateElement(clsEngine.templateDB.get(x).GetTemplateID(), clsConstants.XDF_MANIFEST_ID, false);
        }
        WriteStringToZIPEntry(szManifest, "templates/manifest");

        for (int x = 0; x < clsEngine.templateDB.size(); x++) {
            try {
                String szTemplates = clsEngine.templateDB.get(x).CompileTemplate();
                if (!WriteStringToZIPEntry(szTemplates, "templates/" + clsEngine.templateDB.get(x).GetTemplateID() + "/info")) {
                    return false;
                }
                WriteStringToZIPEntry(clsEngine.templateDB.get(x).GetColumnConfig(), "templates/" + clsEngine.templateDB.get(x).GetTemplateID() + "/columns_pc"); //Not so important, allow failure.

            } catch (Exception e) {
                return false;
            }
        }

        return true;

    }

    private boolean ReadTemplates() {

        try {

            ZipEntry ze = zf.getEntry("templates/manifest");
            String szManifest = ReadDataToString(zf.getInputStream(ze));

            String szTemplateIDs[] = clsEngine.GetElements(szManifest, clsConstants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                clsEngine.UnloadAllTemplates();
            }
            for (int x = 0; x < szTemplateIDs.length; x++) {

                ze = zf.getEntry("templates/" + szTemplateIDs[x] + "/info");
                if (ze == null) {
                    intFailedTemplates++;
                } else {

                    String szTemplate = ReadDataToString(zf.getInputStream(ze));
                    szTemplate = CompatabilityEngine(szTemplate);
                    clsTemplate t = new clsTemplate();
                    if (t.ParseTemplate(szTemplate)) {
                        if (!ValidateMode) {

                            ze = zf.getEntry("templates/" + szTemplateIDs[x] + "/columns_pc");
                            if (ze != null) {
                                String columnData = ReadDataToString(zf.getInputStream(ze));
                                t.LoadColumns(columnData);
                            }

                            clsEngine.LoadTemplate(t);
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
            String szManifest = ReadDataToString(zf.getInputStream(ze));

            String szCharIDs[] = clsEngine.GetElements(szManifest, clsConstants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                clsEngine.charDB.clear();
            }
            for (int x = 0; x < szCharIDs.length; x++) {

                ze = zf.getEntry("characters/" + szCharIDs[x] + "/info");
                if (ze == null) {
                    intFailedCharacters++;
                } else {

                    if (!ValidateMode) {
                        String szCharacter = ReadDataToString(zf.getInputStream(ze));
                        szCharacter = CompatabilityEngine(szCharacter);
                        clsCharacter c = ParseCharacter(szCharacter);
                        ReadCharacterImages(c);
                        _readrelations("characters/" + szCharIDs[x] + "/", c, null);
                        if (c != null) {
                            clsEngine.charDB.add(c);
                        } else {
                            intFailedCharacters++;
                        }
                    }

                }
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private clsCharacter ParseCharacter(String szData) {

        String data = clsEngine.GetElement(szData, clsConstants.CHARACTER, false);
        if (data.isEmpty()) {
            return null;
        }

        String template = clsEngine.GetElement(data, clsConstants.CHARACTER_TEMPLATE, true);
        if (template.isEmpty()) {
            return null;
        }

        clsTemplate t = clsEngine.GetTemplateByID(template);
        if (t == null) {
            return null;
        }

        clsCharacter c = new clsCharacter(t);
        try {
            c.characterID = Long.parseLong(clsEngine.GetElement(data, clsConstants.CHARACTER_ID, false));
        } catch (Exception e) {
            return null;
        }
        c.szData.put(clsConstants.CHARACTER_NAME, clsEngine.GetElement(data, clsConstants.CHARACTER_NAME, true));

        String items[] = clsEngine.GetElements(data, clsConstants.CHARACTER_ITEM, false);
        for (int x = 0; x < items.length; x++) {

            String key = clsEngine.GetElement(items[x], clsConstants.CHARACTER_KEY, true);

            //Check for linking needs.
            String spec_op = clsEngine.GetElement(items[x], clsConstants.CHARACTER_SPECOP, false);
            if (spec_op.equalsIgnoreCase(clsConstants.CHARACTER)) {

                if (!KeysToBeCharacterLinked.contains(key)) {
                    KeysToBeCharacterLinked.add(key);
                }

                try {
                    long lng = Long.parseLong(clsEngine.GetElement(items[x], clsConstants.CHARACTER_DATA, false));
                    c.chrData.put(key, lng);
                } catch (Exception e) {
                }

            } else if (spec_op.equalsIgnoreCase(clsConstants.CHARACTER_EXTENDED)) {

                clsExtendedSheetData esd = new clsExtendedSheetData();
                esd.Properties = clsEngine.GetElements(data, clsConstants.CHARACTER_DATA_PROPERTY, true);
                esd.Values = clsEngine.GetElements(data, clsConstants.CHARACTER_DATA_VALUE, true);
                if (esd.Properties.length != esd.Values.length) {
                    return null;
                }
                c.extData.put(key, esd);

            } else {
                String item_data = clsEngine.GetElement(items[x], clsConstants.CHARACTER_DATA, true);
                c.szData.put(key, item_data);
            }

        }
        return c;
    }

    private boolean ReadCharacterImages(clsCharacter c) {

        if (c == null) {
            return false;
        }

        try {

            ZipEntry ze = zf.getEntry("characters/" + c.characterID + "/images/manifest");
            String szManifest = ReadDataToString(zf.getInputStream(ze));

            String szImageKeys[] = clsEngine.GetElements(szManifest, clsConstants.XDF_MANIFEST_ID, true);
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
    private boolean PerformLinking() {

        if (KeysToBeCharacterLinked.isEmpty()) {
            return true;
        }

        for (int y = 0; y < clsEngine.charDB.size(); y++) {
            //KeysToBeCharacterLinked is for parenting
            for (int x = 0; x < KeysToBeCharacterLinked.size(); x++) {

                Object o = clsEngine.charDB.get(y).chrData.get(KeysToBeCharacterLinked.get(x));
                if (o != null) {
                    if (o.getClass() == Long.class) {
                        clsEngine.charDB.get(y).chrData.put(KeysToBeCharacterLinked.get(x), clsEngine.GetCharacterByID((Long) o));
                    } else {
                        return false;
                    }
                }
            }
        }
        KeysToBeCharacterLinked.clear();
        return true;

    }

    private boolean ReadSettingInfo() {

        try {

            ZipEntry ze = zf.getEntry("setting_info");
            String szSettingInfoData = ReadDataToString(zf.getInputStream(ze));

            try {
                intBuild = Integer.parseInt(clsEngine.GetElement(szSettingInfoData, clsConstants.SETTING_BUILD, false));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Build version invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!CompatabilityChecker()) {
                return false;
            }

            String szSetting = clsEngine.GetElement(szSettingInfoData, clsConstants.SETTING_HEADER, false);
            if (szSetting.isEmpty()) {
                if (!ValidateMode) {
                    clsEngine.szSettingName = "";
                    clsEngine.rootNode = new clsNote();
                    clsEngine.rootNode.szTitle = "Notes";
                    clsEngine.rootNode.boolFolder = true;
                    clsEngine.rootNode.lngID = 0;
                    clsEngine.lngSettingRevision = 0;
                    clsEngine.szOutline = "";
                    clsEngine.szThemes = "";
                    clsEngine.szLastSaved = "";
                }
            } else {
                if (!ValidateMode) {
                    clsEngine.szSettingName = clsEngine.GetElement(szSetting, clsConstants.SETTING_NAME, true);
                    clsEngine.szThemes = clsEngine.GetElement(szSetting, clsConstants.SETTING_THEME, true);
                    clsEngine.szOutline = clsEngine.GetElement(szSetting, clsConstants.SETTING_OUTLINE, true);
                    clsEngine.szLastSaved = clsEngine.GetElement(szSetting, clsConstants.SETTING_LAST_SAVED, false);

                    try {
                        clsEngine.lngSettingRevision = Long.parseLong(clsEngine.GetElement(szSetting, clsConstants.SETTING_REVISION, false));
                    } catch (Exception e) {
                    }
                }
                ImportNodes(szSetting);
            }

            clsEngine.SetSettingTitle(clsEngine.szSettingName);
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occured while reading setting info.\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    private boolean CompatabilityChecker() {

        if (intBuild < 35) {
            JOptionPane.showMessageDialog(null, "This setting file was created by an obsolete version of xeed and this version of xeed lacks backward compatability with that version.\nPlease use the appropriate version (i.e. build " + intBuild + ").", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (intBuild > clsEngine.lngBuild) {
            JOptionPane.showMessageDialog(null, "This setting file was created by a newer version of xeed.\nPlease use the appropriate version (i.e. build " + intBuild + ").", "Error", JOptionPane.ERROR_MESSAGE);
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
            String szManifest = ReadDataToString(zf.getInputStream(ze));

            String szNotesIDs[] = clsEngine.GetElements(szManifest, clsConstants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                clsEngine.rootNode = new clsNote();
                clsEngine.rootNode.szTitle = "Notes";
                clsEngine.rootNode.boolFolder = true;
                clsEngine.rootNode.lngID = 0;
            }
            for (int x = 0; x < szNotesIDs.length; x++) {

                ze = zf.getEntry("notes/" + szNotesIDs[x]);
                String szNode = ReadDataToString(zf.getInputStream(ze));
                szNode = CompatabilityEngine(szNode);

                try {

                    clsNote s = new clsNote();

                    s.szTitle = clsEngine.GetElement(szNode, clsConstants.NOTE_TITLE, true);
                    s.boolFolder = Boolean.parseBoolean(clsEngine.GetElement(szNode, clsConstants.NOTE_FOLDER, false));
                    long parent = Long.parseLong(clsEngine.GetElement(szNode, clsConstants.NOTE_PARENT, false));
                    s.lngID = Long.parseLong(clsEngine.GetElement(szNode, clsConstants.NOTE_ID, false));


                    if (!s.boolFolder) {
                        s.szData = clsEngine.GetElement(szNode, clsConstants.NOTE_DATA, true);
                    }

                    if (!ValidateMode) {
                        clsEngine.rootNode.AddNodeToParent(s, parent);
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

            if (!PerformLinking()) {
                return false;
            }

            clsEngine.hwndMain.PurgeThenPrintCharacters();

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
                JOptionPane.showMessageDialog(null, "Server errors occured while reading and parsing the setting file:" + szErrorMessage, "Partial corruption detected!", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Error while creating back-ups!\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
        for (int x = 0; x < clsEngine.charDB.size(); x++) {
            szManifest += clsEngine.CreateElement(Long.toString(clsEngine.charDB.get(x).characterID), clsConstants.XDF_MANIFEST_ID, false);
        }

        WriteStringToZIPEntry(szManifest, "characters/manifest");

        String szCharacter = "";
        for (int x = 0; x < clsEngine.charDB.size(); x++) {
            try {
                szCharacter = clsEngine.charDB.get(x).CompileCharacter();
                clsRelation[] cr = new clsRelation[clsEngine.charDB.get(x).relationsDB.size()];
                clsEngine.charDB.get(x).relationsDB.toArray(cr);
                _writerelations("characters/" + clsEngine.charDB.get(x).characterID + "/", cr);
                WriteCharacterImages(clsEngine.charDB.get(x), "characters/" + clsEngine.charDB.get(x).characterID + "/images/");

                if (!WriteStringToZIPEntry(szCharacter, "characters/" + clsEngine.charDB.get(x).characterID + "/info")) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    private boolean WriteCharacterImages(clsCharacter c, String szPath) {

        String keys[] = c.GetImgKeys();
        String manifest = "";

        for (int x = 0; x < keys.length; x++) {

            if (c.imgData.get(keys[x]) != null) {
                if (c.imgData.get(keys[x]).getClass() == ImageIcon.class) {

                    ImageIcon img = (ImageIcon) c.imgData.get(keys[x]);
                    _writeimage(img, szPath + keys[x]);
                    manifest += clsEngine.CreateElement(keys[x], clsConstants.XDF_MANIFEST_ID, true);

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
        szInfo += clsEngine.CreateElement(Long.toString(clsEngine.lngBuild), clsConstants.SETTING_BUILD, false);

        String szSetting = clsEngine.CreateElement(clsEngine.szSettingName, clsConstants.SETTING_NAME, true);
        szSetting += clsEngine.CreateElement(Long.toString(clsEngine.lngSettingRevision), clsConstants.SETTING_REVISION, false);
        szSetting += clsEngine.CreateElement(clsEngine.szThemes, clsConstants.SETTING_THEME, true);
        szSetting += clsEngine.CreateElement(clsEngine.szOutline, clsConstants.SETTING_OUTLINE, true);

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM-yy");

        if (!AutoSaves) {
            clsEngine.szLastSaved = formatter.format(currentDate.getTime());
        }

        szSetting += clsEngine.CreateElement(formatter.format(currentDate.getTime()), clsConstants.SETTING_LAST_SAVED, false);

        szSetting = clsEngine.CreateElement(szSetting, clsConstants.SETTING_HEADER, false);

        szInfo += szSetting;
        return WriteStringToZIPEntry(szInfo, "setting_info");
    }

    private boolean WriteNotes() {

        lngNoteID = 0;

        NotesManifest.clear();
        _writenotes(clsEngine.rootNode.GetChildren(), 0);

        String szManifest = "";
        for (int x = 0; x < NotesManifest.size(); x++) {
            szManifest += clsEngine.CreateElement(NotesManifest.get(x), clsConstants.XDF_MANIFEST_ID, false);
        }

        WriteStringToZIPEntry(szManifest, "notes/manifest");
        return true;
    }

    private boolean _readrelations(String szPath, clsCharacter ch, clsGroup gr) {

        try {

            ZipEntry ze = zf.getEntry(szPath + "relations/manifest");
            String szManifest = ReadDataToString(zf.getInputStream(ze));

            String szRelIDs[] = clsEngine.GetElements(szManifest, clsConstants.XDF_MANIFEST_ID, false);
            for (int x = 0; x < szRelIDs.length; x++) {

                ze = zf.getEntry(szPath + "relations/" + szRelIDs[x]);
                if (ze == null) {
                    return false;
                }
                String szRelation = ReadDataToString(zf.getInputStream(ze));
                szRelation = CompatabilityEngine(szRelation);
                clsRelation r = ParseRelation(szRelation);
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

    private void _writenotes(clsNote[] s, long Parent) {

        if (s == null) {
            return;
        }

        for (int x = 0; x < s.length; x++) {

            String szNote = clsEngine.CreateElement(s[x].szTitle, clsConstants.NOTE_TITLE, true);
            szNote += clsEngine.CreateElement(Boolean.toString(s[x].boolFolder), clsConstants.NOTE_FOLDER, false);
            szNote += clsEngine.CreateElement(Long.toString(Parent), clsConstants.NOTE_PARENT, false);
            lngNoteID++;

            szNote += clsEngine.CreateElement(Long.toString(lngNoteID), clsConstants.NOTE_ID, false);

            if (!s[x].boolFolder) {
                szNote += clsEngine.CreateElement(s[x].szData, clsConstants.NOTE_DATA, true);
            }
            szNote = clsEngine.CreateElement(szNote, clsConstants.NOTE, false);

            WriteStringToZIPEntry(szNote, "notes/" + lngNoteID);
            NotesManifest.add(Long.toString(lngNoteID));
            _writenotes(s[x].GetChildren(), lngNoteID);

        }
    }

    private void _writerelations(String szPath, clsRelation[] relations) {

        String szManifest = "";
        for (int x = 0; x < relations.length; x++) {
            szManifest += clsEngine.CreateElement(Integer.toString(x), clsConstants.XDF_MANIFEST_ID, false);
        }

        WriteStringToZIPEntry(szManifest, szPath + "relations/manifest");

        for (int x = 0; x < relations.length; x++) {
            String szCompile = clsEngine.CreateElement(Long.toString(relations[x].lngTargetID), clsConstants.RELATION_TARGET, false);
            szCompile += clsEngine.CreateElement(Long.toString(relations[x].intType), clsConstants.RELATION_TYPE, false);
            szCompile += clsEngine.CreateElement(relations[x].szRelation, clsConstants.RELATION_DATA, true);
            WriteStringToZIPEntry(szCompile, szPath + "relations/" + Integer.toString(x));
        }

    }

    private boolean WriteGroups() {

        String szManifest = "";
        for (int x = 0; x < clsEngine.groupDB.size(); x++) {
            szManifest += clsEngine.CreateElement(Long.toString(clsEngine.groupDB.get(x).lngID), clsConstants.XDF_MANIFEST_ID, false);
        }

        WriteStringToZIPEntry(szManifest, "groups/manifest");

        String szGroup = "";
        for (int x = 0; x < clsEngine.groupDB.size(); x++) {
            try {
                szGroup = CompileGroup(clsEngine.groupDB.get(x));

                clsRelation[] cr = new clsRelation[clsEngine.groupDB.get(x).relationsDB.size()];
                clsEngine.groupDB.get(x).relationsDB.toArray(cr);
                _writerelations("groups/" + clsEngine.groupDB.get(x).lngID + "/", cr);

                if (!WriteStringToZIPEntry(szGroup, "groups/" + Long.toString(clsEngine.groupDB.get(x).lngID) + "/info")) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        return true;

    }

    private String CompileGroup(clsGroup g) {

        String szCompile = "";
        szCompile += clsEngine.CreateElement(Long.toString(g.lngID), clsConstants.GROUP_ID, false);

        long[] lngMembers = g.GetMemebers();
        for (int x = 0; x < lngMembers.length; x++) {
            szCompile += clsEngine.CreateElement(Long.toString(lngMembers[x]), clsConstants.GROUP_MEMBERS, false);
        }

        szCompile += clsEngine.CreateElement(g.szName, clsConstants.GROUP_NAME, true);
        szCompile += clsEngine.CreateElement(g.szDescription, clsConstants.GROUP_DESCRIPTION, true);

        return szCompile;
    }

    private boolean ReadGroups() {

        try {

            ZipEntry ze = zf.getEntry("groups/manifest");
            String szManifest = ReadDataToString(zf.getInputStream(ze));

            String szGroupsIDs[] = clsEngine.GetElements(szManifest, clsConstants.XDF_MANIFEST_ID, false);

            if (!ValidateMode) {
                clsEngine.groupDB.clear();
            }
            for (int x = 0; x < szGroupsIDs.length; x++) {

                ze = zf.getEntry("groups/" + szGroupsIDs[x] + "/info");
                if (ze == null) {
                    return false;
                }
                String szGroup = ReadDataToString(zf.getInputStream(ze));
                szGroup = CompatabilityEngine(szGroup);
                clsGroup g = ParseGroup(szGroup);
                _readrelations("groups/" + szGroupsIDs[x] + "/", null, g);
                if (g != null) {
                    if (!ValidateMode) {
                        clsEngine.groupDB.add(g);
                    }
                } else {
                    intFailedGroups++;
                }
            }

            for (int x = 0; x < clsEngine.charDB.size(); x++) {
                clsGroup.UpdateCharactersGroupList(clsEngine.charDB.get(x).characterID);
            }

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    private clsGroup ParseGroup(String szData) {

        try {
            String[] szMembers = clsEngine.GetElements(szData, clsConstants.GROUP_MEMBERS, false);

            clsGroup g = new clsGroup();
            g.lngID = Long.parseLong(clsEngine.GetElement(szData, clsConstants.GROUP_ID, false));
            g.szName = clsEngine.GetElement(szData, clsConstants.GROUP_NAME, true);
            g.szDescription = clsEngine.GetElement(szData, clsConstants.GROUP_DESCRIPTION, true);

            for (int x = 0; x < szMembers.length; x++) {
                g.AddMember(Long.parseLong(szMembers[x]));
            }

            return g;

        } catch (Exception e) {
            return null;
        }

    }

    private clsRelation ParseRelation(String szData) {

        try {

            clsRelation r = new clsRelation();
            r.lngTargetID = Long.parseLong(clsEngine.GetElement(szData, clsConstants.RELATION_TARGET, false));
            r.intType = Integer.parseInt(clsEngine.GetElement(szData, clsConstants.RELATION_TYPE, false));
            r.szRelation = clsEngine.GetElement(szData, clsConstants.RELATION_DATA, true);

            return r;

        } catch (Exception e) {
            return null;
        }

    }

    public boolean WriteSetting() {

        try {

            if (clsEngine.boolBackup && !AutoSaves) {
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
                clsEngine.lngSettingRevision++;
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

            if (!PerformLinking()) {
                return -1;
            }

            return intFailedCharacters + intFailedGroups + intFailedNotes + intFailedRelations + intFailedTemplates;

        } catch (Exception e) {
            return -1;
        }
    }

    private void ImportNodes(String szData) {

        clsEngine.rootNode = new clsNote();
        clsEngine.rootNode.szTitle = "Notes";
        clsEngine.rootNode.boolFolder = true;
        clsEngine.rootNode.lngID = 0;

        String szNodes[] = clsEngine.GetElements(szData, clsConstants.NOTE, false);

        for (int x = 0; x < szNodes.length; x++) {

            try {

                clsNote s = new clsNote();

                s.szTitle = clsEngine.GetElement(szNodes[x], clsConstants.NOTE_TITLE, true);
                s.boolFolder = Boolean.parseBoolean(clsEngine.GetElement(szNodes[x], clsConstants.NOTE_FOLDER, false));

                long parent = Long.parseLong(clsEngine.GetElement(szNodes[x], clsConstants.NOTE_PARENT, false));
                s.lngID = Long.parseLong(clsEngine.GetElement(szNodes[x], clsConstants.NOTE_ID, false));

                if (!s.boolFolder) {
                    s.szData = clsEngine.GetElement(szNodes[x], clsConstants.NOTE_DATA, true);
                }

                clsEngine.rootNode.AddNodeToParent(s, parent);
            } catch (Exception e) {
            }

        }

    }
}
