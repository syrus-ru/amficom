/*
 * $Id: Constants.java,v 1.3 2005/03/11 16:10:46 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import java.awt.Dimension;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/03/11 16:10:46 $
 * @module schemeclient_v1
 */

public class Constants {
	private Constants () {
		// empty
	}
	public static final Dimension BTN_SIZE = new Dimension(24, 24);
	public static final Dimension TEXT_AREA_SIZE = new Dimension(100, 30);
	public static final int OK = 1;
	public static final int CANCEL = 0;
	
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
	public static final String CHARACTERISTICTYPESORT_OPTICAL = Messages.getString("Constants.optical_characteristics"); //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_ELECTRICAL = Messages.getString("Constants.electrical_characteristics"); //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_OPERATIONAL = Messages.getString("Constants.operational_characteristics"); //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_INTERFACE = Messages.getString("Constants.interface_characteristics"); //$NON-NLS-1$
	public static final String CHARACTERISTICTYPESORT_VISUAL = Messages.getString("Constants.visual_attributes"); //$NON-NLS-1$
	public static final String PORTTYPESORT_OPTICAL = Messages.getString("Constants.optical"); //$NON-NLS-1$
	public static final String PORTTYPESORT_THERMAL = Messages.getString("Constants.thermal"); //$NON-NLS-1$
	public static final String PORTTYPESORT_ELECTICAL = Messages.getString("Constants.electrical"); //$NON-NLS-1$
	public static final String TEXT_OK = Messages.getString("Constants.ok"); //$NON-NLS-1$
	public static final String TEXT_CANCEL = Messages.getString("Constants.cancel"); //$NON-NLS-1$
}
