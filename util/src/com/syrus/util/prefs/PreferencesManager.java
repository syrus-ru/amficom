/*
 * $Id: PreferencesManager.java,v 1.6 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.prefs;

import java.io.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @todo PREFS LOCATION: local or db
 *
 * @version $Revision: 1.6 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public final class PreferencesManager {
	public static final String PREFERENCES_ROOT = "amficom"; //$NON-NLS-1$
	
	private PreferencesManager() {
		assert false;
	}

	static {
		try {
			Class.forName(IIOPConnectionManager.class.getName());
		} catch (final ClassNotFoundException cnfe) {
			// empty
		}
	}

	static {
		migratePreferences(PREFERENCES_ROOT + "/util/connections/jndi", "Connection.properties");  //$NON-NLS-1$//$NON-NLS-2$
		migratePreferences(PREFERENCES_ROOT + "/util/connections/jdbc", "ServerConnection.properties");  //$NON-NLS-1$//$NON-NLS-2$
	}

	private static void migratePreferences(String pathName, String fileName) {
		/*
		 * Look for ini file in the current directory.
		 */
		File file = new File(System.getProperty("user.dir"), fileName); //$NON-NLS-1$
		if (! file.exists()) {
			/*
			 * If failed, look for ini file in user's home directory.
			 */
			file = new File(System.getProperty("user.home"), fileName); //$NON-NLS-1$
			if (! file.exists())
				file = null;
		}
		try {
			Preferences preferences = Preferences.userRoot().node(pathName);
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			Enumeration keys = properties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) (keys.nextElement());
				preferences.put(key, properties.getProperty(key));
			}
			preferences.flush();
		} catch (NullPointerException npe) {
			/*
			 * Stay silent if no property file found.
			 */
		} catch (IOException ioe) {
			/*
			 * Ditto.
			 */
		} catch (BackingStoreException bse) {
			System.err.println("Preferences migration failed!"); //$NON-NLS-1$
		}			
	}

	private static DefaultMutableTreeNode getTreeNode(Preferences node) {
		DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(new PreferencesHolder(node), true);
		String childrenNames[];
		try {
			childrenNames = node.childrenNames();
		} catch (BackingStoreException bse) {
			childrenNames = new String[0];
		}
		for (int i = 0; i < childrenNames.length; i ++)
			defaultMutableTreeNode.add(getTreeNode(node.node(childrenNames[i])));
		String keys[];
		try {
			keys = node.keys();
		} catch (BackingStoreException bse) {
			keys = new String[0];
		}
		for (int i = 0; i < keys.length; i ++) {
			String key = keys[i];
			defaultMutableTreeNode.add(new DefaultMutableTreeNode(key, false));
		}
		return defaultMutableTreeNode;
	}

	public static DefaultMutableTreeNode getTreeNode() {
		return getTreeNode(Preferences.userRoot().node(PREFERENCES_ROOT));
	}

	public static class PreferencesHolder {
		private Preferences preferences;

		private PreferencesHolder() {
			assert false;
		}

		public PreferencesHolder(Preferences preferences) {
			this.preferences = preferences;
		}

		public Preferences getPreferences() {
			return this.preferences;
		}

		public String toString() {
			return this.preferences.name();
		}
	}
}
