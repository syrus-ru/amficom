/*
 * $Id: SetTestCase.java,v 1.8 2005/02/04 14:21:34 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.AbstractMesurementTestCase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeCodenames;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.util.ByteArray;

/**
 * @version $Revision: 1.8 $, $Date: 2005/02/04 14:21:34 $
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

	public void _testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException, IOException {

		ParameterTypeDatabase parameterTypeDatabase = (ParameterTypeDatabase) MeasurementDatabaseContext
				.getParameterTypeDatabase();

		MonitoredElementDatabase monitoredElementDatabase = (MonitoredElementDatabase) ConfigurationDatabaseContext
				.getMonitoredElementDatabase();

		SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();

		List list = setDatabase.retrieveAll();

		List monitoredElementList = monitoredElementDatabase.retrieveAll();

		if (monitoredElementList.isEmpty())
			fail("must be at less one monitored element at db");

		List monitoredElementIds = Collections.singletonList(((MonitoredElement) monitoredElementList.get(0)).getId());

		ParameterType wvlenParam = parameterTypeDatabase.retrieveForCodename("ref_wvlen");
		ParameterType trclenParam = parameterTypeDatabase.retrieveForCodename("ref_trclen");
		ParameterType resParam = parameterTypeDatabase.retrieveForCodename("ref_res");
		ParameterType pulswdParam = parameterTypeDatabase.retrieveForCodename("ref_pulswd");
		ParameterType iorParam = parameterTypeDatabase.retrieveForCodename("ref_ior");
		ParameterType scansParam = parameterTypeDatabase.retrieveForCodename("ref_scans");

		SetParameter[] params = new SetParameter[6];

		Identifier paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[0] = new SetParameter(paramId, wvlenParam, new ByteArray((double) 1625).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		//params[1] = new SetParameter(paramId, trclenParam, new
		// ByteArray((double) 131072).getBytes());
		params[1] = new SetParameter(paramId, trclenParam, new ByteArray((double) 125000).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[2] = new SetParameter(paramId, resParam, new ByteArray((double) 8).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[3] = new SetParameter(paramId, pulswdParam, new ByteArray((long) 5000).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[4] = new SetParameter(paramId, iorParam, new ByteArray((double) 1.46820).getBytes());

		paramId = IdentifierGenerator.generateIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE);
		params[5] = new SetParameter(paramId, scansParam, new ByteArray((long) 4096).getBytes());

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.SET_ENTITY_CODE);
		Set set = Set.createInstance(id, creatorId, SetSort.SET_SORT_MEASUREMENT_PARAMETERS, "testCaseSet", params,
										monitoredElementIds);

		Set set2 = Set.getInstance((Set_Transferable) set.getTransferable());

		assertEquals(set.getId(), set2.getId());

		Set set3 = new Set(set2.getId());

		assertEquals(set2, set3);
		System.out.println(set.toString());

	}

	public void testRetriveAll() throws RetrieveObjectException, IllegalDataException, IOException {
		SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
		List list = setDatabase.retrieveButIds(null);
		for (Iterator it = list.iterator(); it.hasNext();) {
			Set set = (Set) it.next();
			SetParameter[] parameters = set.getParameters();
			for (int i = 0; i < parameters.length; i++) {
				SetParameter param = parameters[i];
				if (param.getType().getCodename().equals(ParameterTypeCodenames.TRACE_AVERAGE_COUNT)) {
					ByteArray byteArray = new ByteArray(param.getValue());
					long l = byteArray.toLong();
					System.out.println(set.getId().toSQLString() + "\t " + ParameterTypeCodenames.TRACE_AVERAGE_COUNT
							+ " = " + l);
				}
			}
		}
	}

}