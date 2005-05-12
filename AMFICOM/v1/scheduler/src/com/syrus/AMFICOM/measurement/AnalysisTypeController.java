/*
 * $Id: AnalysisTypeController.java,v 1.3 2005/05/12 11:26:27 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/12 11:26:27 $
 * @author $Author: bob $
 * @module module
 */
public class AnalysisTypeController extends ObjectResourceController {

	public static final String		KEY_NAME	= "name";

	private List				keys;

	private static boolean			initialized	= false;
	private static AnalysisTypeController	instance	= null;
	private static Object			lock		= new Object();

	private AnalysisTypeController() {

		//		 empty private constructor
		String[] keysArray = new String[] { KEY_NAME};
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static AnalysisTypeController getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new AnalysisTypeController();
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
		if (object instanceof AnalysisType) {
			AnalysisType analysisType = (AnalysisType) object;
			value = analysisType.getDescription();
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
		if (object instanceof AnalysisType) {
//			AnalysisType analysisType = (AnalysisType) object;
		}
	}

}
