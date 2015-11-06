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
public class Group implements Comparable<Group> {

   public ArrayList<Relation> relationsDB = new ArrayList(0);
   public String szName = "";
   public String szDescription = "";
   public long lngID = -1;
   private ArrayList<Long> Members = new ArrayList(0);

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

      Character c = XEED.GetCharacterByID(ID);
      if (c == null) {
         return;
      }

      String comp = "";
      for (int x = 0; x < XEED.groupDB.size(); x++) {
         if (XEED.groupDB.get(x).IsMember(c.characterID)) {
            comp += XEED.groupDB.get(x).szName + ", ";
         }
      }
      if (!comp.isEmpty()) {
         comp = comp.substring(0, comp.length() - ", ".length());
      }
      c.szData.put(Constants.CHARACTER_GROUPS, comp);
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
   public int compareTo(Group o) {
      return szName.compareToIgnoreCase(o.szName);
   }

   public static Group ParseGroup(String szData) {

      try {
         String[] szMembers = XEED.GetElements(szData, Constants.GROUP_MEMBERS, false);

         Group g = new Group();
         g.lngID = Long.parseLong(XEED.GetElement(szData, Constants.GROUP_ID, false));
         g.szName = XEED.GetElement(szData, Constants.GROUP_NAME, true);
         g.szDescription = XEED.GetElement(szData, Constants.GROUP_DESCRIPTION, true);

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
      szCompile += XEED.CreateElement(Long.toString(lngID), Constants.GROUP_ID, false);

      long[] lngMembers = GetMemebers();
      for (int x = 0; x < lngMembers.length; x++) {
         szCompile += XEED.CreateElement(Long.toString(lngMembers[x]), Constants.GROUP_MEMBERS, false);
      }

      szCompile += XEED.CreateElement(szName, Constants.GROUP_NAME, true);
      szCompile += XEED.CreateElement(szDescription, Constants.GROUP_DESCRIPTION, true);

      return szCompile;
   }
}
