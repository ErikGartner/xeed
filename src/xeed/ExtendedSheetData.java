/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import templates.ExtendedForm;

/**
 *
 * @author Erik
 */
public class ExtendedSheetData {

   public String[] Properties = new String[0];
   public String[] Values = new String[0];
   public ExtendedForm form = null;

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
