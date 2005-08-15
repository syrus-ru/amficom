/*-
 * $Id: SchemeOptimizeInfoWrapper.java,v 1.8 2005/08/15 15:19:23 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.scheme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/15 15:19:23 $
 * @author $Author: max $
 * @module scheme
 */
public final class SchemeOptimizeInfoWrapper extends StorableObjectWrapper<SchemeOptimizeInfo> {
	
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
	
	public static final String COLUMN_PARENT_SCHEME_ID = "parent_scheme_id";

	private static SchemeOptimizeInfoWrapper instance;
	
	private List<String> keys;
	
	private SchemeOptimizeInfoWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME,
				COLUMN_DESCRIPTION,
				COLUMN_OPTIMIZATION_MODE,
				COLUMN_ITERATIONS,
				COLUMN_PRICE,
				COLUMN_WAVE_LENGTH,
				COLUMN_LEN_MARGIN,
				COLUMN_MUTATION_RATE,
				COLUMN_MUTATION_DEGREE,
				COLUMN_RTU_DELETE_PROB,
				COLUMN_RTU_CREATE_PROB,
				COLUMN_NODES_SPLICE_PROB,
				COLUMN_NODES_CUT_PROB,
				COLUMN_SURVIVOR_RATE,
				COLUMN_PARENT_SCHEME_ID}));
	}
	
	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		return key;
	}

	@Override
	public Class getPropertyClass(String key) {
		final Class clazz = super.getPropertyClass(key);
		if (clazz != null) {
			return clazz;
		}
		if (key.equals(COLUMN_NAME) 
				|| key.equals(COLUMN_DESCRIPTION)) {
			return String.class;
		} else if (key.equals(COLUMN_OPTIMIZATION_MODE)
				|| key.equals(COLUMN_ITERATIONS)) {
			return Integer.class;
		} else if (key.equals(COLUMN_PRICE)
				|| key.equals(COLUMN_WAVE_LENGTH)
				|| key.equals(COLUMN_LEN_MARGIN)
				|| key.equals(COLUMN_MUTATION_RATE)
				|| key.equals(COLUMN_MUTATION_DEGREE)
				|| key.equals(COLUMN_RTU_DELETE_PROB)
				|| key.equals(COLUMN_RTU_CREATE_PROB)
				|| key.equals(COLUMN_NODES_SPLICE_PROB)
				|| key.equals(COLUMN_NODES_CUT_PROB)
				|| key.equals(COLUMN_SURVIVOR_RATE)) {
			return Double.class;
		} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
			return Identifier.class;
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		//there is no property
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		//there is no property
	}

	@Override
	@SuppressWarnings("boxing")
	public Object getValue(SchemeOptimizeInfo schemeOptimizeInfo, String key) {
		final Object value = super.getValue(schemeOptimizeInfo, key);
		if (value != null) {
			return value;
		}
		if (schemeOptimizeInfo != null) {
			if (key.equals(COLUMN_NAME)) {
				return schemeOptimizeInfo.getName();
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				return schemeOptimizeInfo.getDescription();
			} else if (key.equals(COLUMN_OPTIMIZATION_MODE)) {
				return schemeOptimizeInfo.getOptimizationMode();
			} else if (key.equals(COLUMN_ITERATIONS)) {
				return schemeOptimizeInfo.getIterations();
			} else if (key.equals(COLUMN_PRICE)) {
				return schemeOptimizeInfo.getPrice();
			} else if (key.equals(COLUMN_WAVE_LENGTH)) {
				return schemeOptimizeInfo.getWaveLength();
			} else if (key.equals(COLUMN_LEN_MARGIN)) {
				return schemeOptimizeInfo.getLenMargin();
			} else if (key.equals(COLUMN_MUTATION_RATE)) {
				return schemeOptimizeInfo.getMutationRate();
			} else if (key.equals(COLUMN_MUTATION_DEGREE)) {
				return schemeOptimizeInfo.getMutationDegree();
			} else if (key.equals(COLUMN_RTU_DELETE_PROB)) {
				return schemeOptimizeInfo.getRtuDeleteProb();
			} else if (key.equals(COLUMN_RTU_CREATE_PROB)) {
				return schemeOptimizeInfo.getRtuCreateProb();
			} else if (key.equals(COLUMN_NODES_SPLICE_PROB)) {
				return schemeOptimizeInfo.getNodesSpliceProb();
			} else if (key.equals(COLUMN_NODES_CUT_PROB)) {
				return schemeOptimizeInfo.getNodesCutProb();
			} else if (key.equals(COLUMN_SURVIVOR_RATE)) {
				return schemeOptimizeInfo.getSurvivorRate();
			} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
				return schemeOptimizeInfo.getParentScheme();
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return false;
	}

	@Override
	@SuppressWarnings("boxing")
	public void setValue(SchemeOptimizeInfo schemeOptimizeInfo, String key, Object value) {
		if (schemeOptimizeInfo != null) {
			if (key.equals(COLUMN_NAME)) {
				schemeOptimizeInfo.setName((String) value);
			} else if (key.equals(COLUMN_DESCRIPTION)) {
				schemeOptimizeInfo.setDescription((String) value);
			} else if (key.equals(COLUMN_OPTIMIZATION_MODE)) {
				schemeOptimizeInfo.setOptimizationMode((Integer) value);
			} else if (key.equals(COLUMN_ITERATIONS)) {
				schemeOptimizeInfo.setIterations((Integer) value);
			} else if (key.equals(COLUMN_PRICE)) {
				schemeOptimizeInfo.setPrice((Double) value);
			} else if (key.equals(COLUMN_WAVE_LENGTH)) {
				schemeOptimizeInfo.setWaveLength((Double) value);
			} else if (key.equals(COLUMN_LEN_MARGIN)) {
				schemeOptimizeInfo.setLenMargin((Double) value);
			} else if (key.equals(COLUMN_MUTATION_RATE)) {
				schemeOptimizeInfo.setMutationRate((Double) value);
			} else if (key.equals(COLUMN_MUTATION_DEGREE)) {
				schemeOptimizeInfo.setMutationDegree((Double) value);
			} else if (key.equals(COLUMN_RTU_DELETE_PROB)) {
				schemeOptimizeInfo.setRtuDeleteProb((Double) value);
			} else if (key.equals(COLUMN_RTU_CREATE_PROB)) {
				schemeOptimizeInfo.setRtuCreateProb((Double) value);
			} else if (key.equals(COLUMN_NODES_SPLICE_PROB)) {
				schemeOptimizeInfo.setNodesSpliceProb((Double) value);
			} else if (key.equals(COLUMN_NODES_CUT_PROB)) {
				schemeOptimizeInfo.setNodesCutProb((Double) value);
			} else if (key.equals(COLUMN_SURVIVOR_RATE)) {
				schemeOptimizeInfo.setSurvivorRate((Double) value);
			} else if (key.equals(COLUMN_PARENT_SCHEME_ID)) {
				schemeOptimizeInfo.setParentSchemeId((Identifier) value);
			}
		}
	}

	public static SchemeOptimizeInfoWrapper getInstance() {
		if (instance == null)
			instance = new SchemeOptimizeInfoWrapper();
		return instance;
	}
}
