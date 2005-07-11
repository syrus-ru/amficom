/*-
 * $Id: EquipmentTypeCodenames.java,v 1.3 2005/07/11 12:31:41 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.awt.Component;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/07/11 12:31:41 $
 * @module generalclient_v1
 */

public class EquipmentTypeCodenames {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.resource.EquipmentTypeCodenames";//$NON-NLS-1$
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String	REFLECTOMETER	= "reflectometer";
	public static final String	SWITCH				= "switch";
	public static final String	MUFF					= "muff";
	public static final String	CABLE_PANEL 	= "cable_panel";

	public static final String	TRANSMITTER 	= "transmitter";
	public static final String	RECEIVER 			= "receiver";
	public static final String	MULTIPLEXOR 	= "multiplexor";
	public static final String	CROSS 				= "cross";
	public static final String	FILTER 				= "filter";
	
	public static final String	OTHER 				= "other"; //$NON-NLS-1$
	
	public static final String	THREAD 				= "thread"; 

	public static String[] DEFAULT_CODENAMES = new String[] { OTHER, REFLECTOMETER,
		SWITCH, FILTER, CABLE_PANEL, CROSS, MUFF, MULTIPLEXOR, RECEIVER,
		TRANSMITTER };

	private static ListCellRenderer renderer;
	private static class EqtCodenamesRenderer extends JLabel implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			String key = (String)value;
			try {
				key = RESOURCE_BUNDLE.getString(key);
				setText(key);
			} catch (MissingResourceException e) {
				Log.debugMessage(this.getClass().getSimpleName() + "| resource not found for key " + key, Level.FINER); //$NON-NLS-1$
			}
			return this;
		}
	}
	
	private EquipmentTypeCodenames() {
		// empty
	}

	public static ListCellRenderer getListCellRenderer() {
		if (renderer == null) {
			renderer = new EqtCodenamesRenderer(); 
		}
		return renderer;
	}
	
	public static String getName(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
