/*
 * $Id: TestParameterType.java,v 1.1 2005/02/07 15:23:14 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/07 15:23:14 $
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

//	public void testCreateInstance() throws ApplicationException {
//		Identifier creatorId = new Identifier("Users_58");
//		String codename = ParameterTypeCodenames.ALARM_STATUS;
//		String name = "Alarm status";
//		String description = "Status of alarm";
//		DataType dataType = DataType.DATA_TYPE_INTEGER;
//
//		ParameterType parameterType = ParameterType.createInstance(creatorId, codename, description, name, dataType);
//
//		ParameterType_Transferable ptt = (ParameterType_Transferable) parameterType.getTransferable();
//
//		ParameterType parameterType1 = new ParameterType(ptt);
//		assertEquals(parameterType.getId(), parameterType1.getId());
//		assertEquals(parameterType.getCreated(), parameterType1.getCreated());
//		assertEquals(parameterType.getModified(), parameterType1.getModified());
//		assertEquals(parameterType.getCreatorId(), parameterType1.getCreatorId());
//		assertEquals(parameterType.getModifierId(), parameterType1.getModifierId());
//		assertEquals(parameterType.getCodename(), parameterType1.getCodename());
//		assertEquals(parameterType.getName(), parameterType1.getName());
//		assertEquals(parameterType.getDescription(), parameterType1.getDescription());
//		assertEquals(parameterType.getDataType(), parameterType1.getDataType());
//
//		parameterType.insert();
//	}

	public void testDelete() throws ApplicationException {
		List parameterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
				true);
		GeneralStorableObjectPool.delete(parameterTypes);
	}
}
