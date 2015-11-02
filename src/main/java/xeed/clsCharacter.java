/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import TemplateItems.frmExtended;
import TemplateItems.pnlGroups;
import TemplateItems.pnlRelations;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Erik
 */
public class clsCharacter {

    public String templateIdentifier;
    public long characterID;
    public clsTemplate template;
    public HashMap szData;                       //contains strings
    public HashMap imgData;                      //contains all images
    public HashMap extData;                      //contains extendedsheetdata classes
    public HashMap chrData;                      //contains extendedsheetdata classes
    //public String[] dataKeys;
    public ArrayList<clsRelation> relationsDB = new ArrayList(0);
    //pointers to from objects, aka to the view
    public frmCharacter hwndForm;                       //the window displaying the character
    //more pointers to thing that need calling for active updating, intended to be private and accessed through public methods.
    public pnlRelations hwndRelations;
    public pnlGroups hwndGroups;

    public clsCharacter(clsTemplate t) {

        characterID = clsEngine.CreateUniqueCharacterID();
        template = t;
        templateIdentifier = t.GetTemplateID();
        szData = new HashMap();
        imgData = new HashMap();
        extData = new HashMap();
        chrData = new HashMap();

    }

    public void ChangeTemplate(clsTemplate newTemplate) {

        CloseForm();

        template = newTemplate;
        templateIdentifier = newTemplate.GetTemplateID();
        szData = new HashMap();
        imgData = new HashMap();
        extData = new HashMap();
        chrData = new HashMap();

        clsCharacter[] affectedcharacters = new clsCharacter[1];
        affectedcharacters[0] = this;
        clsEngine.hwndNotifier.FireUpdate(affectedcharacters, false, false, false, false, false, true, false, false, false);

    }

    public String GetCharacterName() {

        String s = (String) szData.get(clsConstants.CHARACTER_NAME);      //
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

        hwndRelations = null;   //förhindra leaks!!
        hwndGroups = null;
        hwndForm.dispose();
        hwndForm = null;

        String keys[] = GetExtKeys();
        for (int x = 0; x < keys.length; x++) {
            clsExtendedSheetData esd = (clsExtendedSheetData) extData.get(keys[x]);
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

        hwndForm = new frmCharacter(template.RenderTemplate(this), this);
        hwndForm.setTitle(GetCharacterName());
        return true;

    }

    public void AddRelation(long ID, int Type, String szDescription) {

        clsRelation r = new clsRelation();
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
        comp += clsEngine.CreateElement(String.valueOf(characterID), clsConstants.CHARACTER_ID, false);
        comp += clsEngine.CreateElement(templateIdentifier, clsConstants.CHARACTER_TEMPLATE, false);
        comp += clsEngine.CreateElement(GetCharacterName(), clsConstants.CHARACTER_NAME, true);

        String keys[] = GetSzKeys();
        for (int x = 0; x < keys.length; x++) {
            if (szData.get(keys[x]) != null) {                                                                            //Do not write null szData, also prevents checking class incase of null.

                String item = "";
                item += clsEngine.CreateElement(keys[x], clsConstants.CHARACTER_KEY, true);
                item += clsEngine.CreateElement((String) szData.get(keys[x]), clsConstants.CHARACTER_DATA, true);
                item = clsEngine.CreateElement(item, clsConstants.CHARACTER_ITEM, false);
                comp += item;
            }
        }


        //Extendeed 
        keys = GetExtKeys();
        for (int x = 0; x < keys.length; x++) {
            if (extData.get(keys[x]) != null) {

                String item = "";
                item += clsEngine.CreateElement(keys[x], clsConstants.CHARACTER_KEY, true);
                clsExtendedSheetData esd = (clsExtendedSheetData) extData.get(keys[x]);
                for (int y = 0; y < esd.Properties.length; y++) {
                    item += clsEngine.CreateElement(esd.Properties[y], clsConstants.CHARACTER_DATA_PROPERTY, true);
                    item += clsEngine.CreateElement(esd.Values[y], clsConstants.CHARACTER_DATA_VALUE, true);
                }
                item += clsEngine.CreateElement(clsConstants.CHARACTER_EXTENDED, clsConstants.CHARACTER_SPECOP, false);
                item = clsEngine.CreateElement(item, clsConstants.CHARACTER_ITEM, false);
                comp += item;

            }
        }

        //Characters
        keys = GetChrKeys();
        for (int x = 0; x < keys.length; x++) {
            if (chrData.get(keys[x]) != null) {

                String item = "";
                item += clsEngine.CreateElement(keys[x], clsConstants.CHARACTER_KEY, true);
                long id = ((clsCharacter) chrData.get(keys[x])).characterID;
                item += clsEngine.CreateElement(Long.toString(id), clsConstants.CHARACTER_DATA, false);
                item += clsEngine.CreateElement(clsConstants.CHARACTER, clsConstants.CHARACTER_SPECOP, false);      //Tells the reader that the szData is a link to a character.
                item = clsEngine.CreateElement(item, clsConstants.CHARACTER_ITEM, false);
                comp += item;

            }
        }

        comp = clsEngine.CreateElement(comp, clsConstants.CHARACTER, false);
        return comp;
    }
    
     public static clsCharacter ParseCharacter(String szData) {

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
