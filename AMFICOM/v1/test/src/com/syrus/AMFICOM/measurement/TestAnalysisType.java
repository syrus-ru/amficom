/*
 * $Id: TestAnalysisType.java,v 1.4 2005/06/19 18:43:56 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/19 18:43:56 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestAnalysisType extends DatabaseCommonTest {

	public TestAnalysisType(String name) {
		super(name);
	}

	public static Test suite() {
		addTestSuite(TestAnalysisType.class);
		return createTestSetup();
	}

	public void testTransferable() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ANALYSIS_TYPE_CODE);
		AnalysisType analysisType = (AnalysisType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Analysis type: '" + analysisType.getId() + "'");
		AnalysisType_Transferable att = (AnalysisType_Transferable) analysisType.getTransferable();
		AnalysisType analysisType1 = new AnalysisType(att);
		Set inParTypIds = analysisType1.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();)
			System.out.println("IN: '" + it.next() + "'");
	}

	public void testChangeParameterTypes() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.ANALYSIS_TYPE_CODE);
		AnalysisType analysisType = (AnalysisType) StorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
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

		ec = new EquivalentCondition(ObjectEntities.PARAMETER_TYPE_CODE);
		Set butIds = new HashSet();
		butIds.addAll(inParTypIds);
		butIds.addAll(criParTypIds);
		butIds.addAll(etaParTypIds);
		butIds.addAll(outParTypIds);
		Set parameterTypes = StorableObjectPool.getStorableObjectsByConditionButIds(butIds, ec, true);
		Set parameterTypeIds = Identifier.createIdentifiers(parameterTypes);
		for (Iterator it = parameterTypeIds.iterator(); it.hasNext();) {
			System.out.println("Loaded: '" + it.next() + "'");
		}

		TypicalCondition tc = new TypicalCondition(ParameterTypeCodenames.REFLECTOGRAMMA,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		inParTypIds = Collections.singleton(((ParameterType) StorableObjectPool.getStorableObjectsByCondition(tc, true).iterator().next()).getId());
		criParTypIds = Collections.EMPTY_SET;
		etaParTypIds = Collections.EMPTY_SET;
		outParTypIds = Collections.EMPTY_SET;
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
