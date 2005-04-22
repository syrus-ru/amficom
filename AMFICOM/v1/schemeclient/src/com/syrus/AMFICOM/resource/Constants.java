/*
 * $Id: Constants.java,v 1.1 2005/04/22 07:31:59 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.awt.*;
import java.awt.Toolkit;

import javax.swing.*;


/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/22 07:31:59 $
 * @module schemeclient_v1
 */

public interface Constants {
	public static Color[] DEFAULT_COLOR_SET = new Color[] { Color.WHITE, Color.BLUE,
			Color.GREEN, Color.RED, Color.GRAY, Color.CYAN, Color.MAGENTA,
			Color.ORANGE, Color.PINK, Color.YELLOW, Color.BLACK };
	public static final Dimension DIMENSION_BUTTON = new Dimension(24, 24);
	public static final Dimension DIMENSION_TEXTAREA = new Dimension(100, 30);
	
	public static final int _OK = 1;
	public static final int _CANCEL = 0;
	
	public static final Icon ICON_CATALOG = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")); //$NON-NLS-1$
	public static final Icon ICON_SCHEME = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")); //$NON-NLS-1$
	
	public static final String EMPTY = ""; //$NON-NLS-1$
	public static final String ROOT = "root"; //$NON-NLS-1$
	public static final String INPUT = "input"; //$NON-NLS-1$
	public static final String OUTPUT = "output"; //$NON-NLS-1$
	public static final String CONFIGURATION = "configuration"; //$NON-NLS-1$
	public static final String NETWORK_DIRECTORY = "network_directory"; //$NON-NLS-1$
	public static final String MONITORING_DIRECTORY = "monitoring_directory"; //$NON-NLS-1$
	public static final String LINK_TYPE = "link_type"; //$NON-NLS-1$
	public static final String CABLE_LINK_TYPE = "cable_link_type"; //$NON-NLS-1$
	public static final String PORT_TYPE = "port_type"; //$NON-NLS-1$
	public static final String EQUIPMENT_TYPE = "equipment_type"; //$NON-NLS-1$
	public static final String MEASUREMENT_TYPE = "measurement_type"; //$NON-NLS-1$
	public static final String MEASUREMENTPORT_TYPE = "measurement_port_type"; //$NON-NLS-1$
	public static final String SCHEME = "scheme"; //$NON-NLS-1$
	public static final String SCHEME_LINK = "scheme_link"; //$NON-NLS-1$
	public static final String SCHEME_CABLELINK = "scheme_cable_link"; //$NON-NLS-1$
	public static final String SCHEME_ELEMENT = "scheme_element"; //$NON-NLS-1$
	public static final String SCHEME_DEVICE = "scheme_device"; //$NON-NLS-1$
	public static final String SCHEME_PORT = "scheme_port"; //$NON-NLS-1$
	public static final String SCHEME_CABLE_PORT = "scheme_cable_port"; //$NON-NLS-1$
	public static final String SCHEME_PATH = "scheme_path"; //$NON-NLS-1$
	public static final String SCHEME_PROTO_ELEMENT = "scheme_proto_element"; //$NON-NLS-1$
	public static final String SCHEME_PROTO_GROUP = "scheme_proto_group"; //$NON-NLS-1$
	public static final String SCHEME_TYPE = "scheme_type"; //$NON-NLS-1$
		
	public static final String NAME = "name"; //$NON-NLS-1$
	public static final String CODENAME = "class"; //$NON-NLS-1$
	public static final String LABEL = "label"; //$NON-NLS-1$
	public static final String DESCRIPTION = "description"; //$NON-NLS-1$
	public static final String MANUFACTURER = "manufacturer"; //$NON-NLS-1$
	public static final String MANUFACTURER_CODE = "manufacturer_code"; //$NON-NLS-1$
	public static final String SUPPLIER = "supplier"; //$NON-NLS-1$
	public static final String SUPPLIER_CODE = "supplier_code"; //$NON-NLS-1$
	public static final String HARDWARE = "hardware"; //$NON-NLS-1$
	public static final String SOFTWARE = "software"; //$NON-NLS-1$
	public static final String SERNUM = "serial"; //$NON-NLS-1$
	public static final String VERSION = "version"; //$NON-NLS-1$
	public static final String LONGITUDE = "longitude"; //$NON-NLS-1$
	public static final String LATITUDE = "latitude"; //$NON-NLS-1$
	public static final String LENGTH = "length"; //$NON-NLS-1$
	public static final String OPTICAL_LENGTH = "optical_length"; //$NON-NLS-1$
	public static final String PHYSICAL_LENGTH = "physical_length"; //$NON-NLS-1$
	public static final String ADDRESS = "address"; //$NON-NLS-1$
	public static final String PORT = "port"; //$NON-NLS-1$
	public static final String INVNUMBER = "invNumber"; //$NON-NLS-1$
	public static final String COLOR = "color"; //$NON-NLS-1$
	
	public static final String NEW_SCHEME = "new_scheme"; //$NON-NLS-1$
		
	public static final String EXISTING_TYPE = "existing_type"; //$NON-NLS-1$
	public static final String NEW_TYPE = "new_type"; //$NON-NLS-1$
	public static final String CHARACTERISTIC = "characteristic"; //$NON-NLS-1$
	public static final String ADD_CHARACTERISTIC = "add_characteristic"; //$NON-NLS-1$
	public static final String REMOVE_CHARACTERISTIC = "remove_characteristic"; //$NON-NLS-1$
	public static final String PARAMETERS = "parameters"; //$NON-NLS-1$
	public static final String MEASUREMENT_PORT_TYPES = "measurement_port_types"; //$NON-NLS-1$
	public static final String PORTS = "ports"; //$NON-NLS-1$
	public static final String TYPE = "type"; //$NON-NLS-1$
	public static final String MEASUREMENT_TYPES = "measurement_types"; //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_OPTICAL = "optical_characteristics"; //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_ELECTRICAL = "electrical_characteristics"; //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_OPERATIONAL = "operational_characteristics"; //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_INTERFACE = "interface_characteristics"; //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_VISUAL = "visual_attributes"; //$NON-NLS-1$
	public static final String PORTTYPESORT_OPTICAL = "optical"; //$NON-NLS-1$
	public static final String PORTTYPESORT_THERMAL = "thermal"; //$NON-NLS-1$
	public static final String PORTTYPESORT_ELECTICAL = "electrical"; //$NON-NLS-1$
	public static final String OK = "ok"; //$NON-NLS-1$
	public static final String CANCEL = "cancel"; //$NON-NLS-1$

	public static final String EQUIPMENT = "equipment"; //$NON-NLS-1$
	public static final String INSTANCE = "instance"; //$NON-NLS-1$
	public static final String KIS = "measurement_module"; //$NON-NLS-1$
	public static final String SCHEME_TYPE_NETWORK = "scheme_type_network"; //$NON-NLS-1$
	public static final String SCHEME_TYPE_CABLE = "scheme_type_cable"; //$NON-NLS-1$
	public static final String SCHEME_TYPE_BUILDING = "scheme_type_building"; //$NON-NLS-1$
}
