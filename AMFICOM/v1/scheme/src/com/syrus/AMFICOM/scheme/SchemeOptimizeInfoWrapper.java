/*-
 * $Id: SchemeOptimizeInfoWrapper.java,v 1.2 2005/05/24 13:58:41 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/24 13:58:41 $
 * @author $Author: bass $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoWrapper extends StorableObjectWrapper {
	
//	schemeoptimizeinfo.sql
//	
//	name VARCHAR2(32 CHAR) NOT NULL,
//	description VARCHAR2(256 CHAR),
//--
//	optimization_mode NUMBER(10) NOT NULL,
//	iterations NUMBER(10) NOT NULL,
//	
//	price BINARY_DOUBLE NOT NULL,
//	wave_length BINARY_DOUBLE NOT NULL,
//	len_margin BINARY_DOUBLE NOT NULL,
//	mutation_rate BINARY_DOUBLE NOT NULL,
//	mutation_degree BINARY_DOUBLE NOT NULL,
//	rtu_delete_prob BINARY_DOUBLE NOT NULL,
//	rtu_create_prob BINARY_DOUBLE NOT NULL,
//	nodes_splice_prob BINARY_DOUBLE NOT NULL,
//	nodes_cut_prob BINARY_DOUBLE NOT NULL,
//	survivor_rate BINARY_DOUBLE NOT NULL,

//	scheme_id VARCHAR2(32 CHAR) NOT NULL,

	public static final String COLUMN_OPTIMIZATION_MODE = "optimization_mode";
	public static final String COLUMN_ITERATIONS = "iterations";
	
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_WAVE_LENGTH = "wave_length";
	public static final String COLUMN_LEN_MARGIN = "len_margin";
	public static final String COLUMN_MUTATION_RATE = "mutation_rate";
	public static final String COLUMN_MUTATION_DEGREE = "mutation_degree";
	public static final String COLUMN_RTU_DELETE_PROB = "rtu_delete_prob";
	public static final String COLUMN_RTU_CREATE_PROB = "rtu_create_prob";
	public static final String COLUMN_NODES_SPLICE_PROB = "nodes_splice_prob";
	public static final String COLUMN_NODES_CUT_PROB = "nodes_cut_prob";
	public static final String COLUMN_SURVIVOR_RATE = "survivor_rate";
	
	public static final String COLUMN_SCHEME_ID = "scheme_id";

	private static SchemeOptimizeInfoWrapper instance;

	public String getKey(int index) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public List getKeys() {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public String getName(String key) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public Class getPropertyClass(String key) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public Object getValue(Object object, String key) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public void setValue(Object object, String key, Object value) {
		throw new UnsupportedOperationException("SchemeOptimizeInfoWrapper | not implemented yet");
	}

	public static SchemeOptimizeInfoWrapper getInstance() {
		if (instance == null)
			instance = new SchemeOptimizeInfoWrapper();
		return instance;
	}
}
