/*
 * $Id: TestAnalysisResult.java,v 1.3 2005/10/15 17:57:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.3 $, $Date: 2005/10/15 17:57:06 $
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

	public void testLoadAnalysisResult() {
		final Identifier
	}

	public void testLoadResult() throws ApplicationException, DataFormatException {
		Identifier analysisId = new Identifier("Analysis_62");
		//Analysis analysis = (Analysis) StorableObjectPool.getStorableObject(analysisId, true);
		LinkedIdsCondition lic = new LinkedIdsCondition(analysisId, ObjectEntities.RESULT_CODE);
		Set set = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		Result result = (Result) set.iterator().next();
		System.out.println("Loaded: " + result.getId());
		Parameter[] parameters = result.getParameters();
		System.out.println("Result has " + parameters.length + " parameters");
		for (int i = 0; i < parameters.length; i++) {
			String codename = parameters[i].getType().getCodename();
			System.out.println("Parameter [" + i + "]: codename " + codename);
			if (codename.equals(ParameterTypeCodenames.DADARA_ALARMS)) {
				ReflectogramAlarm[] alarms = ReflectogramAlarm.alarmsFromByteArray(parameters[i].getValue());
				System.out.println("\t" + "Number of alarms: " + alarms.length);
				for (int j = 0 ; j < alarms.length; j++) {
					System.out.println("\t\t" + "alarm [" + j + "] == " + alarms[j]);
				}
			}
		}
	}
}
