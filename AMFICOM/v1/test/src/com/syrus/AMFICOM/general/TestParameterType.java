/*
 * $Id: TestParameterType.java,v 1.10 2005/06/15 09:54:13 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/15 09:54:13 $
 * @author $Author: arseniy $
 * @module test
 */
public class TestParameterType extends CommonTest {

	public TestParameterType(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestParameterType.class);
	}

	public void testCreateInstance() throws ApplicationException {
		final String codename = ParameterTypeCodenames.REFLECTOGRAMMA_ETALON;
		final String name = "Reflectogramma-etalon";
		String description = "Raflectogramma, marked as etalon";
		DataType dataType = DataType.DATA_TYPE_RAW;
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

		StorableObjectPool.flush(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, true);
	}

	public void _testUpdate() throws ApplicationException {
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
		StorableObjectPool.flush(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, false);
	}

	public void _testRetrieveByCondition() throws ApplicationException {
		TypicalCondition tc1 = new TypicalCondition(ParameterTypeCodenames.ALARM_STATUS,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		TypicalCondition tc2 = new TypicalCondition(ParameterTypeCodenames.HZ_CHO,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
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
				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		TypicalCondition tc2 = new TypicalCondition(ParameterTypeCodenames.HZ_CHO,
				OperationSort.OPERATION_EQUALS,
				new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE),
				StorableObjectWrapper.COLUMN_CODENAME);
		CompoundCondition cc = new CompoundCondition(tc1, CompoundConditionSort.OR, tc2);
		Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(cc, true);
		StorableObjectPool.delete(parameterTypes);
	}

}
