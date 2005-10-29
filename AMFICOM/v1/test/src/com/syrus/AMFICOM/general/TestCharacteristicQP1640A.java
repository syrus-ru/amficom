/*-
 * $Id: TestCharacteristicQP1640A.java,v 1.4 2005/10/29 20:44:02 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_WVLEN;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_TRCLEN_1625;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_RES_1625;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_PULSWD_HIGHRES_1625;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_PULSWD_LOWRES_1625;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_IOR;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_SCANS_1625;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_QP1640A_MAX_POINTS;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @version $Revision: 1.4 $, $Date: 2005/10/29 20:44:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestCharacteristicQP1640A extends TestCase {
	private static final String REF_QP1640A_WVLEN_VALUE = "1625";
	private static final String REF_QP1640A_TRCLEN_1625_VALUE = "5.00 20.00 50.00 75.00 125.00 300.00";
	private static final String REF_QP1640A_RES_1625_VALUE = "0.125 0.250 0.500 1.000 2.000 4.000 8.000 16.000";
	private static final String REF_QP1640A_PULSWD_HIGHRES_1625_VALUE = "5 10 20 50 100 200 500 1000 2000 5000";
	private static final String REF_QP1640A_PULSWD_LOWRES_1625_VALUE = "1000 2000 5000 10000 20000";
	private static final String REF_QP1640A_IOR_VALUE = "1.468200";
	private static final String REF_QP1640A_SCANS_1625_VALUE = "4096 32768 262144";
	private static final String REF_QP1640A_MAX_POINTS_VALUE = "262144";

	public TestCharacteristicQP1640A(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestCharacteristicQP1640A.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {
		final Identifier sysUserId = DatabaseCommonTest.getSysUser().getId();

		final TypicalCondition tc = new TypicalCondition(MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.MEASUREMENTPORT_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set<MeasurementPortType> measurementPortTypes = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		assertTrue("Number of measurement port types: " + measurementPortTypes.size(), measurementPortTypes.size() == 1);
		final MeasurementPortType measurementPortType = measurementPortTypes.iterator().next();



		/*	Wave length*/

		final CharacteristicType refWvlenType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_WVLEN,
				"Длины волн QP1640A",
				"Длины волн QP1640A",
				DataType.INTEGER,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refWvlen = Characteristic.createInstance(sysUserId,
				refWvlenType,
				"Длины волн QP1640A",
				"Длины волн QP1640A",
				REF_QP1640A_WVLEN_VALUE,
				measurementPortType,
				false,
				false);



		/*	Trace length*/

		final CharacteristicType refTrclenType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_TRCLEN_1625,
				"Длина рефлектограммы 1625нм QP1640A",
				"Длина рефлектограммы 1625нм QP1640A",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refTrclen = Characteristic.createInstance(sysUserId,
				refTrclenType,
				"Длина рефлектограммы 1625нм QP1640A",
				"Длина рефлектограммы 1625нм QP1640A",
				REF_QP1640A_TRCLEN_1625_VALUE,
				measurementPortType,
				false,
				false);



		/*	Resolution*/

		final CharacteristicType refResType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_RES_1625,
				"Разрешение 1625нм QP1640A",
				"Разрешение 1625нм QP1640A",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes = Characteristic.createInstance(sysUserId,
				refResType,
				"Разрешение 1625нм QP1640A",
				"Разрешение 1625нм QP1640A",
				REF_QP1640A_RES_1625_VALUE,
				measurementPortType,
				false,
				false);



		/*	Pulse width*/

		final CharacteristicType refPulswdHighResType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_PULSWD_HIGHRES_1625,
				"Ширина импульса высокое разрешение 1625нм QP1640A",
				"Ширина импульса высокое разрешение 1625нм QP1640A",
				DataType.LONG,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswdHighRes = Characteristic.createInstance(sysUserId,
				refPulswdHighResType,
				"Ширина импульса высокое разрешение 1625нм QP1640A",
				"Ширина импульса высокое разрешение 1625нм QP1640A",
				REF_QP1640A_PULSWD_HIGHRES_1625_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswdLowResType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_PULSWD_LOWRES_1625,
				"Ширина импульса низкое разрешение 1625нм QP1640A",
				"Ширина импульса низкое разрешение 1625нм QP1640A",
				DataType.LONG,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswdLowRes = Characteristic.createInstance(sysUserId,
				refPulswdLowResType,
				"Ширина импульса низкое разрешение 1625нм QP1640A",
				"Ширина импульса низкое разрешение 1625нм QP1640A",
				REF_QP1640A_PULSWD_LOWRES_1625_VALUE,
				measurementPortType,
				false,
				false);



		/*	IOR*/

		final CharacteristicType refIorType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_IOR,
				"Показатель преломления QP1640A",
				"Показатель преломления QP1640A",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refIor = Characteristic.createInstance(sysUserId,
				refIorType,
				"Показатель преломления QP1640A",
				"Показатель преломления QP1640A",
				REF_QP1640A_IOR_VALUE,
				measurementPortType,
				false,
				false);



		/*	Scans*/

		final CharacteristicType refScansType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_SCANS_1625,
				"Количество усреднений 1625нм QP1640A",
				"Количество усреднений 1625нм QP1640A",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refScans = Characteristic.createInstance(sysUserId,
				refScansType,
				"Количество усреднений 1625нм QP1640A",
				"Количество усреднений 1625нм QP1640A",
				REF_QP1640A_SCANS_1625_VALUE,
				measurementPortType,
				false,
				false);



		/*	Max points*/

		final CharacteristicType refMaxPointsType = CharacteristicType.createInstance(sysUserId,
				REF_QP1640A_MAX_POINTS,
				"Максимальное количество точек QP1640A",
				"Максимальное количество точек QP1640A",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refMaxPoints = Characteristic.createInstance(sysUserId,
				refMaxPointsType,
				"Максимальное количество точек QP1640A",
				"Максимальное количество точек QP1640A",
				REF_QP1640A_MAX_POINTS_VALUE,
				measurementPortType,
				false,
				false);



		/*	Save all*/

		final Set<Identifiable> toFlushObjects = new HashSet<Identifiable>();

		toFlushObjects.add(refWvlen);
		toFlushObjects.add(refTrclen);
		toFlushObjects.add(refRes);
		toFlushObjects.add(refPulswdHighRes);
		toFlushObjects.add(refPulswdLowRes);
		toFlushObjects.add(refIor);
		toFlushObjects.add(refScans);
		toFlushObjects.add(refMaxPoints);

		StorableObjectPool.flush(toFlushObjects, sysUserId, false);
	}
}
