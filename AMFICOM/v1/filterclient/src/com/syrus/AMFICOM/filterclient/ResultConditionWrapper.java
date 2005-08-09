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
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.Result;

/*
 * $Id: ResultConditionWrapper.java,v 1.4 2005/08/09 22:32:35 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/09 22:32:35 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class ResultConditionWrapper {
	private static short entityCode = ObjectEntities.RESULT_CODE;

	private List<Measurement> measurements;
	private List<Analysis> analyses;
	private List<Evaluation> evaluations;
	private List<Modeling> modellings;

	private Map<String, String[]> keyLinkedNames = new HashMap<String, String[]>();
	private Map<Identifier, String> storableObjectInitialName = new HashMap<Identifier, String>();

	private static final String MEASUREMENT = "filter by measurements";
	private static final String ANALYSIS = "filter by analyses";
	private static final String EVALUATION = "filter by evaluation";
	private static final String MODELING = "filter by modeling";

	private static String[] keys = { MEASUREMENT, ANALYSIS, EVALUATION, MODELING };
	private static String[] keyNames = { MEASUREMENT, ANALYSIS, EVALUATION, MODELING };
	private static byte[] keyTypes = { ConditionWrapper.LIST, ConditionWrapper.LIST, ConditionWrapper.LIST, ConditionWrapper.LIST };

	public ResultConditionWrapper(final Set<Result> initialResults,
			final Set<Measurement> measurements,
			final Set<Analysis> analyses,
			final Set<Evaluation> evaluations,
			final Set<Modeling> modellings) {
		this.measurements = new ArrayList<Measurement>(measurements);
		this.analyses = new ArrayList<Analysis>(analyses);
		this.evaluations = new ArrayList<Evaluation>(evaluations);
		this.modellings = new ArrayList<Modeling>(modellings);

		for (final Result result : initialResults) {
			this.storableObjectInitialName.put(result.getId(), "result " + result.getSort());
		}

		final String[] measurementNames = new String[this.measurements.size()];
		int i = 0;
		for (final Measurement measurement : this.measurements) {
			measurementNames[i++] = measurement.getName();
		}
		this.keyLinkedNames.put(keys[0], measurementNames);

		final String[] analysisNames = new String[this.analyses.size()];
		i = 0;
		for (final Analysis analysis : this.analyses) {
			analysisNames[i++] = "analisis" + analysis.getType();
		}
		this.keyLinkedNames.put(keys[1], analysisNames);

		final String[] evaluationNames = new String[this.evaluations.size()];
		i = 0;
		for (final Evaluation evaluation : this.evaluations) {
			evaluationNames[i++] = "evaluation" + evaluation.getType();
		}
		this.keyLinkedNames.put(keys[2], evaluationNames);

		final String[] modelingNames = new String[this.modellings.size()];
		i = 0;
		for (final Modeling modeling : this.modellings) {
			modelingNames[i++] = modeling.getName();
		}
		this.keyLinkedNames.put(keys[3], modelingNames);
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
		else if (key.equals(keys[1])) {
			return this.analyses.get(indexNumber).getId();
		}
		else if (key.equals(keys[2])) {
			return this.evaluations.get(indexNumber).getId();
		}
		else if (key.equals(keys[3])) {
			return this.modellings.get(indexNumber).getId();
		}
		throw new IllegalDataException("ResultConditionWrapper.getLinkedObject | Wrong key");
	}
}
