/*
 * $Id: Constants.java,v 1.1 2005/03/17 14:45:36 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Toolkit;

import javax.swing.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/17 14:45:36 $
 * @module schemeclient_v1
 */

public class Constants {
	private Constants() {
		// empty
	}
	public static final Icon CATALOG_ICON = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")); //$NON-NLS-1$
	public static final Icon SCHEME_ICON = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")); //$NON-NLS-1$
	
	public static final String ROOT = "root"; //$NON-NLS-1$
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
	public static final String LINK_TYPE = "LinkType"; //$NON-NLS-1$
	public static final String CABLE_LINK_TYPE = "CableLinkType"; //$NON-NLS-1$
	public static final String PORT_TYPE = "PortType"; //$NON-NLS-1$
	public static final String EQUIPMENT_TYPE = "EquipmentType"; //$NON-NLS-1$
	public static final String MEASUREMENT_TYPE = "MeasurementType"; //$NON-NLS-1$
	public static final String MEASUREMENTPORT_TYPE = "MeasurementPortType"; //$NON-NLS-1$
	public static final String CONFIGURATION = "Configuration"; //$NON-NLS-1$
	public static final String NETWORK_DIRECTORY = "NetworkDirectory"; //$NON-NLS-1$
	public static final String MONITORING_DIRECTORY = "MonitoringDirectory"; //$NON-NLS-1$
	
	public static final String TEXT_ROOT = Messages.getString("Constants.root"); //$NON-NLS-1$
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
