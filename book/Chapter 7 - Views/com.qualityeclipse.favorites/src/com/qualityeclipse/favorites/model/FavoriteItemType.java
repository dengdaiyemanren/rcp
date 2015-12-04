package com.qualityeclipse.favorites.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.qualityeclipse.favorites.FavoritesLog;

/**
 * The <code>FavoriteItemType</code> object is returned by the
 * {@link IFavoriteItem#getType()} method and represents a type-safe
 * enumeration that can be used for sorting and storing Favorites
 * objects. It has a human-readable name associated with it for
 * display purposes. Introducing the FavoriteItemType rather than a
 * simple String or int allows the sort order to be separated from the
 * human-readable name associated with a type of Favorites object.
 */
public abstract class FavoriteItemType
      implements Comparable<FavoriteItemType>
{
   private static final ISharedImages PLATFORM_IMAGES = 
      PlatformUI.getWorkbench().getSharedImages();

   private final String id;
   private final String printName;
   private final int ordinal;
   
   private FavoriteItemType(String id, String name, int position) {
      this.id = id;
      this.ordinal = position;
      this.printName = name;
   }
   
   public String getId() {
      return id;
   }
   
   public String getName() {
      return printName;
   }
   
   public abstract Image getImage();
   public abstract IFavoriteItem newFavorite(Object obj);
   public abstract IFavoriteItem loadFavorite(String info);
   
   public int compareTo(FavoriteItemType other) {
      return this.ordinal - other.ordinal;
   }

   ////////////////////////////////////////////////////////////////////////////
   //
   // Constants representing types of favorites objects.
   //
   ////////////////////////////////////////////////////////////////////////////

   public static final FavoriteItemType UNKNOWN 
      = new FavoriteItemType("Unknown", "Unknown", 0) 
   {
      public Image getImage() {
         return null;
      }
   
      public IFavoriteItem newFavorite(Object obj) {
         return null;
      }
   
      public IFavoriteItem loadFavorite(String info) {
         return null;
      }
   };
   
   public static final FavoriteItemType WORKBENCH_FILE 
      = new FavoriteItemType("WBFile", "Workbench File", 1) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
      }
   
      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IFile))
            return null;
         return new FavoriteResource(this, (IFile) obj);
      }
   
      public IFavoriteItem loadFavorite(String info) {
         return FavoriteResource.loadFavorite(this, info);
      }
   };
   
   public static final FavoriteItemType WORKBENCH_FOLDER 
      = new FavoriteItemType("WBFolder", "Workbench Folder", 2)
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
      }
   
      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IFolder))
            return null;
         return new FavoriteResource(this, (IFolder) obj);
      }
   
      public IFavoriteItem loadFavorite(String info) {
         return FavoriteResource.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType WORKBENCH_PROJECT 
      = new FavoriteItemType("WBProj", "WorkbenchProject", 3) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IProject))
            return null;
         return new FavoriteResource(this, (IProject) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteResource.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType JAVA_PROJECT 
      = new FavoriteItemType("JProj", "Java Project", 4) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IJavaProject))
            return null;
         return new FavoriteJavaElement(this, (IJavaProject) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteJavaElement.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType JAVA_PACKAGE_ROOT 
      = new FavoriteItemType("JPkgRoot", "Java Package Root", 5) 
   {
      public Image getImage() {
         return PLATFORM_IMAGES
               .getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IPackageFragmentRoot))
            return null;
         return new FavoriteJavaElement(this,
               (IPackageFragmentRoot) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteJavaElement.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType JAVA_PACKAGE 
      = new FavoriteItemType("JPkg", "Java Package", 6) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PACKAGE);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IPackageFragment))
            return null;
         return new FavoriteJavaElement(this, (IPackageFragment) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteJavaElement.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType JAVA_CLASS_FILE 
      = new FavoriteItemType("JClass", "Java Class File", 7) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CFILE);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IClassFile))
            return null;
         return new FavoriteJavaElement(this, (IClassFile) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteJavaElement.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType JAVA_COMP_UNIT 
      = new FavoriteItemType("JCompUnit", "Java Compilation Unit", 8) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CUNIT);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof ICompilationUnit))
            return null;
         return new FavoriteJavaElement(this, (ICompilationUnit) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteJavaElement.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType JAVA_INTERFACE 
      = new FavoriteItemType("JInterface", "Java Interface", 9) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_INTERFACE);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IType))
            return null;
         try {
            if (!((IType) obj).isInterface())
               return null;
         }
         catch (JavaModelException e) {
            FavoritesLog.logError(e);
         }
         return new FavoriteJavaElement(this, (IType) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteJavaElement.loadFavorite(this, info);
      }
   };

   public static final FavoriteItemType JAVA_CLASS 
      = new FavoriteItemType("JClass", "Java Class", 10) 
   {
      public Image getImage() {
         return org.eclipse.jdt.ui.JavaUI.getSharedImages()
               .getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CLASS);
      }

      public IFavoriteItem newFavorite(Object obj) {
         if (!(obj instanceof IType))
            return null;
         try {
            if (((IType) obj).isInterface())
               return null;
         }
         catch (JavaModelException e) {
            FavoritesLog.logError(e);
         }
         return new FavoriteJavaElement(this, (IType) obj);
      }

      public IFavoriteItem loadFavorite(String info) {
         return FavoriteJavaElement.loadFavorite(this, info);
      }
   };

   ////////////////////////////////////////////////////////////////////////////
   //
   // Accessors for all Favorite types
   //
   ////////////////////////////////////////////////////////////////////////////
   
   private static final FavoriteItemType[] TYPES = { UNKNOWN,
         WORKBENCH_FILE, WORKBENCH_FOLDER, WORKBENCH_PROJECT,
         JAVA_PROJECT, JAVA_PACKAGE_ROOT, JAVA_PACKAGE,
         JAVA_CLASS_FILE, JAVA_COMP_UNIT, JAVA_INTERFACE, JAVA_CLASS, };
   
   public static FavoriteItemType[] getTypes() {
      return TYPES;
   }
}
