/*
 * $Id: TestParameterType.java,v 1.14 2005/07/17 05:26:21 arseniy Exp $ Copyright © 2004 Syrus Systems. Научно-технический центр. Проект:
 * АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.14 $, $Date: 2005/07/17 05:26:21 $
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

	public void _testCreateInstance() throws ApplicationException {
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_WAVELENGTH.stringValue(),
				"Длина волны",
				"Длина волны",
				DataType.INTEGER);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_LENGTH.stringValue(),
				"Длина рефлектограммы",
				"Длина рефлектограммы",
				DataType.DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_RESOLUTION.stringValue(),
				"Разрешение",
				"Разрешение",
				DataType.DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_PULSE_WIDTH_LOW_RES.stringValue(),
				"Ширина импульса в режиме низкого разрешения",
				"Ширина импульса",
				DataType.INTEGER);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_PULSE_WIDTH_HIGH_RES.stringValue(),
				"Ширина импульса в режиме высокого разрешения",
				"Ширина импульса",
				DataType.INTEGER);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_INDEX_OF_REFRACTION.stringValue(),
				"Показатель преломления",
				"Показатель преломления",
				DataType.DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_AVERAGE_COUNT.stringValue(),
				"Количество усреднений",
				"Количество усреднений",
				DataType.DOUBLE);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_FLAG_GAIN_SPLICE_ON.stringValue(),
				"Особый режим",
				"Особый режим",
				DataType.BOOLEAN);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.TRACE_FLAG_LIVE_FIBER_DETECT.stringValue(),
				"Особый режим",
				"Особый режим",
				DataType.BOOLEAN);

		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.REFLECTOGRAMMA.stringValue(),
				"Рефлектограмма",
				"Рефлектограмма",
				DataType.RAW);

		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.REFLECTOGRAMMA_ETALON.stringValue(),
				"Эталонная рефлектограмма",
				"Эталонная рефлектограмма",
				DataType.RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_ETALON.stringValue(),
				"Эталон для анализа",
				"Эталон для анализа",
				DataType.RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_CRITERIA.stringValue(),
				"Критерий анализа",
				"Критерий анализа",
				DataType.RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_ANALYSIS_RESULT.stringValue(),
				"Результат анализа",
				"Результат анализа",
				DataType.RAW);
		ParameterType.createInstance(DatabaseCommonTest.getSysUser().getId(),
				ParameterTypeCodename.DADARA_ALARMS.stringValue(),
				"Отклонения",
				"Отклонения",
				DataType.RAW);

		StorableObjectPool.flush(ObjectEntities.PARAMETER_TYPE_CODE, true);
	}

	public void testGetCharacteristics() throws ApplicationException {
		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.PARAMETER_TYPE_CODE);
		final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(ec, true);

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! CHARACTERISTICS:");
		final EquivalentCondition ec1 = new EquivalentCondition(ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjectsByCondition(ec1, false);
		for (final Characteristic characteristic : characteristics0) {
			System.out.println("Characteristic: '" + characteristic.getName() + "': '" + characteristic.getValue() + "'");
		}

		for (final ParameterType parameterType : parameterTypes) {
			System.out.println("###################### Parameter type: " + parameterType.getDescription());
			final LinkedIdsCondition lic = new LinkedIdsCondition(parameterType.getId(), ObjectEntities.CHARACTERISTIC_CODE);
			final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
			for (final Characteristic characteristic : characteristics) {
				System.out.println("################################### Characteristic: '" + characteristic.getName() + "': '" + characteristic.getValue() + "'");
			}
		}
	}
}
