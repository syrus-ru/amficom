/*
 * $Id: TestMeasurementType.java,v 1.6 2005/07/07 18:15:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodename;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.6 $, $Date: 2005/07/07 18:15:21 $
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
				Collections.<Identifier>emptySet(),
				Collections.<Identifier>emptySet(),
				Collections.<Identifier>emptySet());
		System.out.println("Created: '" + measurementType.getId() + "'");
		StorableObjectPool.flush(measurementType, false);
	}

	public void testChangeParameterTypes() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_TYPE_CODE);
		final MeasurementType measurementType = (MeasurementType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Measurement type: '" + measurementType.getId() + "'");

		Set<Identifier> inParTypIds = measurementType.getInParameterTypeIds();
		for (final Identifier id : inParTypIds) {
			System.out.println("IN: '" + id + "'");
		}

		Set<Identifier> outParTypIds = measurementType.getOutParameterTypeIds();
		for (final Identifier id : outParTypIds) {
			System.out.println("OUT: '" + id + "'");
		}

		final Set<Identifier> measPortTypIds = measurementType.getMeasurementPortTypeIds();
		for (final Identifier id : measPortTypIds) {
			System.out.println("Port type: '" + id + "'");
		}

		TypicalCondition tc = new TypicalCondition(ParameterTypeCodename.TRACE_WAVELENGTH.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodename.TRACE_LENGTH.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		CompoundCondition cc = new CompoundCondition(tc, CompoundConditionSort.OR, tc1);
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.TRACE_RESOLUTION.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.TRACE_PULSE_WIDTH_HIGH_RES.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.TRACE_PULSE_WIDTH_LOW_RES.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.TRACE_INDEX_OF_REFRACTION.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.TRACE_AVERAGE_COUNT.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.TRACE_FLAG_GAIN_SPLICE_ON.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.TRACE_FLAG_LIVE_FIBER_DETECT.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodename.REFLECTOGRAMMA.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		final Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		final Map<String, Identifier> parTypeIdsCodename = new HashMap<String, Identifier>();
		for (final Iterator it = parameterTypes.iterator(); it.hasNext();) {
			final ParameterType parameterType = (ParameterType) it.next();
			parTypeIdsCodename.put(parameterType.getCodename(), parameterType.getId());
		}
		
		inParTypIds = new HashSet<Identifier>();
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_WAVELENGTH.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_LENGTH.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_RESOLUTION.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_PULSE_WIDTH_HIGH_RES.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_PULSE_WIDTH_LOW_RES.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_INDEX_OF_REFRACTION.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_AVERAGE_COUNT.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_FLAG_GAIN_SPLICE_ON.stringValue()));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.TRACE_FLAG_LIVE_FIBER_DETECT.stringValue()));

		outParTypIds = new HashSet<Identifier>();
		outParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodename.REFLECTOGRAMMA.stringValue()));

		measurementType.setInParameterTypeIds(inParTypIds);
		measurementType.setOutParameterTypeIds(outParTypIds);

		StorableObjectPool.flush(measurementType.getId(), false);
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

		StorableObjectPool.flush(measurementType, false);
	}
}
