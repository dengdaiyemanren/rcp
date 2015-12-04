package com.qualityeclipse.favorites;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.qualityeclipse.favorites.model.FavoritesManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class FavoritesActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.qualityeclipse.favorites";

	// The shared instance
	private static FavoritesActivator plugin;
	
	// The configuration preferences
	private IEclipsePreferences configPrefs;
	
	/**
	 * The constructor
	 */
	public FavoritesActivator() {
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception {
      FavoritesManager.getManager().saveFavorites();
		saveConfigPrefs();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 */
	public static FavoritesActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Answer the configuration location for this plug-in
	 * 
	 * @return the plugin's config directory (not <code>null</code>)
	 */
	public File getConfigDir() {
		Location location = Platform.getConfigurationLocation();
		if (location != null) {
			URL configURL = location.getURL();
			if (configURL != null && configURL.getProtocol().startsWith("file")) {
				return new File(configURL.getFile(), PLUGIN_ID);
			}
		}
		// If the configuration directory is read-only,
		// then return an alternate location
		// rather than null or throwing an Exception.
		return getStateLocation().toFile();
	}

	/**
	 * Answer the configuration preferences shared among multiple workspaces.
	 * 
	 * @return the configuration preferences or <code>null</code> if the
	 *         configuration directory is read-only or unspecified.
	 */
	public Preferences getConfigPrefs() {
		if (configPrefs == null)
			configPrefs = new ConfigurationScope().getNode(PLUGIN_ID);
		return configPrefs;
	}
	
	/**
	 * Save the configuration preferences if they have been loaded
	 */
	public void saveConfigPrefs() {
		if (configPrefs != null) {
			try {
				configPrefs.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Return the plug-in's version
	 */
	public Version getVersion() {
		return new Version((String) getBundle().getHeaders().get(
				org.osgi.framework.Constants.BUNDLE_VERSION));
	}
}
