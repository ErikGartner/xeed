/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import TemplateItems.frmExtended;

/**
 *
 * @author Erik
 */
public class clsExtendedSheetData {

    public String[] Properties = new String[0];
    public String[] Values = new String[0];
    public frmExtended form = null;
    
    @Override
    public String toString() {
        String comp = "";
        for (int x = 0; x < Properties.length; x++) {
            comp += Properties[x] + ": " + Values[x];
            if (x != Properties.length - 1) {
                comp += ", ";
            }
        }
        return comp;
    }
}
