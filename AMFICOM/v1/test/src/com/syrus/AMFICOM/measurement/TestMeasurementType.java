/*
 * $Id: TestMeasurementType.java,v 1.2 2005/05/16 17:44:04 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/16 17:44:04 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestMeasurementType extends CommonTest {

	public TestMeasurementType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestMeasurementType.class);
	}

	public void testTransferable() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
		MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Measurement type: '" + measurementType.getId() + "'");

		MeasurementType_Transferable mtt = (MeasurementType_Transferable) measurementType.getTransferable();
		MeasurementType measurementType1 = new MeasurementType(mtt);

		Set inParTypIds = measurementType1.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();)
			System.out.println("IN: '" + it.next() + "'");

		Set measPortTypIds = measurementType1.getMeasurementPortTypeIds();
		for (Iterator it = measPortTypIds.iterator(); it.hasNext();)
			System.out.println("Port type: '" + it.next() + "'");
	}

	public void testChangeParameterTypes() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
		MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Measurement type: '" + measurementType.getId() + "'");

		Set inParTypIds = measurementType.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();)
			System.out.println("IN: '" + it.next() + "'");

		Set outParTypIds = measurementType.getOutParameterTypeIds();
		for (Iterator it = outParTypIds.iterator(); it.hasNext();)
			System.out.println("OUT: '" + it.next() + "'");

		Set measPortTypIds = measurementType.getMeasurementPortTypeIds();
		for (Iterator it = measPortTypIds.iterator(); it.hasNext();)
			System.out.println("Port type: '" + it.next() + "'");

		TypicalCondition tc = new TypicalCondition(ParameterTypeCodenames.TRACE_WAVELENGTH,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodenames.TRACE_LENGTH,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		CompoundCondition cc = new CompoundCondition(tc, CompoundConditionSort.OR, tc1);
		cc.addCondition(new TypicalCondition(ParameterTypeCodenames.TRACE_RESOLUTION,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodenames.TRACE_PULSE_WIDTH,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodenames.TRACE_AVERAGE_COUNT,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodenames.TRACE_FLAG_GAIN_SPLICE_ON,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodenames.TRACE_FLAG_LIVE_FIBER_DETECT,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		cc.addCondition(new TypicalCondition(ParameterTypeCodenames.REFLECTOGRAMMA,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME));
		Set parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(cc, true);
		Map parTypeIdsCodename = new HashMap();
		for (Iterator it = parameterTypes.iterator(); it.hasNext();) {
			final ParameterType parameterType = (ParameterType) it.next();
			parTypeIdsCodename.put(parameterType.getCodename(), parameterType.getId());
		}
		
		inParTypIds = new HashSet();
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_WAVELENGTH));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_LENGTH));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_RESOLUTION));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_PULSE_WIDTH));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_INDEX_OF_REFRACTION));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_AVERAGE_COUNT));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_FLAG_GAIN_SPLICE_ON));
		inParTypIds.add(parTypeIdsCodename.get(ParameterTypeCodenames.TRACE_FLAG_LIVE_FIBER_DETECT));
		outParTypIds = Collections.singleton(parTypeIdsCodename.get(ParameterTypeCodenames.REFLECTOGRAMMA));

		measurementType.setInParameterTypeIds(inParTypIds);
		measurementType.setOutParameterTypeIds(outParTypIds);

		MeasurementStorableObjectPool.flush(measurementType.getId(), false);
	}
}
