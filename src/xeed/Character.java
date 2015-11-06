/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import forms.CharacterForm;
import templates.GroupsPanel;
import templates.RelationsPanel;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 *
 * @author Erik
 */
public class Character {

   public String templateIdentifier;
   public long characterID;
   public Template template;
   public HashMap szData; //contains strings
   public HashMap imgData; //contains all images
   public HashMap extData; //contains extendedsheetdata classes
   public HashMap chrData; //contains extendedsheetdata classes
   //public String[] dataKeys;
   public ArrayList<Relation> relationsDB = new ArrayList(0);
   //pointers to from objects, aka to the view
   public CharacterForm hwndForm; //the window displaying the character
   //more pointers to thing that need calling for active updating, intended to be private and accessed through public methods.
   public RelationsPanel hwndRelations;
   public GroupsPanel hwndGroups;

   public Character(Template t) {

      characterID = XEED.CreateUniqueCharacterID();
      template = t;
      templateIdentifier = t.GetTemplateID();
      szData = new HashMap();
      imgData = new HashMap();
      extData = new HashMap();
      chrData = new HashMap();

   }

   public void ChangeTemplate(Template newTemplate) {

      CloseForm();

      template = newTemplate;
      templateIdentifier = newTemplate.GetTemplateID();
      szData = new HashMap();
      imgData = new HashMap();
      extData = new HashMap();
      chrData = new HashMap();

      Character[] affectedcharacters = new Character[1];
      affectedcharacters[0] = this;
      XEED.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);

   }

   public String GetCharacterName() {

      String s = (String) szData.get(Constants.CHARACTER_NAME); //
      if (s == null) {
         return "";
      } else {
         return s;
      }
   }

   public void CloseForm() {

      if (hwndForm == null) {
         return;
      }

      hwndRelations = null; //förhindra leaks!!
      hwndGroups = null;
      hwndForm.dispose();
      hwndForm = null;

      String keys[] = GetExtKeys();
      for (int x = 0; x < keys.length; x++) {
         ExtendedSheetData esd = (ExtendedSheetData) extData.get(keys[x]);
         if (esd != null) {
            if (esd.form != null) {
               esd.form.dispose();
               esd.form = null;
            }
         }
      }

   }

   public boolean DisplayCharacter() {

      if (hwndForm != null) {
         hwndForm.setVisible(true);
         return true;
      }

      JPanel panel = template.RenderTemplate(this);
      if (panel == null) {
         return false;
      }

      hwndForm = new CharacterForm(template.RenderTemplate(this), this);
      hwndForm.setTitle(GetCharacterName());
      return true;

   }

   public void AddRelation(long ID, int Type, String szDescription) {

      Relation r = new Relation();
      r.intType = Type;
      r.lngTargetID = ID;
      r.szRelation = szDescription;

      for (int x = 0; x < relationsDB.size(); x++) {
         if (relationsDB.get(x).lngTargetID == ID && relationsDB.get(x).intType == Type) {
            relationsDB.set(x, r);
            return;
         }
      }

      relationsDB.add(r);

   }

   public void DeleteRelation(long ID, int Type) {
      for (int x = 0; x < relationsDB.size(); x++) {
         if (relationsDB.get(x).lngTargetID == ID && relationsDB.get(x).intType == Type) {
            relationsDB.remove(x);
            return;
         }
      }
   }

   public String GetRelation(long ID, int Type) {
      for (int x = 0; x < relationsDB.size(); x++) {
         if (relationsDB.get(x).lngTargetID == ID && relationsDB.get(x).intType == Type) {
            return relationsDB.get(x).szRelation;
         }
      }
      return "";
   }

   public String CompileCharacter() {

      String comp = "";
      comp += XEED.CreateElement(String.valueOf(characterID), Constants.CHARACTER_ID, false);
      comp += XEED.CreateElement(templateIdentifier, Constants.CHARACTER_TEMPLATE, false);
      comp += XEED.CreateElement(GetCharacterName(), Constants.CHARACTER_NAME, true);

      String keys[] = GetSzKeys();
      for (int x = 0; x < keys.length; x++) {
         if (szData.get(keys[x]) != null) {
            //Do not write null szData, also prevents checking class incase of null.
            String item = "";
            item += XEED.CreateElement(keys[x], Constants.CHARACTER_KEY, true);
            item += XEED.CreateElement((String) szData.get(keys[x]), Constants.CHARACTER_DATA, true);
            item = XEED.CreateElement(item, Constants.CHARACTER_ITEM, false);
            comp += item;
         }
      }

      //Extendeed 
      keys = GetExtKeys();
      for (int x = 0; x < keys.length; x++) {
         if (extData.get(keys[x]) != null) {

            String item = "";
            item += XEED.CreateElement(keys[x], Constants.CHARACTER_KEY, true);
            ExtendedSheetData esd = (ExtendedSheetData) extData.get(keys[x]);
            for (int y = 0; y < esd.Properties.length; y++) {
               item += XEED.CreateElement(esd.Properties[y], Constants.CHARACTER_DATA_PROPERTY, true);
               item += XEED.CreateElement(esd.Values[y], Constants.CHARACTER_DATA_VALUE, true);
            }
            item += XEED.CreateElement(Constants.CHARACTER_EXTENDED, Constants.CHARACTER_SPECOP, false);
            item = XEED.CreateElement(item, Constants.CHARACTER_ITEM, false);
            comp += item;

         }
      }

      //Characters
      keys = GetChrKeys();
      for (int x = 0; x < keys.length; x++) {
         if (chrData.get(keys[x]) != null) {

            String item = "";
            item += XEED.CreateElement(keys[x], Constants.CHARACTER_KEY, true);
            long id = ((Character) chrData.get(keys[x])).characterID;
            item += XEED.CreateElement(Long.toString(id), Constants.CHARACTER_DATA, false);
            item += XEED.CreateElement(Constants.CHARACTER, Constants.CHARACTER_SPECOP, false); //Tells the reader that the szData is a link to a character.
            item = XEED.CreateElement(item, Constants.CHARACTER_ITEM, false);
            comp += item;

         }
      }

      comp = XEED.CreateElement(comp, Constants.CHARACTER, false);
      return comp;
   }

   public static Character ParseCharacter(String szData) {

      String data = XEED.GetElement(szData, Constants.CHARACTER, false);
      if (data.isEmpty()) {
         return null;
      }

      String template = XEED.GetElement(data, Constants.CHARACTER_TEMPLATE, true);
      if (template.isEmpty()) {
         return null;
      }

      Template t = XEED.GetTemplateByID(template);
      if (t == null) {
         return null;
      }

      Character c = new Character(t);
      try {
         c.characterID = Long.parseLong(XEED.GetElement(data, Constants.CHARACTER_ID, false));
      } catch (Exception e) {
         return null;
      }
      c.szData.put(Constants.CHARACTER_NAME, XEED.GetElement(data, Constants.CHARACTER_NAME, true));

      String items[] = XEED.GetElements(data, Constants.CHARACTER_ITEM, false);
      for (int x = 0; x < items.length; x++) {

         String key = XEED.GetElement(items[x], Constants.CHARACTER_KEY, true);

         //Check for linking needs.
         String spec_op = XEED.GetElement(items[x], Constants.CHARACTER_SPECOP, false);
         if (spec_op.equalsIgnoreCase(Constants.CHARACTER)) {

            try {
               long lng = Long.parseLong(XEED.GetElement(items[x], Constants.CHARACTER_DATA, false));
               c.chrData.put(key, lng);
            } catch (Exception e) {
            }

         } else if (spec_op.equalsIgnoreCase(Constants.CHARACTER_EXTENDED)) {

            ExtendedSheetData esd = new ExtendedSheetData();
            esd.Properties = XEED.GetElements(data, Constants.CHARACTER_DATA_PROPERTY, true);
            esd.Values = XEED.GetElements(data, Constants.CHARACTER_DATA_VALUE, true);
            if (esd.Properties.length != esd.Values.length) {
               return null;
            }
            c.extData.put(key, esd);

         } else {
            String item_data = XEED.GetElement(items[x], Constants.CHARACTER_DATA, true);
            c.szData.put(key, item_data);
         }

      }
      return c;
   }

   public String[] GetSzKeys() {
      Object[] o = szData.keySet().toArray();
      String[] s = new String[o.length];
      for (int x = 0; x < o.length; x++) {
         s[x] = (String) o[x];
      }
      return s;
   }

   public String[] GetImgKeys() {
      Object[] o = imgData.keySet().toArray();
      String[] s = new String[o.length];
      for (int x = 0; x < o.length; x++) {
         s[x] = (String) o[x];
      }
      return s;
   }

   public String[] GetExtKeys() {
      Object[] o = extData.keySet().toArray();
      String[] s = new String[o.length];
      for (int x = 0; x < o.length; x++) {
         s[x] = (String) o[x];
      }
      return s;
   }

   public String[] GetChrKeys() {
      Object[] o = chrData.keySet().toArray();
      String[] s = new String[o.length];
      for (int x = 0; x < o.length; x++) {
         s[x] = (String) o[x];
      }
      return s;
   }

   @Override
   public String toString() {
      return GetCharacterName();
   }
}
