package forms;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang3.StringEscapeUtils;
import xeed.Character;
import xeed.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class CharacterExporterForm extends javax.swing.JFrame {

   private Vector jTableModel = new Vector(0);
   private Vector jTableHeader = new Vector(0);
   private DefaultTableModel df;
   private xeed.Character[] chars;
   private Template template;
   private boolean consolidate = false;

   /**
    * Creates new form frmCharacterExporter
    */
   public CharacterExporterForm(Character[] cs, Template t) {

      initComponents();
      chars = cs;
      template = t;

      try {
         ArrayList<Image> images = new ArrayList(0);
         images.add(ImageIO.read(this.getClass().getResource("/icon.png")));
         images.add(ImageIO.read(this.getClass().getResource("/user_go.png")));
         this.setIconImages(images);
      } catch (IOException e) {
      }
      df = (DefaultTableModel) tblData.getModel();

      String szTitle = "Exporting(" + cs.length + "): ";
      for (int x = 0; x < cs.length; x++) {
         szTitle += cs[x].GetCharacterName();
         if (x != cs.length - 1) {
            szTitle += ", ";
         }
      }
      setTitle(szTitle);

      LoadSorterOptions();

      JustifyColumns();
      LoadFields(false);

   }

   public final void JustifyColumns() {

      tblData.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
      tblData.getColumnModel().getColumn(0).setResizable(false);
      tblData.getColumnModel().getColumn(0).setPreferredWidth(20);
      tblData.getColumnModel().getColumn(1).setPreferredWidth(tblData.getWidth() - 20);

   }

   private void LoadSorterOptions() {

      comboSort.removeAllItems();
      String[] keys = template.GetAllTemplateKeys();
      String[] names = template.GetAllTemplateNames();

      for (int x = 0; x < keys.length; x++) {

         combo_item prop = new combo_item();
         prop.key = keys[x];
         prop.name = names[x];
         boolean type[] = template.GetDataTypeArray(keys[x]);

         prop.isChrData = type[0];
         prop.isExtData = type[1];
         prop.isImgData = type[2];
         prop.isSzData = type[3];

         if ((prop.isChrData || prop.isSzData) && (!prop.isImgData && !prop.isExtData)) {
            comboSort.addItem(prop);
         }
      }

   }

   public final void LoadFields(boolean selected) {

      jTableModel.clear();

      String[] names = template.GetAllTemplateNames();
      String[] keys = template.GetAllTemplateKeys();

      for (int x = 0; x < keys.length; x++) {
         Vector o = new Vector(0);
         o.add(selected);

         table_item t = new table_item();
         t.name = names[x];
         t.key = keys[x];

         o.add(t);
         jTableModel.add(o);
      }
      df.fireTableDataChanged();
   }

   private void SortCharacters() {

      combo_item selected_sort_type = (combo_item) comboSort.getSelectedItem();

      sort_item[] sorts = new sort_item[chars.length];
      for (int x = 0; x < chars.length; x++) {

         sort_item s = new sort_item();
         s.c = chars[x];

         if (selected_sort_type.isSzData) {
            String o = (String) chars[x].szData.get(selected_sort_type.key);
            if (o == null) {
               s.sortString = "";
            } else {
               s.sortString = o;
            }
         } else if (selected_sort_type.isChrData) {
            Character c = (Character) chars[x].chrData.get(selected_sort_type.key);
            if (c == null) {
               s.sortString = "";
            } else {
               s.sortString = c.GetCharacterName();
            }
         }

         sorts[x] = s;
      }

      Arrays.sort(sorts);

      for (int x = 0; x < chars.length; x++) {
         if (!chkReverse.isSelected()) {
            chars[x] = sorts[x].c;
         } else {
            chars[x] = sorts[sorts.length - x - 1].c;
         }
      }

   }

   private boolean ExportCharactersToTextFile(String szPath) {

      SortCharacters();

      try {

         String szCharPath;
         szCharPath = szPath + File.separator + XEED.szSettingName + File.separator;
         new File(szCharPath).mkdirs();
         if (consolidate) {
            new File(szCharPath + template.GetName() + "_characters.txt").delete();
         }

         for (int x = 0; x < chars.length; x++) {

            PrintWriter pw = null;
            if (consolidate) {
               pw = new PrintWriter(new BufferedWriter(new FileWriter(szCharPath + template.GetName()
                     + "_characters.txt", true)));
            } else {
               szCharPath = szPath + File.separator + XEED.szSettingName + File.separator + chars[x].GetCharacterName()
                     + File.separator;
               new File(szCharPath).mkdirs();
               pw = new PrintWriter(new BufferedWriter(new FileWriter(
                     szCharPath + chars[x].GetCharacterName() + ".txt", false)));
            }

            for (int y = 0; y < tblData.getRowCount(); y++) {

               boolean selected = (Boolean) tblData.getValueAt(y, 0);
               table_item item = (table_item) tblData.getValueAt(y, 1);

               if (selected) {

                  if (chars[x].chrData.containsKey(item.key)) {
                     pw.println(item.name + ": " + chars[x].chrData.get(item.key));
                  } else if (chars[x].szData.containsKey(item.key)) {
                     String s = (String) chars[x].szData.get(item.key);
                     s = s.replace("\n", System.getProperty("line.separator"));
                     pw.println(item.name + ": " + s);
                  } else if (chars[x].extData.containsKey(item.key)) {
                     if (!ExportExtendedSheetToFile((ExtendedSheetData) chars[x].extData.get(item.key), szCharPath
                           + chars[x].GetCharacterName() + "_" + item.name + ".txt")) {
                        return false;
                     }
                  } else if (chars[x].imgData.containsKey(item.key)) {
                     ImageIcon img = (ImageIcon) chars[x].imgData.get(item.key);
                     if (!ExportImageToFile(img, szCharPath + chars[x].GetCharacterName() + "_" + item.name + ".png")) {
                        return false;
                     }
                  }
               }

            }
            pw.println();
            pw.println();
            pw.close();

         }
      } catch (Exception e) {
         return false;
      }

      return true;

   }

   private boolean ExportImageToFile(ImageIcon img, String szPath) {

      try {
         BufferedImage bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
         Graphics2D g2 = bi.createGraphics();
         g2.drawImage(img.getImage(), 0, 0, null);
         ImageIO.write(bi, "png", new File(szPath));
      } catch (Exception e) {
         return false;
      }
      return true;

   }

   private boolean ExportExtendedSheetToFile(ExtendedSheetData esd, String szPath) {

      try {
         PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(szPath, false)));
         for (int x = 0; x < esd.Properties.length; x++) {
            pw.println(esd.Properties[x] + ": " + esd.Values[x]);
         }
         pw.close();
      } catch (Exception e) {
         return false;
      }
      return true;

   }

   private boolean ExportCharactersToHTMLFile(String szPath) {

      SortCharacters();

      try {

         String szCharPath;
         szCharPath = szPath + File.separator + XEED.szSettingName + File.separator;
         new File(szCharPath).mkdirs();
         if (consolidate) {
            new File(szCharPath + template.GetName() + "_characters.htm").delete();
         }

         for (int x = 0; x < chars.length; x++) {

            PrintWriter pw = null;
            if (consolidate) {
               pw = new PrintWriter(new BufferedWriter(new FileWriter(szCharPath + template.GetName()
                     + "_characters.htm", true)));
               if (x == 0) {

                  pw.print(HTMLTemplate.HTML_TEMPLATE_HEADER
                        .replace(HTMLTemplate.HTML_TEMPLATE_TITLE_TAG,
                              StringEscapeUtils.escapeHtml4(XEED.szSettingName))
                        .replace(HTMLTemplate.HTML_TEMPLATE_TABLE_ID_LIST, GenerateIDList("tbl", chars.length))
                        .replace(HTMLTemplate.HTML_TEMPLATE_LINK_ID_LIST, GenerateIDList("lnk", chars.length)));

               }
            } else {
               szCharPath = szPath + File.separator + XEED.szSettingName + File.separator + chars[x].GetCharacterName()
                     + File.separator;
               new File(szCharPath).mkdirs();
               pw = new PrintWriter(new BufferedWriter(new FileWriter(
                     szCharPath + chars[x].GetCharacterName() + ".htm", false)));

               pw.print(HTMLTemplate.HTML_TEMPLATE_HEADER
                     .replace(HTMLTemplate.HTML_TEMPLATE_TITLE_TAG, StringEscapeUtils.escapeHtml4(XEED.szSettingName))
                     .replace(HTMLTemplate.HTML_TEMPLATE_TABLE_ID_LIST, GenerateIDList("tbl", chars.length))
                     .replace(HTMLTemplate.HTML_TEMPLATE_LINK_ID_LIST, GenerateIDList("lnk", chars.length)));

            }

            //Add table header with name item.
            pw.print(HTMLTemplate.HTML_TEMPLATE_TABLE_HEADER_ROW
                  .replace(HTMLTemplate.HTML_TEMPLATE_CHARACTER_NAME_TAG,
                        StringEscapeUtils.escapeHtml4(chars[x].GetCharacterName()))
                  .replace(HTMLTemplate.HTML_TEMPLATE_TABLE_ID, "tbl" + x)
                  .replace(HTMLTemplate.HTML_TEMPLATE_EXPAND_LINK_ID, "lnk" + x));

            for (int y = 0; y < tblData.getRowCount(); y++) {

               boolean selected = (Boolean) tblData.getValueAt(y, 0);
               table_item item = (table_item) tblData.getValueAt(y, 1);

               if (selected && !item.key.equals(Constants.CHARACTER_NAME)) {

                  if (chars[x].chrData.containsKey(item.key)) {

                     pw.print(HTMLTemplate.HTML_TEMPLATE_TABLE_ROW.replace(HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_NAME,
                           StringEscapeUtils.escapeHtml4(item.name)).replace(
                           HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_DATA,
                           StringEscapeUtils.escapeHtml4(chars[x].chrData.get(item.key).toString())));

                  } else if (chars[x].szData.containsKey(item.key)) {

                     pw.print(HTMLTemplate.HTML_TEMPLATE_TABLE_ROW.replace(HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_NAME,
                           StringEscapeUtils.escapeHtml4(item.name)).replace(
                           HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_DATA,
                           StringEscapeUtils.escapeHtml4(chars[x].szData.get(item.key).toString())
                                 .replace("\n", "<br>")));

                  } else if (chars[x].extData.containsKey(item.key)) {

                     pw.print(HTMLTemplate.HTML_TEMPLATE_TABLE_ROW.replace(HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_NAME,
                           StringEscapeUtils.escapeHtml4(item.name)).replace(
                           HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_DATA,
                           ExpandedSheetToHTMLTable((ExtendedSheetData) chars[x].extData.get(item.key))));

                  } else if (chars[x].imgData.containsKey(item.key)) {
                     ImageIcon img = (ImageIcon) chars[x].imgData.get(item.key);
                     String szImgPath = szCharPath + chars[x].GetCharacterName() + "_" + item.name + ".png";
                     if (!ExportImageToFile(img, szImgPath)) {
                        return false;
                     }

                     pw.print(HTMLTemplate.HTML_TEMPLATE_TABLE_ROW.replace(HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_NAME,
                           StringEscapeUtils.escapeHtml4(item.name)).replace(
                           HTMLTemplate.HTML_TEMPLATE_TABLE_ITEM_DATA, "<img src='" + szImgPath + "'/>"));

                  }
               }

            }

            //Add table footer.
            pw.print(HTMLTemplate.HTML_TEMPLATE_FOOTER_ROW);
            if (!consolidate || (consolidate && x == chars.length - 1)) {

                if(chkIncludeGraph.isSelected()) {
                    //Add family tree
                    String dTree = charactersToDtree();
                    if (!dTree.isEmpty()) {
                        pw.println(HTMLTemplate.HTML_TEMPLATE_FAMILY_TREE.replace(HTMLTemplate.HTML_TEMPLATE_FAMILY_TREE_DATA, dTree));
                    }
                }

               Calendar currentDate = Calendar.getInstance();
               SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss d MMM yyyy");

               pw.print(HTMLTemplate.HTML_TEMPLATE_FOOTER.replace(
                     HTMLTemplate.HTML_TEMPLATE_FOOTER_TEXT,
                     "Generated by </i><a href='" + XEED.szHomePage + "'>XEED " + XEED.szVersion + " Build "
                           + XEED.lngBuild + "." + XEED.GetXEEDCRC32() + "</a><i> at "
                           + formatter.format(currentDate.getTime())));
            }

            pw.close();

         }
      } catch (Exception e) {
         return false;
      }

      return true;

   }

    /**
     * A very simple and hack-ish implementation of generating a dTree graph from characters.
     * @return
     */
   private String charactersToDtree() {

       Map<Character, DTreeNode> nodeLookup = new HashMap<Character, DTreeNode>();
       DTreeNode root = new DTreeNode(null);
       Set<Character> targetCharacters = new HashSet<Character>();
       targetCharacters.addAll(Arrays.asList(chars));

       // Validate data
       for(Character c: chars){
           // Check that we don't have multiple parents, that makes this more complicated.
           if(c.chrData.size() > 1) {
               JOptionPane.showMessageDialog(null,
                       "Exporting to a family tree is currently limited max one parent field per character.",
                       "Attention", JOptionPane.WARNING_MESSAGE);
               return "";
           }
       }

       for(Character c: chars){
           addCharacterToTree(c, targetCharacters, nodeLookup, root);
       }

       return root.toJSON();
   }

    class DTreeNode {

        public List<DTreeNode> children;
        public Character character;

        public DTreeNode(Character character) {
            this.character = character;
            children = new ArrayList<DTreeNode>();
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            if(character == null) {
                sb.append("[");
            }else {
                sb.append("{\n\"name\": \"" + character.GetCharacterName() + "\", \nchildren: [");
            }
            for(DTreeNode n : children) {
                sb.append(n.toJSON());
                sb.append(", ");
            }
            sb.append("]");
            if(character != null){
                sb.append('}');
            }
            return sb.toString();
        }
    }

   private void addCharacterToTree(Character c, Set<Character> targetCharacters, Map<Character, DTreeNode> nodeMap,
                                   DTreeNode root) {

       if(nodeMap.containsKey(c)) {
           // Tree already contains the character
           return;
       }

       if(c.chrData.size() == 0) {
           // No parents. Add under root
           DTreeNode n = new DTreeNode(c);
           root.children.add(n);
           nodeMap.put(c, n);
           return;
       }

       Character parent = (Character) c.chrData.values().iterator().next();
       if(targetCharacters.contains(parent)) {
           // parent should be part of the graph
           addCharacterToTree(parent, targetCharacters, nodeMap, root);
           DTreeNode parentNode = nodeMap.get(parent);
           DTreeNode n = new DTreeNode(c);
           parentNode.children.add(n);
           nodeMap.put(c, n);
       } else {
           // Don't show parent. Add under root
           DTreeNode n = new DTreeNode(c);
           root.children.add(n);
           nodeMap.put(c, n);
       }

   }


   private String ExpandedSheetToHTMLTable(ExtendedSheetData esd) {

      String table = "";
      table += "<table border='0'>\n";

      for (int x = 0; x < esd.Properties.length; x++) {
         table += "<tr>\n";
         table += "<td width='35%'><PRE>" + StringEscapeUtils.escapeHtml4(esd.Properties[x]) + "</PRE></td>";
         table += "<td width='65%'>" + StringEscapeUtils.escapeHtml4(esd.Values[x]) + "</td>";
         table += "</tr>\n";
      }

      table += "</table>";
      return table;

   }

   private PdfPTable ExpandedSheetToPDFTable(ExtendedSheetData esd, Font f) {

      PdfPTable table = new PdfPTable(new float[] { 2, 8, 10, 2 }); //kanske måste ändra proportionerna

      for (int x = 0; x < esd.Properties.length; x++) {

         PdfPCell cell = new PdfPCell(new Phrase(""));
         cell.setBorder(PdfPCell.NO_BORDER);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(esd.Properties[x], f));
         cell.setBorder(PdfPCell.NO_BORDER);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(esd.Values[x], f));
         cell.setBorder(PdfPCell.NO_BORDER);
         table.addCell(cell);

         cell = new PdfPCell(new Phrase(""));
         cell.setBorder(PdfPCell.NO_BORDER);
         table.addCell(cell);

      }

      table.setSplitLate(false);
      table.setKeepTogether(false);
      table.setComplete(true);
      return table;

   }

   private String GenerateIDList(String id, int i) {

      String list = "";

      for (int y = 0; y < i; y++) {
         list += "#" + id + y;
         if (y != i - 1) {
            list += ",";
         }
      }

      return list;
   }

   private boolean ExportCharactersToPDF(String szPath) {

      Document document = null;
      final Font titleFont = new Font(FontFamily.TIMES_ROMAN, 28, Font.BOLD);
      final Font characterNameFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
      final Font tableBodyFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL);
      final Font subTableBodyFont = new Font(FontFamily.COURIER, 11, Font.NORMAL);
      final Font footerFont = new Font(FontFamily.HELVETICA, 10, Font.ITALIC);
      final Font footerFont_link = new Font(FontFamily.HELVETICA, 10);
      SortCharacters();

      //Generate footer.
      Calendar currentDate = Calendar.getInstance();
      SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss d MMM yyyy");

      Anchor anchor = new Anchor("XEED " + XEED.szVersion + " Build " + XEED.lngBuild + "." + XEED.GetXEEDCRC32(),
            footerFont_link);
      anchor.setReference(XEED.szHomePage);

      Phrase footer = new Phrase("Generated by ", footerFont);
      footer.add(anchor);
      footer.add(new Phrase(" at " + formatter.format(currentDate.getTime()), footerFont));

      try {

         String szCharPath;
         szCharPath = szPath + File.separator + XEED.szSettingName + File.separator;
         new File(szCharPath).mkdirs(); //create the directory

         for (int x = 0; x < chars.length; x++) {

            if (consolidate) {

               if (x == 0) {

                  new File(szCharPath + template.GetName() + "_characters.pdf").delete(); //remove an old file.
                  document = new Document(PageSize.A4, 36, 36, 54, 54); //open the document
                  PdfWriter writer = PdfWriter.getInstance(document,
                        new FileOutputStream(szCharPath + template.GetName() + "_characters.pdf")); //start outputting.
                  HeaderFooter event = new HeaderFooter();
                  event.setFooter(footer);
                  writer.setBoxSize("art", new Rectangle(36, 30, 559, 800));
                  writer.setPageEvent(event);
                  document.open();

                  Paragraph title = new Paragraph(XEED.szSettingName, titleFont); //Add title
                  title.setAlignment(Paragraph.ALIGN_CENTER);
                  document.add(title);

               }

            } else {

               new File(szCharPath + chars[x].GetCharacterName() + ".pdf").delete(); //remove an old file.
               document = new Document(PageSize.A4, 36, 36, 54, 54); //open the document
               PdfWriter writer = PdfWriter.getInstance(document,
                     new FileOutputStream(szCharPath + chars[x].GetCharacterName() + ".pdf")); //start outputting.
               HeaderFooter event = new HeaderFooter();
               event.setFooter(footer);
               writer.setBoxSize("art", new Rectangle(36, 30, 559, 800));
               writer.setPageEvent(event);
               document.open();

               Paragraph title = new Paragraph(XEED.szSettingName, titleFont); //Add title
               title.setAlignment(Paragraph.ALIGN_CENTER);
               document.add(title);

            }

            //construct table
            PdfPTable characterTable = new PdfPTable(new float[] { 1, 3 }); //kanske måste ändra proportionerna
            characterTable.setWidthPercentage(100f);
            characterTable.setSpacingBefore(15f);
            characterTable.setKeepTogether(false);
            characterTable.setSplitLate(false); //En lång rad delas i raden och inte före. Bra med tanke på extended sheets.

            //add character name
            PdfPCell characterName = new PdfPCell(new Phrase(chars[x].GetCharacterName(), characterNameFont));
            characterName.setBackgroundColor(BaseColor.BLACK);
            characterName.setHorizontalAlignment(Element.ALIGN_LEFT);
            characterName.setColspan(2);
            characterName.setBorder(PdfPCell.NO_BORDER);
            characterTable.addCell(characterName);

            for (int y = 0; y < tblData.getRowCount(); y++) {

               boolean selected = (Boolean) tblData.getValueAt(y, 0);
               table_item item = (table_item) tblData.getValueAt(y, 1);

               if (selected && !item.key.equals(Constants.CHARACTER_NAME)) {

                  if (chars[x].chrData.containsKey(item.key)) {

                     PdfPCell cell = new PdfPCell(new Phrase(item.name, tableBodyFont));
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     characterTable.addCell(cell);

                     cell = new PdfPCell(new Phrase(chars[x].chrData.get(item.key).toString(), tableBodyFont));
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     characterTable.addCell(cell);

                  } else if (chars[x].szData.containsKey(item.key)) {

                     PdfPCell cell = new PdfPCell(new Phrase(item.name, tableBodyFont));
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     characterTable.addCell(cell);

                     cell = new PdfPCell(new Phrase(chars[x].szData.get(item.key).toString(), tableBodyFont));
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     characterTable.addCell(cell);

                  } else if (chars[x].extData.containsKey(item.key)) {

                     PdfPCell cell = new PdfPCell(new Phrase(item.name, tableBodyFont));
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     characterTable.addCell(cell);

                     cell = new PdfPCell(ExpandedSheetToPDFTable((ExtendedSheetData) chars[x].extData.get(item.key),
                           subTableBodyFont));
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                     characterTable.addCell(cell);

                  } else if (chars[x].imgData.containsKey(item.key)) {

                     PdfPCell cell = new PdfPCell(new Phrase(item.name, tableBodyFont));
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     characterTable.addCell(cell);

                     ImageIcon img = (ImageIcon) chars[x].imgData.get(item.key);
                     com.itextpdf.text.Image pdfImg = com.itextpdf.text.Image.getInstance(img.getImage(), null);

                     cell = new PdfPCell(pdfImg);
                     cell.setBorder(PdfPCell.NO_BORDER);
                     cell.setPadding(2f);
                     characterTable.addCell(cell);

                  }
               }

            }

            characterTable.setComplete(true);
            document.add(characterTable);

            if (!consolidate || (consolidate && x == chars.length - 1)) {
               document.close();
            }

         }
      } catch (Exception e) {
         return false;
      }

      return true;
   }

   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnExport = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        btnMoveRowUp = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        chkConsolidate = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        comboSort = new javax.swing.JComboBox();
        chkReverse = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        comboFormat = new javax.swing.JComboBox();
        chkIncludeGraph = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Character Exporter");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_go.png"))); // NOI18N
        btnExport.setToolTipText("Export characters");
        btnExport.setName("btnExport"); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTableHeader.add("");
        jTableHeader.add("Field");
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            jTableModel,
            jTableHeader
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, Object.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if(mColIndex!=0){
                    return false;
                }else{
                    return true;
                }
            }

        });
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tblData.setName("tblData"); // NOI18N
        tblData.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblData.setShowHorizontalLines(false);
        tblData.setShowVerticalLines(false);
        tblData.getTableHeader().setReorderingAllowed(false);
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDataKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblData);

        btnMoveRowUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_up.png"))); // NOI18N
        btnMoveRowUp.setToolTipText("Move rows up");
        btnMoveRowUp.setName("btnMoveRowUp"); // NOI18N
        btnMoveRowUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveRowUpActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_down.png"))); // NOI18N
        jButton5.setToolTipText("Move rows down");
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        chkConsolidate.setText("Consolidate");
        chkConsolidate.setToolTipText("Rather than one text file for each character, one text file is created for all characters.");
        chkConsolidate.setName("chkConsolidate"); // NOI18N
        chkConsolidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkConsolidateActionPerformed(evt);
            }
        });

        jLabel1.setText("Sort by:");
        jLabel1.setName("jLabel1"); // NOI18N

        comboSort.setToolTipText("Select primary sorting factor, second is always name.");
        comboSort.setName("comboSort"); // NOI18N

        chkReverse.setText("Reversed");
        chkReverse.setName("chkReverse"); // NOI18N

        jLabel2.setText("Format");
        jLabel2.setName("jLabel2"); // NOI18N

        comboFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HTML", "PDF", "Text" }));
        comboFormat.setName("comboFormat"); // NOI18N

        chkIncludeGraph.setText("Include Graph");
        chkIncludeGraph.setToolTipText("If checked XEED will include the character Genealogy as a dTree graph if exported to HTML.");
        chkIncludeGraph.setName("chkIncludeGraph"); // NOI18N
        chkIncludeGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIncludeGraphActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnMoveRowUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkConsolidate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExport))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboFormat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboSort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkReverse)
                            .addComponent(chkIncludeGraph))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(chkIncludeGraph))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkReverse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExport)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnMoveRowUp)
                        .addComponent(jButton5))
                    .addComponent(chkConsolidate, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed

      if (chars.length == 0) {
         JOptionPane
               .showMessageDialog(
                     null,
                     "No characters selected. Please select characters in the main window and restart the character exporter. ",
                     "Attention", JOptionPane.INFORMATION_MESSAGE);
         XEED.hwndCharacterExporter = null;
         dispose();
         return;
      }

      JFileChooser fc = new JFileChooser();
      fc.setDialogTitle("Select output folder");
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      int intRet = fc.showSaveDialog(null);
      if (intRet != JFileChooser.APPROVE_OPTION) {
         JOptionPane.showMessageDialog(null, "Characters not exported", "Attention", JOptionPane.INFORMATION_MESSAGE);
         return;
      }

      boolean ret = false;
      if (comboFormat.getSelectedIndex() == 0) {
         ret = ExportCharactersToHTMLFile(fc.getSelectedFile().getAbsolutePath());
      } else if (comboFormat.getSelectedIndex() == 1) {
         ret = ExportCharactersToPDF(fc.getSelectedFile().getAbsolutePath());
      } else if (comboFormat.getSelectedIndex() == 2) {
         ret = ExportCharactersToTextFile(fc.getSelectedFile().getAbsolutePath());
      }

      if (ret) {
         JOptionPane.showMessageDialog(null, "Characters succesfully exported!", "Success!",
               JOptionPane.INFORMATION_MESSAGE);
      } else {
         JOptionPane.showMessageDialog(null, "Error while exporting characters!", "Error!", JOptionPane.ERROR_MESSAGE);
      }

   }//GEN-LAST:event_btnExportActionPerformed

   private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      XEED.hwndCharacterExporter = null;
      dispose();
   }//GEN-LAST:event_formWindowClosing

   private void btnMoveRowUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveRowUpActionPerformed

      if (tblData.getSelectedRow() == -1) {
         return;
      }

      int intSelected[] = tblData.getSelectedRows();
      if (intSelected[0] - 1 < 0) {
         return;
      }

      df.moveRow(intSelected[0], intSelected[intSelected.length - 1], intSelected[0] - 1);
      df.fireTableDataChanged();
      tblData.getSelectionModel().setSelectionInterval(intSelected[0] - 1, intSelected[intSelected.length - 1] - 1);

   }//GEN-LAST:event_btnMoveRowUpActionPerformed

   private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

      if (tblData.getSelectedRow() == -1) {
         return;
      }

      int intSelected[] = tblData.getSelectedRows();
      if (intSelected[intSelected.length - 1] + 1 >= tblData.getRowCount()) {
         return;
      }
      df.moveRow(intSelected[0], intSelected[intSelected.length - 1], intSelected[0] + 1);
      df.fireTableDataChanged();
      tblData.getSelectionModel().setSelectionInterval(intSelected[0] + 1, intSelected[intSelected.length - 1] + 1);

   }//GEN-LAST:event_jButton5ActionPerformed

   private void tblDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyPressed
      if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_A) {

         for (int x = 0; x < tblData.getRowCount(); x++) {
            Vector o = (Vector) jTableModel.get(x);
            o.set(0, true);
            jTableModel.set(x, o);
         }

         df.fireTableDataChanged();

      }
   }//GEN-LAST:event_tblDataKeyPressed

   private void chkConsolidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkConsolidateActionPerformed
      consolidate = chkConsolidate.isSelected();
   }//GEN-LAST:event_chkConsolidateActionPerformed

    private void chkIncludeGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIncludeGraphActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkIncludeGraphActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnMoveRowUp;
    private javax.swing.JCheckBox chkConsolidate;
    private javax.swing.JCheckBox chkIncludeGraph;
    private javax.swing.JCheckBox chkReverse;
    private javax.swing.JComboBox comboFormat;
    private javax.swing.JComboBox comboSort;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblData;
    // End of variables declaration//GEN-END:variables

   private class table_item {

      public String name;
      public String key;

      public String toString() {
         return name;
      }
   }

   private class combo_item {

      String name;
      String key;
      boolean isChrData = false;
      boolean isSzData = false;
      boolean isExtData = false;
      boolean isImgData = false;

      public String toString() {
         return name;
      }
   }

   private class sort_item implements Comparable<sort_item> {

      Character c;
      String sortString;

      @Override
      public int compareTo(sort_item o) {
         int ret = sortString.compareTo(o.sortString);
         if (ret == 0 && !sortString.equals(c.GetCharacterName())) {
            return c.GetCharacterName().compareTo(o.c.GetCharacterName());
         } else {
            return ret;
         }
      }
   }

   class HeaderFooter extends PdfPageEventHelper {

      Phrase footer;

      public void setFooter(Phrase s) {
         this.footer = s;
      }

      @Override
      public void onEndPage(PdfWriter writer, Document document) {
         Rectangle rect = writer.getBoxSize("art");
         ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
               new Phrase(Integer.toString(writer.getPageNumber())), rect.getRight(), rect.getTop(), 0);
         ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer,
               (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
      }
   }
}
