/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import java.util.ArrayList;

/**
 *
 * @author Erik
 */
public class clsGroup implements Comparable<clsGroup> {

    public ArrayList<clsRelation> relationsDB = new ArrayList(0);
    public String szName = "";
    public String szDescription = "";
    public long lngID = -1;
    private ArrayList<Long> Members = new ArrayList(0);

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

    public boolean RelationAvailable(long ID, int Type) {
        for (int x = 0; x < relationsDB.size(); x++) {
            if (relationsDB.get(x).lngTargetID == ID && relationsDB.get(x).intType == Type) {
                return true;
            }
        }
        return false;
    }

    public String GetRelation(long ID, int Type) {
        for (int x = 0; x < relationsDB.size(); x++) {
            if (relationsDB.get(x).lngTargetID == ID && relationsDB.get(x).intType == Type) {
                return relationsDB.get(x).szRelation;
            }
        }
        return "";
    }

    public void AddMember(long ID) {
        if (!Members.contains(ID)) {
            Members.add(ID);
        }
        UpdateCharactersGroupList(ID);
    }

    public void DeleteMember(long ID) {
        Members.remove(ID);
        UpdateCharactersGroupList(ID);
    }

    public static void UpdateCharactersGroupList(long ID) {

        clsCharacter c = clsEngine.GetCharacterByID(ID);
        if (c == null) {
            return;
        }

        String comp = "";
        for (int x = 0; x < clsEngine.groupDB.size(); x++) {
            if (clsEngine.groupDB.get(x).IsMember(c.characterID)) {
                comp += clsEngine.groupDB.get(x).szName + ", ";
            }
        }
        if (!comp.isEmpty()) {
            comp = comp.substring(0, comp.length() - ", ".length());
        }
        c.szData.put(clsConstants.CHARACTER_GROUPS, comp);
    }

    public long[] GetMemebers() {
        long[] l = new long[Members.size()];
        for (int x = 0; x < Members.size(); x++) {
            l[x] = Members.get(x);
        }
        return l;
    }

    public boolean IsMember(long ID) {
        return Members.contains(ID);
    }

    @Override
    public String toString() {
        return szName;
    }

    @Override
    public int compareTo(clsGroup o) {
        return szName.compareToIgnoreCase(o.szName);
    }

    public static clsGroup ParseGroup(String szData) {

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

    public String CompileGroup() {

        String szCompile = "";
        szCompile += clsEngine.CreateElement(Long.toString(lngID), clsConstants.GROUP_ID, false);

        long[] lngMembers = GetMemebers();
        for (int x = 0; x < lngMembers.length; x++) {
            szCompile += clsEngine.CreateElement(Long.toString(lngMembers[x]), clsConstants.GROUP_MEMBERS, false);
        }

        szCompile += clsEngine.CreateElement(szName, clsConstants.GROUP_NAME, true);
        szCompile += clsEngine.CreateElement(szDescription, clsConstants.GROUP_DESCRIPTION, true);

        return szCompile;
    }
}
