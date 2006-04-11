/*
 * $Id: SchemeResourceKeys.java,v 1.12.2.1 2006/04/11 10:27:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.awt.Color;
import java.awt.Dimension;

import com.syrus.AMFICOM.client.resource.ResourceKeys;


/**
 * @author $Author: stas $
 * @version $Revision: 1.12.2.1 $, $Date: 2006/04/11 10:27:00 $
 * @module schemeclient
 */
public interface SchemeResourceKeys extends ResourceKeys {

	Color[] DEFAULT_COLOR_SET = new Color[] { Color.WHITE, Color.BLUE,
			Color.GREEN, Color.RED, Color.GRAY, Color.CYAN, Color.MAGENTA,
			Color.ORANGE, Color.PINK, Color.YELLOW, Color.BLACK };
	
	Dimension DIMENSION_BUTTON = new Dimension(24, 24);
	Dimension DIMENSION_TEXTAREA = new Dimension(100, 30);
	
	// in pixels (coarse)
//	Dimension A0 = new Dimension (3360, 4752);
//	Dimension A1 = new Dimension (3360, 2376);
//	Dimension A2 = new Dimension (1680, 2376);
//	Dimension A3 = new Dimension (1680, 1188);
//	Dimension A4 = new Dimension (840, 1188);
	
