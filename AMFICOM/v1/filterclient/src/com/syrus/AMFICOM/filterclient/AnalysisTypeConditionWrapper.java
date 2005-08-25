package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.9 $, $Date: 2005/08/25 10:56:12 $
 * @author $Author: max $
 * @module filterclient
 */
public class AnalysisTypeConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.ANALYSIS_TYPE_CODE;

	private ArrayList<MeasurementType> measurementTypes;

	private Map<String, String[]> keyLinkedNames = new HashMap<String, String[]>();
	private Map<Identifier, String> storableObjectInitialName = new HashMap<Identifier, String>();

	private static final String PARAM = "filter by parameter types";
	private static final String MT = "filter by measurement types";

	private static String[] keys = { PARAM, MT };
	private static String[] keyNames = { PARAM, MT };
	private static byte[] keyTypes = { ConditionWrapper.LIST, ConditionWrapper.LIST };

	public AnalysisTypeConditionWrapper(final Set<AnalysisType> initialAnalysisTypes, final Set<MeasurementType> measurementTypes) {
		this.measurementTypes = new ArrayList<MeasurementType>(measurementTypes);

		for (final AnalysisType analysisType : initialAnalysisTypes) {
			this.storableObjectInitialName.put(analysisType.getId(), analysisType.getDescription());
		}

		final String[] mtNames = new String[this.measurementTypes.size()];
		int i = 0;
		for (final MeasurementType measurementType : this.measurementTypes) {
			mtNames[i++] = measurementType.getDescription();
		}
		this.keyLinkedNames.put(keys[1], mtNames);
	}

	public String[] getLinkedNames(String key) {
		return this.keyLinkedNames.get(key);
	}

	public Set<Identifier> getInitialEntities() {
		return this.storableObjectInitialName.keySet();
	}

	public String getInitialName(final Identifier id) {
		return this.storableObjectInitialName.get(id);
	}

	public List<ConditionKey> getKeys() {
		return Collections.emptyList();
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
		if (key.equals(keys[1])) {
			return this.measurementTypes.get(indexNumber).getId();
		}
		throw new IllegalDataException("AnalysisTypeConditionWrapper.getLinkedObject | Wrong key");
	}
}
