/*
 * $Id: TemporalPatternController.java,v 1.1.2.1 2004/12/16 12:48:25 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2004/12/16 12:48:25 $
 * @author $Author: bob $
 * @module module
 */
public class TemporalPatternController implements ObjectResourceController {

	public static final String		KEY_NAME		= "NAME";

	private List					keys;

	private static boolean			initialized				= false;
	private static TemporalPatternController	instance				= null;
	private static Object			lock					= new Object();

	private TemporalPatternController() {

		//		 empty private constructor
		this.keys = Collections.unmodifiableList(Collections.singletonList(KEY_NAME));

	}

	public static TemporalPatternController getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new TemporalPatternController();
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
			name = LangModelSchedule.getString("Name"); //$NON-NLS-1$
		return name;
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
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
		if (object instanceof TemporalPattern) {
			TemporalPattern temporalPattern = (TemporalPattern) object;
			if (key.equals(KEY_NAME))
				value = temporalPattern.toString(); //$NON-NLS-1$			
		}
		return value;
	}

	public boolean isEditable(String key) {
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof TemporalPattern) {
			TemporalPattern temporalPattern = (TemporalPattern) object;
		}
	}

}
