/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xeed;

import java.util.ArrayList;

/*
 * OBS! Det finns risk för updateloopar! Var försöktig med vilka metoder som
 * kallas!!
 */
public class Notifier implements Runnable {

   private Thread WorkerThread = null;
   private boolean Work = true;
   private ArrayList<UpdateTicket> UpdateQueue = new ArrayList(0);

   public Notifier() {
      WorkerThread = new Thread(this);
      WorkerThread.start();
   }

   @Override
   public synchronized void run() {

      if (Thread.currentThread() != WorkerThread) {
         return;
      }

      while (Work) {

         if (UpdateQueue.isEmpty()) {
            try {
               this.wait(1000);
            } catch (Exception e) {
            }
         } else {

            UpdateTicket ticket = UpdateQueue.get(0);
            UpdateQueue.remove(0);

            if (ticket.UpdateMainWindowCharacterList) {
               for (int x = 0; x < ticket.AffectedCharacters.length; x++) {
                  XEED.hwndMain.UpdateListItem(ticket.AffectedCharacters[x]);
               }
            }

            if (ticket.UpdateCharacterItems || ticket.UpdateCharacterGroupWindow
                  || ticket.UpdateCharacterRelationsWindowCharacterList
                  || ticket.UpdateCharacterRelationsWindowRelation) {
               for (int x = 0; x < ticket.AffectedCharacters.length; x++) {
                  if (ticket.AffectedCharacters[x].hwndForm != null) {
                     ticket.AffectedCharacters[x].hwndForm.ReloadItems(
                           ticket.UpdateCharacterRelationsWindowCharacterList, ticket.UpdateCharacterGroupWindow,
                           ticket.UpdateCharacterItems, ticket.UpdateCharacterRelationsWindowRelation);
                  }
               }
            }

            if (ticket.UpdateGroupMemberList) {
               if (XEED.hwndGroup != null) {
                  XEED.hwndGroup.RefreshData();
                  if (XEED.hwndGroup.relationsHandle != null) { //Updates the character lists of all components.
                     XEED.hwndGroup.relationsHandle.LoadData();
                  }
               }
            }

            if (ticket.UpdateGroupWindowRelations) {
               if (XEED.hwndGroup != null) {
                  if (XEED.hwndGroup.relationsHandle != null) {
                     XEED.hwndGroup.relationsHandle.LoadRelationship();
                  }
               }
            }

            if (ticket.UpdateRelationshipWindowLists) {
               if (XEED.hwndRelations != null) {
                  XEED.hwndRelations.LoadLists();
               }
            }

            if (ticket.UpdateRelationsWindowRelation) {
               if (XEED.hwndRelations != null) {
                  XEED.hwndRelations.LoadRelationship();
               }
            }

         }

      }

   }

   public synchronized void stop() {
      Work = false;
      notifyAll();
   }

   public synchronized void FireUpdate(Character[] AffectedCharacters, boolean UpdateCharacterGroupWindow,
         boolean UpdateCharacterItems, boolean UpdateCharacterRelationsWindowCharacterList,
         boolean UpdateGroupMemberList, boolean UpdateGroupWindowRelations, boolean UpdateMainWindowCharacterList,
         boolean UpdateRelationshipWindowLists, boolean UpdateCharacterRelationsWindowRelation,
         boolean UpdateRelationsWindowRelation) {

      UpdateTicket t = new UpdateTicket();

      t.AffectedCharacters = AffectedCharacters;
      t.UpdateCharacterItems = UpdateCharacterItems;
      t.UpdateCharacterGroupWindow = UpdateCharacterGroupWindow;
      t.UpdateCharacterRelationsWindowCharacterList = UpdateCharacterRelationsWindowCharacterList;
      t.UpdateGroupMemberList = UpdateGroupMemberList;
      t.UpdateGroupWindowRelations = UpdateGroupWindowRelations;
      t.UpdateMainWindowCharacterList = UpdateMainWindowCharacterList;
      t.UpdateRelationshipWindowLists = UpdateRelationshipWindowLists;
      t.UpdateCharacterRelationsWindowRelation = UpdateCharacterRelationsWindowRelation;
      t.UpdateRelationsWindowRelation = UpdateRelationsWindowRelation;
      UpdateQueue.add(t);
      notifyAll();

   }

   class UpdateTicket {

      boolean UpdateMainWindowCharacterList;
      boolean UpdateCharacterItems;
      boolean UpdateGroupMemberList;
      boolean UpdateGroupWindowRelations;
      boolean UpdateCharacterRelationsWindowCharacterList;
      boolean UpdateCharacterGroupWindow;
      boolean UpdateRelationshipWindowLists;
      boolean UpdateCharacterRelationsWindowRelation;
      boolean UpdateRelationsWindowRelation;
      Character[] AffectedCharacters;
   }
}
