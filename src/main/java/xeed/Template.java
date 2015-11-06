/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import templates.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Erik
 */
public class Template {

    //Data in the about the Template
    private ArrayList<Section> sections = new ArrayList(0);
    private String szName;
    private String szAuthor;
    private String szGame;
    private String szDescription;
    private String szDate;
    private int Version;
    private String szTemplateID;
    private long XEEDBuild; //The version of xeed that created the template.
    private String szPath; //Kanske inte behövs.
    private int IDSeed = 0; //Appends to name to make unique id for items.
    private boolean locked = false;
    //Metadata of the template
    private ArrayList<key> keys = new ArrayList(0);
    //Constants defining the type id of an item
    private CharacterPanel topPanel; //private handle to the target pnlCharacter panel

    //Reading and Displaying a of template
    public void PreviewTemplate(String Name) {

        JFrame frame = new JFrame("[Preview] '" + Name + "'");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(RenderTemplate(null), BorderLayout.CENTER);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);

    }

    public boolean IsToShowColumn(String key) {

        for (int x = 0; x < keys.size(); x++) {
            if (keys.get(x).key.equalsIgnoreCase(key)) {
                return keys.get(x).visible;
            }
        }
        return false;
    }

    public void SetColumnVisible(String key, boolean visible) {

        if (key.equals(Constants.CHARACTER_NAME)) { //cannot be changed.
            return;
        }

        for (int x = 0; x < keys.size(); x++) {
            if (keys.get(x).key.equalsIgnoreCase(key)) {
                keys.get(x).visible = visible;
                return;
            }
        }

    }

    public String GetPropertyNameFromKey(String key) {

        for (int x = 0; x < keys.size(); x++) {
            if (keys.get(x).key.equalsIgnoreCase(key)) {
                return keys.get(x).szName;
            }
        }

        return "";
    }

    // <editor-fold defaultstate="collapsed" desc="Property getters">
    public String GetName() {
        return szName;
    }

    public String GetAuthor() {
        return szAuthor;
    }

    public String GetGame() {
        return szGame;
    }

    public String GetTemplateID() {
        return szTemplateID;
    }

    public String GetFilePath() {
        return szPath;
    }

    public long GetXEEDVersion() {
        return XEEDBuild;
    }

    public int GetVersion() {
        return Version;
    }

    public String GetDescription() {
        return szDescription;
    }

    public String GetCreationDate() {
        return szDate;
    }

    public String[] GetAllTemplateKeys() {

        String[] szkeys = new String[keys.size()];
        for (int x = 0; x < keys.size(); x++) {
            szkeys[x] = keys.get(x).key;
        }
        return szkeys;

    }

    public String[] GetAllTemplateNames() {

        String[] names = new String[keys.size()];
        for (int x = 0; x < keys.size(); x++) {
            names[x] = keys.get(x).szName;
        }
        return names;

    }

    public boolean[] GetDataTypeArray(String key) {

        for (int x = 0; x < keys.size(); x++) {
            if (keys.get(x).key.equalsIgnoreCase(key)) {

                boolean[] dataType = new boolean[4];
                dataType[0] = keys.get(x).isChrData;
                dataType[1] = keys.get(x).isExtData;
                dataType[2] = keys.get(x).isImgData;
                dataType[3] = keys.get(x).isSzData;
                return dataType;
            }
        }
        return null;

    }

    public String[] GetCharacterMapKeys() {

        String allKeys[] = GetAllTemplateKeys();
        ArrayList<String> retKeys = new ArrayList(0);

        for (int x = 0; x < allKeys.length; x++) {

            boolean[] types = GetDataTypeArray(allKeys[x]);
            if (types[0]) {
                retKeys.add(allKeys[x]);
            }

        }

        String[] ret = new String[retKeys.size()];
        retKeys.toArray(ret);
        return ret;

    }

    public String[] GetExtendedMapKeys() {

        String allKeys[] = GetAllTemplateKeys();
        ArrayList<String> retKeys = new ArrayList(0);

        for (int x = 0; x < allKeys.length; x++) {

            boolean[] types = GetDataTypeArray(allKeys[x]);
            if (types[1]) {
                retKeys.add(allKeys[x]);
            }

        }

        String[] ret = new String[retKeys.size()];
        retKeys.toArray(ret);
        return ret;

    }

    public String[] GetImageMapKeys() {

        String allKeys[] = GetAllTemplateKeys();
        ArrayList<String> retKeys = new ArrayList(0);

        for (int x = 0; x < allKeys.length; x++) {

            boolean[] types = GetDataTypeArray(allKeys[x]);
            if (types[2]) {
                retKeys.add(allKeys[x]);
            }

        }

        String[] ret = new String[retKeys.size()];
        retKeys.toArray(ret);
        return ret;

    }

    public String[] GetStringMapKeys() {

        String allKeys[] = GetAllTemplateKeys();
        ArrayList<String> retKeys = new ArrayList(0);

        for (int x = 0; x < allKeys.length; x++) {

            boolean[] types = GetDataTypeArray(allKeys[x]);
            if (types[3]) {
                retKeys.add(allKeys[x]);
            }

        }

        String[] ret = new String[retKeys.size()];
        retKeys.toArray(ret);
        return ret;

    }

    // </editor-fold>
    //Creation of templates
    public boolean FinalizeTemplate(String szTemplateName, String szA, String szG, String szD, int iV) {

        JFileChooser fc = new JFileChooser();
        fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
        fc.addChoosableFileFilter(new FileNameExtensionFilter("XEED Template", "xdt"));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.setDialogTitle("Select were to save your template.");

        File folder = new File(XEED.GetCurrentDirectory() + File.separator + "templates");
        folder.mkdir();
        fc.setCurrentDirectory(folder);

        int intRet = fc.showSaveDialog(null);
        if (intRet != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        szName = szTemplateName;
        szAuthor = szA;
        szGame = szG;
        szDescription = szD;
        Version = iV;
        szTemplateID = szName + "_" + System.currentTimeMillis();

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        szDate = formatter.format(currentDate.getTime());

        key group_key = new key();
        group_key.key = Constants.CHARACTER_GROUPS;
        group_key.szName = "Groups";
        group_key.isSzData = true;
        keys.add(group_key);

        String szPath = fc.getSelectedFile().getAbsolutePath();
        if (!szPath.endsWith(".xdt")) {
            szPath += ".xdt";
        }
        locked = WriteTemplateToFile(szPath);

        if (!locked) {
            JOptionPane.showMessageDialog(null, "Error while writing template to file.", "An error occured",
                    JOptionPane.ERROR_MESSAGE);
        }
        return locked;

    }

    public boolean ParseTemplate(String data) {

        String header = XEED.GetElement(data, Constants.TEMPLATE_HEADER, false);

        szName = XEED.GetElement(header, Constants.TEMPLATE_NAME, true);
        if (szName.isEmpty()) {
            return false;
        }

        szDescription = XEED.GetElement(header, Constants.TEMPLATE_DESCRIPTION, true);
        if (szDescription.isEmpty()) {
            return false;
        }

        szTemplateID = XEED.GetElement(header, Constants.TEMPLATE_ID, true);
        if (szTemplateID.isEmpty()) {
            return false;
        }

        szAuthor = XEED.GetElement(header, Constants.TEMPLATE_AUTHOR, true);
        if (szAuthor.isEmpty()) {
            return false;
        }

        szDate = XEED.GetElement(header, Constants.TEMPLATE_DATE, false);
        if (szDate.isEmpty()) {
            return false;
        }

        szGame = XEED.GetElement(header, Constants.TEMPLATE_GAME, true);
        if (szGame.isEmpty()) {
            return false;
        }

        try {
            Version = Integer.parseInt(XEED.GetElement(header, Constants.TEMPLATE_VERSION, false));
            XEEDBuild = Long.parseLong(XEED.GetElement(header, Constants.TEMPLATE_XEED_BUILD, false));
        } catch (Exception e) {
            return false;
        }

        if (XEEDBuild > XEED.lngBuild) {
            JOptionPane.showMessageDialog(null, "The template '" + szName
                    + "' was made with a newer version of XEED\nPlease use the appropriate version i.e. build " + XEEDBuild
                    + ".", "Incorrect version of XEED", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String szSections[] = XEED.GetElements(data, Constants.SECTION, false);
        for (int x = 0; x < szSections.length; x++) {
            Section s = new Section();
            if (!s.ParseSection(szSections[x])) {
                return false;
            }
        }

        key group_key = new key();
        group_key.key = Constants.CHARACTER_GROUPS;
        group_key.szName = "Groups";
        group_key.isSzData = true;
        keys.add(group_key);

        locked = true;
        return true;

    }

    public boolean LoadTemplateFromFile(String szPath) {

        String data = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(szPath), "UTF-8"));
            while (br.ready()) {
                char cbuf[] = new char[1024];
                br.read(cbuf);
                data += String.copyValueOf(cbuf);
            }
            br.close();

        } catch (Exception e) {
            return false;
        }

        return ParseTemplate(data);

    }

    public String CompileTemplate() {

        String data = "";
        data += XEED.CreateElement(szName, Constants.TEMPLATE_NAME, true);
        data += XEED.CreateElement(szDescription, Constants.TEMPLATE_DESCRIPTION, true);
        data += XEED.CreateElement(szTemplateID, Constants.TEMPLATE_ID, true);
        data += XEED.CreateElement(szAuthor, Constants.TEMPLATE_AUTHOR, true);
        data += XEED.CreateElement(szDate, Constants.TEMPLATE_DATE, false);
        data += XEED.CreateElement(szGame, Constants.TEMPLATE_GAME, true);
        data += XEED.CreateElement(String.valueOf(Version), Constants.TEMPLATE_VERSION, false);
        data += XEED.CreateElement(String.valueOf(XEED.lngBuild), Constants.TEMPLATE_XEED_BUILD, false);
        data = XEED.CreateElement(data, Constants.TEMPLATE_HEADER, false);

        for (int x = 0; x < sections.size(); x++) {
            data += XEED.CreateElement(sections.get(x).GetStringRepresentation(), Constants.SECTION, false);
        }

        data = XEED.CreateElement(data, Constants.TEMPLATE, false);

        return data;

    }

    private boolean WriteTemplateToFile(String szPath) {

        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(szPath), "UTF-8");
            out.write(CompileTemplate());
            out.close();

        } catch (Exception e) {
            return false;
        }

        this.szPath = szPath;
        return true;

    }

    public CharacterPanel RenderTemplate(Character c) {

        topPanel = new CharacterPanel();
        topPanel.removeAll();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        for (int x = 0; x < sections.size(); x++) {
            tabbedPane.addTab(sections.get(x).szName, sections.get(x).generate(c));
        }

        if (c != null) {
            c.hwndGroups = new GroupsPanel(c);
            c.hwndRelations = new RelationsPanel(c);
            tabbedPane.addTab("Groups", c.hwndGroups);
            tabbedPane.addTab("Relations", c.hwndRelations);
        } else {
            JPanel pnl = new JPanel();
            pnl.add(new JLabel("Place holder"));
            tabbedPane.addTab("Groups", pnl);
            pnl = new JPanel();
            pnl.add(new JLabel("Place holder"));
            tabbedPane.addTab("Relations", pnl);
        }

        topPanel.add(tabbedPane);
        CharacterPanel tmpPanel = topPanel;
        topPanel = null; //prevents leaks.
        return tmpPanel;

    }

    private String GetUniqueID() {
        IDSeed++;
        return String.valueOf(IDSeed);
    }

    public void LoadColumns(String szData) {

        if (szData == null) {
            return;
        }

        String templateID = XEED.GetElement(szData, Constants.TEMPLATE_ID, true);
        if (!templateID.equalsIgnoreCase(szTemplateID)) {
            return;
        }

        String szColumns[] = XEED.GetElements(szData, Constants.TEMPLATE_COLUMN, false);
        for (int x = 0; x < szColumns.length; x++) {

            String key = XEED.GetElement(szColumns[x], Constants.TEMPLATE_COLUMN_KEY, false);
            boolean visible = Boolean
                    .parseBoolean(XEED.GetElement(szColumns[x], Constants.TEMPLATE_COLUMN_VISIBLE, false));
            SetColumnVisible(key, visible);

        }

        if (XEED.hwndMain.currentTemplate == this) {
            XEED.hwndMain.PurgeThenPrintCharacters();
        }
    }

    public String GetColumnConfig() {

        if (keys.isEmpty()) {
            return "";
        }

        String comp = "";
        comp += XEED.CreateElement(szTemplateID, Constants.TEMPLATE_ID, true);
        for (int x = 0; x < keys.size(); x++) {
            String column = XEED.CreateElement(keys.get(x).key, Constants.TEMPLATE_COLUMN_KEY, true);
            column += XEED.CreateElement(Boolean.toString(keys.get(x).visible), Constants.TEMPLATE_COLUMN_VISIBLE, false);
            comp += XEED.CreateElement(column, Constants.TEMPLATE_COLUMN, false);
        }
        return comp;
    }

    // <editor-fold defaultstate="collapsed" desc="Item adding code">
    public int AddSection(String szName) {

        Section s = new Section();
        s.szName = szName;
        s.init();
        sections.add(s);
        return sections.size() - 1; //indexet i arrayn.

    }

    public void AddTextRow(String szName, int sectionID) {

        TextRow t = new TextRow();
        t.szName = szName;
        t.init();
        sections.get(sectionID).items.add(t);

    }

    public void AddIntegerRow(String szName, int sectionID) {

        IntegerRow y = new IntegerRow();
        y.szName = szName;
        y.init();
        sections.get(sectionID).items.add(y);

    }

    public void AddIntegerADBCRow(String szName, int sectionID) {

        IntegerRowADBC y = new IntegerRowADBC();
        y.szName = szName;
        y.init();
        sections.get(sectionID).items.add(y);

    }

    public void AddTextBox(String szName, int sectionID) {

        TextBox t = new TextBox();
        t.szName = szName;
        t.init();
        sections.get(sectionID).items.add(t);

    }

    public void AddListRow(String szName, String szData[], int sectionID) {

        ListRow l = new ListRow();
        l.szName = szName;
        l.szData = szData;
        l.init();
        sections.get(sectionID).items.add(l);

    }

    public void AddNameRow(String szName, int sectionID) {

        NameRow n = new NameRow();
        n.szName = szName;
        n.init();
        sections.get(sectionID).items.add(n);

    }

    public void AddCheckboxRow(String szName, int sectionID) {

        CheckBoxRow c = new CheckBoxRow();
        c.szName = szName;
        c.init();
        sections.get(sectionID).items.add(c);

    }

    public void AddTextBoxNoTitle(String szName, int sectionID) {

        TextBoxNoTitle c = new TextBoxNoTitle();
        c.szName = szName;
        c.init();
        sections.get(sectionID).items.add(c);

    }

    public void AddTitle(String szName, int sectionID) {

        TitleRow c = new TitleRow();
        c.szName = szName;
        c.init();
        sections.get(sectionID).items.add(c);
    }

    public void AddImageBox(String szName, int sectionID) {

        ImageBox ib = new ImageBox();
        ib.szName = szName;
        ib.init();
        sections.get(sectionID).items.add(ib);

    }

    public void AddParentRow(String szName, int sectionID) {

        ParentRow pb = new ParentRow();
        pb.szName = szName;
        pb.init();
        sections.get(sectionID).items.add(pb);

    }

    public void AddOffspringBox(String szName, int sectionID) {

        OffspringBox ib = new OffspringBox();
        ib.szName = szName;
        ib.init();
        sections.get(sectionID).items.add(ib);
    }

    public void AddExtendedRow(String szName, String szData[], int sectionID) {

        ExtendedSheetRow l = new ExtendedSheetRow();
        l.szName = szName;
        l.szData = szData;
        l.init();
        sections.get(sectionID).items.add(l);
    }

    public void AddListBox(String szName, String szData[], int sectionID) {

        ListBox l = new ListBox();
        l.szName = szName;
        l.szData = szData;
        l.init();
        sections.get(sectionID).items.add(l);

    }

    // </editor-fold>
    @Override
    public String toString() {
        return szName;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Classes">
    private class Section {

        String szName;
        String szIdentifier;
        ArrayList<Item> items = new ArrayList(0);

        public void init() {
            szIdentifier = szName + GetUniqueID();
        }

        public JPanel generate(Character c) {

            SectionPanel sectionpanel = new SectionPanel();

            JPanel leftPanel = new JPanel(); //Left side, main for generic compontents.
            JPanel rightPanel = new JPanel(); //Right side, for complex components, such as images, offspring.
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

            for (int x = 0; x < items.size(); x++) {

                if (items.get(x).left) {
                    leftPanel.add(items.get(x).generate(c));
                    if (x != (items.size() - 1)) {
                        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    }
                } else {
                    rightPanel.add(items.get(x).generate(c));
                    if (x != (items.size() - 1)) {
                        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    }
                }
            }

            GroupLayout layout = new GroupLayout(sectionpanel.pnlActionArea);
            sectionpanel.pnlActionArea.setLayout(layout);

            layout.setAutoCreateContainerGaps(false);
            layout.setAutoCreateGaps(false);
            layout.setHorizontalGroup(layout
                    .createSequentialGroup()
                    .addComponent(leftPanel)
                    .addComponent(rightPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE));
            layout.setVerticalGroup(layout.createSequentialGroup().addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(leftPanel)
                            .addComponent(rightPanel)));

            sectionpanel.pnlActionArea.add(leftPanel);
            if (rightPanel.getComponentCount() > 0) {
                sectionpanel.pnlActionArea.add(rightPanel);
            }
            sectionpanel.validate();
            return sectionpanel;
        }

        public String GetStringRepresentation() {

            String data = "";
            data += XEED.CreateElement(szName, Constants.SECTION_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.SECTION_ID, true);
            data = XEED.CreateElement(data, Constants.SECTION_HEADER, false);

            for (int x = 0; x < items.size(); x++) {
                data += XEED.CreateElement(items.get(x).GetStringRepresentation(), Constants.SECTION_ITEM, false);
            }

            return data;
        }

        public boolean ParseSection(String data) {

            szName = XEED.GetElement(data, Constants.SECTION_NAME, true);
            if (szName.isEmpty()) {
                return false;
            }

            szIdentifier = XEED.GetElement(data, Constants.SECTION_ID, true);
            if (szIdentifier.isEmpty()) {
                return false;
            }

            String szItems[] = XEED.GetElements(data, Constants.SECTION_ITEM, false);
            for (int x = 0; x < szItems.length; x++) {
                if (!ParseItem(szItems[x])) {
                    return false;
                }
            }

            sections.add(this);
            return true;

        }

        public boolean ParseItem(String data) {

            int intType;
            try {
                intType = Integer.parseInt(XEED.GetElement(data, Constants.ITEM_TYPE, false));
            } catch (Exception e) {
                return false;
            }

            key c = new key();

            switch (intType) {

                case 1:
                    TextRow tr = new TextRow();
                    c.szName = tr.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = tr.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    items.add(tr);
                    break;

                case 2:
                    TextBox tb = new TextBox();
                    c.szName = tb.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = tb.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    items.add(tb);
                    break;

                case 3:
                    ListRow lr = new ListRow();
                    c.szName = lr.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = lr.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    lr.szData = XEED.GetElements(data, Constants.ITEM_DATA, true);
                    items.add(lr);
                    break;

                case 4:
                    IntegerRow ir = new IntegerRow();
                    c.szName = ir.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = ir.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    items.add(ir);
                    break;

                case 5:
                    IntegerRowADBC irbc = new IntegerRowADBC();
                    c.szName = irbc.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = irbc.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    items.add(irbc);
                    break;

                case 6:
                    NameRow nr = new NameRow();
                    c.szName = nr.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = nr.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    items.add(nr);
                    c.visible = true;
                    break;

                case 7:
                    CheckBoxRow chbx = new CheckBoxRow();
                    c.szName = chbx.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = chbx.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    items.add(chbx);
                    break;

                case 8:
                    TextBoxNoTitle txtbx = new TextBoxNoTitle();
                    c.szName = txtbx.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = txtbx.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    items.add(txtbx);
                    break;

                case 9:
                    TitleRow title = new TitleRow();
                    title.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    items.add(title);
                    break;

                case 10:
                    ImageBox img = new ImageBox();
                    c.szName = img.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = img.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isImgData = true;
                    img.left = false;
                    items.add(img);
                    break;

                case 11:
                    ParentRow pr = new ParentRow();
                    c.szName = pr.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = pr.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isChrData = true;
                    c.isSzData = true;
                    items.add(pr);
                    break;

                case 12:
                    OffspringBox ob = new OffspringBox();
                    ob.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    ob.left = false;
                    items.add(ob);
                    break;

                case 13:
                    ExtendedSheetRow esr = new ExtendedSheetRow();
                    c.szName = esr.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = esr.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isExtData = true;
                    esr.szData = XEED.GetElements(data, Constants.ITEM_DATA, true);
                    items.add(esr);
                    break;

                case 14:
                    ListBox lb = new ListBox();
                    c.szName = lb.szName = XEED.GetElement(data, Constants.ITEM_NAME, true);
                    c.key = lb.szIdentifier = XEED.GetElement(data, Constants.ITEM_ID, true);
                    c.isSzData = true;
                    lb.szData = XEED.GetElements(data, Constants.ITEM_DATA, true);
                    items.add(lb);
                    break;

            }
            if (!c.key.isEmpty()) {
                keys.add(c);
            }
            return true;

        }
    }

    private class Item {

        String szName;
        String szIdentifier;
        boolean left = true;

        public boolean init() { //only called when creating a template, not loading from file!
            return false;
        }

        //Skrivs Ã¶ver av subklasser om inte, sÃ¥ failar skiten.
        public JPanel generate(Character c) {
            return null;
        }

        public String GetStringRepresentation() {
            return "";
        }
    }

    private class TextRow extends Item {

        final int Type = 1;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {
            TextRowPanel panel = new TextRowPanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class TextBox extends Item {

        final int Type = 2;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            TextBoxPanel panel = new TextBoxPanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;
        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class ListRow extends Item {

        final int Type = 3;
        String[] szData;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            ListRowPanel panel = new ListRowPanel(c, szIdentifier, szName, szData);
            topPanel.items.add(panel);
            return panel;
        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);

            for (int x = 0; x < szData.length; x++) {
                data += XEED.CreateElement(szData[x], Constants.ITEM_DATA, true);
            }

            return data;
        }
    }

    private class IntegerRow extends Item {

        final int Type = 4;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            NumberRowPanel panel = new NumberRowPanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class IntegerRowADBC extends Item {

        final int Type = 5;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            IntegerRowBCPanel panel = new IntegerRowBCPanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class NameRow extends Item {

        final int Type = 6;

        @Override
        public boolean init() {

            szIdentifier = "NAME"; //special
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.visible = true;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            NameRowPanel panel = new NameRowPanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class CheckBoxRow extends Item {

        final int Type = 7;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            CheckboxPanel panel = new CheckboxPanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class TextBoxNoTitle extends Item {

        final int Type = 8;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            TextBoxNoTitlePanel panel = new TextBoxNoTitlePanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class TitleRow extends Item {

        final int Type = 9;

        @Override
        public boolean init() {
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            TitlePanel panel = new TitlePanel(szName);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class ImageBox extends Item {

        final int Type = 10;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            left = false;
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isImgData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            ImagePanel panel = new ImagePanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class ParentRow extends Item {

        final int Type = 11;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isChrData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            ParentRowPanel panel = new ParentRowPanel(c, szIdentifier, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class OffspringBox extends Item {

        final int Type = 12;

        @Override
        public boolean init() {

            left = false;
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            OffspringPanel panel = new OffspringPanel(c, szName);
            topPanel.items.add(panel);
            return panel;

        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);
            return data;
        }
    }

    private class ExtendedSheetRow extends Item {

        final int Type = 13;
        public String[] szData;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isExtData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {
            ExtendedSheetButtonPanel panel = new ExtendedSheetButtonPanel(c, szIdentifier, szData, szName);
            return panel;
        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);

            for (int x = 0; x < szData.length; x++) {
                data += XEED.CreateElement(szData[x], Constants.ITEM_DATA, true);
            }

            return data;
        }
    }

    private class ListBox extends Item {

        final int Type = 14;
        String[] szData;

        @Override
        public boolean init() {

            szIdentifier = szName + GetUniqueID();
            key c = new key();
            c.szName = szName;
            c.key = szIdentifier;
            c.isSzData = true;
            keys.add(c);
            return true;

        }

        @Override
        public JPanel generate(Character c) {

            ListBoxPanel panel = new ListBoxPanel(c, szIdentifier, szName, szData);
            topPanel.items.add(panel);
            return panel;
        }

        @Override
        public String GetStringRepresentation() {
            String data = "";
            data += XEED.CreateElement(szName, Constants.ITEM_NAME, true);
            data += XEED.CreateElement(szIdentifier, Constants.ITEM_ID, true);
            data += XEED.CreateElement(String.valueOf(Type), Constants.ITEM_TYPE, false);

            for (int x = 0; x < szData.length; x++) {
                data += XEED.CreateElement(szData[x], Constants.ITEM_DATA, true);
            }

            return data;
        }
    }

    /*
     * class column {
     *
     * String szName; String key; boolean visible; }
     */
    class key {

        String szName = "";
        String key = "";
        boolean visible = false;
        boolean isChrData = false;
        boolean isSzData = false;
        boolean isExtData = false;
        boolean isImgData = false;
    }
}
