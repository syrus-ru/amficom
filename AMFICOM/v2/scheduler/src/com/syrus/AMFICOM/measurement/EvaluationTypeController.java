/*
 * $Id: EvaluationTypeController.java,v 1.1.2.1 2004/10/19 09:58:31 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2004/10/19 09:58:31 $
 * @author $Author: bob $
 * @module module
 */
public class EvaluationTypeController implements ObjectResourceController {

	public static final String		KEY_NAME	= "name";

	private List				keys;

	private static boolean			initialized	= false;
	private static EvaluationTypeController	instance	= null;
	private static Object			lock		= new Object();

	private EvaluationTypeController() {

		//		 empty private constructor
		String[] keysArray = new String[] { KEY_NAME};
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EvaluationTypeController getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new EvaluationTypeController();
			}
			synchronized (lock) {
				initialized = true;
			}
		}
		return instance;
	}

	public String getKey(int index) {
		String key = null;
		if (index == 0)
			key = KEY_NAME;
		return key;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		String name = null;
		if (key.equals(KEY_NAME)) {
			name = "name";
		}
		return name;
	}

	public Class getPropertyClass(String key) {
		return String.class;
	}

	public Object getPropertyValue(String key) {
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		// TODO Auto-generated method stub

	}

	public Object getValue(Object object, String key) {
		Object value = null;
		if (object instanceof EvaluationType) {
			EvaluationType evaluationType = (EvaluationType) object;
			value = evaluationType.getDescription();
		}
		return value;
	}

	public boolean isEditable(String key) {
		boolean editable = false;
		if (key.equals(KEY_NAME))
			editable = false;
		return editable;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof EvaluationType) {
			EvaluationType evaluationType = (EvaluationType) object;
		}
	}

}
