/*
 * $Id: SchemeEvent.java,v 1.10 2005/10/12 10:08:40 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

import java.awt.Point;
import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2005/10/12 10:08:40 $
 * @module schemeclient
 */

public class SchemeEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = 8625207232243614595L;

	static final public String TYPE = SchemeEvent.class.getName();
	
	public static final long OPEN_SCHEME = 						0x00000001;
	public static final long OPEN_SCHEMEELEMENT = 		0x00000002;
	public static final long OPEN_PROTOELEMENT = 			0x00000004;
	public static final long OPEN_PROTOELEMENT_ASCOPY = 0x00001000;
	
	public static final long INSERT_SCHEME = 						0x00000010;
	public static final long INSERT_SCHEMEELEMENT = 		0x00000020;
	public static final long INSERT_PROTOELEMENT = 			0x00000040;
	public static final long INSERT_SCHEME_CABLELINK = 	0x00000080;
	
//	public static final long CLOSE_SCHEME = 					0x00000008;
//	public static final long CLOSE_SCHEMEELEMENT = 		0x00000010;
//	public static final long SCHEME_CHANGED =					0x10000000;
	
	public static final long UPDATE_OBJECT = 				0x00000100;
	public static final long CREATE_OBJECT = 				0x00000200;
	public static final long DELETE_OBJECT = 				0x00000400;
	public static final long CREATE_ALARMED_LINK = 		0x00100000;
	
	private long type;
	private Point p;
	
	public SchemeEvent(Object source, Identifier object, long type) {
		this(source, object, null, type);
	}
	
	public SchemeEvent(Object source, Identifier object, Point p, long type) {
		super(source, TYPE, null, object);
		this.type = type;
		this.p = p;
	}
	
	public boolean isType(long eventType) {
		return (this.type & eventType) != 0;
	}

	public Identifier getIdentifier() {
		return (Identifier)super.getNewValue();
	}
	
	public StorableObject getStorableObject() throws ApplicationException {
		return StorableObjectPool.getStorableObject(getIdentifier(), true);
	}
	
	public Point getInsertionPoint() {
		return this.p;
	}
}
