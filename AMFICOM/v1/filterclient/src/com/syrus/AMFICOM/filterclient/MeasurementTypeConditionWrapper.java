/*
 * $Id: MeasurementTypeConditionWrapper.java,v 1.12 2005/08/25 10:56:12 max Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.12 $, $Date: 2005/08/25 10:56:12 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class MeasurementTypeConditionWrapper implements ConditionWrapper {
	
	private static short entityCode = ObjectEntities.MEASUREMENT_TYPE_CODE;

	private List<MeasurementPortType> measurementPortTypes;

	private Map<String, String[]> keyLinkedNames = new HashMap<String, String[]>();
	private Map<Identifier, String> storableObjectInitialName = new HashMap<Identifier, String>();

	private static final String CODENAME = "filter by codename";
	private static final String PORTTYPE = "filter by MeasurementPortTypes";

	private static String[] keys = { StorableObjectWrapper.COLUMN_CODENAME, PORTTYPE };
	private static String[] keyNames = { CODENAME, PORTTYPE };
	private static byte[] keyTypes = { ConditionWrapper.STRING, ConditionWrapper.LIST };

	public MeasurementTypeConditionWrapper(final Set<MeasurementType> initialMeasurementType,
			final Set<MeasurementPortType> measurementPortTypes) {

		this.measurementPortTypes = new ArrayList<MeasurementPortType>(measurementPortTypes);

		for (final MeasurementType measurementType : initialMeasurementType) {
			this.storableObjectInitialName.put(measurementType.getId(), measurementType.getDescription());
		}

		final String[] mtNames = new String[this.measurementPortTypes.size()];
		int i = 0;
		for (final MeasurementPortType measurementPortType : this.measurementPortTypes) {
			mtNames[i++] = measurementPortType.getName();
		}
		this.keyLinkedNames.put(keys[1], mtNames);
	}

	public String[] getLinkedNames(final String key) {
		return this.keyLinkedNames.get(key);
	}

	public Object getLinkedObject(final String key, final int indexNumber) throws IllegalDataException {
		if (key.equals(keys[1])) {
			return this.measurementPortTypes.get(indexNumber).getId();
		}
		throw new IllegalDataException("MeasurementTypeConditionWrapper.getLinkedObject | Wrong key");
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
}
