package com.qualityeclipse.favorites.views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.qualityeclipse.favorites.model.FavoritesManager;
import com.qualityeclipse.favorites.model.IFavoriteItem;

/**
 * The <code>FavoritesTransfer</code> class is used to transfer an array of
 * <code>IFavoriteItem</code>s from one part to another in a drag-and-drop
 * operation or a cut, copy, paste action.
 * <p>
 * In every drag-and-drop operation, there is a <code>DragSource</code> and a
 * <code>DropTarget</code>. When a drag occurs, a <code>Transfer</code> is used
 * to marshall the drag data from the source into a byte array. When a drop
 * occurs, another <code>Transfer</code> is used to marshall the byte array into
 * drop data for the target.
 * <p>
 * When a <code>CutAction</code> or a <code>CopyAction</code> is performed, this
 * transfer is used to place references to the selected items on the
 * <code>Clipboard</code>. When a <code>PasteAction</code> is performed, the
 * references on the clipboard are used to move or copy the items to the
 * selected destination.
 */
public class FavoritesTransfer extends ByteArrayTransfer
{
   /**
    * Singleton instance.
    */
   private static final FavoritesTransfer INSTANCE =
         new FavoritesTransfer();

   /**
    * Create a unique ID to make sure that different Eclipse applications use
    * different "types" of <code>FavoritesTransfer</code>.
    */
   private static final String TYPE_NAME =
         "favorites-transfer-format:" + System.currentTimeMillis()
               + ":" + INSTANCE.hashCode();

   /**
    * The registered identifier.
    */
   private static final int TYPEID = registerType(TYPE_NAME);

   /**
    * Singleton constructor.
    */
   private FavoritesTransfer() {
      super();
   }

   /**
    * Answer the single instance.
    */
   public static FavoritesTransfer getInstance() {
      return INSTANCE;
   }

   /**
    * Returns the platform-specfic IDs of the data types that can be converted
    * using this transfer agent.
    * 
    * @return an array of data type identifiers
    */
   protected int[] getTypeIds() {
      return new int[] { TYPEID };
   }

   /**
    * Returns the platform-specfic names of the data types that can be converted
    * using this transfer agent.
    * 
    * @return an array of data type names
    * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
    */
   protected String[] getTypeNames() {
      return new String[] { TYPE_NAME };
   }

   /**
    * Converts a Java representation of data to a platform-specific
    * representation of the data.
    * 
    * @param data
    *       a Java representation of the data to be converted; the type of
    *       object that is passed in is dependent on the <code>Transfer</code>
    *       subclass.
    * 
    * @param transferData
    *       an empty TransferData object; this object will be filled in on
    *       return with the platform-specific representation of the data.
    * @see org.eclipse.swt.dnd.Transfer #javaToNative( java.lang.Object,
    *    org.eclipse.swt.dnd.TransferData)
    */
   protected void javaToNative(Object data, TransferData transferData) {

      if (!(data instanceof IFavoriteItem[]))
         return;
      IFavoriteItem[] items = (IFavoriteItem[]) data;

      /**
       * The serialization format is: (int) number of items Then, the following
       * for each item: (String) the type of item (String) the item-specific
       * info glob
       */

      try {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         DataOutputStream dataOut = new DataOutputStream(out);
         dataOut.writeInt(items.length);
         for (int i = 0; i < items.length; i++) {
            IFavoriteItem item = items[i];
            dataOut.writeUTF(item.getType().getId());
            dataOut.writeUTF(item.getInfo());
         }
         dataOut.close();
         out.close();
         super.javaToNative(out.toByteArray(), transferData);
      }
      catch (IOException e) {
         // Send nothing if there were problems.
      }
   }

   /**
    * Converts a platform-specific representation of data to a Java
    * representation.
    * 
    * @param transferData
    *       the platform-specific representation of the data to be converted
    * @return a java representation of the converted data if the conversion was
    *    successful, else <code>null</code>
    * @see org.eclipse.swt.dnd.Transfer
    *    #nativeToJava(org.eclipse.swt.dnd.TransferData)
    */
   protected Object nativeToJava(TransferData transferData) {

      /**
       * The serialization format is: <br>
       * (int) number of items <br>
       * Then, the following for each item: <br>
       * (String) the type of item <br>
       * (String) the item-specific info glob
       */

      byte[] bytes = (byte[]) super.nativeToJava(transferData);
      if (bytes == null)
         return null;
      DataInputStream in =
            new DataInputStream(new ByteArrayInputStream(bytes));
      try {
         FavoritesManager mgr = FavoritesManager.getManager();
         int count = in.readInt();
         List<IFavoriteItem> items =
               new ArrayList<IFavoriteItem>(count);
         for (int i = 0; i < count; i++) {
            String typeId = in.readUTF();
            String info = in.readUTF();
            items.add(mgr.newFavoriteFor(typeId, info));
         }
         return items.toArray(new IFavoriteItem[items.size()]);
      }
      catch (IOException e) {
         return null;
      }
   }
}