/*
 * $Id: Constants.java,v 1.3 2005/03/30 13:33:39 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.*;
import java.awt.Toolkit;

import javax.swing.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/03/30 13:33:39 $
 * @module schemeclient_v1
 */

public interface Constants {
	public static final Dimension DIMENSION_BUTTON = new Dimension(24, 24);
	public static final Dimension DIMENSION_TEXTAREA = new Dimension(100, 30);
	
	public static final int OK = 1;
	public static final int CANCEL = 0;
	
	public static final Icon ICON_CATALOG = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")); //$NON-NLS-1$
	public static final Icon ICON_SCHEME = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")); //$NON-NLS-1$
	
	public static final String ROOT = "root"; //$NON-NLS-1$
	public static final String INPUT = "input"; //$NON-NLS-1$
	public static final String OUTPUT = "output"; //$NON-NLS-1$
	public static final String CONFIGURATION = "Configuration"; //$NON-NLS-1$
	public static final String NETWORK_DIRECTORY = "NetworkDirectory"; //$NON-NLS-1$
	public static final String MONITORING_DIRECTORY = "MonitoringDirectory"; //$NON-NLS-1$
	public static final String LINK_TYPE = "LinkType"; //$NON-NLS-1$
	public static final String CABLE_LINK_TYPE = "CableLinkType"; //$NON-NLS-1$
	public static final String PORT_TYPE = "PortType"; //$NON-NLS-1$
	public static final String EQUIPMENT_TYPE = "EquipmentType"; //$NON-NLS-1$
	public static final String MEASUREMENT_TYPE = "MeasurementType"; //$NON-NLS-1$
	public static final String MEASUREMENTPORT_TYPE = "MeasurementPortType"; //$NON-NLS-1$
	public static final String SCHEME = "Scheme"; //$NON-NLS-1$
	public static final String SCHEME_LINK = "SchemeLink"; //$NON-NLS-1$
	public static final String SCHEME_CABLELINK = "SchemeCableLink"; //$NON-NLS-1$
	public static final String SCHEME_ELEMENT = "SchemeElement"; //$NON-NLS-1$
	public static final String SCHEME_DEVICE = "SchemeDevice"; //$NON-NLS-1$
	public static final String SCHEME_PORT = "SchemePort"; //$NON-NLS-1$
	public static final String SCHEME_CABLE_PORT = "SchemeCablePort"; //$NON-NLS-1$
	public static final String SCHEME_PATH = "SchemePath"; //$NON-NLS-1$
	public static final String SCHEME_PROTO_ELEMENT = "SchemeProtoElement"; //$NON-NLS-1$
	public static final String SCHEME_PROTO_GROUP = "SchemeProtoGroup"; //$NON-NLS-1$
	public static final String SCHEME_TYPE = "SchemeType"; //$NON-NLS-1$
	
