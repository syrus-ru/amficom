/*-
 * $Id: SchemePathWrapper.java,v 1.1 2005/04/22 16:23:00 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/22 16:23:00 $
 * @author $Author: max $
 * @module scheme_v1
 */
public class SchemePathWrapper {
	
	//TODO: create a table in the database
	
	public static final String COLUMN_START_SCHEME_ELEMENT_ID = "start_scheme_element_id";
	public static final String COLUMN_END_SCHEME_ELEMENT_ID = "end_scheme_element_id";
	//TODO: Wrong colunm name, too many characters
	public static final String COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID = "parent_scheme_monitoring_solution_id";
	public static final String COLUMN_TRANSMISSION_PATH_ID = "transmission_path_id";
	
	public String getKey(int index) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public List getKeys() {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemePathWrapper | not implemented yet");
	}
}
