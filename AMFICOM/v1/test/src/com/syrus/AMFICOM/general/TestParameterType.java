/*
 * $Id: TestParameterType.java,v 1.13 2005/07/07 18:15:21 arseniy Exp $ Copyright � 2004 Syrus Systems. ������-����������� �����. ������:
 * �������.
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
 * @version $Revision: 1.13 $, $Date: 2005/07/07 18:15:21 $
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
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_WAVELENGTH.stringValue(),
				"����� �����",
				"����� �����",
				DataType.DATA_TYPE_INTEGER);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_LENGTH.stringValue(),
				"����� ��������������",
				"����� ��������������",
				DataType.DATA_TYPE_DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_RESOLUTION.stringValue(),
				"����������",
				"����������",
				DataType.DATA_TYPE_DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_PULSE_WIDTH_LOW_RES.stringValue(),
				"������ �������� � ������ ������� ����������",
				"������ ��������",
				DataType.DATA_TYPE_INTEGER);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_PULSE_WIDTH_HIGH_RES.stringValue(),
				"������ �������� � ������ �������� ����������",
				"������ ��������",
				DataType.DATA_TYPE_INTEGER);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_INDEX_OF_REFRACTION.stringValue(),
				"���������� �����������",
				"���������� �����������",
				DataType.DATA_TYPE_DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_AVERAGE_COUNT.stringValue(),
				"���������� ����������",
				"���������� ����������",
				DataType.DATA_TYPE_DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_FLAG_GAIN_SPLICE_ON.stringValue(),
				"������ �����",
				"������ �����",
				DataType.DATA_TYPE_BOOLEAN);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_FLAG_LIVE_FIBER_DETECT.stringValue(),
				"������ �����",
				"������ �����",
				DataType.DATA_TYPE_BOOLEAN);

		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.REFLECTOGRAMMA.stringValue(),
				"��������������",
				"��������������",
				DataType.DATA_TYPE_RAW);

		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.REFLECTOGRAMMA_ETALON.stringValue(),
				"��������� ��������������",
				"��������� ��������������",
				DataType.DATA_TYPE_RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_ETALON.stringValue(),
				"������ ��� �������",
				"������ ��� �������",
				DataType.DATA_TYPE_RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_CRITERIA.stringValue(),
				"�������� �������",
				"�������� �������",
				DataType.DATA_TYPE_RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_ANALYSIS_RESULT.stringValue(),
				"��������� �������",
				"��������� �������",
				DataType.DATA_TYPE_RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_ALARMS.stringValue(),
				"����������",
				"����������",
				DataType.DATA_TYPE_RAW);

		StorableObjectPool.flush(ObjectEntities.PARAMETER_TYPE_CODE, true);
	}

}
