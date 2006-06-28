/*
 * $Id: CronTemporalPatternController.java,v 1.5 2006/03/13 15:54:25 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2006/03/13 15:54:25 $
 * @author $Author: bass $
 * @module module
 */
public class CronTemporalPatternController implements Wrapper {

	public static final String		KEY_NAME		= "NAME";

	private List					keys;

	private static boolean			initialized				= false;
	private static CronTemporalPatternController	instance				= null;
	private static Object			lock					= new Object();

	private CronTemporalPatternController() {

		//		 empty private constructor
		this.keys = Collections.unmodifiableList(Collections.singletonList(KEY_NAME));

	}

	public static CronTemporalPatternController getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new CronTemporalPatternController();
			}
			synchronized (lock) {
				initialized = true;
			}
		}
		return instance;
	}

	public String getKey(int index) {
		return this.keys.get(index).toString();
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		String name = null;
		if (key.equals(KEY_NAME))
			name = I18N.getString("Scheduler.Name"); //$NON-NLS-1$
		return name;
	}

	public Class<?> getPropertyClass(String key) {
		Class<?> clazz = String.class;
		return clazz;
	}

	public Object getPropertyValue(String key) {
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		// TODO Auto-generated method stub

	}

	public Object getValue(Object object, String key) {
		Object value = null;
		if (object instanceof CronTemporalPattern) {
			CronTemporalPattern temporalPattern = (CronTemporalPattern) object;
			if (key.equals(KEY_NAME))
				value = temporalPattern.toString(); //$NON-NLS-1$			
		}
		return value;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof CronTemporalPattern) {
//			CronTemporalPattern temporalPattern = (CronTemporalPattern) object;
		}
	}

}
