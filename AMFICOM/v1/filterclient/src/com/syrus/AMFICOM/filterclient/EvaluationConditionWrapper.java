/*
 * $Id: EvaluationConditionWrapper.java,v 1.4 2005/08/09 22:32:35 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/09 22:32:35 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class EvaluationConditionWrapper {
	private static short entityCode = ObjectEntities.EVALUATION_CODE;

	private List<Measurement> measurements;

	private Map<String, String[]> keyLinkedNames = new HashMap<String, String[]>();
	private Map<Identifier, String> storableObjectInitialName = new HashMap<Identifier, String>();

	private static final String MEASUREMENT = "filter by measurement";

	private static String[] keys = { MEASUREMENT };
	private static String[] keyNames = { MEASUREMENT };
	private static byte[] keyTypes = { ConditionWrapper.LIST };

	public EvaluationConditionWrapper(final Set<Evaluation> initialEvaluations, final Set<Measurement> measurements) {
		this.measurements = new ArrayList<Measurement>(measurements);

		for (final Evaluation evaluation : initialEvaluations) {
			this.storableObjectInitialName.put(evaluation.getId(), "evaluation " + evaluation.getCreated());
		}

		final String[] measurementNames = new String[this.measurements.size()];
		int i = 0;
		for (final Measurement measurement : this.measurements) {
			measurementNames[i++] = measurement.getName();
		}
		this.keyLinkedNames.put(keys[0], measurementNames);
	}

	public String[] getLinkedNames(final String key) {
		return this.keyLinkedNames.get(key);
	}

	public Set<Identifier> getInitialEntities() {
		return this.storableObjectInitialName.keySet();
	}

	public String getInitialName(final Identifier id) {
		return this.storableObjectInitialName.get(id);
	}

	public String[] getKeys() {
		return keys;
	}

	public String[] getKeyNames() {
		return keyNames;
	}

	public short getEntityCode() {
		return entityCode;
	}

	public byte[] getTypes() {
		return keyTypes;
	}

	public Object getLinkedObject(final String key, final int indexNumber) throws IllegalDataException {
		if (key.equals(keys[0])) {
			return this.measurements.get(indexNumber).getId();
		}
		throw new IllegalDataException("EvaluationConditionWrapper.getLinkedObject | Wrong key");
	}
}