	public static final String TEXT_ROOT = Messages.getString("Constants.root"); //$NON-NLS-1$
	public static final String TEXT_EMPTY = ""; //$NON-NLS-1$
	public static final String TEXT_NAME = Messages.getString("Constants.name"); //$NON-NLS-1$
	public static final String TEXT_DESCRIPTION = Messages.getString("Constants.description"); //$NON-NLS-1$
	public static final String TEXT_MANUFACTURER = Messages.getString("Constants.manufacturer"); //$NON-NLS-1$
	public static final String TEXT_MANUFACTURER_CODE = Messages.getString("Constants.manufacturer_code"); //$NON-NLS-1$
	public static final String TEXT_EXISTING_TYPE = Messages.getString("Constants.existing_type"); //$NON-NLS-1$
	public static final String TEXT_NEW_TYPE = Messages.getString("Constants.new_type"); //$NON-NLS-1$
	public static final String TEXT_CHARACTERISTIC = Messages.getString("Constants.characteristic"); //$NON-NLS-1$
	public static final String TEXT_ADD_CHARACTERISTIC = Messages.getString("Constants.add_characteristic"); //$NON-NLS-1$
	public static final String TEXT_REMOVE_CHARACTERISTIC = Messages.getString("Constants.remove_characteristic"); //$NON-NLS-1$
	public static final String TEXT_PARAMETERS = Messages.getString("Constants.parameters"); //$NON-NLS-1$
	public static final String TEXT_MEASUREMENT_PORT_TYPES = Messages.getString("Constants.measurement_port_types"); //$NON-NLS-1$
	public static final String TEXT_PORTS = Messages.getString("Constants.ports"); //$NON-NLS-1$
	public static final String TEXT_TYPE = Messages.getString("Constants.type"); //$NON-NLS-1$
	public static final String TEXT_MEASUREMENT_TYPES = Messages.getString("Constants.measurement_types"); //$NON-NLS-1$
	public static final String TEXT_INPUT = Messages.getString("Constants.input"); //$NON-NLS-1$
	public static final String TEXT_OUTPUT = Messages.getString("Constants.output"); //$NON-NLS-1$
	public static final String TEXT_CHARACTERISTICTYPESORT_OPTICAL = Messages.getString("Constants.optical_characteristics"); //$NON-NLS-1$
	public static final String TEXT_CHARACTERISTICTYPESORT_ELECTRICAL = Messages.getString("Constants.electrical_characteristics"); //$NON-NLS-1$
	public static final String TEXT_CHARACTERISTICTYPESORT_OPERATIONAL = Messages.getString("Constants.operational_characteristics"); //$NON-NLS-1$
	public static final String TEXT_CHARACTERISTICTYPESORT_INTERFACE = Messages.getString("Constants.interface_characteristics"); //$NON-NLS-1$
	public static final String TEXT_CHARACTERISTICTYPESORT_VISUAL = Messages.getString("Constants.visual_attributes"); //$NON-NLS-1$
	public static final String TEXT_PORTTYPESORT_OPTICAL = Messages.getString("Constants.optical"); //$NON-NLS-1$
	public static final String TEXT_PORTTYPESORT_THERMAL = Messages.getString("Constants.thermal"); //$NON-NLS-1$
	public static final String TEXT_PORTTYPESORT_ELECTICAL = Messages.getString("Constants.electrical"); //$NON-NLS-1$
	public static final String TEXT_OK = Messages.getString("Constants.ok"); //$NON-NLS-1$
	public static final String TEXT_CANCEL = Messages.getString("Constants.cancel"); //$NON-NLS-1$

	public static final String TEXT_NETWORK_DIRECTORY = Messages.getString("Constants.network_directory"); //$NON-NLS-1$
	public static final String TEXT_MONITORING_DIRECTORY = Messages.getString("Constants.monitoring_directory"); //$NON-NLS-1$
	public static final String TEXT_CONFIGURATION = Messages.getString("Constants.configuration"); //$NON-NLS-1$
	public static final String TEXT_LINK_TYPE = Messages.getString("Constants.link_type"); //$NON-NLS-1$
	public static final String TEXT_CABLE_LINK_TYPE = Messages.getString("Constants.cable_link_type"); //$NON-NLS-1$
	public static final String TEXT_PORT_TYPE = Messages.getString("Constants.port_type"); //$NON-NLS-1$
	public static final String TEXT_MEASUREMENT_TYPE = Messages.getString("Constants.measurement_type"); //$NON-NLS-1$
	public static final String TEXT_MEASUREMENTPORT_TYPE = Messages.getString("Constants.measurement_port_type"); //$NON-NLS-1$
	public static final String TEXT_SCHEME_TYPE = Messages.getString("Constants.scheme_type"); //$NON-NLS-1$
	public static final String TEXT_SCHEME_TYPE_NETWORK = Messages.getString("Constants.scheme_type_network"); //$NON-NLS-1$
	public static final String TEXT_SCHEME_TYPE_CABLE = Messages.getString("Constants.scheme_type_cable"); //$NON-NLS-1$
	public static final String TEXT_SCHEME_TYPE_BUILDING = Messages.getString("Constants.scheme_type_building"); //$NON-NLS-1$
	public static final String TEXT_SCHEME_PROTO_GROUP = Messages.getString("Constants.scheme_proto_group"); //$NON-NLS-1$

}
