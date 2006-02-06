/*
 * $Id: TestAnalysisResult.java,v 1.5 2006/01/18 15:06:51 saa Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.io.DataFormatException;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.io.DataFormatException;

/**
 * @version $Revision: 1.5 $, $Date: 2006/01/18 15:06:51 $
 * @author $Author: saa $
 * @module test
 */
public final class TestAnalysisResult extends TestCase {

	public TestAnalysisResult(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestAnalysisResult.class);
		return commonTest.createTestSetup();
	}

//	public void testLoadAnalysisResult() {
//		final Identifier
//	}

	public void testLoadResult() throws ApplicationException, DataFormatException {
		long t0 = System.nanoTime();
		Identifier analysisId = new Identifier("Analysis_63");
		//Analysis analysis = (Analysis) StorableObjectPool.getStorableObject(analysisId, true);
		LinkedIdsCondition lic =
			new LinkedIdsCondition(analysisId, ObjectEntities.RESULT_CODE);
		long t1 = System.nanoTime();
		Set set = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		long t2 = System.nanoTime();
		Result result = (Result) set.iterator().next();
		long t3 = System.nanoTime();
		System.out.println("Loaded: " + result.getId());

		final Parameter[] parameters = result.getParameters();
		System.out.println("Result has " + parameters.length + " parameters");
		for (int i = 0; i < parameters.length; i++) {
			final ParameterType parameterType = parameters[i].getType();
			String codename = parameterType.getCodename();
			System.out.println("Parameter [" + i + "]: codename " + codename);
			if (parameterType.equals(ParameterType.DADARA_ALARMS)) {
				ReflectogramMismatchImpl[] alarms =
					ReflectogramMismatchImpl.alarmsFromByteArray(parameters[i].getValue());
				System.out.println("\t" + "Number of alarms: " + alarms.length);
				for (int j = 0 ; j < alarms.length; j++) {
					System.out.println("\t\t" + "alarm [" + j + "] == " + alarms[j]);
				}
			}
		}
		System.out.println("id & lic: " + (t1 - t0) / 1e6 + " ms");
		System.out.println("getSOByC: " + (t2 - t1) / 1e6 + " ms");
		System.out.println("iterator: " + (t3 - t2) / 1e6 + " ms");
	}
}
