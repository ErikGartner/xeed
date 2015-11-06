/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

/**
 *
 * @author Erik
 */
public class Relation implements Cloneable {

    public long lngTargetID;
    public int intType; //0- character, 1- group.  the target of the relation
    public String szRelation = "";
    public String szSummary = "";

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public static Relation ParseRelation(String szData) {

        try {

            Relation r = new Relation();
            r.lngTargetID = Long.parseLong(XEED.GetElement(szData, Constants.RELATION_TARGET, false));
            r.intType = Integer.parseInt(XEED.GetElement(szData, Constants.RELATION_TYPE, false));
            r.szRelation = XEED.GetElement(szData, Constants.RELATION_DATA, true);

            return r;

        } catch (Exception e) {
            return null;
        }
    }
}
