/*-
 * $Id: SetupConstraintCharateristic.java,v 1.1.2.1 2006/04/18 17:33:08 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.CharacteristicTypeSort.OPTICAL;
import static com.syrus.AMFICOM.general.DataType.INTEGER;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;
import static com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A;
import static com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename.REFLECTOMETRY_QP1643A;
import static com.syrus.AMFICOM.reflectometry.ReflectometryCharacteristicTypeCodename.HIGH_RES_MAX_PULSE_WIDTH_NS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryCharacteristicTypeCodename.LOW_RES_MIN_PULSE_WIDTH_NS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryCharacteristicTypeCodename.POINTS_MAX_NUMBER;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.measurement.MeasurementPortType;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/18 17:33:08 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class SetupConstraintCharateristic extends TestCase {
	private static final String RESOURCE_KEY_ROOT = "CharateristicType.Description.";

	private static final int POINTS_MAX_NUMBER_QP1640A = 262144;
	private static final int POINTS_MAX_NUMBER_QP1643A = 262144;
	private static final int POINTS_MAX_NUMBER_PK7600 = 131072;

	private static final int LOW_RES_MIN_PULSE_WIDTH_NS_QP1640A = 1000;
	private static final int LOW_RES_MIN_PULSE_WIDTH_NS_QP1643A = 10000;

	private static final int HIGH_RES_MAX_PULSE_WIDTH_NS_QP1640A = 5000;
	private static final int HIGH_RES_MAX_PULSE_WIDTH_NS_QP1643A = 5000;

	public SetupConstraintCharateristic(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(SetupConstraintCharateristic.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier creatorId = LoginManager.getUserId();


		String characteristicTypeCodename;

		/* Create POINTS_MAX_NUMBER CharacteristicType */
		characteristicTypeCodename = POINTS_MAX_NUMBER.stringValue();
		final CharacteristicType pointsMaxNumberCharacteristicType = CharacteristicType.createInstance(creatorId,
				characteristicTypeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + characteristicTypeCodename),
				I18N.getString(RESOURCE_KEY_ROOT + characteristicTypeCodename),
				INTEGER,
				OPTICAL);

		/* Create LOW_RES_MIN_PULSE_WIDTH CharacteristicType */
		characteristicTypeCodename = LOW_RES_MIN_PULSE_WIDTH_NS.stringValue();
		final CharacteristicType lowResMinPulseWidthCharacteristicType = CharacteristicType.createInstance(creatorId,
				characteristicTypeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + characteristicTypeCodename),
				I18N.getString(RESOURCE_KEY_ROOT + characteristicTypeCodename),
				INTEGER,
				OPTICAL);

		/* Create HIGH_RES_MAX_PULSE_WIDTH CharacteristicType */
		characteristicTypeCodename = HIGH_RES_MAX_PULSE_WIDTH_NS.stringValue();
		final CharacteristicType highResMaxPulseWidthCharacteristicType = CharacteristicType.createInstance(creatorId,
				characteristicTypeCodename,
				I18N.getString(RESOURCE_KEY_ROOT + characteristicTypeCodename),
				I18N.getString(RESOURCE_KEY_ROOT + characteristicTypeCodename),
				INTEGER,
				OPTICAL);


		/* Retrieve MeasurementPortTypes */
		final CompoundCondition measurementPortTypeCondition = new CompoundCondition(new TypicalCondition(REFLECTOMETRY_QP1640A.stringValue(),
					OPERATION_EQUALS,
					MEASUREMENTPORT_TYPE_CODE,
					COLUMN_CODENAME),
				OR,
				new TypicalCondition(REFLECTOMETRY_QP1643A.stringValue(),
					OPERATION_EQUALS,
					MEASUREMENTPORT_TYPE_CODE,
					COLUMN_CODENAME));
		measurementPortTypeCondition.addCondition(new TypicalCondition(REFLECTOMETRY_PK7600.stringValue(),
				OPERATION_EQUALS,
				MEASUREMENTPORT_TYPE_CODE,
				COLUMN_CODENAME));
		final Set<MeasurementPortType> measurementPortTypes = StorableObjectPool.getStorableObjectsByCondition(measurementPortTypeCondition, true);
		assertTrue("Not all measurement port types can be loaded", measurementPortTypes.size() == measurementPortTypeCondition.getConditionsNumber());
		final Map<String, MeasurementPortType> measurementPortTypeCodenamesMap = StorableObjectType.createCodenamesMap(measurementPortTypes);
		final MeasurementPortType qp1640AMeasurementPortType = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_QP1640A.stringValue());
		final MeasurementPortType qp1643AMeasurementPortType = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_QP1643A.stringValue());
		final MeasurementPortType pk7600MeasurementPortType = measurementPortTypeCodenamesMap.get(REFLECTOMETRY_PK7600.stringValue());
		assertTrue("QP1640A not found", qp1640AMeasurementPortType != null);
		assertTrue("QP1643A not found", qp1643AMeasurementPortType != null);
		assertTrue("PK7600 not found", pk7600MeasurementPortType != null);


		final Set<Characteristic> characteristics = new HashSet<Characteristic>();


		/* QP1640A*/

		characteristics.add(Characteristic.createInstance(creatorId,
				pointsMaxNumberCharacteristicType,
				pointsMaxNumberCharacteristicType.getName(),
				pointsMaxNumberCharacteristicType.getDescription(),
				Integer.toString(POINTS_MAX_NUMBER_QP1640A),
				qp1640AMeasurementPortType,
				false,
				false));
		characteristics.add(Characteristic.createInstance(creatorId,
				lowResMinPulseWidthCharacteristicType,
				lowResMinPulseWidthCharacteristicType.getName(),
				lowResMinPulseWidthCharacteristicType.getDescription(),
				Integer.toString(LOW_RES_MIN_PULSE_WIDTH_NS_QP1640A),
				qp1640AMeasurementPortType,
				false,
				false));
		characteristics.add(Characteristic.createInstance(creatorId,
				highResMaxPulseWidthCharacteristicType,
				highResMaxPulseWidthCharacteristicType.getName(),
				highResMaxPulseWidthCharacteristicType.getDescription(),
				Integer.toString(HIGH_RES_MAX_PULSE_WIDTH_NS_QP1640A),
				qp1640AMeasurementPortType,
				false,
				false));


		/* QP1643A*/

		characteristics.add(Characteristic.createInstance(creatorId,
				pointsMaxNumberCharacteristicType,
				pointsMaxNumberCharacteristicType.getName(),
				pointsMaxNumberCharacteristicType.getDescription(),
				Integer.toString(POINTS_MAX_NUMBER_QP1643A),
				qp1643AMeasurementPortType,
				false,
				false));
		characteristics.add(Characteristic.createInstance(creatorId,
				lowResMinPulseWidthCharacteristicType,
				lowResMinPulseWidthCharacteristicType.getName(),
				lowResMinPulseWidthCharacteristicType.getDescription(),
				Integer.toString(LOW_RES_MIN_PULSE_WIDTH_NS_QP1643A),
				qp1643AMeasurementPortType,
				false,
				false));
		characteristics.add(Characteristic.createInstance(creatorId,
				highResMaxPulseWidthCharacteristicType,
				highResMaxPulseWidthCharacteristicType.getName(),
				highResMaxPulseWidthCharacteristicType.getDescription(),
				Integer.toString(HIGH_RES_MAX_PULSE_WIDTH_NS_QP1643A),
				qp1643AMeasurementPortType,
				false,
				false));


		/* PK7600*/

		characteristics.add(Characteristic.createInstance(creatorId,
				pointsMaxNumberCharacteristicType,
				pointsMaxNumberCharacteristicType.getName(),
				pointsMaxNumberCharacteristicType.getDescription(),
				Integer.toString(POINTS_MAX_NUMBER_PK7600),
				pk7600MeasurementPortType,
				false,
				false));


		StorableObjectPool.flush(characteristics, creatorId, false);
	}
}
