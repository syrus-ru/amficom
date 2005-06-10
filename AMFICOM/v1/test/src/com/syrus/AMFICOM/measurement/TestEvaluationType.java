/*
 * $Id: TestEvaluationType.java,v 1.2 2005/06/10 15:18:18 arseniy Exp $
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
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/10 15:18:18 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestEvaluationType extends CommonTest {

	public TestEvaluationType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestEvaluationType.class);
	}

	public void testTransferable() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
		EvaluationType evaluationType = (EvaluationType) MeasurementStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Evaluation type: '" + evaluationType.getId() + "'");
		EvaluationType_Transferable att = (EvaluationType_Transferable) evaluationType.getTransferable();
		EvaluationType evaluationType1 = new EvaluationType(att);
		Set inParTypIds = evaluationType1.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();)
			System.out.println("IN: '" + it.next() + "'");
	}

	public void testChangeParameterTypes() throws ApplicationException {
		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
		EvaluationType evaluationType = (EvaluationType) MeasurementStorableObjectPool.getStorableObjectsByCondition(ec, true).iterator().next();
		System.out.println("Evaluation type: '" + evaluationType.getId() + "'");

		Set inParTypIds = evaluationType.getInParameterTypeIds();
		for (Iterator it = inParTypIds.iterator(); it.hasNext();) {
			System.out.println("IN: '" + it.next() + "'");
		}
		Set thParTypIds = evaluationType.getThresholdParameterTypeIds();
		for (Iterator it = thParTypIds.iterator(); it.hasNext();) {
			System.out.println("THS: '" + it.next() + "'");
		}
		Set etaParTypIds = evaluationType.getEtalonParameterTypeIds();
		for (Iterator it = etaParTypIds.iterator(); it.hasNext();) {
			System.out.println("ETA: '" + it.next() + "'");
		}
		Set outParTypIds = evaluationType.getOutParameterTypeIds();
		for (Iterator it = outParTypIds.iterator(); it.hasNext();) {
			System.out.println("OUT: '" + it.next() + "'");
		}
		Set measTypIds = evaluationType.getMeasurementTypeIds();
		for (Iterator it = measTypIds.iterator(); it.hasNext();) {
			System.out.println("MT: '" + it.next() + "'");
		}

		ec = new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
		Set butIds = new HashSet();
		butIds.addAll(inParTypIds);
		butIds.addAll(thParTypIds);
		butIds.addAll(etaParTypIds);
		butIds.addAll(outParTypIds);
		Set parameterTypes = GeneralStorableObjectPool.getStorableObjectsByConditionButIds(butIds, ec, true);
		Set parameterTypeIds = Identifier.createIdentifiers(parameterTypes);
		for (Iterator it = parameterTypeIds.iterator(); it.hasNext();) {
			System.out.println("Loaded: '" + it.next() + "'");
		}

		thParTypIds = Collections.EMPTY_SET;
		etaParTypIds = Collections.EMPTY_SET;
		outParTypIds = Collections.EMPTY_SET;

		evaluationType.setThresholdParameterTypeIds(thParTypIds);
		evaluationType.setEtalonParameterTypeIds(etaParTypIds);
		evaluationType.setOutParameterTypeIds(outParTypIds);

		MeasurementStorableObjectPool.flush(evaluationType.getId(), false);
	}
}
