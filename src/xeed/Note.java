/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Erik
 */
public class Note implements Comparable<Note> {

   public String szData = "";
   public String szTitle = "";
   public boolean boolFolder = false;
   public ArrayList<Note> children = new ArrayList(0);
   public long lngID = 0; //Används när när nodes importeras och sparas

   @Override
   public String toString() {
      return szTitle;
   }

   public Note[] GetChildren() {
      if (children.isEmpty()) {
         return null;
      }
      Note tmpArray[] = new Note[children.size()];
      children.toArray(tmpArray);
      Arrays.sort(tmpArray);
      return tmpArray;
   }

   public void AddChild(Note s) {
      children.add(s);
   }

   public void RemoveChild(Note s) {
      children.remove(s);
   }

   public boolean AddNodeToParent(Note s, long parent) {

      if (s == null) {
         return false;
      }

      if (parent == lngID) {
         AddChild(s);
         return true;
      }

      Note childs[] = GetChildren();
      if (childs == null) {
         return false;
      }

      for (int x = 0; x < childs.length; x++) {

         if (childs[x].boolFolder) {
            if (childs[x].AddNodeToParent(s, parent)) {
               return true;
            }
         }
      }
      return false;
   }

   public static Note ParseNodes(String szData) {

      Note topNote = new Note();
      topNote.szTitle = "Notes";
      topNote.boolFolder = true;
      topNote.lngID = 0;

      String szNodes[] = XEED.GetElements(szData, Constants.NOTE, false);

      for (int x = 0; x < szNodes.length; x++) {

         try {

            Note s = new Note();

            s.szTitle = XEED.GetElement(szNodes[x], Constants.NOTE_TITLE, true);
            s.boolFolder = Boolean.parseBoolean(XEED.GetElement(szNodes[x], Constants.NOTE_FOLDER, false));

            long parent = Long.parseLong(XEED.GetElement(szNodes[x], Constants.NOTE_PARENT, false));
            s.lngID = Long.parseLong(XEED.GetElement(szNodes[x], Constants.NOTE_ID, false));

            if (!s.boolFolder) {
               s.szData = XEED.GetElement(szNodes[x], Constants.NOTE_DATA, true);
            }

            topNote.AddNodeToParent(s, parent);
         } catch (Exception e) {
            return null;
         }

      }

      return topNote;

   }

   @Override
   public int compareTo(Note o) {
      return szTitle.compareToIgnoreCase(o.szTitle);
   }
}
