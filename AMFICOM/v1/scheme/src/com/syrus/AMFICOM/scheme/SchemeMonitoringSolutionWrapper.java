/*-
 * $Id: SchemeMonitoringSolutionWrapper.java,v 1.3 2005/06/07 16:32:59 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/07 16:32:59 $
 * @author $Author: bass $
 * @module scheme_v1
 */
public final class SchemeMonitoringSolutionWrapper extends StorableObjectWrapper {
	
//	schememonitoringsolution.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	price_usd NUMBER(10) NOT NULL,
//	scheme_optimize_info_id VARCHAR2(32 CHAR),
	
	public static final String COLUMN_PRICE_USD = "price_usd";
	public static final String COLUMN_SCHEME_OPTIMIZE_INFO_ID = "scheme_optimize_info_id";

	private static SchemeMonitoringSolutionWrapper instance;

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeMonitoringSolutionWrapper | not implemented yet");
	}

	public static SchemeMonitoringSolutionWrapper getInstance() {
		if (instance == null)
			instance = new SchemeMonitoringSolutionWrapper();
		return instance;
	}
}
