/*-
 * $Id: TestCharacteristicPK7600.java,v 1.1 2005/10/29 20:41:18 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_131;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_16;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_262;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_32;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_320;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_4;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_65;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_PULSWD_1625_8;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_131;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_16;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_262;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_32;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_320;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_4;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_65;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_RES_1625_8;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_TRCLEN;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_WVLEN;
import static com.syrus.AMFICOM.general.CharacteristicTypeCodenames.REF_PK7600_IOR;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename;

/**
 * @version $Revision: 1.1 $, $Date: 2005/10/29 20:41:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestCharacteristicPK7600 extends TestCase {
	private static final String REF_PK7600_WVLEN_VALUE = "1625";

	private static final String REF_PK7600_TRCLEN_VALUE = "4.096 8.192 16.384 32.768 65.535 131.072 262.144 320.000";

	private static final String REF_PK7600_RES_1625_4_VALUE = "0.25 0.50 1.00 2.00 4.00 8.00 16.00";
	private static final String REF_PK7600_RES_1625_8_VALUE = "0.25 0.50 1.00 2.00 4.00 8.00 16.00";
	private static final String REF_PK7600_RES_1625_16_VALUE = "0.25 0.50 1.00 2.00 4.00 8.00 16.00";
	private static final String REF_PK7600_RES_1625_32_VALUE = "0.25 0.50 1.00 2.00 4.00 8.00 16.00";
	private static final String REF_PK7600_RES_1625_65_VALUE = "0.50 1.00 2.00 4.00 8.00 16.00";
	private static final String REF_PK7600_RES_1625_131_VALUE = "1.00 2.00 4.00 8.00 16.00";
	private static final String REF_PK7600_RES_1625_262_VALUE = "2.00 4.00 8.00 16.00";
	private static final String REF_PK7600_RES_1625_320_VALUE = "2.00 4.00 8.00 16.00";

	private static final String REF_PK7600_PULSWD_1625_4_VALUE = "1 5 10 20 50";
	private static final String REF_PK7600_PULSWD_1625_8_VALUE = "1 5 10 20 50 100";
	private static final String REF_PK7600_PULSWD_1625_16_VALUE = "1 5 10 20 50 100";
	private static final String REF_PK7600_PULSWD_1625_32_VALUE = "1 5 10 20 50 100";
	private static final String REF_PK7600_PULSWD_1625_65_VALUE = "1 5 10 20 50 100 500 1000";
	private static final String REF_PK7600_PULSWD_1625_131_VALUE = "1 5 10 20 50 100 500 1000";
	private static final String REF_PK7600_PULSWD_1625_262_VALUE = "1 5 10 20 50 100 500 1000 2000";
	private static final String REF_PK7600_PULSWD_1625_320_VALUE = "1 5 10 20 50 100 500 1000 2000";

	private static final String REF_PK7600_IOR_VALUE = "1.468200";

	public TestCharacteristicPK7600(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestCharacteristicPK7600.class);
		return commonTest.createTestSetup();
	}

	public void testCreateAll() throws ApplicationException {
		final Identifier sysUserId = DatabaseCommonTest.getSysUser().getId();

		final TypicalCondition tc = new TypicalCondition(MeasurementPortTypeCodename.REFLECTOMETRY_PK7600.stringValue(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.MEASUREMENTPORT_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
		final Set<MeasurementPortType> measurementPortTypes = StorableObjectPool.getStorableObjectsByCondition(tc, true);
		assertTrue("Number of measurement port types: " + measurementPortTypes.size(), measurementPortTypes.size() == 1);
		final MeasurementPortType measurementPortType = measurementPortTypes.iterator().next();



		/*	Wave length*/

		final CharacteristicType refWvlenType = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_WVLEN,
				"����� ���� PK7600",
				"����� ���� PK7600",
				DataType.INTEGER,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refWvlen = Characteristic.createInstance(sysUserId,
				refWvlenType,
				"����� ���� PK7600",
				"����� ���� PK7600",
				REF_PK7600_WVLEN_VALUE,
				measurementPortType,
				false,
				false);



		/*	Trace length*/

		final CharacteristicType refTrclenType = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_TRCLEN,
				"����� �������������� PK7600",
				"����� �������������� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refTrclen = Characteristic.createInstance(sysUserId,
				refTrclenType,
				"����� �������������� PK7600",
				"����� �������������� PK7600",
				REF_PK7600_TRCLEN_VALUE,
				measurementPortType,
				false,
				false);



		/*	Resolution*/

		final CharacteristicType refRes1625t4Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_4,
				"���������� 1625�� 4�� PK7600",
				"���������� 1625�� 4�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t4 = Characteristic.createInstance(sysUserId,
				refRes1625t4Type,
				"���������� 1625�� 4�� PK7600",
				"���������� 1625�� 4�� PK7600",
				REF_PK7600_RES_1625_4_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refRes1625t8Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_8,
				"���������� 1625�� 8�� PK7600",
				"���������� 1625�� 8�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t8 = Characteristic.createInstance(sysUserId,
				refRes1625t8Type,
				"���������� 1625�� 8�� PK7600",
				"���������� 1625�� 8�� PK7600",
				REF_PK7600_RES_1625_8_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refRes1625t16Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_16,
				"���������� 1625�� 16�� PK7600",
				"���������� 1625�� 16�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t16 = Characteristic.createInstance(sysUserId,
				refRes1625t16Type,
				"���������� 1625�� 16�� PK7600",
				"���������� 1625�� 16�� PK7600",
				REF_PK7600_RES_1625_16_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refRes1625t32Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_32,
				"���������� 1625�� 32�� PK7600",
				"���������� 1625�� 32�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t32 = Characteristic.createInstance(sysUserId,
				refRes1625t32Type,
				"���������� 1625�� 32�� PK7600",
				"���������� 1625�� 32�� PK7600",
				REF_PK7600_RES_1625_32_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refRes1625t65Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_65,
				"���������� 1625�� 65�� PK7600",
				"���������� 1625�� 65�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t65 = Characteristic.createInstance(sysUserId,
				refRes1625t65Type,
				"���������� 1625�� 65�� PK7600",
				"���������� 1625�� 65�� PK7600",
				REF_PK7600_RES_1625_65_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refRes1625t131Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_131,
				"���������� 1625�� 131�� PK7600",
				"���������� 1625�� 131�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t131 = Characteristic.createInstance(sysUserId,
				refRes1625t131Type,
				"���������� 1625�� 131�� PK7600",
				"���������� 1625�� 131�� PK7600",
				REF_PK7600_RES_1625_131_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refRes1625t262Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_262,
				"���������� 1625�� 262�� PK7600",
				"���������� 1625�� 262�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t262 = Characteristic.createInstance(sysUserId,
				refRes1625t262Type,
				"���������� 1625�� 262�� PK7600",
				"���������� 1625�� 262�� PK7600",
				REF_PK7600_RES_1625_262_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refRes1625t320Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_RES_1625_320,
				"���������� 1625�� 320�� PK7600",
				"���������� 1625�� 320�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refRes1625t320 = Characteristic.createInstance(sysUserId,
				refRes1625t320Type,
				"���������� 1625�� 320�� PK7600",
				"���������� 1625�� 320�� PK7600",
				REF_PK7600_RES_1625_320_VALUE,
				measurementPortType,
				false,
				false);



		/*	Pulse width*/

		final CharacteristicType refPulswd1625t4Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_4,
				"������ �������� 1625�� 4�� PK7600",
				"������ �������� 1625�� 4�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t4 = Characteristic.createInstance(sysUserId,
				refPulswd1625t4Type,
				"������ �������� 1625�� 4�� PK7600",
				"������ �������� 1625�� 4�� PK7600",
				REF_PK7600_PULSWD_1625_4_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswd1625t8Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_8,
				"������ �������� 1625�� 8�� PK7600",
				"������ �������� 1625�� 8�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t8 = Characteristic.createInstance(sysUserId,
				refPulswd1625t8Type,
				"������ �������� 1625�� 8�� PK7600",
				"������ �������� 1625�� 8�� PK7600",
				REF_PK7600_PULSWD_1625_8_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswd1625t16Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_16,
				"������ �������� 1625�� 16�� PK7600",
				"������ �������� 1625�� 16�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t16 = Characteristic.createInstance(sysUserId,
				refPulswd1625t16Type,
				"������ �������� 1625�� 16�� PK7600",
				"������ �������� 1625�� 16�� PK7600",
				REF_PK7600_PULSWD_1625_16_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswd1625t32Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_32,
				"������ �������� 1625�� 32�� PK7600",
				"������ �������� 1625�� 32�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t32 = Characteristic.createInstance(sysUserId,
				refPulswd1625t32Type,
				"������ �������� 1625�� 32�� PK7600",
				"������ �������� 1625�� 32�� PK7600",
				REF_PK7600_PULSWD_1625_32_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswd1625t65Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_65,
				"������ �������� 1625�� 65�� PK7600",
				"������ �������� 1625�� 65�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t65 = Characteristic.createInstance(sysUserId,
				refPulswd1625t65Type,
				"������ �������� 1625�� 65�� PK7600",
				"������ �������� 1625�� 65�� PK7600",
				REF_PK7600_PULSWD_1625_65_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswd1625t131Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_131,
				"������ �������� 1625�� 131�� PK7600",
				"������ �������� 1625�� 131�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t131 = Characteristic.createInstance(sysUserId,
				refPulswd1625t131Type,
				"������ �������� 1625�� 131�� PK7600",
				"������ �������� 1625�� 131�� PK7600",
				REF_PK7600_PULSWD_1625_131_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswd1625t262Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_262,
				"������ �������� 1625�� 262�� PK7600",
				"������ �������� 1625�� 262�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t262 = Characteristic.createInstance(sysUserId,
				refPulswd1625t262Type,
				"������ �������� 1625�� 262�� PK7600",
				"������ �������� 1625�� 262�� PK7600",
				REF_PK7600_PULSWD_1625_262_VALUE,
				measurementPortType,
				false,
				false);

		final CharacteristicType refPulswd1625t320Type = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_PULSWD_1625_320,
				"������ �������� 1625�� 320�� PK7600",
				"������ �������� 1625�� 320�� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refPulswd1625t320 = Characteristic.createInstance(sysUserId,
				refPulswd1625t320Type,
				"������ �������� 1625�� 320�� PK7600",
				"������ �������� 1625�� 320�� PK7600",
				REF_PK7600_PULSWD_1625_320_VALUE,
				measurementPortType,
				false,
				false);



		/*	IOR*/

		final CharacteristicType refIorType = CharacteristicType.createInstance(sysUserId,
				REF_PK7600_IOR,
				"���������� ����������� PK7600",
				"���������� ����������� PK7600",
				DataType.DOUBLE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL);
		final Characteristic refIor = Characteristic.createInstance(sysUserId,
				refIorType,
				"���������� ����������� PK7600",
				"���������� ����������� PK7600",
				REF_PK7600_IOR_VALUE,
				measurementPortType,
				false,
				false);



		/*	Save all*/

		final Set<Identifiable> toFlushObjects = new HashSet<Identifiable>();

		toFlushObjects.add(refWvlen);

		toFlushObjects.add(refTrclen);

		toFlushObjects.add(refRes1625t4);
		toFlushObjects.add(refRes1625t8);
		toFlushObjects.add(refRes1625t16);
		toFlushObjects.add(refRes1625t32);
		toFlushObjects.add(refRes1625t65);
		toFlushObjects.add(refRes1625t131);
		toFlushObjects.add(refRes1625t262);
		toFlushObjects.add(refRes1625t320);

		toFlushObjects.add(refPulswd1625t4);
		toFlushObjects.add(refPulswd1625t8);
		toFlushObjects.add(refPulswd1625t16);
		toFlushObjects.add(refPulswd1625t32);
		toFlushObjects.add(refPulswd1625t65);
		toFlushObjects.add(refPulswd1625t131);
		toFlushObjects.add(refPulswd1625t262);
		toFlushObjects.add(refPulswd1625t320);

		toFlushObjects.add(refIor);

		StorableObjectPool.flush(toFlushObjects, sysUserId, false);
	}
}
