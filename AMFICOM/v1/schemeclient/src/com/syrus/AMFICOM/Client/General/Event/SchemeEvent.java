/*
 * $Id: SchemeEvent.java,v 1.4 2005/08/01 07:52:27 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import java.beans.PropertyChangeEvent;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/08/01 07:52:27 $
 * @module schemeclient_v1
 */

public class SchemeEvent extends PropertyChangeEvent {
	static final public String TYPE = SchemeEvent.class.getName();
	
	public static final long OPEN_SCHEME = 						0x00000001;
	public static final long OPEN_SCHEMEELEMENT = 		0x00000002;
	public static final long OPEN_PROTOELEMENT = 			0x00000004;
//	public static final long CLOSE_SCHEME = 					0x00000008;
//	public static final long CLOSE_SCHEMEELEMENT = 		0x00000010;
	public static final long SCHEME_CHANGED =					0x00000020;
	
	public static final long UPDATE_OBJECT = 				0x00000100;
	public static final long CREATE_OBJECT = 				0x00000200;
	public static final long DELETE_OBJECT = 				0x00000400;
	public static final long CREATE_ALARMED_LINK = 		0x00100000;
	
	private long type;
	
	public SchemeEvent(Object source, Object object, long type) {
		super(source, TYPE, null, object);
		this.type = type;
	}
	
	public boolean isType(long eventType) {
		return (type & eventType) != 0;
	}

	public Object getObject() {
		return super.getNewValue();
	}
}
