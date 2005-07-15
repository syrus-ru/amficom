/*
 * $Id: TestAnalysisType.java,v 1.6 2005/07/15 12:05:25 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodename;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.6 $, $Date: 2005/07/15 12:05:25 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestAnalysisType extends TestCase {

	public TestAnalysisType(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestAnalysisType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		AnalysisType analysisType = AnalysisType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				AnalysisType.CODENAME_DADARA,
				"Анализ рефлектограмм",
				Collections.<Identifier> emptySet(),
				Collections.<Identifier> emptySet(),
				Collections.<Identifier> emptySet(),
				Collections.<Identifier> emptySet(),
				Collections.<Identifier> emptySet());
		StorableObjectPool.flush(analysisType, false);
	}

	public void testChangeParameterTypes() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ANALYSIS_TYPE_CODE);
		AnalysisType analysisType = (AnalysisType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Analysis type: '" + analysisType.getId() + "'");

		Set<Identifier> inParTypIds = analysisType.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();) {
			System.out.println("IN: '" + it.next() + "'");
		}
		Set<Identifier> criParTypIds = analysisType.getCriteriaParameterTypeIds();
		for (Iterator it = criParTypIds.iterator(); it.hasNext();) {
			System.out.println("CRI: '" + it.next() + "'");
		}
		Set<Identifier> etaParTypIds = analysisType.getEtalonParameterTypeIds();
		for (Iterator it = etaParTypIds.iterator(); it.hasNext();) {
			System.out.println("ETA: '" + it.next() + "'");
		}
		Set<Identifier> outParTypIds = analysisType.getOutParameterTypeIds();
		for (Iterator it = outParTypIds.iterator(); it.hasNext();) {
			System.out.println("OUT: '" + it.next() + "'");
		}
		Set<Identifier> measTypIds = analysisType.getMeasurementTypeIds();
		for (Iterator it = measTypIds.iterator(); it.hasNext();) {
			System.out.println("MT: '" + it.next() + "'");
		}

		ec = new EquivalentCondition(ObjectEntities.PARAMETER_TYPE_CODE);
		final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(ec, true);
		final Set<Identifier> parameterTypeIds = Identifier.createIdentifiers(parameterTypes);
		for (final Identifier id : parameterTypeIds) {
			System.out.println("Loaded: '" + id + "'");
		}

		TypicalCondition tc = new TypicalCondition(ParameterTypeCodename.REFLECTOGRAMMA.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		inParTypIds = Collections.singleton(((ParameterType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next()).getId());
		criParTypIds = Collections.emptySet();
		etaParTypIds = Collections.emptySet();
		outParTypIds = Collections.emptySet();
//		Iterator it = parameterTypeIds.iterator();
//		int i;
//
//		inParTypIds = new HashSet(inParTypIds);
//		i = 0;
//		for (; it.hasNext() && i < 3; i++)
//			inParTypIds.add(it.next());
//		criParTypIds = new HashSet(criParTypIds);
//		i = 0;
//		for (; it.hasNext() && i < 3; i++)
//			criParTypIds.add(it.next());
//		etaParTypIds = new HashSet(etaParTypIds);
//		i = 0;
//		for (; it.hasNext() && i < 3; i++)
//			etaParTypIds.add(it.next());
//		outParTypIds = new HashSet(outParTypIds);
//		i = 0;
//		for (; it.hasNext() && i < 3; i++)
//			outParTypIds.add(it.next());
		
		analysisType.setInParameterTypeIds(inParTypIds);
		analysisType.setCriteriaParameterTypeIds(criParTypIds);
		analysisType.setEtalonParameterTypeIds(etaParTypIds);
		analysisType.setOutParameterTypeIds(outParTypIds);

		measTypIds = Collections.singleton(new Identifier("MeasurementType_19"));
		analysisType.setMeasurementTypeIds(measTypIds);

		StorableObjectPool.flush(analysisType.getId(), true);
	}
}
