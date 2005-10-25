/*-
 * $Id: DataWrapper.java,v 1.2 2005/10/25 19:53:08 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.util.List;

import com.syrus.util.PropertyChangeException;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:08 $
 * @module report
 */
public class DataWrapper extends StorableElementWrapper<DataStorableElement> {

	public static final String COLUMN_REPORT_NAME = "report_name";
	public static final String COLUMN_MODEL_CLASS_NAME = "model_class_name";
	
	public static final int SIZE_REPORT_NAME_COLUMN = 128;
	public static final int SIZE_MODEL_CLASS_NAME_COLUMN = 128;

	private static DataWrapper instance;

	DataWrapper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(final DataStorableElement data, String key, Object value) throws PropertyChangeException {
		throw new UnsupportedOperationException();
	}

	public List<String> getKeys() {
		throw new UnsupportedOperationException();
	}

	public String getName(String key) {
		throw new UnsupportedOperationException();
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException();
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValue(final DataStorableElement data, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}

	public static DataWrapper getInstance() {
		if (instance == null) {
			instance = new DataWrapper();
		}
		return instance;
	}
}
