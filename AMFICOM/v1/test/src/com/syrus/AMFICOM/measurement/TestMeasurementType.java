/*
 * $Id: TestMeasurementType.java,v 1.8 2005/08/20 19:40:40 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/20 19:40:40 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestMeasurementType extends TestCase {

	public TestMeasurementType(String name) {
		super(name);
	}

	public static Test suite() {
		DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestMeasurementType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final MeasurementType measurementType = MeasurementType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				MeasurementType.CODENAME_REFLECTOMETRY,
				"Рефлектометрические измерения",
				Collections.<ParameterType>emptySet(),
				Collections.<ParameterType>emptySet(),
				Collections.<Identifier>emptySet());
		System.out.println("Created: '" + measurementType.getId() + "'");
		StorableObjectPool.flush(measurementType, DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testChangeParameterTypes() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_TYPE_CODE);
		final MeasurementType measurementType = (MeasurementType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Measurement type: '" + measurementType.getId() + "'");

		Set<ParameterType> inParTypes = measurementType.getInParameterTypes();
		for (final ParameterType parameterType : inParTypes) {
			System.out.println("IN: '" + parameterType.getCodename() + "', '" + parameterType.getDescription() + "'");
		}
		Set<ParameterType> outParTypes = measurementType.getOutParameterTypes();
		for (final ParameterType parameterType : outParTypes) {
			System.out.println("OUT: '" + parameterType.getCodename() + "', '" + parameterType.getDescription() + "'");
		}

		final Set<Identifier> measPortTypIds = measurementType.getMeasurementPortTypeIds();
		for (final Identifier id : measPortTypIds) {
			System.out.println("Port type: '" + id + "'");
		}

		inParTypes = new HashSet<ParameterType>();
		inParTypes.add(ParameterType.REF_WAVE_LENGTH);
		inParTypes.add(ParameterType.REF_TRACE_LENGTH);
		inParTypes.add(ParameterType.REF_RESOLUTION);
		inParTypes.add(ParameterType.REF_PULSE_WIDTH_HIGH_RES);
		inParTypes.add(ParameterType.REF_PULSE_WIDTH_LOW_RES);
		inParTypes.add(ParameterType.REF_INDEX_OF_REFRACTION);
		inParTypes.add(ParameterType.REF_AVERAGE_COUNT);
		inParTypes.add(ParameterType.REF_FLAG_GAIN_SPLICE_ON);
		inParTypes.add(ParameterType.REF_FLAG_LIFE_FIBER_DETECT);

		outParTypes = new HashSet<ParameterType>();
		outParTypes.add(ParameterType.REFLECTOGRAMMA);

		measurementType.setInParameterTypes(inParTypes);
		measurementType.setOutParameterTypes(outParTypes);

		StorableObjectPool.flush(measurementType.getId(), DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testChangeMeasurementPortTypes() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
		final Set measurementPortTypes = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final MeasurementPortType measurementPortType = (MeasurementPortType) measurementPortTypes.iterator().next();
		System.out.println("Measurement port type: '" + measurementPortType.getId() + "'");

		ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_TYPE_CODE);
		final Set measurementTypes = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final MeasurementType measurementType = (MeasurementType) measurementTypes.iterator().next();
		System.out.println("Measurement type: '" + measurementType.getId() + "'");

		measurementType.setMeasurementPortTypeIds(Collections.singleton(measurementPortType.getId()));

		StorableObjectPool.flush(measurementType, DatabaseCommonTest.getSysUser().getId(), false);
	}
}
