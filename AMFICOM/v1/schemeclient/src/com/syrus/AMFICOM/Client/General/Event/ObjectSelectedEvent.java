/*-
 * $Id: ObjectSelectedEvent.java,v 1.7 2005/08/08 11:58:06 arseniy Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.client.UI.VisualManager;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
 */

public class ObjectSelectedEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = -4308063227316813319L;

	public static final long PORT_TYPE = 						0x00000001;
	public static final long CABLEPORT_TYPE = 			0x00000040;
	public static final long MEASUREMENTPORT_TYPE = 0x00000002;
	public static final long MEASUREMENT_TYPE = 		0x00000004;
	public static final long LINK_TYPE = 						0x00000008;
	public static final long CABLELINK_TYPE = 			0x00000010;
	public static final long EQUIPMENT_TYPE = 			0x00000020;
	
	public static final long SCHEME_PROTOELEMENT =	0x00000100;
	public static final long SCHEME_PROTOGROUP =		0x00000200;
	public static final long SCHEME =								0x00000400;
	public static final long SCHEME_ELEMENT =				0x00000800;
	public static final long SCHEME_DEVICE =				0x00001000;
	public static final long SCHEME_PORT =					0x00002000;
	public static final long SCHEME_CABLEPORT = 		0x00004000;
	public static final long SCHEME_LINK =					0x00008000;
	public static final long SCHEME_CABLELINK =			0x00010000;
	public static final long SCHEME_PATH =					0x00020000;
	
	public static final long RESULT =								0x00040000;
	
	public static final long OTHER_OBJECT =					0x00100000;
	public static final long ALL_DESELECTED =				0x10000000;
	
	public static final String TYPE = ObjectSelectedEvent.class.getName();
	private long type;
	private VisualManager manager;
	
	public ObjectSelectedEvent(Object source,	Object selectedObject, VisualManager manager, long type) {
		super(source, TYPE, null, selectedObject);
		this.type = type;
		this.manager = manager;
	}

	public boolean isSelected(long value) {
		return (this.type & value) != 0;
	}
	
	public VisualManager getVisualManager() {
		return this.manager;
	}
	
	public Object getSelectedObject() {
		return getNewValue();
	}
}
