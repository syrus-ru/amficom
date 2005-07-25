/*-
 * $Id: SchemePathWrapper.java,v 1.9 2005/07/25 12:12:33 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/07/25 12:12:33 $
 * @author $Author: bass $
 * @module scheme
 */
public final class SchemePathWrapper extends StorableObjectWrapper {
	public static final String COLUMN_TRANSMISSION_PATH_ID = "transmission_path_id";

	public static final String COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID = "prnt_schm_mntrng_sltn_id";

	private static SchemePathWrapper instance;

	public List<String> getKeys() {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	@Override
	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	@Override
	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public static SchemePathWrapper getInstance() {
		if (instance == null)
			instance = new SchemePathWrapper();
		return instance;
	}
}
