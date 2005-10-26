/**
 * $Id: AlarmWrapper.java,v 1.1 2005/10/26 13:51:56 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.observer.alarm;

import java.util.Collections;
import java.util.List;

import com.syrus.util.Wrapper;

public class AlarmWrapper implements Wrapper {

	public List getKeys() {
		return Collections.emptyList();
	}

	public String getName(String key) {
		return null;
	}

	public Class getPropertyClass(String key) {
		return null;
	}

	public Object getPropertyValue(String key) {
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
	}

	public Object getValue(Object object, String key) {
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
	}

}
