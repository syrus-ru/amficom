/*
 * $Id: PreferencesManager.java,v 1.1 2004/05/06 11:48:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.prefs;

import com.syrus.io.IniFile;
import java.io.*;
import java.util.List;
import java.util.prefs.*;
import javax.swing.tree.*;

/**
 * @todo PREFS LOCATION: local or db
 *
 * @version $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 * @module util
 */
public final class PreferencesManager {
	public static final String PREFERENCES_ROOT = "amficom";
	
	private PreferencesManager() {
	}

	static {
		Class clazz1 = IIOPConnectionManager.class;
		Class clazz2 = SMTPConnectionManager.class;
	}

	static {
		migratePreferences(PREFERENCES_ROOT + "/util/connections/jndi", "Connection.properties");
		migratePreferences(PREFERENCES_ROOT + "/util/connections/jdbc", "ServerConnection.properties");
	}

	private static void migratePreferences(String pathName, String fileName) {
		/*
		 * Look for ini file in the current directory.
		 */
		File file = new File(System.getProperty("user.dir"), fileName);
		if (! file.exists()) {
			/*
			 * If failed, look for ini file in user's home directory.
			 */
			file = new File(System.getProperty("user.home"), fileName);
			if (! file.exists())
				file = null;
		}
		try {
			IniFile iniFile = new IniFile(file);
			Preferences preferences = Preferences.userRoot().node(pathName);
			List list = iniFile.getKeys();
			String keys[] = (String[]) (list.toArray(new String[list.size()]));
			outer:
			for (int i = 0; i < keys.length; i ++) {
				/*
				 * Trim keys that start with whitespace. Ignore keys that start
				 * with '#' (standard) or ';' (non-standard, Syrus-specific ;-)
				 */
				String key = keys[i];
				try {
					int firstNonWhiteSpaceIndex = 0;
					while (true) {
						char c = key.charAt(firstNonWhiteSpaceIndex);
						if ((c == '#') || (c == ';'))
							continue outer;
						else if ((c == ' ') || (c == '\t'))
							firstNonWhiteSpaceIndex ++;
						else {
							key = key.substring(firstNonWhiteSpaceIndex);
							break;
						}
					}
				} catch (StringIndexOutOfBoundsException sioobe) {
					continue;
				}
				preferences.put(key, iniFile.getValue(keys[i]));
			}
			preferences.flush();
		} catch (NullPointerException npe) {
			/*
			 * Stay silent if no ini file found.
			 */
			;
		} catch (IOException ioe) {
			/*
			 * Ditto.
			 */
			;
		} catch (BackingStoreException bse) {
			System.err.println("Preferences migration failed!");
		}			
	}

	private static String getNodeFQN(Preferences node) {
		try {
			return getNodeFQN(node.parent()) + '/' + node.name();
		} catch (NullPointerException npe) {
			return node.name();
		}
	}

	private static void processNode(Preferences node) {
		System.out.println(getNodeFQN(node));
		String keys[];
		String childrenNames[];
		try {
			keys = node.keys();
		} catch (BackingStoreException bse) {
			keys = new String[0];
		}
		for (int i = 0; i < keys.length; i ++) {
			String key = keys[i];
			System.out.println("\t\"" + key + "\" = \"" + node.get(key, "") + '"');
		}
		try {
			childrenNames = node.childrenNames();
		} catch (BackingStoreException bse) {
			childrenNames = new String[0];
		}
		for (int i = 0; i < childrenNames.length; i ++)
			processNode(node.node(childrenNames[i]));
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
		}

		public PreferencesHolder(Preferences preferences) {
			this.preferences = preferences;
		}

		public Preferences getPreferences() {
			return preferences;
		}

		public String toString() {
			return preferences.name();
		}
	}
}
