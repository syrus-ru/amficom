/*
 * $Id: EventTypeWrapper.java,v 1.1 2005/01/31 13:15:50 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.List;

import com.syrus.AMFICOM.general.Wrapper;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/31 13:15:50 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventTypeWrapper implements Wrapper {

	private static EventTypeWrapper instance;

	private List keys;

	public String getKey(int index) {
		//TODO implement
		return null;
	}

	public List getKeys() {
		//TODO implement
		return null;
	}

	public String getName(String key) {
		//TODO implement
		return null;
	}

	public Class getPropertyClass(String key) {
		//TODO implement
		return null;
	}

	public Object getPropertyValue(String key) {
		//TODO implement
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		//TODO implement

	}

	public Object getValue(Object object, String key) {
		//TODO implement
		return null;
	}

	public boolean isEditable(String key) {
		//TODO implement
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		//TODO implement

	}

}
