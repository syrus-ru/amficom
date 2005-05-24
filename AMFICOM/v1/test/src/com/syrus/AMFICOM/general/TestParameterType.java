/*
 * $Id: TestParameterType.java,v 1.6 2005/05/24 12:25:56 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/24 12:25:56 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class TestParameterType extends CommonTest {

	public TestParameterType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestParameterType.class);
	}

	public void t1estCreateInstance() throws ApplicationException {
		String codename = ParameterTypeCodenames.TRACE_FLAG_GAIN_SPLICE_ON;
		String name = "Gain splice flag";
		String description = "Gain splice on/off";
		DataType dataType = DataType.DATA_TYPE_BOOLEAN;
		ParameterType parameterType = ParameterType.createInstance(creatorUser.getId(), codename, description, name, dataType);

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

		StorableObjectPool.putStorableObject(parameterType);


		codename = ParameterTypeCodenames.TRACE_FLAG_LIVE_FIBER_DETECT;
		name = "Live fiber detect flag";
		description = "Live fiber detect on/off";
		dataType = DataType.DATA_TYPE_BOOLEAN;
		parameterType = ParameterType.createInstance(creatorUser.getId(), codename, description, name, dataType);

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

		StorableObjectPool.putStorableObject(parameterType);


		StorableObjectPool.flush(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, true);
	}

	public void testUpdate() throws ApplicationException {
		TypicalCondition tc = new TypicalCondition("ref_pulswd",
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		Set objects = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		ParameterType parameterType = (ParameterType) objects.iterator().next();
		System.out.println("Parameter type: '" + parameterType.getId() + "'");
		parameterType.setCodename(ParameterTypeCodenames.TRACE_PULSE_WIDTH_HIGH_RES);
		parameterType.setName("Ширина импульса");
		parameterType.setDescription("Ширина импульса в режиме высокого разрешения");

		parameterType = ParameterType.createInstance(creatorUser.getId(),
				ParameterTypeCodenames.TRACE_PULSE_WIDTH_LOW_RES,
				"Ширина импульса в режиме низкого разрешения",
				"Ширина импульса",
				parameterType.getDataType());
		StorableObjectPool.putStorableObject(parameterType);
		StorableObjectPool.flush(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, false);
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
