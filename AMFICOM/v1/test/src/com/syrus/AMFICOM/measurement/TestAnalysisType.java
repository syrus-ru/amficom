/*
 * $Id: TestAnalysisType.java,v 1.1 2005/05/13 21:18:44 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/13 21:18:44 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestAnalysisType extends CommonTest {

	public TestAnalysisType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestAnalysisType.class);
	}

	public void testTransferable() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		AnalysisType analysisType = (AnalysisType) MeasurementStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Analysis type: '" + analysisType.getId() + "'");
		AnalysisType_Transferable att = (AnalysisType_Transferable) analysisType.getTransferable();
		AnalysisType analysisType1 = new AnalysisType(att);
		Set inParTypIds = analysisType1.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();)
			System.out.println("IN: '" + it.next() + "'");
	}

	public void testChangeParameterTypes() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		AnalysisType analysisType = (AnalysisType) MeasurementStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Analysis type: '" + analysisType.getId() + "'");

		Set inParTypIds = analysisType.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();) {
			System.out.println("IN: '" + it.next() + "'");
		}
		Set criParTypIds = analysisType.getCriteriaParameterTypeIds();
		for (Iterator it = criParTypIds.iterator(); it.hasNext();) {
			System.out.println("CRI: '" + it.next() + "'");
		}
		Set etaParTypIds = analysisType.getEtalonParameterTypeIds();
		for (Iterator it = etaParTypIds.iterator(); it.hasNext();) {
			System.out.println("ETA: '" + it.next() + "'");
		}
		Set outParTypIds = analysisType.getOutParameterTypeIds();
		for (Iterator it = outParTypIds.iterator(); it.hasNext();) {
			System.out.println("OUT: '" + it.next() + "'");
		}
		Set measTypIds = analysisType.getMeasurementTypeIds();
		for (Iterator it = measTypIds.iterator(); it.hasNext();) {
			System.out.println("MT: '" + it.next() + "'");
		}

		ec = new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
		Set butIds = new HashSet();
		butIds.addAll(inParTypIds);
		butIds.addAll(criParTypIds);
		butIds.addAll(etaParTypIds);
		butIds.addAll(outParTypIds);
		Set parameterTypes = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(butIds, ec, true);
		Set parameterTypeIds = Identifier.getIdentifiers(parameterTypes);
		for (Iterator it = parameterTypeIds.iterator(); it.hasNext();) {
			System.out.println("Loaded: '" + it.next() + "'");
		}

		inParTypIds = Collections.singleton(new Identifier("ParameterType_367"));
		criParTypIds = Collections.EMPTY_SET;
		etaParTypIds = Collections.EMPTY_SET;
		outParTypIds = Collections.EMPTY_SET;
		analysisType.setInParameterTypeIds(inParTypIds);
		analysisType.setCriteriaParameterTypeIds(criParTypIds);
		analysisType.setEtalonParameterTypeIds(etaParTypIds);
		analysisType.setOutParameterTypeIds(outParTypIds);

		measTypIds = Collections.singleton(new Identifier("MeasurementType_19"));
		analysisType.setMeasurementTypeIds(measTypIds);

		MeasurementStorableObjectPool.flush(analysisType.getId(), true);
	}
}
