package com.qualityeclipse.favorites;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

/**
 * The main plugin class to be used in the desktop.
 */
public class FavoritesPlugin extends AbstractUIPlugin {
   
   // The identifier for this plugin
   public static final String ID = "com.qualityeclipse.favorites";

   // The shared instance.
   private static FavoritesPlugin plugin;

   // The configuration preferences
   private IEclipsePreferences configPrefs;

   /**
    * The constructor.
    */
   public FavoritesPlugin() {
      plugin = this;
   }

   /**
    * This method is called upon plug-in activation
    */
   public void start(BundleContext context) throws Exception {
      super.start(context);
      getLog().log(new Status(IStatus.ERROR, ID, 0, "config dir " + getConfigDir().getAbsolutePath(), null));
      Preferences configPrefs = getConfigPrefs();
      String configKey = "myKey";
      getLog().log(new Status(IStatus.ERROR, ID, 0, "config node " + configPrefs.get(configKey, "myDefault"), null));
      configPrefs.put(configKey, "foo");
      configPrefs.flush();
   }

   /**
    * This method is called when the plug-in is stopped
    */
   public void stop(BundleContext context) throws Exception {
      super.stop(context);
      if (configPrefs != null) {
         configPrefs.flush();
         configPrefs = null;
      }
      plugin = null;
   }

   /**
    * Returns the shared instance.
    */
   public static FavoritesPlugin getDefault() {
      return plugin;
   }

   /**
    * Returns an image descriptor for the image file 
    * at the given plug-in relative path.
    * 
    * @param path
    *           the path
    * @return the image descriptor
    */
   public static ImageDescriptor getImageDescriptor(String path) {
      return AbstractUIPlugin.imageDescriptorFromPlugin(
            "com.qualityeclipse.favorites", path);
   }
   
   /**
    * Answer the configuration directory for this plug-in
    * that is shared by all workspaces of this installation.
    */
   public File getConfigDir() {
      Location location = Platform.getConfigurationLocation();
      if (location != null) {
         URL configURL = location.getURL();
         if (configURL != null
               && configURL.getProtocol().startsWith("file")) {
            return new File(configURL.getFile(), ID);
         }
      }
      // If the configuration directory is read-only,
      // then return an alternate location
      // rather than null or throwing an Exception
      return getStateLocation().toFile();
   }

   /**
    * Answer the configuration preferences for this plug-in
    * that are shared by all workspaces of this installation
    */
   public Preferences getConfigPrefs() {
      if (configPrefs == null)
         configPrefs = new ConfigurationScope().getNode(ID);
      return configPrefs;
   }
   
   public static boolean isEarlyStartupDisabled() {
      String plugins = PlatformUI.getPreferenceStore().getString(
         /*
          * Copy constant out of internal Eclipse interface
          * IPreferenceConstants.PLUGINS_NOT_ACTIVATED_ON_STARTUP
          * so that we are not accessing internal type.
          */
         "PLUGINS_NOT_ACTIVATED_ON_STARTUP");
      return plugins.indexOf(FavoritesPlugin.ID) != -1;
   }
}
