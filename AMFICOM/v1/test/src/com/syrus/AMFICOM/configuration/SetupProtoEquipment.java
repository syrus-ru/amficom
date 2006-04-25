/*-
 * $Id: SetupProtoEquipment.java,v 1.2 2006/04/25 10:14:46 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;
import static com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename.REFLECTOMETRY_QP1640A;
import static com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename.REFLECTOMETRY_QP1643A;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.reflectometry.MeasurementPortTypeCodename;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/25 10:14:46 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class SetupProtoEquipment extends TestCase {

	public static enum ProtoEquipmentName {
		QP1640A("QP1640A", REFLECTOMETRY_QP1640A),
		QP1643A("QP1643A", REFLECTOMETRY_QP1643A),
		QP1640MR("QP1640MR", REFLECTOMETRY_PK7600);

		private String name;
		private MeasurementPortTypeCodename measurementPortTypeCodename;

		private static final Map<String, ProtoEquipmentName> nameMap;

		static {
			nameMap = new HashMap<String, ProtoEquipmentName>();
			for (final ProtoEquipmentName protoEquipmentName : values()) {
				nameMap.put(protoEquipmentName.stringValue(), protoEquipmentName);
			}
		}

		private ProtoEquipmentName(final String name, MeasurementPortTypeCodename measurementPortTypeCodename) {
			this.name = name;
			this.measurementPortTypeCodename = measurementPortTypeCodename;
		}

		public String stringValue() {
			return this.name;
		}

		public MeasurementPortTypeCodename getMeasurementPortTypeCodename() {
			return this.measurementPortTypeCodename;
		}

		@Override
		public String toString() {
			return this.name() + "(" + Integer.toString(this.ordinal()) + ", " + this.stringValue() + ")";
		}

		public static ProtoEquipmentName valueOfStr(final String nameStr) {
			return nameMap.get(nameStr);
		}
	}

	public SetupProtoEquipment(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(SetupProtoEquipment.class);
		return commonTest.createTestSetup();
	}

	public void testCreate() throws ApplicationException {
		final Identifier userId = LoginManager.getUserId();

		final TypicalCondition equipmentTypeCondition = new TypicalCondition(EquipmentTypeCodename.REFLECTOMETER.stringValue(),
				OPERATION_EQUALS,
				EQUIPMENT_TYPE_CODE,
				COLUMN_CODENAME);
		final Set<EquipmentType> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(equipmentTypeCondition, true);
		if (equipmentTypes.isEmpty()) {
			throw new ObjectNotFoundException("Cannot find EquipmentTypes");
		}
		assert equipmentTypes.size() == 1 : ONLY_ONE_EXPECTED;
		final EquipmentType equipmentType = equipmentTypes.iterator().next();

		final Set<ProtoEquipment> protoEquipments = new HashSet<ProtoEquipment>();
		for (final ProtoEquipmentName protoEquipmentName : ProtoEquipmentName.values()) {
			final String name = protoEquipmentName.stringValue();
			protoEquipments.add(ProtoEquipment.createInstance(userId,
					equipmentType.getId(),
					name,
					name,
					"Кооператив Нэт-тэст",
					"15A30"));
		}
		StorableObjectPool.flush(protoEquipments, userId, false);
	}
}
