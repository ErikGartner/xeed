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
public class clsNote implements Comparable<clsNote> {

    public String szData = "";
    public String szTitle = "";
    public boolean boolFolder = false;
    public ArrayList<clsNote> children = new ArrayList(0);
    public long lngID = 0;                 //Används när när nodes importeras och sparas

    @Override
    public String toString() {
        return szTitle;
    }

    public clsNote[] GetChildren() {
        if (children.isEmpty()) {
            return null;
        }
        clsNote tmpArray[] = new clsNote[children.size()];
        children.toArray(tmpArray);
        Arrays.sort(tmpArray);
        return tmpArray;
    }

    public void AddChild(clsNote s) {
        children.add(s);
    }

    public void RemoveChild(clsNote s) {
        children.remove(s);
    }

    public boolean AddNodeToParent(clsNote s, long parent) {

        if (s == null) {
            return false;
        }

        if (parent == lngID) {
            AddChild(s);
            return true;
        }

        clsNote childs[] = GetChildren();
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

    public static clsNote ParseNodes(String szData) {

        clsNote topNote =  new clsNote();
        topNote.szTitle = "Notes";
        topNote.boolFolder = true;
        topNote.lngID = 0;

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

                topNote.AddNodeToParent(s, parent);
            } catch (Exception e) {
                return null;
            }

        }
        
        return topNote;

    }

    @Override
    public int compareTo(clsNote o) {
        return szTitle.compareToIgnoreCase(o.szTitle);
    }
}
