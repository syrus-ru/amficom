/*
 * $Id: SetTestCase.java,v 1.3 2004/08/19 13:10:33 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.measurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.util.ByteArray;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/19 13:10:33 $
 * @author $Author: bob $
 * @module tools
 */
public class SetTestCase extends AbstractMesurementTestCase {

	public SetTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = SetTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(SetTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException, IOException {
		
		List list = SetDatabase.retrieveAll();
		
		List monitoredElementList = MonitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");
		
		List monitoredElementIds = new ArrayList();
		monitoredElementIds.add(((MonitoredElement)monitoredElementList.get(0)).getId());

		ParameterType wvlenParam = ParameterTypeDatabase.retrieveForCodename("ref_wvlen");
		ParameterType trclenParam = ParameterTypeDatabase.retrieveForCodename("ref_trclen");
		ParameterType resParam = ParameterTypeDatabase.retrieveForCodename("ref_res");
		ParameterType pulswdParam = ParameterTypeDatabase.retrieveForCodename("ref_pulswd");
		ParameterType iorParam = ParameterTypeDatabase.retrieveForCodename("ref_ior");
		ParameterType scansParam = ParameterTypeDatabase.retrieveForCodename("ref_scans");

		SetParameter[] params = new SetParameter[6];

		Identifier paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[0] = new SetParameter(paramId, wvlenParam, new ByteArray((int) 1625).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[1] = new SetParameter(paramId, trclenParam, new ByteArray((double) 131072).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[2] = new SetParameter(paramId, resParam, new ByteArray((double) 8).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[3] = new SetParameter(paramId, pulswdParam, new ByteArray((long) 5000).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[4] = new SetParameter(paramId, iorParam, new ByteArray((double) 1.457).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[5] = new SetParameter(paramId, scansParam, new ByteArray((double) 4000).getBytes());

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.SET_ENTITY_CODE);
		Set set = Set.createInstance(id, creatorId, SetSort.SET_SORT_MEASUREMENT_PARAMETERS, "testCaseSet", params,
										monitoredElementIds);
		
		Set set2 = new Set((Set_Transferable) set.getTransferable());

		assertEquals(set.getId(), set2.getId());

		Set set3 = new Set(set2.getId());

		assertEquals(set2, set3);
		
		System.out.println(set.toString());
		
	}

}