	// in millimeters
	Long[] WIDTHS = new Long[] {
		new Long(210), // A4
		new Long(420), // A3
		new Long(420), // A2
		new Long(840), // A1
		new Long(840)  // A0 
	};
	Long[] HEIGHTS = new Long[] {
		new Long(297), // A4
		new Long(297), // A3
		new Long(594), // A2
		new Long(594), // A1
		new Long(1188) // A0 
	};
	String[] SIZES = new String[] { "size_a4", "size_a3", //$NON-NLS-1$//$NON-NLS-2$
			"size_a2", "size_a1", "size_a0", "size_custom" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	
	String ICON_CATALOG = "icon.catalog";
	String ICON_SCHEME = "icon.scheme";
	String ICON_SCHEMATICS = "icon.schematics";
	String ICON_COMPONENTS = "icon.components";
	String ICON_NEW = "icon.new";
	String ICON_SAVE = "icon.save";
	
	String COLOR_PORT_NO_TYPE = "color.port_no_type";
	String COLOR_PORT_NO_LINK = "color.port_no_link";
	String COLOR_PORT_TERMAL = "color.port_thermal";
	String COLOR_PORT_COMMON = "color.port_common";
	
	String	FRAME_TREE = "Window.treeFrame";
	String	FRAME_EDITOR_MAIN	= "Window.editorFrame";
	String	FRAME_GENERAL_PROPERTIES	= "Window.generalFrame";
	String 	FRAME_ADDITIONAL_PROPERIES = "Window.additionalFrame";
	String	FRAME_CHARACTERISTICS = "Window.characteristicFrame";
	
	String EMPTY = ""; //$NON-NLS-1$
	String ROOT = "scheme.root"; //$NON-NLS-1$
	String INPUT = "input"; //$NON-NLS-1$
	String OUTPUT = "output"; //$NON-NLS-1$
	String CONFIGURATION = "configuration"; //$NON-NLS-1$
	String NETWORK_DIRECTORY = "network_directory"; //$NON-NLS-1$
	String MONITORING_DIRECTORY = "monitoring_directory"; //$NON-NLS-1$
	String LINK_TYPE = "link_type"; //$NON-NLS-1$
	String CABLE_LINK_TYPE = "cable_link_type"; //$NON-NLS-1$
	String PORT_TYPE = "port_type"; //$NON-NLS-1$
	String CABLE_PORT_TYPE = "cable_port_type"; //$NON-NLS-1$
//	String EQUIPMENT_TYPE = "equipment_type"; //$NON-NLS-1$
	String PROTO_EQUIPMENT = "equipment_type"; //$NON-NLS-1$
	String MEASUREMENT_TYPE = "measurement_type"; //$NON-NLS-1$
	String MEASUREMENTPORT_TYPE = "measurement_port_type"; //$NON-NLS-1$
	String SCHEME = "scheme"; //$NON-NLS-1$
	String SCHEME_LINK = "scheme_link"; //$NON-NLS-1$
	String SCHEME_CABLELINK = "scheme_cable_link"; //$NON-NLS-1$
	String SCHEME_ELEMENT = "scheme_element"; //$NON-NLS-1$
	String SCHEME_DEVICE = "scheme_device"; //$NON-NLS-1$
	String SCHEME_PORT = "scheme_port"; //$NON-NLS-1$
	String SCHEME_CABLE_PORT = "scheme_cable_port"; //$NON-NLS-1$
	String SCHEME_PATH = "scheme_path"; //$NON-NLS-1$
	String SCHEME_PROTO_ELEMENT = "scheme_proto_element"; //$NON-NLS-1$
	String SCHEME_PROTO_GROUP = "scheme_proto_group"; //$NON-NLS-1$
	String SCHEME_TYPE = "scheme_type"; //$NON-NLS-1$
	String SCHEME_KIND = "scheme_kind"; //$NON-NLS-1$
	String SCHEME_DIMENSION = "scheme_dimension"; //$NON-NLS-1$
	String SCHEME_SHORT_WIDTH = "scheme_short_width"; //$NON-NLS-1$
	String SCHEME_SHORT_HEIGHT = "scheme_short_height"; //$NON-NLS-1$
	String SCHEME_SIZE = "scheme_size"; //$NON-NLS-1$
	String SCHEME_MONITORING_SOLUTION = "scheme_monitoring_solution"; //$NON-NLS-1$
	
	String START_ELEMENT = "start_element"; //$NON-NLS-1$
	String PATH_ELEMENTS = "path_elements"; //$NON-NLS-1$
	String END_ELEMENT = "end_element"; //$NON-NLS-1$
	
	String NAME = "name"; //$NON-NLS-1$
	String CODENAME = "class"; //$NON-NLS-1$
	String LABEL = "label"; //$NON-NLS-1$
	String DESCRIPTION = "description"; //$NON-NLS-1$
	String MANUFACTURER = "manufacturer"; //$NON-NLS-1$
	String MANUFACTURER_CODE = "manufacturer_code"; //$NON-NLS-1$
	String SUPPLIER = "supplier"; //$NON-NLS-1$
	String SUPPLIER_CODE = "supplier_code"; //$NON-NLS-1$
	String HARDWARE = "hardware"; //$NON-NLS-1$
	String SOFTWARE = "software"; //$NON-NLS-1$
	String SERNUM = "serial"; //$NON-NLS-1$
	String VERSION = "version"; //$NON-NLS-1$
	String LONGITUDE = "longitude"; //$NON-NLS-1$
	String LATITUDE = "latitude"; //$NON-NLS-1$
	String LENGTH = "length"; //$NON-NLS-1$
	String OPTICAL_LENGTH = "optical_length"; //$NON-NLS-1$
	String PHYSICAL_LENGTH = "physical_length"; //$NON-NLS-1$
	String ADDRESS = "address"; //$NON-NLS-1$
	String PORT = "port"; //$NON-NLS-1$
	String INVNUMBER = "invNumber"; //$NON-NLS-1$
	String MARK = "mark"; //$NON-NLS-1$
	String COLOR = "color"; //$NON-NLS-1$
	String SYNCHRONIZE = "synchronize"; //$NON-NLS-1$
	String THREAD_NUMBER = "threadNumber"; //$NON-NLS-1$
	String THREAD = "thread"; //$NON-NLS-1$
	String PARENT_GROUP = "parent_group"; //$NON-NLS-1$
	String THREAD_PORT_MAP = "thread_port_map"; //$NON-NLS-1$
			
	String NEW_SCHEME = "new_scheme"; //$NON-NLS-1$
		
	String EXISTING_MEASUREMENT_PORT = "existing_measurement_port"; //$NON-NLS-1$
	String NEW_MEASUREMENT_PORT = "new_measurement_port"; //$NON-NLS-1$
	String EXISTING_TYPE = "existing_type"; //$NON-NLS-1$
	String NEW_TYPE = "new_type"; //$NON-NLS-1$
	String CHARACTERISTIC = "characteristic"; //$NON-NLS-1$
	String ADD_CHARACTERISTIC = "add_characteristic"; //$NON-NLS-1$
	String REMOVE_CHARACTERISTIC = "remove_characteristic"; //$NON-NLS-1$
	String PARAMETERS = "parameters"; //$NON-NLS-1$
	String MEASUREMENT_PORT_TYPES = "measurement_port_types"; //$NON-NLS-1$
	String MEASUREMENT_PORT = "measurement_port"; //$NON-NLS-1$
	String PORTS = "ports"; //$NON-NLS-1$
	String TYPE = "type"; //$NON-NLS-1$
	String MEASUREMENT_TYPES = "measurement_types"; //$NON-NLS-1$
	String CHARACTERISTICTYPESORT_OPTICAL = "optical_characteristics"; //$NON-NLS-1$
	String CHARACTERISTICTYPESORT_ELECTRICAL = "electrical_characteristics"; //$NON-NLS-1$
	String CHARACTERISTICTYPESORT_OPERATIONAL = "operational_characteristics"; //$NON-NLS-1$
	String CHARACTERISTICTYPESORT_INTERFACE = "interface_characteristics"; //$NON-NLS-1$
	String CHARACTERISTICTYPESORT_VISUAL = "visual_attributes"; //$NON-NLS-1$
	String PORTTYPESORT_OPTICAL = "optical"; //$NON-NLS-1$
	String PORTTYPESORT_THERMAL = "thermal"; //$NON-NLS-1$
	String PORTTYPESORT_ELECTICAL = "electrical"; //$NON-NLS-1$
	String PORTTYPEKIND_CABLE = "cable_port"; //$NON-NLS-1$
	String PORTTYPEKIND_SIMPLE = "port"; //$NON-NLS-1$
	String OK = "ok"; //$NON-NLS-1$
	String CANCEL = "cancel"; //$NON-NLS-1$
	String LOCAL_ADDRESS = "local_address";  //$NON-NLS-1$
	
	String METRE = "metre_short"; //$NON-NLS-1$
	String MILLIMETER = "millimetre_short"; //$NON-NLS-1$

	String EQUIPMENT = "equipment"; //$NON-NLS-1$
	String INSTANCE = "instance"; //$NON-NLS-1$
	String KIS = "measurement_module"; //$NON-NLS-1$
	String SCHEME_TYPE_NETWORK = "scheme_type_network"; //$NON-NLS-1$
	String SCHEME_TYPE_CABLE = "scheme_type_cable"; //$NON-NLS-1$
	String SCHEME_TYPE_BUILDING = "scheme_type_building"; //$NON-NLS-1$
	String SCHEME_TYPE_FLOOR = "scheme_type_floor"; //$NON-NLS-1$
	
}
