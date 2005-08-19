/*
 * $Id: TestAnalysisType.java,v 1.7 2005/08/19 15:55:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/19 15:55:21 $
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
		final AnalysisType analysisType = AnalysisType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				AnalysisType.CODENAME_DADARA,
				"Анализ рефлектограмм",
				Collections.<ParameterType> emptySet(),
				Collections.<ParameterType> emptySet(),
				Collections.<ParameterType> emptySet(),
				Collections.<ParameterType> emptySet(),
				Collections.<Identifier> emptySet());
		StorableObjectPool.flush(analysisType, DatabaseCommonTest.getSysUser().getId(), false);
	}

	public void testChangeParameterTypes() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ANALYSIS_TYPE_CODE);
		final AnalysisType analysisType = (AnalysisType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Analysis type: '" + analysisType.getId() + "'");

		Set<ParameterType> inParTypes = analysisType.getInParameterTypes();
		for (final ParameterType parameterType : inParTypes) {
			System.out.println("IN: '" + parameterType.getCodename() + "', '" + parameterType.getDescription() + "'");
		}
		Set<ParameterType> criParTypes = analysisType.getCriteriaParameterTypes();
		for (final ParameterType parameterType : criParTypes) {
			System.out.println("CRI: '" + parameterType.getCodename() + "', '" + parameterType.getDescription() + "'");
		}
		Set<ParameterType> etaParTypes = analysisType.getEtalonParameterTypes();
		for (final ParameterType parameterType : etaParTypes) {
			System.out.println("ETA: '" + parameterType.getCodename() + "', '" + parameterType.getDescription() + "'");
		}
		Set<ParameterType> outParTypes = analysisType.getOutParameterTypes();
		for (final ParameterType parameterType : outParTypes) {
			System.out.println("OUT: '" + parameterType.getCodename() + "', '" + parameterType.getDescription() + "'");
		}

		Set<Identifier> measTypIds = analysisType.getMeasurementTypeIds();
		for (Iterator it = measTypIds.iterator(); it.hasNext();) {
			System.out.println("MT: '" + it.next() + "'");
		}

		inParTypes = new HashSet<ParameterType>();
		inParTypes.add(ParameterType.REFLECTOGRAMMA);
		inParTypes.remove(ParameterType.DADARA_ALARMS);
		criParTypes = new HashSet<ParameterType>();
		criParTypes.add(ParameterType.DADARA_CRITERIA);
		etaParTypes = new HashSet<ParameterType>();
		etaParTypes.add(ParameterType.DADARA_ETALON);
		outParTypes = new HashSet<ParameterType>();
		outParTypes.add(ParameterType.DADARA_ANALYSIS_RESULT);
		outParTypes.add(ParameterType.DADARA_ALARMS);
		
		analysisType.setInParameterTypes(inParTypes);
		analysisType.setCriteriaParameterTypes(criParTypes);
		analysisType.setEtalonParameterTypes(etaParTypes);
		analysisType.setOutParameterTypes(outParTypes);

		StorableObjectPool.flush(analysisType, DatabaseCommonTest.getSysUser().getId(), true);
	}

	public void testChangeMeasurementTypes() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ANALYSIS_TYPE_CODE);
		final AnalysisType analysisType = (AnalysisType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Analysis type: '" + analysisType.getId() + "'");

		ec = new EquivalentCondition(ObjectEntities.MEASUREMENT_TYPE_CODE);
		final MeasurementType measurementType = (MeasurementType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();

		analysisType.setMeasurementTypeIds(Collections.singleton(measurementType.getId()));

		StorableObjectPool.flush(analysisType, DatabaseCommonTest.getSysUser().getId(), true);
	}
}
