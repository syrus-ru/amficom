/*
 * $Id: TestAnalysisResult.java,v 1.4 2005/12/15 14:52:27 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

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
 * @version $Revision: 1.4 $, $Date: 2005/12/15 14:52:27 $
 * @author $Author: arseniy $
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

	public void testLoadResult() throws ApplicationException, DataFormatException {
		final Identifier analysisId = new Identifier("Analysis_62");
		//Analysis analysis = (Analysis) StorableObjectPool.getStorableObject(analysisId, true);
		final LinkedIdsCondition lic = new LinkedIdsCondition(analysisId, ObjectEntities.RESULT_CODE);
		final Set<Result> results = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		final Result result = results.iterator().next();
		System.out.println("Loaded: " + result.getId());

		final Parameter[] parameters = result.getParameters();
		System.out.println("Result has " + parameters.length + " parameters");
		for (int i = 0; i < parameters.length; i++) {
			final ParameterType parameterType = parameters[i].getType();
			System.out.println("Parameter [" + i + "]: codename " + parameterType.getCodename());
			if (parameterType == ParameterType.DADARA_ALARMS) {
				final ReflectogramMismatch[] reflectogramMismatches = ReflectogramMismatchImpl.alarmsFromByteArray(parameters[i].getValue());
				for (final ReflectogramMismatch reflectogramMismatch : reflectogramMismatches) {
					System.out.println("\t" + "ReflectogramMismatch: " + reflectogramMismatch);
				}
			}
		}
	}
}
