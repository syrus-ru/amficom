/*-
 * $Id: EquipmentTypeCodenames.java,v 1.5 2005/08/19 15:41:35 stas Exp $
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

import com.syrus.AMFICOM.configuration.EquipmentTypeCodename;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/08/19 15:41:35 $
 * @module schemeclient_v1
 */

public class EquipmentTypeCodenames {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.resource.EquipmentTypeCodenames";//$NON-NLS-1$
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static EquipmentTypeCodename[] DEFAULT_CODENAMES = new EquipmentTypeCodename[] { 
		EquipmentTypeCodename.OTHER, EquipmentTypeCodename.REFLECTOMETER,
		EquipmentTypeCodename.SWITCH, EquipmentTypeCodename.FILTER, 
		EquipmentTypeCodename.CABLE_PANEL, EquipmentTypeCodename.CROSS, 
		EquipmentTypeCodename.MUFF, EquipmentTypeCodename.MULTIPLEXOR, 
		EquipmentTypeCodename.RECEIVER, EquipmentTypeCodename.TRANSMITTER };

	private static ListCellRenderer renderer;
	private static class EqtCodenamesRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 130381146909218843L;

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			String key;
			if (value instanceof String) {
				key = (String) value;
			} else if (value instanceof EquipmentTypeCodename) {
				key = ((EquipmentTypeCodename)value).stringValue();
			} else {
				key = value.toString();
			}
			
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
