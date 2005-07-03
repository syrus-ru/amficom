package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.Result;

/*
 * $Id: ResultConditionWrapper.java,v 1.2 2005/06/17 11:01:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/17 11:01:05 $
 * @author $Author: bass $
 * @module filterclient_v1
 */
public class ResultConditionWrapper {
	private static short entityCode = ObjectEntities.RESULT_CODE;
	
	private ArrayList measurements;
	private ArrayList analyses;
	private ArrayList evaluations;
	private ArrayList modellings;
	
	private Map keyLinkedNames = new HashMap();
	private Map storableObjectInitialName = new HashMap();
	
	private static final String MEASUREMENT = "filter by measurements";
	private static final String ANALYSIS = "filter by analyses";
	private static final String EVALUATION = "filter by evaluation";
	private static final String MODELING = "filter by modeling";
	
	private static String[] keys = {MEASUREMENT, ANALYSIS, EVALUATION, MODELING};
	private static String[] keyNames = {MEASUREMENT, ANALYSIS, EVALUATION, MODELING};
	private static byte[] keyTypes = {ConditionWrapper.LIST, ConditionWrapper.LIST, ConditionWrapper.LIST, ConditionWrapper.LIST};
	
	public ResultConditionWrapper(Collection initialResults,
			Collection measurements, Collection analyses, Collection evaluations, Collection modellings) {
		this.measurements = new ArrayList(measurements);
		this.analyses = new ArrayList(analyses);
		this.evaluations = new ArrayList(evaluations);
		this.modellings = new ArrayList(modellings);
				
		for (Iterator iter = initialResults.iterator(); iter.hasNext();) {
			Result result = (Result) iter.next();
			this.storableObjectInitialName.put(result, "result " + result.getSort());
		}
		
		String[] measurementNames = new String[this.measurements.size()];
		int i=0;
		for (Iterator iter = this.measurements.iterator(); iter.hasNext();i++) {
			Measurement measurement = (Measurement) iter.next();
			measurementNames[i] = measurement.getName();
		}
		this.keyLinkedNames.put(keys[0], measurementNames);
		String[] analysisNames = new String[this.analyses.size()];
		i=0;
		for (Iterator iter = this.analyses.iterator(); iter.hasNext();i++) {
			Analysis analysis = (Analysis) iter.next();
			analysisNames[i] = "analisis" + analysis.getType();
		}
		this.keyLinkedNames.put(keys[0], analysisNames);
		String[] evaluationNames = new String[this.evaluations.size()];
		i=0;
		for (Iterator iter = this.evaluations.iterator(); iter.hasNext();i++) {
			Evaluation evaluation = (Evaluation) iter.next();
			evaluationNames[i] = "evaluation" + evaluation.getType();
		}
		this.keyLinkedNames.put(keys[0], evaluationNames);
		String[] modelingNames = new String[this.modellings.size()];
		i=0;
		for (Iterator iter = this.modellings.iterator(); iter.hasNext();i++) {
			Modeling modeling = (Modeling) iter.next();
			modelingNames[i] = modeling.getName();
		}
		this.keyLinkedNames.put(keys[0], modelingNames);
	}
	
	public String[] getLinkedNames(String key) {
		return (String[]) this.keyLinkedNames.get(key);
	}
	
	public Collection getInitialEntities() {
		return this.storableObjectInitialName.keySet();
	}
	
	public String getInitialName(StorableObject storableObject) {
		return (String) this.storableObjectInitialName.get(storableObject);		
	}
	
	public String[] getKeys() {return keys;}
	public String[] getKeyNames() {return keyNames;}
	public short getEntityCode() {return entityCode;}
	public byte[] getTypes() {return keyTypes;}
	
	public Object getLinkedObject(String key, int indexNumber) throws IllegalDataException {
		if (key.equals(keys[0]))
			return ((Measurement)this.measurements.get(indexNumber)).getId();
		else if (key.equals(keys[1]))
			return ((Analysis)this.analyses.get(indexNumber)).getId();
		else if (key.equals(keys[2]))
			return ((Evaluation)this.evaluations.get(indexNumber)).getId();
		else if (key.equals(keys[3]))
			return ((Modeling)this.modellings.get(indexNumber)).getId();
		throw new IllegalDataException("ResultConditionWrapper.getLinkedObject | Wrong key");
	}
}
