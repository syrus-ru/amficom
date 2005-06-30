/*
 * $Id: TestParameterType.java,v 1.12 2005/06/30 07:54:03 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/30 07:54:03 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestParameterType extends TestCase {

	public TestParameterType(String name) {
		super(name);
	}

	public static Test suite() {
		DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestParameterType.class);
		return commonTest.createTestSetup();
	}

	public void testCreateInstance() throws ApplicationException {
		final String codename = ParameterTypeCodenames.REFLECTOGRAMMA_ETALON;
		final String name = "Reflectogramma-etalon";
		String description = "Raflectogramma, marked as etalon";
		DataType dataType = DataType.DATA_TYPE_RAW;
		ParameterType parameterType = ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				codename,
				description,
				name,
				dataType);

		StorableObjectPool.flush(ObjectEntities.PARAMETER_TYPE_CODE, true);
	}

	public void _testRetrieveByCondition() throws ApplicationException {
		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodenames.ALARM_STATUS,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETER_TYPE_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		TypicalCondition tc2 = new TypicalCondition(ParameterTypeCodenames.HZ_CHO,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETER_TYPE_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		CompoundCondition cc = new CompoundCondition(tc1, CompoundConditionSort.OR, tc2);
		Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		for (Iterator it = parameterTypes.iterator(); it.hasNext();) {
			ParameterType parameterType = (ParameterType) it.next();
			System.out.println("id: '" + parameterType.getId() + "' codename: '" + parameterType.getCodename() + "', description: '" + parameterType.getDescription() + "'");
		}
	}

	public void _testDelete() throws ApplicationException {
		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodenames.ALARM_STATUS,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETER_TYPE_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		TypicalCondition tc2 = new TypicalCondition(ParameterTypeCodenames.HZ_CHO,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETER_TYPE_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		CompoundCondition cc = new CompoundCondition(tc1, CompoundConditionSort.OR, tc2);
		Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		StorableObjectPool.delete(parameterTypes);
	}

}
