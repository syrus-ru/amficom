/*
 * $Id: TestParameterType.java,v 1.2 2005/02/08 10:53:52 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/08 10:53:52 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class TestParameterType extends CommonGeneralTest {

	public TestParameterType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestParameterType.class);
	}

	public void testCreateInstance() throws ApplicationException {
		Identifier creatorId = new Identifier("Users_58");
		String codename = ParameterTypeCodenames.ALARM_STATUS;
		String name = "Alarm status";
		String description = "Status of alarm";
		DataType dataType = DataType.DATA_TYPE_INTEGER;

		ParameterType parameterType = ParameterType.createInstance(creatorId, codename, description, name, dataType);

		ParameterType_Transferable ptt = (ParameterType_Transferable) parameterType.getTransferable();

		ParameterType parameterType1 = new ParameterType(ptt);
		assertEquals(parameterType.getId(), parameterType1.getId());
		assertEquals(parameterType.getCreated(), parameterType1.getCreated());
		assertEquals(parameterType.getModified(), parameterType1.getModified());
		assertEquals(parameterType.getCreatorId(), parameterType1.getCreatorId());
		assertEquals(parameterType.getModifierId(), parameterType1.getModifierId());
		assertEquals(parameterType.getCodename(), parameterType1.getCodename());
		assertEquals(parameterType.getName(), parameterType1.getName());
		assertEquals(parameterType.getDescription(), parameterType1.getDescription());
		assertEquals(parameterType.getDataType(), parameterType1.getDataType());

		parameterType.insert();




		codename = ParameterTypeCodenames.HZ_CHO;
		name = "hz cho";
		description = "hz cho";
		dataType = DataType.DATA_TYPE_RAW;

		parameterType = ParameterType.createInstance(creatorId, codename, description, name, dataType);

		ptt = (ParameterType_Transferable) parameterType.getTransferable();

		parameterType1 = new ParameterType(ptt);
		assertEquals(parameterType.getId(), parameterType1.getId());
		assertEquals(parameterType.getCreated(), parameterType1.getCreated());
		assertEquals(parameterType.getModified(), parameterType1.getModified());
		assertEquals(parameterType.getCreatorId(), parameterType1.getCreatorId());
		assertEquals(parameterType.getModifierId(), parameterType1.getModifierId());
		assertEquals(parameterType.getCodename(), parameterType1.getCodename());
		assertEquals(parameterType.getName(), parameterType1.getName());
		assertEquals(parameterType.getDescription(), parameterType1.getDescription());
		assertEquals(parameterType.getDataType(), parameterType1.getDataType());

		parameterType.insert();
	}

//	public void testRetrieveByCondition() throws ApplicationException {
//		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodenames.ALARM_STATUS,
//				OperationSort.OPERATION_EQUALS,
//				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
//				StorableObjectWrapper.COLUMN_CODENAME);
//		TypicalCondition tc2 = new TypicalCondition(ParameterTypeCodenames.HZ_CHO,
//				OperationSort.OPERATION_EQUALS,
//				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
//				StorableObjectWrapper.COLUMN_CODENAME);
//		CompoundCondition cc = new CompoundCondition(tc1, CompoundConditionSort.OR, tc2);
//		List parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(cc, true);
//		for (Iterator it = parameterTypes.iterator(); it.hasNext();) {
//			ParameterType parameterType = (ParameterType) it.next();
//			System.out.println("id: '" + parameterType.getId() + "' codename: '" + parameterType.getCodename() + "', description: '" + parameterType.getDescription() + "'");
//		}
//	}

//	public void testDelete() throws ApplicationException {
//		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodenames.ALARM_STATUS,
//				OperationSort.OPERATION_EQUALS,
//				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
//				StorableObjectWrapper.COLUMN_CODENAME);
//		TypicalCondition tc2 = new TypicalCondition(ParameterTypeCodenames.HZ_CHO,
//				OperationSort.OPERATION_EQUALS,
//				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
//				StorableObjectWrapper.COLUMN_CODENAME);
//		CompoundCondition cc = new CompoundCondition(tc1, CompoundConditionSort.OR, tc2);
//		List parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(cc, true);
//		GeneralStorableObjectPool.delete(parameterTypes);
//	}
}
