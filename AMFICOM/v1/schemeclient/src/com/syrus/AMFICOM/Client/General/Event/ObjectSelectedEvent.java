/*-
 * $Id: ObjectSelectedEvent.java,v 1.1 2005/04/05 14:24:05 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.client_.general.ui_.VisualManager;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:24:05 $
 * @module schemeclient_v1
 */

public class ObjectSelectedEvent extends OperationEvent {
	public static final long PORT_TYPE = 						0x00000001;
	public static final long MEASUREMENTPORT_TYPE = 0x00000002;
	public static final long MEASUREMENT_TYPE = 		0x00000004;
	public static final long LINK_TYPE = 						0x00000008;
	public static final long CABLELINK_TYPE = 			0x00000010;
	
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
	
	public static final long OTHER_OBJECT =					0x00100000;
	
	public static final String TYPE = "objectselected";
	private long type;
	private VisualManager manager;
	private Object selectedObject;
	
	public ObjectSelectedEvent(Object source,	Object selectedObject, VisualManager manager, long type) {
		super(source, 0, TYPE);
		this.type = type;
		this.manager = manager;
		this.selectedObject = selectedObject;
	}

	public boolean isSelected(long value) {
		return (type & value) != 0;
	}
	
	public VisualManager getVisualManager() {
		return manager;
	}
	
	public Object getSelectedObject() {
		return selectedObject;
	}
}
