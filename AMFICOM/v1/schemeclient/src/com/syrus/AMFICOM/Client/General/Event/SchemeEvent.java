/*
 * $Id: SchemeEvent.java,v 1.1 2005/04/05 14:24:05 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:24:05 $
 * @module schemeclient_v1
 */

public class SchemeEvent extends OperationEvent {
	static final public String TYPE = "schemeEvent";
	
	public static final long OPEN_SCHEME = 						0x00000001;
	public static final long OPEN_SCHEMEELEMENT = 		0x00000002;
	public static final long OPEN_PROTOELEMENT = 			0x00000004;
	public static final long CLOSE_SCHEME = 					0x00000008;
	public static final long CLOSE_SCHEMEELEMENT = 		0x00000010;
	public static final long SCHEME_CHANGED =					0x00000020;
//public static final long OPEN_UGO = 							0x00000040;
	
	public static final long UGO_TEXT_UPDATE = 				0x00000100;
	public static final long UGO_ICON_UPDATE = 				0x00000200;
	public static final long CABLE_PORT_NAME_UPDATE = 0x00001000;
	public static final long CABLE_PORT_TYPE_UPDATE = 0x00002000;
	public static final long PORT_NAME_UPDATE = 			0x00004000;
	public static final long PORT_TYPE_UPDATE = 			0x00008000;
	public static final long CABLE_LINK_NAME_UPDATE = 0x00010000;
	public static final long LINK_NAME_UPDATE = 			0x00020000;
	public static final long CREATE_ALARMED_LINK = 		0x00100000;
	public static final long OBJECT_TYPE_UPDATE = 		0x10000000;
	
	private Object object;
	private Object value;
	private long type;
	
	public SchemeEvent(Object source, Object object, Object value, long type) {
		super(source, 0, TYPE);
		this.type = type;
	}
	
	public boolean isType(long eventType) {
		return (type & eventType) != 0;
	}

	public Object getObject() {
		return object;
	}
	
	public Object getValue() {
		return value;
	}
}
