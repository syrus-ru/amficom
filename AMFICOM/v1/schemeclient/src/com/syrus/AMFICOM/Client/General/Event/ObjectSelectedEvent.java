/*-
 * $Id: ObjectSelectedEvent.java,v 1.12 2006/06/01 14:29:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.Identifiable;

/**
 * @author $Author: stas $
 * @version $Revision: 1.12 $, $Date: 2006/06/01 14:29:21 $
 * @module schemeclient
 */

public class ObjectSelectedEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = -4308063227316813319L;

	public static final long PORT_TYPE = 						0x00000001;
	public static final long CABLEPORT_TYPE = 			0x00000040;
	public static final long MEASUREMENTPORT_TYPE = 0x00000002;
//	public static final long MEASUREMENT_TYPE = 		0x00000004;
	public static final long LINK_TYPE = 						0x00000008;
	public static final long CABLELINK_TYPE = 			0x00000010;
	public static final long PROTO_EQUIPMENT = 			0x00000020;
	
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
	
	public static final long MEASUREMENT =					0x00040000;

	public static final long OTHER_OBJECT =					0x00100000;
	public static final long ALL_DESELECTED =				0x00200000;
	
	public static final long MULTIPLE =							0x10000000;
	public static final long INRACK =								0x20000000;
	public static final long INSURE_VISIBLE =				0x40000000;
	
	
	public static final String TYPE = ObjectSelectedEvent.class.getName();
	private long type;
	private VisualManager manager;
	
	public ObjectSelectedEvent(Object source,	Set<Identifiable> selectedObjects, VisualManager manager, long type) {
		super(source, TYPE, null, selectedObjects);
		this.type = type;
		this.manager = manager;
	}
	
	public ObjectSelectedEvent(Object source,	Identifiable selectedObject, VisualManager manager, long type) {
		this(source, Collections.<Identifiable>singleton(selectedObject), manager, type);
	}

	public boolean isSelected(long value) {
		return (this.type & value) != 0;
	}
	
	public VisualManager getVisualManager() {
		return this.manager;
	}
	
	public Set<Identifiable> getSelectedObjects() {
		return (Set<Identifiable>) getNewValue();
	}
	
	public Identifiable getSelectedObject() {
		Set<Identifiable> identifiables = getSelectedObjects();
		if (identifiables.isEmpty()) {
			return null;
		}
		return identifiables.iterator().next();
	}
}
