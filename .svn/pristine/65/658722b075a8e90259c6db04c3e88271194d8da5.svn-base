package xeed;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class frmMain extends javax.swing.JFrame implements ActionListener, ItemListener {

    /*
     * Måste ha det så här, få inte läcka i constructor här...
     */
    public clsTemplate currentTemplate = null; //det template vars karaktärer visas nu.
    public Vector jTableModel = new Vector(0);                                  // Model för tabellen
    public Vector jTableHeader = new Vector(0);                                 // Headern för tabellen
    public DefaultTableModel df = null;                                         // Länkar de ovanstående
    private ArrayList<mnuTemplateItem> mnuTemplates = new ArrayList(0);
    private ButtonGroup btngroupFilters = new ButtonGroup();
    private pnlSearch hwndSearchField = null;

    public frmMain() {
    }

    public void initMain() {

        initComponents();
        df = (DefaultTableModel) tblCharacters.getModel();
        tblCharacters.setDefaultRenderer(Object.class, new ImageCellRenderer());

        try {
            ArrayList<Image> images = new ArrayList(0);
            images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
            images.add(ImageIO.read(this.getClass().getResource("/icon_16.png")));
            this.setIconImages(images);
        } catch (IOException e) {
        }

        clsEngine.NewSetting(false);

        /*
         * Behandlar argument
         */
        if (clsEngine.szArguments.length > 0) {
            for (int x = 0; x < clsEngine.szArguments.length; x++) {
                if (clsEngine.szArguments[x].startsWith("/l:")) {
                    clsEngine.szPath = clsEngine.szArguments[x].substring("/l:".length());
                    File p = new File(clsEngine.szPath);
                    clsEngine.szDefaultDirectory = p.getParent();
                    clsEngine.LoadSetting(false, false);
                } else if (clsEngine.szArguments[x].startsWith("/del_ud:")) {
                    new File(clsEngine.szArguments[x].substring("/del_ud:".length())).delete();
                }
            }
        }

        setVisible(true);
    }

    public int GetCharacterRow() {

        if (tblCharacters.getRowCount() < 1) {
            return -1;
        }

        for (int x = 0; x < tblCharacters.getColumnCount(); x++) {
            if (tblCharacters.getValueAt(0, x).getClass() == clsCharacter.class) {
                return x;
            }
        }

        return -1;

    }

    public clsCharacter GetSelectedCharacter() {

        if (tblCharacters.getSelectedRowCount() == 0) {
            return null;
        }

        int CharacterRow = GetCharacterRow();
        if (CharacterRow == -1) {
            return null;
        }

        int intArray[] = tblCharacters.getSelectedRows();
        return (clsCharacter) tblCharacters.getValueAt(intArray[0], CharacterRow);

    }

    public clsCharacter[] GetSelectedCharacters() {

        if (tblCharacters.getSelectedRowCount() == 0) {
            return null;
        }

        int CharacterRow = GetCharacterRow();
        if (CharacterRow == -1) {
            return null;
        }

        int intArray[] = tblCharacters.getSelectedRows();
        clsCharacter[] cs = new clsCharacter[intArray.length];

        for (int y = 0; y < intArray.length; y++) {
            cs[y] = (clsCharacter) tblCharacters.getValueAt(intArray[y], CharacterRow);
        }

        return cs;

    }

    public void UpdateListItem(clsCharacter chr) {

        for (int x = 0; x < jTableModel.size(); x++) {
            Vector o = (Vector) jTableModel.get(x);
            clsCharacter c = null;
            for (int y = 0; y < o.size(); y++) {
                if (o.get(y).getClass() == clsCharacter.class) {
                    c = (clsCharacter) o.get(y);
                    break;
                }
            }
            if (c == null) {
                return;
            }

            if (c.characterID == chr.characterID) {
                if (AllowedByFilters(chr) && clsEngine.charDB.contains(chr)) {
                    Vector newo = CreateTableVector(chr);
                    jTableModel.setElementAt(newo, x);
                    df.fireTableDataChanged();
                    return;
                } else {
                    jTableModel.remove(x);
                    df.fireTableDataChanged();
                    return;
                }
            }
        }

        if (AllowedByFilters(chr) && clsEngine.charDB.contains(chr)) {
            AddListItem(chr);
        }
    }

    public void AddListItem(clsCharacter chr) {

        if (!AllowedByFilters(chr)) {
            return;
        }

        Vector o = CreateTableVector(chr);

        jTableModel.add(o);
        df.fireTableDataChanged();
    }

    public Vector CreateTableVector(clsCharacter chr) {

        Vector o = new Vector(0);
        clsTemplate t = chr.template;
        String keys[] = t.GetAllTemplateKeys();
        for (int x = 0; x < keys.length; x++) {
            if (t.IsToShowColumn(keys[x])) {

                if (keys[x].equalsIgnoreCase(clsConstants.CHARACTER_NAME)) {
                    o.add(chr);
                } else {
                    if (chr.chrData.containsKey(keys[x])) {
                        o.add(((clsCharacter) chr.chrData.get(keys[x])).GetCharacterName());
                    } else if (chr.szData.containsKey(keys[x])) {
                        o.add(chr.szData.get(keys[x]));
                    } else if (chr.extData.containsKey(keys[x])) {
                        o.add(chr.extData.get(keys[x]));
                    } else if (chr.imgData.containsKey(keys[x])) {
                        o.add(chr.imgData.get(keys[x]));
                    } else {
                        o.add("");
                    }
                }

            }
        }

        return o;
    }

    public boolean AllowedByFilters(clsCharacter chr) {

        if (currentTemplate == null) {
            return false;
        }

        if (!chr.templateIdentifier.equalsIgnoreCase(currentTemplate.GetTemplateID())) {
            return false;
        }

        return true;
    }

    public void PurgeThenPrintCharacters() {

        jTableHeader = new Vector(0);

        if (currentTemplate != null) {
            String keys[] = currentTemplate.GetAllTemplateKeys();
            String names[] = currentTemplate.GetAllTemplateNames();

            for (int x = 0; x < keys.length; x++) {
                if (currentTemplate.IsToShowColumn(keys[x])) {
                    jTableHeader.add(names[x]);
                }
            }
        }

        jTableModel = new Vector(0, jTableHeader.size());
        df.setDataVector(jTableModel, jTableHeader);
        df.fireTableStructureChanged();

        for (int i = 0; i < clsEngine.charDB.size(); i++) {
            AddListItem(clsEngine.charDB.get(i));
        }
    }

    public void DeleteSelectedCharacters() {

        clsCharacter[] cs = GetSelectedCharacters();

        for (int x = 0; x < cs.length; x++) {
            clsEngine.DeleteCharacter(cs[x]);
        }

        df.fireTableDataChanged();
        tblCharacters.clearSelection();
    }

    public void HandlePopUpRequest(MouseEvent evt) {

        if (!evt.isPopupTrigger()) {
            return;
        }

        if (tblCharacters.getSelectedRow() == -1) {
            popMnuRemoveCharacter.setEnabled(false);
        } else {
            popMnuRemoveCharacter.setEnabled(true);
        }

        popMnuMain.show(evt.getComponent(), evt.getX(), evt.getY());
    }

    public void AddCharacterMenuItem(clsTemplate t, boolean selected) {

        mnuTemplateItem mi = new mnuTemplateItem();
        JMenuItem menuItem = new JMenuItem(t.GetName(), new ImageIcon(getClass().getResource("/user_add.png")));
        JMenuItem menuItemPop = new JMenuItem(t.GetName(), new ImageIcon(getClass().getResource("/user_add.png")));
        JRadioButtonMenuItem menuFilter = new JRadioButtonMenuItem(t.GetName(), selected);

        menuItem.addActionListener(this);
        menuItem.setName(t.GetTemplateID());
        menuItemPop.addActionListener(this);
        menuItemPop.setName(t.GetTemplateID());
        menuFilter.addActionListener(this);
        menuFilter.setName(t.GetTemplateID());
        btngroupFilters.add(menuFilter);

        mi.t = t;
        mi.mnuAdd = menuItem;
        mi.mnuFilter = menuFilter;
        mi.mnuAddPop = menuItemPop;

        mnuTemplates.add(mi);
        mnuCharacters.add(menuItem);
        popMnuMain.add(menuItemPop);
        mnuFilter.add(mi.mnuFilter);

    }

    //remember to call AFTER the new currentTemplate is set!
    public void RemoveCharacterMenuItem(clsTemplate t) {

        mnuTemplateItem mi = null;
        for (int x = 0; x < mnuTemplates.size(); x++) {
            if (mnuTemplates.get(x).t.GetTemplateID().equalsIgnoreCase(t.GetTemplateID())) {
                mi = mnuTemplates.get(x);
                break;
            }
        }

        if (mi == null) {
            return;
        }

        popMnuMain.remove(mi.mnuAddPop);
        mnuCharacters.remove(mi.mnuAdd);
        btngroupFilters.remove(mi.mnuFilter);
        mnuFilter.remove(mi.mnuFilter);
        mnuTemplates.remove(mi);

        if (currentTemplate != null) {
            for (int x = 0; x < mnuTemplates.size(); x++) {
                if (mnuTemplates.get(x).t.GetTemplateID().equalsIgnoreCase(currentTemplate.GetTemplateID())) {
                    mnuTemplates.get(x).mnuFilter.setSelected(true);
                    break;
                }
            }
        }

    }

    public void RemoveSearchField() {
        if (hwndSearchField != null) {
            getContentPane().remove(hwndSearchField);
            hwndSearchField = null;
            getContentPane().validate();
        }
    }

    public void SetSelectedCharacters(ArrayList<clsCharacter> cs) {

        int CharacterRow = GetCharacterRow();
        if (CharacterRow == -1) {
            return;
        }

        tblCharacters.getSelectionModel().clearSelection();

        if (cs == null) {       //check after clear, since null means to clear selection.
            return;
        }

        for (int x = 0; x < tblCharacters.getRowCount(); x++) {
            clsCharacter c = (clsCharacter) tblCharacters.getValueAt(x, CharacterRow);

            if (cs.contains(c)) {
                tblCharacters.getSelectionModel().addSelectionInterval(x, x);
            }
        }

    }

    public void SelectCharactersByStringSearch(String s, boolean all) {

        s = s.toLowerCase();

        ArrayList<clsCharacter> cs = new ArrayList(0);
        for (int x = 0; x < jTableModel.size(); x++) {

            Vector v = (Vector) jTableModel.get(x);
            clsCharacter c = (clsCharacter) v.get(0);
            if (c.GetCharacterName().toLowerCase().indexOf(s) > -1) {
                cs.add(c);
            } else {
                if (all) {
                    for (int y = 1; y < v.size(); y++) {
                        String s2 = (String) v.get(y);
                        if (s2.toLowerCase().indexOf(s) > -1) {
                            cs.add(c);
                            break;
                        }
                    }
                }
            }
        }

        SetSelectedCharacters(cs);

    }

    //Listeners for character items.
    @Override
    public void actionPerformed(ActionEvent ae) {

        for (int x = 0; x < mnuTemplates.size(); x++) {

            if (ae.getSource() == mnuTemplates.get(x).mnuAdd || ae.getSource() == mnuTemplates.get(x).mnuAddPop) {

                clsEngine.AddCharacter(mnuTemplates.get(x).t).DisplayCharacter();

            } else if (ae.getSource() == mnuTemplates.get(x).mnuFilter) {

                currentTemplate = mnuTemplates.get(x).t;
                PurgeThenPrintCharacters();

            }

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popMnuMain = new javax.swing.JPopupMenu();
        popMnuRemoveCharacter = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCharacters = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuNewSetting = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        mnuLoadSetting = new javax.swing.JMenuItem();
        mnuSaveSetting = new javax.swing.JMenuItem();
        mnuSaveAs = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mnuExportChars = new javax.swing.JMenuItem();
        mnuExportSetting = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        mnuOptions = new javax.swing.JMenuItem();
        mnuAbout = new javax.swing.JMenuItem();
        mnuSetting = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        mnuOverview = new javax.swing.JMenuItem();
        mnuNotes = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuLineageChart = new javax.swing.JMenuItem();
        mnuCharacters = new javax.swing.JMenu();
        mnuConverter = new javax.swing.JMenuItem();
        mnuDeleteCharacter = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();
        mnuTemplateCreator = new javax.swing.JMenuItem();
        mnuTemplateManager = new javax.swing.JMenuItem();
        mnuFilter = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();

        popMnuRemoveCharacter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_delete.png"))); // NOI18N
        popMnuRemoveCharacter.setText("Remove");
        popMnuRemoveCharacter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popMnuRemoveCharacterActionPerformed(evt);
            }
        });
        popMnuMain.add(popMnuRemoveCharacter);
        popMnuMain.add(jSeparator3);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("XEED");
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setComponentPopupMenu(popMnuMain);
        jScrollPane2.setDoubleBuffered(true);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(650, 350));
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jScrollPane2MousePressed(evt);
            }
        });

        tblCharacters.setAutoCreateRowSorter(true);
        tblCharacters.setModel(new DefaultTableModel(jTableModel, jTableHeader) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        }
    );
    tblCharacters.setDoubleBuffered(true);
    tblCharacters.setGridColor(new java.awt.Color(255, 255, 255));
    tblCharacters.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    tblCharacters.setShowHorizontalLines(false);
    tblCharacters.setShowVerticalLines(false);
    tblCharacters.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            tblCharactersMouseReleased(evt);
        }
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tblCharactersMouseClicked(evt);
        }
        public void mousePressed(java.awt.event.MouseEvent evt) {
            tblCharactersMousePressed(evt);
        }
    });
    tblCharacters.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            tblCharactersKeyPressed(evt);
        }
    });
    jScrollPane2.setViewportView(tblCharacters);

    getContentPane().add(jScrollPane2);

    jMenuBar1.setName("File"); // NOI18N

    mnuFile.setText("File");
    mnuFile.setMaximumSize(new java.awt.Dimension(33, 32767));

    mnuNewSetting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
    mnuNewSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_add.png"))); // NOI18N
    mnuNewSetting.setText("New");
    mnuNewSetting.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuNewSettingActionPerformed(evt);
        }
    });
    mnuFile.add(mnuNewSetting);
    mnuFile.add(jSeparator6);

    mnuLoadSetting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
    mnuLoadSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_go.png"))); // NOI18N
    mnuLoadSetting.setText("Load");
    mnuLoadSetting.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuLoadSettingActionPerformed(evt);
        }
    });
    mnuFile.add(mnuLoadSetting);

    mnuSaveSetting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    mnuSaveSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_save.png"))); // NOI18N
    mnuSaveSetting.setText("Save");
    mnuSaveSetting.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuSaveSettingActionPerformed(evt);
        }
    });
    mnuFile.add(mnuSaveSetting);

    mnuSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    mnuSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_save.png"))); // NOI18N
    mnuSaveAs.setText("Save As");
    mnuSaveAs.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuSaveAsActionPerformed(evt);
        }
    });
    mnuFile.add(mnuSaveAs);
    mnuFile.add(jSeparator5);

    mnuExportChars.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_go.png"))); // NOI18N
    mnuExportChars.setText("Export Characters");
    mnuExportChars.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuExportCharsActionPerformed(evt);
        }
    });
    mnuFile.add(mnuExportChars);

    mnuExportSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page_go.png"))); // NOI18N
    mnuExportSetting.setText("Export Notes");
    mnuExportSetting.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuExportSettingActionPerformed(evt);
        }
    });
    mnuFile.add(mnuExportSetting);
    mnuFile.add(jSeparator8);

    mnuOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wrench.png"))); // NOI18N
    mnuOptions.setText("Options");
    mnuOptions.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuOptionsActionPerformed(evt);
        }
    });
    mnuFile.add(mnuOptions);

    mnuAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/information.png"))); // NOI18N
    mnuAbout.setText("About");
    mnuAbout.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuAboutActionPerformed(evt);
        }
    });
    mnuFile.add(mnuAbout);

    jMenuBar1.add(mnuFile);

    mnuSetting.setText("Setting");

    jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/group.png"))); // NOI18N
    jMenuItem3.setText("Groups");
    jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem3ActionPerformed(evt);
        }
    });
    mnuSetting.add(jMenuItem3);

    mnuOverview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/world.png"))); // NOI18N
    mnuOverview.setText("Overview");
    mnuOverview.setToolTipText("");
    mnuOverview.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuOverviewActionPerformed(evt);
        }
    });
    mnuSetting.add(mnuOverview);

    mnuNotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/page_edit.png"))); // NOI18N
    mnuNotes.setText("Notes");
    mnuNotes.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuNotesActionPerformed(evt);
        }
    });
    mnuSetting.add(mnuNotes);

    jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/group_link.png"))); // NOI18N
    jMenuItem2.setText("Relations");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem2ActionPerformed(evt);
        }
    });
    mnuSetting.add(jMenuItem2);
    mnuSetting.add(jSeparator1);

    mnuLineageChart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chart_organisation.png"))); // NOI18N
    mnuLineageChart.setText("Graphs");
    mnuLineageChart.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuLineageChartActionPerformed(evt);
        }
    });
    mnuSetting.add(mnuLineageChart);

    jMenuBar1.add(mnuSetting);

    mnuCharacters.setText("Characters");

    mnuConverter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_edit.png"))); // NOI18N
    mnuConverter.setText("Converter");
    mnuConverter.setToolTipText("");
    mnuConverter.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuConverterActionPerformed(evt);
        }
    });
    mnuCharacters.add(mnuConverter);

    mnuDeleteCharacter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
    mnuDeleteCharacter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_delete.png"))); // NOI18N
    mnuDeleteCharacter.setText("Delete");
    mnuDeleteCharacter.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuDeleteCharacterActionPerformed(evt);
        }
    });
    mnuCharacters.add(mnuDeleteCharacter);
    mnuCharacters.add(jSeparator2);

    jMenuBar1.add(mnuCharacters);

    jMenu2.setText("Templates");

    mnuTemplateCreator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/layout_add.png"))); // NOI18N
    mnuTemplateCreator.setText("Creator");
    mnuTemplateCreator.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuTemplateCreatorActionPerformed(evt);
        }
    });
    jMenu2.add(mnuTemplateCreator);

    mnuTemplateManager.setIcon(new javax.swing.ImageIcon(getClass().getResource("/layout_edit.png"))); // NOI18N
    mnuTemplateManager.setText("Manager");
    mnuTemplateManager.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mnuTemplateManagerActionPerformed(evt);
        }
    });
    jMenu2.add(mnuTemplateManager);

    jMenuBar1.add(jMenu2);

    mnuFilter.setText("Filter");

    jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/table_gear.png"))); // NOI18N
    jMenuItem1.setText("Customise Columns");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem1ActionPerformed(evt);
        }
    });
    mnuFilter.add(jMenuItem1);
    mnuFilter.add(jSeparator7);

    jMenuBar1.add(mnuFilter);

    setJMenuBar(jMenuBar1);

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        if (!clsEngine.IsSettingEmpty()) {
            int intRet = JOptionPane.showOptionDialog(null, "Do you want to save the current setting?", "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (intRet == 0) {
                if (!clsEngine.SaveSetting(false, true)) {
                    return;
                }
            } else if (intRet == -1) {
                return;
            }
        }

        clsEngine.ReadWriteOptions(true);

        if (clsEngine.hwndFileLock != null) {
            clsEngine.hwndFileLock.ReleaseLock();
        }

        System.exit(0);

    }//GEN-LAST:event_formWindowClosing

    private void tblCharactersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCharactersMouseClicked

        if (evt.getButton() != 1) {
            return;
        }

        if (evt.getClickCount() != 2) {
            return;
        }

        if (tblCharacters.getSelectedRow() == -1) {
            return;
        }

        if (tblCharacters.getSelectedRowCount() > 1) {
            int intRet = JOptionPane.showOptionDialog(null, "Are you sure you want to open " + tblCharacters.getSelectedRowCount() + " characters?", "Open multiple characters?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (intRet != 0) {
                return;
            }
        }


        clsCharacter[] cs = GetSelectedCharacters();
        for (int x = 0; x < cs.length; x++) {
            if (cs[x] != null) {
                cs[x].DisplayCharacter();
            }
        }
    }//GEN-LAST:event_tblCharactersMouseClicked

    private void tblCharactersKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCharactersKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            clsCharacter[] cs = GetSelectedCharacters();
            if (cs == null) {
                return;
            }

            if (cs.length > 1) {
                int intRet = JOptionPane.showOptionDialog(null, "Are you sure you want to open the selected " + cs.length + " characters?", "Open multiple characters?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (intRet != 0) {
                    return;
                }
            } else if (cs.length == 0) {
                return;
            }

            for (int x = 0; x < cs.length; x++) {
                if (cs[x] != null) {
                    cs[x].DisplayCharacter();
                }
            }


        } else if (evt.getKeyCode() == KeyEvent.VK_F && evt.isControlDown()) {

            if (hwndSearchField == null) {
                hwndSearchField = new pnlSearch(evt.isShiftDown());
                getContentPane().add(hwndSearchField, 0);
            }
            getContentPane().validate();
            hwndSearchField.GetFocus();

        } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {

            RemoveSearchField();

        }

    }//GEN-LAST:event_tblCharactersKeyPressed

    private void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAboutActionPerformed
        if (clsEngine.hwndAbout == null) {
            clsEngine.hwndAbout = new frmAbout();
        }
        clsEngine.hwndAbout.setVisible(true);
}//GEN-LAST:event_mnuAboutActionPerformed

    private void mnuOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOptionsActionPerformed

        if (clsEngine.hwndOption == null) {
            clsEngine.hwndOption = new frmOptions();
        }
        clsEngine.hwndOption.setVisible(true);
    }//GEN-LAST:event_mnuOptionsActionPerformed

    private void mnuLineageChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLineageChartActionPerformed
        if (clsEngine.hwndGraph == null) {
            clsEngine.hwndGraph = new frmGraph();
        }
        clsEngine.hwndGraph.setVisible(true);
}//GEN-LAST:event_mnuLineageChartActionPerformed

    private void mnuNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNotesActionPerformed
        if (clsEngine.hwndNotes == null) {
            clsEngine.hwndNotes = new frmNotes();
        }
        clsEngine.hwndNotes.setVisible(true);
}//GEN-LAST:event_mnuNotesActionPerformed

    private void mnuExportCharsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportCharsActionPerformed

        clsCharacter[] cs = GetSelectedCharacters();
        if (cs == null) {
            JOptionPane.showMessageDialog(null, "Please select one or more characters before launching the exporter", "No characters selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cs.length == 0) {
            JOptionPane.showMessageDialog(null, "Please select one or more characters before launching the exporter", "No characters selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentTemplate == null) {
            JOptionPane.showMessageDialog(null, "Please select one or more characters before launching the exporter", "No characters selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (clsEngine.hwndCharacterExporter == null) {
            clsEngine.hwndCharacterExporter = new frmCharacterExporter(cs, currentTemplate);
        }
        clsEngine.hwndCharacterExporter.setVisible(true);

}//GEN-LAST:event_mnuExportCharsActionPerformed

    private void mnuSaveSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveSettingActionPerformed
        setEnabled(false);
        clsEngine.SaveSetting(false, false);
        setEnabled(true);
        requestFocus();
}//GEN-LAST:event_mnuSaveSettingActionPerformed
    private void mnuLoadSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLoadSettingActionPerformed
        setEnabled(false);
        clsEngine.LoadSetting(true, true);
        setEnabled(true);
        requestFocus();
}//GEN-LAST:event_mnuLoadSettingActionPerformed
    private void mnuNewSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewSettingActionPerformed
        clsEngine.NewSetting(true);
    }//GEN-LAST:event_mnuNewSettingActionPerformed
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (clsEngine.hwndColumns == null) {
            clsEngine.hwndColumns = new frmColumn();
        }
        clsEngine.hwndColumns.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuExportSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportSettingActionPerformed
        try {

            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select a were you want your setting notes folder.");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


            int intRet = fc.showSaveDialog(null);

            if (intRet != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "Notes wasn't exported.", "Operation cancelled", JOptionPane.WARNING_MESSAGE);
                return;
            }

            File d = null;
            if (!clsEngine.szSettingName.isEmpty()) {
                d = new File(fc.getSelectedFile().getAbsolutePath() + File.separator + clsEngine.szSettingName + " notes");
            } else {
                d = new File(fc.getSelectedFile().getAbsolutePath() + File.separator + "Setting Notes");
            }
            d.mkdirs();
            clsEngine.ExportNodesToFiles(clsEngine.rootNode.GetChildren(), 0, d.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error when exporting the setting notes!\nPlease make sure all note/folder names are valid for export\n" + e, "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mnuExportSettingActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (clsEngine.hwndGroup == null) {
            clsEngine.hwndGroup = new frmGroup();
        }
        clsEngine.hwndGroup.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (clsEngine.hwndRelations == null) {
            clsEngine.hwndRelations = new frmRelations();
        }
        clsEngine.hwndRelations.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    private void mnuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveAsActionPerformed
        setEnabled(false);
        clsEngine.SaveSetting(false, true);
        setEnabled(true);
        requestFocus();
    }//GEN-LAST:event_mnuSaveAsActionPerformed

    private void popMnuRemoveCharacterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popMnuRemoveCharacterActionPerformed
        mnuDeleteCharacterActionPerformed(evt);
    }//GEN-LAST:event_popMnuRemoveCharacterActionPerformed

    private void jScrollPane2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MousePressed
        HandlePopUpRequest(evt);
    }//GEN-LAST:event_jScrollPane2MousePressed

    private void tblCharactersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCharactersMouseReleased
        HandlePopUpRequest(evt);
    }//GEN-LAST:event_tblCharactersMouseReleased

    private void tblCharactersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCharactersMousePressed
        HandlePopUpRequest(evt);
    }//GEN-LAST:event_tblCharactersMousePressed

    private void jScrollPane2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseReleased
        HandlePopUpRequest(evt);
    }//GEN-LAST:event_jScrollPane2MouseReleased

    private void mnuTemplateCreatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTemplateCreatorActionPerformed
        if (clsEngine.hwndTemplateCreator == null) {
            clsEngine.hwndTemplateCreator = new frmTemplateCreator();
        }
        clsEngine.hwndTemplateCreator.setVisible(true);
    }//GEN-LAST:event_mnuTemplateCreatorActionPerformed

    private void mnuTemplateManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTemplateManagerActionPerformed
        if (clsEngine.hwndTemplateManager == null) {
            clsEngine.hwndTemplateManager = new frmTemplateManager();
        }
        clsEngine.hwndTemplateManager.setVisible(true);
    }//GEN-LAST:event_mnuTemplateManagerActionPerformed

    private void mnuDeleteCharacterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDeleteCharacterActionPerformed

        if (tblCharacters.getSelectedRowCount() == 0) {
            return;
        }

        if (tblCharacters.getSelectedRowCount() > 1) {
            int intRet = JOptionPane.showOptionDialog(null, "Are you sure you want to delete the selected " + tblCharacters.getSelectedRowCount() + " characters?", "Delete selected characters?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (intRet != 0) {
                return;
            }
        } else {
            int intRet = JOptionPane.showOptionDialog(null, "Are you sure you want to delete " + GetSelectedCharacter().GetCharacterName(), "Delete selected character?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (intRet != 0) {
                return;
            }
        }

        DeleteSelectedCharacters();

    }//GEN-LAST:event_mnuDeleteCharacterActionPerformed

    private void mnuConverterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConverterActionPerformed

        clsCharacter[] cs = GetSelectedCharacters();
        if (cs == null) {
            JOptionPane.showMessageDialog(null, "Please select one or more characters before launching the converter", "No characters selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cs.length == 0) {
            JOptionPane.showMessageDialog(null, "Please select one or more characters before launching the converter", "No characters selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentTemplate == null) {
            JOptionPane.showMessageDialog(null, "Please select one or more characters before launching the converter", "No characters selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (clsEngine.templateDB.size() < 2) {
            JOptionPane.showMessageDialog(null, "You need more than 1 template to use the character converter.", "Insufficient number of templates", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (clsEngine.hwndCharacterConverter == null) {
            clsEngine.hwndCharacterConverter = new frmCharacterConverter(cs);
        }
        clsEngine.hwndCharacterConverter.setVisible(true);
    }//GEN-LAST:event_mnuConverterActionPerformed

    private void mnuOverviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOverviewActionPerformed
        if (clsEngine.hwndSettingInfo == null) {
            clsEngine.hwndSettingInfo = new frmSettingInfo();
        }
        clsEngine.hwndSettingInfo.setVisible(true);
    }//GEN-LAST:event_mnuOverviewActionPerformed
    /*
     *
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JMenu mnuCharacters;
    private javax.swing.JMenuItem mnuConverter;
    private javax.swing.JMenuItem mnuDeleteCharacter;
    private javax.swing.JMenuItem mnuExportChars;
    private javax.swing.JMenuItem mnuExportSetting;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuFilter;
    private javax.swing.JMenuItem mnuLineageChart;
    private javax.swing.JMenuItem mnuLoadSetting;
    private javax.swing.JMenuItem mnuNewSetting;
    private javax.swing.JMenuItem mnuNotes;
    private javax.swing.JMenuItem mnuOptions;
    private javax.swing.JMenuItem mnuOverview;
    private javax.swing.JMenuItem mnuSaveAs;
    private javax.swing.JMenuItem mnuSaveSetting;
    private javax.swing.JMenu mnuSetting;
    private javax.swing.JMenuItem mnuTemplateCreator;
    private javax.swing.JMenuItem mnuTemplateManager;
    private javax.swing.JPopupMenu popMnuMain;
    private javax.swing.JMenuItem popMnuRemoveCharacter;
    public javax.swing.JTable tblCharacters;
    // End of variables declaration//GEN-END:variables

    @Override
    public void itemStateChanged(ItemEvent ie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    class mnuTemplateItem {

        clsTemplate t;
        JMenuItem mnuAdd;
        JMenuItem mnuAddPop;
        JRadioButtonMenuItem mnuFilter;
    }

    class ImageCellRenderer extends JLabel implements TableCellRenderer {
        //TODO Fixa multiline textbox.

        public ImageCellRenderer() {
            super();
            setOpaque(true);
            setVerticalAlignment(JLabel.TOP);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }

            if (value == null) {
                setText("");
                setIcon(null);
            } else {
                if (value.getClass() != ImageIcon.class) {
                    setText(value.toString().replace('\n', ' '));
                    setIcon(null);
                } else {
                    setText(null);
                    setIcon((ImageIcon) value);
                    if (getPreferredSize().height > table.getRowHeight(row)) {
                        table.setRowHeight(row, getPreferredSize().height);
                    }
                }
            }

            return this;
        }
    }
}
