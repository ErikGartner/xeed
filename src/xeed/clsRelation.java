/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

/**
 *
 * @author Erik
 */
public class clsRelation implements Cloneable {

    public long lngTargetID;
    public int intType; //0- character, 1- group.  the target of the relation
    public String szRelation = "";
    public String szSummary = "";

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public static clsRelation ParseRelation(String szData) {

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
}
